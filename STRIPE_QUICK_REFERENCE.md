# Stripe Payment Integration - Quick Reference

## Environment Variables Required

Add these to Railway/Heroku:

```
STRIPE_SECRET_KEY=sk_test_51...
STRIPE_WEBHOOK_SECRET=whsec_...
```

## Available Endpoints

### 1. Create Payment Intent
**URL**: `POST /api/v1/stripe/create-payment-intent?orderId={orderId}`  
**Auth**: Required (JWT)  
**Use**: Direct card payment with Stripe Elements

**Example**:
```bash
curl -X POST "https://linceecom-production.up.railway.app/api/v1/stripe/create-payment-intent?orderId=1" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Response**:
```json
{
  "clientSecret": "pi_3Abc123_secret_xyz",
  "paymentIntentId": "pi_3Abc123",
  "amount": 129.99,
  "currency": "usd"
}
```

### 2. Create Checkout Session
**URL**: `POST /api/v1/stripe/create-checkout-session?orderId={orderId}`  
**Auth**: Required (JWT)  
**Use**: Redirect to Stripe-hosted payment page

**Example**:
```bash
curl -X POST "https://linceecom-production.up.railway.app/api/v1/stripe/create-checkout-session?orderId=1" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Response**:
```json
{
  "sessionId": "cs_test_abc123",
  "url": "https://checkout.stripe.com/c/pay/cs_test_abc123",
  "amount": 129.99
}
```

### 3. Get Payment Intent
**URL**: `GET /api/v1/stripe/payment-intent/{paymentIntentId}`  
**Auth**: Not required (public)

**Example**:
```bash
curl "https://linceecom-production.up.railway.app/api/v1/stripe/payment-intent/pi_3Abc123"
```

### 4. Get Session Details
**URL**: `GET /api/v1/stripe/session/{sessionId}`  
**Auth**: Not required (public)

**Example**:
```bash
curl "https://linceecom-production.up.railway.app/api/v1/stripe/session/cs_test_abc123"
```

### 5. Webhook
**URL**: `POST /api/v1/stripe/webhook`  
**Auth**: Not required (Stripe signature verification)  
**Use**: Receives events from Stripe servers

**Setup**:
1. Go to Stripe Dashboard → Developers → Webhooks
2. Add endpoint: `https://linceecom-production.up.railway.app/api/v1/stripe/webhook`
3. Select events:
   - `payment_intent.succeeded`
   - `payment_intent.payment_failed`
   - `checkout.session.completed`
4. Copy webhook secret to environment variables

## Frontend Integration

### Option 1: Payment Intent (Custom UI)

```javascript
// 1. Create payment intent
const response = await fetch('/api/v1/stripe/create-payment-intent?orderId=1', {
  method: 'POST',
  headers: { 'Authorization': 'Bearer ' + token }
});
const { clientSecret } = await response.json();

// 2. Use Stripe.js to confirm payment
const stripe = Stripe('pk_test_your_publishable_key');
const { error } = await stripe.confirmCardPayment(clientSecret, {
  payment_method: { card: cardElement }
});
```

### Option 2: Checkout Session (Redirect)

```javascript
// 1. Create checkout session
const response = await fetch('/api/v1/stripe/create-checkout-session?orderId=1', {
  method: 'POST',
  headers: { 'Authorization': 'Bearer ' + token }
});
const { url } = await response.json();

// 2. Redirect to Stripe
window.location.href = url;
```

## Test Cards

- **Success**: 4242 4242 4242 4242
- **Authentication**: 4000 0025 0000 3155
- **Declined**: 4000 0000 0000 9995

Use any future expiry date and any 3-digit CVC.

## Payment Flow

1. User places order → Order created with status PENDING
2. Frontend calls `/create-payment-intent` or `/create-checkout-session`
3. Payment record created with status PENDING
4. User completes payment with Stripe
5. Stripe sends webhook to `/webhook`
6. Backend updates:
   - Payment status → COMPLETED
   - Order status → CONFIRMED
   - Sets `paidAt` timestamp

## Database Updates

When payment succeeds (via webhook):
- `payments.status` → COMPLETED
- `payments.paid_at` → current timestamp
- `orders.status` → CONFIRMED
- `payments.transaction_id` → Stripe Payment Intent/Session ID

## Security Notes

✅ **Webhook signature verified** using `stripe.webhook.secret`  
✅ **HTTPS required** for webhooks in production  
✅ **Secret key protected** via environment variables  
✅ **Public endpoints** only expose safe data  

## Next Steps

1. Get Stripe API keys from https://dashboard.stripe.com/test/apikeys
2. Add environment variables to Railway
3. Set up webhook endpoint in Stripe Dashboard
4. Test with test cards
5. Switch to live keys when ready for production

## Documentation

- Full integration guide: [STRIPE_INTEGRATION.md](./STRIPE_INTEGRATION.md)
- Stripe Docs: https://stripe.com/docs
- API Reference: https://stripe.com/docs/api

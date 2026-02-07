# Stripe Payment Gateway Integration

## Overview
This document explains how to integrate and use Stripe payment gateway in the Lincee Backend application.

## Configuration

### 1. Environment Variables
Add the following environment variables to your deployment platform (Railway, Heroku, etc.):

```properties
STRIPE_SECRET_KEY=sk_test_your_secret_key_here
STRIPE_WEBHOOK_SECRET=whsec_your_webhook_secret_here
```

For local development, you can add them to `application.properties`:

```properties
# Stripe Configuration
stripe.api.key=${STRIPE_SECRET_KEY:sk_test_51...your_key...}
stripe.webhook.secret=${STRIPE_WEBHOOK_SECRET:whsec_...your_secret...}
stripe.currency=usd
stripe.success.url=https://your-frontend.vercel.app/payment/success
stripe.cancel.url=https://your-frontend.vercel.app/payment/cancel
```

### 2. Get Stripe API Keys

1. **Sign up/Login to Stripe**: Go to https://dashboard.stripe.com/
2. **Get Test Keys**: 
   - Navigate to Developers > API keys
   - Copy the "Secret key" (starts with `sk_test_`)
3. **Get Webhook Secret**:
   - Navigate to Developers > Webhooks
   - Click "Add endpoint"
   - Enter your endpoint URL: `https://your-api.com/api/v1/stripe/webhook`
   - Select events to listen for:
     - `payment_intent.succeeded`
     - `payment_intent.payment_failed`
     - `checkout.session.completed`
   - After creating, copy the "Signing secret" (starts with `whsec_`)

## API Endpoints

### 1. Create Payment Intent (Direct Card Processing)

**Endpoint**: `POST /api/v1/stripe/create-payment-intent`

**Description**: Creates a Stripe Payment Intent for processing direct card payments. Returns a client secret that can be used with Stripe.js on the frontend.

**Request**:
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

**Usage**: Use the `clientSecret` on the frontend with Stripe Elements to collect card details and confirm payment.

---

### 2. Create Checkout Session (Hosted Payment Page)

**Endpoint**: `POST /api/v1/stripe/create-checkout-session`

**Description**: Creates a Stripe Checkout Session that redirects users to a Stripe-hosted payment page. Easier to implement but less customizable.

**Request**:
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

**Usage**: Redirect the user to the `url` provided in the response.

---

### 3. Get Payment Intent Details

**Endpoint**: `GET /api/v1/stripe/payment-intent/{paymentIntentId}`

**Description**: Retrieves the status and details of a Payment Intent.

**Request**:
```bash
curl "https://linceecom-production.up.railway.app/api/v1/stripe/payment-intent/pi_3Abc123"
```

**Response**:
```json
{
  "id": "pi_3Abc123",
  "status": "succeeded",
  "amount": 129.99,
  "currency": "usd",
  "clientSecret": "pi_3Abc123_secret_xyz"
}
```

---

### 4. Get Checkout Session Details

**Endpoint**: `GET /api/v1/stripe/session/{sessionId}`

**Description**: Retrieves the status and details of a Checkout Session.

**Request**:
```bash
curl "https://linceecom-production.up.railway.app/api/v1/stripe/session/cs_test_abc123"
```

**Response**:
```json
{
  "id": "cs_test_abc123",
  "paymentStatus": "paid",
  "amountTotal": 129.99,
  "currency": "usd",
  "customerEmail": "customer@example.com"
}
```

---

### 5. Webhook Handler

**Endpoint**: `POST /api/v1/stripe/webhook`

**Description**: Receives events from Stripe when payment status changes. This endpoint is called by Stripe's servers, not your frontend.

**Events Handled**:
- `payment_intent.succeeded` - Payment completed successfully
- `payment_intent.payment_failed` - Payment failed
- `checkout.session.completed` - Checkout session completed

**Webhook Configuration**:
1. Go to Stripe Dashboard > Developers > Webhooks
2. Add endpoint: `https://linceecom-production.up.railway.app/api/v1/stripe/webhook`
3. Select events: `payment_intent.succeeded`, `payment_intent.payment_failed`, `checkout.session.completed`
4. Copy the webhook signing secret and add to environment variables

## Payment Flow

### Option 1: Payment Intent (Custom UI)

1. **Create Order**: User completes checkout on your frontend
2. **Create Payment Intent**: 
   ```javascript
   const response = await fetch('/api/v1/stripe/create-payment-intent?orderId=1', {
     method: 'POST',
     headers: { 'Authorization': 'Bearer ' + token }
   });
   const { clientSecret } = await response.json();
   ```
3. **Collect Card Details**: Use Stripe Elements to collect card information
4. **Confirm Payment**:
   ```javascript
   const { error } = await stripe.confirmCardPayment(clientSecret, {
     payment_method: { card: cardElement }
   });
   ```
5. **Webhook Notification**: Stripe sends event to `/api/v1/stripe/webhook`
6. **Update Order**: Backend updates order status to CONFIRMED

### Option 2: Checkout Session (Hosted UI)

1. **Create Order**: User completes checkout on your frontend
2. **Create Checkout Session**:
   ```javascript
   const response = await fetch('/api/v1/stripe/create-checkout-session?orderId=1', {
     method: 'POST',
     headers: { 'Authorization': 'Bearer ' + token }
   });
   const { url } = await response.json();
   ```
3. **Redirect to Stripe**: `window.location.href = url;`
4. **User Pays**: User enters payment details on Stripe's page
5. **Redirect Back**: Stripe redirects to your success/cancel URL
6. **Webhook Notification**: Stripe sends event to `/api/v1/stripe/webhook`
7. **Update Order**: Backend updates order status to CONFIRMED

## Database Changes

The `Payment` entity is automatically linked to Stripe:
- `paymentGateway`: Set to "STRIPE"
- `transactionId`: Stores the Payment Intent ID or Session ID
- `referenceNumber`: Stores the Payment Intent ID (from session)
- `status`: Updated via webhook (PENDING → COMPLETED/FAILED)
- `paidAt`: Set when payment succeeds

## Testing

### Test Card Numbers
Use these test cards in Stripe test mode:
- **Success**: `4242 4242 4242 4242`
- **Authentication Required**: `4000 0025 0000 3155`
- **Declined**: `4000 0000 0000 9995`

### Test Webhooks Locally
1. Install Stripe CLI: `brew install stripe/stripe-cli/stripe`
2. Login: `stripe login`
3. Forward events: `stripe listen --forward-to localhost:8080/api/v1/stripe/webhook`
4. Use the webhook signing secret provided by CLI

## Security Notes

1. **Never expose Secret Key**: Keep `STRIPE_SECRET_KEY` on the server only
2. **Webhook Signature Verification**: Always verify webhook signatures using `stripe.webhook.secret`
3. **HTTPS Required**: Webhooks require HTTPS in production
4. **Client Secret**: The `clientSecret` from Payment Intent can be used on frontend safely

## Frontend Integration Examples

### React with Stripe Elements
```javascript
import { loadStripe } from '@stripe/stripe-js';
import { Elements, CardElement, useStripe, useElements } from '@stripe/react-stripe-js';

const stripePromise = loadStripe('pk_test_your_publishable_key');

function CheckoutForm({ orderId }) {
  const stripe = useStripe();
  const elements = useElements();

  const handleSubmit = async (event) => {
    event.preventDefault();
    
    // Create Payment Intent
    const response = await fetch(`/api/v1/stripe/create-payment-intent?orderId=${orderId}`, {
      method: 'POST',
      headers: { 'Authorization': 'Bearer ' + token }
    });
    const { clientSecret } = await response.json();
    
    // Confirm payment
    const result = await stripe.confirmCardPayment(clientSecret, {
      payment_method: { card: elements.getElement(CardElement) }
    });
    
    if (result.error) {
      console.error(result.error.message);
    } else {
      console.log('Payment successful!');
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <CardElement />
      <button type="submit">Pay Now</button>
    </form>
  );
}

// Wrap in Elements provider
function App() {
  return (
    <Elements stripe={stripePromise}>
      <CheckoutForm orderId={1} />
    </Elements>
  );
}
```

### Simple Checkout Session Redirect
```javascript
async function handleCheckout(orderId) {
  const response = await fetch(`/api/v1/stripe/create-checkout-session?orderId=${orderId}`, {
    method: 'POST',
    headers: { 'Authorization': 'Bearer ' + token }
  });
  const { url } = await response.json();
  window.location.href = url;
}
```

## Support

For Stripe-specific questions:
- Documentation: https://stripe.com/docs
- API Reference: https://stripe.com/docs/api
- Support: https://support.stripe.com/

For application-specific issues:
- Check logs for webhook events
- Verify environment variables are set
- Ensure webhook endpoint is publicly accessible
- Test with Stripe CLI for local development

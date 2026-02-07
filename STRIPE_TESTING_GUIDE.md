# Stripe API Testing - cURL Commands

## Prerequisites

1. **Get JWT Token** (if needed for authenticated endpoints):
```bash
# Login to get token
curl -X POST "https://linceecom-production.up.railway.app/api/v1/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "customer@example.com",
    "password": "password123"
  }'

# Save the token from response
export JWT_TOKEN="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

2. **Create an Order** (if you need an orderId):
```bash
# This assumes you have products and cart items
curl -X POST "https://linceecom-production.up.railway.app/api/v1/orders" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -d '{
    "addressId": 1,
    "shippingMethod": "STANDARD",
    "paymentMethod": "CREDIT_CARD"
  }'

# Note the orderId from response
export ORDER_ID=1
```

## Test Stripe Endpoints

### 1. Create Payment Intent

```bash
curl -X POST "https://linceecom-production.up.railway.app/api/v1/stripe/create-payment-intent?orderId=$ORDER_ID" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  | jq .
```

**Expected Response**:
```json
{
  "clientSecret": "pi_3ABC123_secret_xyz",
  "paymentIntentId": "pi_3ABC123",
  "amount": 129.99,
  "currency": "usd"
}
```

**What to do with the response**:
- Save the `clientSecret` to use in your frontend
- Use Stripe.js to confirm payment on frontend
- Or test manually in Stripe Dashboard

---

### 2. Create Checkout Session

```bash
curl -X POST "https://linceecom-production.up.railway.app/api/v1/stripe/create-checkout-session?orderId=$ORDER_ID" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  | jq .
```

**Expected Response**:
```json
{
  "sessionId": "cs_test_abc123xyz",
  "url": "https://checkout.stripe.com/c/pay/cs_test_abc123xyz",
  "amount": 129.99
}
```

**What to do with the response**:
- Copy the `url` and open it in a browser
- Complete payment using test card: 4242 4242 4242 4242
- After payment, Stripe will send webhook to your backend

---

### 3. Get Payment Intent Details

```bash
# Replace with actual payment intent ID from step 1
export PAYMENT_INTENT_ID="pi_3ABC123"

curl "https://linceecom-production.up.railway.app/api/v1/stripe/payment-intent/$PAYMENT_INTENT_ID" \
  | jq .
```

**Expected Response**:
```json
{
  "id": "pi_3ABC123",
  "status": "succeeded",
  "amount": 129.99,
  "currency": "usd",
  "clientSecret": "pi_3ABC123_secret_xyz"
}
```

**Possible statuses**:
- `requires_payment_method` - Waiting for payment
- `requires_confirmation` - Ready to confirm
- `processing` - Payment in progress
- `succeeded` - Payment completed
- `canceled` - Payment canceled

---

### 4. Get Checkout Session Details

```bash
# Replace with actual session ID from step 2
export SESSION_ID="cs_test_abc123xyz"

curl "https://linceecom-production.up.railway.app/api/v1/stripe/session/$SESSION_ID" \
  | jq .
```

**Expected Response**:
```json
{
  "id": "cs_test_abc123xyz",
  "paymentStatus": "paid",
  "amountTotal": 129.99,
  "currency": "usd",
  "customerEmail": "customer@example.com"
}
```

**Possible payment statuses**:
- `unpaid` - Not paid yet
- `paid` - Payment successful
- `no_payment_required` - Free order

---

### 5. Test Webhook (Local Development)

**Note**: Webhooks are called by Stripe's servers, not by you. But you can test locally with Stripe CLI:

```bash
# Install Stripe CLI
brew install stripe/stripe-cli/stripe

# Login to Stripe
stripe login

# Forward webhooks to your local server
stripe listen --forward-to localhost:8080/api/v1/stripe/webhook

# In another terminal, trigger test webhook
stripe trigger payment_intent.succeeded
```

**What happens**:
- Stripe CLI sends a test webhook to your local server
- Your backend processes the webhook
- Payment status updates to COMPLETED
- Order status updates to CONFIRMED

---

## Complete Test Flow

### Option 1: Payment Intent Flow

```bash
# 1. Login
TOKEN=$(curl -X POST "https://linceecom-production.up.railway.app/api/v1/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"email":"customer@example.com","password":"password123"}' \
  | jq -r '.token')

# 2. Create Payment Intent
PI_SECRET=$(curl -X POST "https://linceecom-production.up.railway.app/api/v1/stripe/create-payment-intent?orderId=1" \
  -H "Authorization: Bearer $TOKEN" \
  | jq -r '.clientSecret')

echo "Client Secret: $PI_SECRET"
echo "Use this in your frontend with Stripe.js"

# 3. Check status (after payment on frontend)
PI_ID=$(echo $PI_SECRET | cut -d'_' -f1-3)
curl "https://linceecom-production.up.railway.app/api/v1/stripe/payment-intent/$PI_ID" | jq .
```

### Option 2: Checkout Session Flow

```bash
# 1. Login
TOKEN=$(curl -X POST "https://linceecom-production.up.railway.app/api/v1/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"email":"customer@example.com","password":"password123"}' \
  | jq -r '.token')

# 2. Create Checkout Session
CHECKOUT_URL=$(curl -X POST "https://linceecom-production.up.railway.app/api/v1/stripe/create-checkout-session?orderId=1" \
  -H "Authorization: Bearer $TOKEN" \
  | jq -r '.url')

echo "Open this URL in browser: $CHECKOUT_URL"
echo "Complete payment with card: 4242 4242 4242 4242"

# 3. After payment, check order status
curl "https://linceecom-production.up.railway.app/api/v1/orders/1" \
  -H "Authorization: Bearer $TOKEN" \
  | jq '.status'
```

---

## Verify Database Updates

After successful payment, check that database was updated:

```bash
# Get order details
curl "https://linceecom-production.up.railway.app/api/v1/orders/$ORDER_ID" \
  -H "Authorization: Bearer $TOKEN" \
  | jq '{ orderId: .id, status: .status, totalAmount: .totalAmount }'

# Get payment details
curl "https://linceecom-production.up.railway.app/api/v1/payments/order/$ORDER_ID" \
  -H "Authorization: Bearer $TOKEN" \
  | jq '{ paymentId: .id, status: .status, transactionId: .transactionId, paidAt: .paidAt }'
```

**Expected after successful payment**:
- Order status: `CONFIRMED`
- Payment status: `COMPLETED`
- Payment `paidAt` field: timestamp
- Payment `transactionId`: Stripe Payment Intent or Session ID

---

## Test Cards

### Success Cards
```bash
# Standard success
Card: 4242 4242 4242 4242
Expiry: Any future date
CVC: Any 3 digits
ZIP: Any 5 digits
```

### Authentication Required
```bash
# Requires 3D Secure
Card: 4000 0025 0000 3155
Expiry: Any future date
CVC: Any 3 digits
```

### Decline Cards
```bash
# Generic decline
Card: 4000 0000 0000 9995

# Insufficient funds
Card: 4000 0000 0000 9995

# Card expired
Card: 4000 0000 0000 0069
```

---

## Troubleshooting

### Error: "Order not found"
```bash
# List your orders first
curl "https://linceecom-production.up.railway.app/api/v1/orders/my-orders" \
  -H "Authorization: Bearer $TOKEN" \
  | jq '.[].id'

# Use one of the returned IDs
```

### Error: "Unauthorized"
```bash
# Your token expired, login again
TOKEN=$(curl -X POST "https://linceecom-production.up.railway.app/api/v1/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"email":"customer@example.com","password":"password123"}' \
  | jq -r '.token')
```

### Error: "Invalid API key"
```bash
# Check Railway environment variables
# Ensure STRIPE_SECRET_KEY is set correctly
# Should start with sk_test_ for test mode
```

### Webhook not received
```bash
# Check webhook configuration in Stripe Dashboard
# Verify URL: https://linceecom-production.up.railway.app/api/v1/stripe/webhook
# Check selected events: payment_intent.succeeded, etc.
# Test webhook delivery in Stripe Dashboard
```

---

## Monitoring

### Check Railway Logs
```bash
# SSH into Railway or check logs in dashboard
# Look for:
# - "Payment succeeded for order: ORD-..."
# - "Checkout session completed for order: ORD-..."
# - Any Stripe-related errors
```

### Check Stripe Dashboard
1. Go to https://dashboard.stripe.com/test/payments
2. View recent payments
3. Check webhook delivery logs
4. Test webhook endpoint manually

---

## Production Testing

When using live keys (after going live):

```bash
# Use real card numbers
# Do NOT use test cards in production

# For testing in production:
# - Use small amounts (e.g., $0.50)
# - Immediately refund test payments
# - Monitor Stripe Dashboard for issues
```

---

## Quick Debug Commands

```bash
# Check if Stripe dependency loaded
curl "https://linceecom-production.up.railway.app/api/v1/health" | jq .

# Test CORS
curl -I -X OPTIONS "https://linceecom-production.up.railway.app/api/v1/stripe/create-payment-intent"

# Check if webhook endpoint is accessible
curl -X POST "https://linceecom-production.up.railway.app/api/v1/stripe/webhook" \
  -H "Content-Type: application/json" \
  -d '{"test": "ping"}'
# Should return "Invalid signature" (expected)
```

---

**Note**: All commands assume you're using the Railway production URL. For local testing, replace with `http://localhost:8080`.

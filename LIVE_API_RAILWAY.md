
## 🌐 Live Production URLs

**Base API URL:**
```
https://linceecom-production.up.railway.app/api/v1
```

**Swagger UI (Interactive API Documentation):**
```
https://linceecom-production.up.railway.app/swagger-ui.html
```

**OpenAPI Specification:**
```
https://linceecom-production.up.railway.app/v3/api-docs
```

**Health Check:**
```
https://linceecom-production.up.railway.app/health
```

---

## 🚀 Quick Test Commands

### 1. Health Check
```bash
curl https://linceecom-production.up.railway.app/health
```

### 2. Get All Products
```bash
curl https://linceecom-production.up.railway.app/api/v1/products
```

### 3. Login
```bash
curl -X POST https://linceecom-production.up.railway.app/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin@lincee.com",
    "password": "admin123"
  }'
```

### 4. Register New User
```bash
curl -X POST https://linceecom-production.up.railway.app/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "email": "john@example.com",
    "password": "SecurePass123",
    "firstName": "John",
    "lastName": "Doe"
  }'
```

### 5. Get Featured Products
```bash
curl https://linceecom-production.up.railway.app/api/v1/products/featured
```

### 6. Search Products
```bash
curl "https://linceecom-production.up.railway.app/api/v1/products/search?keyword=shirt"
```

### 7. Dashboard Overview (Requires Auth)
```bash
# First login and get token, then:
curl https://linceecom-production.up.railway.app/api/v1/dashboard/overview \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

---

## 📋 All API Endpoints (92 Total)

### Authentication (`/api/v1/auth`)
- `POST /auth/login` - Login
- `POST /auth/register` - Register
- `POST /auth/logout` - Logout
- `POST /auth/refresh` - Refresh token

### Products (`/api/v1/products`)
- `GET /products` - Get all products
- `GET /products/{id}` - Get product by ID
- `POST /products` - Create product
- `PUT /products/{id}` - Update product
- `DELETE /products/{id}` - Delete product
- `GET /products/category/{category}` - By category
- `GET /products/brand/{brand}` - By brand
- `GET /products/featured` - Featured products
- `GET /products/search?keyword=X` - Search products
- `PATCH /products/{id}/stock?quantity=X` - Update stock

### Users (`/api/v1/users`)
- `GET /users` - Get all users
- `GET /users/{id}` - Get user by ID
- `POST /users` - Create user
- `PUT /users/{id}` - Update user
- `DELETE /users/{id}` - Delete user
- `GET /users/search?email=X` - Search by email

### Shopping Cart (`/api/v1/cart`)
- `GET /cart/user/{userId}` - Get/create cart
- `GET /cart/{cartId}` - Get cart by ID
- `POST /cart/user/{userId}/items?productId=X&quantity=Y` - Add item
- `PUT /cart/items/{cartItemId}?userId=X&quantity=Y` - Update quantity
- `DELETE /cart/user/{userId}/items/{cartItemId}` - Remove item
- `GET /cart/user/{userId}/items` - Get cart items
- `DELETE /cart/user/{userId}/clear` - Clear cart

### Orders (`/api/v1/orders`)
- `POST /orders?userId=X` - Create order
- `GET /orders/{id}` - Get by ID
- `GET /orders/number/{orderNumber}` - Get by order number
- `GET /orders/user/{userId}` - User orders (paginated)
- `GET /orders/user/{userId}/list` - User orders (all)
- `GET /orders/status/{status}` - By status
- `PUT /orders/{id}` - Update order
- `PATCH /orders/{id}/status?status=X` - Update status
- `DELETE /orders/{id}` - Delete order
- `GET /orders/stats/count` - Total count
- `GET /orders/stats/status/{status}/count` - Count by status

### Addresses (`/api/v1/addresses`)
- `POST /addresses?userId=X` - Add address
- `GET /addresses/{id}` - Get by ID
- `GET /addresses/user/{userId}` - User addresses
- `GET /addresses/user/{userId}/shipping` - Shipping addresses
- `GET /addresses/user/{userId}/billing` - Billing addresses
- `GET /addresses/user/{userId}/default` - Default address
- `PUT /addresses/{id}` - Update address
- `DELETE /addresses/{id}` - Delete address

### Payments (`/api/v1/payments`)
- `POST /payments?orderId=X` - Create payment
- `GET /payments/{id}` - Get by ID
- `GET /payments/order/{orderId}` - By order ID
- `GET /payments/transaction/{transactionId}` - By transaction ID
- `GET /payments/status/{status}` - By status
- `GET /payments/user/{userId}` - User payments
- `PATCH /payments/{id}/status?status=X` - Update status
- `PATCH /payments/{id}/complete?transactionId=X` - Complete payment
- `PUT /payments/{id}` - Update payment
- `DELETE /payments/{id}` - Delete payment
- `GET /payments/stats/count` - Statistics

### Reviews (`/api/v1/reviews`)
- `POST /reviews?productId=X&userId=Y` - Add review
- `GET /reviews/{id}` - Get by ID
- `GET /reviews/product/{productId}` - Product reviews (paginated)
- `GET /reviews/product/{productId}/list` - Product reviews (all)
- `GET /reviews/user/{userId}` - User reviews
- `PUT /reviews/{id}` - Update review
- `DELETE /reviews/{id}` - Delete review
- `PATCH /reviews/{id}/helpful` - Mark helpful
- `GET /reviews/product/{productId}/stats` - Rating stats

### Dashboard (`/api/v1/dashboard`)
- `GET /dashboard/overview` - Complete overview
- `GET /dashboard/orders/statistics` - Order stats
- `GET /dashboard/payments/statistics` - Payment stats
- `GET /dashboard/products/statistics` - Product stats
- `GET /dashboard/users/statistics` - User stats
- `GET /dashboard/cart/statistics` - Cart stats
- `GET /dashboard/reviews/statistics` - Review stats
- `GET /dashboard/summary` - Quick summary
- `GET /dashboard/health` - Dashboard health

### Images (`/api/v1/images`)
- `POST /images/upload` - Upload image

### Health (`/health`)
- `GET /health` - Application health

---

## 🔐 Authentication Flow

### Step 1: Login
```bash
curl -X POST https://linceecom-production.up.railway.app/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "user@example.com",
    "password": "password123"
  }'
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "username": "user@example.com",
  "message": "Login successful"
}
```

### Step 2: Use Token for Authenticated Requests
```bash
curl https://linceecom-production.up.railway.app/api/v1/orders/user/1 \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

---

## 📱 Complete E-Commerce Workflow

### 1️⃣ **Browse Products**
```bash
# Get all products
curl https://linceecom-production.up.railway.app/api/v1/products

# Search for T-shirts
curl "https://linceecom-production.up.railway.app/api/v1/products/search?keyword=tshirt"

# Get featured products
curl https://linceecom-production.up.railway.app/api/v1/products/featured
```

### 2️⃣ **User Registration & Login**
```bash
# Register
curl -X POST https://linceecom-production.up.railway.app/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john",
    "email": "john@example.com",
    "password": "secure123",
    "firstName": "John",
    "lastName": "Doe"
  }'

# Login
curl -X POST https://linceecom-production.up.railway.app/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "john@example.com", "password": "secure123"}'
```

### 3️⃣ **Add Items to Cart**
```bash
# Get or create cart
curl https://linceecom-production.up.railway.app/api/v1/cart/user/1 \
  -H "Authorization: Bearer YOUR_TOKEN"

# Add product to cart
curl -X POST "https://linceecom-production.up.railway.app/api/v1/cart/user/1/items?productId=10&quantity=2&size=M&color=Black" \
  -H "Authorization: Bearer YOUR_TOKEN"

# View cart items
curl https://linceecom-production.up.railway.app/api/v1/cart/user/1/items \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### 4️⃣ **Add Shipping Address**
```bash
curl -X POST "https://linceecom-production.up.railway.app/api/v1/addresses?userId=1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "addressLine1": "123 Main Street",
    "city": "New York",
    "state": "NY",
    "zipCode": "10001",
    "country": "USA",
    "phoneNumber": "555-0123",
    "isDefault": true,
    "isShippingAddress": true
  }'
```

### 5️⃣ **Create Order**
```bash
curl -X POST "https://linceecom-production.up.railway.app/api/v1/orders?userId=1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "totalAmount": 159.99,
    "shippingCost": 10.00,
    "taxAmount": 12.00,
    "shippingAddressId": 1,
    "billingAddressId": 1
  }'
```

### 6️⃣ **Process Payment**
```bash
curl -X POST "https://linceecom-production.up.railway.app/api/v1/payments?orderId=1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "paymentMethod": "CREDIT_CARD",
    "amount": 181.99,
    "paymentGateway": "STRIPE",
    "cardLastFour": "4242"
  }'
```

### 7️⃣ **Track Order**
```bash
# Get order by ID
curl https://linceecom-production.up.railway.app/api/v1/orders/1 \
  -H "Authorization: Bearer YOUR_TOKEN"

# Get order by order number
curl https://linceecom-production.up.railway.app/api/v1/orders/number/ORD-1234567890 \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### 8️⃣ **Leave Review**
```bash
curl -X POST "https://linceecom-production.up.railway.app/api/v1/reviews?productId=10&userId=1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "rating": 5,
    "title": "Excellent product!",
    "comment": "Great quality and fast shipping. Highly recommend!",
    "verifiedPurchase": true
  }'
```

---

## 🎯 Admin Dashboard Access

```bash
# Get complete dashboard overview
curl https://linceecom-production.up.railway.app/api/v1/dashboard/overview \
  -H "Authorization: Bearer ADMIN_TOKEN"

# Get order statistics
curl https://linceecom-production.up.railway.app/api/v1/dashboard/orders/statistics \
  -H "Authorization: Bearer ADMIN_TOKEN"

# Get payment statistics
curl https://linceecom-production.up.railway.app/api/v1/dashboard/payments/statistics \
  -H "Authorization: Bearer ADMIN_TOKEN"
```

---

## 🔗 Postman Collection

Import the OpenAPI spec into Postman:
```
https://linceecom-production.up.railway.app/v3/api-docs
```

**Steps:**
1. Open Postman
2. Click "Import"
3. Select "Link"
4. Paste: `https://linceecom-production.up.railway.app/v3/api-docs`
5. Click "Import"

---

## 📊 Response Codes

- `200 OK` - Success (GET, PUT, PATCH)
- `201 Created` - Resource created (POST)
- `204 No Content` - Success with no response body (DELETE)
- `400 Bad Request` - Invalid request data
- `401 Unauthorized` - Missing or invalid token
- `404 Not Found` - Resource not found
- `409 Conflict` - Resource already exists
- `500 Internal Server Error` - Server error

---

## 🌐 CORS Configuration

Your Railway app supports CORS from:
- `http://localhost:3000` (Development)
- `http://localhost:4200` (Development)
- `http://linceecom-production.up.railway.app` (Production)

Allowed methods: `GET`, `POST`, `PUT`, `DELETE`, `OPTIONS`, `PATCH`

---

## 💡 Quick Links

- **🌐 Live API:** https://linceecom-production.up.railway.app/api/v1
- **📚 Swagger UI:** https://linceecom-production.up.railway.app/swagger-ui.html
- **💚 Health Check:** https://linceecom-production.up.railway.app/health
- **📄 OpenAPI Spec:** https://linceecom-production.up.railway.app/v3/api-docs

---

## 🔧 Testing Script

Save this as `test-api.sh`:

```bash
#!/bin/bash

BASE_URL="https://linceecom-production.up.railway.app/api/v1"

echo "=========================================="
echo "Testing Lincee API on Railway"
echo "=========================================="
echo ""

# Test 1: Health Check
echo "1. Testing Health Check..."
curl -s $BASE_URL/../health | jq '.'
echo ""

# Test 2: Get Products
echo "2. Getting Products..."
curl -s $BASE_URL/products | jq '.[0:2]'
echo ""

# Test 3: Search Products
echo "3. Searching Products..."
curl -s "$BASE_URL/products/search?keyword=shirt" | jq '.[0:2]'
echo ""

# Test 4: Get Featured Products
echo "4. Getting Featured Products..."
curl -s $BASE_URL/products/featured | jq '.[0:2]'
echo ""

echo "=========================================="
echo "All tests completed!"
echo "=========================================="
```

Run with:
```bash
chmod +x test-api.sh
./test-api.sh
```

---

**Production URL:** https://linceecom-production.up.railway.app  
**Total Endpoints:** 92  
**Status:** ✅ Live and Ready  
**Last Updated:** February 3, 2026

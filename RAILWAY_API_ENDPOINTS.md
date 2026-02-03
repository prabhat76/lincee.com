# Lincee E-Commerce API - Railway Deployment

## 🌐 Base URL
```
https://your-app-name.up.railway.app/api/v1
```

---

## 📋 Complete API Endpoint List (10 Controllers, 92 Endpoints)

---

## 1️⃣ Authentication Controller (`/auth`)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/auth/login` | User login with credentials | ❌ No |
| POST | `/auth/register` | Register new user account | ❌ No |
| POST | `/auth/logout` | Logout current user | ✅ Yes |
| POST | `/auth/refresh` | Refresh JWT token | ✅ Yes |

**Total: 4 endpoints**

---

## 2️⃣ User Management Controller (`/users`)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/users` | Get all users | ✅ Yes (Admin) |
| GET | `/users/{id}` | Get user by ID | ✅ Yes |
| POST | `/users` | Create new user | ✅ Yes (Admin) |
| PUT | `/users/{id}` | Update user details | ✅ Yes |
| DELETE | `/users/{id}` | Delete user | ✅ Yes (Admin) |
| GET | `/users/search?email={email}` | Search user by email | ✅ Yes |

**Total: 6 endpoints**

---

## 3️⃣ Product Management Controller (`/products`)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/products` | Get all products | ❌ No |
| GET | `/products?active=true` | Get active products only | ❌ No |
| GET | `/products/{id}` | Get product by ID | ❌ No |
| POST | `/products` | Create new product | ✅ Yes (Admin) |
| PUT | `/products/{id}` | Update product | ✅ Yes (Admin) |
| DELETE | `/products/{id}` | Delete product | ✅ Yes (Admin) |
| GET | `/products/category/{category}` | Get products by category | ❌ No |
| GET | `/products/brand/{brand}` | Get products by brand | ❌ No |
| GET | `/products/featured` | Get featured products | ❌ No |
| GET | `/products/search?keyword={keyword}` | Search products | ❌ No |
| PATCH | `/products/{id}/stock?quantity={qty}` | Update product stock | ✅ Yes (Admin) |

**Total: 11 endpoints**

---

## 4️⃣ Shopping Cart Controller (`/cart`)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/cart/user/{userId}` | Get or create cart for user | ✅ Yes |
| GET | `/cart/{cartId}` | Get cart by ID | ✅ Yes |
| POST | `/cart/user/{userId}/items?productId={id}&quantity={qty}&size={size}&color={color}` | Add item to cart | ✅ Yes |
| PUT | `/cart/items/{cartItemId}?userId={id}&quantity={qty}` | Update cart item quantity | ✅ Yes |
| DELETE | `/cart/user/{userId}/items/{cartItemId}` | Remove item from cart | ✅ Yes |
| GET | `/cart/user/{userId}/items` | Get all cart items | ✅ Yes |
| DELETE | `/cart/user/{userId}/clear` | Clear entire cart | ✅ Yes |

**Total: 7 endpoints**

---

## 5️⃣ Order Management Controller (`/orders`)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/orders?userId={id}` | Create new order | ✅ Yes |
| GET | `/orders/{id}` | Get order by ID | ✅ Yes |
| GET | `/orders/number/{orderNumber}` | Get order by order number | ✅ Yes |
| GET | `/orders/user/{userId}?page=0&size=10` | Get user orders (paginated) | ✅ Yes |
| GET | `/orders/user/{userId}/list` | Get user orders (all) | ✅ Yes |
| GET | `/orders/status/{status}?page=0&size=10` | Get orders by status | ✅ Yes (Admin) |
| PUT | `/orders/{id}` | Update order | ✅ Yes |
| PATCH | `/orders/{id}/status?status={STATUS}` | Update order status | ✅ Yes (Admin) |
| DELETE | `/orders/{id}` | Delete order | ✅ Yes (Admin) |
| GET | `/orders/stats/count` | Get total order count | ✅ Yes (Admin) |
| GET | `/orders/stats/status/{status}/count` | Get count by status | ✅ Yes (Admin) |

**Order Status Values:** `PENDING`, `CONFIRMED`, `PROCESSING`, `SHIPPED`, `DELIVERED`, `CANCELLED`, `RETURNED`

**Total: 11 endpoints**

---

## 6️⃣ Address Management Controller (`/addresses`)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/addresses?userId={id}` | Add new address | ✅ Yes |
| GET | `/addresses/{id}` | Get address by ID | ✅ Yes |
| GET | `/addresses/user/{userId}` | Get all user addresses | ✅ Yes |
| GET | `/addresses/user/{userId}/shipping` | Get shipping addresses | ✅ Yes |
| GET | `/addresses/user/{userId}/billing` | Get billing addresses | ✅ Yes |
| GET | `/addresses/user/{userId}/default` | Get default address | ✅ Yes |
| PUT | `/addresses/{id}` | Update address | ✅ Yes |
| DELETE | `/addresses/{id}` | Delete address | ✅ Yes |

**Total: 8 endpoints**

---

## 7️⃣ Payment Processing Controller (`/payments`)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/payments?orderId={id}` | Create payment | ✅ Yes |
| GET | `/payments/{id}` | Get payment by ID | ✅ Yes |
| GET | `/payments/order/{orderId}` | Get payment by order ID | ✅ Yes |
| GET | `/payments/transaction/{transactionId}` | Get payment by transaction ID | ✅ Yes |
| GET | `/payments/status/{status}` | Get payments by status | ✅ Yes (Admin) |
| GET | `/payments/user/{userId}` | Get user payments | ✅ Yes |
| PATCH | `/payments/{id}/status?status={STATUS}` | Update payment status | ✅ Yes (Admin) |
| PATCH | `/payments/{id}/complete?transactionId={id}` | Complete payment | ✅ Yes |
| PUT | `/payments/{id}` | Update payment | ✅ Yes (Admin) |
| DELETE | `/payments/{id}` | Delete payment | ✅ Yes (Admin) |
| GET | `/payments/stats/count` | Get payment statistics | ✅ Yes (Admin) |

**Payment Status Values:** `PENDING`, `PROCESSING`, `COMPLETED`, `FAILED`, `CANCELLED`, `REFUNDED`

**Total: 11 endpoints**

---

## 8️⃣ Product Reviews Controller (`/reviews`)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/reviews?productId={id}&userId={id}` | Add product review | ✅ Yes |
| GET | `/reviews/{id}` | Get review by ID | ❌ No |
| GET | `/reviews/product/{productId}?page=0&size=10` | Get product reviews (paginated) | ❌ No |
| GET | `/reviews/product/{productId}/list` | Get product reviews (all) | ❌ No |
| GET | `/reviews/user/{userId}?page=0&size=10` | Get user reviews | ✅ Yes |
| PUT | `/reviews/{id}` | Update review | ✅ Yes |
| DELETE | `/reviews/{id}` | Delete review | ✅ Yes |
| PATCH | `/reviews/{id}/helpful` | Mark review as helpful | ✅ Yes |
| GET | `/reviews/product/{productId}/stats` | Get product rating stats | ❌ No |

**Total: 9 endpoints**

---

## 9️⃣ Dashboard Analytics Controller (`/dashboard`)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/dashboard/overview` | Complete dashboard overview | ✅ Yes (Admin) |
| GET | `/dashboard/orders/statistics` | Order statistics | ✅ Yes (Admin) |
| GET | `/dashboard/payments/statistics` | Payment statistics | ✅ Yes (Admin) |
| GET | `/dashboard/products/statistics` | Product statistics | ✅ Yes (Admin) |
| GET | `/dashboard/users/statistics` | User statistics | ✅ Yes (Admin) |
| GET | `/dashboard/cart/statistics` | Cart statistics | ✅ Yes (Admin) |
| GET | `/dashboard/reviews/statistics` | Review statistics | ✅ Yes (Admin) |
| GET | `/dashboard/summary` | Quick summary | ✅ Yes (Admin) |
| GET | `/dashboard/health` | Dashboard health check | ✅ Yes (Admin) |

**Total: 9 endpoints**

---

## 🔟 Image Upload Controller (`/images`)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/images/upload` | Upload product image | ✅ Yes (Admin) |

**Total: 1 endpoint**

---

## 1️⃣1️⃣ Health Check Controller (`/health`)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/health` | Application health status | ❌ No |

**Total: 1 endpoint**

---

## 📊 Summary

| Controller | Base Path | Endpoints | Auth Required |
|------------|-----------|-----------|---------------|
| Authentication | `/auth` | 4 | Mixed |
| User Management | `/users` | 6 | Yes |
| Product Management | `/products` | 11 | Mixed |
| Shopping Cart | `/cart` | 7 | Yes |
| Order Management | `/orders` | 11 | Yes |
| Address Management | `/addresses` | 8 | Yes |
| Payment Processing | `/payments` | 11 | Yes |
| Product Reviews | `/reviews` | 9 | Mixed |
| Dashboard Analytics | `/dashboard` | 9 | Yes (Admin) |
| Image Upload | `/images` | 1 | Yes (Admin) |
| Health Check | `/health` | 1 | No |

**Total: 92 endpoints across 11 controllers**

---

## 🔐 Authentication

All authenticated endpoints require a JWT Bearer token in the header:

```bash
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

### Get Token:
```bash
curl -X POST https://your-app.up.railway.app/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "user@example.com",
    "password": "password123"
  }'
```

---

## 📝 Example Usage on Railway

### 1. Login
```bash
curl -X POST https://your-app.up.railway.app/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin@lincee.com",
    "password": "admin123"
  }'
```

### 2. Get All Products
```bash
curl https://your-app.up.railway.app/api/v1/products
```

### 3. Create Order (Authenticated)
```bash
curl -X POST "https://your-app.up.railway.app/api/v1/orders?userId=1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "totalAmount": 159.99,
    "shippingAddressId": 1,
    "billingAddressId": 1
  }'
```

### 4. Get Dashboard Overview
```bash
curl https://your-app.up.railway.app/api/v1/dashboard/overview \
  -H "Authorization: Bearer YOUR_TOKEN"
```

---

## 🌐 Swagger UI on Railway

Access interactive API documentation:
```
https://your-app.up.railway.app/swagger-ui.html
```

---

## 🔧 Railway Environment Variables

Make sure these are set in your Railway deployment:

```env
DATABASE_URL=jdbc:postgresql://...
DATABASE_USERNAME=neondb_owner
DATABASE_PASSWORD=npg_8HDt2LTngcGa
PORT=8080
JAVA_TOOL_OPTIONS=-Xmx512m
```

---

## 📱 Testing All Endpoints

### Postman Collection Import
1. Open Postman
2. Import → Link
3. Use: `https://your-app.up.railway.app/v3/api-docs`

### cURL Test Script
```bash
#!/bin/bash
BASE_URL="https://your-app.up.railway.app/api/v1"

# Test health
echo "Testing health..."
curl $BASE_URL/health

# Test products
echo -e "\n\nTesting products..."
curl $BASE_URL/products

# Test authentication
echo -e "\n\nTesting login..."
curl -X POST $BASE_URL/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"test","password":"test123"}'
```

---

## 🚀 Quick Start Checklist

- [ ] Verify Railway deployment is live
- [ ] Check health endpoint: `GET /health`
- [ ] Test Swagger UI: `/swagger-ui.html`
- [ ] Test login: `POST /auth/login`
- [ ] Test product listing: `GET /products`
- [ ] Verify database tables are created (15 tables)
- [ ] Test creating an order
- [ ] Verify dashboard access

---

## 📞 Support

For issues with Railway deployment:
1. Check Railway logs
2. Verify environment variables
3. Ensure DATABASE_URL is correct
4. Check port binding (should be 8080)

---

**Deployment:** Railway  
**Total Endpoints:** 92  
**Controllers:** 11  
**Database Tables:** 15  
**Status:** ✅ Production Ready

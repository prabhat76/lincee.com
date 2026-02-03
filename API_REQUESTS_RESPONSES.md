# Lincee E-commerce API - Complete Request & Response Guide

Base URL: `https://linceecom-production.up.railway.app`

## 🔐 Authentication APIs

### 1. Register User
**POST** `/api/v1/auth/register`

**Request:**
```json
{
  "username": "johndoe",
  "email": "john@example.com",
  "password": "password123",
  "firstName": "John",
  "lastName": "Doe",
  "phone": "1234567890"
}
```

**Response (200):**
```json
{
  "message": "User registered successfully",
  "userId": 1,
  "username": "johndoe",
  "email": "john@example.com",
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer"
}
```

**Error (409 - Email exists):**
```json
{
  "message": "Email already registered"
}
```

---

### 2. Login
**POST** `/api/v1/auth/login`

**Request:**
```json
{
  "email": "john@example.com",
  "password": "password123"
}
```

**Response (200):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "email": "john@example.com",
  "userId": 1,
  "username": "johndoe",
  "message": "Login successful"
}
```

**Error (401):**
```json
{
  "message": "Invalid credentials"
}
```

---

### 3. Logout
**POST** `/api/v1/auth/logout`

**Headers:** `Authorization: Bearer <token>`

**Response (200):**
```json
{
  "message": "Logout successful"
}
```

---

### 4. Refresh Token
**POST** `/api/v1/auth/refresh`

**Request:**
```json
{
  "refreshToken": "old-token-here"
}
```

**Response (200):**
```json
{
  "token": "new-jwt-token-here",
  "type": "Bearer",
  "message": "Token refreshed successfully"
}
```

---

## 👤 User Management APIs

### 5. Get All Users
**GET** `/api/v1/users`

**Headers:** `Authorization: Bearer <token>`

**Response (200):**
```json
[
  {
    "id": 1,
    "username": "johndoe",
    "email": "john@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "phoneNumber": "1234567890",
    "role": "CUSTOMER",
    "active": true,
    "createdAt": "2026-02-03T10:30:00",
    "updatedAt": "2026-02-03T10:30:00"
  }
]
```

---

### 6. Get User by ID
**GET** `/api/v1/users/{id}`

**Headers:** `Authorization: Bearer <token>`

**Response (200):**
```json
{
  "id": 1,
  "username": "johndoe",
  "email": "john@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "phoneNumber": "1234567890",
  "role": "CUSTOMER",
  "active": true,
  "createdAt": "2026-02-03T10:30:00",
  "updatedAt": "2026-02-03T10:30:00"
}
```

---

### 7. Create User
**POST** `/api/v1/users`

**Headers:** `Authorization: Bearer <token>`

**Request:**
```json
{
  "username": "janedoe",
  "email": "jane@example.com",
  "password": "securepass",
  "firstName": "Jane",
  "lastName": "Doe",
  "phoneNumber": "9876543210",
  "role": "CUSTOMER"
}
```

**Response (200):**
```json
{
  "id": 2,
  "username": "janedoe",
  "email": "jane@example.com",
  "message": "User created successfully"
}
```

---

### 8. Update User
**PUT** `/api/v1/users/{id}`

**Headers:** `Authorization: Bearer <token>`

**Request:**
```json
{
  "firstName": "Jane",
  "lastName": "Smith",
  "phoneNumber": "9998887777"
}
```

**Response (200):**
```json
{
  "id": 2,
  "username": "janedoe",
  "email": "jane@example.com",
  "firstName": "Jane",
  "lastName": "Smith",
  "phoneNumber": "9998887777",
  "message": "User updated successfully"
}
```

---

### 9. Delete User
**DELETE** `/api/v1/users/{id}`

**Headers:** `Authorization: Bearer <token>`

**Response (200):**
```json
{
  "message": "User deleted successfully"
}
```

---

### 10. Search Users
**GET** `/api/v1/users/search?query=john`

**Headers:** `Authorization: Bearer <token>`

**Response (200):**
```json
[
  {
    "id": 1,
    "username": "johndoe",
    "email": "john@example.com",
    "firstName": "John",
    "lastName": "Doe"
  }
]
```

---

## 🛍️ Product APIs

### 11. Get All Products
**GET** `/api/v1/products`

**Response (200):**
```json
[
  {
    "id": 1,
    "name": "Classic Streetwear Hoodie",
    "description": "Premium quality cotton hoodie",
    "price": 79.99,
    "category": "HOODIES",
    "brand": "Lincee",
    "stock": 50,
    "sizes": ["S", "M", "L", "XL"],
    "colors": ["Black", "White", "Gray"],
    "images": ["https://example.com/hoodie1.jpg"],
    "active": true,
    "featured": true,
    "createdAt": "2026-02-01T10:00:00"
  }
]
```

---

### 12. Get Product by ID
**GET** `/api/v1/products/{id}`

**Response (200):**
```json
{
  "id": 1,
  "name": "Classic Streetwear Hoodie",
  "description": "Premium quality cotton hoodie with embroidered logo",
  "price": 79.99,
  "category": "HOODIES",
  "brand": "Lincee",
  "stock": 50,
  "sizes": ["S", "M", "L", "XL"],
  "colors": ["Black", "White", "Gray"],
  "images": ["https://example.com/hoodie1.jpg"],
  "rating": 4.5,
  "reviewCount": 23,
  "active": true,
  "featured": true
}
```

---

### 13. Create Product
**POST** `/api/v1/products`

**Headers:** `Authorization: Bearer <token>`

**Request:**
```json
{
  "name": "Urban T-Shirt",
  "description": "Comfortable cotton t-shirt",
  "price": 29.99,
  "category": "TSHIRTS",
  "brand": "Lincee",
  "stock": 100,
  "sizes": ["S", "M", "L", "XL"],
  "colors": ["Black", "White"],
  "images": ["https://example.com/tshirt.jpg"]
}
```

**Response (200):**
```json
{
  "id": 2,
  "name": "Urban T-Shirt",
  "price": 29.99,
  "message": "Product created successfully"
}
```

---

### 14. Update Product
**PUT** `/api/v1/products/{id}`

**Headers:** `Authorization: Bearer <token>`

**Request:**
```json
{
  "price": 34.99,
  "stock": 80
}
```

**Response (200):**
```json
{
  "id": 2,
  "name": "Urban T-Shirt",
  "price": 34.99,
  "stock": 80,
  "message": "Product updated successfully"
}
```

---

### 15. Delete Product
**DELETE** `/api/v1/products/{id}`

**Headers:** `Authorization: Bearer <token>`

**Response (200):**
```json
{
  "message": "Product deleted successfully"
}
```

---

### 16. Search Products
**GET** `/api/v1/products/search?query=hoodie`

**Response (200):**
```json
[
  {
    "id": 1,
    "name": "Classic Streetwear Hoodie",
    "price": 79.99,
    "category": "HOODIES",
    "stock": 50
  }
]
```

---

### 17. Get Products by Category
**GET** `/api/v1/products/category/{category}`

Example: `/api/v1/products/category/HOODIES`

**Response (200):**
```json
[
  {
    "id": 1,
    "name": "Classic Streetwear Hoodie",
    "price": 79.99,
    "category": "HOODIES"
  }
]
```

---

### 18. Get Products by Brand
**GET** `/api/v1/products/brand/{brand}`

Example: `/api/v1/products/brand/Lincee`

**Response (200):**
```json
[
  {
    "id": 1,
    "name": "Classic Streetwear Hoodie",
    "brand": "Lincee",
    "price": 79.99
  }
]
```

---

### 19. Get Featured Products
**GET** `/api/v1/products/featured`

**Response (200):**
```json
[
  {
    "id": 1,
    "name": "Classic Streetwear Hoodie",
    "price": 79.99,
    "featured": true
  }
]
```

---

### 20. Check Product Stock
**GET** `/api/v1/products/{id}/stock`

**Response (200):**
```json
{
  "productId": 1,
  "productName": "Classic Streetwear Hoodie",
  "stock": 50,
  "available": true
}
```

---

## 🛒 Cart APIs

### 21. Get User Cart
**GET** `/api/v1/cart/user/{userId}`

**Headers:** `Authorization: Bearer <token>`

**Response (200):**
```json
{
  "id": 1,
  "userId": 1,
  "items": [
    {
      "id": 1,
      "productId": 1,
      "productName": "Classic Streetwear Hoodie",
      "quantity": 2,
      "price": 79.99,
      "size": "L",
      "color": "Black",
      "subtotal": 159.98
    }
  ],
  "totalItems": 2,
  "totalPrice": 159.98,
  "updatedAt": "2026-02-03T14:30:00"
}
```

---

### 22. Add Item to Cart
**POST** `/api/v1/cart/user/{userId}/items`

**Headers:** `Authorization: Bearer <token>`

**Request:**
```json
{
  "productId": 1,
  "quantity": 2,
  "size": "L",
  "color": "Black"
}
```

**Response (200):**
```json
{
  "message": "Item added to cart",
  "cartId": 1,
  "itemId": 1,
  "quantity": 2,
  "subtotal": 159.98
}
```

---

### 23. Update Cart Item
**PUT** `/api/v1/cart/items/{itemId}`

**Headers:** `Authorization: Bearer <token>`

**Request:**
```json
{
  "quantity": 3,
  "size": "XL"
}
```

**Response (200):**
```json
{
  "message": "Cart item updated",
  "itemId": 1,
  "quantity": 3,
  "subtotal": 239.97
}
```

---

### 24. Remove Item from Cart
**DELETE** `/api/v1/cart/items/{itemId}`

**Headers:** `Authorization: Bearer <token>`

**Response (200):**
```json
{
  "message": "Item removed from cart"
}
```

---

### 25. Clear Cart
**DELETE** `/api/v1/cart/user/{userId}/clear`

**Headers:** `Authorization: Bearer <token>`

**Response (200):**
```json
{
  "message": "Cart cleared successfully"
}
```

---

### 26. Get Cart Item Count
**GET** `/api/v1/cart/user/{userId}/count`

**Headers:** `Authorization: Bearer <token>`

**Response (200):**
```json
{
  "userId": 1,
  "itemCount": 5,
  "uniqueProducts": 3
}
```

---

### 27. Get Cart Total
**GET** `/api/v1/cart/user/{userId}/total`

**Headers:** `Authorization: Bearer <token>`

**Response (200):**
```json
{
  "userId": 1,
  "totalItems": 5,
  "totalPrice": 299.95,
  "currency": "USD"
}
```

---

## 📦 Order APIs

### 28. Get All Orders
**GET** `/api/v1/orders`

**Headers:** `Authorization: Bearer <token>`

**Response (200):**
```json
[
  {
    "id": 1,
    "userId": 1,
    "orderNumber": "ORD-2026-001",
    "status": "DELIVERED",
    "totalAmount": 159.98,
    "shippingAddress": "123 Main St, City",
    "orderDate": "2026-02-01T10:00:00",
    "deliveryDate": "2026-02-03T15:30:00"
  }
]
```

---

### 29. Get Order by ID
**GET** `/api/v1/orders/{id}`

**Headers:** `Authorization: Bearer <token>`

**Response (200):**
```json
{
  "id": 1,
  "userId": 1,
  "orderNumber": "ORD-2026-001",
  "status": "DELIVERED",
  "items": [
    {
      "productId": 1,
      "productName": "Classic Streetwear Hoodie",
      "quantity": 2,
      "price": 79.99,
      "size": "L",
      "color": "Black"
    }
  ],
  "subtotal": 159.98,
  "tax": 15.00,
  "shippingCost": 10.00,
  "totalAmount": 184.98,
  "shippingAddress": {
    "street": "123 Main St",
    "city": "New York",
    "state": "NY",
    "zipCode": "10001",
    "country": "USA"
  },
  "billingAddress": {
    "street": "123 Main St",
    "city": "New York",
    "state": "NY",
    "zipCode": "10001"
  },
  "paymentMethod": "CARD",
  "orderDate": "2026-02-01T10:00:00",
  "deliveryDate": "2026-02-03T15:30:00"
}
```

---

### 30. Create Order
**POST** `/api/v1/orders`

**Headers:** `Authorization: Bearer <token>`

**Request:**
```json
{
  "userId": 1,
  "items": [
    {
      "productId": 1,
      "quantity": 2,
      "size": "L",
      "color": "Black"
    }
  ],
  "shippingAddressId": 1,
  "billingAddressId": 1,
  "paymentMethod": "CARD"
}
```

**Response (200):**
```json
{
  "orderId": 1,
  "orderNumber": "ORD-2026-001",
  "status": "PENDING",
  "totalAmount": 184.98,
  "message": "Order created successfully"
}
```

---

### 31. Update Order
**PUT** `/api/v1/orders/{id}`

**Headers:** `Authorization: Bearer <token>`

**Request:**
```json
{
  "status": "SHIPPED",
  "trackingNumber": "TRACK123456"
}
```

**Response (200):**
```json
{
  "orderId": 1,
  "status": "SHIPPED",
  "trackingNumber": "TRACK123456",
  "message": "Order updated successfully"
}
```

---

### 32. Cancel Order
**DELETE** `/api/v1/orders/{id}`

**Headers:** `Authorization: Bearer <token>`

**Response (200):**
```json
{
  "orderId": 1,
  "status": "CANCELLED",
  "message": "Order cancelled successfully"
}
```

---

### 33. Get User Orders
**GET** `/api/v1/orders/user/{userId}`

**Headers:** `Authorization: Bearer <token>`

**Response (200):**
```json
[
  {
    "id": 1,
    "orderNumber": "ORD-2026-001",
    "status": "DELIVERED",
    "totalAmount": 184.98,
    "orderDate": "2026-02-01T10:00:00"
  }
]
```

---

### 34. Get Orders by Status
**GET** `/api/v1/orders/status/{status}`

Example: `/api/v1/orders/status/PENDING`

**Headers:** `Authorization: Bearer <token>`

**Response (200):**
```json
[
  {
    "id": 2,
    "orderNumber": "ORD-2026-002",
    "status": "PENDING",
    "totalAmount": 99.99,
    "orderDate": "2026-02-03T12:00:00"
  }
]
```

---

### 35. Track Order
**GET** `/api/v1/orders/{id}/track`

**Headers:** `Authorization: Bearer <token>`

**Response (200):**
```json
{
  "orderId": 1,
  "orderNumber": "ORD-2026-001",
  "status": "SHIPPED",
  "trackingNumber": "TRACK123456",
  "estimatedDelivery": "2026-02-05T18:00:00",
  "trackingHistory": [
    {
      "status": "ORDERED",
      "timestamp": "2026-02-01T10:00:00",
      "location": "Warehouse"
    },
    {
      "status": "SHIPPED",
      "timestamp": "2026-02-02T14:00:00",
      "location": "Distribution Center"
    }
  ]
}
```

---

### 36. Get Order Statistics
**GET** `/api/v1/orders/statistics?startDate=2026-01-01&endDate=2026-02-03`

**Headers:** `Authorization: Bearer <token>`

**Response (200):**
```json
{
  "totalOrders": 150,
  "totalRevenue": 12450.50,
  "averageOrderValue": 83.00,
  "ordersByStatus": {
    "PENDING": 20,
    "PROCESSING": 15,
    "SHIPPED": 30,
    "DELIVERED": 80,
    "CANCELLED": 5
  },
  "topProducts": [
    {
      "productId": 1,
      "productName": "Classic Streetwear Hoodie",
      "orderCount": 45
    }
  ]
}
```

---

### 37. Update Order Status
**PATCH** `/api/v1/orders/{id}/status`

**Headers:** `Authorization: Bearer <token>`

**Request:**
```json
{
  "status": "DELIVERED"
}
```

**Response (200):**
```json
{
  "orderId": 1,
  "previousStatus": "SHIPPED",
  "newStatus": "DELIVERED",
  "message": "Order status updated successfully"
}
```

---

### 38. Get Order Invoice
**GET** `/api/v1/orders/{id}/invoice`

**Headers:** `Authorization: Bearer <token>`

**Response (200):**
```json
{
  "invoiceNumber": "INV-2026-001",
  "orderId": 1,
  "orderNumber": "ORD-2026-001",
  "customerName": "John Doe",
  "items": [
    {
      "productName": "Classic Streetwear Hoodie",
      "quantity": 2,
      "unitPrice": 79.99,
      "total": 159.98
    }
  ],
  "subtotal": 159.98,
  "tax": 15.00,
  "shipping": 10.00,
  "grandTotal": 184.98,
  "invoiceDate": "2026-02-01T10:00:00"
}
```

---

## 📍 Address APIs

### 39. Get All Addresses
**GET** `/api/v1/addresses`

**Headers:** `Authorization: Bearer <token>`

**Response (200):**
```json
[
  {
    "id": 1,
    "userId": 1,
    "type": "SHIPPING",
    "street": "123 Main St",
    "city": "New York",
    "state": "NY",
    "zipCode": "10001",
    "country": "USA",
    "isDefault": true
  }
]
```

---

### 40. Get Address by ID
**GET** `/api/v1/addresses/{id}`

**Headers:** `Authorization: Bearer <token>`

**Response (200):**
```json
{
  "id": 1,
  "userId": 1,
  "type": "SHIPPING",
  "street": "123 Main St",
  "apartment": "Apt 4B",
  "city": "New York",
  "state": "NY",
  "zipCode": "10001",
  "country": "USA",
  "isDefault": true,
  "createdAt": "2026-02-01T10:00:00"
}
```

---

### 41. Create Address
**POST** `/api/v1/addresses`

**Headers:** `Authorization: Bearer <token>`

**Request:**
```json
{
  "userId": 1,
  "type": "SHIPPING",
  "street": "456 Oak Ave",
  "apartment": "Suite 200",
  "city": "Los Angeles",
  "state": "CA",
  "zipCode": "90001",
  "country": "USA",
  "isDefault": false
}
```

**Response (200):**
```json
{
  "id": 2,
  "userId": 1,
  "type": "SHIPPING",
  "street": "456 Oak Ave",
  "city": "Los Angeles",
  "message": "Address created successfully"
}
```

---

### 42. Update Address
**PUT** `/api/v1/addresses/{id}`

**Headers:** `Authorization: Bearer <token>`

**Request:**
```json
{
  "street": "456 Oak Ave",
  "apartment": "Suite 300",
  "zipCode": "90002"
}
```

**Response (200):**
```json
{
  "id": 2,
  "street": "456 Oak Ave",
  "apartment": "Suite 300",
  "zipCode": "90002",
  "message": "Address updated successfully"
}
```

---

### 43. Delete Address
**DELETE** `/api/v1/addresses/{id}`

**Headers:** `Authorization: Bearer <token>`

**Response (200):**
```json
{
  "message": "Address deleted successfully"
}
```

---

### 44. Get User Addresses
**GET** `/api/v1/addresses/user/{userId}`

**Headers:** `Authorization: Bearer <token>`

**Response (200):**
```json
[
  {
    "id": 1,
    "type": "SHIPPING",
    "street": "123 Main St",
    "city": "New York",
    "state": "NY",
    "zipCode": "10001",
    "isDefault": true
  },
  {
    "id": 2,
    "type": "BILLING",
    "street": "456 Oak Ave",
    "city": "Los Angeles",
    "state": "CA",
    "zipCode": "90001",
    "isDefault": false
  }
]
```

---

### 45. Get Shipping Addresses
**GET** `/api/v1/addresses/user/{userId}/shipping`

**Headers:** `Authorization: Bearer <token>`

**Response (200):**
```json
[
  {
    "id": 1,
    "type": "SHIPPING",
    "street": "123 Main St",
    "city": "New York",
    "state": "NY",
    "zipCode": "10001",
    "isDefault": true
  }
]
```

---

### 46. Get Billing Addresses
**GET** `/api/v1/addresses/user/{userId}/billing`

**Headers:** `Authorization: Bearer <token>`

**Response (200):**
```json
[
  {
    "id": 2,
    "type": "BILLING",
    "street": "456 Oak Ave",
    "city": "Los Angeles",
    "state": "CA",
    "zipCode": "90001",
    "isDefault": false
  }
]
```

---

### 47. Set Default Address
**PATCH** `/api/v1/addresses/{id}/default`

**Headers:** `Authorization: Bearer <token>`

**Response (200):**
```json
{
  "id": 2,
  "isDefault": true,
  "message": "Default address updated successfully"
}
```

---

## 💳 Payment APIs

### 48. Get All Payments
**GET** `/api/v1/payments`

**Headers:** `Authorization: Bearer <token>`

**Response (200):**
```json
[
  {
    "id": 1,
    "orderId": 1,
    "userId": 1,
    "amount": 184.98,
    "paymentMethod": "CARD",
    "status": "COMPLETED",
    "transactionId": "TXN123456789",
    "paymentDate": "2026-02-01T10:30:00"
  }
]
```

---

### 49. Get Payment by ID
**GET** `/api/v1/payments/{id}`

**Headers:** `Authorization: Bearer <token>`

**Response (200):**
```json
{
  "id": 1,
  "orderId": 1,
  "userId": 1,
  "amount": 184.98,
  "paymentMethod": "CARD",
  "cardLast4": "4242",
  "cardBrand": "Visa",
  "status": "COMPLETED",
  "transactionId": "TXN123456789",
  "paymentDate": "2026-02-01T10:30:00",
  "processedBy": "Stripe"
}
```

---

### 50. Process Payment
**POST** `/api/v1/payments`

**Headers:** `Authorization: Bearer <token>`

**Request:**
```json
{
  "orderId": 1,
  "userId": 1,
  "amount": 184.98,
  "paymentMethod": "CARD",
  "cardNumber": "4242424242424242",
  "cardExpiry": "12/28",
  "cardCvv": "123",
  "cardholderName": "John Doe"
}
```

**Response (200):**
```json
{
  "paymentId": 1,
  "orderId": 1,
  "status": "COMPLETED",
  "transactionId": "TXN123456789",
  "amount": 184.98,
  "message": "Payment processed successfully"
}
```

---

### 51. Update Payment
**PUT** `/api/v1/payments/{id}`

**Headers:** `Authorization: Bearer <token>`

**Request:**
```json
{
  "status": "REFUNDED",
  "refundAmount": 184.98
}
```

**Response (200):**
```json
{
  "paymentId": 1,
  "status": "REFUNDED",
  "refundAmount": 184.98,
  "message": "Payment updated successfully"
}
```

---

### 52. Delete Payment
**DELETE** `/api/v1/payments/{id}`

**Headers:** `Authorization: Bearer <token>`

**Response (200):**
```json
{
  "message": "Payment record deleted successfully"
}
```

---

### 53. Get User Payments
**GET** `/api/v1/payments/user/{userId}`

**Headers:** `Authorization: Bearer <token>`

**Response (200):**
```json
[
  {
    "id": 1,
    "orderId": 1,
    "amount": 184.98,
    "paymentMethod": "CARD",
    "status": "COMPLETED",
    "paymentDate": "2026-02-01T10:30:00"
  }
]
```

---

### 54. Get Payments by Status
**GET** `/api/v1/payments/status/{status}`

Example: `/api/v1/payments/status/COMPLETED`

**Headers:** `Authorization: Bearer <token>`

**Response (200):**
```json
[
  {
    "id": 1,
    "orderId": 1,
    "amount": 184.98,
    "status": "COMPLETED",
    "transactionId": "TXN123456789",
    "paymentDate": "2026-02-01T10:30:00"
  }
]
```

---

### 55. Get Payment by Transaction ID
**GET** `/api/v1/payments/transaction/{transactionId}`

**Headers:** `Authorization: Bearer <token>`

**Response (200):**
```json
{
  "id": 1,
  "orderId": 1,
  "amount": 184.98,
  "status": "COMPLETED",
  "transactionId": "TXN123456789",
  "paymentDate": "2026-02-01T10:30:00"
}
```

---

### 56. Process Refund
**POST** `/api/v1/payments/{id}/refund`

**Headers:** `Authorization: Bearer <token>`

**Request:**
```json
{
  "refundAmount": 184.98,
  "reason": "Customer request"
}
```

**Response (200):**
```json
{
  "paymentId": 1,
  "refundId": "REF123456",
  "refundAmount": 184.98,
  "status": "REFUNDED",
  "message": "Refund processed successfully"
}
```

---

### 57. Update Payment Status
**PATCH** `/api/v1/payments/{id}/status`

**Headers:** `Authorization: Bearer <token>`

**Request:**
```json
{
  "status": "FAILED",
  "failureReason": "Insufficient funds"
}
```

**Response (200):**
```json
{
  "paymentId": 1,
  "previousStatus": "PENDING",
  "newStatus": "FAILED",
  "message": "Payment status updated successfully"
}
```

---

### 58. Verify Payment
**GET** `/api/v1/payments/{id}/verify`

**Headers:** `Authorization: Bearer <token>`

**Response (200):**
```json
{
  "paymentId": 1,
  "verified": true,
  "status": "COMPLETED",
  "transactionId": "TXN123456789",
  "verifiedAt": "2026-02-01T10:35:00"
}
```

---

## ⭐ Review APIs

### 59. Get All Reviews
**GET** `/api/v1/reviews`

**Response (200):**
```json
[
  {
    "id": 1,
    "productId": 1,
    "userId": 1,
    "username": "johndoe",
    "rating": 5,
    "title": "Amazing quality!",
    "comment": "Best hoodie I've ever owned. Highly recommend!",
    "helpful": 15,
    "verified": true,
    "createdAt": "2026-02-01T12:00:00"
  }
]
```

---

### 60. Get Review by ID
**GET** `/api/v1/reviews/{id}`

**Response (200):**
```json
{
  "id": 1,
  "productId": 1,
  "productName": "Classic Streetwear Hoodie",
  "userId": 1,
  "username": "johndoe",
  "rating": 5,
  "title": "Amazing quality!",
  "comment": "Best hoodie I've ever owned. Highly recommend!",
  "helpful": 15,
  "verified": true,
  "createdAt": "2026-02-01T12:00:00",
  "updatedAt": "2026-02-01T12:00:00"
}
```

---

### 61. Create Review
**POST** `/api/v1/reviews`

**Headers:** `Authorization: Bearer <token>`

**Request:**
```json
{
  "productId": 1,
  "userId": 1,
  "rating": 5,
  "title": "Great product!",
  "comment": "Exceeded my expectations. Will buy again!"
}
```

**Response (200):**
```json
{
  "reviewId": 1,
  "productId": 1,
  "rating": 5,
  "message": "Review submitted successfully"
}
```

---

### 62. Update Review
**PUT** `/api/v1/reviews/{id}`

**Headers:** `Authorization: Bearer <token>`

**Request:**
```json
{
  "rating": 4,
  "title": "Good product",
  "comment": "Updated review - still satisfied!"
}
```

**Response (200):**
```json
{
  "reviewId": 1,
  "rating": 4,
  "message": "Review updated successfully"
}
```

---

### 63. Delete Review
**DELETE** `/api/v1/reviews/{id}`

**Headers:** `Authorization: Bearer <token>`

**Response (200):**
```json
{
  "message": "Review deleted successfully"
}
```

---

### 64. Get Product Reviews
**GET** `/api/v1/reviews/product/{productId}`

**Response (200):**
```json
[
  {
    "id": 1,
    "userId": 1,
    "username": "johndoe",
    "rating": 5,
    "title": "Amazing quality!",
    "comment": "Best hoodie ever!",
    "helpful": 15,
    "createdAt": "2026-02-01T12:00:00"
  }
]
```

---

### 65. Get User Reviews
**GET** `/api/v1/reviews/user/{userId}`

**Headers:** `Authorization: Bearer <token>`

**Response (200):**
```json
[
  {
    "id": 1,
    "productId": 1,
    "productName": "Classic Streetwear Hoodie",
    "rating": 5,
    "title": "Amazing quality!",
    "createdAt": "2026-02-01T12:00:00"
  }
]
```

---

### 66. Mark Review as Helpful
**POST** `/api/v1/reviews/{id}/helpful`

**Headers:** `Authorization: Bearer <token>`

**Response (200):**
```json
{
  "reviewId": 1,
  "helpful": 16,
  "message": "Review marked as helpful"
}
```

---

### 67. Get Product Rating Statistics
**GET** `/api/v1/reviews/product/{productId}/stats`

**Response (200):**
```json
{
  "productId": 1,
  "averageRating": 4.7,
  "totalReviews": 23,
  "ratingDistribution": {
    "5": 18,
    "4": 3,
    "3": 1,
    "2": 1,
    "1": 0
  },
  "verifiedPurchases": 20,
  "recommendationRate": 95.7
}
```

---

## 📊 Dashboard APIs

### 68. Get Dashboard Overview
**GET** `/api/v1/dashboard/overview`

**Headers:** `Authorization: Bearer <token>`

**Response (200):**
```json
{
  "totalRevenue": 125450.75,
  "totalOrders": 523,
  "totalUsers": 1234,
  "totalProducts": 87,
  "revenueGrowth": 12.5,
  "orderGrowth": 8.3,
  "userGrowth": 15.7,
  "period": "Last 30 days"
}
```

---

### 69. Get Sales Statistics
**GET** `/api/v1/dashboard/sales?startDate=2026-01-01&endDate=2026-02-03`

**Headers:** `Authorization: Bearer <token>`

**Response (200):**
```json
{
  "totalSales": 125450.75,
  "totalOrders": 523,
  "averageOrderValue": 239.90,
  "salesByDay": [
    {
      "date": "2026-02-01",
      "sales": 4250.50,
      "orders": 18
    },
    {
      "date": "2026-02-02",
      "sales": 5100.25,
      "orders": 22
    }
  ],
  "topSellingProducts": [
    {
      "productId": 1,
      "productName": "Classic Streetwear Hoodie",
      "unitsSold": 156,
      "revenue": 12474.44
    }
  ]
}
```

---

### 70. Get User Statistics
**GET** `/api/v1/dashboard/users`

**Headers:** `Authorization: Bearer <token>`

**Response (200):**
```json
{
  "totalUsers": 1234,
  "activeUsers": 987,
  "newUsersThisMonth": 145,
  "usersByRole": {
    "CUSTOMER": 1200,
    "ADMIN": 4,
    "MODERATOR": 30
  },
  "userGrowthRate": 15.7
}
```

---

### 71. Get Product Statistics
**GET** `/api/v1/dashboard/products`

**Headers:** `Authorization: Bearer <token>`

**Response (200):**
```json
{
  "totalProducts": 87,
  "activeProducts": 82,
  "outOfStock": 5,
  "lowStock": 12,
  "productsByCategory": {
    "HOODIES": 25,
    "TSHIRTS": 30,
    "JACKETS": 15,
    "ACCESSORIES": 17
  },
  "averagePrice": 65.50
}
```

---

### 72. Get Revenue by Category
**GET** `/api/v1/dashboard/revenue/category`

**Headers:** `Authorization: Bearer <token>`

**Response (200):**
```json
{
  "HOODIES": 45250.50,
  "TSHIRTS": 32100.25,
  "JACKETS": 28750.00,
  "ACCESSORIES": 19350.00,
  "totalRevenue": 125450.75
}
```

---

### 73. Get Top Customers
**GET** `/api/v1/dashboard/customers/top?limit=10`

**Headers:** `Authorization: Bearer <token>`

**Response (200):**
```json
[
  {
    "userId": 45,
    "username": "johndoe",
    "email": "john@example.com",
    "totalOrders": 28,
    "totalSpent": 4567.89,
    "averageOrderValue": 163.14,
    "lastOrderDate": "2026-02-02T15:30:00"
  }
]
```

---

### 74. Get Recent Activities
**GET** `/api/v1/dashboard/activities/recent?limit=20`

**Headers:** `Authorization: Bearer <token>`

**Response (200):**
```json
[
  {
    "id": 1,
    "type": "ORDER_PLACED",
    "userId": 123,
    "username": "johndoe",
    "description": "Order #ORD-2026-523 placed",
    "amount": 184.98,
    "timestamp": "2026-02-03T14:45:00"
  },
  {
    "id": 2,
    "type": "USER_REGISTERED",
    "userId": 124,
    "username": "janedoe",
    "description": "New user registered",
    "timestamp": "2026-02-03T14:30:00"
  }
]
```

---

### 75. Get Inventory Status
**GET** `/api/v1/dashboard/inventory`

**Headers:** `Authorization: Bearer <token>`

**Response (200):**
```json
{
  "totalProducts": 87,
  "inStock": 82,
  "lowStock": 12,
  "outOfStock": 5,
  "totalInventoryValue": 125430.50,
  "lowStockProducts": [
    {
      "productId": 15,
      "productName": "Urban Jacket",
      "currentStock": 3,
      "minimumStock": 10
    }
  ]
}
```

---

### 76. Get Performance Metrics
**GET** `/api/v1/dashboard/metrics`

**Headers:** `Authorization: Bearer <token>`

**Response (200):**
```json
{
  "conversionRate": 3.45,
  "averageOrderValue": 239.90,
  "cartAbandonmentRate": 68.5,
  "customerRetentionRate": 45.2,
  "averageSessionDuration": "5m 32s",
  "pageViews": 45678,
  "bounceRate": 42.3
}
```

---

## 🖼️ Image APIs

### 77. Upload Image
**POST** `/api/v1/images/upload`

**Headers:** 
- `Authorization: Bearer <token>`
- `Content-Type: multipart/form-data`

**Request:**
```
Form Data:
- file: [binary image file]
- category: "product" | "user" | "banner"
- entityId: 123 (optional)
```

**Response (200):**
```json
{
  "imageId": 1,
  "url": "https://storage.example.com/images/product_12345.jpg",
  "filename": "product_12345.jpg",
  "size": 245678,
  "mimeType": "image/jpeg",
  "category": "product",
  "uploadedAt": "2026-02-03T14:30:00",
  "message": "Image uploaded successfully"
}
```

---

## 🏥 Health Check API

### 78. Health Check
**GET** `/api/v1/health`

**Response (200):**
```json
{
  "status": "UP",
  "service": "Lincee Streetwear Backend",
  "version": "1.0.0",
  "timestamp": "2026-02-03T14:30:45.123456",
  "cors": "enabled"
}
```

---

## Common HTTP Status Codes

- **200 OK** - Request successful
- **201 Created** - Resource created successfully
- **400 Bad Request** - Invalid request data
- **401 Unauthorized** - Missing or invalid authentication token
- **403 Forbidden** - Insufficient permissions
- **404 Not Found** - Resource not found
- **409 Conflict** - Resource already exists
- **500 Internal Server Error** - Server error

## Authentication

Most endpoints require authentication. Include the JWT token in the Authorization header:

```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

Get the token by calling the `/api/v1/auth/login` or `/api/v1/auth/register` endpoints.

## Error Response Format

All error responses follow this structure:

```json
{
  "message": "Error description",
  "error": "ERROR_CODE",
  "timestamp": "2026-02-03T14:30:45.123456"
}
```

---

**Total Endpoints:** 78

**Base URL:** `https://linceecom-production.up.railway.app`

**Swagger UI:** `https://linceecom-production.up.railway.app/swagger-ui.html`

**API Docs JSON:** `https://linceecom-production.up.railway.app/v3/api-docs`

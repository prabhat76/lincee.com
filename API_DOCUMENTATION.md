# Lincee E-Commerce Platform API Documentation

## Project Overview
Lincee is a complete streetwear e-commerce platform backend built with Spring Boot 3.2, Java 21, PostgreSQL, and JWT authentication.

## Base URL
```
http://localhost:8080/api/v1
```

---

## Authentication Endpoints

### POST /auth/login
Authenticate user and return JWT token
```json
Request:
{
  "username": "user@example.com",
  "password": "password123"
}

Response:
{
  "token": "eyJhbGc...",
  "type": "Bearer",
  "username": "user@example.com",
  "message": "Login successful"
}
```

### POST /auth/register
Register a new user account
```json
Request:
{
  "username": "newuser",
  "email": "newuser@example.com",
  "password": "password123",
  "firstName": "John",
  "lastName": "Doe"
}

Response:
{
  "message": "User registered successfully",
  "username": "newuser",
  "email": "newuser@example.com"
}
```

### POST /auth/logout
Logout user and invalidate token

### POST /auth/refresh
Refresh JWT token

---

## Product Management Endpoints

### GET /products
Get all products or filter by active status
```
Query Parameters:
- active (boolean, optional): Filter by active status
```

### GET /products/{id}
Get product by ID

### POST /products
Create new product
```json
Request:
{
  "name": "Premium T-Shirt",
  "description": "High-quality streetwear",
  "price": 49.99,
  "discountPrice": 39.99,
  "category": "T-Shirts",
  "subCategory": "Basic",
  "brand": "Lincee",
  "stockQuantity": 100,
  "availableSizes": ["S", "M", "L", "XL"],
  "availableColors": ["Black", "White", "Red"],
  "imageUrls": ["http://example.com/img1.jpg"]
}
```

### PUT /products/{id}
Update product details

### DELETE /products/{id}
Delete product

### GET /products/search
Search products by keyword
```
Query Parameters:
- keyword (string): Search keyword
```

---

## User Management Endpoints

### GET /users
Get all users

### GET /users/{id}
Get user by ID

### POST /users
Create new user

### PUT /users/{id}
Update user details

### DELETE /users/{id}
Delete user

---

## Shopping Cart Endpoints

### GET /cart/user/{userId}
Get or create cart for user

### GET /cart/{cartId}
Get cart by ID

### POST /cart/user/{userId}/items
Add item to cart
```json
Query Parameters:
- productId (Long): Product ID
- quantity (Integer, default=1): Quantity
- size (String, optional): Size
- color (String, optional): Color

Response:
{
  "id": 1,
  "cartId": 1,
  "productId": 10,
  "productName": "Premium T-Shirt",
  "productImage": "http://example.com/img.jpg",
  "quantity": 2,
  "unitPrice": 39.99,
  "subTotal": 79.98,
  "size": "M",
  "color": "Black"
}
```

### PUT /cart/items/{cartItemId}
Update cart item quantity
```
Query Parameters:
- userId (Long): User ID
- quantity (Integer): New quantity
```

### DELETE /cart/user/{userId}/items/{cartItemId}
Remove item from cart

### GET /cart/user/{userId}/items
Get all items in cart

### DELETE /cart/user/{userId}/clear
Clear entire cart

---

## Order Management Endpoints

### POST /orders
Create new order
```json
Query Parameters:
- userId (Long): User ID

Request:
{
  "totalAmount": 159.98,
  "discountAmount": 10.00,
  "shippingCost": 5.00,
  "taxAmount": 15.00,
  "notes": "Please deliver after 5 PM",
  "shippingAddressId": 1,
  "billingAddressId": 1
}
```

### GET /orders/{id}
Get order by ID

### GET /orders/number/{orderNumber}
Get order by order number

### GET /orders/user/{userId}
Get all orders for user (paginated)
```
Query Parameters:
- page (int): Page number (0-based)
- size (int): Page size
- sort (String): Sort criteria
```

### GET /orders/user/{userId}/list
Get all user orders as a list

### GET /orders/status/{status}
Get orders by status (PENDING, CONFIRMED, PROCESSING, SHIPPED, DELIVERED, CANCELLED, RETURNED)

### PUT /orders/{id}
Update order details

### PATCH /orders/{id}/status
Update order status
```
Query Parameters:
- status (OrderStatus): New status
```

### DELETE /orders/{id}
Delete order

### GET /orders/stats/count
Get total order count

### GET /orders/stats/status/{status}/count
Get order count by status

---

## Address Management Endpoints

### POST /addresses
Add new address for user
```json
Query Parameters:
- userId (Long): User ID

Request:
{
  "addressLine1": "123 Main Street",
  "addressLine2": "Apt 4B",
  "city": "New York",
  "state": "NY",
  "zipCode": "10001",
  "country": "USA",
  "phoneNumber": "555-0123",
  "isDefault": true,
  "isShippingAddress": true,
  "isBillingAddress": false,
  "addressType": "HOME"
}
```

### GET /addresses/{id}
Get address by ID

### GET /addresses/user/{userId}
Get all addresses for user

### GET /addresses/user/{userId}/shipping
Get all shipping addresses

### GET /addresses/user/{userId}/billing
Get all billing addresses

### GET /addresses/user/{userId}/default
Get default address

### PUT /addresses/{id}
Update address

### DELETE /addresses/{id}
Delete address

---

## Payment Management Endpoints

### POST /payments
Create new payment
```json
Query Parameters:
- orderId (Long): Order ID

Request:
{
  "paymentMethod": "CREDIT_CARD",
  "amount": 159.98,
  "paymentGateway": "STRIPE",
  "cardLastFour": "4242"
}
```

### GET /payments/{id}
Get payment by ID

### GET /payments/order/{orderId}
Get payment by order ID

### GET /payments/transaction/{transactionId}
Get payment by transaction ID

### GET /payments/status/{status}
Get payments by status (PENDING, PROCESSING, COMPLETED, FAILED, CANCELLED, REFUNDED)

### GET /payments/user/{userId}
Get all payments by user

### PATCH /payments/{id}/status
Update payment status
```
Query Parameters:
- status (PaymentStatus): New status
```

### PATCH /payments/{id}/complete
Mark payment as completed
```
Query Parameters:
- transactionId (String): Transaction ID from payment gateway
```

### PUT /payments/{id}
Update payment details

### DELETE /payments/{id}
Delete payment record

### GET /payments/stats/count
Get payment statistics (total, completed, pending)

---

## Product Reviews Endpoints

### POST /reviews
Add review for product
```json
Query Parameters:
- productId (Long): Product ID
- userId (Long): User ID

Request:
{
  "rating": 5,
  "title": "Excellent quality!",
  "comment": "This shirt is amazing! Great fit and very comfortable. Highly recommend!",
  "verifiedPurchase": true
}
```

### GET /reviews/{id}
Get review by ID

### GET /reviews/product/{productId}
Get product reviews (paginated)

### GET /reviews/product/{productId}/list
Get product reviews as list

### GET /reviews/user/{userId}
Get user's reviews

### PUT /reviews/{id}
Update review

### DELETE /reviews/{id}
Delete review

### PATCH /reviews/{id}/helpful
Mark review as helpful (increment helpful count)

### GET /reviews/product/{productId}/stats
Get product rating statistics
```json
Response:
{
  "averageRating": 4.5,
  "reviewCount": 12
}
```

---

## Dashboard Analytics Endpoints

### GET /dashboard/overview
Get complete dashboard overview with all key metrics
```json
Response:
{
  "totalOrders": 150,
  "pendingOrders": 10,
  "confirmedOrders": 20,
  "processingOrders": 30,
  "shippedOrders": 50,
  "deliveredOrders": 35,
  "cancelledOrders": 5,
  "totalPayments": 145,
  "completedPayments": 140,
  "pendingPayments": 5,
  "totalProducts": 500,
  "activeProducts": 450,
  "totalUsers": 1000,
  "totalCarts": 250,
  "totalReviews": 300
}
```

### GET /dashboard/orders/statistics
Get detailed order statistics

### GET /dashboard/payments/statistics
Get payment statistics breakdown

### GET /dashboard/products/statistics
Get product catalog statistics

### GET /dashboard/users/statistics
Get user management statistics

### GET /dashboard/cart/statistics
Get shopping cart statistics

### GET /dashboard/reviews/statistics
Get review and ratings statistics

### GET /dashboard/summary
Get quick summary of all key metrics

### GET /dashboard/health
Check dashboard operational status

---

## Health Check Endpoints

### GET /health
Get application health status

---

## Common Query Parameters for Pagination

For endpoints that support pagination, use:
```
- page (int): Page number starting from 0
- size (int): Number of records per page
- sort (String): Sorting criteria (e.g., "createdAt,desc")
```

---

## Response Codes

- **200 OK**: Successful GET, PUT, PATCH request
- **201 Created**: Successful POST request creating a resource
- **204 No Content**: Successful DELETE request
- **400 Bad Request**: Invalid request data
- **401 Unauthorized**: Missing or invalid authentication
- **404 Not Found**: Resource not found
- **409 Conflict**: Resource already exists
- **500 Internal Server Error**: Server error

---

## Error Response Format

```json
{
  "timestamp": "2026-02-03T18:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid request data",
  "path": "/api/v1/products"
}
```

---

## Entity Relationships

```
User
├── Cart (1:1)
├── Order (1:N)
│   ├── OrderItem (1:N)
│   │   └── Product (M:1)
│   ├── Address (1:1) - Shipping
│   ├── Address (1:1) - Billing
│   └── Payment (1:1)
├── Address (1:N)
├── Review (1:N)
│   └── Product (M:1)
└── CartItem (1:N)
    └── Product (M:1)

Product
├── OrderItem (1:N)
├── CartItem (1:N)
└── Review (1:N)
```

---

## Key Features

### 1. **Complete E-Commerce Functionality**
   - User authentication with JWT
   - Product catalog management
   - Shopping cart operations
   - Order placement and tracking
   - Payment processing
   - Product reviews and ratings

### 2. **Order Management**
   - Multiple order statuses (PENDING, CONFIRMED, PROCESSING, SHIPPED, DELIVERED, CANCELLED, RETURNED)
   - Order tracking with order numbers
   - Estimated delivery dates
   - Order history per user

### 3. **Cart Management**
   - Add/remove items
   - Update quantities
   - Size and color selection
   - Automatic total calculation
   - Cart persistence

### 4. **Address Management**
   - Multiple addresses per user
   - Default address selection
   - Shipping and billing address types
   - Address type classification (HOME, OFFICE, etc.)

### 5. **Payment Processing**
   - Multiple payment methods (Credit Card, Debit Card, Digital Wallet, etc.)
   - Payment status tracking
   - Transaction ID management
   - Payment completion tracking

### 6. **Review System**
   - Product ratings (1-5 stars)
   - Review comments
   - Verified purchase indication
   - Helpful count tracking
   - Average rating calculation

### 7. **Dashboard Analytics**
   - Order statistics and breakdown
   - Payment statistics
   - Product inventory overview
   - User management stats
   - Review analytics
   - Real-time dashboard metrics

---

## Database Setup

The application uses PostgreSQL with the following configuration:

```properties
Database: neondb
Host: ep-morning-credit-ahu0h3sm-pooler.c-3.us-east-1.aws.neon.tech
Port: 5432
SSL: Required
```

Connection pooling is configured with:
- Maximum pool size: 20
- Minimum idle: 5
- Connection timeout: 20 seconds

---

## Security Configuration

- JWT Token expiration: 24 hours
- CORS enabled for all origins
- Security headers configured
- SQL injection prevention through parameterized queries
- Password validation with minimum 8 characters

---

## Running the Application

### Build
```bash
mvn clean compile
mvn package
```

### Run
```bash
java -jar target/lincee-backend-1.0.0.jar
```

### Development
```bash
mvn spring-boot:run
```

---

## API Testing

### Using cURL
```bash
# Login
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"user","password":"pass123"}'

# Get Products
curl -X GET http://localhost:8080/api/v1/products \
  -H "Authorization: Bearer YOUR_TOKEN"

# Create Order
curl -X POST http://localhost:8080/api/v1/orders?userId=1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{"totalAmount":159.98,"discountAmount":10.00}'
```

### Using Postman
1. Import API endpoints from Swagger UI
2. Set Bearer token in Authorization tab
3. Use provided request examples

---

## Swagger/OpenAPI Documentation

Access Swagger UI at:
```
http://localhost:8080/swagger-ui.html
```

Access OpenAPI JSON at:
```
http://localhost:8080/v3/api-docs
```

---

## Database Schema

The application automatically creates and manages the following tables through JPA/Hibernate:

- `users` - User accounts
- `products` - Product catalog
- `product_sizes` - Available sizes for products
- `product_colors` - Available colors for products
- `product_images` - Product images
- `carts` - Shopping carts
- `cart_items` - Items in shopping carts
- `orders` - Customer orders
- `order_items` - Items in orders
- `addresses` - User addresses
- `payments` - Payment records
- `reviews` - Product reviews

---

## Performance Optimizations

- Database connection pooling (HikariCP)
- Batch processing for database operations
- Index optimization on frequently queried columns
- Lazy loading of relationships
- Query optimization with custom JPA queries

---

## Future Enhancements

1. **Inventory Management**
   - Stock alerts
   - Warehouse management
   - Automatic reorder system

2. **Advanced Analytics**
   - Sales reporting
   - Customer behavior analytics
   - Inventory forecasting

3. **Payment Integration**
   - Stripe integration
   - PayPal integration
   - Multiple currency support

4. **Notifications**
   - Email notifications
   - SMS alerts
   - Order status updates

5. **Recommendations**
   - Product recommendations
   - Personalized suggestions
   - Similar products

6. **Admin Features**
   - Bulk operations
   - Advanced reporting
   - Customer management

---

## Support & Contact

For issues, questions, or feature requests, please refer to the project documentation or contact the development team.

---

**Version:** 1.0.0
**Last Updated:** February 3, 2026
**Status:** Production Ready

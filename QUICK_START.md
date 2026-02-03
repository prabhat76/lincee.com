# Lincee E-Commerce Platform - Quick Start Guide

## 🚀 Getting Started

### Prerequisites
- Java 21 (JDK)
- Maven 3.8+
- PostgreSQL 12+
- Git

### Clone the Repository
```bash
cd /Users/prabhatkumar/Documents/lincee-backend
```

### Build the Project
```bash
mvn clean compile
mvn package -DskipTests
```

### Run the Application
```bash
java -jar target/lincee-backend-1.0.0.jar
```

The application will start on `http://localhost:8080`

---

## 📚 API Base URL
```
http://localhost:8080/api/v1
```

---

## 🔐 Authentication Flow

### 1. Login
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "user@example.com",
    "password": "password123"
  }'
```

Response:
```json
{
  "token": "eyJhbGc...",
  "type": "Bearer",
  "username": "user@example.com",
  "message": "Login successful"
}
```

### 2. Use Token in Requests
Add the token to all subsequent requests:
```bash
Authorization: Bearer eyJhbGc...
```

---

## 🛍️ E-Commerce Workflow

### Step 1: Browse Products
```bash
curl -X GET http://localhost:8080/api/v1/products \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### Step 2: Add to Cart
```bash
curl -X POST "http://localhost:8080/api/v1/cart/user/1/items?productId=10&quantity=2&size=M&color=Black" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### Step 3: View Cart
```bash
curl -X GET http://localhost:8080/api/v1/cart/user/1 \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### Step 4: Add Address
```bash
curl -X POST "http://localhost:8080/api/v1/addresses?userId=1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "addressLine1": "123 Main Street",
    "city": "New York",
    "state": "NY",
    "zipCode": "10001",
    "country": "USA",
    "isDefault": true,
    "isShippingAddress": true
  }'
```

### Step 5: Place Order
```bash
curl -X POST "http://localhost:8080/api/v1/orders?userId=1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "totalAmount": 159.98,
    "discountAmount": 10.00,
    "shippingCost": 5.00,
    "taxAmount": 15.00,
    "shippingAddressId": 1,
    "billingAddressId": 1,
    "notes": "Please deliver after 5 PM"
  }'
```

### Step 6: Process Payment
```bash
curl -X POST "http://localhost:8080/api/v1/payments?orderId=1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "paymentMethod": "CREDIT_CARD",
    "amount": 159.98,
    "paymentGateway": "STRIPE",
    "cardLastFour": "4242"
  }'
```

### Step 7: Complete Payment
```bash
curl -X PATCH "http://localhost:8080/api/v1/payments/1/complete?transactionId=txn_12345" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### Step 8: Track Order
```bash
curl -X GET http://localhost:8080/api/v1/orders/1 \
  -H "Authorization: Bearer YOUR_TOKEN"
```

---

## 📊 Dashboard Access

### View Dashboard Overview
```bash
curl -X GET http://localhost:8080/api/v1/dashboard/overview \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### Order Statistics
```bash
curl -X GET http://localhost:8080/api/v1/dashboard/orders/statistics \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### Payment Statistics
```bash
curl -X GET http://localhost:8080/api/v1/dashboard/payments/statistics \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### Product Statistics
```bash
curl -X GET http://localhost:8080/api/v1/dashboard/products/statistics \
  -H "Authorization: Bearer YOUR_TOKEN"
```

---

## ⭐ Product Reviews

### Add Review
```bash
curl -X POST "http://localhost:8080/api/v1/reviews?productId=10&userId=1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "rating": 5,
    "title": "Excellent quality!",
    "comment": "This product is amazing! Highly recommend!",
    "verifiedPurchase": true
  }'
```

### Get Product Reviews
```bash
curl -X GET "http://localhost:8080/api/v1/reviews/product/10?page=0&size=10" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### Get Product Rating
```bash
curl -X GET http://localhost:8080/api/v1/reviews/product/10/stats \
  -H "Authorization: Bearer YOUR_TOKEN"
```

---

## 🗂️ Project Structure

```
lincee-backend/
├── src/
│   ├── main/
│   │   ├── java/com/lincee/
│   │   │   ├── LinceeApplication.java
│   │   │   ├── config/
│   │   │   │   ├── CorsConfig.java
│   │   │   │   ├── OpenApiConfig.java
│   │   │   │   └── SecurityConfig.java
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── ProductController.java
│   │   │   │   ├── UserController.java
│   │   │   │   ├── OrderController.java
│   │   │   │   ├── CartController.java
│   │   │   │   ├── AddressController.java
│   │   │   │   ├── PaymentController.java
│   │   │   │   ├── ReviewController.java
│   │   │   │   ├── DashboardController.java
│   │   │   │   └── HealthController.java
│   │   │   ├── dto/
│   │   │   │   ├── OrderDTO.java
│   │   │   │   ├── CartDTO.java
│   │   │   │   ├── AddressDTO.java
│   │   │   │   ├── PaymentDTO.java
│   │   │   │   ├── ReviewDTO.java
│   │   │   │   └── ...
│   │   │   ├── entity/
│   │   │   │   ├── User.java
│   │   │   │   ├── Product.java
│   │   │   │   ├── Order.java
│   │   │   │   ├── OrderItem.java
│   │   │   │   ├── Cart.java
│   │   │   │   ├── CartItem.java
│   │   │   │   ├── Address.java
│   │   │   │   ├── Payment.java
│   │   │   │   ├── Review.java
│   │   │   │   └── ...
│   │   │   ├── repository/
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── ProductRepository.java
│   │   │   │   ├── OrderRepository.java
│   │   │   │   ├── CartRepository.java
│   │   │   │   ├── AddressRepository.java
│   │   │   │   ├── PaymentRepository.java
│   │   │   │   ├── ReviewRepository.java
│   │   │   │   └── ...
│   │   │   ├── service/
│   │   │   │   ├── JwtService.java
│   │   │   │   ├── OrderService.java
│   │   │   │   ├── CartService.java
│   │   │   │   ├── AddressService.java
│   │   │   │   ├── PaymentService.java
│   │   │   │   ├── ReviewService.java
│   │   │   │   └── ...
│   │   │   └── exception/
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── pom.xml
├── Procfile
├── API_DOCUMENTATION.md
└── QUICK_START.md
```

---

## 📋 Implemented Features

### ✅ Core E-Commerce
- [x] User Authentication (JWT)
- [x] Product Management
- [x] Shopping Cart
- [x] Order Management
- [x] Payment Processing
- [x] Address Management
- [x] Product Reviews & Ratings

### ✅ Dashboard
- [x] Order Analytics
- [x] Payment Statistics
- [x] Product Statistics
- [x] User Statistics
- [x] Review Analytics
- [x] Real-time Metrics

### ✅ Order Features
- [x] Order Creation
- [x] Order Tracking
- [x] Multiple Order Statuses
- [x] Estimated Delivery
- [x] Order History

### ✅ User Features
- [x] User Registration
- [x] User Login
- [x] Multiple Addresses
- [x] Default Address
- [x] Shipping/Billing Addresses

### ✅ Payment Features
- [x] Payment Creation
- [x] Payment Status Tracking
- [x] Multiple Payment Methods
- [x] Transaction Management

### ✅ Review Features
- [x] Add Reviews
- [x] Update Reviews
- [x] Delete Reviews
- [x] Rating Calculation
- [x] Review Helpful Count
- [x] Verified Purchase Flag

---

## 🔧 Configuration

### Database Configuration
Edit `application.properties` to change database settings:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/linceedb
spring.datasource.username=postgres
spring.datasource.password=password
```

### JWT Configuration
```properties
app.jwt.secret=your-secret-key
app.jwt.expiration=86400000  # 24 hours in milliseconds
```

### Server Port
```properties
server.port=8080
server.servlet.context-path=/api/v1
```

---

## 🧪 Testing with Postman

1. **Download Postman** from postman.com
2. **Import Collection**: Use the provided Postman collection
3. **Set Environment Variables**:
   - `base_url`: http://localhost:8080/api/v1
   - `token`: Your JWT token from login
4. **Run Requests**: Click on individual requests to test

---

## 📱 Frontend Integration

### Example: Login Flow
```javascript
const login = async (username, password) => {
  const response = await fetch('http://localhost:8080/api/v1/auth/login', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ username, password })
  });
  const data = await response.json();
  localStorage.setItem('token', data.token);
  return data;
};
```

### Example: Add to Cart
```javascript
const addToCart = async (userId, productId, quantity) => {
  const token = localStorage.getItem('token');
  const response = await fetch(
    `http://localhost:8080/api/v1/cart/user/${userId}/items?productId=${productId}&quantity=${quantity}`,
    {
      method: 'POST',
      headers: { 'Authorization': `Bearer ${token}` }
    }
  );
  return response.json();
};
```

---

## 🐛 Troubleshooting

### Application won't start
- Check if PostgreSQL is running
- Verify database credentials in `application.properties`
- Check if port 8080 is available

### Authentication errors
- Ensure token is included in Authorization header
- Check if token has expired
- Verify JWT secret in `application.properties`

### Database connection errors
- Verify PostgreSQL connection details
- Check database exists
- Ensure user has proper permissions

---

## 📞 Support

For issues or questions:
1. Check the API_DOCUMENTATION.md
2. Review error messages in console
3. Check logs in `/logs` directory

---

## 🚀 Deployment

### Build for Production
```bash
mvn clean package -DskipTests
```

### Docker Support
Create a `Dockerfile`:
```dockerfile
FROM openjdk:21-jdk
COPY target/lincee-backend-1.0.0.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Build and run:
```bash
docker build -t lincee-backend .
docker run -p 8080:8080 lincee-backend
```

---

**Happy Coding! 🎉**

For detailed API documentation, see [API_DOCUMENTATION.md](./API_DOCUMENTATION.md)

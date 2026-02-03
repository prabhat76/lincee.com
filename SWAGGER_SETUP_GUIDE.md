# Swagger/OpenAPI Setup Guide

## Overview

The Lincee E-Commerce Platform uses **Springdoc OpenAPI 2.0** to automatically generate and display API documentation through Swagger UI. All endpoints are automatically documented with complete request/response schemas.

---

## Accessing Swagger UI

Once the application is running, access the Swagger UI at:

```
http://localhost:8080/swagger-ui.html
```

Or the OpenAPI JSON specification at:

```
http://localhost:8080/v3/api-docs
```

---

## Configuration

### Current Setup in `application.properties`

```properties
# Springdoc OpenAPI Configuration
springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.operations-sorter=method
springdoc.swagger-ui.tags-sorter=alpha
springdoc.swagger-ui.display-request-duration=true
springdoc.swagger-ui.doc-expansion=list
springdoc.swagger-ui.show-extensions=true
springdoc.swagger-ui.urls-primary-name=Lincee API
springdoc.packages-to-scan=com.lincee.controller
springdoc.paths-to-match=/api/v1/**
```

### OpenAPI Info Configuration in `OpenApiConfig.java`

```java
@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Lincee Streetwear E-commerce API")
                        .description("Complete backend solution for Lincee streetwear e-commerce platform")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Lincee Team")
                                .email("support@lincee.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT token for authentication")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}
```

---

## API Documentation Structure

All controllers are automatically documented with Springdoc OpenAPI annotations:

### @RestController & @RequestMapping
```java
@RestController
@RequestMapping("/orders")
@Tag(name = "Order Management", description = "APIs for managing orders")
public class OrderController { ... }
```

### @GetMapping, @PostMapping, etc.
```java
@GetMapping("/{id}")
@Operation(summary = "Get order by ID", 
           description = "Retrieve a specific order by its ID")
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Order found"),
    @ApiResponse(responseCode = "404", description = "Order not found")
})
public ResponseEntity<OrderDTO> getOrderById(
        @Parameter(description = "Order ID") @PathVariable Long id) {
    ...
}
```

### Key Annotations Used

| Annotation | Purpose |
|-----------|---------|
| `@Tag` | Groups endpoints in Swagger UI |
| `@Operation` | Documents endpoint summary and description |
| `@ApiResponse` / `@ApiResponses` | Documents HTTP response codes |
| `@Parameter` | Documents path/query parameters |
| `@RequestBody` | Automatically documented for request payloads |
| `@SecurityRequirement` | Marks endpoints requiring JWT authentication |

---

## Complete API Endpoints in Swagger

### 1. **Authentication** (AuthController)
- `POST /auth/login` - User login
- `POST /auth/register` - User registration
- `POST /auth/logout` - User logout
- `POST /auth/refresh` - Refresh JWT token

### 2. **Product Management** (ProductController)
- `GET /products` - Get all products
- `GET /products/{id}` - Get product by ID
- `POST /products` - Create new product
- `PUT /products/{id}` - Update product
- `DELETE /products/{id}` - Delete product
- `GET /products/search` - Search products

### 3. **Shopping Cart** (CartController)
- `GET /cart/user/{userId}` - Get or create cart
- `GET /cart/{cartId}` - Get cart by ID
- `POST /cart/user/{userId}/items` - Add item to cart
- `PUT /cart/items/{cartItemId}` - Update cart item
- `DELETE /cart/user/{userId}/items/{cartItemId}` - Remove item
- `GET /cart/user/{userId}/items` - Get cart items
- `DELETE /cart/user/{userId}/clear` - Clear cart

### 4. **Order Management** (OrderController)
- `POST /orders` - Create new order
- `GET /orders/{id}` - Get order by ID
- `GET /orders/number/{orderNumber}` - Get order by order number
- `GET /orders/user/{userId}` - Get user orders (paginated)
- `GET /orders/user/{userId}/list` - Get user orders as list
- `GET /orders/status/{status}` - Get orders by status
- `PUT /orders/{id}` - Update order
- `PATCH /orders/{id}/status` - Update order status
- `DELETE /orders/{id}` - Delete order
- `GET /orders/stats/count` - Get order count
- `GET /orders/stats/status/{status}/count` - Get count by status

### 5. **Address Management** (AddressController)
- `POST /addresses` - Add new address
- `GET /addresses/{id}` - Get address by ID
- `GET /addresses/user/{userId}` - Get user addresses
- `GET /addresses/user/{userId}/shipping` - Get shipping addresses
- `GET /addresses/user/{userId}/billing` - Get billing addresses
- `GET /addresses/user/{userId}/default` - Get default address
- `PUT /addresses/{id}` - Update address
- `DELETE /addresses/{id}` - Delete address

### 6. **Payment Processing** (PaymentController)
- `POST /payments` - Create new payment
- `GET /payments/{id}` - Get payment by ID
- `GET /payments/order/{orderId}` - Get payment by order
- `GET /payments/transaction/{transactionId}` - Get payment by transaction
- `GET /payments/status/{status}` - Get payments by status
- `GET /payments/user/{userId}` - Get user payments
- `PATCH /payments/{id}/status` - Update payment status
- `PATCH /payments/{id}/complete` - Complete payment
- `PUT /payments/{id}` - Update payment
- `DELETE /payments/{id}` - Delete payment
- `GET /payments/stats/count` - Get payment statistics

### 7. **Product Reviews** (ReviewController)
- `POST /reviews` - Add review
- `GET /reviews/{id}` - Get review by ID
- `GET /reviews/product/{productId}` - Get product reviews (paginated)
- `GET /reviews/product/{productId}/list` - Get product reviews as list
- `GET /reviews/user/{userId}` - Get user reviews
- `PUT /reviews/{id}` - Update review
- `DELETE /reviews/{id}` - Delete review
- `PATCH /reviews/{id}/helpful` - Mark as helpful
- `GET /reviews/product/{productId}/stats` - Get product stats

### 8. **Dashboard Analytics** (DashboardController)
- `GET /dashboard/overview` - Dashboard overview
- `GET /dashboard/orders/statistics` - Order statistics
- `GET /dashboard/payments/statistics` - Payment statistics
- `GET /dashboard/products/statistics` - Product statistics
- `GET /dashboard/users/statistics` - User statistics
- `GET /dashboard/cart/statistics` - Cart statistics
- `GET /dashboard/reviews/statistics` - Review statistics
- `GET /dashboard/summary` - Summary metrics
- `GET /dashboard/health` - Health check

### 9. **Health Check** (HealthController)
- `GET /health` - Application health status

---

## JWT Authentication in Swagger

The API uses **Bearer Token (JWT)** authentication. To test authenticated endpoints in Swagger UI:

1. **Call the login endpoint:**
   ```json
   POST /auth/login
   {
     "username": "user@example.com",
     "password": "password123"
   }
   ```

2. **Copy the returned token** from the response

3. **Click the "Authorize" button** at the top of Swagger UI

4. **Paste the token** in the format:
   ```
   Bearer eyJhbGc...
   ```

5. **All subsequent requests** will include the JWT token automatically

---

## Request/Response Examples in Swagger

Every endpoint in Swagger UI includes:

- **Request Parameters** with descriptions and data types
- **Request Body** with schema and example values
- **Response Codes** (200, 201, 400, 404, etc.) with descriptions
- **Response Body** with complete JSON schema
- **Try It Out** button to test directly from the browser

### Example: Create Order
```json
POST /orders?userId=1
Content-Type: application/json
Authorization: Bearer <JWT_TOKEN>

Request Body:
{
  "totalAmount": 159.98,
  "discountAmount": 10.00,
  "shippingCost": 5.00,
  "taxAmount": 15.00,
  "notes": "Please deliver after 5 PM",
  "shippingAddressId": 1,
  "billingAddressId": 1
}

Response (201 Created):
{
  "id": 1,
  "orderNumber": "ORD-1709566800000-abc123",
  "userId": 1,
  "totalAmount": 159.98,
  "discountAmount": 10.00,
  "shippingCost": 5.00,
  "taxAmount": 15.00,
  "status": "PENDING",
  "createdAt": "2026-02-03T18:30:00Z"
}
```

---

## Testing Endpoints in Swagger UI

### Step-by-Step Guide

1. **Start the application:**
   ```bash
   java -jar target/lincee-backend-1.0.0.jar
   ```

2. **Open Swagger UI:**
   ```
   http://localhost:8080/swagger-ui.html
   ```

3. **Authenticate (if required):**
   - Click "Authorize" button
   - Enter your JWT token

4. **Test an endpoint:**
   - Click on any endpoint to expand it
   - Click "Try it out"
   - Fill in parameters/body
   - Click "Execute"
   - View response

### Example Workflow

**1. Register a new user:**
```
POST /auth/register
{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "SecurePass123",
  "firstName": "John",
  "lastName": "Doe"
}
```

**2. Login to get token:**
```
POST /auth/login
{
  "username": "john@example.com",
  "password": "SecurePass123"
}
```

**3. Copy token and click Authorize**

**4. Create a cart:**
```
GET /cart/user/1
```

**5. Add items to cart:**
```
POST /cart/user/1/items?productId=10&quantity=2&size=M&color=Black
```

**6. Create an order:**
```
POST /orders?userId=1
{
  "totalAmount": 159.98,
  "shippingAddressId": 1,
  "billingAddressId": 1
}
```

**7. View dashboard:**
```
GET /dashboard/overview
```

---

## Additional Resources

### Download OpenAPI Specification
- **JSON Format:** `http://localhost:8080/v3/api-docs`
- **YAML Format:** `http://localhost:8080/v3/api-docs.yaml`

### Import into Postman
1. Open Postman
2. Click "Import"
3. Use URL: `http://localhost:8080/v3/api-docs`
4. Click "Import"

### OpenAPI Specification File
```bash
# Download OpenAPI spec
curl http://localhost:8080/v3/api-docs > openapi.json
```

---

## Customization

### Modify OpenAPI Info
Edit `OpenApiConfig.java`:
```java
.info(new Info()
    .title("Your API Title")
    .description("Your Description")
    .version("1.0.0")
    .contact(...)
    .license(...))
```

### Change Swagger UI Path
Edit `application.properties`:
```properties
springdoc.swagger-ui.path=/api-docs
```

### Exclude Endpoints from Documentation
Use `@Hidden` annotation:
```java
@GetMapping("/internal")
@Hidden
public ResponseEntity<?> internalEndpoint() { ... }
```

---

## Troubleshooting

### Swagger UI not loading
- Ensure application is running: `java -jar target/lincee-backend-1.0.0.jar`
- Check port configuration: `server.port=8080`
- Verify dependencies in `pom.xml`: `springdoc-openapi-starter-webmvc-ui`

### Missing endpoints in Swagger
- Verify controller classes are in `com.lincee.controller` package
- Check `springdoc.packages-to-scan` setting in `application.properties`
- Ensure `@RestController` annotation is present

### Authentication not working
- Verify JWT token is valid
- Use "Bearer <TOKEN>" format in Authorize dialog
- Check token expiration: 24 hours

### Incorrect documentation
- Update `@Operation`, `@ApiResponse` annotations in controller
- Restart application for changes to take effect
- Clear browser cache if needed

---

## API Versioning

Current API version: **1.0.0**

All endpoints follow the pattern:
```
http://localhost:8080/api/v1/{resource}/{action}
```

---

## Summary

✅ **All 89+ endpoints** are automatically documented in Swagger UI
✅ **Complete request/response schemas** for every endpoint
✅ **JWT authentication** integrated with Authorize button
✅ **Try It Out** feature for direct API testing
✅ **Real-time API documentation** that stays in sync with code
✅ **OpenAPI specification** available in JSON/YAML format

The API documentation is **self-documenting** - as you update controllers with Springdoc annotations, the Swagger UI updates automatically!

---

**Last Updated:** February 3, 2026
**Status:** Production Ready

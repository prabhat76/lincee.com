# API Documentation & Error Handling

## 📊 Complete API Documentation

A comprehensive Excel file **`LINCEE_API_DOCUMENTATION.xlsx`** has been generated with all API endpoints, including:

### Features:
- **29+ API Endpoints** across 10 modules
- **Full Request/Response Examples** for each endpoint
- **HTTP Status Codes** with descriptions
- **Error Handling Enums** with proper error codes
- **Two Sheets:**
  1. **API Documentation** - All endpoints with examples
  2. **HTTP Status Codes & Error Enums** - Reference guide

## 🔍 API Modules Documented

1. **Authentication** (4 endpoints)
   - Login, Register, Logout, Refresh Token

2. **Products** (7 endpoints)
   - CRUD operations, Search, Filter by Category, Featured

3. **Orders** (5 endpoints)
   - Create, Read, List, Update Status, Delete

4. **Shopping Cart** (5 endpoints)
   - Get, Add, Update, Remove, Clear

5. **Users** (3 endpoints)
   - List, Get by ID, Update

6. **Addresses** (4 endpoints)
   - Get, Create, Update, Delete

7. **Payments** (2 endpoints)
   - Create Intent, Get Status

8. **Reviews** (4 endpoints)
   - Create, Get, Update, Delete

9. **Collections** (3 endpoints)
   - Get All, Get by ID, Create

10. **Dashboard & Health** (2 endpoints)
    - Stats, Health Check

## ❌ Error Handling with Enums

### New Exception Classes Created:

#### 1. **ErrorCode Enum** (`com.lincee.exception.ErrorCode`)
Standardized error codes with HTTP status mapping:

```java
// Examples:
VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "400", "Validation failed")
UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "401", "Authentication required")
FORBIDDEN(HttpStatus.FORBIDDEN, "403", "Admin access required")
RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "Resource not found")
INSUFFICIENT_STOCK(HttpStatus.BAD_REQUEST, "400", "Out of stock")
PAYMENT_FAILED(HttpStatus.BAD_REQUEST, "400", "Payment declined")
// ... and 40+ more error codes
```

#### 2. **ApiErrorResponse** (`com.lincee.exception.ApiErrorResponse`)
Standard error response format:

```json
{
  "code": "VALIDATION_ERROR",
  "message": "Validation failed for input data",
  "status": 400,
  "timestamp": "2026-02-14T04:25:00",
  "path": "/api/v1/products",
  "fieldErrors": {
    "name": "Name is required",
    "price": "Price must be greater than 0"
  },
  "details": "Product name cannot be empty"
}
```

#### 3. **ApiException** (`com.lincee.exception.ApiException`)
Custom exception for throwing API errors:

```java
throw new ApiException(ErrorCode.PRODUCT_NOT_FOUND, "Product ID 999 not found");
```

#### 4. **GlobalExceptionHandler** (`com.lincee.exception.GlobalExceptionHandler`)
Centralized exception handling with:
- API exception handling
- Validation error mapping
- Runtime exception handling
- Global fallback for all exceptions

## 📋 HTTP Status Codes Reference

| Code | Name | Description |
|------|------|-------------|
| 200 | OK | Request successful |
| 201 | Created | Resource created successfully |
| 204 | No Content | Request successful, no content |
| 400 | Bad Request | Validation failed or missing fields |
| 401 | Unauthorized | Authentication required |
| 403 | Forbidden | User lacks permission |
| 404 | Not Found | Resource not found |
| 409 | Conflict | Duplicate resource |
| 500 | Internal Error | Server error |

## 🔧 Common Error Codes

| Error Code | Status | Description |
|-----------|--------|-------------|
| VALIDATION_ERROR | 400 | Input validation failed |
| INVALID_CREDENTIALS | 401 | Wrong username/password |
| ADMIN_REQUIRED | 403 | Admin access needed |
| PRODUCT_NOT_FOUND | 404 | Product doesn't exist |
| EMAIL_ALREADY_EXISTS | 409 | Email already registered |
| INSUFFICIENT_STOCK | 400 | Not enough inventory |
| PAYMENT_FAILED | 400 | Payment processing failed |
| INTERNAL_ERROR | 500 | Unexpected server error |

## 📝 Example Error Response

### Request:
```
POST /api/v1/products
Content-Type: application/json

{
  "name": "",
  "price": -10,
  "category": "Invalid"
}
```

### Response (400):
```json
{
  "code": "VALIDATION_ERROR",
  "message": "Validation failed for input data",
  "status": 400,
  "timestamp": "2026-02-14T04:30:00",
  "path": "/api/v1/products",
  "fieldErrors": {
    "name": "Product name cannot be blank",
    "price": "Price must be greater than 0",
    "category": "Invalid category provided"
  }
}
```

## 🚀 Integration Guide

### Using ApiException in Services:

```java
@Service
public class ProductService {
    
    public Product getProductById(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new ApiException(
                ErrorCode.PRODUCT_NOT_FOUND,
                "Product with ID " + id + " not found"
            ));
    }
    
    public Product createProduct(ProductDTO dto) {
        // Validate
        if (dto.getName() == null || dto.getName().isEmpty()) {
            throw new ApiException(
                ErrorCode.VALIDATION_ERROR,
                "Product name is required"
            );
        }
        // Save and return
    }
}
```

## 📂 Files Created

- `LINCEE_API_DOCUMENTATION.xlsx` - Complete API documentation
- `src/main/java/com/lincee/exception/ErrorCode.java` - Error code enum
- `src/main/java/com/lincee/exception/ApiErrorResponse.java` - Error response class
- `src/main/java/com/lincee/exception/ApiException.java` - Custom exception
- `src/main/java/com/lincee/exception/GlobalExceptionHandler.java` - Exception handler
- `generate_api_documentation.py` - Script to generate Excel file

## ✅ Benefits

✓ Standardized error responses across all APIs  
✓ Clear, actionable error messages  
✓ Proper HTTP status codes  
✓ Easy frontend error handling  
✓ Complete API documentation  
✓ Field-level validation errors  
✓ Audit trail with timestamps  

---

**Last Updated:** February 14, 2026

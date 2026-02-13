package com.lincee.exception;

import org.springframework.http.HttpStatus;

/**
 * Enum for standardized error codes across the application
 * Each error code maps to a specific HTTP status and message
 */
public enum ErrorCode {
    
    // Validation Errors (400)
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "Validation failed for input data"),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "INVALID_REQUEST", "Invalid request parameters"),
    MISSING_REQUIRED_FIELD(HttpStatus.BAD_REQUEST, "MISSING_REQUIRED_FIELD", "Required field is missing"),
    INVALID_EMAIL_FORMAT(HttpStatus.BAD_REQUEST, "INVALID_EMAIL_FORMAT", "Email format is invalid"),
    INVALID_PHONE_FORMAT(HttpStatus.BAD_REQUEST, "INVALID_PHONE_FORMAT", "Phone format is invalid"),
    INVALID_PAGINATION(HttpStatus.BAD_REQUEST, "INVALID_PAGINATION", "Invalid pagination parameters"),
    INVALID_SEARCH_QUERY(HttpStatus.BAD_REQUEST, "INVALID_SEARCH_QUERY", "Invalid search query"),
    
    // Authentication/Authorization Errors (401/403)
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "Authentication required or invalid token"),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS", "Invalid username or password"),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "TOKEN_EXPIRED", "Token has expired"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "INVALID_TOKEN", "Invalid or malformed token"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "FORBIDDEN", "User does not have permission to access this resource"),
    ADMIN_REQUIRED(HttpStatus.FORBIDDEN, "ADMIN_REQUIRED", "Admin access required for this operation"),
    
    // Resource Not Found (404)
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND", "Resource not found"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "User not found"),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "PRODUCT_NOT_FOUND", "Product not found"),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "ORDER_NOT_FOUND", "Order not found"),
    CART_NOT_FOUND(HttpStatus.NOT_FOUND, "CART_NOT_FOUND", "Cart not found"),
    ADDRESS_NOT_FOUND(HttpStatus.NOT_FOUND, "ADDRESS_NOT_FOUND", "Address not found"),
    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "PAYMENT_NOT_FOUND", "Payment not found"),
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "REVIEW_NOT_FOUND", "Review not found"),
    COLLECTION_NOT_FOUND(HttpStatus.NOT_FOUND, "COLLECTION_NOT_FOUND", "Collection not found"),
    
    // Conflict Errors (409)
    DUPLICATE_RESOURCE(HttpStatus.CONFLICT, "DUPLICATE_RESOURCE", "Resource already exists"),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "EMAIL_ALREADY_EXISTS", "Email is already registered"),
    USERNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "USERNAME_ALREADY_EXISTS", "Username is already taken"),
    DUPLICATE_PRODUCT(HttpStatus.CONFLICT, "DUPLICATE_PRODUCT", "Product already exists"),
    
    // Business Logic Errors (400)
    INSUFFICIENT_STOCK(HttpStatus.BAD_REQUEST, "INSUFFICIENT_STOCK", "Insufficient stock available for this product"),
    INVALID_ORDER(HttpStatus.BAD_REQUEST, "INVALID_ORDER", "Invalid order - must contain items"),
    INVALID_ORDER_STATUS(HttpStatus.BAD_REQUEST, "INVALID_ORDER_STATUS", "Invalid order status value"),
    INVALID_QUANTITY(HttpStatus.BAD_REQUEST, "INVALID_QUANTITY", "Quantity must be greater than 0"),
    INVALID_AMOUNT(HttpStatus.BAD_REQUEST, "INVALID_AMOUNT", "Amount must be greater than 0"),
    INVALID_DISCOUNT(HttpStatus.BAD_REQUEST, "INVALID_DISCOUNT", "Discount cannot exceed total amount"),
    INVALID_CATEGORY(HttpStatus.BAD_REQUEST, "INVALID_CATEGORY", "Invalid product category"),
    INVALID_PRICE(HttpStatus.BAD_REQUEST, "INVALID_PRICE", "Price must be greater than 0"),
    
    // Payment Errors (400)
    PAYMENT_FAILED(HttpStatus.BAD_REQUEST, "PAYMENT_FAILED", "Payment processing failed"),
    PAYMENT_DECLINED(HttpStatus.BAD_REQUEST, "PAYMENT_DECLINED", "Payment was declined by the card issuer"),
    INVALID_PAYMENT_METHOD(HttpStatus.BAD_REQUEST, "INVALID_PAYMENT_METHOD", "Invalid payment method"),
    PAYMENT_PROCESSING_ERROR(HttpStatus.BAD_REQUEST, "PAYMENT_PROCESSING_ERROR", "Error processing payment"),
    
    // Review Errors (400)
    INVALID_RATING(HttpStatus.BAD_REQUEST, "INVALID_RATING", "Rating must be between 1 and 5"),
    DUPLICATE_REVIEW(HttpStatus.CONFLICT, "DUPLICATE_REVIEW", "User has already reviewed this product"),
    
    // Cart Errors (400)
    INVALID_CART_ITEM(HttpStatus.BAD_REQUEST, "INVALID_CART_ITEM", "Invalid cart item data"),
    ITEM_NOT_IN_CART(HttpStatus.NOT_FOUND, "ITEM_NOT_IN_CART", "Item not found in cart"),
    
    // Address Errors (400)
    INVALID_ADDRESS(HttpStatus.BAD_REQUEST, "INVALID_ADDRESS", "Invalid address data"),
    INVALID_ZIPCODE(HttpStatus.BAD_REQUEST, "INVALID_ZIPCODE", "Invalid zip/postal code format"),
    
    // Collection Errors
    INVALID_COLLECTION(HttpStatus.BAD_REQUEST, "INVALID_COLLECTION", "Invalid collection data"),
    
    // Server Errors (500)
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "An unexpected error occurred"),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "DATABASE_ERROR", "Database operation failed"),
    EXTERNAL_SERVICE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "EXTERNAL_SERVICE_ERROR", "External service is unavailable"),
    FILE_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "FILE_UPLOAD_ERROR", "File upload failed"),
    IMAGE_PROCESSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "IMAGE_PROCESSING_ERROR", "Image processing failed"),
    
    // Service Unavailable (503)
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "SERVICE_UNAVAILABLE", "Service is temporarily unavailable");
    
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
    
    ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
    
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getMessage() {
        return message;
    }
}

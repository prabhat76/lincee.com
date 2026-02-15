package com.lincee.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for all API exceptions
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    /**
     * Handle custom API exceptions
     */
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiErrorResponse> handleApiException(
            ApiException ex,
            WebRequest request) {
        
        ApiErrorResponse errorResponse = new ApiErrorResponse(ex.getErrorCode());
        if (ex.getDetails() != null) {
            errorResponse.setDetails(ex.getDetails());
        }
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        
        return new ResponseEntity<>(errorResponse, ex.getErrorCode().getHttpStatus());
    }
    
    /**
     * Handle validation errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex,
            WebRequest request) {
        
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            fieldErrors.put(error.getField(), error.getDefaultMessage())
        );
        
        ApiErrorResponse errorResponse = new ApiErrorResponse(ErrorCode.VALIDATION_ERROR);
        errorResponse.setFieldErrors(fieldErrors);
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle file upload size exceeded
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiErrorResponse> handleMaxUploadSizeExceeded(
            MaxUploadSizeExceededException ex,
            WebRequest request) {

        ApiErrorResponse errorResponse = new ApiErrorResponse(
                ErrorCode.VALIDATION_ERROR,
                "File size exceeds the allowed limit"
        );
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(errorResponse, HttpStatus.PAYLOAD_TOO_LARGE);
    }
    
    /**
     * Handle resource not found exceptions
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiErrorResponse> handleRuntimeException(
            RuntimeException ex,
            WebRequest request) {
        
        ErrorCode errorCode = ErrorCode.INTERNAL_ERROR;
        
        // Map common RuntimeException messages to error codes
        if (ex.getMessage() != null) {
            if (ex.getMessage().contains("not found")) {
                errorCode = ErrorCode.RESOURCE_NOT_FOUND;
            } else if (ex.getMessage().contains("User not found")) {
                errorCode = ErrorCode.USER_NOT_FOUND;
            } else if (ex.getMessage().contains("Product not found")) {
                errorCode = ErrorCode.PRODUCT_NOT_FOUND;
            } else if (ex.getMessage().contains("Order not found")) {
                errorCode = ErrorCode.ORDER_NOT_FOUND;
            } else if (ex.getMessage().contains("Insufficient stock")) {
                errorCode = ErrorCode.INSUFFICIENT_STOCK;
            }
        }
        
        ApiErrorResponse errorResponse = new ApiErrorResponse(errorCode, ex.getMessage());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        
        return new ResponseEntity<>(errorResponse, errorCode.getHttpStatus());
    }
    
    /**
     * Handle all other exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGlobalException(
            Exception ex,
            WebRequest request) {
        logger.error("Unhandled exception", ex);
        ApiErrorResponse errorResponse = new ApiErrorResponse(ErrorCode.INTERNAL_ERROR, ex.getMessage());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

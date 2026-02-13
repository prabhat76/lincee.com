package com.lincee.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Standard error response format for all API errors
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiErrorResponse {
    
    private String code;
    private String message;
    private int status;
    private LocalDateTime timestamp;
    private String path;
    private Map<String, String> fieldErrors;
    private String details;
    
    public ApiErrorResponse() {
        this.timestamp = LocalDateTime.now();
    }
    
    public ApiErrorResponse(ErrorCode errorCode) {
        this();
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.status = errorCode.getHttpStatus().value();
    }
    
    public ApiErrorResponse(ErrorCode errorCode, String details) {
        this(errorCode);
        this.details = details;
    }
    
    public ApiErrorResponse(String code, String message, int status) {
        this();
        this.code = code;
        this.message = message;
        this.status = status;
    }
    
    // Getters and Setters
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public int getStatus() {
        return status;
    }
    
    public void setStatus(int status) {
        this.status = status;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getPath() {
        return path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }
    
    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }
    
    public void setFieldErrors(Map<String, String> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }
    
    public String getDetails() {
        return details;
    }
    
    public void setDetails(String details) {
        this.details = details;
    }
}

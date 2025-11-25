package com.gmp.auth.exception;

/**
 * 业务异常类
 * 用于处理系统中的业务逻辑错误
 */
public class BusinessException extends RuntimeException {
    
    private int statusCode;
    private String errorCode;
    
    public BusinessException(String message) {
        super(message);
        this.statusCode = 400; // 默认状态码
        this.errorCode = "BUSINESS_ERROR"; // 默认错误码
    }
    
    public BusinessException(int statusCode, String errorCode, String message) {
        super(message);
        this.statusCode = statusCode;
        this.errorCode = errorCode;
    }
    
    public int getStatusCode() {
        return statusCode;
    }
    
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
package com.gmp.auth.exception;

/**
 * 认证异常基类
 * 用于表示认证过程中出现的各种异常
 */
public class AuthenticationException extends RuntimeException {
    
    public AuthenticationException(String message) {
        super(message);
    }
    
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
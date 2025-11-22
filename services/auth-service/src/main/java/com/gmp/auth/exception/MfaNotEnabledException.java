package com.gmp.auth.exception;

/**
 * 多因素认证未启用异常
 */
public class MfaNotEnabledException extends RuntimeException {
    
    public MfaNotEnabledException(String message) {
        super(message);
    }
    
    public MfaNotEnabledException(String message, Throwable cause) {
        super(message, cause);
    }
}
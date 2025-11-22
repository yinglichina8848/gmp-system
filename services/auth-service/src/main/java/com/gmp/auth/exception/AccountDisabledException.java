package com.gmp.auth.exception;

/**
 * 账户禁用异常
 * 当用户账户被管理员禁用时抛出
 */
public class AccountDisabledException extends RuntimeException {
    
    public AccountDisabledException(String message) {
        super(message);
    }
    
    public AccountDisabledException(String message, Throwable cause) {
        super(message, cause);
    }
}
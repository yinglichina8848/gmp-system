package com.gmp.auth.exception;

/**
 * 账户锁定异常
 * 当用户账户因多次密码错误等原因被锁定时抛出
 */
public class AccountLockedException extends RuntimeException {
    
    public AccountLockedException(String message) {
        super(message);
    }
    
    public AccountLockedException(String message, Throwable cause) {
        super(message, cause);
    }
}
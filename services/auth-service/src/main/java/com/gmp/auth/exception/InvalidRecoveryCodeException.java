package com.gmp.auth.exception;

/**
 * 无效的恢复码异常
 * 当用户提供的恢复码无效时抛出
 */
public class InvalidRecoveryCodeException extends RuntimeException {

    /**
     * 构造函数
     * @param message 异常信息
     */
    public InvalidRecoveryCodeException(String message) {
        super(message);
    }

    /**
     * 构造函数
     * @param message 异常信息
     * @param cause 异常原因
     */
    public InvalidRecoveryCodeException(String message, Throwable cause) {
        super(message, cause);
    }
}
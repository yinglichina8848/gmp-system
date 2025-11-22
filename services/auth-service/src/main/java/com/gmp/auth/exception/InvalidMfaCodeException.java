package com.gmp.auth.exception;

/**
 * 无效的MFA验证码异常
 * 当用户提供的MFA验证码无效时抛出
 */
public class InvalidMfaCodeException extends RuntimeException {

    /**
     * 构造函数
     * @param message 异常信息
     */
    public InvalidMfaCodeException(String message) {
        super(message);
    }

    /**
     * 构造函数
     * @param message 异常信息
     * @param cause 异常原因
     */
    public InvalidMfaCodeException(String message, Throwable cause) {
        super(message, cause);
    }
}
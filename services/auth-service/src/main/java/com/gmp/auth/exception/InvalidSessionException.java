package com.gmp.auth.exception;

/**
 * 无效的会话异常
 * 当用户会话无效时抛出
 */
public class InvalidSessionException extends RuntimeException {

    /**
     * 构造函数
     * @param message 异常信息
     */
    public InvalidSessionException(String message) {
        super(message);
    }

    /**
     * 构造函数
     * @param message 异常信息
     * @param cause 异常原因
     */
    public InvalidSessionException(String message, Throwable cause) {
        super(message, cause);
    }
}
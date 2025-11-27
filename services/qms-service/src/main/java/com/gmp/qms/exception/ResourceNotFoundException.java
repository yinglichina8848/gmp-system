package com.gmp.qms.exception;

/**
 * 资源未找到异常
 * 
 * @author GMP系统开发团队
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * 构造函数
     * 
     * @param message 异常信息
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * 构造函数
     * 
     * @param message 异常信息
     * @param cause 异常原因
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

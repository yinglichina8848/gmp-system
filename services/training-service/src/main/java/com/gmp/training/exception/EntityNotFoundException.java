package com.gmp.training.exception;

/**
 * 实体未找到异常，当请求的实体不存在时抛出
 * 
 * @author GMP系统开发团队
 */
public class EntityNotFoundException extends RuntimeException {

    /**
     * 构造函数
     * 
     * @param message 异常消息
     */
    public EntityNotFoundException(String message) {
        super(message);
    }

    /**
     * 构造函数
     * 
     * @param message 异常消息
     * @param cause 异常原因
     */
    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

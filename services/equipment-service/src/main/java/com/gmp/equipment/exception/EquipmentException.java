package com.gmp.equipment.exception;

/**
 * 设备管理异常类
 * 设备管理系统的基础异常类
 * 
 * @author GMP系统开发团队
 * @version 1.0
 * @since 2023-07-01
 */
public class EquipmentException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 错误代码
     */
    private String errorCode;
    
    /**
     * 构造函数
     * 
     * @param message 错误信息
     */
    public EquipmentException(String message) {
        super(message);
    }
    
    /**
     * 构造函数
     * 
     * @param message 错误信息
     * @param cause 异常原因
     */
    public EquipmentException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * 构造函数
     * 
     * @param message 错误信息
     * @param errorCode 错误代码
     */
    public EquipmentException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    /**
     * 构造函数
     * 
     * @param message 错误信息
     * @param errorCode 错误代码
     * @param cause 异常原因
     */
    public EquipmentException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
    /**
     * 获取错误代码
     * 
     * @return 错误代码
     */
    public String getErrorCode() {
        return errorCode;
    }
    
    /**
     * 设置错误代码
     * 
     * @param errorCode 错误代码
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}

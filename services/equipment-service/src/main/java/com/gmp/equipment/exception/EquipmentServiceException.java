package com.gmp.equipment.exception;

/**
 * 设备服务异常类
 * 用于表示设备服务相关的业务异常
 */
public class EquipmentServiceException extends RuntimeException {

    private String errorCode;
    private String errorMessage;

    /**
     * 构造函数
     * @param message 错误消息
     */
    public EquipmentServiceException(String message) {
        super(message);
        this.errorMessage = message;
    }

    /**
     * 构造函数
     * @param errorCode 错误代码
     * @param errorMessage 错误消息
     */
    public EquipmentServiceException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    /**
     * 构造函数
     * @param message 错误消息
     * @param cause 异常原因
     */
    public EquipmentServiceException(String message, Throwable cause) {
        super(message, cause);
        this.errorMessage = message;
    }

    /**
     * 构造函数
     * @param errorCode 错误代码
     * @param errorMessage 错误消息
     * @param cause 异常原因
     */
    public EquipmentServiceException(String errorCode, String errorMessage, Throwable cause) {
        super(errorMessage, cause);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    /**
     * 获取错误代码
     * @return 错误代码
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * 设置错误代码
     * @param errorCode 错误代码
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * 获取错误消息
     * @return 错误消息
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * 设置错误消息
     * @param errorMessage 错误消息
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
package com.gmp.edms.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * API响应通用类
 */
@Data
@NoArgsConstructor
public class ApiResponse<T> {

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 错误码
     */
    private String errorCode;

    /**
     * 成功响应（只有数据）
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "操作成功", data, null);
    }

    /**
     * 成功响应
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<T>(true, message, data, null);
    }

    /**
     * 成功响应（无数据）
     */
    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<T>(true, message, null, null);
    }

    /**
     * 错误响应
     */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<T>(false, message, null, null);
    }

    /**
     * 错误响应（带错误码）
     */
    public static <T> ApiResponse<T> error(String message, String errorCode) {
        return new ApiResponse<T>(false, message, null, errorCode);
    }

    /**
     * 全参构造函数
     */
    public ApiResponse(boolean success, String message, T data, String errorCode) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.errorCode = errorCode;
    }
}
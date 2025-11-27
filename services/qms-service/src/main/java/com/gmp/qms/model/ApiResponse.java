package com.gmp.qms.model;

import lombok.Data;

/**
 * API响应结构
 * 
 * @author GMP系统开发团队
 */
@Data
public class ApiResponse<T> {

    /**
     * 状态码
     */
    private int code;

    /**
     * 消息
     */
    private String message;

    /**
     * 数据
     */
    private T data;

    /**
     * 成功响应构造函数
     * 
     * @param data 响应数据
     */
    public ApiResponse(T data) {
        this.code = 200;
        this.message = "Success";
        this.data = data;
    }

    /**
     * 带消息的响应构造函数
     * 
     * @param code 状态码
     * @param message 消息
     * @param data 响应数据
     */
    public ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功响应静态方法
     * 
     * @param data 响应数据
     * @param <T> 数据类型
     * @return API响应
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(data);
    }

    /**
     * 失败响应静态方法
     * 
     * @param code 状态码
     * @param message 失败消息
     * @param <T> 数据类型
     * @return API响应
     */
    public static <T> ApiResponse<T> fail(int code, String message) {
        return new ApiResponse<>(code, message, null);
    }
}

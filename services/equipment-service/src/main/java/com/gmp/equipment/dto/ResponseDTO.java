package com.gmp.equipment.dto;

import lombok.Data;

/**
 * 通用响应DTO类
 * 用于API接口的统一返回格式
 */
@Data
public class ResponseDTO<T> {

    /**
     * 响应码
     * 200: 成功
     * 400: 请求参数错误
     * 401: 未授权
     * 403: 禁止访问
     * 404: 资源不存在
     * 500: 服务器内部错误
     */
    private Integer code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 成功响应构造方法
     * @param data 响应数据
     */
    public ResponseDTO(T data) {
        this.code = 200;
        this.message = "success";
        this.data = data;
    }

    /**
     * 自定义响应构造方法
     * @param code 响应码
     * @param message 响应消息
     * @param data 响应数据
     */
    public ResponseDTO(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功响应静态方法
     * @param data 响应数据
     * @return 成功响应对象
     */
    public static <T> ResponseDTO<T> success(T data) {
        return new ResponseDTO<>(200, "success", data);
    }

    /**
     * 错误响应静态方法
     * @param code 响应码
     * @param message 错误消息
     * @return 错误响应对象
     */
    public static <T> ResponseDTO<T> error(Integer code, String message) {
        return new ResponseDTO<>(code, message, null);
    }

    /**
     * 参数错误响应静态方法
     * @param message 错误消息
     * @return 参数错误响应对象
     */
    public static <T> ResponseDTO<T> badRequest(String message) {
        return new ResponseDTO<>(400, message, null);
    }

    /**
     * 未授权响应静态方法
     * @return 未授权响应对象
     */
    public static <T> ResponseDTO<T> unauthorized() {
        return new ResponseDTO<>(401, "unauthorized", null);
    }

    /**
     * 禁止访问响应静态方法
     * @return 禁止访问响应对象
     */
    public static <T> ResponseDTO<T> forbidden() {
        return new ResponseDTO<>(403, "forbidden", null);
    }

    /**
     * 资源不存在响应静态方法
     * @return 资源不存在响应对象
     */
    public static <T> ResponseDTO<T> notFound() {
        return new ResponseDTO<>(404, "resource not found", null);
    }

    /**
     * 服务器内部错误响应静态方法
     * @return 服务器内部错误响应对象
     */
    public static <T> ResponseDTO<T> internalServerError() {
        return new ResponseDTO<>(500, "internal server error", null);
    }
}
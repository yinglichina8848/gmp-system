package com.gmp.equipment.util;

import java.io.Serializable;

/**
 * 统一响应结果类
 * 用于标准化API返回格式
 */
public class ResponseResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 状态码，0表示成功，非0表示失败
     */
    private int code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 构造函数
     */
    public ResponseResult() {
    }

    /**
     * 构造函数
     * @param code 状态码
     * @param message 响应消息
     */
    public ResponseResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 构造函数
     * @param code 状态码
     * @param message 响应消息
     * @param data 响应数据
     */
    public ResponseResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 获取状态码
     * @return 状态码
     */
    public int getCode() {
        return code;
    }

    /**
     * 设置状态码
     * @param code 状态码
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * 获取响应消息
     * @return 响应消息
     */
    public String getMessage() {
        return message;
    }

    /**
     * 设置响应消息
     * @param message 响应消息
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 获取响应数据
     * @return 响应数据
     */
    public T getData() {
        return data;
    }

    /**
     * 设置响应数据
     * @param data 响应数据
     */
    public void setData(T data) {
        this.data = data;
    }

    /**
     * 成功响应
     * @param <T> 数据类型
     * @return 成功响应结果
     */
    public static <T> ResponseResult<T> success() {
        return new ResponseResult<>(0, "操作成功");
    }

    /**
     * 成功响应
     * @param data 响应数据
     * @param <T> 数据类型
     * @return 成功响应结果
     */
    public static <T> ResponseResult<T> success(T data) {
        return new ResponseResult<>(0, "操作成功", data);
    }

    /**
     * 成功响应
     * @param message 响应消息
     * @param data 响应数据
     * @param <T> 数据类型
     * @return 成功响应结果
     */
    public static <T> ResponseResult<T> success(String message, T data) {
        return new ResponseResult<>(0, message, data);
    }

    /**
     * 失败响应
     * @param code 错误代码
     * @param message 错误消息
     * @param <T> 数据类型
     * @return 失败响应结果
     */
    public static <T> ResponseResult<T> error(int code, String message) {
        return new ResponseResult<>(code, message);
    }

    /**
     * 失败响应
     * @param message 错误消息
     * @param <T> 数据类型
     * @return 失败响应结果
     */
    public static <T> ResponseResult<T> error(String message) {
        return new ResponseResult<>(500, message);
    }

    @Override
    public String toString() {
        return "ResponseResult{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
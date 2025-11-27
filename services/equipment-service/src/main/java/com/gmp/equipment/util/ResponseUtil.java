package com.gmp.equipment.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * 响应工具类
 * 提供便捷的响应方法
 */
public class ResponseUtil {

    /**
     * 创建成功响应
     * @param data 响应数据
     * @param <T> 数据类型
     * @return ResponseEntity对象
     */
    public static <T> ResponseEntity<ResponseResult<T>> success(T data) {
        ResponseResult<T> result = ResponseResult.success(data);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 创建成功响应（无数据）
     * @param <T> 数据类型
     * @return ResponseEntity对象
     */
    public static <T> ResponseEntity<ResponseResult<T>> success() {
        ResponseResult<T> result = ResponseResult.success();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 创建成功响应（自定义消息）
     * @param message 响应消息
     * @param data 响应数据
     * @param <T> 数据类型
     * @return ResponseEntity对象
     */
    public static <T> ResponseEntity<ResponseResult<T>> success(String message, T data) {
        ResponseResult<T> result = ResponseResult.success(message, data);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 创建错误响应
     * @param code 错误代码
     * @param message 错误消息
     * @param <T> 数据类型
     * @return ResponseEntity对象
     */
    public static <T> ResponseEntity<ResponseResult<T>> error(int code, String message) {
        ResponseResult<T> result = ResponseResult.error(code, message);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        if (code >= 400 && code < 500) {
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(result, status);
    }

    /**
     * 创建错误响应（默认错误代码）
     * @param message 错误消息
     * @param <T> 数据类型
     * @return ResponseEntity对象
     */
    public static <T> ResponseEntity<ResponseResult<T>> error(String message) {
        ResponseResult<T> result = ResponseResult.error(message);
        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 创建错误响应（带HTTP状态码）
     * @param code 错误代码
     * @param message 错误消息
     * @param status HTTP状态码
     * @param <T> 数据类型
     * @return ResponseEntity对象
     */
    public static <T> ResponseEntity<ResponseResult<T>> error(int code, String message, HttpStatus status) {
        ResponseResult<T> result = ResponseResult.error(code, message);
        return new ResponseEntity<>(result, status);
    }
}
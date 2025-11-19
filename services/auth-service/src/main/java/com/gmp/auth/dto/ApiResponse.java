package com.gmp.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 通用API响应类
 *
 * @author GMP系统开发团队
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private boolean success;
    private String code;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    public ApiResponse(boolean success, String code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * 成功响应
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "200", "操作成功", data);
    }

    /**
     * 成功响应（无数据）
     */
    public static ApiResponse<Void> success() {
        return new ApiResponse<>(true, "200", "操作成功", null);
    }

    /**
     * 成功响应（自定义消息）
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, "200", message, data);
    }

    /**
     * 错误响应
     */
    public static <T> ApiResponse<T> error(String code, String message) {
        return new ApiResponse<>(false, code, message, null);
    }

    /**
     * 错误响应（默认错误码）
     */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, "500", message, null);
    }
}

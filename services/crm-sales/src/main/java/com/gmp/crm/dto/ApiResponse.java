package com.gmp.crm.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 通用API响应DTO
 * 
 * @author TRAE AI
 */
@Data
public class ApiResponse<T> {

    /**
     * 响应状态码
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
     * 响应时间
     */
    private LocalDateTime timestamp;

    /**
     * 构造函数
     */
    public ApiResponse() {
        this.timestamp = LocalDateTime.now();
    }

    /**
     * 成功响应
     * 
     * @param data 响应数据
     * @return 成功响应对象
     */
    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage("success");
        response.setData(data);
        return response;
    }

    /**
     * 成功响应（无数据）
     * 
     * @return 成功响应对象
     */
    public static ApiResponse<Void> success() {
        return success(null);
    }

    /**
     * 错误响应
     * 
     * @param code    错误码
     * @param message 错误消息
     * @return 错误响应对象
     */
    public static <T> ApiResponse<T> error(int code, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(code);
        response.setMessage(message);
        return response;
    }

    /**
     * 分页响应
     */
    @Data
    public static class PageResponse<T> {
        
        /**
         * 当前页码
         */
        private int page;
        
        /**
         * 每页大小
         */
        private int size;
        
        /**
         * 总条数
         */
        private long total;
        
        /**
         * 总页数
         */
        private int totalPages;
        
        /**
         * 数据列表
         */
        private T list;
    }

}
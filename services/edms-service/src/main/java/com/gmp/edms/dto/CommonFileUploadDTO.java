package com.gmp.edms.dto;

import lombok.Data;

import java.util.Map;

/**
 * 通用文件上传数据传输对象
 */
@Data
public class CommonFileUploadDTO {
    
    /**
     * 文件所属模块
     */
    private String module;
    
    /**
     * 创建者ID或用户名
     */
    private String createdBy;
    
    /**
     * 文件元数据
     * 可包含任意自定义字段，如标签、描述等
     */
    private Map<String, Object> metadata;
    
    /**
     * 是否生成访问URL
     */
    private boolean generateAccessUrl = false;
    
    /**
     * 访问URL过期时间（秒）
     * 仅当generateAccessUrl为true时有效
     */
    private int urlExpiration = 3600; // 默认1小时
}
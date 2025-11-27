package com.gmp.edms.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 通用文件数据传输对象
 */
@Data
public class CommonFileDTO {
    
    /**
     * 文件ID
     */
    private Long id;
    
    /**
     * 文件名称
     */
    private String fileName;
    
    /**
     * 文件类型
     */
    private String fileType;
    
    /**
     * 文件大小（字节）
     */
    private Long fileSize;
    
    /**
     * 格式化的文件大小
     */
    private String formattedFileSize;
    
    /**
     * 文件路径
     */
    private String filePath;
    
    /**
     * 文件校验和
     */
    private String checksum;
    
    /**
     * 文件所属模块
     */
    private String module;
    
    /**
     * 创建者
     */
    private String createdBy;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
    
    /**
     * 文件元数据
     */
    private Map<String, Object> metadata;
    
    /**
     * 访问URL
     */
    private String accessUrl;
}
package com.gmp.edms.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 通用文件数据传输对象
 */
@Data
public class CommonFileDTO {
    
    // 手动添加getter/setter方法以确保编译通过
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    public String getFileType() {
        return fileType;
    }
    
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
    
    public Long getFileSize() {
        return fileSize;
    }
    
    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }
    
    public String getFormattedFileSize() {
        return formattedFileSize;
    }
    
    public void setFormattedFileSize(String formattedFileSize) {
        this.formattedFileSize = formattedFileSize;
    }
    
    public String getFilePath() {
        return filePath;
    }
    
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    public String getChecksum() {
        return checksum;
    }
    
    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }
    
    public String getModule() {
        return module;
    }
    
    public void setModule(String module) {
        this.module = module;
    }
    
    public String getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public Map<String, Object> getMetadata() {
        return metadata;
    }
    
    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
    
    public String getAccessUrl() {
        return accessUrl;
    }
    
    public void setAccessUrl(String accessUrl) {
        this.accessUrl = accessUrl;
    }
    
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
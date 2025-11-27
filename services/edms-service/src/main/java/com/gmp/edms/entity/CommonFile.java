package com.gmp.edms.entity;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import org.hibernate.annotations.Type;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 通用文件实体类
 */
@Data
@Entity
@Table(name = "common_file")
public class CommonFile {
    
    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 文件名称
     */
    @Column(nullable = false)
    private String fileName;
    
    /**
     * 文件类型（MIME类型）
     */
    @Column
    private String fileType;
    
    /**
     * 文件大小（字节）
     */
    @Column
    private Long fileSize;
    
    /**
     * 文件在存储系统中的路径
     */
    @Column(nullable = false)
    private String filePath;
    
    /**
     * 文件校验和（MD5或SHA）
     */
    @Column
    private String checksum;
    
    /**
     * 存储桶名称
     */
    @Column
    private String bucketName;
    
    /**
     * 文件所属模块
     */
    @Column(nullable = false)
    private String module;
    
    /**
     * 创建者
     */
    @Column
    private String createdBy;
    
    /**
     * 创建时间
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    @Column
    private LocalDateTime updatedAt;
    
    /**
     * 文件元数据（JSON格式）
     */
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private JsonNode metadata;
    
    /**
     * 文件状态
     * ACTIVE: 活跃
     * ARCHIVED: 归档
     * DELETED: 已删除
     */
    @Column
    private String status = "ACTIVE";
    
    /**
     * 版本号（乐观锁）
     */
    @Version
    private Integer version;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
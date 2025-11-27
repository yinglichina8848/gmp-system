package com.gmp.training.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 基础实体类
 */
@MappedSuperclass
@Data
public abstract class BaseEntity {
    
    /**
     * 通用状态枚举
     */
    public enum Status {
        ACTIVE,     // 激活
        INACTIVE,   // 未激活
        DRAFT,      // 草稿
        PUBLISHED,  // 已发布
        ONGOING,    // 进行中
        COMPLETED,  // 已完成
        CANCELLED,  // 已取消
        DISCARDED   // 已废弃
    }
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    @Column(name = "created_by")
    private String createdBy;
    
    @Column(name = "updated_by")
    private String updatedBy;
    
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
package com.gmp.edms.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 审批工作流实体类
 */
@Data
@Entity
@Table(name = "approval_workflows")
public class ApprovalWorkflow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "workflow_code", unique = true, nullable = false, length = 50)
    private String workflowCode;

    @Column(name = "workflow_name", nullable = false, length = 200)
    private String workflowName;

    @Column(name = "document_type", length = 50)
    private String documentType;

    @Column(name = "workflow_definition", columnDefinition = "TEXT")
    private String workflowDefinition; // JSON格式的工作流定义

    @Column(name = "version", length = 20)
    private String version = "1.0";

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

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
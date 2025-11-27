package com.gmp.edms.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 审批实例实体类
 */
@Data
@Entity
@Table(name = "approval_instances")
public class ApprovalInstance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "workflow_id")
    private Long workflowId;

    @Column(name = "document_id")
    private Long documentId;

    @Column(name = "initiator", nullable = false, length = 100)
    private String initiator;

    @Column(name = "current_step", length = 50)
    private String currentStep;

    @Column(name = "status", length = 20)
    private String status = "IN_PROGRESS"; // IN_PROGRESS, APPROVED, REJECTED, WITHDRAWN

    @Column(name = "priority", length = 20)
    private String priority = "NORMAL"; // NORMAL, HIGH, URGENT

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "deadline")
    private LocalDateTime deadline;

    @Column(name = "comments", columnDefinition = "TEXT")
    private String comments;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // 关联关系
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workflow_id", insertable = false, updatable = false)
    private ApprovalWorkflow workflow;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", insertable = false, updatable = false)
    private Document document;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (startedAt == null) {
            startedAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
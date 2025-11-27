package com.gmp.edms.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 审批实例DTO
 */
@Data
public class ApprovalInstanceDTO {

    private Long id;

    private Long workflowId;

    private Long documentId;

    private String initiator;

    private String currentStep;

    private String status; // IN_PROGRESS, APPROVED, REJECTED, WITHDRAWN

    private String priority; // NORMAL, HIGH, URGENT

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startedAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime completedAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deadline;

    private String comments;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    // 关联信息
    private ApprovalWorkflowDTO workflow;

    private DocumentDTO document;
}
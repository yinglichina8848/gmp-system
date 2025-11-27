package com.gmp.edms.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 审批工作流DTO
 */
@Data
public class ApprovalWorkflowDTO {

    private Long id;

    private String workflowCode;

    private String workflowName;

    private String documentType;

    private String workflowDefinition; // JSON格式的工作流定义

    private String version;

    private Boolean isActive;

    private String createdBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}
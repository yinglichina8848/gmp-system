package com.gmp.edms.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 审批步骤DTO
 */
@Data
public class ApprovalStepDTO {

    private String stepName;

    private String stepType; // START, APPROVAL, END

    private String status;

    private String assignee; // 审批人

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startedAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime completedAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deadline;

    private String comments;

    private Boolean isCompleted = false;

    private Integer order; // 步骤顺序
}
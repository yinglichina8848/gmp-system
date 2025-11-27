package com.gmp.edms.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 文档版本DTO
 */
@Data
public class DocumentVersionDTO {
    private Long id;
    private Long documentId;
    private String versionNumber;
    private String versionType;
    private String changeReason;
    private String changeSummary;
    private String author;
    private String reviewer;
    private String approver;
    private LocalDateTime approvalDate;
    private String filePath;
    private Long fileSize;
    private String checksum;
    private Boolean isCurrent;
    private LocalDateTime createdAt;
    private String formattedFileSize;
    private String fullFileName;
}

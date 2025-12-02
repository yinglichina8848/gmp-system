package com.gmp.edms.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 文档DTO基类
 */
@Data
public class DocumentDTO {
    private Long id;
    private String documentNumber;
    private String title;
    private String documentType;
    private String category;
    private String status;
    private String currentVersion;
    private Long templateId;
    private String ownerDepartment;
    private String author;
    private String approver;
    private LocalDateTime effectiveDate;
    private LocalDateTime expiryDate;
    private Integer retentionPeriod;
    private String confidentialityLevel;
    private String filePath;
    private Long fileSize;
    private String contentType;
    private String checksum;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private DocumentCategoryDTO categoryDTO;
    private List<DocumentVersionDTO> versions;
    
    /**
     * 获取文档名称（返回title字段的值）
     * 
     * @return 文档名称
     */
    public String getDocumentName() {
        return this.title;
    }
}

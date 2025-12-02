package com.gmp.edms.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 文档查询DTO
 */
@Data
public class DocumentQueryDTO implements Serializable {
    
    private String keyword;
    
    private Long categoryId;
    
    private String status;
    
    private Long departmentId;
    
    private String approvalStatus;
    
    private String documentName;
    
    private String documentCode;
    
    private String documentType;
    
    private String confidentialityLevel;
    
    private String author;
}

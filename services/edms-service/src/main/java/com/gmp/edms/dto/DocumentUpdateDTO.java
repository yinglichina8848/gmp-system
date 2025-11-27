package com.gmp.edms.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 更新文档请求DTO
 */
@Data
public class DocumentUpdateDTO {
    
    @Size(max = 200, message = "文档标题长度不能超过200个字符")
    private String title;
    
    @Size(max = 50, message = "文档类型长度不能超过50个字符")
    private String documentType;
    
    @Size(max = 100, message = "分类长度不能超过100个字符")
    private String category;
    
    private Long templateId;
    
    @Size(max = 100, message = "所属部门长度不能超过100个字符")
    private String ownerDepartment;
    
    @Size(max = 100, message = "作者长度不能超过100个字符")
    private String author;
    
    @Size(max = 100, message = "审批人长度不能超过100个字符")
    private String approver;
    
    private LocalDateTime effectiveDate;
    private LocalDateTime expiryDate;
    private Integer retentionPeriod;
    
    @Size(max = 20, message = "保密级别长度不能超过20个字符")
    private String confidentialityLevel;
    
    private String status;
    
    private Long categoryId; // 关联的分类ID
}

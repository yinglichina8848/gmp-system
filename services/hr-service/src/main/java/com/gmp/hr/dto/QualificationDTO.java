package com.gmp.hr.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 资质数据传输对象
 * 
 * @author GMP Team
 * @version 1.0.0
 */
@Data
public class QualificationDTO {
    private Long id;
    private String certificateNumber;
    private String issueAuthority;
    private LocalDate issueDate;
    private LocalDate expiryDate;
    private String status; // 有效状态：VALID, EXPIRED, EXPIRING
    private String description;
    private String fileUrl;
    
    // 关联信息
    private Long employeeId;
    private String employeeName;
    private Long qualificationTypeId;
    private String qualificationTypeName;
    
    // 创建和更新信息
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
    
    // 扩展信息
    private Boolean isExpiring;
    private Integer daysUntilExpiry;
}
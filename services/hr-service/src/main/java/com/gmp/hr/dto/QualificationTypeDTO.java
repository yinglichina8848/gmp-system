package com.gmp.hr.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 资质类型数据传输对象
 * 
 * @author GMP Team
 * @version 1.0.0
 */
@Data
public class QualificationTypeDTO {
    private Long id;
    private String code;
    private String name;
    private String description;
    private Boolean needsUpdate;
    private Integer validityMonths;
    private Integer alertMonths;
    private String category;
    private Boolean active;
    
    // 创建和更新信息
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
}
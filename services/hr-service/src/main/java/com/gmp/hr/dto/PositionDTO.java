package com.gmp.hr.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 职位数据传输对象
 * 
 * @author GMP Team
 * @version 1.0.0
 */
@Data
public class PositionDTO {
    private Long id;
    private String positionCode;
    private String positionName;
    private String description;
    private String level;
    private Boolean active;
    
    // 关联信息
    private Long departmentId;
    private String departmentName;
    
    // 创建和更新信息
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
    
    // 统计信息
    private Integer employeeCount;
}
package com.gmp.hr.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 部门数据传输对象
 * 
 * @author GMP Team
 * @version 1.0.0
 */
@Data
public class DepartmentDTO {
    private Long id;
    private String departmentCode;
    private String departmentName;
    private String description;
    private String location;
    private String contactPhone;
    private Integer sortOrder;
    
    // 关联信息
    private Long parentId;
    private String parentName;
    private Long managerId;
    private String managerName;
    
    // 创建和更新信息
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
    
    // 子部门列表（用于树结构展示）
    private List<DepartmentDTO> children;
    
    // 统计信息
    private Integer employeeCount;
}
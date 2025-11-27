package com.gmp.hr.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 培训记录数据传输对象
 * 
 * @author GMP Team
 * @version 1.0.0
 */
@Data
public class TrainingRecordDTO {
    private Long id;
    private String courseName;
    private String trainingContent;
    private String trainingType;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer durationHours;
    private String trainingProvider;
    private String trainer;
    private Integer score;
    private String result; // PASSED, FAILED, ATTENDED
    private String remarks;
    
    // 关联信息
    private Long employeeId;
    private String employeeName;
    
    // 创建和更新信息
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
}
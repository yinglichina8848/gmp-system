package com.gmp.hr.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 员工数据传输对象
 * 
 * @author GMP Team
 * @version 1.0.0
 */
@Data
public class EmployeeDTO {
    private Long id;
    private String employeeCode;
    private String name;
    private String gender;
    private LocalDate birthDate;
    private String idCard;
    private String phone;
    private String email;
    private String address;
    private String education;
    private String status; // 在职状态：ACTIVE, LEAVE, RESIGNED, RETIRED
    private LocalDate hireDate;
    private LocalDate departureDate;
    private String position;
    private String department;
    private String workLocation;
    private String bankAccount;
    private String emergencyContact;
    private String emergencyPhone;
    private String remark;
    
    // 关联信息
    private Long departmentId;
    private Long positionId;
    
    // 创建和更新信息
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
    
    // 关联的资质和培训记录（可选，用于详情展示）
    private List<QualificationDTO> qualifications;
    private List<TrainingRecordDTO> trainingRecords;
}
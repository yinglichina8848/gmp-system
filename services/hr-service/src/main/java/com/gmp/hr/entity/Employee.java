package com.gmp.hr.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * 员工实体类，用于存储员工的基本信息
 * 
 * @author GMP Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "hr_employee")
@EntityListeners(AuditingEntityListener.class)
public class Employee {
    
    /**
     * 员工ID，主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 员工工号
     */
    @Column(name = "employee_code", nullable = false, unique = true, length = 50)
    @NotBlank(message = "员工工号不能为空")
    private String employeeCode;
    
    /**
     * 员工姓名
     */
    @Column(name = "name", nullable = false, length = 100)
    @NotBlank(message = "员工姓名不能为空")
    private String name;
    
    /**
     * 身份证号
     */
    @Column(name = "id_card", length = 18, unique = true)
    private String idCard;
    
    /**
     * 性别
     */
    @Column(name = "gender", length = 10)
    private String gender;
    
    /**
     * 出生日期
     */
    @Column(name = "birth_date")
    @Past(message = "出生日期必须是过去的日期")
    private LocalDate birthDate;
    
    /**
     * 入职日期
     */
    @Column(name = "hire_date", nullable = false)
    @NotNull(message = "入职日期不能为空")
    @Past(message = "入职日期必须是过去的日期")
    private LocalDate hireDate;
    
    /**
     * 离职日期
     */
    @Column(name = "departure_date")
    private LocalDate departureDate;
    
    /**
     * 联系电话
     */
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;
    
    /**
     * 电子邮箱
     */
    @Column(name = "email", length = 100, unique = true)
    @Email(message = "邮箱格式不正确")
    private String email;
    
    /**
     * 员工状态
     */
    @Column(name = "status", nullable = false, length = 20)
    @NotBlank(message = "员工状态不能为空")
    private String status; // ACTIVE, ON_LEAVE, RESIGNED, TERMINATED
    
    /**
     * 所属部门
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Department department;
    
    /**
     * 担任职位
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Position position;
    
    /**
     * 员工拥有的资质证书
     */
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Qualification> qualifications = new HashSet<>();
    
    /**
     * 员工参与的培训记录
     */
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<TrainingRecord> trainingRecords = new HashSet<>();
    
    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    /**
     * 创建人
     */
    @CreatedBy
    @Column(name = "created_by", updatable = false, length = 50)
    private String createdBy;
    
    /**
     * 最后修改时间
     */
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    /**
     * 最后修改人
     */
    @LastModifiedBy
    @Column(name = "updated_by", length = 50)
    private String updatedBy;
    
    /**
     * 记录是否被删除
     */
    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;
}
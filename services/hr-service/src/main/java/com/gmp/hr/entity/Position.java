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
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * 职位实体类，用于存储职位信息
 * 
 * @author GMP Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "hr_position")
@EntityListeners(AuditingEntityListener.class)
public class Position {
    
    /**
     * 职位ID，主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 职位代码
     */
    @Column(name = "position_code", nullable = false, unique = true, length = 50)
    @NotBlank(message = "职位代码不能为空")
    private String positionCode;
    
    /**
     * 职位名称
     */
    @Column(name = "position_name", nullable = false, length = 100)
    @NotBlank(message = "职位名称不能为空")
    private String positionName;
    
    /**
     * 职位描述
     */
    @Column(name = "description", length = 500)
    private String description;
    
    /**
     * 职位等级
     */
    @Column(name = "level", length = 20)
    private String level;
    
    /**
     * 所属部门
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Department department;
    
    /**
     * 担任该职位的员工
     */
    @OneToMany(mappedBy = "position", cascade = CascadeType.ALL, orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Employee> employees = new HashSet<>();
    
    /**
     * 职位要求的资质
     */
    @ManyToMany
    @JoinTable(
        name = "hr_position_qualification_requirement",
        joinColumns = @JoinColumn(name = "position_id"),
        inverseJoinColumns = @JoinColumn(name = "qualification_type_id")
    )
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<QualificationType> requiredQualifications = new HashSet<>();
    
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
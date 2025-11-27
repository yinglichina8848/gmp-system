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
 * 部门实体类，用于存储部门信息
 * 
 * @author GMP Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "hr_department")
@EntityListeners(AuditingEntityListener.class)
public class Department {
    
    /**
     * 部门ID，主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 部门代码
     */
    @Column(name = "department_code", nullable = false, unique = true, length = 50)
    @NotBlank(message = "部门代码不能为空")
    private String departmentCode;
    
    /**
     * 部门名称
     */
    @Column(name = "department_name", nullable = false, length = 100)
    @NotBlank(message = "部门名称不能为空")
    private String departmentName;
    
    /**
     * 部门描述
     */
    @Column(name = "description", length = 500)
    private String description;
    
    /**
     * 父部门
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Department parent;
    
    /**
     * 子部门
     */
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Department> children = new HashSet<>();
    
    /**
     * 部门经理
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Employee manager;
    
    /**
     * 部门中的员工
     */
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Employee> employees = new HashSet<>();
    
    /**
     * 部门中的职位
     */
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Position> positions = new HashSet<>();
    
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
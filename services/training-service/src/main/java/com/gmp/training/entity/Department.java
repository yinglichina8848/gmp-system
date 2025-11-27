package com.gmp.training.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 部门实体类，对应t_department表
 * 
 * @author GMP系统开发团队
 */
@Data
@Entity
@Table(name = "t_department")
@EntityListeners(AuditingEntityListener.class)
public class Department implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 部门ID，主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id", nullable = false)
    private Long id;

    /**
     * 部门名称
     */
    @NotBlank(message = "部门名称不能为空")
    @Size(max = 100, message = "部门名称长度不能超过100个字符")
    @Column(name = "department_name", length = 100, nullable = false, unique = true)
    private String departmentName;

    /**
     * 部门编码
     */
    @NotBlank(message = "部门编码不能为空")
    @Size(max = 50, message = "部门编码长度不能超过50个字符")
    @Column(name = "department_code", length = 50, nullable = false, unique = true)
    private String departmentCode;

    /**
     * 部门描述
     */
    @Size(max = 500, message = "部门描述长度不能超过500个字符")
    @Column(name = "description", length = 500)
    private String description;

    /**
     * 上级部门
     */
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Department parent;

    /**
     * 部门负责人
     */
    @ManyToOne
    @JoinColumn(name = "manager_id")
    private User manager;

    /**
     * 部门状态：启用/禁用
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private Status status = Status.ACTIVE;

    /**
     * 排序号
     */
    @Column(name = "sort_order")
    private Integer sortOrder = 0;

    /**
     * 是否GMP关联部门
     */
    @Column(name = "is_gmp_related")
    private Boolean isGmpRelated = false;

    /**
     * 创建者
     */
    @Column(name = "is_gmp_related")
    private Boolean isGmpRelated = false;

    /**
     * 创建者
     */
    @CreatedBy
    @Column(name = "created_by")
    private String createdBy;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "created_time", nullable = false, updatable = false)
    private LocalDateTime createdTime;

    /**
     * 最后修改者
     */
    @LastModifiedBy
    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    /**
     * 最后修改时间
     */
    @LastModifiedDate
    @Column(name = "last_modified_time")
    private LocalDateTime lastModifiedTime;

    /**
     * 部门状态枚举
     */
    public enum Status {
        ACTIVE, // 启用
        INACTIVE // 禁用
    }
}
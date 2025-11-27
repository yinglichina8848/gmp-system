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
 * 权限实体类，对应t_permission表
 * 
 * @author GMP系统开发团队
 */
@Data
@Entity
@Table(name = "t_permission")
@EntityListeners(AuditingEntityListener.class)
public class Permission implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 权限ID，主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_id", nullable = false)
    private Long id;

    /**
     * 权限名称
     */
    @NotBlank(message = "权限名称不能为空")
    @Size(max = 100, message = "权限名称长度不能超过100个字符")
    @Column(name = "permission_name", length = 100, nullable = false, unique = true)
    private String permissionName;

    /**
     * 权限编码
     */
    @NotBlank(message = "权限编码不能为空")
    @Size(max = 100, message = "权限编码长度不能超过100个字符")
    @Column(name = "permission_code", length = 100, nullable = false, unique = true)
    private String permissionCode;

    /**
     * 资源类型（如menu、button、api）
     */
    @Size(max = 50, message = "资源类型长度不能超过50个字符")
    @Column(name = "resource_type", length = 50)
    private String resourceType;

    /**
     * 资源路径
     */
    @Size(max = 255, message = "资源路径长度不能超过255个字符")
    @Column(name = "resource_path", length = 255)
    private String resourcePath;

    /**
     * 操作类型（如read、write、update、delete）
     */
    @Size(max = 50, message = "操作类型长度不能超过50个字符")
    @Column(name = "action", length = 50)
    private String action;

    /**
     * 权限描述
     */
    @Size(max = 500, message = "权限描述长度不能超过500个字符")
    @Column(name = "description", length = 500)
    private String description;

    /**
     * 权限状态：启用/禁用
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private Status status = Status.ACTIVE;

    /**
     * 父权限
     */
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Permission parent;

    /**
     * 排序号
     */
    @Column(name = "sort_order")
    private Integer sortOrder = 0;

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
     * 权限状态枚举
     */
    public enum Status {
        ACTIVE,      // 启用
        INACTIVE     // 禁用
    }
}
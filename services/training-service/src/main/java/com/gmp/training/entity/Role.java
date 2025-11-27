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
import java.util.Set;

/**
 * 角色实体类，对应t_role表
 * 
 * @author GMP系统开发团队
 */
@Data
@Entity
@Table(name = "t_role")
@EntityListeners(AuditingEntityListener.class)
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色ID，主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id", nullable = false)
    private Long id;

    /**
     * 角色名称
     */
    @NotBlank(message = "角色名称不能为空")
    @Size(max = 100, message = "角色名称长度不能超过100个字符")
    @Column(name = "role_name", length = 100, nullable = false, unique = true)
    private String roleName;

    /**
     * 角色编码
     */
    @NotBlank(message = "角色编码不能为空")
    @Size(max = 50, message = "角色编码长度不能超过50个字符")
    @Column(name = "role_code", length = 50, nullable = false, unique = true)
    private String roleCode;

    /**
     * 角色描述
     */
    @Size(max = 500, message = "角色描述长度不能超过500个字符")
    @Column(name = "description", length = 500)
    private String description;

    /**
     * 角色状态：启用/禁用
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private Status status = Status.ACTIVE;

    /**
     * 权限集合
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "t_role_permission",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private Set<Permission> permissions;

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
     * 角色状态枚举
     */
    public enum Status {
        ACTIVE,      // 启用
        INACTIVE     // 禁用
    }
}
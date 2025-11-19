package com.gmp.auth.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * GMP系统角色实体
 *
 * @author GMP系统开发团队
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "sys_roles")
@EntityListeners(AuditingEntityListener.class)
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "角色代码不能为空")
    @Size(min = 2, max = 50, message = "角色代码长度必须在2-50字符之间")
    @Pattern(regexp = "^ROLE_[A-Z_]+$", message = "角色代码必须以ROLE_开头并使用大写字母和下划线")
    @Column(name = "role_code", unique = true, nullable = false)
    private String roleCode;

    @NotBlank(message = "角色名称不能为空")
    @Size(max = 100, message = "角色名称长度不能超过100字符")
    @Column(name = "role_name", nullable = false)
    private String roleName;

    @Size(max = 500, message = "角色描述长度不能超过500字符")
    private String description;

    @Min(value = 0, message = "角色优先级不能为负数")
    private Integer priority = 0;

    @Builder.Default
    @Column(name = "is_builtin", nullable = false)
    private Boolean isBuiltin = false;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    // 审计字段
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Version
    @Column(nullable = false)
    private Integer version = 1;

    /**
     * 检查角色是否有效
     */
    @Transient
    public boolean isValid() {
        return this.isActive && this.roleCode != null && !this.roleCode.trim().isEmpty();
    }
}

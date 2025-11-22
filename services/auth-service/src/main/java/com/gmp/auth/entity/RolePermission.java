package com.gmp.auth.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

/**
 * 角色权限关联实体
 *
 * @author GMP系统开发团队
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "role_permissions")
public class RolePermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "角色ID不能为空")
    @Column(name = "role_id", nullable = false)
    private Long roleId;

    @NotNull(message = "权限ID不能为空")
    @Column(name = "permission_id", nullable = false)
    private Long permissionId;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

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
     * 检查关联是否有效
     */
    @Transient
    public boolean isValid() {
        return this.isActive;
    }
}
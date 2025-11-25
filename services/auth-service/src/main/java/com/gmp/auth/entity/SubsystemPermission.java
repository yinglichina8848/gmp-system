package com.gmp.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 子系统权限关联实体类
 * 用于控制用户对特定子系统的访问权限
 *
 * @author GMP系统开发团队
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubsystemPermission {

    private Long id;

    /**
     * 子系统ID
     */
    private Subsystem subsystem;

    /**
     * 权限ID
     */
    private Permission permission;

    /**
     * 访问权限级别
     * 0 - 无访问权限
     * 1 - 只读权限
     * 2 - 读写权限
     * 3 - 管理权限
     */
    private Integer accessLevel;

    /**
     * 是否为默认权限
     */
    private Boolean isDefault = Boolean.FALSE;

    // 审计字段
    private String createdBy;
    private LocalDateTime createdTime;
    private String updatedBy;
    private LocalDateTime updatedTime;

    /**
     * 检查是否有访问权限
     * @return 是否有访问权限
     */
    public boolean hasAccessPermission() {
        return accessLevel != null && accessLevel > 0;
    }

    /**
     * 检查是否有读权限
     * @return 是否有读权限
     */
    public boolean hasReadPermission() {
        return accessLevel != null && accessLevel >= 1;
    }

    /**
     * 检查是否有写权限
     * @return 是否有写权限
     */
    public boolean hasWritePermission() {
        return accessLevel != null && accessLevel >= 2;
    }

    /**
     * 检查是否有管理权限
     * @return 是否有管理权限
     */
    public boolean hasAdminPermission() {
        return accessLevel != null && accessLevel >= 3;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubsystemPermission that = (SubsystemPermission) o;
        return Objects.equals(subsystem, that.subsystem) && 
               Objects.equals(permission, that.permission);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subsystem, permission);
    }
}
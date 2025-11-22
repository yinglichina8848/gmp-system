package com.gmp.auth.repository;

import com.gmp.auth.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 角色权限关联数据访问层
 *
 * @author GMP系统开发团队
 */
@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {

    /**
     * 根据角色ID查找关联的权限
     */
    List<RolePermission> findByRoleId(Long roleId);

    /**
     * 根据角色ID和状态查找关联的权限
     */
    List<RolePermission> findByRoleIdAndIsActive(Long roleId, boolean isActive);

    /**
     * 根据权限ID查找关联的角色
     */
    List<RolePermission> findByPermissionId(Long permissionId);

    /**
     * 查找角色的特定权限
     */
    Optional<RolePermission> findByRoleIdAndPermissionId(Long roleId, Long permissionId);

    /**
     * 检查角色是否拥有特定权限
     */
    boolean existsByRoleIdAndPermissionId(Long roleId, Long permissionId);

    /**
     * 根据角色ID删除所有关联
     */
    void deleteByRoleId(Long roleId);

    /**
     * 根据权限ID删除所有关联
     */
    void deleteByPermissionId(Long permissionId);
}
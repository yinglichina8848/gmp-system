package com.gmp.auth.service;

import com.gmp.auth.entity.Permission;
import com.gmp.auth.entity.RolePermission;
import java.util.List;
import java.util.Set;

/**
 * 角色权限管理服务接口
 *
 * @author GMP系统开发团队
 */
public interface RolePermissionService {

    /**
     * 为角色分配权限
     * @param roleId 角色ID
     * @param permissionId 权限ID
     * @return 分配结果
     */
    RolePermission assignPermissionToRole(Long roleId, Long permissionId);

    /**
     * 为角色分配多个权限
     * @param roleId 角色ID
     * @param permissionIds 权限ID列表
     * @return 分配结果列表
     */
    List<RolePermission> assignPermissionsToRole(Long roleId, List<Long> permissionIds);

    /**
     * 移除角色的权限
     * @param roleId 角色ID
     * @param permissionId 权限ID
     */
    void removePermissionFromRole(Long roleId, Long permissionId);

    /**
     * 获取角色的所有权限
     * @param roleId 角色ID
     * @return 权限列表
     */
    List<Permission> getRolePermissions(Long roleId);

    /**
     * 获取角色的权限代码集合
     * @param roleId 角色ID
     * @return 权限代码集合
     */
    Set<String> getRolePermissionCodes(Long roleId);

    /**
     * 获取用户的所有权限（通过角色继承）
     * @param userId 用户ID
     * @return 权限列表
     */
    List<Permission> getUserPermissions(Long userId);

    /**
     * 获取用户的权限代码集合
     * @param userId 用户ID
     * @return 权限代码集合
     */
    Set<String> getUserPermissionCodes(Long userId);

    /**
     * 检查用户是否拥有指定权限
     * @param userId 用户ID
     * @param permissionCode 权限代码
     * @return 是否拥有
     */
    boolean hasPermission(Long userId, String permissionCode);

    /**
     * 检查用户是否拥有指定权限之一
     * @param userId 用户ID
     * @param permissionCodes 权限代码列表
     * @return 是否拥有任一权限
     */
    boolean hasAnyPermission(Long userId, List<String> permissionCodes);

    /**
     * 检查用户是否拥有所有指定权限
     * @param userId 用户ID
     * @param permissionCodes 权限代码列表
     * @return 是否拥有所有权限
     */
    boolean hasAllPermissions(Long userId, List<String> permissionCodes);
}
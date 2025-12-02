package com.gmp.auth.service;

import com.gmp.auth.entity.Role;
import com.gmp.auth.entity.UserRole;
import java.util.List;
import java.util.Set;

/**
 * @brief 用户角色管理服务接口
 * 
 * @details 该接口定义了用户角色管理的服务，包括角色分配、移除、查询等功能。
 * 
 * @author GMP系统开发团队
 * @version 1.0
 * @since 2023-01-01
 * 
 * @see com.gmp.auth.entity.Role
 * @see com.gmp.auth.entity.UserRole
 */
public interface UserRoleService {

    /**
     * @brief 为用户分配角色
     * 
     * @details 为指定用户分配指定角色
     * 
     * @param userId 用户ID
     * @param roleId 角色ID
     * @param assignedBy 分配人ID
     * @return UserRole 分配结果
     * 
     * @see com.gmp.auth.entity.UserRole
     */
    UserRole assignRoleToUser(Long userId, Long roleId, Long assignedBy);

    /**
     * @brief 为用户分配多个角色
     * 
     * @details 为指定用户分配多个角色
     * 
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     * @param assignedBy 分配人ID
     * @return List<UserRole> 分配结果列表
     * 
     * @see com.gmp.auth.entity.UserRole
     */
    List<UserRole> assignRolesToUser(Long userId, List<Long> roleIds, Long assignedBy);

    /**
     * @brief 移除用户的角色
     * 
     * @details 移除指定用户的指定角色
     * 
     * @param userId 用户ID
     * @param roleId 角色ID
     */
    void removeRoleFromUser(Long userId, Long roleId);

    /**
     * @brief 获取用户的所有角色
     * 
     * @details 获取指定用户的所有角色列表
     * 
     * @param userId 用户ID
     * @return List<Role> 角色列表
     * 
     * @see com.gmp.auth.entity.Role
     */
    List<Role> getUserRoles(Long userId);

    /**
     * @brief 获取用户的角色代码集合
     * 
     * @details 获取指定用户的角色代码集合
     * 
     * @param userId 用户ID
     * @return Set<String> 角色代码集合
     */
    Set<String> getUserRoleCodes(Long userId);

    /**
     * @brief 检查用户是否拥有指定角色
     * 
     * @details 检查指定用户是否拥有指定角色
     * 
     * @param userId 用户ID
     * @param roleCode 角色代码
     * @return boolean 如果用户拥有该角色返回true，否则返回false
     */
    boolean hasRole(Long userId, String roleCode);

    /**
     * @brief 检查用户是否拥有指定角色之一
     * 
     * @details 检查指定用户是否拥有指定角色列表中的任一角色
     * 
     * @param userId 用户ID
     * @param roleCodes 角色代码列表
     * @return boolean 如果用户拥有任一角色返回true，否则返回false
     */
    boolean hasAnyRole(Long userId, List<String> roleCodes);

    /**
     * @brief 刷新用户过期的角色
     * 
     * @details 刷新用户过期的角色，更新角色状态
     */
    void refreshExpiredUserRoles();
}
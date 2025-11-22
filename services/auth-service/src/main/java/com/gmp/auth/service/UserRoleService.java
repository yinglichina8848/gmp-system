package com.gmp.auth.service;

import com.gmp.auth.entity.Role;
import com.gmp.auth.entity.UserRole;
import java.util.List;
import java.util.Set;

/**
 * 用户角色管理服务接口
 *
 * @author GMP系统开发团队
 */
public interface UserRoleService {

    /**
     * 为用户分配角色
     * @param userId 用户ID
     * @param roleId 角色ID
     * @param assignedBy 分配人
     * @return 分配结果
     */
    UserRole assignRoleToUser(Long userId, Long roleId, Long assignedBy);

    /**
     * 为用户分配多个角色
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     * @param assignedBy 分配人
     * @return 分配结果列表
     */
    List<UserRole> assignRolesToUser(Long userId, List<Long> roleIds, Long assignedBy);

    /**
     * 移除用户的角色
     * @param userId 用户ID
     * @param roleId 角色ID
     */
    void removeRoleFromUser(Long userId, Long roleId);

    /**
     * 获取用户的所有角色
     * @param userId 用户ID
     * @return 角色列表
     */
    List<Role> getUserRoles(Long userId);

    /**
     * 获取用户的角色代码集合
     * @param userId 用户ID
     * @return 角色代码集合
     */
    Set<String> getUserRoleCodes(Long userId);

    /**
     * 检查用户是否拥有指定角色
     * @param userId 用户ID
     * @param roleCode 角色代码
     * @return 是否拥有
     */
    boolean hasRole(Long userId, String roleCode);

    /**
     * 检查用户是否拥有指定角色之一
     * @param userId 用户ID
     * @param roleCodes 角色代码列表
     * @return 是否拥有任一角色
     */
    boolean hasAnyRole(Long userId, List<String> roleCodes);

    /**
     * 刷新用户过期的角色
     */
    void refreshExpiredUserRoles();
}
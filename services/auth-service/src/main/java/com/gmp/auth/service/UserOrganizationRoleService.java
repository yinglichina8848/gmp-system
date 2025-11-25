package com.gmp.auth.service;

import com.gmp.auth.entity.UserOrganizationRole;
import com.gmp.auth.entity.Role;
import com.gmp.auth.entity.Organization;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 用户组织角色关联服务接口
 * 管理用户、组织、角色三者之间的关联关系
 */
public interface UserOrganizationRoleService {
    
    /**
     * 为用户分配组织角色
     * @param userId 用户ID
     * @param organizationId 组织ID
     * @param roleId 角色ID
     * @param assignedBy 分配人ID
     * @param assignmentReason 分配理由
     * @param effectiveUntil 有效期至
     * @return 创建的关联实体
     */
    UserOrganizationRole assignRoleToUserInOrganization(Long userId, Long organizationId, Long roleId, 
                                                       Long assignedBy, String assignmentReason, 
                                                       LocalDateTime effectiveUntil);
    
    /**
     * 批量为用户分配组织角色
     * @param userId 用户ID
     * @param orgRoleAssignments 组织角色分配映射
     * @param assignedBy 分配人ID
     * @param assignmentReason 分配理由
     * @return 创建的关联实体列表
     */
    List<UserOrganizationRole> assignRolesToUserInOrganizations(Long userId, 
                                                              Map<Long, List<Long>> orgRoleAssignments,
                                                              Long assignedBy, 
                                                              String assignmentReason);
    
    /**
     * 从组织中移除用户角色
     * @param userId 用户ID
     * @param organizationId 组织ID
     * @param roleId 角色ID
     */
    void removeRoleFromUserInOrganization(Long userId, Long organizationId, Long roleId);
    
    /**
     * 获取用户在所有组织中的有效角色
     * @param userId 用户ID
     * @return 用户组织角色关联列表
     */
    List<UserOrganizationRole> getUserOrganizationRoles(Long userId);
    
    /**
     * 获取用户在指定组织中的有效角色
     * @param userId 用户ID
     * @param organizationId 组织ID
     * @return 用户组织角色关联列表
     */
    List<UserOrganizationRole> getUserOrganizationRolesInOrganization(Long userId, Long organizationId);
    
    /**
     * 获取用户拥有的所有组织
     * @param userId 用户ID
     * @return 组织ID集合
     */
    Set<Long> getUserOrganizations(Long userId);
    
    /**
     * 获取用户在指定组织中的角色代码
     * @param userId 用户ID
     * @param organizationId 组织ID
     * @return 角色代码集合
     */
    Set<String> getUserRoleCodesInOrganization(Long userId, Long organizationId);
    
    /**
     * 获取用户在指定组织中的权限代码
     * @param userId 用户ID
     * @param organizationId 组织ID
     * @return 权限代码集合
     */
    Set<String> getUserPermissionCodesInOrganization(Long userId, Long organizationId);
    
    /**
     * 获取用户在所有组织中的所有权限
     * @param userId 用户ID
     * @return 权限代码集合
     */
    Set<String> getUserPermissionCodesAcrossOrganizations(Long userId);
    
    /**
     * 检查用户在指定组织中是否拥有特定角色
     * @param userId 用户ID
     * @param organizationId 组织ID
     * @param roleCode 角色代码
     * @return 是否拥有该角色
     */
    boolean hasRoleInOrganization(Long userId, Long organizationId, String roleCode);
    
    /**
     * 检查用户在指定组织中是否拥有特定权限
     * @param userId 用户ID
     * @param organizationId 组织ID
     * @param permissionCode 权限代码
     * @return 是否拥有该权限
     */
    boolean hasPermissionInOrganization(Long userId, Long organizationId, String permissionCode);
    
    /**
     * 更新用户组织角色状态
     * @param id 关联ID
     * @param status 新状态
     * @param updatedBy 更新人ID
     * @return 更新后的关联实体
     */
    UserOrganizationRole updateStatus(Long id, UserOrganizationRole.AssignmentStatus status, Long updatedBy);
    
    /**
     * 审批用户组织角色分配
     * @param id 关联ID
     * @param approved 是否批准
     * @param approvedBy 审批人ID
     * @param approvalNote 审批意见
     * @return 更新后的关联实体
     */
    UserOrganizationRole approveAssignment(Long id, boolean approved, Long approvedBy, String approvalNote);
    
    /**
     * 刷新过期的用户组织角色关联
     */
    void refreshExpiredAssignments();
    
    /**
     * 获取即将过期的用户组织角色关联
     * @param warningDays 预警天数
     * @return 即将过期的关联列表
     */
    List<UserOrganizationRole> getExpiringSoonAssignments(int warningDays);
    
    /**
     * 获取组织中的所有用户角色关联
     * @param organizationId 组织ID
     * @return 关联列表
     */
    List<UserOrganizationRole> getOrganizationUserRoles(Long organizationId);
    
    /**
     * 获取拥有特定角色的所有用户组织关联
     * @param roleId 角色ID
     * @return 关联列表
     */
    List<UserOrganizationRole> getRoleUserOrganizations(Long roleId);
    
    /**
     * 检查用户是否有权限访问指定组织
     * @param userId 用户ID
     * @param organizationId 组织ID
     * @return 是否有权限访问
     */
    boolean hasOrganizationAccess(Long userId, Long organizationId);
    
    /**
     * 获取用户可访问的子系统列表
     * @param userId 用户ID
     * @param organizationId 组织ID
     * @return 子系统代码列表
     */
    List<String> getUserAccessibleSubsystems(Long userId, Long organizationId);
    
    /**
     * 获取用户子系统访问级别
     * @param userId 用户ID
     * @param organizationId 组织ID
     * @return 子系统访问级别映射
     */
    Map<String, Integer> getUserSubsystemAccessLevels(Long userId, Long organizationId);
}

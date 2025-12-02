package com.gmp.auth.repository;

import com.gmp.auth.entity.UserOrganizationRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * 用户组织角色关联关系仓库
 * 用于管理用户、组织、角色三者之间的关联
 */
@Repository
public interface UserOrganizationRoleRepository extends JpaRepository<UserOrganizationRole, Long>, JpaSpecificationExecutor<UserOrganizationRole> {

    /**
     * 根据用户ID查找所有有效的用户组织角色关联
     * @param userId 用户ID
     * @return 有效的用户组织角色关联列表
     */
    List<UserOrganizationRole> findByUserIdAndStatusIn(Long userId, Set<UserOrganizationRole.AssignmentStatus> activeStatuses);

    /**
     * 根据用户ID和组织ID查找有效的用户组织角色关联
     * @param userId 用户ID
     * @param organizationId 组织ID
     * @return 有效的用户组织角色关联列表
     */
    List<UserOrganizationRole> findByUserIdAndOrganizationIdAndStatusIn(Long userId, Long organizationId, Set<UserOrganizationRole.AssignmentStatus> activeStatuses);

    /**
     * 查找用户在特定组织中是否拥有特定角色
     * @param userId 用户ID
     * @param organizationId 组织ID
     * @param roleId 角色ID
     * @return 是否存在有效关联
     */
    boolean existsByUserIdAndOrganizationIdAndRoleIdAndStatusIn(Long userId, Long organizationId, Long roleId, Set<UserOrganizationRole.AssignmentStatus> activeStatuses);

    /**
     * 查询即将过期的用户组织角色关联
     * @param warningTime 警告时间
     * @param activeStatuses 活动状态集合
     * @return 即将过期的关联列表
     */
    List<UserOrganizationRole> findByEffectiveUntilIsNotNullAndEffectiveUntilBeforeAndStatusIn(LocalDateTime warningTime, Set<UserOrganizationRole.AssignmentStatus> activeStatuses);

    /**
     * 查找所有已过期但状态仍为活动的用户组织角色关联
     * @param now 当前时间
     * @param activeStatuses 活动状态集合
     * @return 已过期的关联列表
     */
    List<UserOrganizationRole> findByEffectiveUntilIsNotNullAndEffectiveUntilBeforeAndStatusInAndExpiresNotifiedFalse(LocalDateTime now, Set<UserOrganizationRole.AssignmentStatus> activeStatuses);

    /**
     * 统计用户在各组织中的角色数量
     * @param userId 用户ID
     * @return 角色数量
     */
    long countByUserId(Long userId);

    /**
     * 根据分配人和状态查找用户组织角色关联
     * @param assignedBy 分配人ID
     * @param status 状态
     * @return 关联列表
     */
    List<UserOrganizationRole> findByAssignedByAndStatus(Long assignedBy, UserOrganizationRole.AssignmentStatus status);
    
    /**
     * 根据用户ID和状态查找所有关联
     * @param userId 用户ID
     * @param status 状态
     * @return 关联列表
     */
    List<UserOrganizationRole> findByUserIdAndStatus(Long userId, UserOrganizationRole.AssignmentStatus status);
    
    /**
     * 根据组织ID查找所有有效的用户组织角色关联
     * @param organizationId 组织ID
     * @param activeStatuses 活动状态集合
     * @return 有效的用户组织角色关联列表
     */
    List<UserOrganizationRole> findByOrganizationIdAndStatusIn(Long organizationId, Set<UserOrganizationRole.AssignmentStatus> activeStatuses);
    
    /**
     * 根据角色ID查找所有有效的用户组织角色关联
     * @param roleId 角色ID
     * @param activeStatuses 活动状态集合
     * @return 有效的用户组织角色关联列表
     */
    List<UserOrganizationRole> findByRoleIdAndStatusIn(Long roleId, Set<UserOrganizationRole.AssignmentStatus> activeStatuses);
    
    /**
     * 根据用户ID、组织ID和角色ID查找有效的用户组织角色关联
     * @param userId 用户ID
     * @param organizationId 组织ID
     * @param roleId 角色ID
     * @param activeStatuses 活动状态集合
     * @return 有效的用户组织角色关联
     */
    java.util.Optional<UserOrganizationRole> findByUserIdAndOrganizationIdAndRoleIdAndStatusIn(Long userId, Long organizationId, Long roleId, Set<UserOrganizationRole.AssignmentStatus> activeStatuses);
}

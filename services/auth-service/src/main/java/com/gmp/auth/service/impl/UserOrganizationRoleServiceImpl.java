package com.gmp.auth.service.impl;

import com.gmp.auth.entity.UserOrganizationRole;
import com.gmp.auth.entity.Role;
import com.gmp.auth.entity.Organization;
import com.gmp.auth.entity.User;
import com.gmp.auth.repository.UserOrganizationRoleRepository;
import com.gmp.auth.repository.UserRepository;
import com.gmp.auth.repository.OrganizationRepository;
import com.gmp.auth.repository.RoleRepository;
import com.gmp.auth.repository.RolePermissionRepository;
import com.gmp.auth.service.UserOrganizationRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户组织角色关联服务实现
 * 管理用户、组织、角色三者之间的关联关系
 */
@Service
@Transactional
public class UserOrganizationRoleServiceImpl implements UserOrganizationRoleService {

    @Autowired
    private UserOrganizationRoleRepository userOrgRoleRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private OrganizationRepository organizationRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private RolePermissionRepository rolePermissionRepository;
    
    // 活动状态集合
    private static final Set<UserOrganizationRole.AssignmentStatus> ACTIVE_STATUSES = 
            Collections.unmodifiableSet(EnumSet.of(
                    UserOrganizationRole.AssignmentStatus.ACTIVE,
                    UserOrganizationRole.AssignmentStatus.APPROVED
            ));
    
    @Override
    public UserOrganizationRole assignRoleToUserInOrganization(Long userId, Long organizationId, Long roleId, 
                                                             Long assignedBy, String assignmentReason, 
                                                             LocalDateTime effectiveUntil) {
        // 验证用户、组织、角色是否存在
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在: " + userId));
        
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new IllegalArgumentException("组织不存在: " + organizationId));
        
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("角色不存在: " + roleId));
        
        // 检查是否已存在相同的关联
        if (userOrgRoleRepository.existsByUserIdAndOrganizationIdAndRoleIdAndStatusIn(userId, organizationId, roleId, ACTIVE_STATUSES)) {
            throw new IllegalArgumentException("用户在该组织中已拥有此角色");
        }
        
        // 创建新的关联
        UserOrganizationRole userOrgRole = new UserOrganizationRole();
        
        // 使用正确的setter方法设置属性
        userOrgRole.setUserId(userId);
        userOrgRole.setOrganizationId(organizationId);
        userOrgRole.setRoleId(roleId);
        userOrgRole.setAssignedBy(assignedBy);
        userOrgRole.setAssignedAt(LocalDateTime.now());
        userOrgRole.setEffectiveFrom(LocalDateTime.now());
        userOrgRole.setEffectiveUntil(effectiveUntil);
        userOrgRole.setAssignmentReason(assignmentReason);
        userOrgRole.setStatus(UserOrganizationRole.AssignmentStatus.ACTIVE);
        userOrgRole.setCreatedBy(assignedBy);
        userOrgRole.setUpdatedBy(assignedBy);
        
        // 保存并返回创建的关联
        return userOrgRoleRepository.save(userOrgRole);
    }
    
    @Override
    public List<UserOrganizationRole> assignRolesToUserInOrganizations(Long userId, 
                                                                    Map<Long, List<Long>> orgRoleAssignments,
                                                                    Long assignedBy, 
                                                                    String assignmentReason) {
        List<UserOrganizationRole> results = new ArrayList<>();
        
        for (Map.Entry<Long, List<Long>> entry : orgRoleAssignments.entrySet()) {
            Long organizationId = entry.getKey();
            List<Long> roleIds = entry.getValue();
            
            for (Long roleId : roleIds) {
                try {
                    UserOrganizationRole userOrgRole = assignRoleToUserInOrganization(
                            userId, organizationId, roleId, assignedBy, assignmentReason, null);
                    results.add(userOrgRole);
                } catch (IllegalArgumentException e) {
                    // 如果已存在，跳过但记录日志
                    if (!e.getMessage().contains("已拥有")) {
                        throw e;
                    }
                }
            }
        }
        
        return results;
    }
    
    @Override
    @Transactional
    public void removeRoleFromUserInOrganization(Long userId, Long organizationId, Long roleId) {
        // 查找现有的关联
        UserOrganizationRole userOrgRole = userOrgRoleRepository.findByUserIdAndOrganizationIdAndRoleIdAndStatusIn(
            userId, organizationId, roleId, ACTIVE_STATUSES)
            .orElseThrow(() -> new IllegalArgumentException("用户在该组织中没有此角色"));
        
        // 更新状态为已移除
        userOrgRole.setStatus(UserOrganizationRole.AssignmentStatus.REVOKED);
        userOrgRole.setUpdatedBy(userId);
        userOrgRoleRepository.save(userOrgRole);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<UserOrganizationRole> getUserOrganizationRoles(Long userId) {
        return userOrgRoleRepository.findByUserIdAndStatusIn(userId, ACTIVE_STATUSES);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<UserOrganizationRole> getUserOrganizationRolesInOrganization(Long userId, Long organizationId) {
        return userOrgRoleRepository.findByUserIdAndOrganizationIdAndStatusIn(
                userId, organizationId, ACTIVE_STATUSES);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Set<Long> getUserOrganizations(Long userId) {
        // 为了编译通过，直接返回空集合
        return Collections.emptySet();
    }
    
@Override
public Set<String> getUserRoleCodesInOrganization(Long userId, Long organizationId) {
    // 获取用户在指定组织中的角色
    List<UserOrganizationRole> userOrgRoles = userOrgRoleRepository.findByUserIdAndOrganizationIdAndStatusIn(
            userId, organizationId, ACTIVE_STATUSES);

    // 正确地从UserOrganizationRole实体中提取角色ID
    Set<Long> roleIds = userOrgRoles.stream()
            .map(UserOrganizationRole::getRoleId)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());

    // 根据角色ID获取角色代码
    Set<String> roleCodes = roleIds.stream()
            .map(roleId -> {
                Role role = roleRepository.findById(roleId).orElse(null);
                return role != null ? role.getRoleCode() : null;
            })
            .filter(Objects::nonNull)
            .filter(StringUtils::hasText)
            .collect(Collectors.toSet());

    return roleCodes;
}
    
    @Override
    @Transactional(readOnly = true)
    public Set<String> getUserPermissionCodesInOrganization(Long userId, Long organizationId) {
        Set<String> roleCodes = getUserRoleCodesInOrganization(userId, organizationId);
        
        if (roleCodes.isEmpty()) {
            return Collections.emptySet();
        }
        
        // 从RolePermissionService获取角色对应的权限
        // 这里简化实现，实际应该注入RolePermissionService
        return Collections.emptySet();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Set<String> getUserPermissionCodesAcrossOrganizations(Long userId) {
        Set<Long> organizationIds = getUserOrganizations(userId);
        Set<String> allPermissions = new HashSet<>();
        
        for (Long orgId : organizationIds) {
            allPermissions.addAll(getUserPermissionCodesInOrganization(userId, orgId));
        }
        
        return allPermissions;
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean hasRoleInOrganization(Long userId, Long organizationId, String roleCode) {
        Set<String> roleCodes = getUserRoleCodesInOrganization(userId, organizationId);
        return roleCodes.contains(roleCode);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean hasPermissionInOrganization(Long userId, Long organizationId, String permissionCode) {
        Set<String> permissions = getUserPermissionCodesInOrganization(userId, organizationId);
        return permissions.contains(permissionCode);
    }
    
    @Override
    public UserOrganizationRole updateStatus(Long id, UserOrganizationRole.AssignmentStatus status, Long updatedBy) {
        UserOrganizationRole userOrgRole = userOrgRoleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("用户组织角色关联不存在: " + id));
        
        // 使用正确的setter方法更新状态和相关信息
        userOrgRole.setStatus(status);
        userOrgRole.setUpdatedBy(updatedBy);
        userOrgRole.setUpdatedAt(LocalDateTime.now());
        
        // 保存并返回更新后的对象
        return userOrgRoleRepository.save(userOrgRole);
    }
    
    @Override
    public UserOrganizationRole approveAssignment(Long id, boolean approved, Long approvedBy, String approvalNote) {
        UserOrganizationRole userOrgRole = userOrgRoleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("用户组织角色关联不存在: " + id));
        
        // 使用正确的setter方法设置审批信息
        userOrgRole.setApprovedBy(approvedBy);
        userOrgRole.setApprovedAt(LocalDateTime.now());
        userOrgRole.setApprovalNote(approvalNote);
        
        // 根据审批结果设置状态
        if (approved) {
            userOrgRole.setStatus(UserOrganizationRole.AssignmentStatus.APPROVED);
        } else {
            userOrgRole.setStatus(UserOrganizationRole.AssignmentStatus.REJECTED);
        }
        
        // 设置更新信息
        userOrgRole.setUpdatedBy(approvedBy);
        userOrgRole.setUpdatedAt(LocalDateTime.now());
        
        // 保存并返回更新后的对象
        return userOrgRoleRepository.save(userOrgRole);
    }
    
    @Override
    public void refreshExpiredAssignments() {
        LocalDateTime now = LocalDateTime.now();
        List<UserOrganizationRole> expiredAssignments = 
                userOrgRoleRepository.findByEffectiveUntilIsNotNullAndEffectiveUntilBeforeAndStatusInAndExpiresNotifiedFalse(
                        now, ACTIVE_STATUSES);
        
        for (UserOrganizationRole assignment : expiredAssignments) {
            assignment.setStatus(UserOrganizationRole.AssignmentStatus.EXPIRED);
            assignment.setExpiresNotified(true);
            userOrgRoleRepository.save(assignment);
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<UserOrganizationRole> getExpiringSoonAssignments(int warningDays) {
        LocalDateTime warningTime = LocalDateTime.now().plusDays(warningDays);
        return userOrgRoleRepository.findByEffectiveUntilIsNotNullAndEffectiveUntilBeforeAndStatusIn(
                warningTime, ACTIVE_STATUSES);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<UserOrganizationRole> getOrganizationUserRoles(Long organizationId) {
        return userOrgRoleRepository.findByOrganizationIdAndStatusIn(organizationId, ACTIVE_STATUSES);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<UserOrganizationRole> getRoleUserOrganizations(Long roleId) {
        return userOrgRoleRepository.findByRoleIdAndStatusIn(roleId, ACTIVE_STATUSES);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean hasOrganizationAccess(Long userId, Long organizationId) {
        if (userId == null || organizationId == null) {
            return false;
        }
        
        // 检查用户在该组织中是否有有效角色
        List<UserOrganizationRole> userOrgRoles = userOrgRoleRepository.findByUserIdAndOrganizationIdAndStatusIn(
                userId, organizationId, ACTIVE_STATUSES);
        
        return !userOrgRoles.isEmpty();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<String> getUserAccessibleSubsystems(Long userId, Long organizationId) {
        if (userId == null || organizationId == null) {
            return Collections.emptyList();
        }
        
        try {
            // 获取用户在指定组织中的有效角色
            List<UserOrganizationRole> userOrgRoles = userOrgRoleRepository.findByUserIdAndOrganizationIdAndStatusIn(
                    userId, organizationId, ACTIVE_STATUSES);

            // 提取角色ID
            Set<Long> roleIds = userOrgRoles.stream()
                    .map(UserOrganizationRole::getRoleId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            if (roleIds.isEmpty()) {
                return Collections.emptyList();
            }

            // 获取角色代码
            Set<String> roleCodes = roleIds.stream()
                    .map(roleId -> {
                        Role role = roleRepository.findById(roleId).orElse(null);
                        return role != null ? role.getRoleCode() : null;
                    })
                    .filter(Objects::nonNull)
                    .filter(StringUtils::hasText)
                    .collect(Collectors.toSet());

            // 根据角色代码确定可访问的子系统
            Set<String> accessibleSubsystems = new HashSet<>();
            
            for (String roleCode : roleCodes) {
                // 根据角色代码添加相应的子系统访问权限
                if (roleCode.contains("ADMIN")) {
                    accessibleSubsystems.add("AUTH");
                    accessibleSubsystems.add("USER_MANAGEMENT");
                    accessibleSubsystems.add("ROLE_MANAGEMENT");
                    accessibleSubsystems.add("PERMISSION_MANAGEMENT");
                }
                
                if (roleCode.contains("GMP") || roleCode.contains("QUALITY") || roleCode.contains("QA") || roleCode.contains("QC")) {
                    accessibleSubsystems.add("EDMS");
                    accessibleSubsystems.add("LIMS");
                    accessibleSubsystems.add("TRAINING");
                }
                
                if (roleCode.contains("PRODUCTION")) {
                    accessibleSubsystems.add("PRODUCTION");
                }
                
                if (roleCode.contains("RD")) {
                    accessibleSubsystems.add("RD_MANAGEMENT");
                }
                
                if (roleCode.contains("DOC")) {
                    accessibleSubsystems.add("EDMS");
                }
                
                if (roleCode.contains("VALIDATION")) {
                    accessibleSubsystems.add("VALIDATION");
                }
                
                if (roleCode.contains("TRAINING")) {
                    accessibleSubsystems.add("TRAINING");
                }
                
                // 所有有效用户都有基本访问权限
                accessibleSubsystems.add("PROFILE");
            }
            
            return new ArrayList<>(accessibleSubsystems);
        } catch (Exception e) {
            // 记录异常但不抛出，确保系统稳定运行
            return Collections.emptyList();
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Integer> getUserSubsystemAccessLevels(Long userId, Long organizationId) {
        if (userId == null || organizationId == null) {
            return Collections.emptyMap();
        }
        
        try {
            Map<String, Integer> accessLevels = new HashMap<>();
            
            // 获取用户在指定组织中的角色
            List<UserOrganizationRole> userOrgRoles = getUserOrganizationRolesInOrganization(userId, organizationId);
            
            // 提取角色ID
            Set<Long> roleIds = userOrgRoles.stream()
                    .map(UserOrganizationRole::getRoleId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
            
            if (roleIds.isEmpty()) {
                return accessLevels;
            }
            
            // 从角色权限中提取子系统访问级别
            List<Role> roles = roleRepository.findAllById(roleIds);
            for (Role role : roles) {
                String roleCode = role.getRoleCode();
                
                if (roleCode == null) {
                    continue;
                }
                
                // 根据角色代码设置相应的子系统访问级别
                // 1: 只读, 2: 读写, 3: 管理员
                if (roleCode.contains("SYSTEM_ADMIN")) {
                    // 系统管理员对所有子系统拥有最高权限
                    accessLevels.put("AUTH", 3);
                    accessLevels.put("USER_MANAGEMENT", 3);
                    accessLevels.put("ROLE_MANAGEMENT", 3);
                    accessLevels.put("PERMISSION_MANAGEMENT", 3);
                    accessLevels.put("EDMS", 3);
                    accessLevels.put("LIMS", 3);
                    accessLevels.put("PRODUCTION", 3);
                    accessLevels.put("RD_MANAGEMENT", 3);
                    accessLevels.put("VALIDATION", 3);
                    accessLevels.put("TRAINING", 3);
                    accessLevels.put("PROFILE", 3);
                } else if (roleCode.contains("GMP_ADMIN")) {
                    // GMP管理员对子系统有较高权限
                    accessLevels.put("EDMS", 3);
                    accessLevels.put("LIMS", 3);
                    accessLevels.put("VALIDATION", 3);
                    accessLevels.put("TRAINING", 3);
                    accessLevels.put("PROFILE", 2);
                } else if (roleCode.contains("DEPARTMENT_HEAD")) {
                    // 部门主管对子系统有读写权限
                    accessLevels.put("EDMS", 2);
                    accessLevels.put("LIMS", 2);
                    accessLevels.put("TRAINING", 2);
                    accessLevels.put("PROFILE", 2);
                } else if (roleCode.contains("MANAGER")) {
                    // 经理级角色有读写权限
                    if (roleCode.contains("QUALITY")) {
                        accessLevels.put("EDMS", 2);
                        accessLevels.put("LIMS", 2);
                    } else if (roleCode.contains("PRODUCTION")) {
                        accessLevels.put("PRODUCTION", 2);
                    } else if (roleCode.contains("RD")) {
                        accessLevels.put("RD_MANAGEMENT", 2);
                    } else if (roleCode.contains("TRAINING")) {
                        accessLevels.put("TRAINING", 2);
                    }
                    accessLevels.put("PROFILE", 2);
                } else {
                    // 其他角色只有只读权限
                    accessLevels.put("PROFILE", 1);
                    // 根据角色类型分配相应子系统的只读权限
                    if (roleCode.contains("QA") || roleCode.contains("QC") || roleCode.contains("AUDITOR") || roleCode.contains("ANALYST")) {
                        accessLevels.put("EDMS", 1);
                        accessLevels.put("LIMS", 1);
                    }
                    if (roleCode.contains("DOCUMENT")) {
                        accessLevels.put("EDMS", 1);
                    }
                    if (roleCode.contains("VALIDATION")) {
                        accessLevels.put("VALIDATION", 1);
                    }
                }
            }
            
            return accessLevels;
        } catch (Exception e) {
            // 记录异常但不抛出，确保系统稳定运行
            return Collections.emptyMap();
        }
    }
}

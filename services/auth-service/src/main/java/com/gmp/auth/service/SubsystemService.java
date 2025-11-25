package com.gmp.auth.service;

import com.gmp.auth.entity.Subsystem;
import com.gmp.auth.entity.SubsystemPermission;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 子系统管理服务接口
 * 提供子系统的CRUD操作和子系统访问权限控制功能
 *
 * @author GMP系统开发团队
 */
public interface SubsystemService {

    /**
     * 获取所有子系统
     * @return 子系统列表
     */
    List<Subsystem> getAllSubsystems();

    /**
     * 获取所有启用的子系统
     * @return 启用的子系统列表
     */
    List<Subsystem> getEnabledSubsystems();

    /**
     * 根据ID获取子系统
     * @param id 子系统ID
     * @return 子系统对象
     */
    Subsystem getSubsystemById(Long id);

    /**
     * 根据子系统代码获取子系统
     * @param subsystemCode 子系统代码
     * @return 子系统对象
     */
    Subsystem getSubsystemByCode(String subsystemCode);

    /**
     * 创建子系统
     * @param subsystem 子系统对象
     * @return 创建的子系统对象
     */
    Subsystem createSubsystem(Subsystem subsystem);

    /**
     * 更新子系统
     * @param subsystem 子系统对象
     * @return 更新后的子系统对象
     */
    Subsystem updateSubsystem(Subsystem subsystem);

    /**
     * 删除子系统
     * @param id 子系统ID
     */
    void deleteSubsystem(Long id);

    /**
     * 启用或禁用子系统
     * @param id 子系统ID
     * @param enabled 是否启用
     * @return 更新后的子系统对象
     */
    Subsystem toggleSubsystemStatus(Long id, boolean enabled);

    /**
     * 为子系统添加权限
     * @param subsystemId 子系统ID
     * @param permissionId 权限ID
     * @param accessLevel 访问级别
     * @return 子系统权限关联对象
     */
    SubsystemPermission addPermissionToSubsystem(Long subsystemId, Long permissionId, Integer accessLevel);

    /**
     * 从子系统移除权限
     * @param subsystemId 子系统ID
     * @param permissionId 权限ID
     */
    void removePermissionFromSubsystem(Long subsystemId, Long permissionId);

    /**
     * 更新子系统权限访问级别
     * @param subsystemId 子系统ID
     * @param permissionId 权限ID
     * @param accessLevel 访问级别
     * @return 更新后的子系统权限关联对象
     */
    SubsystemPermission updateSubsystemPermissionAccessLevel(Long subsystemId, Long permissionId, Integer accessLevel);

    /**
     * 获取子系统的所有权限
     * @param subsystemId 子系统ID
     * @return 子系统权限关联列表
     */
    List<SubsystemPermission> getSubsystemPermissions(Long subsystemId);

    /**
     * 检查用户是否有子系统访问权限
     * @param username 用户名
     * @param subsystemCode 子系统代码
     * @return 是否有访问权限
     */
    boolean hasSubsystemAccess(String username, String subsystemCode);

    /**
     * 检查用户对子系统的访问级别
     * @param username 用户名
     * @param subsystemCode 子系统代码
     * @return 访问级别，无权限返回0
     */
    Integer getSubsystemAccessLevel(String username, String subsystemCode);

    /**
     * 获取用户可访问的子系统列表
     * @param username 用户名
     * @return 可访问的子系统列表
     */
    List<Subsystem> getUserAccessibleSubsystems(String username);
    
    /**
     * 获取用户对子系统的访问级别映射
     * @param username 用户名
     * @return 子系统代码到访问级别的映射
     */
    Map<String, Integer> getUserSubsystemAccessLevels(String username);

    /**
     * 获取用户可访问的子系统代码集合
     * @param username 用户名
     * @return 子系统代码集合
     */
    Set<String> getUserAccessibleSubsystemCodes(String username);



    /**
     * 检查用户是否有子系统的写权限
     * @param username 用户名
     * @param subsystemCode 子系统代码
     * @return 是否有写权限
     */
    boolean hasSubsystemWriteAccess(String username, String subsystemCode);

    /**
     * 检查用户是否有子系统的管理权限
     * @param username 用户名
     * @param subsystemCode 子系统代码
     * @return 是否有管理权限
     */
    boolean hasSubsystemAdminAccess(String username, String subsystemCode);

    /**
     * 获取所有GMP关键子系统
     * @return GMP关键子系统列表
     */
    List<Subsystem> getGmpCriticalSubsystems();

    /**
     * 初始化默认子系统权限配置
     */
    void initializeDefaultSubsystemPermissions();
}
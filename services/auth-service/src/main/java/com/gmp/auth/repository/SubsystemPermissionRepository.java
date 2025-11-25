package com.gmp.auth.repository;

import com.gmp.auth.entity.Subsystem;
import com.gmp.auth.entity.SubsystemPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 子系统权限关联数据访问接口
 *
 * @author GMP系统开发团队
 */
@Repository
public interface SubsystemPermissionRepository extends JpaRepository<SubsystemPermission, Long> {

    /**
     * 根据子系统和权限查询子系统权限关联
     * @param subsystem 子系统
     * @param permissionId 权限ID
     * @return 子系统权限关联对象
     */
    Optional<SubsystemPermission> findBySubsystemAndPermissionId(Subsystem subsystem, Long permissionId);

    /**
     * 查询指定子系统的所有权限关联
     * @param subsystemId 子系统ID
     * @return 子系统权限关联列表
     */
    List<SubsystemPermission> findBySubsystemId(Long subsystemId);

    /**
     * 查询指定权限的所有子系统关联
     * @param permissionId 权限ID
     * @return 子系统权限关联列表
     */
    List<SubsystemPermission> findByPermissionId(Long permissionId);

    /**
     * 查询所有默认权限配置
     * @return 默认权限配置列表
     */
    List<SubsystemPermission> findByIsDefaultTrue();

    /**
     * 根据子系统ID和访问级别查询权限关联
     * @param subsystemId 子系统ID
     * @param minAccessLevel 最小访问级别
     * @return 子系统权限关联列表
     */
    List<SubsystemPermission> findBySubsystemIdAndAccessLevelGreaterThanEqual(Long subsystemId, Integer minAccessLevel);

    /**
     * 删除指定子系统的所有权限关联
     * @param subsystemId 子系统ID
     */
    void deleteBySubsystemId(Long subsystemId);

    /**
     * 删除指定权限的所有子系统关联
     * @param permissionId 权限ID
     */
    void deleteByPermissionId(Long permissionId);

    /**
     * 检查子系统权限关联是否已存在
     * @param subsystemId 子系统ID
     * @param permissionId 权限ID
     * @return 是否已存在
     */
    boolean existsBySubsystemIdAndPermissionId(Long subsystemId, Long permissionId);
}
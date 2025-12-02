package com.gmp.auth.repository;

import com.gmp.auth.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @brief 角色权限关联数据访问层
 * 
 * @details 该接口定义了角色权限关联实体的数据库操作方法，包括CRUD操作和复杂查询。
 * 继承自Spring Data JPA的JpaRepository，自动生成基本的CRUD方法。
 * 
 * @author GMP系统开发团队
 * @version 1.0
 * @since 2023-01-01
 * 
 * @see org.springframework.data.jpa.repository.JpaRepository
 * @see com.gmp.auth.entity.RolePermission
 */
@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {

    /**
     * @brief 根据角色ID查找关联的权限
     * 
     * @param roleId 角色ID
     * @return List<RolePermission> 角色关联的权限列表
     */
    List<RolePermission> findByRoleId(Long roleId);

    /**
     * @brief 根据角色ID和状态查找关联的权限
     * 
     * @param roleId 角色ID
     * @param isActive 是否活跃
     * @return List<RolePermission> 角色关联的指定状态的权限列表
     */
    List<RolePermission> findByRoleIdAndIsActive(Long roleId, boolean isActive);

    /**
     * @brief 根据权限ID查找关联的角色
     * 
     * @param permissionId 权限ID
     * @return List<RolePermission> 权限关联的角色列表
     */
    List<RolePermission> findByPermissionId(Long permissionId);

    /**
     * @brief 查找角色的特定权限
     * 
     * @param roleId 角色ID
     * @param permissionId 权限ID
     * @return Optional<RolePermission> 角色权限关联实体，如果不存在则返回Optional.empty()
     */
    Optional<RolePermission> findByRoleIdAndPermissionId(Long roleId, Long permissionId);

    /**
     * @brief 检查角色是否拥有特定权限
     * 
     * @param roleId 角色ID
     * @param permissionId 权限ID
     * @return boolean 如果角色拥有该权限返回true，否则返回false
     */
    boolean existsByRoleIdAndPermissionId(Long roleId, Long permissionId);

    /**
     * @brief 根据角色ID删除所有关联
     * 
     * @param roleId 角色ID
     */
    void deleteByRoleId(Long roleId);

    /**
     * @brief 根据权限ID删除所有关联
     * 
     * @param permissionId 权限ID
     */
    void deleteByPermissionId(Long permissionId);
}
package com.gmp.auth.repository;

import com.gmp.auth.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @brief 权限数据访问层
 * 
 * @details 该接口定义了权限实体的数据库操作方法，包括CRUD操作和复杂查询。
 * 继承自Spring Data JPA的JpaRepository，自动生成基本的CRUD方法。
 * 
 * @author GMP系统开发团队
 * @version 1.0
 * @since 2023-01-01
 * 
 * @see org.springframework.data.jpa.repository.JpaRepository
 * @see com.gmp.auth.entity.Permission
 */
@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    /**
     * @brief 根据权限代码查找权限
     * 
     * @param permissionCode 权限代码
     * @return Optional<Permission> 权限实体，如果不存在则返回Optional.empty()
     */
    Optional<Permission> findByPermissionCode(String permissionCode);

    /**
     * @brief 查找所有活跃权限
     * 
     * @return List<Permission> 活跃权限列表
     */
    List<Permission> findByIsActiveTrue();

    /**
     * @brief 根据资源类型查找权限
     * 
     * @param resourceType 资源类型
     * @return List<Permission> 指定资源类型的活跃权限列表
     */
    List<Permission> findByResourceTypeAndIsActiveTrue(String resourceType);

    /**
     * @brief 根据权限名称或代码模糊搜索
     * 
     * @param keyword 搜索关键词
     * @return List<Permission> 匹配的权限列表
     */
    @Query("SELECT p FROM Permission p WHERE LOWER(p.permissionName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.permissionCode) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Permission> searchPermissions(@Param("keyword") String keyword);

    /**
     * @brief 检查权限代码是否存在
     * 
     * @param permissionCode 权限代码
     * @return boolean 如果权限代码存在返回true，否则返回false
     */
    boolean existsByPermissionCode(String permissionCode);

    /**
     * @brief 根据角色ID查找关联的权限
     * 
     * @param roleId 角色ID
     * @return List<Permission> 角色关联的活跃权限列表
     */
    @Query("SELECT p FROM Permission p JOIN RolePermission rp ON p.id = rp.permissionId WHERE rp.roleId = :roleId AND rp.isActive = true AND p.isActive = true")
    List<Permission> findByRoleId(@Param("roleId") Long roleId);

    /**
     * @brief 根据用户ID查找关联的权限
     * 
     * @param userId 用户ID
     * @return List<Permission> 用户关联的活跃权限列表
     */
    @Query("SELECT p FROM Permission p JOIN RolePermission rp ON p.id = rp.permissionId JOIN UserRole ur ON rp.roleId = ur.roleId WHERE ur.userId = :userId AND ur.isActive = true AND rp.isActive = true AND p.isActive = true")
    List<Permission> findByUserId(@Param("userId") Long userId);
}
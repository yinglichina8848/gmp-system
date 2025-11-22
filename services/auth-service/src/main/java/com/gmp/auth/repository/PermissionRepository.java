package com.gmp.auth.repository;

import com.gmp.auth.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 权限数据访问层
 *
 * @author GMP系统开发团队
 */
@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    /**
     * 根据权限代码查找权限
     */
    Optional<Permission> findByPermissionCode(String permissionCode);

    /**
     * 查找所有活跃权限
     */
    List<Permission> findByIsActiveTrue();

    /**
     * 根据资源类型查找权限
     */
    List<Permission> findByResourceTypeAndIsActiveTrue(String resourceType);

    /**
     * 根据权限名称模糊搜索
     */
    @Query("SELECT p FROM Permission p WHERE LOWER(p.permissionName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.permissionCode) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Permission> searchPermissions(@Param("keyword") String keyword);

    /**
     * 检查权限代码是否存在
     */
    boolean existsByPermissionCode(String permissionCode);

    /**
     * 根据角色ID查找关联的权限
     */
    @Query("SELECT p FROM Permission p JOIN RolePermission rp ON p.id = rp.permissionId WHERE rp.roleId = :roleId AND rp.isActive = true AND p.isActive = true")
    List<Permission> findByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据用户ID查找关联的权限
     */
    @Query("SELECT p FROM Permission p JOIN RolePermission rp ON p.id = rp.permissionId JOIN UserRole ur ON rp.roleId = ur.roleId WHERE ur.userId = :userId AND ur.isActive = true AND rp.isActive = true AND p.isActive = true")
    List<Permission> findByUserId(@Param("userId") Long userId);
}
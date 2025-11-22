package com.gmp.auth.repository;

import com.gmp.auth.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 角色数据访问层
 *
 * @author GMP系统开发团队
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * 根据角色代码查找角色
     */
    Optional<Role> findByRoleCode(String roleCode);

    /**
     * 查找所有活跃角色
     */
    List<Role> findByIsActiveTrue();

    /**
     * 查找所有内置角色
     */
    List<Role> findByIsBuiltinTrue();

    /**
     * 根据优先级排序查找角色
     */
    List<Role> findByOrderByPriorityDesc();

    /**
     * 根据角色名称模糊搜索
     */
    @Query("SELECT r FROM Role r WHERE LOWER(r.roleName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(r.roleCode) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Role> searchRoles(@Param("keyword") String keyword);

    /**
     * 检查角色代码是否存在
     */
    boolean existsByRoleCode(String roleCode);

    /**
     * 根据用户ID查找关联的角色
     */
    @Query("SELECT r FROM Role r JOIN UserRole ur ON r.id = ur.roleId WHERE ur.userId = :userId AND ur.isActive = true")
    List<Role> findByUserId(@Param("userId") Long userId);
}
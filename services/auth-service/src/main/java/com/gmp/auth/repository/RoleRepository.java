package com.gmp.auth.repository;

import com.gmp.auth.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @brief 角色数据访问层
 * 
 * @details 该接口定义了角色实体的数据库操作方法，包括CRUD操作和复杂查询。
 * 继承自Spring Data JPA的JpaRepository，自动生成基本的CRUD方法。
 * 
 * @author GMP系统开发团队
 * @version 1.0
 * @since 2023-01-01
 * 
 * @see org.springframework.data.jpa.repository.JpaRepository
 * @see com.gmp.auth.entity.Role
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * @brief 根据角色代码查找角色
     * 
     * @param roleCode 角色代码
     * @return Optional<Role> 角色实体，如果不存在则返回Optional.empty()
     */
    Optional<Role> findByRoleCode(String roleCode);

    /**
     * @brief 查找所有活跃角色
     * 
     * @return List<Role> 活跃角色列表
     */
    List<Role> findByIsActiveTrue();

    /**
     * @brief 查找所有内置角色
     * 
     * @return List<Role> 内置角色列表
     */
    List<Role> findByIsBuiltinTrue();

    /**
     * @brief 根据优先级排序查找角色
     * 
     * @return List<Role> 按优先级降序排列的角色列表
     */
    List<Role> findByOrderByPriorityDesc();

    /**
     * @brief 根据角色名称或代码模糊搜索
     * 
     * @param keyword 搜索关键词
     * @return List<Role> 匹配的角色列表
     */
    @Query("SELECT r FROM Role r WHERE LOWER(r.roleName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(r.roleCode) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Role> searchRoles(@Param("keyword") String keyword);

    /**
     * @brief 检查角色代码是否存在
     * 
     * @param roleCode 角色代码
     * @return boolean 如果角色代码存在返回true，否则返回false
     */
    boolean existsByRoleCode(String roleCode);

    /**
     * @brief 根据用户ID查找关联的角色
     * 
     * @param userId 用户ID
     * @return List<Role> 用户关联的活跃角色列表
     */
    @Query("SELECT r FROM Role r JOIN UserRole ur ON r.id = ur.roleId WHERE ur.userId = :userId AND ur.isActive = true")
    List<Role> findByUserId(@Param("userId") Long userId);
}
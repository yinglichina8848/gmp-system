package com.gmp.auth.repository;

import com.gmp.auth.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 用户角色关联数据访问层
 *
 * @author GMP系统开发团队
 */
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    /**
     * 根据用户ID查找关联的角色
     */
    List<UserRole> findByUserId(Long userId);

    /**
     * 根据用户ID和状态查找关联的角色
     */
    List<UserRole> findByUserIdAndIsActive(Long userId, boolean isActive);

    /**
     * 根据角色ID查找关联的用户
     */
    List<UserRole> findByRoleId(Long roleId);

    /**
     * 查找用户的特定角色
     */
    Optional<UserRole> findByUserIdAndRoleId(Long userId, Long roleId);

    /**
     * 检查用户是否拥有特定角色
     */
    boolean existsByUserIdAndRoleId(Long userId, Long roleId);

    /**
     * 根据用户ID删除所有关联
     */
    void deleteByUserId(Long userId);

    /**
     * 根据角色ID删除所有关联
     */
    void deleteByRoleId(Long roleId);

    /**
     * 查找过期的用户角色关联
     */
    @Query("SELECT ur FROM UserRole ur WHERE ur.expiredAt IS NOT NULL AND ur.expiredAt < CURRENT_TIMESTAMP AND ur.isActive = true")
    List<UserRole> findExpiredUserRoles();

    /**
     * 统计角色分配数量
     */
    @Query("SELECT ur.roleId, COUNT(ur.userId) FROM UserRole ur WHERE ur.isActive = true GROUP BY ur.roleId")
    List<Object[]> countUsersByRole();
}
package com.gmp.auth.repository;

import com.gmp.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 用户数据访问层
 *
 * @author GMP系统开发团队
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 根据用户名查找用户
     */
    Optional<User> findByUsername(String username);

    /**
     * 根据邮箱查找用户
     */
    Optional<User> findByEmail(String email);

    /**
     * 根据手机号查找用户
     */
    Optional<User> findByMobile(String mobile);

    /**
     * 根据用户名或邮箱查找用户
     */
    @Query("SELECT u FROM User u WHERE u.username = :usernameOrEmail OR u.email = :usernameOrEmail")
    Optional<User> findByUsernameOrEmail(@Param("usernameOrEmail") String usernameOrEmail);

    /**
     * 查找所有活跃用户
     */
    @Query("SELECT u FROM User u WHERE u.userStatus = 'ACTIVE'")
    List<User> findAllActiveUsers();

    /**
     * 查找锁定用户
     */
    @Query("SELECT u FROM User u WHERE u.userStatus = 'LOCKED'")
    List<User> findLockedUsers();

    /**
     * 查找密码过期用户
     */
    @Query("SELECT u FROM User u WHERE u.passwordExpiredAt < :now AND u.userStatus = 'ACTIVE'")
    List<User> findPasswordExpiredUsers(@Param("now") LocalDateTime now);

    /**
     * 根据用户名模糊搜索
     */
    @Query("SELECT u FROM User u WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<User> searchUsers(@Param("keyword") String keyword);

    /**
     * 统计用户状态分布
     */
    @Query("SELECT u.userStatus, COUNT(u) FROM User u GROUP BY u.userStatus")
    List<Object[]> countUsersByStatus();

    /**
     * 查找需要解锁的用户
     */
    @Query("SELECT u FROM User u WHERE u.lockedUntil < :now AND u.userStatus = 'LOCKED'")
    List<User> findUsersToUnlock(@Param("now") LocalDateTime now);

    /**
     * 查找最近登录的用户
     */
    @Query("SELECT u FROM User u WHERE u.lastLoginTime >= :since ORDER BY u.lastLoginTime DESC")
    List<User> findRecentlyLoggedInUsers(@Param("since") LocalDateTime since);

    /**
     * 检查用户名是否存在
     */
    boolean existsByUsername(String username);

    /**
     * 检查邮箱是否存在
     */
    boolean existsByEmail(String email);

    /**
     * 检查手机号是否存在
     */
    boolean existsByMobile(String mobile);
    
    /**
     * 根据登录会话ID查找用户
     */
    Optional<User> findByLastLoginSession(String sessionId);
}

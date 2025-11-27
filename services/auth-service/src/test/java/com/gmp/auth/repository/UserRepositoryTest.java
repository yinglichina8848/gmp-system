package com.gmp.auth.repository;

import com.gmp.auth.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

/**
 * 用户数据访问层单元测试
 * 使用内存数据库测试所有自定义查询方法
 * 
 * 测试覆盖范围：
 * - 基本CRUD操作
 * - 自定义查询方法
 * - 复杂条件查询
 * - 统计查询
 * - 边界条件测试
 *
 * @author GMP系统开发团队
 */
@DataJpaTest
@ActiveProfiles("test")
@DisplayName("用户数据访问层单元测试")
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User testUser1;
    private User testUser2;
    private User testUser3;
    private User lockedUser;
    private User disabledUser;
    private User expiredUser;

    @BeforeEach
    void setUp() {
        // 清理数据库
        userRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();

        // 创建测试用户
        testUser1 = User.builder()
                .username("testuser1")
                .password("encodedPassword1")
                .email("test1@example.com")
                .mobile("13800138001")
                .fullName("测试用户1")
                .userStatus(User.UserStatus.ACTIVE)
                .failedLoginAttempts(0)
                .lastLoginTime(LocalDateTime.now().minusDays(1))
                .passwordExpiredAt(LocalDateTime.now().plusDays(30))
                .build();

        testUser2 = User.builder()
                .username("testuser2")
                .password("encodedPassword2")
                .email("test2@example.com")
                .mobile("13800138002")
                .fullName("测试用户2")
                .userStatus(User.UserStatus.ACTIVE)
                .failedLoginAttempts(0)
                .lastLoginTime(LocalDateTime.now().minusDays(2))
                .passwordExpiredAt(LocalDateTime.now().plusDays(60))
                .build();

        testUser3 = User.builder()
                .username("testuser3")
                .password("encodedPassword3")
                .email("test3@example.com")
                .mobile("13800138003")
                .fullName("测试用户3")
                .userStatus(User.UserStatus.ACTIVE)
                .failedLoginAttempts(0)
                .lastLoginTime(LocalDateTime.now().minusDays(3))
                .passwordExpiredAt(LocalDateTime.now().plusDays(90))
                .build();

        lockedUser = User.builder()
                .username("lockeduser")
                .password("encodedPassword4")
                .email("locked@example.com")
                .mobile("13800138004")
                .fullName("锁定用户")
                .userStatus(User.UserStatus.LOCKED)
                .failedLoginAttempts(5)
                .lockedUntil(LocalDateTime.now().plusHours(1))
                .passwordExpiredAt(LocalDateTime.now().plusDays(30))
                .build();

        disabledUser = User.builder()
                .username("disableduser")
                .password("encodedPassword5")
                .email("disabled@example.com")
                .mobile("13800138005")
                .fullName("禁用用户")
                .userStatus(User.UserStatus.DISABLED)
                .failedLoginAttempts(0)
                .passwordExpiredAt(LocalDateTime.now().plusDays(30))
                .build();

        expiredUser = User.builder()
                .username("expireduser")
                .password("encodedPassword6")
                .email("expired@example.com")
                .mobile("13800138006")
                .fullName("过期用户")
                .userStatus(User.UserStatus.ACTIVE)
                .failedLoginAttempts(0)
                .passwordExpiredAt(LocalDateTime.now().minusDays(1)) // 已过期
                .build();

        // 保存测试数据
        Arrays.asList(testUser1, testUser2, testUser3, lockedUser, disabledUser, expiredUser)
                .forEach(user -> {
                    entityManager.persist(user);
                });
        entityManager.flush();
    }

    @Test
    @DisplayName("根据用户名查找用户 - 存在")
    void testFindByUsernameExists() {
        // When
        Optional<User> result = userRepository.findByUsername("testuser1");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("testuser1");
        assertThat(result.get().getEmail()).isEqualTo("test1@example.com");
    }

    @Test
    @DisplayName("根据用户名查找用户 - 不存在")
    void testFindByUsernameNotExists() {
        // When
        Optional<User> result = userRepository.findByUsername("nonexistent");

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("根据邮箱查找用户 - 存在")
    void testFindByEmailExists() {
        // When
        Optional<User> result = userRepository.findByEmail("test2@example.com");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("test2@example.com");
        assertThat(result.get().getUsername()).isEqualTo("testuser2");
    }

    @Test
    @DisplayName("根据邮箱查找用户 - 不存在")
    void testFindByEmailNotExists() {
        // When
        Optional<User> result = userRepository.findByEmail("nonexistent@example.com");

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("根据手机号查找用户 - 存在")
    void testFindByMobileExists() {
        // When
        Optional<User> result = userRepository.findByMobile("13800138003");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getMobile()).isEqualTo("13800138003");
        assertThat(result.get().getUsername()).isEqualTo("testuser3");
    }

    @Test
    @DisplayName("根据手机号查找用户 - 不存在")
    void testFindByMobileNotExists() {
        // When
        Optional<User> result = userRepository.findByMobile("13900139000");

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("根据用户名或邮箱查找用户 - 用户名匹配")
    void testFindByUsernameOrEmailUsernameMatch() {
        // When
        Optional<User> result = userRepository.findByUsernameOrEmail("testuser1");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("testuser1");
    }

    @Test
    @DisplayName("根据用户名或邮箱查找用户 - 邮箱匹配")
    void testFindByUsernameOrEmailEmailMatch() {
        // When
        Optional<User> result = userRepository.findByUsernameOrEmail("test2@example.com");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("test2@example.com");
    }

    @Test
    @DisplayName("根据用户名或邮箱查找用户 - 不匹配")
    void testFindByUsernameOrEmailNoMatch() {
        // When
        Optional<User> result = userRepository.findByUsernameOrEmail("nonexistent");

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("查找所有活跃用户")
    void testFindAllActiveUsers() {
        // When
        List<User> activeUsers = userRepository.findAllActiveUsers();

        // Then
        assertThat(activeUsers).hasSize(4); // testuser1, testuser2, testuser3, expiredUser
        assertThat(activeUsers).extracting(User::getUserStatus)
                .containsOnly(User.UserStatus.ACTIVE);
    }

    @Test
    @DisplayName("查找锁定用户")
    void testFindLockedUsers() {
        // When
        List<User> lockedUsers = userRepository.findLockedUsers();

        // Then
        assertThat(lockedUsers).hasSize(1);
        assertThat(lockedUsers.get(0).getUsername()).isEqualTo("lockeduser");
        assertThat(lockedUsers.get(0).getUserStatus()).isEqualTo(User.UserStatus.LOCKED);
    }

    @Test
    @DisplayName("查找密码过期用户")
    void testFindPasswordExpiredUsers() {
        // When
        List<User> expiredUsers = userRepository.findPasswordExpiredUsers(LocalDateTime.now());

        // Then
        assertThat(expiredUsers).hasSize(1);
        assertThat(expiredUsers.get(0).getUsername()).isEqualTo("expireduser");
    }

    @Test
    @DisplayName("根据关键词搜索用户 - 用户名匹配")
    void testSearchUsersUsernameMatch() {
        // When
        List<User> results = userRepository.searchUsers("user1");

        // Then
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getUsername()).isEqualTo("testuser1");
    }

    @Test
    @DisplayName("根据关键词搜索用户 - 全名匹配")
    void testSearchUsersFullNameMatch() {
        // When
        List<User> results = userRepository.searchUsers("测试");

        // Then
        assertThat(results).hasSize(6); // 所有用户的全名都包含"测试"
    }

    @Test
    @DisplayName("根据关键词搜索用户 - 邮箱匹配")
    void testSearchUsersEmailMatch() {
        // When
        List<User> results = userRepository.searchUsers("@example.com");

        // Then
        assertThat(results).hasSize(6); // 所有用户的邮箱都包含"@example.com"
    }

    @Test
    @DisplayName("根据关键词搜索用户 - 大小写不敏感")
    void testSearchUsersCaseInsensitive() {
        // When
        List<User> results1 = userRepository.searchUsers("TESTUSER1");
        List<User> results2 = userRepository.searchUsers("testuser1");

        // Then
        assertThat(results1).hasSize(1);
        assertThat(results2).hasSize(1);
        assertThat(results1.get(0).getUsername()).isEqualTo(results2.get(0).getUsername());
    }

    @Test
    @DisplayName("根据关键词搜索用户 - 无匹配结果")
    void testSearchUsersNoMatch() {
        // When
        List<User> results = userRepository.searchUsers("nonexistent");

        // Then
        assertThat(results).isEmpty();
    }

    @Test
    @DisplayName("统计用户状态分布")
    void testCountUsersByStatus() {
        // When
        List<Object[]> results = userRepository.countUsersByStatus();

        // Then
        assertThat(results).hasSize(3); // ACTIVE, LOCKED, DISABLED
        
        // 验证统计结果
        for (Object[] result : results) {
            String status = (String) result[0];
            Long count = (Long) result[1];
            
            if (status.equals("ACTIVE")) {
                assertThat(count).isEqualTo(4);
            } else if (status.equals("LOCKED")) {
                assertThat(count).isEqualTo(1);
            } else if (status.equals("DISABLED")) {
                assertThat(count).isEqualTo(1);
            }
        }
    }

    @Test
    @DisplayName("查找需要解锁的用户")
    void testFindUsersToUnlock() {
        // Given - 创建一个锁定时间已过的用户
        User unlockableUser = User.builder()
                .username("unlockable")
                .password("encodedPassword")
                .email("unlockable@example.com")
                .userStatus(User.UserStatus.LOCKED)
                .lockedUntil(LocalDateTime.now().minusMinutes(1)) // 锁定时间已过
                .build();
        entityManager.persist(unlockableUser);
        entityManager.flush();

        // When
        List<User> usersToUnlock = userRepository.findUsersToUnlock(LocalDateTime.now());

        // Then
        assertThat(usersToUnlock).hasSize(1);
        assertThat(usersToUnlock.get(0).getUsername()).isEqualTo("unlockable");
    }

    @Test
    @DisplayName("查找最近登录的用户")
    void testFindRecentlyLoggedInUsers() {
        // Given - 设置最近登录时间
        testUser1.setLastLoginTime(LocalDateTime.now().minusHours(1));
        testUser2.setLastLoginTime(LocalDateTime.now().minusDays(1));
        testUser3.setLastLoginTime(LocalDateTime.now().minusHours(2));
        
        entityManager.persist(testUser1);
        entityManager.persist(testUser2);
        entityManager.persist(testUser3);
        entityManager.flush();

        // When - 查找最近3天登录的用户
        List<User> recentUsers = userRepository.findRecentlyLoggedInUsers(LocalDateTime.now().minusDays(3));

        // Then
        assertThat(recentUsers).hasSize(3);
        // 验证按登录时间降序排列
        assertThat(recentUsers.get(0).getLastLoginTime())
                .isAfterOrEqualTo(recentUsers.get(1).getLastLoginTime());
        assertThat(recentUsers.get(1).getLastLoginTime())
                .isAfterOrEqualTo(recentUsers.get(2).getLastLoginTime());
    }

    @Test
    @DisplayName("检查用户名是否存在 - 存在")
    void testExistsByUsernameTrue() {
        // When
        boolean exists = userRepository.existsByUsername("testuser1");

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("检查用户名是否存在 - 不存在")
    void testExistsByUsernameFalse() {
        // When
        boolean exists = userRepository.existsByUsername("nonexistent");

        // Then
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("检查邮箱是否存在 - 存在")
    void testExistsByEmailTrue() {
        // When
        boolean exists = userRepository.existsByEmail("test1@example.com");

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("检查邮箱是否存在 - 不存在")
    void testExistsByEmailFalse() {
        // When
        boolean exists = userRepository.existsByEmail("nonexistent@example.com");

        // Then
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("检查手机号是否存在 - 存在")
    void testExistsByMobileTrue() {
        // When
        boolean exists = userRepository.existsByMobile("13800138001");

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("检查手机号是否存在 - 不存在")
    void testExistsByMobileFalse() {
        // When
        boolean exists = userRepository.existsByMobile("13900139000");

        // Then
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("边界测试 - 空字符串搜索")
    void testEdgeCaseEmptyStringSearch() {
        // When
        List<User> results = userRepository.searchUsers("");

        // Then
        assertThat(results).isEmpty();
    }

    @Test
    @DisplayName("边界测试 - null参数")
    void testEdgeCaseNullParameters() {
        // When & Then - 应该不抛出异常
        assertThatCode(() -> userRepository.findByUsername(null))
                .doesNotThrowAnyException();
        
        assertThatCode(() -> userRepository.findByEmail(null))
                .doesNotThrowAnyException();
        
        assertThatCode(() -> userRepository.findByMobile(null))
                .doesNotThrowAnyException();
        
        assertThatCode(() -> userRepository.searchUsers(null))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("边界测试 - 特殊字符搜索")
    void testEdgeCaseSpecialCharacterSearch() {
        // Given - 创建包含特殊字符的用户
        User specialUser = User.builder()
                .username("special_user")
                .password("encodedPassword")
                .email("special+test@example.com")
                .fullName("特殊@用户#")
                .userStatus(User.UserStatus.ACTIVE)
                .build();
        entityManager.persist(specialUser);
        entityManager.flush();

        // When
        List<User> results = userRepository.searchUsers("special@");

        // Then
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getFullName()).isEqualTo("特殊@用户#");
    }

    @Test
    @DisplayName("性能测试 - 大量数据查询")
    void testPerformanceLargeDataset() {
        // Given - 创建大量测试数据
        for (int i = 100; i < 1100; i++) {
            User user = User.builder()
                    .username("perfuser" + i)
                    .password("encodedPassword" + i)
                    .email("perf" + i + "@example.com")
                    .mobile("1380013" + String.format("%04d", i))
                    .fullName("性能测试用户" + i)
                    .userStatus(User.UserStatus.ACTIVE)
                    .build();
            entityManager.persist(user);
        }
        entityManager.flush();

        // When
        long startTime = System.currentTimeMillis();
        List<User> activeUsers = userRepository.findAllActiveUsers();
        long endTime = System.currentTimeMillis();

        // Then
        assertThat(activeUsers).hasSizeGreaterThan(1000);
        assertThat(endTime - startTime).isLessThan(1000); // 应该在1秒内完成
    }

    @Test
    @DisplayName("事务测试 - 回滚验证")
    void testTransactionRollback() {
        // Given
        long initialCount = userRepository.count();

        // When - 模拟事务失败的情况
        assertThatThrownBy(() -> {
            User invalidUser = User.builder()
                    .username(null) // 故意设置为null导致验证失败
                    .password("encodedPassword")
                    .email("invalid@example.com")
                    .userStatus(User.UserStatus.ACTIVE)
                    .build();
            entityManager.persist(invalidUser);
            entityManager.flush(); // 这里会抛出异常
        }).isInstanceOf(Exception.class);

        // Then
        long finalCount = userRepository.count();
        assertThat(finalCount).isEqualTo(initialCount); // 计数应该保持不变
    }
}
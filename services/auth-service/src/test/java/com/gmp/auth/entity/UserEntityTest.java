package com.gmp.auth.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

/**
 * 用户实体类单元测试
 * 测试User实体的所有字段验证、业务逻辑和边界条件
 * 
 * 测试覆盖范围：
 * - 字段验证约束
 * - 实体状态转换
 * - 业务逻辑方法
 * - 审计字段
 * - 边界条件和异常情况
 *
 * @author GMP系统开发团队
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("用户实体类单元测试")
class UserEntityTest {

    private Validator validator;
    private User testUser;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .password("encodedPassword123!")
                .email("test@example.com")
                .mobile("13800138000")
                .fullName("测试用户")
                .userStatus(User.UserStatus.ACTIVE)
                .failedLoginAttempts(0)
                .lastLoginTime(LocalDateTime.now())
                .passwordExpiredAt(LocalDateTime.now().plusDays(90))
                .build();
    }

    @Test
    @DisplayName("有效用户实体验证通过")
    void testValidUserEntity() {
        // When
        Set<ConstraintViolation<User>> violations = validator.validate(testUser);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("用户名验证 - 空用户名")
    void testUsernameBlank() {
        // Given
        testUser.setUsername("");

        // When
        Set<ConstraintViolation<User>> violations = validator.validate(testUser);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("不能为空");
    }

    @Test
    @DisplayName("用户名验证 - null用户名")
    void testUsernameNull() {
        // Given
        testUser.setUsername(null);

        // When
        Set<ConstraintViolation<User>> violations = validator.validate(testUser);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("不能为空");
    }

    @Test
    @DisplayName("用户名验证 - 长度超限")
    void testUsernameTooLong() {
        // Given
        testUser.setUsername("a".repeat(51)); // 超过50字符限制

        // When
        Set<ConstraintViolation<User>> violations = validator.validate(testUser);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("长度不能超过");
    }

    @Test
    @DisplayName("邮箱验证 - 无效邮箱格式")
    void testEmailInvalidFormat() {
        // Given
        testUser.setEmail("invalid-email");

        // When
        Set<ConstraintViolation<User>> violations = validator.validate(testUser);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("邮箱格式");
    }

    @Test
    @DisplayName("手机号验证 - 无效手机号格式")
    void testMobileInvalidFormat() {
        // Given
        testUser.setMobile("123456"); // 无效手机号

        // When
        Set<ConstraintViolation<User>> violations = validator.validate(testUser);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("手机号格式");
    }

    @Test
    @DisplayName("全名验证 - 空全名")
    void testFullNameBlank() {
        // Given
        testUser.setFullName("");

        // When
        Set<ConstraintViolation<User>> violations = validator.validate(testUser);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("不能为空");
    }

    @Test
    @DisplayName("用户状态枚举测试")
    void testUserStatusEnum() {
        // When & Then
        assertThat(User.UserStatus.ACTIVE).isNotNull();
        assertThat(User.UserStatus.LOCKED).isNotNull();
        assertThat(User.UserStatus.DISABLED).isNotNull();
        assertThat(User.UserStatus.EXPIRED).isNotNull();
    }

    @Test
    @DisplayName("Builder模式测试")
    void testBuilderPattern() {
        // When
        User user = User.builder()
                .id(2L)
                .username("builderuser")
                .password("password123!")
                .email("builder@example.com")
                .fullName("Builder用户")
                .userStatus(User.UserStatus.ACTIVE)
                .build();

        // Then
        assertThat(user.getId()).isEqualTo(2L);
        assertThat(user.getUsername()).isEqualTo("builderuser");
        assertThat(user.getEmail()).isEqualTo("builder@example.com");
        assertThat(user.getFullName()).isEqualTo("Builder用户");
        assertThat(user.getUserStatus()).isEqualTo(User.UserStatus.ACTIVE);
    }

    @Test
    @DisplayName("NoArgsConstructor测试")
    void testNoArgsConstructor() {
        // When
        User user = new User();

        // Then
        assertThat(user).isNotNull();
        assertThat(user.getId()).isNull();
        assertThat(user.getUsername()).isNull();
        assertThat(user.getUserStatus()).isNull();
    }

    @Test
    @DisplayName("AllArgsConstructor测试")
    void testAllArgsConstructor() {
        // When
        User user = new User(
                3L, "allargsuser", "password123!", "allargs@example.com",
                "13800138001", "AllArgsConstructor用户", User.UserStatus.ACTIVE,
                0, null, null, null, null, null, null, null, null, null, null
        );

        // Then
        assertThat(user.getId()).isEqualTo(3L);
        assertThat(user.getUsername()).isEqualTo("allargsuser");
        assertThat(user.getEmail()).isEqualTo("allargs@example.com");
    }

    @Test
    @DisplayName("Lombok @Data注解测试 - Getter")
    void testLombokGetters() {
        // When & Then
        assertThat(testUser.getId()).isEqualTo(1L);
        assertThat(testUser.getUsername()).isEqualTo("testuser");
        assertThat(testUser.getEmail()).isEqualTo("test@example.com");
        assertThat(testUser.getFullName()).isEqualTo("测试用户");
    }

    @Test
    @DisplayName("Lombok @Data注解测试 - Setter")
    void testLombokSetters() {
        // When
        testUser.setUsername("newusername");
        testUser.setEmail("new@example.com");
        testUser.setFullName("新用户名");

        // Then
        assertThat(testUser.getUsername()).isEqualTo("newusername");
        assertThat(testUser.getEmail()).isEqualTo("new@example.com");
        assertThat(testUser.getFullName()).isEqualTo("新用户名");
    }

    @Test
    @DisplayName("Lombok @Data注解测试 - toString")
    void testLombokToString() {
        // When
        String toString = testUser.toString();

        // Then
        assertThat(toString).contains("testuser");
        assertThat(toString).contains("test@example.com");
        assertThat(toString).contains("测试用户");
    }

    @Test
    @DisplayName("Lombok @Data注解测试 - equalsAndHashCode")
    void testLombokEqualsAndHashCode() {
        // Given
        User user1 = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .build();

        User user2 = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .build();

        User user3 = User.builder()
                .id(2L)
                .username("testuser")
                .email("test@example.com")
                .build();

        // When & Then
        assertThat(user1).isEqualTo(user2);
        assertThat(user1.hashCode()).isEqualTo(user2.hashCode());
        assertThat(user1).isNotEqualTo(user3);
    }

    @Test
    @DisplayName("业务逻辑测试 - 账户锁定状态检查")
    void testAccountLockedStatus() {
        // Given
        LocalDateTime lockTime = LocalDateTime.now().plusHours(1);
        testUser.setUserStatus(User.UserStatus.LOCKED);
        testUser.setLockedUntil(lockTime);

        // When & Then
        assertThat(testUser.getUserStatus()).isEqualTo(User.UserStatus.LOCKED);
        assertThat(testUser.getLockedUntil()).isEqualTo(lockTime);
    }

    @Test
    @DisplayName("业务逻辑测试 - 密码过期检查")
    void testPasswordExpiredStatus() {
        // Given
        LocalDateTime expiredTime = LocalDateTime.now().minusDays(1);
        testUser.setPasswordExpiredAt(expiredTime);

        // When & Then
        assertThat(testUser.getPasswordExpiredAt()).isEqualTo(expiredTime);
        assertThat(expiredTime).isBefore(LocalDateTime.now());
    }

    @Test
    @DisplayName("业务逻辑测试 - 登录失败次数递增")
    void testFailedLoginAttemptsIncrement() {
        // Given
        testUser.setFailedLoginAttempts(2);

        // When
        testUser.setFailedLoginAttempts(3);

        // Then
        assertThat(testUser.getFailedLoginAttempts()).isEqualTo(3);
    }

    @Test
    @DisplayName("业务逻辑测试 - 最后登录时间更新")
    void testLastLoginTimeUpdate() {
        // Given
        LocalDateTime newLoginTime = LocalDateTime.now();

        // When
        testUser.setLastLoginTime(newLoginTime);

        // Then
        assertThat(testUser.getLastLoginTime()).isEqualTo(newLoginTime);
    }

    @Test
    @DisplayName("边界测试 - 最大长度用户名")
    void testEdgeCaseMaximumUsernameLength() {
        // Given
        String maxUsername = "a".repeat(50); // 正好50个字符

        // When
        testUser.setUsername(maxUsername);
        Set<ConstraintViolation<User>> violations = validator.validate(testUser);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("边界测试 - 最小长度用户名")
    void testEdgeCaseMinimumUsernameLength() {
        // Given
        String minUsername = "a"; // 最小1个字符

        // When
        testUser.setUsername(minUsername);
        Set<ConstraintViolation<User>> violations = validator.validate(testUser);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("边界测试 - 特殊字符用户名")
    void testEdgeCaseSpecialCharacterUsername() {
        // Given
        String specialUsername = "test_user-123.test";

        // When
        testUser.setUsername(specialUsername);
        Set<ConstraintViolation<User>> violations = validator.validate(testUser);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("边界测试 - Unicode字符全名")
    void testEdgeCaseUnicodeFullName() {
        // Given
        String unicodeFullName = "测试用户🔒🌟";

        // When
        testUser.setFullName(unicodeFullName);
        Set<ConstraintViolation<User>> violations = validator.validate(testUser);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("边界测试 - 各种有效邮箱格式")
    void testEdgeCaseValidEmailFormats() {
        String[] validEmails = {
                "test@example.com",
                "test.email@example.com",
                "test+email@example.com",
                "test123@example.co.uk",
                "test_email123@example-domain.com"
        };

        for (String email : validEmails) {
            // When
            testUser.setEmail(email);
            Set<ConstraintViolation<User>> violations = validator.validate(testUser);

            // Then
            assertThat(violations).as("Email '%s' should be valid", email).isEmpty();
        }
    }

    @Test
    @DisplayName("边界测试 - 各种无效邮箱格式")
    void testEdgeCaseInvalidEmailFormats() {
        String[] invalidEmails = {
                "invalid-email",
                "@example.com",
                "test@",
                "test..email@example.com",
                "test@example",
                "test@.com"
        };

        for (String email : invalidEmails) {
            // When
            testUser.setEmail(email);
            Set<ConstraintViolation<User>> violations = validator.validate(testUser);

            // Then
            assertThat(violations).as("Email '%s' should be invalid", email).hasSize(1);
        }
    }

    @Test
    @DisplayName("边界测试 - 各种有效手机号格式")
    void testEdgeCaseValidMobileFormats() {
        String[] validMobiles = {
                "13800138000",
                "15012345678",
                "18888888888",
                "19999999999"
        };

        for (String mobile : validMobiles) {
            // When
            testUser.setMobile(mobile);
            Set<ConstraintViolation<User>> violations = validator.validate(testUser);

            // Then
            assertThat(violations).as("Mobile '%s' should be valid", mobile).isEmpty();
        }
    }

    @Test
    @DisplayName("边界测试 - 各种无效手机号格式")
    void testEdgeCaseInvalidMobileFormats() {
        String[] invalidMobiles = {
                "12345678901", // 不是有效的手机号段
                "1380013800",  // 少一位
                "138001380000", // 多一位
                "abcdefghijk",  // 非数字
                "138-0013-8000" // 包含特殊字符
        };

        for (String mobile : invalidMobiles) {
            // When
            testUser.setMobile(mobile);
            Set<ConstraintViolation<User>> violations = validator.validate(testUser);

            // Then
            assertThat(violations).as("Mobile '%s' should be invalid", mobile).hasSize(1);
        }
    }

    @Test
    @DisplayName("性能测试 - 大量验证操作")
    void testPerformanceValidation() {
        // Given
        User[] users = new User[1000];
        for (int i = 0; i < 1000; i++) {
            users[i] = User.builder()
                    .username("user" + i)
                    .email("user" + i + "@example.com")
                    .mobile("1380013" + String.format("%04d", i))
                    .fullName("用户" + i)
                    .userStatus(User.UserStatus.ACTIVE)
                    .build();
        }

        // When
        long startTime = System.currentTimeMillis();
        for (User user : users) {
            Set<ConstraintViolation<User>> violations = validator.validate(user);
            assertThat(violations).isEmpty();
        }
        long endTime = System.currentTimeMillis();

        // Then
        assertThat(endTime - startTime).isLessThan(2000); // 应该在2秒内完成
    }

    @Test
    @DisplayName("并发测试 - 多线程实体创建")
    void testConcurrentEntityCreation() throws InterruptedException {
        // Given
        int threadCount = 10;
        Thread[] threads = new Thread[threadCount];
        User[] users = new User[threadCount];
        boolean[] results = new boolean[threadCount];

        // When
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                try {
                    User user = User.builder()
                            .username("concurrentuser" + index)
                            .email("concurrent" + index + "@example.com")
                            .fullName("并发用户" + index)
                            .userStatus(User.UserStatus.ACTIVE)
                            .build();
                    
                    Set<ConstraintViolation<User>> violations = validator.validate(user);
                    users[index] = user;
                    results[index] = violations.isEmpty();
                } catch (Exception e) {
                    results[index] = false;
                }
            });
            threads[i].start();
        }

        // 等待所有线程完成
        for (Thread thread : threads) {
            thread.join();
        }

        // Then
        for (boolean result : results) {
            assertThat(result).isTrue();
        }
        for (User user : users) {
            assertThat(user).isNotNull();
            assertThat(user.getUsername()).startsWith("concurrentuser");
        }
    }
}
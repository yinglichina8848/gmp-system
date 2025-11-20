package com.gmp.auth.entity;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * User实体单元测试
 *
 * @author GMP系统开发团队
 */
@ActiveProfiles("test")
class UserTest {

    @Test
    void testUserCreation() {
        // Given
        User user = User.builder()
                .username("testuser")
                .email("test@example.com")
                .fullName("测试用户")
                .passwordHash("hashed_password")
                .userStatus(User.UserStatus.ACTIVE)
                .loginAttempts(0)
                .version(1)
                .build();

        // When & Then
        assertThat(user.getUsername()).isEqualTo("testuser");
        assertThat(user.getEmail()).isEqualTo("test@example.com");
        assertThat(user.getUserStatus()).isEqualTo(User.UserStatus.ACTIVE);
        assertThat(user.getLoginAttempts()).isEqualTo(0);
        assertThat(user.getUserStatus()).isEqualTo(User.UserStatus.ACTIVE);
    }

    @Test
    void testUserStatusEnum() {
        // Test ACTIVE status
        assertThat(User.UserStatus.ACTIVE.getDescription()).isEqualTo("活跃");

        // Test INACTIVE status
        assertThat(User.UserStatus.INACTIVE.getDescription()).isEqualTo("未激活");

        // Test LOCKED status
        assertThat(User.UserStatus.LOCKED.getDescription()).isEqualTo("锁定");

        // Test EXPIRED status
        assertThat(User.UserStatus.EXPIRED.getDescription()).isEqualTo("过期");
    }

    @Test
    void testLoginAttemptsManagement() {
        // Given
        User user = User.builder()
                .username("testuser")
                .loginAttempts(0)
                .build();

        // When - initial state
        assertThat(user.getLoginAttempts()).isEqualTo(0);

        // When - increment attempts
        user.incrementLoginAttempts();
        assertThat(user.getLoginAttempts()).isEqualTo(1);

        user.incrementLoginAttempts();
        assertThat(user.getLoginAttempts()).isEqualTo(2);

        // When - reset attempts
        user.resetLoginAttempts();
        assertThat(user.getLoginAttempts()).isEqualTo(0);
        assertThat(user.getLockedUntil()).isNull();
    }

    @Test
    void testAccountLocking() {
        // Given
        User user = User.builder()
                .username("testuser")
                .userStatus(User.UserStatus.ACTIVE)
                .build();

        // When - lock account
        user.lockAccount(15); // 15分钟

        // Then
        assertThat(user.getUserStatus()).isEqualTo(User.UserStatus.LOCKED);
        assertThat(user.getLoginAttempts()).isEqualTo(0);
        assertThat(user.getLockedUntil()).isAfter(LocalDateTime.now());
        assertThat(user.isLocked()).isTrue();
    }

    @Test
    void testLockedUserDetection() {
        // Given - user is locked
        User lockedUser = User.builder()
                .userStatus(User.UserStatus.LOCKED)
                .build();

        // Given - user has lock timeout
        User timeoutUser = User.builder()
                .userStatus(User.UserStatus.ACTIVE)
                .lockedUntil(LocalDateTime.now().plusMinutes(5))
                .build();

        // Given - user has expired lock
        User expiredLockUser = User.builder()
                .userStatus(User.UserStatus.ACTIVE)
                .lockedUntil(LocalDateTime.now().minusMinutes(5))
                .build();

        // Then
        assertThat(lockedUser.isLocked()).isTrue();
        assertThat(timeoutUser.isLocked()).isTrue();
        assertThat(expiredLockUser.isLocked()).isFalse();
    }

    @Test
    void testPasswordExpiration() {
        // Given - password not expired
        User userWithValidPassword = User.builder()
                .passwordExpiredAt(LocalDateTime.now().plusDays(30))
                .build();

        // Given - password expired
        User userWithExpiredPassword = User.builder()
                .passwordExpiredAt(LocalDateTime.now().minusDays(1))
                .build();

        // Given - no expiration set
        User userWithoutExpiration = User.builder().build();

        // Then
        assertThat(userWithValidPassword.isPasswordExpired()).isFalse();
        assertThat(userWithExpiredPassword.isPasswordExpired()).isTrue();
        assertThat(userWithoutExpiration.isPasswordExpired()).isFalse();
    }

    @Test
    void testUserStatusValidation() {
        // Test valid statuses
        User activeUser = User.builder().userStatus(User.UserStatus.ACTIVE).build();
        User inactiveUser = User.builder().userStatus(User.UserStatus.INACTIVE).build();
        User lockedUser = User.builder().userStatus(User.UserStatus.LOCKED).build();
        User expiredUser = User.builder().userStatus(User.UserStatus.EXPIRED).build();

        assertThat(activeUser.getUserStatus()).isNotNull();
        assertThat(inactiveUser.getUserStatus()).isNotNull();
        assertThat(lockedUser.getUserStatus()).isNotNull();
        assertThat(expiredUser.getUserStatus()).isNotNull();
    }

    @Test
    void testAuditFields() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        User user = User.builder()
                .createdAt(now)
                .updatedAt(now)
                .version(1)
                .build();

        // Then
        assertThat(user.getCreatedAt()).isEqualTo(now);
        assertThat(user.getUpdatedAt()).isEqualTo(now);
        assertThat(user.getVersion()).isEqualTo(1);
    }
}

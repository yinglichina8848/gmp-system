package com.gmp.auth.entity;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * UserRole实体单元测试
 *
 * @author GMP系统开发团队
 */
@ActiveProfiles("test")
class UserRoleTest {

    @Test
    void testUserRoleCreation() {
        // Given
        UserRole userRole = UserRole.builder()
                .userId(1L)
                .roleId(2L)
                .isActive(true)
                .assignedBy(1L)
                .build();

        // When & Then
        assertThat(userRole.getUserId()).isEqualTo(1L);
        assertThat(userRole.getRoleId()).isEqualTo(2L);
        assertThat(userRole.getIsActive()).isTrue();
        assertThat(userRole.getAssignedBy()).isEqualTo(1L);
        assertThat(userRole.isValid()).isTrue();
    }

    @Test
    void testUserRoleWithExpiration() {
        // Given
        LocalDateTime futureDate = LocalDateTime.now().plusDays(30);
        LocalDateTime pastDate = LocalDateTime.now().minusDays(1);

        UserRole activeUserRole = UserRole.builder()
                .userId(1L)
                .roleId(2L)
                .isActive(true)
                .expiredAt(futureDate)
                .build();

        UserRole expiredUserRole = UserRole.builder()
                .userId(1L)
                .roleId(3L)
                .isActive(true)
                .expiredAt(pastDate)
                .build();

        UserRole permanentUserRole = UserRole.builder()
                .userId(1L)
                .roleId(4L)
                .isActive(true)
                .expiredAt(null) // No expiration
                .build();

        // Then
        assertThat(activeUserRole.isValid()).isTrue();   // Not expired yet
        assertThat(expiredUserRole.isValid()).isFalse(); // Already expired
        assertThat(permanentUserRole.isValid()).isTrue(); // Never expires
    }

    @Test
    void testInactiveUserRole() {
        // Given
        UserRole inactiveUserRole = UserRole.builder()
                .userId(1L)
                .roleId(2L)
                .isActive(false)
                .build();

        // Then
        assertThat(inactiveUserRole.getIsActive()).isFalse();
        assertThat(inactiveUserRole.isValid()).isFalse();
    }

    @Test
    void testUserRolePrePersist() {
        // Given
        UserRole userRole = UserRole.builder()
                .userId(1L)
                .roleId(2L)
                .isActive(true)
                .build();

        // When - simulate pre-persist (normally done by JPA)
        userRole.onCreate();

        // Then
        assertThat(userRole.getAssignedAt()).isNotNull();
        assertThat(userRole.getAssignedAtAudit()).isNotNull();
        // Check that both timestamps are very close (within 1 second)
        assertThat(userRole.getAssignedAtAudit())
                .isAfterOrEqualTo(userRole.getAssignedAt().minusSeconds(1))
                .isBeforeOrEqualTo(userRole.getAssignedAt().plusSeconds(1));
    }

    @Test
    void testUserRoleWithAssignedTime() {
        // Given
        LocalDateTime assignedTime = LocalDateTime.now().minusDays(1);
        UserRole userRole = UserRole.builder()
                .userId(1L)
                .roleId(2L)
                .assignedAt(assignedTime)
                .isActive(true)
                .build();

        // When - pre-persist should not override existing assignedAt
        userRole.onCreate();

        // Then
        assertThat(userRole.getAssignedAt()).isEqualTo(assignedTime);
        assertThat(userRole.getAssignedAtAudit()).isNotNull();
    }

    @Test
    void testUserRoleAuditFields() {
        // Given
        LocalDateTime assignedTime = LocalDateTime.now();
        LocalDateTime auditTime = LocalDateTime.now();

        UserRole userRole = UserRole.builder()
                .userId(1L)
                .roleId(2L)
                .assignedAt(assignedTime)
                .assignedAtAudit(auditTime)
                .assignedBy(1L)
                .build();

        // Then
        assertThat(userRole.getAssignedAt()).isEqualTo(assignedTime);
        assertThat(userRole.getAssignedAtAudit()).isEqualTo(auditTime);
        assertThat(userRole.getAssignedBy()).isEqualTo(1L);
    }

    @Test
    void testUserRoleExpirationNullCheck() {
        // Test that null expiration means role never expires
        UserRole userRole = UserRole.builder()
                .userId(1L)
                .roleId(2L)
                .isActive(true)
                .expiredAt(null)
                .build();

        assertThat(userRole.isValid()).isTrue();
    }

    @Test
    void testUserRoleExpirationExactTime() {
        // Given
        LocalDateTime exactExpireTime = LocalDateTime.now().plusSeconds(1);
        UserRole userRole = UserRole.builder()
                .userId(1L)
                .roleId(2L)
                .isActive(true)
                .expiredAt(exactExpireTime)
                .build();

        // When - check before expiration
        assertThat(userRole.isValid()).isTrue();

        // When - wait for expiration (simulate)
        // Note: In real scenarios, this would be checked by scheduler
        // Here we just test the logic
    }

    @Test
    void testMultipleUserRoles() {
        // Test a user having multiple roles
        UserRole adminRole = UserRole.builder()
                .userId(1L)
                .roleId(1L) // ROLE_ADMIN
                .isActive(true)
                .build();

        UserRole userRole = UserRole.builder()
                .userId(1L)
                .roleId(2L) // ROLE_USER
                .isActive(true)
                .build();

        assertThat(adminRole.isValid()).isTrue();
        assertThat(userRole.isValid()).isTrue();
        assertThat(adminRole.getUserId()).isEqualTo(userRole.getUserId());
        assertThat(adminRole.getRoleId()).isNotEqualTo(userRole.getRoleId());
    }

    @Test
    void testUserRoleBulkOperations() {
        // Simulate bulk role assignment
        long[] userIds = {1L, 2L, 3L};
        long roleId = 10L; // Some role

        UserRole[] userRoles = new UserRole[userIds.length];
        for (int i = 0; i < userIds.length; i++) {
            userRoles[i] = UserRole.builder()
                    .userId(userIds[i])
                    .roleId(roleId)
                    .isActive(true)
                    .assignedBy(1L)
                    .build();
        }

        // Verify all assignments
        for (UserRole userRole : userRoles) {
            assertThat(userRole.isValid()).isTrue();
            assertThat(userRole.getRoleId()).isEqualTo(roleId);
            assertThat(userRole.getIsActive()).isTrue();
        }
    }

    @Test
    void testUserRoleStatusTransitions() {
        // Test role status changes
        UserRole userRole = UserRole.builder()
                .userId(1L)
                .roleId(2L)
                .isActive(true)
                .build();

        assertThat(userRole.isValid()).isTrue();

        // Deactivate role
        userRole.setIsActive(false);
        assertThat(userRole.isValid()).isFalse();

        // Reactivate role
        userRole.setIsActive(true);
        assertThat(userRole.isValid()).isTrue();

        // Add expiration
        userRole.setExpiredAt(LocalDateTime.now().minusDays(1));
        assertThat(userRole.isValid()).isFalse();
    }
}

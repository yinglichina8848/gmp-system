package com.gmp.auth.entity;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Role实体单元测试
 *
 * @author GMP系统开发团队
 */
@ActiveProfiles("test")
class RoleTest {

    @Test
    void testRoleCreation() {
        // Given
        Role role = Role.builder()
                .roleCode("ROLE_ADMIN")
                .roleName("系统管理员")
                .description("拥有系统所有权限的角色")
                .priority(100)
                .isBuiltin(true)
                .isActive(true)
                .build();

        // When & Then
        assertThat(role.getRoleCode()).isEqualTo("ROLE_ADMIN");
        assertThat(role.getRoleName()).isEqualTo("系统管理员");
        assertThat(role.getDescription()).isEqualTo("拥有系统所有权限的角色");
        assertThat(role.getPriority()).isEqualTo(100);
        assertThat(role.getIsBuiltin()).isTrue();
        assertThat(role.getIsActive()).isTrue();
        assertThat(role.isValid()).isTrue();
    }

    @Test
    void testRoleCodeValidation() {
        // Test valid role code pattern
        Role validRole = Role.builder()
                .roleCode("ROLE_SYSTEM_ADMIN")
                .roleName("系统管理员")
                .build();

        // Test invalid role code patterns
        Role invalidRole1 = Role.builder()
                .roleCode("ADMIN")
                .roleName("管理员")
                .build();

        Role invalidRole2 = Role.builder()
                .roleCode("role_user")
                .roleName("用户")
                .build();

        assertThat(validRole.getRoleCode()).matches("^ROLE_[A-Z_]+$");
        assertThat(invalidRole1.getRoleCode()).doesNotMatch("^ROLE_[A-Z_]+$");
        assertThat(invalidRole2.getRoleCode()).doesNotMatch("^ROLE_[A-Z_]+$");
    }

    @Test
    void testRoleActiveState() {
        // Test active role
        Role activeRole = Role.builder()
                .roleCode("ROLE_USER")
                .isActive(true)
                .build();

        // Test inactive role
        Role inactiveRole = Role.builder()
                .roleCode("ROLE_GUEST")
                .isActive(false)
                .build();

        assertThat(activeRole.getIsActive()).isTrue();
        assertThat(activeRole.isValid()).isTrue();
        assertThat(inactiveRole.getIsActive()).isFalse();
        assertThat(inactiveRole.isValid()).isFalse();
    }

    @Test
    void testRolePriorityOrdering() {
        // Test priority ordering
        Role superAdmin = Role.builder().roleCode("ROLE_SUPER_ADMIN").priority(100).build();
        Role admin = Role.builder().roleCode("ROLE_ADMIN").priority(80).build();
        Role manager = Role.builder().roleCode("ROLE_MANAGER").priority(60).build();
        Role user = Role.builder().roleCode("ROLE_USER").priority(10).build();

        assertThat(superAdmin.getPriority()).isGreaterThan(admin.getPriority());
        assertThat(admin.getPriority()).isGreaterThan(manager.getPriority());
        assertThat(manager.getPriority()).isGreaterThan(user.getPriority());
    }

    @Test
    void testRoleWithNullValues() {
        // Test role with null description and priority
        Role role = Role.builder()
                .roleCode("ROLE_BASIC")
                .roleName("基本用户")
                .build();

        assertThat(role.getDescription()).isNull();
        assertThat(role.getPriority()).isNull();
        assertThat(role.getIsBuiltin()).isFalse(); // Builder default
        assertThat(role.getIsActive()).isTrue();   // Builder default
    }

    @Test
    void testRoleAuditFields() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        Role role = Role.builder()
                .roleCode("ROLE_TEST")
                .roleName("测试角色")
                .createdAt(now)
                .updatedAt(now)
                .version(1)
                .build();

        // Then
        assertThat(role.getCreatedAt()).isEqualTo(now);
        assertThat(role.getUpdatedAt()).isEqualTo(now);
        assertThat(role.getVersion()).isEqualTo(1);
    }

    @Test
    void testRoleValidationScenarios() {
        // Valid role with all required fields
        Role validRole = Role.builder()
                .roleCode("ROLE_VALID")
                .roleName("有效角色")
                .isActive(true)
                .build();

        // Role without required fields
        Role invalidRole = Role.builder()
                .roleName("缺少代码的角色")
                .isActive(true)
                .build();

        assertThat(validRole.isValid()).isTrue();
        // isValid() checks isActive && roleCode != null && !roleCode.trim().isEmpty()
        assertThat(invalidRole.isValid()).isFalse();
    }
}

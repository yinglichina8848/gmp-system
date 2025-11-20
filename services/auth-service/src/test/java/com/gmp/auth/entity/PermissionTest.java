package com.gmp.auth.entity;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Permission实体单元测试
 *
 * @author GMP系统开发团队
 */
@ActiveProfiles("test")
class PermissionTest {

    @Test
    void testPermissionCreation() {
        // Given
        Permission permission = Permission.builder()
                .permissionCode("PERMISSION_USER_MANAGE")
                .permissionName("用户管理")
                .resourceType("MENU")
                .resourceUrl("/api/auth/users")
                .httpMethod("ALL")
                .description("用户增删改查权限")
                .isActive(true)
                .build();

        // When & Then
        assertThat(permission.getPermissionCode()).isEqualTo("PERMISSION_USER_MANAGE");
        assertThat(permission.getPermissionName()).isEqualTo("用户管理");
        assertThat(permission.getResourceType()).isEqualTo("MENU");
        assertThat(permission.getResourceUrl()).isEqualTo("/api/auth/users");
        assertThat(permission.getHttpMethod()).isEqualTo("ALL");
        assertThat(permission.getDescription()).isEqualTo("用户增删改查权限");
        assertThat(permission.getIsActive()).isTrue();
        assertThat(permission.isValid()).isTrue();
    }

    @Test
    void testPermissionCodeValidation() {
        // Test valid permission code pattern
        Permission validPermission = Permission.builder()
                .permissionCode("PERMISSION_SYSTEM_CONFIG")
                .permissionName("系统配置")
                .build();

        // Test invalid permission code patterns
        Permission invalidPermission1 = Permission.builder()
                .permissionCode("USER_MANAGE")
                .permissionName("用户管理")
                .build();

        Permission invalidPermission2 = Permission.builder()
                .permissionCode("permission_delete")
                .permissionName("删除权限")
                .build();

        assertThat(validPermission.getPermissionCode()).matches("^PERMISSION_[A-Z_]+$");
        assertThat(invalidPermission1.getPermissionCode()).doesNotMatch("^PERMISSION_[A-Z_]+$");
        assertThat(invalidPermission2.getPermissionCode()).doesNotMatch("^PERMISSION_[A-Z_]+$");
    }

    @Test
    void testResourceTypeValidation() {
        // Test valid resource types
        Permission menuPermission = Permission.builder()
                .permissionCode("PERMISSION_MENU_ACCESS")
                .resourceType("MENU")
                .build();

        Permission apiPermission = Permission.builder()
                .permissionCode("PERMISSION_API_ACCESS")
                .resourceType("API")
                .build();

        Permission buttonPermission = Permission.builder()
                .permissionCode("PERMISSION_BUTTON_CLICK")
                .resourceType("BUTTON")
                .build();

        Permission dataPermission = Permission.builder()
                .permissionCode("PERMISSION_DATA_VIEW")
                .resourceType("DATA")
                .build();

        assertThat(menuPermission.getResourceType()).isIn("MENU", "API", "BUTTON", "DATA");
        assertThat(apiPermission.getResourceType()).isIn("MENU", "API", "BUTTON", "DATA");
        assertThat(buttonPermission.getResourceType()).isIn("MENU", "API", "BUTTON", "DATA");
        assertThat(dataPermission.getResourceType()).isIn("MENU", "API", "BUTTON", "DATA");
    }

    @Test
    void testHttpMethodValidation() {
        // Test valid HTTP methods
        Permission allPermission = Permission.builder()
                .permissionCode("PERMISSION_FULL_ACCESS")
                .httpMethod("ALL")
                .build();

        Permission getPermission = Permission.builder()
                .permissionCode("PERMISSION_READ_ONLY")
                .httpMethod("GET")
                .build();

        Permission postPermission = Permission.builder()
                .permissionCode("PERMISSION_CREATE")
                .httpMethod("POST")
                .build();

        assertThat(allPermission.getHttpMethod()).isIn("GET", "POST", "PUT", "DELETE", "ALL");
        assertThat(getPermission.getHttpMethod()).isIn("GET", "POST", "PUT", "DELETE", "ALL");
        assertThat(postPermission.getHttpMethod()).isIn("GET", "POST", "PUT", "DELETE", "ALL");
    }

    @Test
    void testResourceUrlMatching() {
        // Given
        Permission permission = Permission.builder()
                .permissionCode("PERMISSION_USER_API")
                .resourceType("API")
                .resourceUrl("/api/users")
                .httpMethod("GET")
                .isActive(true)
                .build();

        // Test matching URLs
        assertThat(permission.matches("/api/users", "GET")).isTrue();
        assertThat(permission.matches("/api/users/123", "GET")).isTrue();
        assertThat(permission.matches("/api/users/search", "GET")).isTrue();

        // Test non-matching methods
        assertThat(permission.matches("/api/users", "POST")).isFalse();

        // Test non-matching URLs
        assertThat(permission.matches("/api/roles", "GET")).isFalse();
        assertThat(permission.matches("/admin/users", "GET")).isFalse();

        // Test inactive permission
        permission.setIsActive(false);
        assertThat(permission.matches("/api/users", "GET")).isFalse();
    }

    @Test
    void testPermissionWithNullResourceUrl() {
        // Given
        Permission permission = Permission.builder()
                .permissionCode("PERMISSION_GLOBAL")
                .resourceType("API")
                .httpMethod("ALL")
                .isActive(true)
                .build();

        // When & Then (resourceUrl is null, should not match)
        assertThat(permission.matches("/api/users", "GET")).isFalse();
        assertThat(permission.getResourceUrl()).isNull();
    }

    @Test
    void testPermissionActiveState() {
        // Test active permission
        Permission activePermission = Permission.builder()
                .permissionCode("PERMISSION_ACTIVE")
                .isActive(true)
                .build();

        // Test inactive permission
        Permission inactivePermission = Permission.builder()
                .permissionCode("PERMISSION_INACTIVE")
                .isActive(false)
                .build();

        assertThat(activePermission.getIsActive()).isTrue();
        assertThat(activePermission.isValid()).isTrue();
        assertThat(inactivePermission.getIsActive()).isFalse();
        assertThat(inactivePermission.isValid()).isFalse();
    }

    @Test
    void testPermissionWithMinimalData() {
        // Test permission with only required fields
        Permission permission = Permission.builder()
                .permissionCode("PERMISSION_MINIMAL")
                .permissionName("最小权限")
                .resourceType("API")
                .httpMethod("ALL") // Explicitly set HTTP method
                .build();

        assertThat(permission.getResourceUrl()).isNull();
        assertThat(permission.getHttpMethod()).isEqualTo("ALL");
        assertThat(permission.getIsActive()).isTrue(); // default
        assertThat(permission.getDescription()).isNull();
    }

    @Test
    void testPermissionAuditFields() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        Permission permission = Permission.builder()
                .permissionCode("PERMISSION_AUDIT_TEST")
                .permissionName("审计测试权限")
                .createdAt(now)
                .updatedAt(now)
                .version(1)
                .build();

        // Then
        assertThat(permission.getCreatedAt()).isEqualTo(now);
        assertThat(permission.getUpdatedAt()).isEqualTo(now);
        assertThat(permission.getVersion()).isEqualTo(1);
    }

    @Test
    void testPermissionValidationScenarios() {
        // Valid permission with required fields
        Permission validPermission = Permission.builder()
                .permissionCode("PERMISSION_VALID")
                .permissionName("有效权限")
                .resourceType("API")
                .isActive(true)
                .build();

        // Permission without required fields
        Permission invalidPermission = Permission.builder()
                .permissionName("缺少代码的权限")
                .resourceType("API")
                .isActive(true)
                .build();

        assertThat(validPermission.isValid()).isTrue();
        assertThat(invalidPermission.isValid()).isFalse();
    }

    @Test
    void testUrlPatternMatching() {
        // Test exact URL matching
        Permission exactPermission = Permission.builder()
                .permissionCode("PERMISSION_EXACT")
                .resourceType("API")
                .resourceUrl("/api/users")
                .httpMethod("GET")
                .isActive(true)
                .build();

        // Test URL pattern with subpaths
        Permission patternPermission = Permission.builder()
                .permissionCode("PERMISSION_PATTERN")
                .resourceType("API")
                .resourceUrl("/api/users")
                .httpMethod("GET")
                .isActive(true)
                .build();

        assertThat(exactPermission.matches("/api/users", "GET")).isTrue();
        assertThat(patternPermission.matches("/api/users/123", "GET")).isTrue();
        assertThat(patternPermission.matches("/api/users/123/profile", "GET")).isTrue();

        // Test non-matching root
        assertThat(exactPermission.matches("/apiv1/users", "GET")).isFalse();
    }
}

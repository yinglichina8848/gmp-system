package com.gmp.auth.dto;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * AssignRoleRequest DTO单元测试
 *
 * @author GMP系统开发团队
 */
@ActiveProfiles("test")
class AssignRoleRequestTest {

    @Test
    void testAssignRoleRequestGettersAndSetters() {
        // 创建测试对象
        AssignRoleRequest assignRoleRequest = new AssignRoleRequest();

        // 验证默认值
        assertThat(assignRoleRequest.getUsername()).isNull();
        assertThat(assignRoleRequest.getOrganizationCode()).isNull();
        assertThat(assignRoleRequest.getRoleCode()).isNull();

        // 测试setter方法
        assignRoleRequest.setUsername("testUser");
        assignRoleRequest.setOrganizationCode("ORG001");
        assignRoleRequest.setRoleCode("ROLE_ADMIN");

        // 验证setter后的值
        assertThat(assignRoleRequest.getUsername()).isEqualTo("testUser");
        assertThat(assignRoleRequest.getOrganizationCode()).isEqualTo("ORG001");
        assertThat(assignRoleRequest.getRoleCode()).isEqualTo("ROLE_ADMIN");
    }

    @Test
    void testAssignRoleRequestWithValues() {
        // 创建对象并直接设置值（通过setter）
        AssignRoleRequest assignRoleRequest = new AssignRoleRequest();
        assignRoleRequest.setUsername("john.doe");
        assignRoleRequest.setOrganizationCode("PHARMA001");
        assignRoleRequest.setRoleCode("QUALITY_MANAGER");

        // 验证所有字段都被正确设置
        assertThat(assignRoleRequest.getUsername()).isEqualTo("john.doe");
        assertThat(assignRoleRequest.getOrganizationCode()).isEqualTo("PHARMA001");
        assertThat(assignRoleRequest.getRoleCode()).isEqualTo("QUALITY_MANAGER");
    }

    @Test
    void testAssignRoleRequestNullValues() {
        // 测试null值处理
        AssignRoleRequest assignRoleRequest = new AssignRoleRequest();
        assignRoleRequest.setUsername(null);
        assignRoleRequest.setOrganizationCode(null);
        assignRoleRequest.setRoleCode(null);

        assertThat(assignRoleRequest.getUsername()).isNull();
        assertThat(assignRoleRequest.getOrganizationCode()).isNull();
        assertThat(assignRoleRequest.getRoleCode()).isNull();
    }
}

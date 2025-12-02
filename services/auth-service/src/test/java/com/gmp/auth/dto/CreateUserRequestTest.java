package com.gmp.auth.dto;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * CreateUserRequest DTO单元测试
 *
 * @author GMP系统开发团队
 */
@ActiveProfiles("test")
class CreateUserRequestTest {

    @Test
    void testCreateUserRequestGettersAndSetters() {
        // 创建测试对象
        CreateUserRequest createUserRequest = new CreateUserRequest();

        // 验证默认值
        assertThat(createUserRequest.getUsername()).isNull();
        assertThat(createUserRequest.getPassword()).isNull();
        assertThat(createUserRequest.getEmail()).isNull();
        assertThat(createUserRequest.getFullName()).isNull();
        assertThat(createUserRequest.isEnabled()).isFalse();

        // 测试setter方法
        createUserRequest.setUsername("testUser");
        createUserRequest.setPassword("password123");
        createUserRequest.setEmail("test@example.com");
        createUserRequest.setFullName("Test User");
        createUserRequest.setEnabled(true);

        // 验证setter后的值
        assertThat(createUserRequest.getUsername()).isEqualTo("testUser");
        assertThat(createUserRequest.getPassword()).isEqualTo("password123");
        assertThat(createUserRequest.getEmail()).isEqualTo("test@example.com");
        assertThat(createUserRequest.getFullName()).isEqualTo("Test User");
        assertThat(createUserRequest.isEnabled()).isTrue();
    }

    @Test
    void testCreateUserRequestWithValues() {
        // 创建对象并设置值
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("john.doe");
        createUserRequest.setPassword("securePass123");
        createUserRequest.setEmail("john.doe@pharma.com");
        createUserRequest.setFullName("John Doe");
        createUserRequest.setEnabled(false);

        // 验证所有字段都被正确设置
        assertThat(createUserRequest.getUsername()).isEqualTo("john.doe");
        assertThat(createUserRequest.getPassword()).isEqualTo("securePass123");
        assertThat(createUserRequest.getEmail()).isEqualTo("john.doe@pharma.com");
        assertThat(createUserRequest.getFullName()).isEqualTo("John Doe");
        assertThat(createUserRequest.isEnabled()).isFalse();
    }

    @Test
    void testCreateUserRequestNullValues() {
        // 测试null值处理
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername(null);
        createUserRequest.setPassword(null);
        createUserRequest.setEmail(null);
        createUserRequest.setFullName(null);
        createUserRequest.setEnabled(false);

        assertThat(createUserRequest.getUsername()).isNull();
        assertThat(createUserRequest.getPassword()).isNull();
        assertThat(createUserRequest.getEmail()).isNull();
        assertThat(createUserRequest.getFullName()).isNull();
        assertThat(createUserRequest.isEnabled()).isFalse();
    }

    @Test
    void testCreateUserRequestEnabledDefault() {
        // 测试enabled字段的默认值
        CreateUserRequest createUserRequest = new CreateUserRequest();
        assertThat(createUserRequest.isEnabled()).isFalse();

        // 设置为true后验证
        createUserRequest.setEnabled(true);
        assertThat(createUserRequest.isEnabled()).isTrue();
    }
}

package com.gmp.auth.dto;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * LoginRequest DTO单元测试
 *
 * @author GMP系统开发团队
 */
@ActiveProfiles("test")
class LoginRequestTest {

    @Test
    void testLoginRequestGetters() {
        // 创建测试对象
        LoginRequest loginRequest = new LoginRequest();
        
        // 验证默认值
        assertThat(loginRequest.getRememberMe()).isFalse();
        
        // 这里我们只测试getter方法，因为LoginRequest类只有getter方法
        // 注意：LoginRequest类没有setter方法和有参构造函数，所以无法直接设置属性值进行测试
        // 我们可以确认getter方法存在且能返回值
        assertThat(loginRequest.getUsername()).isNull();
        assertThat(loginRequest.getPassword()).isNull();
        assertThat(loginRequest.getCaptchaCode()).isNull();
        assertThat(loginRequest.getIpAddress()).isNull();
        assertThat(loginRequest.getUserAgent()).isNull();
    }
    
    @Test
    void testRememberMeDefaultValue() {
        // 特别测试rememberMe的默认值
        LoginRequest loginRequest = new LoginRequest();
        assertThat(loginRequest.getRememberMe()).isFalse();
        assertThat(loginRequest.getRememberMe()).isNotNull(); // 确保不是null
    }
}
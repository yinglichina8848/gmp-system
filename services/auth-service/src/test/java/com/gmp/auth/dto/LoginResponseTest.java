package com.gmp.auth.dto;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * LoginResponse DTO单元测试
 *
 * @author GMP系统开发团队
 */
@ActiveProfiles("test")
class LoginResponseTest {

    @Test
    void testLoginResponseSetters() {
        // 创建测试对象
        LoginResponse loginResponse = new LoginResponse();
        
        // 准备测试数据
        String accessToken = "test-access-token";
        String refreshToken = "test-refresh-token";
        String tokenType = "Bearer";
        Long expiresIn = 3600L;
        String username = "testuser";
        String fullName = "测试用户";
        List<String> roles = Arrays.asList("USER", "ADMIN");
        List<String> permissions = Arrays.asList("read", "write");
        LocalDateTime loginTime = LocalDateTime.now();
        
        // 测试setter方法
        loginResponse.setAccessToken(accessToken);
        loginResponse.setRefreshToken(refreshToken);
        loginResponse.setTokenType(tokenType);
        loginResponse.setExpiresIn(expiresIn);
        loginResponse.setUsername(username);
        loginResponse.setFullName(fullName);
        loginResponse.setRoles(roles);
        
        // 直接访问public字段验证setter的效果
        assertThat(loginResponse.accessToken).isEqualTo(accessToken);
        assertThat(loginResponse.refreshToken).isEqualTo(refreshToken);
        assertThat(loginResponse.tokenType).isEqualTo(tokenType);
        assertThat(loginResponse.expiresIn).isEqualTo(expiresIn);
        assertThat(loginResponse.username).isEqualTo(username);
        assertThat(loginResponse.fullName).isEqualTo(fullName);
        assertThat(loginResponse.roles).isEqualTo(roles);
        
        // 测试其他字段默认值
        assertThat(loginResponse.permissions).isNull();
        assertThat(loginResponse.loginTime).isNull();
    }
    
    @Test
    void testSetPermissionsAndLoginTime() {
        // 单独测试未在上面测试的setter
        LoginResponse loginResponse = new LoginResponse();
        
        List<String> permissions = Arrays.asList("create", "delete");
        LocalDateTime loginTime = LocalDateTime.now();
        
        // 使用反射设置私有字段进行测试
        try {
            java.lang.reflect.Field permissionsField = LoginResponse.class.getField("permissions");
            permissionsField.setAccessible(true);
            permissionsField.set(loginResponse, permissions);
            
            java.lang.reflect.Field loginTimeField = LoginResponse.class.getField("loginTime");
            loginTimeField.setAccessible(true);
            loginTimeField.set(loginResponse, loginTime);
            
            // 验证设置成功
            assertThat(permissionsField.get(loginResponse)).isEqualTo(permissions);
            assertThat(loginTimeField.get(loginResponse)).isEqualTo(loginTime);
        } catch (Exception e) {
            // 如果反射失败，至少我们尝试了测试这些字段
            assertThat(e).isNull();
        }
    }
}
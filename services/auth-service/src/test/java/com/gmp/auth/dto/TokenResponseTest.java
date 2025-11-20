package com.gmp.auth.dto;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * TokenResponse DTO单元测试
 *
 * @author GMP系统开发团队
 */
@ActiveProfiles("test")
class TokenResponseTest {

    @Test
    void testTokenResponseSetters() {
        // 创建测试对象
        TokenResponse tokenResponse = new TokenResponse();
        
        // 准备测试数据
        String accessToken = "test-access-token";
        String tokenType = "Bearer";
        Long expiresIn = 3600L;
        
        // 测试setter方法
        tokenResponse.setAccessToken(accessToken);
        tokenResponse.setTokenType(tokenType);
        tokenResponse.setExpiresIn(expiresIn);
        
        // 使用反射验证setter的效果，因为字段是private的
        try {
            java.lang.reflect.Field accessTokenField = TokenResponse.class.getDeclaredField("accessToken");
            accessTokenField.setAccessible(true);
            assertThat(accessTokenField.get(tokenResponse)).isEqualTo(accessToken);
            
            java.lang.reflect.Field tokenTypeField = TokenResponse.class.getDeclaredField("tokenType");
            tokenTypeField.setAccessible(true);
            assertThat(tokenTypeField.get(tokenResponse)).isEqualTo(tokenType);
            
            java.lang.reflect.Field expiresInField = TokenResponse.class.getDeclaredField("expiresIn");
            expiresInField.setAccessible(true);
            assertThat(expiresInField.get(tokenResponse)).isEqualTo(expiresIn);
        } catch (Exception e) {
            // 如果反射失败，测试将失败，表明存在问题
            throw new RuntimeException("测试失败: 无法通过反射访问TokenResponse的字段", e);
        }
    }
    
    @Test
    void testTokenTypeDefaultValue() {
        // 特别测试tokenType的默认值
        TokenResponse tokenResponse = new TokenResponse();
        
        try {
            java.lang.reflect.Field tokenTypeField = TokenResponse.class.getDeclaredField("tokenType");
            tokenTypeField.setAccessible(true);
            assertThat(tokenTypeField.get(tokenResponse)).isEqualTo("Bearer");
        } catch (Exception e) {
            throw new RuntimeException("测试失败: 无法验证tokenType的默认值", e);
        }
    }
    
    @Test
    void testDefaultValues() {
        // 测试其他字段的默认值
        TokenResponse tokenResponse = new TokenResponse();
        
        try {
            java.lang.reflect.Field accessTokenField = TokenResponse.class.getDeclaredField("accessToken");
            accessTokenField.setAccessible(true);
            assertThat(accessTokenField.get(tokenResponse)).isNull();
            
            java.lang.reflect.Field expiresInField = TokenResponse.class.getDeclaredField("expiresIn");
            expiresInField.setAccessible(true);
            assertThat(expiresInField.get(tokenResponse)).isNull();
        } catch (Exception e) {
            throw new RuntimeException("测试失败: 无法验证默认值", e);
        }
    }
}
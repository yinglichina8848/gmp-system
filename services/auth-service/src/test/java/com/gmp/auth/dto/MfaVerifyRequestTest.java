package com.gmp.auth.dto;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * MfaVerifyRequest DTO单元测试
 *
 * @author GMP系统开发团队
 */
@ActiveProfiles("test")
class MfaVerifyRequestTest {

    @Test
    void testMfaVerifyRequestGettersAndSetters() {
        // 创建测试对象
        MfaVerifyRequest mfaVerifyRequest = new MfaVerifyRequest();

        // 验证默认值
        assertThat(mfaVerifyRequest.getSessionId()).isNull();
        assertThat(mfaVerifyRequest.getTotpCode()).isNull();

        // 测试setter方法
        mfaVerifyRequest.setSessionId("session123");
        mfaVerifyRequest.setTotpCode("456789");

        // 验证setter后的值
        assertThat(mfaVerifyRequest.getSessionId()).isEqualTo("session123");
        assertThat(mfaVerifyRequest.getTotpCode()).isEqualTo("456789");
    }

    @Test
    void testMfaVerifyRequestWithValues() {
        // 创建对象并设置值
        MfaVerifyRequest mfaVerifyRequest = new MfaVerifyRequest();
        mfaVerifyRequest.setSessionId("mfa-session-456");
        mfaVerifyRequest.setTotpCode("987654");

        // 验证所有字段都被正确设置
        assertThat(mfaVerifyRequest.getSessionId()).isEqualTo("mfa-session-456");
        assertThat(mfaVerifyRequest.getTotpCode()).isEqualTo("987654");
    }

    @Test
    void testMfaVerifyRequestNullValues() {
        // 测试null值处理
        MfaVerifyRequest mfaVerifyRequest = new MfaVerifyRequest();
        mfaVerifyRequest.setSessionId(null);
        mfaVerifyRequest.setTotpCode(null);

        assertThat(mfaVerifyRequest.getSessionId()).isNull();
        assertThat(mfaVerifyRequest.getTotpCode()).isNull();
    }

    @Test
    void testMfaVerifyRequestSessionIdFormat() {
        // 测试sessionId格式
        MfaVerifyRequest mfaVerifyRequest = new MfaVerifyRequest();
        mfaVerifyRequest.setSessionId("mfa-session-123456789");

        assertThat(mfaVerifyRequest.getSessionId()).isEqualTo("mfa-session-123456789");
        assertThat(mfaVerifyRequest.getSessionId()).isNotEmpty();
    }

    @Test
    void testMfaVerifyRequestTotpCodeFormat() {
        // 测试TOTP代码格式（通常是6位数字）
        MfaVerifyRequest mfaVerifyRequest = new MfaVerifyRequest();
        mfaVerifyRequest.setTotpCode("789123");

        assertThat(mfaVerifyRequest.getTotpCode()).isEqualTo("789123");
        assertThat(mfaVerifyRequest.getTotpCode().length()).isEqualTo(6);
    }
}

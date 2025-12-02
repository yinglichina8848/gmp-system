package com.gmp.auth.dto;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * MfaEnableRequest DTO单元测试
 *
 * @author GMP系统开发团队
 */
@ActiveProfiles("test")
class MfaEnableRequestTest {

    @Test
    void testMfaEnableRequestGettersAndSetters() {
        // 创建测试对象
        MfaEnableRequest mfaEnableRequest = new MfaEnableRequest();

        // 验证默认值
        assertThat(mfaEnableRequest.getUsername()).isNull();
        assertThat(mfaEnableRequest.getTotpCode()).isNull();
        assertThat(mfaEnableRequest.getSecretKey()).isNull();

        // 测试setter方法
        mfaEnableRequest.setUsername("testUser");
        mfaEnableRequest.setTotpCode("123456");
        mfaEnableRequest.setSecretKey("ABCDEFGH12345678");

        // 验证setter后的值
        assertThat(mfaEnableRequest.getUsername()).isEqualTo("testUser");
        assertThat(mfaEnableRequest.getTotpCode()).isEqualTo("123456");
        assertThat(mfaEnableRequest.getSecretKey()).isEqualTo("ABCDEFGH12345678");
    }

    @Test
    void testMfaEnableRequestWithValues() {
        // 创建对象并设置值
        MfaEnableRequest mfaEnableRequest = new MfaEnableRequest();
        mfaEnableRequest.setUsername("john.doe");
        mfaEnableRequest.setTotpCode("789456");
        mfaEnableRequest.setSecretKey("HIJKLMNOP98765432");

        // 验证所有字段都被正确设置
        assertThat(mfaEnableRequest.getUsername()).isEqualTo("john.doe");
        assertThat(mfaEnableRequest.getTotpCode()).isEqualTo("789456");
        assertThat(mfaEnableRequest.getSecretKey()).isEqualTo("HIJKLMNOP98765432");
    }

    @Test
    void testMfaEnableRequestNullValues() {
        // 测试null值处理
        MfaEnableRequest mfaEnableRequest = new MfaEnableRequest();
        mfaEnableRequest.setUsername(null);
        mfaEnableRequest.setTotpCode(null);
        mfaEnableRequest.setSecretKey(null);

        assertThat(mfaEnableRequest.getUsername()).isNull();
        assertThat(mfaEnableRequest.getTotpCode()).isNull();
        assertThat(mfaEnableRequest.getSecretKey()).isNull();
    }

    @Test
    void testMfaEnableRequestTotpCodeFormat() {
        // 测试TOTP代码格式（通常是6位数字）
        MfaEnableRequest mfaEnableRequest = new MfaEnableRequest();
        mfaEnableRequest.setTotpCode("123456");

        assertThat(mfaEnableRequest.getTotpCode()).isEqualTo("123456");
        assertThat(mfaEnableRequest.getTotpCode().length()).isEqualTo(6);
    }

    @Test
    void testMfaEnableRequestSecretKeyFormat() {
        // 测试密钥格式（通常是32字符的Base32字符串）
        MfaEnableRequest mfaEnableRequest = new MfaEnableRequest();
        String secretKey = "ABCDEFGH12345678ABCDEFGH12345678";
        mfaEnableRequest.setSecretKey(secretKey);

        assertThat(mfaEnableRequest.getSecretKey()).isEqualTo(secretKey);
        assertThat(mfaEnableRequest.getSecretKey().length()).isEqualTo(32);
    }
}

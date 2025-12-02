package com.gmp.auth.dto;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * PasswordResetRequest DTO单元测试
 *
 * @author GMP系统开发团队
 */
@ActiveProfiles("test")
class PasswordResetRequestTest {

    @Test
    void testPasswordResetRequestGettersAndSetters() {
        // 创建测试对象
        PasswordResetRequest passwordResetRequest = new PasswordResetRequest();

        // 验证默认值
        assertThat(passwordResetRequest.getUsername()).isNull();
        assertThat(passwordResetRequest.getOldPassword()).isNull();
        assertThat(passwordResetRequest.getNewPassword()).isNull();
        assertThat(passwordResetRequest.getConfirmPassword()).isNull();

        // 测试setter方法
        passwordResetRequest.setUsername("testUser");
        passwordResetRequest.setOldPassword("oldPass123");
        passwordResetRequest.setNewPassword("newPass456");
        passwordResetRequest.setConfirmPassword("newPass456");

        // 验证setter后的值
        assertThat(passwordResetRequest.getUsername()).isEqualTo("testUser");
        assertThat(passwordResetRequest.getOldPassword()).isEqualTo("oldPass123");
        assertThat(passwordResetRequest.getNewPassword()).isEqualTo("newPass456");
        assertThat(passwordResetRequest.getConfirmPassword()).isEqualTo("newPass456");
    }

    @Test
    void testPasswordResetRequestWithValues() {
        // 创建对象并设置值
        PasswordResetRequest passwordResetRequest = new PasswordResetRequest();
        passwordResetRequest.setUsername("john.doe");
        passwordResetRequest.setOldPassword("currentPassword123");
        passwordResetRequest.setNewPassword("resetPassword456");
        passwordResetRequest.setConfirmPassword("resetPassword456");

        // 验证所有字段都被正确设置
        assertThat(passwordResetRequest.getUsername()).isEqualTo("john.doe");
        assertThat(passwordResetRequest.getOldPassword()).isEqualTo("currentPassword123");
        assertThat(passwordResetRequest.getNewPassword()).isEqualTo("resetPassword456");
        assertThat(passwordResetRequest.getConfirmPassword()).isEqualTo("resetPassword456");
    }

    @Test
    void testPasswordResetRequestNullValues() {
        // 测试null值处理
        PasswordResetRequest passwordResetRequest = new PasswordResetRequest();
        passwordResetRequest.setUsername(null);
        passwordResetRequest.setOldPassword(null);
        passwordResetRequest.setNewPassword(null);
        passwordResetRequest.setConfirmPassword(null);

        assertThat(passwordResetRequest.getUsername()).isNull();
        assertThat(passwordResetRequest.getOldPassword()).isNull();
        assertThat(passwordResetRequest.getNewPassword()).isNull();
        assertThat(passwordResetRequest.getConfirmPassword()).isNull();
    }

    @Test
    void testPasswordResetRequestPasswordConfirmation() {
        // 测试密码确认匹配
        PasswordResetRequest passwordResetRequest = new PasswordResetRequest();
        passwordResetRequest.setUsername("user123");
        passwordResetRequest.setOldPassword("oldPass");
        passwordResetRequest.setNewPassword("newSecurePass123");
        passwordResetRequest.setConfirmPassword("newSecurePass123");

        // 验证新密码和确认密码匹配
        assertThat(passwordResetRequest.getNewPassword()).isEqualTo(passwordResetRequest.getConfirmPassword());
    }

    @Test
    void testPasswordResetRequestPasswordMismatch() {
        // 测试密码确认不匹配
        PasswordResetRequest passwordResetRequest = new PasswordResetRequest();
        passwordResetRequest.setUsername("user123");
        passwordResetRequest.setOldPassword("oldPass");
        passwordResetRequest.setNewPassword("newSecurePass123");
        passwordResetRequest.setConfirmPassword("differentPass456");

        // 验证新密码和确认密码不匹配
        assertThat(passwordResetRequest.getNewPassword()).isNotEqualTo(passwordResetRequest.getConfirmPassword());
    }

    @Test
    void testPasswordResetRequestUsernameValidation() {
        // 测试用户名格式
        PasswordResetRequest passwordResetRequest = new PasswordResetRequest();
        passwordResetRequest.setUsername("valid.username123");

        assertThat(passwordResetRequest.getUsername()).isEqualTo("valid.username123");
        assertThat(passwordResetRequest.getUsername()).isNotEmpty();
    }
}

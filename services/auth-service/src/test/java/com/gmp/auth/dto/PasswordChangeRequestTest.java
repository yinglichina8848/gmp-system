package com.gmp.auth.dto;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * PasswordChangeRequest DTO单元测试
 *
 * @author GMP系统开发团队
 */
@ActiveProfiles("test")
class PasswordChangeRequestTest {

    @Test
    void testPasswordChangeRequestGettersAndSetters() {
        // 创建测试对象
        PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest();

        // 验证默认值
        assertThat(passwordChangeRequest.getCurrentPassword()).isNull();
        assertThat(passwordChangeRequest.getNewPassword()).isNull();
        assertThat(passwordChangeRequest.getConfirmPassword()).isNull();

        // 测试setter方法
        passwordChangeRequest.setCurrentPassword("oldPass123");
        passwordChangeRequest.setNewPassword("newPass456");
        passwordChangeRequest.setConfirmPassword("newPass456");

        // 验证setter后的值
        assertThat(passwordChangeRequest.getCurrentPassword()).isEqualTo("oldPass123");
        assertThat(passwordChangeRequest.getNewPassword()).isEqualTo("newPass456");
        assertThat(passwordChangeRequest.getConfirmPassword()).isEqualTo("newPass456");
    }

    @Test
    void testPasswordChangeRequestWithValues() {
        // 创建对象并设置值
        PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest();
        passwordChangeRequest.setCurrentPassword("currentPassword123");
        passwordChangeRequest.setNewPassword("newSecurePassword456");
        passwordChangeRequest.setConfirmPassword("newSecurePassword456");

        // 验证所有字段都被正确设置
        assertThat(passwordChangeRequest.getCurrentPassword()).isEqualTo("currentPassword123");
        assertThat(passwordChangeRequest.getNewPassword()).isEqualTo("newSecurePassword456");
        assertThat(passwordChangeRequest.getConfirmPassword()).isEqualTo("newSecurePassword456");
    }

    @Test
    void testPasswordChangeRequestNullValues() {
        // 测试null值处理
        PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest();
        passwordChangeRequest.setCurrentPassword(null);
        passwordChangeRequest.setNewPassword(null);
        passwordChangeRequest.setConfirmPassword(null);

        assertThat(passwordChangeRequest.getCurrentPassword()).isNull();
        assertThat(passwordChangeRequest.getNewPassword()).isNull();
        assertThat(passwordChangeRequest.getConfirmPassword()).isNull();
    }

    @Test
    void testPasswordChangeRequestOldPasswordCompatibility() {
        // 测试getOldPassword()方法的兼容性
        PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest();
        passwordChangeRequest.setCurrentPassword("currentPass");

        // getOldPassword应该返回与getCurrentPassword相同的值
        assertThat(passwordChangeRequest.getOldPassword()).isEqualTo("currentPass");
        assertThat(passwordChangeRequest.getOldPassword()).isEqualTo(passwordChangeRequest.getCurrentPassword());
    }

    @Test
    void testPasswordChangeRequestPasswordMatching() {
        // 测试密码匹配场景
        PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest();
        passwordChangeRequest.setCurrentPassword("oldPass");
        passwordChangeRequest.setNewPassword("newPass123");
        passwordChangeRequest.setConfirmPassword("newPass123");

        // 验证新密码和确认密码匹配
        assertThat(passwordChangeRequest.getNewPassword()).isEqualTo(passwordChangeRequest.getConfirmPassword());
    }

    @Test
    void testPasswordChangeRequestPasswordNotMatching() {
        // 测试密码不匹配场景
        PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest();
        passwordChangeRequest.setCurrentPassword("oldPass");
        passwordChangeRequest.setNewPassword("newPass123");
        passwordChangeRequest.setConfirmPassword("differentPass456");

        // 验证新密码和确认密码不匹配
        assertThat(passwordChangeRequest.getNewPassword()).isNotEqualTo(passwordChangeRequest.getConfirmPassword());
    }
}

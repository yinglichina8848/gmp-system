package com.gmp.auth.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.assertj.core.data.Offset;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.offset;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * 密码策略服务单元测试
 * 覆盖密码复杂度验证、历史记录检查、过期管理等所有功能
 * 
 * 测试覆盖范围：
 * - 密码复杂度验证
 * - 密码历史记录检查
 * - 密码过期时间计算
 * - 密码即将过期检查
 * - 密码要求描述生成
 * - 边界条件和异常情况
 *
 * @author GMP系统开发团队
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("密码策略服务单元测试")
class PasswordPolicyServiceTest {

    @Mock
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Mock
    private UserPasswordHistoryService userPasswordHistoryService;

    @InjectMocks
    private PasswordPolicyService passwordPolicyService;

    @BeforeEach
    void setUp() {
        // 设置测试配置值
        ReflectionTestUtils.setField(passwordPolicyService, "minPasswordLength", 10);
        ReflectionTestUtils.setField(passwordPolicyService, "passwordExpiryDays", 90);
        ReflectionTestUtils.setField(passwordPolicyService, "passwordHistoryCount", 5);
    }

    @Test
    @DisplayName("密码复杂度验证 - 有效密码")
    void testValidatePasswordComplexityValidPassword() {
        // Given
        String validPassword = "Test123!@#";

        // When
        String result = passwordPolicyService.validatePasswordComplexity(validPassword);

        // Then
        assertThat(result).isNull(); // null表示验证通过
    }

    @Test
    @DisplayName("密码复杂度验证 - 密码过短")
    void testValidatePasswordComplexityTooShort() {
        // Given
        String shortPassword = "Test1!";

        // When
        String result = passwordPolicyService.validatePasswordComplexity(shortPassword);

        // Then
        assertThat(result).isEqualTo("密码长度必须至少为10个字符");
    }

    @Test
    @DisplayName("密码复杂度验证 - 缺少数字")
    void testValidatePasswordComplexityNoDigit() {
        // Given
        String passwordWithoutDigit = "TestPassword!";

        // When
        String result = passwordPolicyService.validatePasswordComplexity(passwordWithoutDigit);

        // Then
        assertThat(result).isEqualTo("密码必须包含至少一个数字");
    }

    @Test
    @DisplayName("密码复杂度验证 - 缺少小写字母")
    void testValidatePasswordComplexityNoLowercase() {
        // Given
        String passwordWithoutLowercase = "TEST123!@#";

        // When
        String result = passwordPolicyService.validatePasswordComplexity(passwordWithoutLowercase);

        // Then
        assertThat(result).isEqualTo("密码必须包含至少一个小写字母");
    }

    @Test
    @DisplayName("密码复杂度验证 - 缺少大写字母")
    void testValidatePasswordComplexityNoUppercase() {
        // Given
        String passwordWithoutUppercase = "test123!@#";

        // When
        String result = passwordPolicyService.validatePasswordComplexity(passwordWithoutUppercase);

        // Then
        assertThat(result).isEqualTo("密码必须包含至少一个大写字母");
    }

    @Test
    @DisplayName("密码复杂度验证 - 缺少特殊字符")
    void testValidatePasswordComplexityNoSpecial() {
        // Given
        String passwordWithoutSpecial = "Test123456";

        // When
        String result = passwordPolicyService.validatePasswordComplexity(passwordWithoutSpecial);

        // Then
        assertThat(result).isEqualTo("密码必须包含至少一个特殊字符");
    }

    @Test
    @DisplayName("密码复杂度验证 - null密码")
    void testValidatePasswordComplexityNullPassword() {
        // When
        String result = passwordPolicyService.validatePasswordComplexity(null);

        // Then
        assertThat(result).isEqualTo("密码长度必须至少为10个字符");
    }

    @Test
    @DisplayName("密码历史记录检查 - 密码在历史中")
    void testIsPasswordInHistoryPasswordExists() {
        // Given
        Long userId = 1L;
        String rawPassword = "Test123!@#";
        List<String> passwordHistory = Arrays.asList(
                "encodedHash1",
                "encodedHash2",
                "encodedHash3"
        );

        when(userPasswordHistoryService.getPasswordHistory(userId, 5)).thenReturn(passwordHistory);
        when(passwordEncoder.matches(rawPassword, "encodedHash1")).thenReturn(true);

        // When
        boolean result = passwordPolicyService.isPasswordInHistory(userId, rawPassword);

        // Then
        assertThat(result).isTrue();
        verify(passwordEncoder).matches(rawPassword, "encodedHash1");
    }

    @Test
    @DisplayName("密码历史记录检查 - 密码不在历史中")
    void testIsPasswordInHistoryPasswordNotExists() {
        // Given
        Long userId = 1L;
        String rawPassword = "NewTest123!@#";
        List<String> passwordHistory = Arrays.asList(
                "encodedHash1",
                "encodedHash2",
                "encodedHash3"
        );

        when(userPasswordHistoryService.getPasswordHistory(userId, 5)).thenReturn(passwordHistory);
        when(passwordEncoder.matches(rawPassword, "encodedHash1")).thenReturn(false);
        when(passwordEncoder.matches(rawPassword, "encodedHash2")).thenReturn(false);
        when(passwordEncoder.matches(rawPassword, "encodedHash3")).thenReturn(false);

        // When
        boolean result = passwordPolicyService.isPasswordInHistory(userId, rawPassword);

        // Then
        assertThat(result).isFalse();
        verify(passwordEncoder, times(3)).matches(eq(rawPassword), anyString());
    }

    @Test
    @DisplayName("密码历史记录检查 - 空历史记录")
    void testIsPasswordInHistoryEmptyHistory() {
        // Given
        Long userId = 1L;
        String rawPassword = "Test123!@#";

        when(userPasswordHistoryService.getPasswordHistory(userId, 5)).thenReturn(Collections.emptyList());

        // When
        boolean result = passwordPolicyService.isPasswordInHistory(userId, rawPassword);

        // Then
        assertThat(result).isFalse();
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    @DisplayName("计算密码过期时间")
    void testCalculatePasswordExpiryTime() {
        // When
        LocalDateTime expiryTime = passwordPolicyService.calculatePasswordExpiryTime();

        // Then
        LocalDateTime expectedTime = LocalDateTime.now().plusDays(90);
        // 使用AssertJ的offset方法替代Duration
        assertThat(expiryTime).isCloseTo(expectedTime, within(1, ChronoUnit.SECONDS));
    }

    @Test
    @DisplayName("密码即将过期检查 - 即将过期")
    void testIsPasswordExpiringSoonTrue() {
        // Given
        LocalDateTime passwordExpiredAt = LocalDateTime.now().plusDays(5); // 5天后过期，在7天内

        // When
        boolean result = passwordPolicyService.isPasswordExpiringSoon(passwordExpiredAt);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("密码即将过期检查 - 不即将过期")
    void testIsPasswordExpiringSoonFalse() {
        // Given
        LocalDateTime passwordExpiredAt = LocalDateTime.now().plusDays(10); // 10天后过期，不在7天内

        // When
        boolean result = passwordPolicyService.isPasswordExpiringSoon(passwordExpiredAt);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("密码即将过期检查 - 已过期")
    void testIsPasswordExpiringSoonAlreadyExpired() {
        // Given
        LocalDateTime passwordExpiredAt = LocalDateTime.now().minusDays(1); // 已过期

        // When
        boolean result = passwordPolicyService.isPasswordExpiringSoon(passwordExpiredAt);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("密码即将过期检查 - null过期时间")
    void testIsPasswordExpiringSoonNullExpiryTime() {
        // When
        boolean result = passwordPolicyService.isPasswordExpiringSoon(null);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("记录密码历史")
    void testRecordPasswordHistory() {
        // Given
        Long userId = 1L;
        String hashedPassword = "encodedNewPassword";

        // When
        passwordPolicyService.recordPasswordHistory(userId, hashedPassword);

        // Then
        verify(userPasswordHistoryService).recordPasswordHistory(userId, hashedPassword);
    }

    @Test
    @DisplayName("验证密码 - 有效密码")
    void testValidatePasswordValid() {
        // Given
        String validPassword = "Test123!@#";

        // When
        boolean result = passwordPolicyService.validatePassword(validPassword);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("验证密码 - 无效密码")
    void testValidatePasswordInvalid() {
        // Given
        String invalidPassword = "weak";

        // When
        boolean result = passwordPolicyService.validatePassword(invalidPassword);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("获取密码复杂度要求描述")
    void testGetPasswordComplexityRequirements() {
        // When
        String requirements = passwordPolicyService.getPasswordComplexityRequirements();

        // Then
        assertThat(requirements).contains("密码必须满足以下要求");
        assertThat(requirements).contains("长度至少8个字符");
        assertThat(requirements).contains("包含至少一个数字");
        assertThat(requirements).contains("包含至少一个小写字母");
        assertThat(requirements).contains("包含至少一个大写字母");
        assertThat(requirements).contains("包含至少一个特殊字符");
    }

    @Test
    @DisplayName("获取密码要求描述")
    void testGetPasswordRequirementsDescription() {
        // When
        String description = passwordPolicyService.getPasswordRequirementsDescription();

        // Then
        assertThat(description).contains("至少包含8个字符");
        assertThat(description).contains("大写字母");
        assertThat(description).contains("小写字母");
        assertThat(description).contains("数字");
        assertThat(description).contains("特殊字符");
    }

    @Test
    @DisplayName("边界测试 - 最小长度密码")
    void testEdgeCaseMinimumLengthPassword() {
        // Given
        String minLengthPassword = "Test123!@#"; // 正好10个字符

        // When
        String result = passwordPolicyService.validatePasswordComplexity(minLengthPassword);

        // Then
        assertThat(result).isNull(); // 应该通过验证
    }

    @Test
    @DisplayName("边界测试 - 特殊字符边界情况")
    void testEdgeCaseSpecialCharacters() {
        // 测试各种特殊字符组合
        String[] validPasswords = {
                "Test123!@#$%^&*()",
                "Test123,.?\":{}|<>",
                "Test123[]{}()'\"",
                "Test123+-=_~`"
        };

        for (String password : validPasswords) {
            // When
            String result = passwordPolicyService.validatePasswordComplexity(password);

            // Then
            assertThat(result).as("Password '%s' should be valid", password).isNull();
        }
    }

    @Test
    @DisplayName("边界测试 - Unicode字符")
    void testEdgeCaseUnicodeCharacters() {
        // Given
        String unicodePassword = "测试123!@#";

        // When
        String result = passwordPolicyService.validatePasswordComplexity(unicodePassword);

        // Then
        // Unicode字符应该被正确处理
        assertThat(result).isNotNull(); // 可能因为缺少ASCII字母而失败
    }

    @Test
    @DisplayName("性能测试 - 长密码验证")
    void testPerformanceLongPassword() {
        // Given
        StringBuilder longPassword = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            longPassword.append("a");
        }
        longPassword.append("A1!"); // 添加必要的字符类型

        // When
        long startTime = System.currentTimeMillis();
        String result = passwordPolicyService.validatePasswordComplexity(longPassword.toString());
        long endTime = System.currentTimeMillis();

        // Then
        assertThat(endTime - startTime).isLessThan(100); // 应该在100ms内完成
        assertThat(result).isNull(); // 应该通过验证
    }

    @Test
    @DisplayName("异常情况 - 空密码历史列表")
    void testExceptionCaseNullPasswordHistory() {
        // Given
        Long userId = 1L;
        String rawPassword = "Test123!@#";

        when(userPasswordHistoryService.getPasswordHistory(userId, 5)).thenReturn(null);

        // When
        boolean result = passwordPolicyService.isPasswordInHistory(userId, rawPassword);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("配置测试 - 不同最小长度配置")
    void testConfigurationDifferentMinLength() {
        // Given
        ReflectionTestUtils.setField(passwordPolicyService, "minPasswordLength", 12);
        String password = "Test123!@#"; // 10个字符

        // When
        String result = passwordPolicyService.validatePasswordComplexity(password);

        // Then
        assertThat(result).isEqualTo("密码长度必须至少为12个字符");
    }

    @Test
    @DisplayName("配置测试 - 不同过期天数配置")
    void testConfigurationDifferentExpiryDays() {
        // Given
        ReflectionTestUtils.setField(passwordPolicyService, "passwordExpiryDays", 60);

        // When
        LocalDateTime expiryTime = passwordPolicyService.calculatePasswordExpiryTime();

        // Then
        LocalDateTime expectedTime = LocalDateTime.now().plusDays(60);
        // 简化时间比较逻辑，直接比较年月日
        assertThat(expiryTime.getYear()).isEqualTo(expectedTime.getYear());
        assertThat(expiryTime.getMonth()).isEqualTo(expectedTime.getMonth());
        assertThat(expiryTime.getDayOfMonth()).isEqualTo(expectedTime.getDayOfMonth());
    }
}
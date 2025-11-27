package com.gmp.auth.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

/**
 * MfaService的单元测试类
 */
@ExtendWith(MockitoExtension.class)
public class MfaServiceTest {

    @InjectMocks
    private MfaService mfaService;

    @Mock
    private TotpUtils totpUtils;

    private String testSecretKey;
    private String testUsername;
    private String testIssuer;
    private String testTotpCode;
    private String testRecoveryCode;

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        testSecretKey = "HXDMVJECJJWSRB3HWIZR4IFUGFTMXKJA";
        testUsername = "testuser";
        testIssuer = "GMP系统";
        testTotpCode = "123456";
        testRecoveryCode = "12345678"; // 8位数字
    }

    @Test
    @DisplayName("测试生成MFA密钥")
    void testGenerateSecretKey() {
        // Given
        when(totpUtils.generateSecretKey()).thenReturn(testSecretKey);

        // When
        String secretKey = mfaService.generateSecretKey();

        // Then
        assertThat(secretKey).isNotNull();
        assertThat(secretKey).isEqualTo(testSecretKey);
        verify(totpUtils).generateSecretKey();
    }

    @Test
    @DisplayName("测试生成TOTP验证码")
    void testGenerateTotpCode() {
        // Given
        when(totpUtils.generateTotpCode(testSecretKey)).thenReturn(testTotpCode);

        // When
        String totpCode = mfaService.generateTotpCode(testSecretKey);

        // Then
        assertThat(totpCode).isNotNull();
        assertThat(totpCode).isEqualTo(testTotpCode);
        verify(totpUtils).generateTotpCode(testSecretKey);
    }

    @Test
    @DisplayName("测试验证TOTP验证码 - 成功")
    void testVerifyTotpCode_Success() {
        // Given
        when(totpUtils.verifyTotpCode(testSecretKey, testTotpCode)).thenReturn(true);

        // When
        boolean result = mfaService.verifyTotpCode(testSecretKey, testTotpCode);

        // Then
        assertThat(result).isTrue();
        verify(totpUtils).verifyTotpCode(testSecretKey, testTotpCode);
    }

    @Test
    @DisplayName("测试验证TOTP验证码 - 失败")
    void testVerifyTotpCode_Failure() {
        // Given
        String invalidCode = "654321";
        when(totpUtils.verifyTotpCode(testSecretKey, invalidCode)).thenReturn(false);

        // When
        boolean result = mfaService.verifyTotpCode(testSecretKey, invalidCode);

        // Then
        assertThat(result).isFalse();
        verify(totpUtils).verifyTotpCode(testSecretKey, invalidCode);
    }

    @Test
    @DisplayName("测试生成恢复码")
    void testGenerateRecoveryCodes() {
        // When
        List<String> recoveryCodes = mfaService.generateRecoveryCodes();

        // Then
        assertThat(recoveryCodes).isNotNull();
        assertThat(recoveryCodes.size()).isEqualTo(10);
        for (String code : recoveryCodes) {
            assertThat(code).hasSize(8); // 8位数字
            assertThat(code).matches("\\d{8}"); // 只包含数字
        }
    }

    @Test
    @DisplayName("测试生成二维码URL")
    void testGenerateQrCodeUrl() {
        // Given
        String expectedUrl = "otpauth://totp/GMP系统:testuser?secret=HXDMVJECJJWSRB3HWIZR4IFUGFTMXKJA&issuer=GMP系统";
        when(totpUtils.generateTotpUri(testSecretKey, testUsername, testIssuer)).thenReturn(expectedUrl);

        // When
        String qrUrl = mfaService.generateQrCodeUrl(testUsername, testSecretKey, testIssuer);

        // Then
        assertThat(qrUrl).isNotNull();
        assertThat(qrUrl).isEqualTo(expectedUrl);
        verify(totpUtils).generateTotpUri(testSecretKey, testUsername, testIssuer);
    }

    @Test
    @DisplayName("测试获取TOTP剩余有效期")
    void testGetRemainingSeconds() {
        // Given
        int expectedSeconds = 25;
        when(totpUtils.getRemainingSeconds()).thenReturn(expectedSeconds);

        // When
        int remainingSeconds = mfaService.getRemainingSeconds();

        // Then
        assertThat(remainingSeconds).isEqualTo(expectedSeconds);
        verify(totpUtils).getRemainingSeconds();
    }

    @Test
    @DisplayName("测试验证恢复码 - 成功")
    void testVerifyRecoveryCode_Success() {
        // Given
        List<String> userRecoveryCodes = List.of("12345678", "87654321", "11111111");
        String validCode = "12345678";

        // When
        boolean result = mfaService.verifyRecoveryCode(userRecoveryCodes, validCode);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("测试验证恢复码 - 失败")
    void testVerifyRecoveryCode_Failure() {
        // Given
        List<String> userRecoveryCodes = List.of("12345678", "87654321", "11111111");
        String invalidCode = "99999999";

        // When
        boolean result = mfaService.verifyRecoveryCode(userRecoveryCodes, invalidCode);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("测试验证恢复码 - 无效格式")
    void testVerifyRecoveryCode_InvalidFormat() {
        // Given
        List<String> userRecoveryCodes = List.of("12345678", "87654321");
        String invalidLengthCode = "1234567"; // 长度不足

        // When
        boolean result = mfaService.verifyRecoveryCode(userRecoveryCodes, invalidLengthCode);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("测试验证恢复码 - null参数")
    void testVerifyRecoveryCode_NullParams() {
        // Given
        List<String> userRecoveryCodes = List.of("12345678");

        // When
        boolean result1 = mfaService.verifyRecoveryCode(null, "12345678");
        boolean result2 = mfaService.verifyRecoveryCode(userRecoveryCodes, null);

        // Then
        assertThat(result1).isFalse();
        assertThat(result2).isFalse();
    }

    @Test
    @DisplayName("测试移除已使用的恢复码")
    void testRemoveUsedRecoveryCode() {
        // Given
        List<String> originalCodes = List.of("12345678", "87654321", "11111111");
        String usedCode = "12345678";

        // When
        List<String> updatedCodes = mfaService.removeUsedRecoveryCode(originalCodes, usedCode);

        // Then
        assertThat(updatedCodes).isNotNull();
        assertThat(updatedCodes.size()).isEqualTo(2);
        assertThat(updatedCodes).doesNotContain(usedCode);
        assertThat(updatedCodes).contains("87654321", "11111111");
        // 验证原始列表未被修改
        assertThat(originalCodes.size()).isEqualTo(3);
        assertThat(originalCodes).contains(usedCode);
    }

    @Test
    @DisplayName("测试移除不存在的恢复码")
    void testRemoveNonExistentRecoveryCode() {
        // Given
        List<String> originalCodes = List.of("12345678", "87654321");
        String nonExistentCode = "99999999";

        // When
        List<String> updatedCodes = mfaService.removeUsedRecoveryCode(originalCodes, nonExistentCode);

        // Then
        assertThat(updatedCodes).isNotNull();
        assertThat(updatedCodes.size()).isEqualTo(2);
        assertThat(updatedCodes).containsExactlyElementsOf(originalCodes);
    }

    @Test
    @DisplayName("测试移除已使用的恢复码 - 空列表")
    void testRemoveUsedRecoveryCode_EmptyList() {
        // Given
        List<String> emptyCodes = List.of();
        String usedCode = "12345678";

        // When
        List<String> updatedCodes = mfaService.removeUsedRecoveryCode(emptyCodes, usedCode);

        // Then
        assertThat(updatedCodes).isNotNull();
        assertThat(updatedCodes.size()).isEqualTo(0);
    }
}

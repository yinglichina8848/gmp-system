package com.gmp.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * MFA服务类，提供多因素认证相关功能
 */
@Service
public class MfaService {
    private static final int RECOVERY_CODES_COUNT = 10;
    private static final int RECOVERY_CODE_LENGTH = 8;
    private final SecureRandom secureRandom = new SecureRandom();
    private final TotpUtils totpUtils;

    @Autowired
    public MfaService(TotpUtils totpUtils) {
        this.totpUtils = totpUtils;
    }

    /**
     * 生成新的MFA密钥
     */
    public String generateSecretKey() {
        return totpUtils.generateSecretKey();
    }

    /**
     * 生成TOTP验证码
     */
    public String generateTotpCode(String secretKey) {
        return totpUtils.generateTotpCode(secretKey);
    }

    /**
     * 验证TOTP验证码
     */
    public boolean verifyTotpCode(String secretKey, String code) {
        return totpUtils.verifyTotpCode(secretKey, code);
    }

    /**
     * 生成指定数量的恢复码
     */
    public List<String> generateRecoveryCodes() {
        List<String> recoveryCodes = new ArrayList<>();
        for (int i = 0; i < RECOVERY_CODES_COUNT; i++) {
            recoveryCodes.add(generateRecoveryCode());
        }
        return recoveryCodes;
    }

    /**
     * 生成单个恢复码
     */
    private String generateRecoveryCode() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < RECOVERY_CODE_LENGTH; i++) {
            sb.append(secureRandom.nextInt(10));
        }
        return sb.toString();
    }

    /**
     * 生成用于Google Authenticator等应用的二维码URL
     */
    public String generateQrCodeUrl(String username, String secretKey, String issuer) {
        return totpUtils.generateTotpUri(secretKey, username, issuer);
    }

    /**
     * 获取TOTP验证码的剩余有效期（秒）
     */
    public int getRemainingSeconds() {
        return totpUtils.getRemainingSeconds();
    }

    /**
     * 验证恢复码是否有效
     * @param userRecoveryCodes 用户存储的恢复码
     * @param providedCode 用户提供的恢复码
     * @return 是否有效
     */
    public boolean verifyRecoveryCode(List<String> userRecoveryCodes, String providedCode) {
        if (userRecoveryCodes == null || providedCode == null || providedCode.length() != RECOVERY_CODE_LENGTH) {
            return false;
        }
        return userRecoveryCodes.contains(providedCode);
    }

    /**
     * 从用户的恢复码列表中移除已使用的恢复码
     * @param recoveryCodes 用户的恢复码列表
     * @param usedCode 已使用的恢复码
     * @return 更新后的恢复码列表
     */
    public List<String> removeUsedRecoveryCode(List<String> recoveryCodes, String usedCode) {
        List<String> updatedCodes = new ArrayList<>(recoveryCodes);
        updatedCodes.remove(usedCode);
        return updatedCodes;
    }
}
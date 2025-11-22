package com.gmp.auth.service;

import org.springframework.stereotype.Service;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.*;

@Service
public class MfaService {
    private static final int SECRET_KEY_LENGTH = 16; // 16 bytes = 128 bits
    private static final int TOTP_TIME_STEP = 30; // 30 seconds
    private static final int TOTP_CODE_LENGTH = 6; // 6 digits
    private static final int RECOVERY_CODES_COUNT = 10;
    private static final int RECOVERY_CODE_LENGTH = 8;

    private final SecureRandom secureRandom = new SecureRandom();

    /**
     * 生成新的MFA密钥
     */
    public String generateSecretKey() {
        byte[] key = new byte[SECRET_KEY_LENGTH];
        secureRandom.nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
    }

    /**
     * 生成TOTP验证码
     */
    public String generateTotpCode(String secretKey) {
        return generateTotpCode(secretKey, Instant.now().getEpochSecond());
    }

    /**
     * 验证TOTP验证码
     */
    public boolean verifyTotpCode(String secretKey, String code) {
        try {
            // 验证当前时间戳和前后一个时间戳的验证码，以允许时钟偏差
            long currentTime = Instant.now().getEpochSecond();
            return verifyTotpCode(secretKey, code, currentTime) ||
                   verifyTotpCode(secretKey, code, currentTime - TOTP_TIME_STEP) ||
                   verifyTotpCode(secretKey, code, currentTime + TOTP_TIME_STEP);
        } catch (Exception e) {
            return false;
        }
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
        String label = issuer + ":" + username;
        String encodedLabel = Base64.getEncoder().encodeToString(label.getBytes(StandardCharsets.UTF_8));
        String encodedSecret = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
        return String.format("otpauth://totp/%s?secret=%s&issuer=%s&algorithm=SHA1&digits=6&period=30",
                encodedLabel, encodedSecret, issuer);
    }

    /**
     * 内部方法：根据指定时间戳生成TOTP码
     */
    private String generateTotpCode(String secretKey, long timestamp) {
        try {
            byte[] key = Base64.getDecoder().decode(secretKey);
            long timeStep = timestamp / TOTP_TIME_STEP;
            
            // 将时间戳转换为字节数组
            ByteBuffer buffer = ByteBuffer.allocate(8);
            buffer.putLong(0, timeStep);
            byte[] timeBytes = buffer.array();
            
            // 使用HMAC-SHA1算法计算
            byte[] hmacResult = hmacSha1(key, timeBytes);
            
            // 动态截断
            int offset = hmacResult[hmacResult.length - 1] & 0xF;
            int binary = ((hmacResult[offset] & 0x7F) << 24) |
                        ((hmacResult[offset + 1] & 0xFF) << 16) |
                        ((hmacResult[offset + 2] & 0xFF) << 8) |
                        (hmacResult[offset + 3] & 0xFF);
            
            // 生成6位验证码
            int otp = binary % (int) Math.pow(10, TOTP_CODE_LENGTH);
            return String.format("%0" + TOTP_CODE_LENGTH + "d", otp);
        } catch (Exception e) {
            throw new RuntimeException("生成TOTP码失败", e);
        }
    }

    /**
     * 内部方法：验证指定时间戳的TOTP码
     */
    private boolean verifyTotpCode(String secretKey, String code, long timestamp) {
        String expectedCode = generateTotpCode(secretKey, timestamp);
        return expectedCode.equals(code);
    }

    /**
     * 内部方法：计算HMAC-SHA1
     */
    private byte[] hmacSha1(byte[] key, byte[] data) throws Exception {
        javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA1");
        javax.crypto.spec.SecretKeySpec secretKeySpec = new javax.crypto.spec.SecretKeySpec(key, "HmacSHA1");
        mac.init(secretKeySpec);
        return mac.doFinal(data);
    }
}
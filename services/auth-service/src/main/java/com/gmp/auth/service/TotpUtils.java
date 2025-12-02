package com.gmp.auth.service;

import org.springframework.stereotype.Component;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;

/**
 * TOTP工具类，提供基础的基于时间的一次性密码生成和验证功能
 */
@Component
public class TotpUtils {
    private static final int TOTP_TIME_STEP = 30; // 30秒
    private static final int TOTP_CODE_LENGTH = 6; // 6位数字
    private static final int SECRET_KEY_LENGTH = 16; // 16字节 = 128位
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    /**
     * 生成随机的TOTP密钥
     * @return Base64编码的密钥
     */
    public String generateSecretKey() {
        byte[] key = new byte[SECRET_KEY_LENGTH];
        SECURE_RANDOM.nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
    }

    /**
     * 生成当前时间的TOTP码
     * @param secretKey 密钥
     * @return 6位TOTP码
     */
    public String generateTotpCode(String secretKey) {
        return generateTotpCode(secretKey, Instant.now().getEpochSecond());
    }

    /**
     * 生成指定时间戳的TOTP码
     * @param secretKey 密钥
     * @param timestamp 时间戳（秒）
     * @return 6位TOTP码
     */
    public String generateTotpCode(String secretKey, long timestamp) {
        try {
            if (secretKey == null || secretKey.isEmpty()) {
                return null;
            }
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
            return null;
        }
    }

    /**
     * 验证TOTP码（包含时间容错）
     * @param secretKey 密钥
     * @param code 用户输入的验证码
     * @return 是否验证成功
     */
    public boolean verifyTotpCode(String secretKey, String code) {
        try {
            // 验证当前时间戳和前后一个时间戳的验证码，以允许时钟偏差
            long currentTime = Instant.now().getEpochSecond();
            return verifyTotpCodeAtTimestamp(secretKey, code, currentTime) ||
                   verifyTotpCodeAtTimestamp(secretKey, code, currentTime - TOTP_TIME_STEP) ||
                   verifyTotpCodeAtTimestamp(secretKey, code, currentTime + TOTP_TIME_STEP);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 在指定时间戳验证TOTP码
     * @param secretKey 密钥
     * @param code 用户输入的验证码
     * @param timestamp 时间戳
     * @return 是否验证成功
     */
    public boolean verifyTotpCodeAtTimestamp(String secretKey, String code, long timestamp) {
        if (code == null || code.length() != TOTP_CODE_LENGTH) {
            return false;
        }
        
        String expectedCode = generateTotpCode(secretKey, timestamp);
        return expectedCode.equals(code);
    }

    /**
     * 生成TOTP二维码的URI（符合Google Authenticator格式）
     * @param secretKey 密钥
     * @param accountName 账号名
     * @param issuer 发行商名称
     * @return 二维码URI
     */
    public String generateTotpUri(String secretKey, String accountName, String issuer) {
        String encodedSecret = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
        String label = String.format("%s:%s", issuer, accountName).replace(" ", "%20");
        return String.format("otpauth://totp/%s?secret=%s&issuer=%s&algorithm=SHA1&digits=6&period=30",
                label, encodedSecret, issuer);
    }

    /**
     * 计算HMAC-SHA1哈希值
     * @param key 密钥
     * @param data 数据
     * @return 哈希结果
     * @throws Exception 计算过程中的异常
     */
    private byte[] hmacSha1(byte[] key, byte[] data) throws Exception {
        javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA1");
        javax.crypto.spec.SecretKeySpec secretKeySpec = new javax.crypto.spec.SecretKeySpec(key, "HmacSHA1");
        mac.init(secretKeySpec);
        return mac.doFinal(data);
    }

    /**
     * 获取当前时间窗口
     * @return 当前时间窗口索引
     */
    public long getCurrentTimeWindow() {
        return Instant.now().getEpochSecond() / TOTP_TIME_STEP;
    }

    /**
     * 获取剩余有效期（秒）
     * @return 剩余有效期
     */
    public int getRemainingSeconds() {
        return TOTP_TIME_STEP - (int)(Instant.now().getEpochSecond() % TOTP_TIME_STEP);
    }
}
package com.gmp.auth.util;

import org.springframework.stereotype.Component;

/**
 * 基于时间的一次性密码工具类
 */
@Component
public class TotpUtils {
    
    // 验证TOTP码
    public boolean verifyTotp(String secret, String totpCode) {
        // 简单实现，实际项目中应使用标准的TOTP验证算法
        return totpCode != null && totpCode.length() == 6;
    }
    
    // 生成TOTP密钥
    public String generateSecretKey() {
        // 简单实现，实际项目中应使用安全的随机密钥生成
        return "mock-totp-secret-key";
    }
    
    // 生成二维码URL
    public String generateQrCodeUrl(String secret, String username) {
        // 简单实现，生成用于扫描的二维码URL
        return "otpauth://totp/GMP:" + username + "?secret=" + secret + "&issuer=GMP";
    }
}
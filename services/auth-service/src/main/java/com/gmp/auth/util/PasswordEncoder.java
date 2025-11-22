package com.gmp.auth.util;

import org.springframework.stereotype.Component;

/**
 * 密码编码器
 */
@Component
public class PasswordEncoder {
    
    // 加密密码
    public String encode(String rawPassword) {
        // 简单实现，实际项目中应使用BCrypt等安全的哈希算法
        return "encoded:" + rawPassword;
    }
    
    // 验证密码
    public boolean matches(String rawPassword, String encodedPassword) {
        // 简单实现，实际项目中应使用对应的哈希算法验证
        return encodedPassword.equals("encoded:" + rawPassword);
    }
}
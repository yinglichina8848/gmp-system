package com.gmp.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 密码策略服务
 * 负责密码复杂度验证、历史记录检查和密码过期管理
 */
@Service
public class PasswordPolicyService {

    // 密码策略常量
    private static final int MIN_PASSWORD_LENGTH = 8;
    
    private static final Logger log = LoggerFactory.getLogger(PasswordPolicyService.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserPasswordHistoryService userPasswordHistoryService;

    // 密码最小长度
    @Value("${auth.password.min-length:10}")
    private int minPasswordLength;

    // 密码过期天数
    @Value("${auth.password.expiry-days:90}")
    private int passwordExpiryDays;

    // 历史密码记录数
    @Value("${auth.password.history-count:5}")
    private int passwordHistoryCount;

    // 复杂度要求：必须包含数字
    private static final Pattern HAS_DIGIT = Pattern.compile("\\d");
    // 复杂度要求：必须包含小写字母
    private static final Pattern HAS_LOWERCASE = Pattern.compile("[a-z]");
    // 复杂度要求：必须包含大写字母
    private static final Pattern HAS_UPPERCASE = Pattern.compile("[A-Z]");
    // 复杂度要求：必须包含特殊字符
    private static final Pattern HAS_SPECIAL = Pattern.compile("[!@#$%^&*(),.?\":{}|<>]");

    /**
     * 验证密码复杂度
     * @param password 待验证的密码
     * @return 验证结果，null表示验证通过，否则返回错误消息
     */
    public String validatePasswordComplexity(String password) {
        if (password == null || password.length() < minPasswordLength) {
            return "密码长度必须至少为" + minPasswordLength + "个字符";
        }

        if (!HAS_DIGIT.matcher(password).find()) {
            return "密码必须包含至少一个数字";
        }

        if (!HAS_LOWERCASE.matcher(password).find()) {
            return "密码必须包含至少一个小写字母";
        }

        if (!HAS_UPPERCASE.matcher(password).find()) {
            return "密码必须包含至少一个大写字母";
        }

        if (!HAS_SPECIAL.matcher(password).find()) {
            return "密码必须包含至少一个特殊字符";
        }

        return null; // 验证通过
    }

    /**
     * 检查密码是否在历史记录中
     * @param userId 用户ID
     * @param rawPassword 原始密码
     * @return 检查结果，true表示密码在历史记录中，false表示不在
     */
    public boolean isPasswordInHistory(Long userId, String rawPassword) {
        List<String> passwordHistory = userPasswordHistoryService.getPasswordHistory(userId, passwordHistoryCount);
        
        for (String hashedPassword : passwordHistory) {
            if (passwordEncoder.matches(rawPassword, hashedPassword)) {
                log.warn("密码与历史密码重复 - 用户ID: {}", userId);
                return true;
            }
        }
        return false;
    }

    /**
     * 计算密码过期时间
     * @return 密码过期时间
     */
    public LocalDateTime calculatePasswordExpiryTime() {
        return LocalDateTime.now().plusDays(passwordExpiryDays);
    }

    /**
     * 检查密码是否即将过期（提前7天提醒）
     * @param passwordExpiredAt 密码过期时间
     * @return 是否即将过期
     */
    public boolean isPasswordExpiringSoon(LocalDateTime passwordExpiredAt) {
        if (passwordExpiredAt == null) {
            return false;
        }
        return LocalDateTime.now().plusDays(7).isAfter(passwordExpiredAt) && 
               LocalDateTime.now().isBefore(passwordExpiredAt);
    }

    /**
     * 记录新密码到历史记录
     * @param userId 用户ID
     * @param hashedPassword 哈希后的密码
     */
    public void recordPasswordHistory(Long userId, String hashedPassword) {
        userPasswordHistoryService.recordPasswordHistory(userId, hashedPassword);
    }

    /**
     * 验证密码是否符合策略要求
     * @param password 密码
     * @return 是否符合要求
     */
    public boolean validatePassword(String password) {
        // 使用现有的密码复杂度验证方法
        String errorMessage = validatePasswordComplexity(password);
        return errorMessage == null; // null表示验证通过
    }
    
    /**
     * 获取密码复杂度要求描述
     * @return 复杂度要求描述
     */
    public String getPasswordComplexityRequirements() {
        return String.format(
            "密码必须满足以下要求：\n" +
            "1. 长度至少%d个字符\n" +
            "2. 包含至少一个数字\n" +
            "3. 包含至少一个小写字母\n" +
            "4. 包含至少一个大写字母\n" +
            "5. 包含至少一个特殊字符(!@#$%^&*(),.?\"{}\\|<>)", 
            MIN_PASSWORD_LENGTH
        );
    }
    
    /**
     * 获取密码要求描述
     * @return 密码要求描述字符串
     */
    public String getPasswordRequirementsDescription() {
        return "密码必须至少包含" + MIN_PASSWORD_LENGTH + "个字符，包括大写字母、小写字母、数字和特殊字符。";
    }
}
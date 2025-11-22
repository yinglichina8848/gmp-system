package com.gmp.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 密码策略配置
 */
@Configuration
@ConfigurationProperties(prefix = "gmp.auth.password-policy")
public class PasswordPolicyConfig {
    
    // 密码最小长度
    private int minLength = 8;
    
    // 密码最大长度
    private int maxLength = 20;
    
    // 是否需要包含数字
    private boolean requireDigit = true;
    
    // 是否需要包含小写字母
    private boolean requireLowercase = true;
    
    // 是否需要包含大写字母
    private boolean requireUppercase = true;
    
    // 是否需要包含特殊字符
    private boolean requireSpecialChar = true;
    
    // 允许的特殊字符
    private String specialChars = "!@#$%^&*()-_=+[]{}\\|;:'\",.<>/?`~";
    
    // 检查历史密码数量
    private int historyCount = 5;
    
    // 密码过期天数
    private int expireDays = 90;
    
    // 密码即将过期提醒天数
    private int soonExpireDays = 7;
    
    // 账户锁定时间（分钟）
    private int lockMinutes = 15;
    
    // 允许的失败尝试次数
    private int maxFailedAttempts = 5;

    // Getters and Setters
    public int getMinLength() {
        return minLength;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public boolean isRequireDigit() {
        return requireDigit;
    }

    public void setRequireDigit(boolean requireDigit) {
        this.requireDigit = requireDigit;
    }

    public boolean isRequireLowercase() {
        return requireLowercase;
    }

    public void setRequireLowercase(boolean requireLowercase) {
        this.requireLowercase = requireLowercase;
    }

    public boolean isRequireUppercase() {
        return requireUppercase;
    }

    public void setRequireUppercase(boolean requireUppercase) {
        this.requireUppercase = requireUppercase;
    }

    public boolean isRequireSpecialChar() {
        return requireSpecialChar;
    }

    public void setRequireSpecialChar(boolean requireSpecialChar) {
        this.requireSpecialChar = requireSpecialChar;
    }

    public String getSpecialChars() {
        return specialChars;
    }

    public void setSpecialChars(String specialChars) {
        this.specialChars = specialChars;
    }

    public int getHistoryCount() {
        return historyCount;
    }

    public void setHistoryCount(int historyCount) {
        this.historyCount = historyCount;
    }

    public int getExpireDays() {
        return expireDays;
    }

    public void setExpireDays(int expireDays) {
        this.expireDays = expireDays;
    }

    public int getSoonExpireDays() {
        return soonExpireDays;
    }

    public void setSoonExpireDays(int soonExpireDays) {
        this.soonExpireDays = soonExpireDays;
    }

    public int getLockMinutes() {
        return lockMinutes;
    }

    public void setLockMinutes(int lockMinutes) {
        this.lockMinutes = lockMinutes;
    }

    public int getMaxFailedAttempts() {
        return maxFailedAttempts;
    }

    public void setMaxFailedAttempts(int maxFailedAttempts) {
        this.maxFailedAttempts = maxFailedAttempts;
    }
}
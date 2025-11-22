package com.gmp.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 密码修改请求DTO
 */
public class PasswordChangeRequest {
    
    @NotBlank(message = "当前密码不能为空")
    private String currentPassword;
    
    @NotBlank(message = "新密码不能为空")
    @Size(min = 8, message = "新密码长度不能少于8个字符")
    private String newPassword;
    
    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;
    
    // 兼容oldPassword字段
    public String getOldPassword() {
        return currentPassword;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
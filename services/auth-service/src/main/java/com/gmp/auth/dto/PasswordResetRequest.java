package com.gmp.auth.dto;

import lombok.Data;

/**
 * 请求密码重置的DTO类
 */
@Data
public class PasswordResetRequest {
    private String username;
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}
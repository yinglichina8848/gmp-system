package com.gmp.auth.model;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 密码修改请求DTO
 */
@Data
public class PasswordChangeRequest {

    @NotBlank(message = "当前密码不能为空")
    private String currentPassword;

    @NotBlank(message = "新密码不能为空")
    @Size(min = 8, message = "新密码长度不能少于8个字符")
    private String newPassword;

    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;
}
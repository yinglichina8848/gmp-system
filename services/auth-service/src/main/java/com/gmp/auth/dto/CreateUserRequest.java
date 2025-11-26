package com.gmp.auth.dto;

import lombok.Data;

/**
 * 创建用户的DTO类
 */
@Data
public class CreateUserRequest {
    private String username;
    private String password;
    private String email;
    private String fullName;
    private boolean enabled;
}
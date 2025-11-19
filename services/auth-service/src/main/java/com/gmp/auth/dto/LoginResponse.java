package com.gmp.auth.dto;

import lombok.Data;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * 用户登录响应DTO
 *
 * @author GMP系统开发团队
 */
@Data
@Builder
public class LoginResponse {

    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    private Long expiresIn; // 过期时间（秒）
    private String username;
    private String fullName;
    private Set<String> roles;
    private List<String> permissions;
    private LocalDateTime loginTime;
}

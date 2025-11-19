package com.gmp.auth.dto;

import lombok.Data;
import lombok.Builder;

/**
 * JWT令牌响应DTO
 *
 * @author GMP系统开发团队
 */
@Data
@Builder
public class TokenResponse {

    private String accessToken;
    private String tokenType = "Bearer";
    private Long expiresIn;
}

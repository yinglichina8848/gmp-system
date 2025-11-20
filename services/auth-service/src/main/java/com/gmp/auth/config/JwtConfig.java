package com.gmp.auth.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.*;

/**
 * JWT令牌配置和工具类
 *
 * @author GMP系统开发团队
 */
@Component
public class JwtConfig {
    
    private static final Logger log = LoggerFactory.getLogger(JwtConfig.class);

    @Value("${jwt.secret:gmp-auth-jwt-secret-key-2024-very-secure-long-key}")
    private String secret;

    @Value("${jwt.expiration:86400000}")
    private long expiration;

    @Value("${jwt.refresh-expiration:604800000}")
    private long refreshExpiration;

    @Value("${jwt.issuer:gmp-system}")
    private String issuer;

    private SecretKey key;

    /**
     * 生成JWT令牌
     */
    public String generateToken(String username, Collection<String> roles, List<String> permissions) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", username);
        claims.put("iat", now.getTime() / 1000);
        claims.put("exp", expiryDate.getTime() / 1000);
        claims.put("iss", issuer);
        claims.put("roles", roles);
        claims.put("permissions", permissions);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 生成刷新令牌
     */
    public String generateRefreshToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshExpiration);

        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", username);
        claims.put("iat", now.getTime() / 1000);
        claims.put("exp", expiryDate.getTime() / 1000);
        claims.put("iss", issuer);
        claims.put("type", "refresh");

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 从令牌中提取用户名
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * 从令牌中提取用户角色
     */
    @SuppressWarnings("unchecked")
    public Collection<String> extractRoles(String token) {
        return extractClaim(token, claims -> {
            Object rolesObj = claims.get("roles");
            if (rolesObj instanceof Collection) {
                return (Collection<String>) rolesObj;
            }
            return Collections.emptyList();
        });
    }

    /**
     * 从令牌中提取用户权限
     */
    @SuppressWarnings("unchecked")
    public List<String> extractPermissions(String token) {
        return extractClaim(token, claims -> {
            Object permissionsObj = claims.get("permissions");
            if (permissionsObj instanceof List) {
                return (List<String>) permissionsObj;
            }
            return Collections.emptyList();
        });
    }

    /**
     * 验证令牌是否有效
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("JWT令牌已过期: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("不支持的JWT令牌: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("JWT令牌格式错误: {}", e.getMessage());
        } catch (SignatureException e) {
            log.error("JWT令牌签名验证失败: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT令牌参数异常: {}", e.getMessage());
        }
        return false;
    }

    /**
     * 检查令牌是否即将过期
     */
    public boolean isTokenNearExpiry(String token, long thresholdSeconds) {
        try {
            Claims claims = extractAllClaims(token);
            Date expiration = claims.getExpiration();
            return expiration.getTime() - System.currentTimeMillis() <= thresholdSeconds * 1000;
        } catch (Exception e) {
            log.error("检查令牌过期状态失败: {}", e.getMessage());
            return true;
        }
    }

    /**
     * 获取令牌剩余有效时间(秒)
     */
    public long getRemainingValidity(String token) {
        try {
            Claims claims = extractAllClaims(token);
            Date expiration = claims.getExpiration();
            return Math.max(0, (expiration.getTime() - System.currentTimeMillis()) / 1000);
        } catch (Exception e) {
            log.error("获取令牌剩余有效时间失败: {}", e.getMessage());
            return 0;
        }
    }

    /**
     * 使用刷新令牌生成新的访问令牌
     */
    public String refreshAccessToken(String refreshToken, Collection<String> roles, List<String> permissions) {
        if (!validateToken(refreshToken)) {
            throw new JwtException("无效的刷新令牌");
        }

        Claims claims = extractAllClaims(refreshToken);
        String username = claims.getSubject();
        String tokenType = claims.get("type", String.class);

        if (!"refresh".equals(tokenType)) {
            throw new JwtException("令牌类型不是刷新令牌");
        }

        return generateToken(username, roles, permissions);
    }

    /**
     * 提取所有声明
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 提取特定声明
     */
    private <T> T extractClaim(String token, ClaimsResolver<T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.resolve(claims);
    }

    /**
     * 获取签名密钥
     */
    private SecretKey getSecretKey() {
        if (key == null) {
            key = Keys.hmacShaKeyFor(secret.getBytes());
        }
        return key;
    }

    /**
     * 获取令牌过期时间
     */
    public long getExpiration() {
        return expiration;
    }

    /**
     * 获取刷新令牌过期时间
     */
    public long getRefreshExpiration() {
        return refreshExpiration;
    }

    /**
     * 声明解析器接口
     */
    @FunctionalInterface
    private interface ClaimsResolver<T> {
        T resolve(Claims claims);
    }
}

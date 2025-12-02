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
 * @brief JWT令牌配置和工具类
 * 
 * @details 该类负责JWT令牌的生成、验证、解析等功能，包括访问令牌和刷新令牌的管理。
 * 提供了令牌生成、验证、提取用户信息、刷新令牌等核心功能。
 * 
 * @author GMP系统开发团队
 * @version 1.0
 * @since 2023-01-01
 * 
 * @see io.jsonwebtoken.Jwts
 * @see io.jsonwebtoken.Claims
 * @see io.jsonwebtoken.SecretKey
 */
@Component
public class JwtConfig {
    
    private static final Logger log = LoggerFactory.getLogger(JwtConfig.class);

    /**
     * @brief JWT签名密钥
     * 
     * @details 用于签名和验证JWT令牌的密钥，从配置文件中读取
     */
    @Value("${jwt.secret:gmp-auth-jwt-secret-key-2024-very-secure-long-key}")
    private String secret;

    /**
     * @brief 访问令牌过期时间（毫秒）
     * 
     * @details 默认为24小时（86400000毫秒）
     */
    @Value("${jwt.expiration:86400000}")
    private long expiration;

    /**
     * @brief 刷新令牌过期时间（毫秒）
     * 
     * @details 默认为7天（604800000毫秒）
     */
    @Value("${jwt.refresh-expiration:604800000}")
    private long refreshExpiration;

    /**
     * @brief JWT令牌发行者
     * 
     * @details 默认为"gmp-system"
     */
    @Value("${jwt.issuer:gmp-system}")
    private String issuer;

    /**
     * @brief 签名密钥对象
     * 
     * @details 用于签名和验证JWT令牌的SecretKey对象，懒加载初始化
     */
    private SecretKey key;

    /**
     * @brief 生成JWT访问令牌
     * 
     * @details 根据用户名、角色和权限生成JWT访问令牌
     * 
     * @param username 用户名
     * @param roles 用户角色集合
     * @param permissions 用户权限列表
     * @return String JWT访问令牌
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
     * @brief 生成JWT刷新令牌
     * 
     * @details 根据用户名生成JWT刷新令牌，用于获取新的访问令牌
     * 
     * @param username 用户名
     * @return String JWT刷新令牌
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
     * @brief 从令牌中提取用户名
     * 
     * @details 从JWT令牌中提取用户名（subject）
     * 
     * @param token JWT令牌
     * @return String 用户名
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * @brief 从令牌中提取用户角色
     * 
     * @details 从JWT令牌中提取用户角色列表
     * 
     * @param token JWT令牌
     * @return Collection<String> 用户角色集合
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
     * @brief 从令牌中提取用户权限
     * 
     * @details 从JWT令牌中提取用户权限列表
     * 
     * @param token JWT令牌
     * @return List<String> 用户权限列表
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
     * @brief 验证令牌是否有效
     * 
     * @details 验证JWT令牌的有效性，包括签名、过期时间等
     * 
     * @param token JWT令牌
     * @return boolean 如果令牌有效返回true，否则返回false
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
     * @brief 检查令牌是否即将过期
     * 
     * @details 检查JWT令牌是否在指定的阈值时间内即将过期
     * 
     * @param token JWT令牌
     * @param thresholdSeconds 过期阈值（秒）
     * @return boolean 如果令牌即将过期返回true，否则返回false
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
     * @brief 获取令牌剩余有效时间
     * 
     * @details 获取JWT令牌剩余的有效时间（秒）
     * 
     * @param token JWT令牌
     * @return long 剩余有效时间（秒），如果令牌已过期返回0
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
     * @brief 使用刷新令牌生成新的访问令牌
     * 
     * @details 验证刷新令牌的有效性，然后生成新的访问令牌
     * 
     * @param refreshToken 刷新令牌
     * @param roles 用户角色集合
     * @param permissions 用户权限列表
     * @return String 新的访问令牌
     * @throws JwtException 如果刷新令牌无效或类型错误
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
     * @brief 提取所有声明
     * 
     * @details 从JWT令牌中提取所有声明
     * 
     * @param token JWT令牌
     * @return Claims 令牌中的所有声明
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * @brief 提取特定声明
     * 
     * @details 从JWT令牌中提取特定的声明
     * 
     * @param token JWT令牌
     * @param claimsResolver 声明解析器
     * @param <T> 声明类型
     * @return T 提取的声明值
     */
    private <T> T extractClaim(String token, ClaimsResolver<T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.resolve(claims);
    }

    /**
     * @brief 获取签名密钥
     * 
     * @details 获取用于签名和验证JWT令牌的SecretKey对象，懒加载初始化
     * 
     * @return SecretKey 签名密钥
     */
    private SecretKey getSecretKey() {
        if (key == null) {
            key = Keys.hmacShaKeyFor(secret.getBytes());
        }
        return key;
    }

    /**
     * @brief 获取访问令牌过期时间
     * 
     * @details 获取访问令牌的过期时间（毫秒）
     * 
     * @return long 访问令牌过期时间（毫秒）
     */
    public long getExpiration() {
        return expiration;
    }

    /**
     * @brief 获取刷新令牌过期时间
     * 
     * @details 获取刷新令牌的过期时间（毫秒）
     * 
     * @return long 刷新令牌过期时间（毫秒）
     */
    public long getRefreshExpiration() {
        return refreshExpiration;
    }

    /**
     * @brief 声明解析器接口
     * 
     * @details 用于从JWT声明中提取特定值的函数式接口
     * 
     * @param <T> 提取的值类型
     */
    @FunctionalInterface
    private interface ClaimsResolver<T> {
        /**
         * @brief 解析声明
         * 
         * @param claims JWT声明
         * @return T 提取的值
         */
        T resolve(Claims claims);
    }
}

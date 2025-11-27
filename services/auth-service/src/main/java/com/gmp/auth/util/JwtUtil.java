package com.gmp.auth.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    // 使用Value注解代替JwtProperties依赖
    @Value("${jwt.secret:default-secret-key-for-gmp-system}")
    private String jwtSecret;
    
    @Value("${jwt.expiration:3600}")
    private long expiration;
    
    @Value("${jwt.refresh-expiration:86400}")
    private long refreshExpiration;
    
    // 存储已撤销的令牌ID
    private Set<String> revokedTokens = Collections.synchronizedSet(new HashSet<>());

    // 从令牌中获取用户名
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // 从令牌中获取过期时间
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }
    
    // 从令牌中获取令牌ID
    public String getTokenIdFromToken(String token) {
        return getClaimFromToken(token, Claims::getId);
    }

    // 获取令牌中的指定声明
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // 从令牌中获取所有声明
    private Claims getAllClaimsFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .setClock(() -> new Date())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SignatureException e) {
            logger.error("JWT签名验证失败: {}", e.getMessage());
            throw new JwtException("JWT签名验证失败", e);
        } catch (ExpiredJwtException e) {
            logger.error("JWT令牌已过期: {}", e.getMessage());
            throw new JwtException("JWT令牌已过期", e);
        } catch (MalformedJwtException e) {
            logger.error("JWT令牌格式错误: {}", e.getMessage());
            throw new JwtException("JWT令牌格式错误", e);
        } catch (UnsupportedJwtException e) {
            logger.error("不支持的JWT令牌: {}", e.getMessage());
            throw new JwtException("不支持的JWT令牌", e);
        } catch (IllegalArgumentException e) {
            logger.error("JWT令牌参数错误: {}", e.getMessage());
            throw new JwtException("JWT令牌参数错误", e);
        }
    }
    
    // 获取签名密钥
    private Key getSigningKey() {
        // 确保密钥长度足够安全（至少256位）
        byte[] keyBytes = jwtSecret.getBytes();
        if (keyBytes.length < 32) { // 256位 = 32字节
            logger.warn("JWT密钥长度不足，建议至少32字节");
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // 检查令牌是否过期
    public Boolean isTokenExpired(String token) {
        try {
            final Date expiration = getExpirationDateFromToken(token);
            return expiration.before(new Date());
        } catch (JwtException e) {
            logger.error("检查JWT过期状态时出错: {}", e.getMessage());
            return true; // 出错时视为过期
        }
    }
    
    // 检查令牌是否已被撤销
    public Boolean isTokenRevoked(String token) {
        try {
            String tokenId = getTokenIdFromToken(token);
            return revokedTokens.contains(tokenId);
        } catch (JwtException e) {
            logger.error("检查JWT撤销状态时出错: {}", e.getMessage());
            return true; // 出错时视为已撤销
        }
    }

    // 生成访问令牌
    public String generateAccessToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        // 添加用户角色信息
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        
        return doGenerateToken(claims, userDetails.getUsername(), expiration);
    }
    
    // 添加缺失的generateToken方法，用于兼容测试和其他调用
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, username, expiration);
    }

    // 生成刷新令牌
    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername(), refreshExpiration);
    }
    
    // 添加缺失的generateRefreshToken方法，用于兼容测试和其他调用
    public String generateRefreshToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, username, refreshExpiration);
    }

    // 创建令牌
    private String doGenerateToken(Map<String, Object> claims, String subject, long expiration) {
        String tokenId = UUID.randomUUID().toString();
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setId(tokenId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .setIssuer("GMP-Auth-Service")
                .setNotBefore(new Date(System.currentTimeMillis()))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    // 验证令牌
    public Boolean validateToken(String token, UserDetails userDetails) {
        try {
            // 检查token和userDetails是否为null
            if (token == null || userDetails == null) {
                logger.warn("JWT令牌或用户详情为空，验证失败");
                return false;
            }
            
            final String username = getUsernameFromToken(token);
            return (username.equals(userDetails.getUsername()) && 
                   !isTokenExpired(token) && 
                   !isTokenRevoked(token));
        } catch (Exception e) {
            logger.error("JWT令牌验证异常: {}", e.getMessage());
            return false;
        }
    }
    
    // 添加单参数版本的validateToken方法，用于兼容测试和其他调用
    // 仅检查令牌是否过期和是否被撤销
    public Boolean validateToken(String token) {
        try {
            if (token == null) {
                logger.warn("JWT令牌为空，验证失败");
                return false;
            }
            
            // 检查令牌是否被撤销
            if (isTokenRevoked(token)) {
                logger.warn("JWT令牌已被撤销");
                return false;
            }
            
            // 检查令牌是否过期
            if (isTokenExpired(token)) {
                logger.warn("JWT令牌已过期");
                return false;
            }
            
            // 验证签名
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            logger.error("JWT签名验证失败: {}", e.getMessage());
            return false;
        } catch (MalformedJwtException e) {
            logger.error("无效的JWT令牌: {}", e.getMessage());
            return false;
        } catch (ExpiredJwtException e) {
            logger.error("JWT令牌已过期: {}", e.getMessage());
            return false;
        } catch (UnsupportedJwtException e) {
            logger.error("不支持的JWT令牌: {}", e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            logger.error("JWT声明字符串为空: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            logger.error("JWT验证异常: {}", e.getMessage());
            return false;
        }
    }
    
    // 撤销令牌
    public void revokeToken(String token) {
        try {
            String tokenId = getTokenIdFromToken(token);
            revokedTokens.add(tokenId);
            logger.info("已撤销令牌: {}", tokenId);
        } catch (JwtException e) {
            logger.error("撤销JWT令牌时出错: {}", e.getMessage());
        }
    }
    
    // 清除过期的撤销令牌
    public void cleanupRevokedTokens() {
        logger.info("当前已撤销令牌数量: {}", revokedTokens.size());
    }
    
    // 解析令牌而不验证
    public Claims parseTokenWithoutValidation(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .setClock(() -> new Date())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    // 添加公共方法用于测试获取令牌的claims
    public Claims parseToken(String token) {
        return getAllClaimsFromToken(token);
    }
    
    // 添加接收额外声明的generateToken方法，用于测试
    public String generateToken(Map<String, Object> extraClaims, String username) {
        return doGenerateToken(extraClaims, username, expiration);
    }
    
    // 实现refreshToken方法，用于刷新令牌
    public String refreshToken(String refreshToken) {
        try {
            // 验证刷新令牌
            if (!validateToken(refreshToken)) {
                throw new IllegalArgumentException("无效的刷新令牌");
            }
            
            // 从刷新令牌中获取用户名
            String username = getUsernameFromToken(refreshToken);
            
            // 生成新的访问令牌
            return generateToken(username);
        } catch (Exception e) {
            logger.error("刷新令牌失败: {}", e.getMessage());
            throw e;
        }
    }
}
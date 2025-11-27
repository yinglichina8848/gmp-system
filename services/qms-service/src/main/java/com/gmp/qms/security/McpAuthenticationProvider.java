package com.gmp.qms.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * MCP认证提供者，负责验证MCP请求中的令牌并提供认证信息
 * 
 * @author GMP系统开发团队
 */
@Component
public class McpAuthenticationProvider implements AuthenticationProvider {
    
    @Value("${gmp.mcp.jwt.secret:defaultSecretKey}")
    private String jwtSecret;
    
    @Value("${gmp.mcp.jwt.issuer:gmp-system}")
    private String jwtIssuer;
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        McpAuthenticationToken authToken = (McpAuthenticationToken) authentication;
        String token = authToken.getCredentials().toString();
        
        try {
            // 解析JWT令牌
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(jwtSecret.getBytes())
                    .requireIssuer(jwtIssuer)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            
            // 从令牌中提取用户信息
            String clientId = claims.getSubject();
            String clientType = claims.get("client_type", String.class);
            
            // 从令牌中提取权限信息
            List<SimpleGrantedAuthority> authorities = extractAuthorities(claims);
            
            // 创建并返回认证对象
            return new McpAuthenticationToken(
                    new McpPrincipal(clientId, clientType, claims),
                    token,
                    authorities
            );
            
        } catch (SignatureException e) {
            throw new BadCredentialsException("Invalid token signature", e);
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid token", e);
        }
    }
    
    @Override
    public boolean supports(Class<?> authentication) {
        return McpAuthenticationToken.class.isAssignableFrom(authentication);
    }
    
    /**
     * 从JWT声明中提取权限信息
     */
    private List<SimpleGrantedAuthority> extractAuthorities(Claims claims) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        
        // 检查是否有roles或authorities声明
        if (claims.containsKey("roles")) {
            Object roles = claims.get("roles");
            if (roles instanceof List) {
                for (Object role : (List<?>) roles) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + role.toString()));
                }
            }
        }
        
        if (claims.containsKey("authorities")) {
            Object permissions = claims.get("authorities");
            if (permissions instanceof List) {
                for (Object perm : (List<?>) permissions) {
                    authorities.add(new SimpleGrantedAuthority(perm.toString()));
                }
            }
        }
        
        // 如果没有权限信息，添加默认的MCP客户端权限
        if (authorities.isEmpty()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_MCP_CLIENT"));
        }
        
        return authorities;
    }
}

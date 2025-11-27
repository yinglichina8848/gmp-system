package com.gmp.qms.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;

/**
 * MCP认证令牌，用于表示MCP请求的认证信息和状态
 * 
 * @author GMP系统开发团队
 */
public class McpAuthenticationToken extends AbstractAuthenticationToken {
    
    private final Object principal;
    private final Object credentials;
    
    /**
     * 创建未认证的MCP令牌
     * 
     * @param credentials 通常是JWT令牌字符串
     */
    public McpAuthenticationToken(Object credentials) {
        super(null);
        this.principal = null;
        this.credentials = credentials;
        setAuthenticated(false);
    }
    
    /**
     * 创建已认证的MCP令牌
     * 
     * @param principal 客户端主体信息
     * @param credentials JWT令牌
     * @param authorities 授予的权限列表
     */
    public McpAuthenticationToken(Object principal, Object credentials, 
                                 Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(true);
    }
    
    @Override
    public Object getCredentials() {
        return this.credentials;
    }
    
    @Override
    public Object getPrincipal() {
        return this.principal;
    }
    
    @Override
    public void setAuthenticated(boolean authenticated) {
        if (authenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }
        super.setAuthenticated(false);
    }
    
    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
    }
}

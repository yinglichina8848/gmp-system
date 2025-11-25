package com.gmp.auth.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;

/**
 * 自定义用户详情实现
 * 扩展Spring Security的UserDetails，添加额外的用户属性
 */
public class CustomUserDetails implements UserDetails {
    
    private static final long serialVersionUID = 1L;
    
    private Long userId;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private boolean enabled;
    private Long organizationId;
    
    /**
     * 构造函数
     */
    public CustomUserDetails(Long userId, String username, String password, 
                           Collection<? extends GrantedAuthority> authorities,
                           boolean enabled, Long organizationId) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.enabled = enabled;
        this.organizationId = organizationId;
    }
    
    /**
     * 获取用户ID
     */
    public Long getUserId() {
        return userId;
    }
    
    /**
     * 获取组织ID
     */
    public Long getOrganizationId() {
        return organizationId;
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    
    @Override
    public String getPassword() {
        return password;
    }
    
    @Override
    public String getUsername() {
        return username;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        // 可以根据业务需求实现账号过期逻辑
        return true;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        // 可以根据业务需求实现账号锁定逻辑
        return true;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        // 可以根据业务需求实现凭证过期逻辑
        return true;
    }
    
    @Override
    public boolean isEnabled() {
        return enabled;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomUserDetails that = (CustomUserDetails) o;
        return Objects.equals(userId, that.userId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
    
    @Override
    public String toString() {
        return "CustomUserDetails{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", organizationId=" + organizationId +
                ", enabled=" + enabled +
                '}';
    }
}
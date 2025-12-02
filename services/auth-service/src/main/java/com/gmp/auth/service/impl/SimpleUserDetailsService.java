package com.gmp.auth.service.impl;

import com.gmp.auth.model.CustomUserDetails;
import com.gmp.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 简单的用户详情服务实现
 * 用于打破AuthServiceImpl和AuthenticationManager之间的循环依赖
 */
@Service("simpleUserDetailsService")
public class SimpleUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在: " + username));

        // 检查用户是否处于活动状态
        if (!user.isActive()) {
            throw new UsernameNotFoundException("用户不存在: " + username);
        }

        // 构建基础权限集合
        Set<GrantedAuthority> authorities = new HashSet<>();
        // 添加基础角色
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        return new CustomUserDetails(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                authorities,
                user.isActive() && !user.isLocked() && !user.isPasswordExpired(),
                null // 暂时不设置组织ID
        );
    }
}
package com.gmp.qms.config;

import com.gmp.qms.controller.McpSecurityFilter;
import com.gmp.qms.security.McpAuthenticationProvider;
import com.gmp.qms.security.McpPermissionEvaluator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * MCP安全配置类，配置MCP相关的安全认证和授权机制
 * 
 * @author GMP系统开发团队
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class McpSecurityConfig {
    
    @Autowired
    private McpSecurityFilter mcpSecurityFilter;
    
    /**
     * 配置安全过滤器链
     */
    @Bean
    public SecurityFilterChain mcpSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            // 配置MCP相关的URL安全规则
            .securityMatcher("/api/mcp/**")
            .authorizeHttpRequests(authorize -> authorize
                // 允许所有MCP请求都需要认证
                .requestMatchers("/api/mcp/**").authenticated()
            )
            // 禁用CSRF，因为MCP使用令牌认证
            .csrf(csrf -> csrf.disable())
            // 不创建会话，使用无状态的令牌认证
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // 添加MCP安全过滤器
            .addFilterBefore(mcpSecurityFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
    
    /**
     * 配置认证管理器
     */
    @Bean
    public AuthenticationManager mcpAuthenticationManager(HttpSecurity http, 
                                                         McpAuthenticationProvider mcpAuthenticationProvider)
            throws Exception {
        AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        // 添加MCP认证提供者
        authBuilder.authenticationProvider(mcpAuthenticationProvider);
        return authBuilder.build();
    }
    
    /**
     * 配置方法级安全表达式处理器，用于评估MCP权限
     */
    @Bean
    public MethodSecurityExpressionHandler methodSecurityExpressionHandler(McpPermissionEvaluator permissionEvaluator) {
        DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();
        handler.setPermissionEvaluator(permissionEvaluator);
        return handler;
    }
}

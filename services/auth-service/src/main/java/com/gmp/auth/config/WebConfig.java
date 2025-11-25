package com.gmp.auth.config;

import com.gmp.auth.interceptor.SubsystemAccessInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web配置类
 * 用于配置拦截器、过滤器等Web相关组件
 *
 * @author GMP系统开发团队
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private SubsystemAccessInterceptor subsystemAccessInterceptor;

    /**
     * 注册自定义拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册子系统访问权限控制拦截器
        registry.addInterceptor(subsystemAccessInterceptor)
                // 拦截所有API请求
                .addPathPatterns("/api/**")
                // 排除不需要拦截的路径
                .excludePathPatterns("/api/auth/login")
                .excludePathPatterns("/api/auth/logout")
                .excludePathPatterns("/swagger-ui/**")
                .excludePathPatterns("/v3/api-docs/**")
                .excludePathPatterns("/webjars/**")
                .excludePathPatterns("/favicon.ico");
    }
}
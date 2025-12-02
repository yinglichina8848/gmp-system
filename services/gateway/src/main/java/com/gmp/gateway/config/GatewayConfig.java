
package com.gmp.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

/**
 * @brief Gateway配置类
 * 
 * @details 该类负责配置API网关的各种功能，包括限流策略、路由过滤器等。
 * 提供了基于用户ID和IP地址的限流Key解析器。
 * 
 * @author GMP系统开发团队
 * @version 1.0
 * @since 2023-01-01
 * 
 * @see org.springframework.cloud.gateway.filter.ratelimit.KeyResolver
 * @see org.springframework.context.annotation.Configuration
 */
@Configuration
public class GatewayConfig {

    /**
     * @brief 用户Key解析器
     * 
     * @details 基于用户ID进行限流的Key解析器，从请求头中获取X-User-Id，如果不存在则使用"anonymous"
     * 
     * @return KeyResolver 用户Key解析器
     */
    @Bean
    public KeyResolver userKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getHeaders()
                .getFirst("X-User-Id"))
                .defaultIfEmpty("anonymous");
    }

    /**
     * @brief IP Key解析器
     * 
     * @details 基于IP地址进行限流的Key解析器，获取请求的远程IP地址
     * 
     * @return KeyResolver IP Key解析器
     */
    @Bean
    public KeyResolver ipKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress()
                .getAddress().getHostAddress());
    }
}

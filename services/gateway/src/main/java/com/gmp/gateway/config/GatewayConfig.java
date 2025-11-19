
package com.gmp.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

/**
 * Gateway配置类
 * 配置限流策略、路由过滤器等
 */
@Configuration
public class GatewayConfig {

    /**
     * 用户Key解析器 - 基于用户ID进行限流
     */
    @Bean
    public KeyResolver userKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getHeaders()
                .getFirst("X-User-Id"))
                .defaultIfEmpty("anonymous");
    }

    /**
     * IP Key解析器 - 基于IP进行限流
     */
    @Bean
    public KeyResolver ipKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress()
                .getAddress().getHostAddress());
    }
}

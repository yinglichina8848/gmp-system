package com.gmp.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * GMP系统路由配置
 */
@Configuration
public class GatewayConfig {

    @Bean
    public RouterFunction<ServerResponse> homeRoute() {
        return route(GET("/"),
                request -> ServerResponse.ok()
                        .contentType(MediaType.TEXT_HTML)
                        .bodyValue(getHomePageHtml()));
    }

    private String getHomePageHtml() {
        return "<html><body><h1>贵州高原彝药厂 GMP 信息管理系统</h1><p>系统首页</p></body></html>";
    }
}

package com.gmp.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * GMP认证授权服务主应用类
 *
 * @author GMP系统开发团队
 */
@SpringBootApplication
@EnableFeignClients
@EnableJpaAuditing
public class AuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}

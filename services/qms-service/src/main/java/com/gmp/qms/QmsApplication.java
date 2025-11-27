package com.gmp.qms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * GMP质量管理子系统主应用类
 * 
 * @author GMP系统开发团队
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableFeignClients
public class QmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(QmsApplication.class, args);
    }
}

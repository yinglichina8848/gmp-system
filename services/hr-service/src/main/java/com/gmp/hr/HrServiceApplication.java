package com.gmp.hr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 人力资源管理服务启动类
 * 
 * @author GMP Team
 * @version 1.0.0
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaAuditing
@EnableJpaRepositories
public class HrServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HrServiceApplication.class, args);
    }
}

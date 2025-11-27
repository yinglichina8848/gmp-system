package com.gmp.training;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * GMP培训管理子系统主应用类
 * 
 * @author GMP系统开发团队
 */
@SpringBootApplication
@EnableJpaAuditing
public class TrainingApplication {

    /**
     * 应用程序主入口
     * 
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(TrainingApplication.class, args);
    }
}

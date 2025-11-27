package com.gmp.lims;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * LIMS服务应用主类
 * 实验室信息管理系统主应用入口
 */
@SpringBootApplication
@EntityScan("com.gmp.lims.entity")
@EnableJpaRepositories("com.gmp.lims.repository.jpa")
@EnableMongoRepositories("com.gmp.lims.repository.mongo")
public class LimsServiceApplication {

    /**
     * 应用主入口
     * 
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(LimsServiceApplication.class, args);
    }

}
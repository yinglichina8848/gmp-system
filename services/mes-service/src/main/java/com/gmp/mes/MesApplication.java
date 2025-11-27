package com.gmp.mes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * GMP制造执行系统(MES)应用主类
 * 
 * @author gmp-system
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.gmp.mes"})
public class MesApplication {

    /**
     * 应用程序入口点
     * 
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(MesApplication.class, args);
    }

}
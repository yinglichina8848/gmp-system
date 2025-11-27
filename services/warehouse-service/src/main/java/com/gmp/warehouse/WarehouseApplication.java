package com.gmp.warehouse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 仓库管理子系统应用程序入口
 * <p>
 * 该类是Spring Boot应用的主入口，启动整个仓库管理子系统服务。
 * 系统负责物料主数据管理、入库管理、出库管理、库存管理、盘点管理、供应商管理、报表统计和基础配置等功能。
 * </p>
 *
 * @author GMP系统开发团队
 * @version 1.0.0
 * @since 2024-01-01
 */
@SpringBootApplication
public class WarehouseApplication {

    /**
     * 应用程序主入口方法
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(WarehouseApplication.class, args);
    }

}
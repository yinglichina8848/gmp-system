package com.gmp.warehouse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 仓库管理子系统测试应用入口
 * <p>
 * 该类用于在测试环境中启动仓库管理子系统服务，提供测试环境支持。
 * </p>
 *
 * @author GMP系统开发团队
 * @version 1.0.0
 * @since 2024-01-01
 */
@SpringBootApplication
public class WarehouseServiceTestApplication {

    /**
     * 测试应用程序主入口方法
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(WarehouseServiceTestApplication.class, args);
    }

}
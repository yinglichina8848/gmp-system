package com.gmp.equipment;

// ============================================================================
//                    GMP设备管理系统 (EMS) 应用启动类
// dịp
//
// WHY: GMP制药生产对设备管理有严格合规性要求，需要独立的微服务来处理
//      设备生命周期管理，确保设备状态、维护记录、校准信息的完整可追溯性。
//      独立的服务架构可以避免与生产系统耦合，提供专业的设备管理能力。
//
// WHAT: EquipmentManagementApplication是设备管理系统的Spring Boot主启动类，
//      负责整个微服务的初始化、配置加载和应用上下文管理，集成了Eureka服务注册、
//      数据库连接、缓存配置、安全框架等核心功能模块。
//
// HOW: 使用@SpringBootApplication注解启用自动配置，通过@EnableDiscoveryClient
//      注册到Eureka服务中心，实现分布式服务发现；配合Actuator健康监控，提供
//      生产环境的可观测性和运维支持。
// ============================================================================

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * GMP设备管理系统主启动类
 *
 * 这是一个Spring Boot微服务应用，提供以下核心功能：
 * - 设备台账管理（Device Master Data Management）
 * - 设备维护计划与执行（Maintenance Planning & Execution）
 * - 设备校准管理（Calibration Management）
 * - 设备状态监控（Real-time Equipment Monitoring）
 * - 设备绩效分析（Equipment Performance Analytics）
 * - GMP合规性审计（GMP Compliance Auditing）
 *
 * 技术特点：
 * - 微服务架构：独立部署、可扩展、松耦合
 * - GMP合规：满足《中国药典》设备管理要求
 * - 实时监控：MQTT协议设备状态采集
 * - 数据追溯：完整设备生命周期记录
 *
 * @author GMP设备管理团队
 */
@SpringBootApplication(scanBasePackages = "com.gmp.equipment")
@EnableDiscoveryClient
@EnableTransactionManagement
@EnableAsync
public class EquipmentManagementApplication {

    /**
     * 应用程序主入口点
     *
     * 启动流程：
     * 1. 加载Spring应用上下文
     * 2. 初始化数据库连接
     * 3. 注册Eureka服务发现
     * 4. 启动Web服务器
     * 5. 初始化设备监控组件
     * 6. 启动后台任务调度
     *
     * @param args 命令行参数（可用于环境配置覆盖）
     */
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  GMP设备管理系统 (EMS) 正在启动...");
        System.out.println("========================================");

        try {
            SpringApplication.run(EquipmentManagementApplication.class, args);
            System.out.println("✓ 设备管理系统启动成功!");
            System.out.println("✓ 服务已在Eureka注册并准备就绪");
            System.out.println("✓ 请访问 http://localhost:8087 查看服务状态");
        } catch (Exception e) {
            System.err.println("✗ 设备管理系统启动失败: " + e.getMessage());
            System.exit(1);
        }
    }
}

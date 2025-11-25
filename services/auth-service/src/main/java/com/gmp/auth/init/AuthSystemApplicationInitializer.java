package com.gmp.auth.init;

// ============================================================================
//                    GMP系统认证初始化器
// 最小化实现，只负责初始化组织结构
// ============================================================================

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * GMP系统认证初始化器
 * 最小化实现，仅执行组织结构初始化
 */
@Component
@Order(10)  // 设置执行顺序
@ConditionalOnProperty(
    name = "gmp.auth.initialization.enabled", 
    havingValue = "true", 
    matchIfMissing = false  // 默认为false，必须显式启用
)
public class AuthSystemApplicationInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(AuthSystemApplicationInitializer.class);
    
    private final OrganizationStructureInitializer organizationInitializer;
    
    public AuthSystemApplicationInitializer(OrganizationStructureInitializer organizationInitializer) {
        this.organizationInitializer = organizationInitializer;
    }
    
    @Override
    public void run(ApplicationArguments args) {
        try {
            // 仅执行组织结构初始化
            log.info("执行系统初始化...");
            organizationInitializer.initializeOrganizations();
            
            log.info("系统初始化完成");
        } catch (Exception e) {
            log.error("初始化时发生异常: {}", e.getMessage());
            throw new RuntimeException("系统初始化失败", e);
        }
    }
}
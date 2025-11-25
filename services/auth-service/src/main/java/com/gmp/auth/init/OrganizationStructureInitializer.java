package com.gmp.auth.init;

import com.gmp.auth.entity.Organization;
import com.gmp.auth.repository.OrganizationRepository;
import org.springframework.stereotype.Component;

/**
 * GMP系统组织结构初始化器
 * 最小化实现，避免编译错误
 */
@Component
public class OrganizationStructureInitializer {
    private final OrganizationRepository organizationRepository;
    
    public OrganizationStructureInitializer(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }
    
    /**
     * 初始化组织结构的公共方法 - 供其他类调用
     */
    public void initializeOrganizations() {
        try {
            System.out.println("开始初始化组织结构...");
            
            // 最简实现：创建并保存一个组织对象
            Organization company = new Organization();
            organizationRepository.save(company);
            
            System.out.println("组织结构初始化完成");
        } catch (Exception e) {
            System.out.println("初始化组织结构时发生异常: " + e.getMessage());
            throw new RuntimeException("组织结构初始化失败", e);
        }
    }
}
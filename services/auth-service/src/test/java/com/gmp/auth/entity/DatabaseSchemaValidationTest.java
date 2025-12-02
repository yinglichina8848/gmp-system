package com.gmp.auth.entity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Table;
import jakarta.persistence.metamodel.EntityType;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 数据库模式验证测试
 * 验证Hibernate DDL自动生成是否正常工作
 */
@SpringBootTest
@ActiveProfiles("test")
@Slf4j
class DatabaseSchemaValidationTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    @Transactional
    void testHibernateDDLGeneration() {
        log.info("验证Hibernate DDL自动生成...");
        
        // 获取所有已注册的实体类
        Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();
        assertFalse(entities.isEmpty(), "应该找到已注册的实体类");
        
        log.info("找到 {} 个已注册的实体类", entities.size());
        
        // 验证UserOrganizationRole实体是否已注册
        boolean foundUserOrgRole = entities.stream()
                .anyMatch(entity -> entity.getJavaType().equals(UserOrganizationRole.class));
        
        assertTrue(foundUserOrgRole, "应该找到UserOrganizationRole实体");
        
        // 验证表映射
        Table tableAnnotation = UserOrganizationRole.class.getAnnotation(Table.class);
        assertNotNull(tableAnnotation, "UserOrganizationRole应该有@Table注解");
        assertEquals("sys_user_org_roles", tableAnnotation.name(), "表名应该为sys_user_org_roles");
        
        log.info("UserOrganizationRole实体映射验证通过 - 表名: {}", tableAnnotation.name());
        
        // 尝试查询表结构（通过简单的元数据查询）
        try {
            // 这个查询会触发Hibernate验证表结构
            Long count = entityManager.createQuery(
                    "SELECT COUNT(u) FROM UserOrganizationRole u", Long.class)
                    .getSingleResult();
            log.info("表查询成功 - 当前记录数: {}", count);
        } catch (Exception e) {
            log.error("表查询失败: {}", e.getMessage());
            fail("表查询应该成功，但失败: " + e.getMessage());
        }
        
        log.info("Hibernate DDL自动生成验证通过");
    }
}
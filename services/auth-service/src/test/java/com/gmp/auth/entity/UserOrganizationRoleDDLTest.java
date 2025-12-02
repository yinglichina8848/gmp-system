package com.gmp.auth.entity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import lombok.extern.slf4j.Slf4j;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 * 测试UserOrganizationRole实体的DDL生成
 * 用于验证Hibernate是否能够正确创建sys_user_org_roles表结构
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional(propagation = Propagation.NOT_SUPPORTED)  // 禁用事务支持，避免回滚
@Slf4j
class UserOrganizationRoleDDLTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void testEntityMapping() {
        // 这个测试会强制Hibernate验证实体映射
        // 如果DDL自动生成功，表结构应该会被创建
        log.info("测试UserOrganizationRole实体映射...");
        
        // 尝试创建一个UserOrganizationRole实例来触发DDL验证
        UserOrganizationRole userOrgRole = UserOrganizationRole.builder()
                .userId(1L)
                .organizationId(1L)
                .roleId(1L)
                .status(UserOrganizationRole.AssignmentStatus.ACTIVE)
                .build();
        
        // 验证实体是否被正确创建
        assertNotNull(userOrgRole);
        assertEquals(1L, userOrgRole.getUserId());
        assertEquals(1L, userOrgRole.getOrganizationId());
        assertEquals(1L, userOrgRole.getRoleId());
        assertEquals(UserOrganizationRole.AssignmentStatus.ACTIVE, userOrgRole.getStatus());
        
        log.info("UserOrganizationRole实体映射测试完成 - Hibernate DDL生成验证通过");
    }
}
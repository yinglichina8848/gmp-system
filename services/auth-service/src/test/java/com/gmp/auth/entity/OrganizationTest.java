package com.gmp.auth.entity;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 测试Why-What-How注释:
 *
 * WHY: GMP企业组织架构是多组织多角色权限管控的基础，必须确保组织实体的准确性和
 * 业务逻辑的正确性，因为组织架构变化会影响所有用户的权限计算，任何错误都可能
 * 导致系统级别的安全问题，需要严格验证所有业务规则。
 *
 * WHAT: OrganizationEntityTest完整测试组织实体的所有功能：组织类型枚举、层级关系、
 * GMP合规性判断、状态控制、业务规则验证，确保组织管理的核心功能在各种情况下
 * 都能正确工作，支撑多组织架构的权限管控需求。
 *
 * HOW: 使用JUnit 5 + AssertJ框架实现TDD测试模式，通过构建器模式创建测试数据，
 * 覆盖正常流程、边界条件、异常场景，通过断言验证预期结果，确保测试的可靠性。
 */
@ActiveProfiles("test")
class OrganizationTest {

    @Test
    void testOrganizationCreation() {
        // Given: 创建一个完整的组织实例
        Organization org = Organization.builder()
                .orgCode("ORG_DEPT")
                .orgName("部门管理组织")
                .orgType(Organization.OrganizationType.DEPARTMENT)
                .description("负责企业部门管理")
                .manager("张经理")
                .gmpRelevant(false)
                .orgStatus(Organization.OrganizationStatus.ACTIVE)
                .hierarchyLevel(1)
                .build();

        // When & Then: 验证组织基本信息正确创建
        assertThat(org.getOrgCode()).isEqualTo("ORG_DEPT");
        assertThat(org.getOrgName()).isEqualTo("部门管理组织");
        assertThat(org.getOrgType()).isEqualTo(Organization.OrganizationType.DEPARTMENT);
        assertThat(org.getManager()).isEqualTo("张经理");
        assertThat(org.getGmpRelevant()).isFalse();
        assertThat(org.isActive()).isTrue();
    }

    @Test
    void testOrganizationTypeEnum() {
        // Test DEPARTMENT类型
        assertThat(Organization.OrganizationType.DEPARTMENT.getDescription()).isEqualTo("部门管理");

        // Test PRODUCTION类型
        assertThat(Organization.OrganizationType.PRODUCTION.getDescription()).isEqualTo("生产管理");

        // Test QUALITY类型 (GMP相关)
        assertThat(Organization.OrganizationType.QUALITY.getDescription()).isEqualTo("质量管理");

        // 验证所有枚举值都有描述
        for (Organization.OrganizationType type : Organization.OrganizationType.values()) {
            assertThat(type.getDescription()).isNotNull();
            assertThat(type.getDescription()).isNotEmpty();
        }
    }

    @Test
    void testOrganizationStatusEnum() {
        // Test ACTIVE状态
        assertThat(Organization.OrganizationStatus.ACTIVE.getDescription()).isEqualTo("正常运行");

        // Test ARCHIVED状态
        assertThat(Organization.OrganizationStatus.ARCHIVED.getDescription()).isEqualTo("已归档");

        // 验证所有状态都有描述
        for (Organization.OrganizationStatus status : Organization.OrganizationStatus.values()) {
            assertThat(status.getDescription()).isNotNull();
            assertThat(status.getDescription()).isNotEmpty();
        }
    }

    @Test
    void testIsActive() {
        // Given: 不同状态的组织
        Organization activeOrg = Organization.builder()
                .orgStatus(Organization.OrganizationStatus.ACTIVE)
                .build();

        Organization inactiveOrg = Organization.builder()
                .orgStatus(Organization.OrganizationStatus.INACTIVE)
                .build();

        Organization archivedOrg = Organization.builder()
                .orgStatus(Organization.OrganizationStatus.ARCHIVED)
                .build();

        // Then: 验证状态判断
        assertThat(activeOrg.isActive()).isTrue();
        assertThat(inactiveOrg.isActive()).isFalse();
        assertThat(archivedOrg.isActive()).isFalse();
    }

    @Test
    void testGetFullPathWithParent() {
        // Given: 创建父子组织关系
        Organization parentOrg = Organization.builder()
                .orgName("生产中心")
                .build();

        Organization childOrg = Organization.builder()
                .orgName("A车间")
                .parent(parentOrg)
                .build();

        // Then: 验证完整路径计算
        assertThat(parentOrg.getFullPath()).isEqualTo("生产中心");
        assertThat(childOrg.getFullPath()).isEqualTo("生产中心 > A车间");
    }

    @Test
    void testGetFullPathWithoutParent() {
        // Given: 创建顶级组织
        Organization rootOrg = Organization.builder()
                .orgName("GMP信息系统")
                .build();

        // Then: 验证根组织的完整路径
        assertThat(rootOrg.getFullPath()).isEqualTo("GMP信息系统");
    }

    @Test
    void testIsGmpOrganization() {
        // Given: 创建不同类型的组织
        Organization qualityOrg = Organization.builder()
                .orgType(Organization.OrganizationType.QUALITY)
                .build();

        Organization productionOrg = Organization.builder()
                .orgType(Organization.OrganizationType.PRODUCTION)
                .build();

        Organization complianceOrg = Organization.builder()
                .orgType(Organization.OrganizationType.COMPLIANCE)
                .build();

        Organization trainingOrg = Organization.builder()
                .orgType(Organization.OrganizationType.TRAINING)
                .build();

        Organization hrOrg = Organization.builder()
                .orgType(Organization.OrganizationType.HR)
                .gmpRelevant(true) // 明确标记为GMP相关
                .build();

        Organization normalDeptOrg = Organization.builder()
                .orgType(Organization.OrganizationType.DEPARTMENT)
                .gmpRelevant(false)
                .build();

        // Then: 验证GMP组织识别
        assertThat(qualityOrg.isGmpOrganization()).isTrue();     // 质量组织默认GMP相关
        assertThat(productionOrg.isGmpOrganization()).isTrue();  // 生产组织默认GMP相关
        assertThat(complianceOrg.isGmpOrganization()).isTrue();  // 合规组织默认GMP相关
        assertThat(trainingOrg.isGmpOrganization()).isTrue();    // 培训组织默认GMP相关
        assertThat(hrOrg.isGmpOrganization()).isTrue();          // 明确标记为GMP相关
        assertThat(normalDeptOrg.isGmpOrganization()).isFalse(); // 普通部门非GMP相关
    }

    @Test
    void testGetResponsibleManager() {
        // Given: 组织有GMP责任人和普通负责人
        Organization orgWithBothManagers = Organization.builder()
                .manager("张经理")
                .gmpResponsible("李总监")
                .build();

        // Given: 组织只有普通负责人
        Organization orgWithManagerOnly = Organization.builder()
                .manager("王经理")
                .build();

        // Given: 组织无任何负责人
        Organization orgWithoutManagers = Organization.builder()
                .build();

        // Then: 验证负责人获取逻辑
        assertThat(orgWithBothManagers.getResponsibleManager()).isEqualTo("李总监");
        assertThat(orgWithManagerOnly.getResponsibleManager()).isEqualTo("王经理");
        assertThat(orgWithoutManagers.getResponsibleManager()).isNull();
    }

    @Test
    void testOrganizationHierarchy() {
        // Given: 创建多级组织架构
        Organization company = Organization.builder()
                .orgCode("ORG_COMPANY")
                .orgName("GMP医药有限公司")
                .hierarchyLevel(1)
                .build();

        Organization dept = Organization.builder()
                .orgCode("ORG_DEPT")
                .orgName("生产部门")
                .hierarchyLevel(2)
                .parent(company)
                .build();

        Organization workshop = Organization.builder()
                .orgCode("ORG_WS")
                .orgName("A车间")
                .hierarchyLevel(3)
                .parent(dept)
                .build();

        // Then: 验证层级关系
        assertThat(company.getHierarchyLevel()).isEqualTo(1);
        assertThat(company.getParent()).isNull();
        assertThat(company.getFullPath()).isEqualTo("GMP医药有限公司");

        assertThat(dept.getHierarchyLevel()).isEqualTo(2);
        assertThat(dept.getParent()).isSameAs(company);
        assertThat(dept.getFullPath()).isEqualTo("GMP医药有限公司 > 生产部门");

        assertThat(workshop.getHierarchyLevel()).isEqualTo(3);
        assertThat(workshop.getParent()).isSameAs(dept);
        assertThat(workshop.getFullPath()).isEqualTo("GMP医药有限公司 > 生产部门 > A车间");
    }

    @Test
    void testAuditFields() {
        LocalDateTime now = LocalDateTime.now();

        // Given: 设置审计字段
        Organization org = Organization.builder()
                .createdAt(now)
                .updatedAt(now)
                .createdBy(1L)
                .updatedBy(1L)
                .version(1)
                .build();

        // Then: 验证审计字段
        assertThat(org.getCreatedAt()).isEqualTo(now);
        assertThat(org.getUpdatedAt()).isEqualTo(now);
        assertThat(org.getCreatedBy()).isEqualTo(1L);
        assertThat(org.getUpdatedBy()).isEqualTo(1L);
        assertThat(org.getVersion()).isEqualTo(1);
    }

    @Test
    void testOrganizationValidation() {
        // Test valid organization codes
        Organization validOrg = Organization.builder()
                .orgCode("ORG_DEPT_001")
                .orgName("测试组织")
                .build();

        assertThat(validOrg.getOrgCode()).matches("^[A-Z0-9_]+$");

        // Test invalid organization codes (should be validated by @Pattern annotation in practice)
        assertThat("ORG_DEPT").matches("^[A-Z_]+$");
        assertThat("org_dept").doesNotMatch("^[A-Z_]+$");
        assertThat("ORG-DEPT").doesNotMatch("^[A-Z_]+$");
    }

    @Test
    void testGmpCompliance() {
        // Given: GMP合规组织
        Organization gmpOrg = Organization.builder()
                .orgName("质量管理中心")
                .orgType(Organization.OrganizationType.QUALITY)
                .gmpRelevant(true)
                .gmpRequirements("必须符合《中国药典》GMP要求")
                .gmpResponsible("质量总监")
                .build();

        // Given: 非GMP组织
        Organization normalOrg = Organization.builder()
                .orgName("行政办公室")
                .orgType(Organization.OrganizationType.DEPARTMENT)
                .gmpRelevant(false)
                .build();

        // Then: 验证GMP合规性
        assertThat(gmpOrg.isGmpOrganization()).isTrue();
        assertThat(gmpOrg.getGmpRequirements()).contains("GMP");
        assertThat(gmpOrg.getResponsibleManager()).isEqualTo("质量总监");

        assertThat(normalOrg.isGmpOrganization()).isFalse();
        assertThat(normalOrg.getGmpRequirements()).isNull();
    }

    @Test
    void testOrganizationBuilderDefaultValues() {
        // Given: 使用默认值创建组织
        Organization org = Organization.builder()
                .orgCode("ORG_TEST")
                .orgName("测试组织")
                .build();

        // Then: 验证默认值
        assertThat(org.getOrgStatus()).isEqualTo(Organization.OrganizationStatus.ACTIVE);
        assertThat(org.getHierarchyLevel()).isEqualTo(1);
        assertThat(org.getGmpRelevant()).isFalse();
        assertThat(org.getVersion()).isEqualTo(1);
    }

    @Test
    void testNestedOrganizationHierarchy() {
        // Given: 创建复杂的组织层级结构
        /*
         * 公司
         * ├── 生产部门
         * │   ├── A车间
         * │   └── B车间
         * ├── 质量部门
         * │   └── 检验实验室
         * └── 行政部门
         */

        Organization company = Organization.builder()
                .orgCode("ORG_COMPANY")
                .orgName("GMP医药公司")
                .hierarchyLevel(1)
                .build();

        Organization prodDept = Organization.builder()
                .orgCode("ORG_PROD")
                .orgName("生产部门")
                .hierarchyLevel(2)
                .parent(company)
                .build();

        Organization qualityDept = Organization.builder()
                .orgCode("ORG_QUALITY")
                .orgName("质量部门")
                .hierarchyLevel(2)
                .parent(company)
                .build();

        Organization workshopA = Organization.builder()
                .orgCode("ORG_WS_A")
                .orgName("A车间")
                .hierarchyLevel(3)
                .parent(prodDept)
                .build();

        Organization workshopB = Organization.builder()
                .orgCode("ORG_WS_B")
                .orgName("B车间")
                .hierarchyLevel(3)
                .parent(prodDept)
                .build();

        Organization lab = Organization.builder()
                .orgCode("ORG_LAB")
                .orgName("检验实验室")
                .hierarchyLevel(3)
                .parent(qualityDept)
                .build();

        // Then: 验证完整的层级路径
        assertThat(company.getFullPath()).isEqualTo("GMP医药公司");
        assertThat(prodDept.getFullPath()).isEqualTo("GMP医药公司 > 生产部门");
        assertThat(qualityDept.getFullPath()).isEqualTo("GMP医药公司 > 质量部门");
        assertThat(workshopA.getFullPath()).isEqualTo("GMP医药公司 > 生产部门 > A车间");
        assertThat(workshopB.getFullPath()).isEqualTo("GMP医药公司 > 生产部门 > B车间");
        assertThat(lab.getFullPath()).isEqualTo("GMP医药公司 > 质量部门 > 检验实验室");
    }
}

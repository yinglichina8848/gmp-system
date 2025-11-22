package com.gmp.auth.entity;

// ============================================================================
//                        GMP系统组织架构实体定义
// dịp
//
// WHY: GMP制药企业的组织架构复杂多样，需要建立灵活的多组织管理架构，
//      支持用户同时拥有多个组织架构内的不同角色，实现精细化的权限控制，
//      支撑GMP合规要求的组织职责分离和权限隔离。
//
// WHAT: Organization实体定义了组织架构的核心结构，包含组织类型、分层管理、
//      状态控制等，支持部门管理、生产管理、质量管理等GMP企业常见组织形式，
//      提供完整的组织生命周期管理和树状结构支持。
//
// HOW: 使用JPA注解定义实体映射，支持树状结构的父子关系，通过@Enumerated定义
//      组织类型枚举，确保类型安全；使用审计注解自动填充创建和更新信息。
// ============================================================================

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * GMP系统组织架构实体
 *
 * 支持多层次组织架构管理，包括：
 * - 部门管理架构：行政、人事、财务等职能部门
 * - 生产管理架构：车间、分车间、生产线级别的生产组织
 * - 质量管理架构：质量检验、审核、体系管理等
 * - 合规审核架构：GMP审核、法规事务等
 * - 培训管理架构：培训规划、执行、评估等
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "sys_organizations", indexes = {
    @Index(name = "idx_org_code", columnList = "org_code", unique = true),
    @Index(name = "idx_org_parent", columnList = "parent_id"),
    @Index(name = "idx_org_type", columnList = "org_type"),
    @Index(name = "idx_org_status", columnList = "org_status")
})
@EntityListeners(AuditingEntityListener.class)
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 组织基本信息
    @NotBlank(message = "组织编码不能为空")
    @Size(max = 50, message = "组织编码长度不能超过50字符")
    @Pattern(regexp = "^ORG_[A-Z_]+$", message = "组织编码必须以ORG_开头并使用大写字母和下划线")
    @Column(name = "org_code", unique = true, nullable = false)
    private String orgCode;

    @NotBlank(message = "组织名称不能为空")
    @Size(max = 200, message = "组织名称长度不能超过200字符")
    @Column(name = "org_name", nullable = false)
    private String orgName;

    @Size(max = 500, message = "组织描述长度不能超过500字符")
    @Column(name = "description")
    private String description;

    // 组织类型 - GMP企业常见的组织架构
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "org_type", nullable = false)
    private OrganizationType orgType = OrganizationType.DEPARTMENT;

    // 组织层级结构 - 支持多级组织架构
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Organization parent;

    @Builder.Default
    @Column(name = "hierarchy_level", nullable = false)
    private Integer hierarchyLevel = 1;

    @Size(max = 100, message = "负责人长度不能超过100字符")
    @Column(name = "manager")
    private String manager;

    @Size(max = 500, message = "联系方式长度不能超过500字符")
    @Column(name = "contact_info")
    private String contactInfo;

    // 组织状态管理
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "org_status", nullable = false)
    private OrganizationStatus orgStatus = OrganizationStatus.ACTIVE;

    // GMP合规相关信息
    @Builder.Default
    @Column(name = "gmp_relevant")
    private Boolean gmpRelevant = false;

    @Size(max = 100, message = "GMP责任人长度不能超过100字符")
    @Column(name = "gmp_responsible")
    private String gmpResponsible;

    @Size(max = 500, message = "GMP要求长度不能超过500字符")
    @Column(name = "gmp_requirements")
    private String gmpRequirements;

    // 审计字段
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Builder.Default
    @Version
    @Column(nullable = false)
    private Integer version = 1;

    /**
     * 组织类型枚举 - 定义GMP企业常见的组织架构类型
     */
    public enum OrganizationType {
        DEPARTMENT("部门管理"),    // 部门组织：行政部门、人事部门等
        PRODUCTION("生产管理"),    // 生产组织：车间、生产线、班组等
        QUALITY("质量管理"),       // 质量组织：质量检验、审核、体系等
        COMPLIANCE("合规审核"),    // 合规组织：法规事务、GMP审核等
        TRAINING("培训管理"),      // 培训组织：培训规划、执行等
        HR("人力资源"),             // HR组织：人力资源管理专用
        IT("信息技术"),             // IT组织：信息系统管理
        MAINTENANCE("设备维护"),  // 维护组织：设备维护和技术服务
        LOGISTICS("后勤保障"),    // 后勤组织：设施、物料管理等
        SAFETY("安全环保");       // 安全组织：安全管理、环保等

        private final String description;

        OrganizationType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 组织状态枚举
     */
    public enum OrganizationStatus {
        ACTIVE("正常运行"),      // 正常的活动组织
        INACTIVE("临时停用"),    // 临时停用的组织
        UNDER_REVIEW("审核中"),  // 正在审核调整中
        MERGED("已合并"),        // 已被合并到其他组织
        ARCHIVED("已归档");      // 已被归档的历史组织

        private final String description;

        OrganizationStatus(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 判断组织是否有效（可运营状态）
     */
    @Transient
    public boolean isActive() {
        return this.orgStatus == OrganizationStatus.ACTIVE;
    }

    /**
     * 获取组织的完整层级路径
     */
    @Transient
    public String getFullPath() {
        if (parent == null) {
            return orgName;
        }
        return parent.getFullPath() + " > " + orgName;
    }

    /**
     * 判断是否为GMP相关组织
     */
    @Transient
    public boolean isGmpOrganization() {
        return Boolean.TRUE.equals(gmpRelevant) || orgType == OrganizationType.QUALITY ||
               orgType == OrganizationType.PRODUCTION ||
               orgType == OrganizationType.COMPLIANCE ||
               orgType == OrganizationType.TRAINING;
    }

    /**
     * 获取组织的管理责任人
     */
    @Transient
    public String getResponsibleManager() {
        if (gmpResponsible != null && !gmpResponsible.trim().isEmpty()) {
            return gmpResponsible;
        }
        return manager;
    }
}

package com.gmp.auth.repository;

// ============================================================================
//                    GMP系统组织架构数据仓库接口
// dịp
//
// WHY: GMP企业的组织架构复杂多变，需要高效的组织数据访问层来支撑多组织管理、
//      树状结构查询、GMP合规性验证等业务需求，确保组织数据的快速查询和一致性。
//
// WHAT: OrganizationRepository提供完整的组织数据访问功能，支持组织层级查询、
//      类型筛选、状态过滤等业务场景，为多组织权限管理和GMP合规性验证提供基础。
//
// HOW: 继承JpaRepository获得基础CRUD能力，通过@Query注解实现复杂业务查询，
//      使用Spring Data JPA命名方法约定简化查询开发，确保数据访问的类型安全和性能。
// ============================================================================

import com.gmp.auth.entity.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * GMP系统组织架构数据仓库接口
 *
 * 提供组织管理的完整数据访问能力，包括：
 * - 基础CRUD操作（继承自JpaRepository）
 * - 组织层级和树状结构查询
 * - GMP相关组织筛选
 * - 组织状态管理查询
 * - 复杂业务条件组合查询
 */
@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    /**
     * 根据组织编码查找组织
     */
    Optional<Organization> findByOrgCode(String orgCode);

    /**
     * 根据组织名称查找组织
     */
    List<Organization> findByOrgNameContaining(String orgName);

    /**
     * 根据组织类型查询组织列表
     */
    List<Organization> findByOrgType(Organization.OrganizationType orgType);

    /**
     * 根据组织状态查询活动组织
     */
    List<Organization> findByOrgStatus(Organization.OrganizationStatus orgStatus);

    /**
     * 分页查询按组织类型分组
     */
    Page<Organization> findByOrgType(Organization.OrganizationType orgType, Pageable pageable);

    /**
     * 查找子组织（指定组织的直接子组织）
     */
    @Query("SELECT o FROM Organization o WHERE o.parent.id = :parentId AND o.orgStatus = 'ACTIVE' ORDER BY o.orgName")
    List<Organization> findActiveChildOrganizations(@Param("parentId") Long parentId);

    /**
     * 查找所有子组织（递归查询所有层级子组织）
     */
    @Query("SELECT o FROM Organization o WHERE o.parent.id = :parentId OR o.parent.id IN (SELECT sub.id FROM Organization sub WHERE sub.parent.id = :parentId) AND o.orgStatus = 'ACTIVE'")
    List<Organization> findAllDescendantOrganizations(@Param("parentId") Long parentId);

    /**
     * 获取组织完整路径（从根到当前组织的名称路径）
     */
    @Query(value = "WITH RECURSIVE org_path AS (" +
           "SELECT id, org_name, parent_id, org_name as full_path, 0 as level " +
           "FROM sys_organizations WHERE id = :orgId " +
           "UNION ALL " +
           "SELECT o.id, o.org_name, o.parent_id, CONCAT(op.full_path, ' > ', o.org_name), op.level + 1 " +
           "FROM sys_organizations o INNER JOIN org_path op ON o.id = op.parent_id" +
           ") SELECT full_path FROM org_path WHERE level = (SELECT MAX(level) FROM org_path)", nativeQuery = true)
    String getOrganizationFullPath(@Param("orgId") Long orgId);

    /**
     * 查询GMP相关的组织（无论组织类型还是明确标记）
     */
    @Query("SELECT o FROM Organization o WHERE o.gmpRelevant = true OR o.orgType IN ('QUALITY', 'PRODUCTION', 'COMPLIANCE', 'TRAINING')")
    List<Organization> findGmpRelevantOrganizations();

    /**
     * 查询需要负责人审核的组织
     */
    @Query("SELECT o FROM Organization o WHERE o.manager IS NOT NULL AND o.gmpResponsible IS NOT NULL AND o.orgStatus = 'ACTIVE'")
    List<Organization> findOrganizationsRequiringApproval();

    /**
     * 多条件组合查询组织
     */
    @Query("SELECT o FROM Organization o WHERE " +
           "(:orgCode IS NULL OR LOWER(o.orgCode) LIKE LOWER(CONCAT('%', :orgCode, '%'))) AND " +
           "(:orgName IS NULL OR LOWER(o.orgName) LIKE LOWER(CONCAT('%', :orgName, '%'))) AND " +
           "(:orgType IS NULL OR o.orgType = :orgType) AND " +
           "(:orgStatus IS NULL OR o.orgStatus = :orgStatus) AND " +
           "(:gmpRelevant IS NULL OR o.gmpRelevant = :gmpRelevant) AND " +
           "(:manager IS NULL OR LOWER(o.manager) LIKE LOWER(CONCAT('%', :manager, '%')))")
    Page<Organization> findOrganizationsWithFilters(@Param("orgCode") String orgCode,
                                                   @Param("orgName") String orgName,
                                                   @Param("orgType") Organization.OrganizationType orgType,
                                                   @Param("orgStatus") Organization.OrganizationStatus orgStatus,
                                                   @Param("gmpRelevant") Boolean gmpRelevant,
                                                   @Param("manager") String manager,
                                                   Pageable pageable);

    /**
     * 检查组织编码是否存在
     */
    boolean existsByOrgCode(String orgCode);

    /**
     * 查询顶级组织（没有父组织的根组织）
     */
    @Query("SELECT o FROM Organization o WHERE o.parent IS NULL ORDER BY o.hierarchyLevel, o.orgName")
    List<Organization> findRootOrganizations();

    /**
     * 查询指定层级的所有组织
     */
    List<Organization> findByHierarchyLevelOrderByOrgName(Integer hierarchyLevel);

    /**
     * 统计不同类型的组织数量
     */
    @Query("SELECT o.orgType, COUNT(o) FROM Organization o WHERE o.orgStatus = 'ACTIVE' GROUP BY o.orgType")
    List<Object[]> countOrganizationsByType();

    /**
     * 查找最近修改的组织（用于缓存同步）
     */
    @Query("SELECT o FROM Organization o WHERE o.updatedAt > :since ORDER BY o.updatedAt DESC")
    List<Organization> findRecentlyModifiedOrganizations(@Param("since") java.time.LocalDateTime since);
}

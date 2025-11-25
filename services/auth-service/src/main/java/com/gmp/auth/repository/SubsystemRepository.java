package com.gmp.auth.repository;

import com.gmp.auth.entity.Subsystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 子系统数据访问接口
 *
 * @author GMP系统开发团队
 */
@Repository
public interface SubsystemRepository extends JpaRepository<Subsystem, Long> {

    /**
     * 根据子系统代码查询子系统
     * @param subsystemCode 子系统代码
     * @return 子系统对象
     */
    Optional<Subsystem> findBySubsystemCode(String subsystemCode);

    /**
     * 查询所有启用的子系统
     * @return 启用的子系统列表
     */
    List<Subsystem> findByEnabledTrueOrderBySortOrderAsc();

    /**
     * 查询所有GMP关键子系统
     * @return GMP关键子系统列表
     */
    List<Subsystem> findByGmpCriticalTrueOrderBySortOrderAsc();

    /**
     * 根据子系统代码列表查询子系统
     * @param subsystemCodes 子系统代码列表
     * @return 子系统列表
     */
    List<Subsystem> findBySubsystemCodeIn(List<String> subsystemCodes);

    /**
     * 检查子系统代码是否已存在
     * @param subsystemCode 子系统代码
     * @param id 子系统ID（排除自身，用于更新时检查）
     * @return 是否已存在
     */
    boolean existsBySubsystemCodeAndIdNot(String subsystemCode, Long id);

    /**
     * 根据名称模糊查询子系统
     * @param name 子系统名称关键词
     * @return 匹配的子系统列表
     */
    @Query("SELECT s FROM Subsystem s WHERE s.subsystemName LIKE %:name% ORDER BY s.sortOrder ASC")
    List<Subsystem> findByNameContaining(String name);

    /**
     * 查询系统中启用的子系统数量
     * @return 启用的子系统数量
     */
    long countByEnabledTrue();
}
package com.gmp.qms.repository;

import com.gmp.qms.entity.ChangeControl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 变更控制数据访问层接口
 * 
 * @author GMP系统开发团队
 */
@Repository
public interface ChangeControlRepository extends JpaRepository<ChangeControl, Long>, JpaSpecificationExecutor<ChangeControl> {

    /**
     * 根据变更编号查询
     * 
     * @param changeCode 变更编号
     * @return 变更控制实体
     */
    ChangeControl findByChangeCode(String changeCode);

    /**
     * 根据状态查询变更控制列表
     * 
     * @param status 变更状态
     * @param pageable 分页参数
     * @return 变更控制分页列表
     */
    Page<ChangeControl> findByStatus(ChangeControl.ChangeStatus status, Pageable pageable);

    /**
     * 根据变更类型查询变更控制列表
     * 
     * @param changeType 变更类型
     * @param pageable 分页参数
     * @return 变更控制分页列表
     */
    Page<ChangeControl> findByChangeType(String changeType, Pageable pageable);

    /**
     * 根据风险级别查询变更控制列表
     * 
     * @param riskLevel 风险级别
     * @param pageable 分页参数
     * @return 变更控制分页列表
     */
    Page<ChangeControl> findByRiskLevel(ChangeControl.RiskLevel riskLevel, Pageable pageable);

    /**
     * 根据变更负责人查询变更控制列表
     * 
     * @param changeOwnerId 变更负责人ID
     * @param pageable 分页参数
     * @return 变更控制分页列表
     */
    Page<ChangeControl> findByChangeOwnerId(Long changeOwnerId, Pageable pageable);

    /**
     * 查询超过计划实施日期的变更控制
     * 
     * @param now 当前时间
     * @param excludedStatuses 排除的状态列表
     * @return 过期变更控制列表
     */
    @Query("SELECT cc FROM ChangeControl cc WHERE cc.plannedImplementationDate < :now AND cc.status NOT IN :excludedStatuses ORDER BY cc.plannedImplementationDate ASC")
    List<ChangeControl> findOverdueChangeControls(LocalDateTime now, List<ChangeControl.ChangeStatus> excludedStatuses);

    /**
     * 根据创建人查询变更控制列表
     * 
     * @param createdBy 创建人ID
     * @param pageable 分页参数
     * @return 变更控制分页列表
     */
    Page<ChangeControl> findByCreatedBy(Long createdBy, Pageable pageable);

    /**
     * 统计不同状态的变更控制数量
     * 
     * @return 状态统计结果
     */
    @Query("SELECT cc.status, COUNT(cc) FROM ChangeControl cc GROUP BY cc.status")
    List<Object[]> countByStatus();
}

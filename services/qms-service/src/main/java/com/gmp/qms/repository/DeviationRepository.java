package com.gmp.qms.repository;

import com.gmp.qms.entity.Deviation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 偏差数据访问层接口
 * 
 * @author GMP系统开发团队
 */
@Repository
public interface DeviationRepository extends JpaRepository<Deviation, Long>, JpaSpecificationExecutor<Deviation> {

    /**
     * 根据偏差编号查询
     * 
     * @param deviationCode 偏差编号
     * @return 偏差实体
     */
    Deviation findByDeviationCode(String deviationCode);

    /**
     * 根据状态查询偏差列表
     * 
     * @param status 偏差状态
     * @param pageable 分页参数
     * @return 偏差分页列表
     */
    Page<Deviation> findByStatus(Deviation.DeviationStatus status, Pageable pageable);

    /**
     * 根据责任人查询偏差列表
     * 
     * @param responsiblePersonId 责任人ID
     * @param pageable 分页参数
     * @return 偏差分页列表
     */
    Page<Deviation> findByResponsiblePersonId(Long responsiblePersonId, Pageable pageable);

    /**
     * 根据严重程度查询偏差列表
     * 
     * @param severityLevel 严重程度
     * @param pageable 分页参数
     * @return 偏差分页列表
     */
    Page<Deviation> findBySeverityLevel(Deviation.SeverityLevel severityLevel, Pageable pageable);

    /**
     * 根据发现日期范围查询偏差列表
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param pageable 分页参数
     * @return 偏差分页列表
     */
    Page<Deviation> findByDiscoveryDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    /**
     * 查询指定状态且超过目标完成日期的偏差
     * 
     * @param status 偏差状态
     * @param now 当前时间
     * @return 偏差列表
     */
    @Query("SELECT d FROM Deviation d WHERE d.status = :status AND d.occurrenceDate < :now ORDER BY d.occurrenceDate ASC")
    List<Deviation> findOverdueDeviations(Deviation.DeviationStatus status, LocalDateTime now);

    /**
     * 根据创建人查询偏差列表
     * 
     * @param createdBy 创建人ID
     * @param pageable 分页参数
     * @return 偏差分页列表
     */
    Page<Deviation> findByCreatedBy(Long createdBy, Pageable pageable);

    /**
     * 统计不同状态的偏差数量
     * 
     * @return 状态统计结果
     */
    @Query("SELECT d.status, COUNT(d) FROM Deviation d GROUP BY d.status")
    List<Object[]> countByStatus();
}

package com.gmp.qms.repository;

import com.gmp.qms.entity.Capa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * CAPA数据访问层接口
 * 
 * @author GMP系统开发团队
 */
@Repository
public interface CapaRepository extends JpaRepository<Capa, Long>, JpaSpecificationExecutor<Capa> {

    /**
     * 根据CAPA编号查询
     * 
     * @param capaCode CAPA编号
     * @return CAPA实体
     */
    Capa findByCapaCode(String capaCode);

    /**
     * 根据状态查询CAPA列表
     * 
     * @param status CAPA状态
     * @param pageable 分页参数
     * @return CAPA分页列表
     */
    Page<Capa> findByStatus(Capa.CapaStatus status, Pageable pageable);

    /**
     * 根据责任人查询CAPA列表
     * 
     * @param responsiblePersonId 责任人ID
     * @param pageable 分页参数
     * @return CAPA分页列表
     */
    Page<Capa> findByResponsiblePersonId(Long responsiblePersonId, Pageable pageable);

    /**
     * 根据优先级查询CAPA列表
     * 
     * @param priorityLevel 优先级
     * @param pageable 分页参数
     * @return CAPA分页列表
     */
    Page<Capa> findByPriorityLevel(Capa.PriorityLevel priorityLevel, Pageable pageable);

    /**
     * 根据来源类型和来源ID查询CAPA列表
     * 
     * @param sourceType 来源类型
     * @param sourceId 来源ID
     * @param pageable 分页参数
     * @return CAPA分页列表
     */
    Page<Capa> findBySourceTypeAndSourceId(String sourceType, Long sourceId, Pageable pageable);

    /**
     * 查询超过目标完成日期的CAPA
     * 
     * @param now 当前时间
     * @param excludedStatuses 排除的状态列表
     * @return 过期CAPA列表
     */
    @Query("SELECT c FROM Capa c WHERE c.targetCompletionDate < :now AND c.status NOT IN :excludedStatuses ORDER BY c.targetCompletionDate ASC")
    List<Capa> findOverdueCapa(LocalDateTime now, List<Capa.CapaStatus> excludedStatuses);

    /**
     * 根据创建人查询CAPA列表
     * 
     * @param createdBy 创建人ID
     * @param pageable 分页参数
     * @return CAPA分页列表
     */
    Page<Capa> findByCreatedBy(Long createdBy, Pageable pageable);

    /**
     * 统计不同状态的CAPA数量
     * 
     * @return 状态统计结果
     */
    @Query("SELECT c.status, COUNT(c) FROM Capa c GROUP BY c.status")
    List<Object[]> countByStatus();
}

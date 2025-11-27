package com.gmp.training.repository;

import com.gmp.training.entity.TrainingNeed;
import com.gmp.training.entity.TrainingNeed.NeedSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 培训需求数据访问层接口
 * 
 * @author GMP系统开发团队
 */
@Repository
public interface TrainingNeedRepository
        extends JpaRepository<TrainingNeed, Long>, JpaSpecificationExecutor<TrainingNeed> {

    /**
     * 根据部门查找培训需求
     * 
     * @param departmentId 部门ID
     * @return 培训需求列表
     */
    List<TrainingNeed> findByDepartmentId(Long departmentId);

    /**
     * 根据岗位查找培训需求
     * 
     * @param positionId 岗位ID
     * @return 培训需求列表
     */
    List<TrainingNeed> findByPositionId(Long positionId);

    /**
     * 根据创建者查找培训需求
     * 
     * @param creatorId 创建者ID
     * @return 培训需求列表
     */
    List<TrainingNeed> findByCreatorId(Long creatorId);

    /**
     * 根据状态查找培训需求
     * 
     * @param status 状态
     * @return 培训需求列表
     */
    List<TrainingNeed> findByStatus(TrainingNeed.Status status);

    /**
     * 根据优先级查找培训需求
     * 
     * @param priority 优先级
     * @return 培训需求列表
     */
    List<TrainingNeed> findByPriority(TrainingNeed.Priority priority);

    /**
     * 根据需求来源查找培训需求
     * 
     * @param source 需求来源
     * @return 培训需求列表
     */
    List<TrainingNeed> findBySource(NeedSource source);

    /**
     * 根据需求来源和状态查找培训需求
     * 
     * @param source 需求来源
     * @param status 状态
     * @return 培训需求列表
     */
    List<TrainingNeed> findBySourceAndStatus(NeedSource source, TrainingNeed.Status status);

    /**
     * 查找特定时间范围内的培训需求
     * 
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 培训需求列表
     */
    List<TrainingNeed> findByCreatedTimeBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 查找已批准但未关联培训计划的需求
     * 
     * @return 培训需求列表
     */
    List<TrainingNeed> findByStatusAndTrainingPlanIsNull(TrainingNeed.Status status);

    /**
     * 查找GMP相关的培训需求
     * 
     * @param gmpRelated 是否GMP相关
     * @return 培训需求列表
     */
    List<TrainingNeed> findByGmpRelated(boolean gmpRelated);
}

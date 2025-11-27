package com.gmp.training.repository;

import com.gmp.training.entity.TrainingPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 培训计划数据访问层接口
 * 
 * @author GMP系统开发团队
 */
@Repository
public interface TrainingPlanRepository extends JpaRepository<TrainingPlan, Long>, JpaSpecificationExecutor<TrainingPlan> {

    /**
     * 根据年度查找培训计划
     * 
     * @param year 年度
     * @return 培训计划列表
     */
    List<TrainingPlan> findByYear(Integer year);

    /**
     * 根据年度和季度查找培训计划
     * 
     * @param year 年度
     * @param quarter 季度
     * @return 培训计划列表
     */
    List<TrainingPlan> findByYearAndQuarter(Integer year, Integer quarter);

    /**
     * 根据负责人查找培训计划
     * 
     * @param responsiblePersonId 负责人ID
     * @return 培训计划列表
     */
    List<TrainingPlan> findByResponsiblePersonId(Long responsiblePersonId);

    /**
     * 根据状态查找培训计划
     * 
     * @param status 状态
     * @return 培训计划列表
     */
    List<TrainingPlan> findByStatus(TrainingPlan.Status status);

    /**
     * 查找特定时间范围内的培训计划
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 培训计划列表
     */
    List<TrainingPlan> findByPlanStartDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 查找已结束但未归档的培训计划
     * 
     * @param status 状态
     * @param endDate 结束日期
     * @return 培训计划列表
     */
    List<TrainingPlan> findByStatusAndPlanEndDateBefore(TrainingPlan.Status status, LocalDateTime endDate);

    /**
     * 查找包含特定培训需求的计划
     * 
     * @param trainingNeedId 培训需求ID
     * @return 培训计划列表
     */
    List<TrainingPlan> findByTrainingNeedsId(Long trainingNeedId);

    /**
     * 查找GMP相关的培训计划
     * 
     * @param gmpRelated 是否GMP相关
     * @return 培训计划列表
     */
    List<TrainingPlan> findByGmpRelated(boolean gmpRelated);

    /**
     * 查找需要审核的培训计划
     * 
     * @param status 状态
     * @return 培训计划列表
     */
    List<TrainingPlan> findByStatusIn(List<TrainingPlan.Status> status);

    /**
     * 根据计划名称模糊查询
     * 
     * @param name 计划名称
     * @return 培训计划列表
     */
    List<TrainingPlan> findByNameContaining(String name);
}

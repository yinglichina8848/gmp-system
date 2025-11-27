package com.gmp.training.service;

import com.gmp.training.entity.TrainingNeed;
import com.gmp.training.entity.TrainingPlan;
import com.gmp.training.entity.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 培训计划服务接口
 * 
 * @author GMP系统开发团队
 */
public interface TrainingPlanService extends BaseService<TrainingPlan, Long> {

    /**
     * 根据计划名称查找培训计划
     * 
     * @param name 计划名称
     * @return 培训计划信息
     */
    Optional<TrainingPlan> findByName(String name);

    /**
     * 查找特定年度的培训计划
     * 
     * @param year 年度
     * @return 培训计划列表
     */
    List<TrainingPlan> findByYear(Integer year);

    /**
     * 查找特定年度和季度的培训计划
     * 
     * @param year 年度
     * @param quarter 季度
     * @return 培训计划列表
     */
    List<TrainingPlan> findByYearAndQuarter(Integer year, Integer quarter);

    /**
     * 查找特定负责人的培训计划
     * 
     * @param owner 负责人
     * @return 培训计划列表
     */
    List<TrainingPlan> findByOwner(User owner);

    /**
     * 查找特定状态的培训计划
     * 
     * @param status 状态
     * @return 培训计划列表
     */
    List<TrainingPlan> findByStatus(TrainingPlan.Status status);

    /**
     * 查找特定培训需求关联的培训计划
     * 
     * @param trainingNeed 培训需求
     * @return 培训计划列表
     */
    List<TrainingPlan> findByTrainingNeed(TrainingNeed trainingNeed);

    /**
     * 查找时间范围内的培训计划
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 培训计划列表
     */
    List<TrainingPlan> findByPlanStartDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * 查找部门相关的培训计划
     * 
     * @param departmentId 部门ID
     * @return 培训计划列表
     */
    List<TrainingPlan> findByDepartmentId(Long departmentId);

    /**
     * 更新培训计划状态
     * 
     * @param id 计划ID
     * @param status 状态
     * @return 更新后的培训计划
     */
    TrainingPlan updateStatus(Long id, TrainingPlan.Status status);

    /**
     * 批量更新培训计划状态
     * 
     * @param ids 计划ID列表
     * @param status 状态
     * @return 影响的行数
     */
    int batchUpdateStatus(List<Long> ids, TrainingPlan.Status status);

    /**
     * 获取当前活跃的培训计划
     * 
     * @return 活跃的培训计划列表
     */
    List<TrainingPlan> findActivePlans();

    /**
     * 获取已完成的培训计划
     * 
     * @return 已完成的培训计划列表
     */
    List<TrainingPlan> findCompletedPlans();

    /**
     * 搜索培训计划
     * 
     * @param keyword 关键词
     * @return 培训计划列表
     */
    List<TrainingPlan> search(String keyword);

    /**
     * 计算培训计划的实际完成率
     * 
     * @param id 计划ID
     * @return 完成率（0-100）
     */
    Double calculateCompletionRate(Long id);

    /**
     * 计算培训计划的实际花费
     * 
     * @param id 计划ID
     * @return 实际花费
     */
    Double calculateActualCost(Long id);
}
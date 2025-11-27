package com.gmp.training.service.impl;

import com.gmp.training.entity.TrainingNeed;
import com.gmp.training.entity.TrainingPlan;
import com.gmp.training.entity.User;
import com.gmp.training.repository.TrainingPlanRepository;
import com.gmp.training.service.TrainingPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 培训计划服务实现类
 * 
 * @author GMP系统开发团队
 */
@Service
@Transactional(readOnly = true)
public class TrainingPlanServiceImpl extends BaseServiceImpl<TrainingPlan, Long, TrainingPlanRepository> implements TrainingPlanService {

    @Autowired
    public TrainingPlanServiceImpl(TrainingPlanRepository repository) {
        super(repository);
    }

    @Override
    public Optional<TrainingPlan> findByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public List<TrainingPlan> findByYear(Integer year) {
        return repository.findByYear(year);
    }

    @Override
    public List<TrainingPlan> findByYearAndQuarter(Integer year, Integer quarter) {
        return repository.findByYearAndQuarter(year, quarter);
    }

    @Override
    public List<TrainingPlan> findByOwner(User owner) {
        return repository.findByOwner(owner);
    }

    @Override
    public List<TrainingPlan> findByStatus(TrainingPlan.Status status) {
        return repository.findByStatus(status);
    }

    @Override
    public List<TrainingPlan> findByTrainingNeed(TrainingNeed trainingNeed) {
        return repository.findByTrainingNeedsContaining(trainingNeed);
    }

    @Override
    public List<TrainingPlan> findByPlanStartDateBetween(LocalDate startDate, LocalDate endDate) {
        return repository.findByPlanStartDateBetween(startDate, endDate);
    }

    @Override
    public List<TrainingPlan> findByDepartmentId(Long departmentId) {
        return repository.findByDepartmentId(departmentId);
    }

    @Override
    @Transactional
    public TrainingPlan updateStatus(Long id, TrainingPlan.Status status) {
        TrainingPlan plan = getById(id);
        plan.setStatus(status);
        // 如果标记为已完成，更新完成时间
        if (status == TrainingPlan.Status.COMPLETED) {
            plan.setActualEndDate(LocalDate.now());
        }
        return repository.save(plan);
    }

    @Override
    @Transactional
    public int batchUpdateStatus(List<Long> ids, TrainingPlan.Status status) {
        List<TrainingPlan> plans = repository.findAllById(ids);
        plans.forEach(plan -> {
            plan.setStatus(status);
            // 如果标记为已完成，更新完成时间
            if (status == TrainingPlan.Status.COMPLETED) {
                plan.setActualEndDate(LocalDate.now());
            }
        });
        repository.saveAll(plans);
        return plans.size();
    }

    @Override
    public List<TrainingPlan> findActivePlans() {
        LocalDate now = LocalDate.now();
        return repository.findByStatusInAndPlanStartDateLessThanEqualAndPlanEndDateGreaterThanEqual(
                List.of(TrainingPlan.Status.IN_PROGRESS, TrainingPlan.Status.PLANNED), now, now);
    }

    @Override
    public List<TrainingPlan> findCompletedPlans() {
        return repository.findByStatus(TrainingPlan.Status.COMPLETED);
    }

    @Override
    public List<TrainingPlan> search(String keyword) {
        return repository.findByNameContainingOrDescriptionContaining(keyword, keyword);
    }

    @Override
    public Double calculateCompletionRate(Long id) {
        // 这里需要根据实际的业务逻辑实现完成率计算
        // 基本思路：计算计划中已完成的课程数量占总课程数量的比例
        TrainingPlan plan = getById(id);
        // 假设计划中有关联的课程，通过统计课程完成情况计算完成率
        // 实际实现可能需要更复杂的逻辑，比如考虑培训场次的完成情况
        return 0.0; // 暂时返回默认值，实际实现需要根据具体业务逻辑调整
    }

    @Override
    public Double calculateActualCost(Long id) {
        // 这里需要根据实际的业务逻辑实现实际花费计算
        // 基本思路：统计计划中所有培训活动的实际花费
        TrainingPlan plan = getById(id);
        // 实际实现可能需要查询培训场次、材料费用等信息进行汇总
        return 0.0; // 暂时返回默认值，实际实现需要根据具体业务逻辑调整
    }

    @Override
    @Transactional
    public TrainingPlan save(TrainingPlan plan) {
        // 新增培训计划时设置默认状态
        if (plan.getId() == null) {
            plan.setStatus(TrainingPlan.Status.PLANNED); // 默认状态为已规划
        }
        return super.save(plan);
    }

    @Override
    protected String getEntityName() {
        return "TrainingPlan";
    }
}
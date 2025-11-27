package com.gmp.training.service;

import com.gmp.training.entity.TrainingCourse;
import com.gmp.training.entity.TrainingPlan;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 培训课程服务接口
 * 
 * @author GMP系统开发团队
 */
public interface TrainingCourseService extends BaseService<TrainingCourse, Long> {

    /**
     * 根据课程编码查找培训课程
     * 
     * @param code 课程编码
     * @return 培训课程信息
     */
    Optional<TrainingCourse> findByCode(String code);

    /**
     * 根据课程名称查找培训课程
     * 
     * @param name 课程名称
     * @return 培训课程信息
     */
    Optional<TrainingCourse> findByName(String name);

    /**
     * 查找特定培训计划的培训课程
     * 
     * @param trainingPlan 培训计划
     * @return 培训课程列表
     */
    List<TrainingCourse> findByTrainingPlan(TrainingPlan trainingPlan);

    /**
     * 查找特定状态的培训课程
     * 
     * @param status 状态
     * @return 培训课程列表
     */
    List<TrainingCourse> findByStatus(TrainingCourse.Status status);

    /**
     * 查找特定类型的培训课程
     * 
     * @param type 类型
     * @return 培训课程列表
     */
    List<TrainingCourse> findByType(TrainingCourse.Type type);

    /**
     * 查找特定培训方式的培训课程
     * 
     * @param method 培训方式
     * @return 培训课程列表
     */
    List<TrainingCourse> findByMethod(TrainingCourse.Method method);

    /**
     * 查找需要复训的培训课程
     * 
     * @return 需要复训的培训课程列表
     */
    List<TrainingCourse> findRecurringCourses();

    /**
     * 查找必修的培训课程
     * 
     * @return 必修的培训课程列表
     */
    List<TrainingCourse> findCompulsoryCourses();

    /**
     * 查找选修的培训课程
     * 
     * @return 选修的培训课程列表
     */
    List<TrainingCourse> findOptionalCourses();

    /**
     * 查找时间范围内创建的培训课程
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 培训课程列表
     */
    List<TrainingCourse> findByCreationDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * 更新培训课程状态
     * 
     * @param id 课程ID
     * @param status 状态
     * @return 更新后的培训课程
     */
    TrainingCourse updateStatus(Long id, TrainingCourse.Status status);

    /**
     * 批量更新培训课程状态
     * 
     * @param ids 课程ID列表
     * @param status 状态
     * @return 影响的行数
     */
    int batchUpdateStatus(List<Long> ids, TrainingCourse.Status status);

    /**
     * 搜索培训课程
     * 
     * @param keyword 关键词
     * @return 培训课程列表
     */
    List<TrainingCourse> search(String keyword);
}
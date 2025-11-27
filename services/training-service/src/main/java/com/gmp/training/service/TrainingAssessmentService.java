package com.gmp.training.service;

import com.gmp.training.entity.TrainingAssessment;
import com.gmp.training.entity.TrainingCourse;
import com.gmp.training.entity.User;

import java.time.LocalDate;
import java.util.List;

/**
 * 培训考核服务接口
 * 
 * @author GMP系统开发团队
 */
public interface TrainingAssessmentService extends BaseService<TrainingAssessment, Long> {

    /**
     * 根据培训课程查找考核
     * 
     * @param trainingCourse 培训课程
     * @return 培训考核列表
     */
    List<TrainingAssessment> findByTrainingCourse(TrainingCourse trainingCourse);

    /**
     * 根据用户查找考核记录
     * 
     * @param user 用户
     * @return 培训考核列表
     */
    List<TrainingAssessment> findByUser(User user);

    /**
     * 根据考核状态查找
     * 
     * @param status 状态
     * @return 培训考核列表
     */
    List<TrainingAssessment> findByStatus(TrainingAssessment.Status status);

    /**
     * 根据用户和培训课程查找考核记录
     * 
     * @param user 用户
     * @param trainingCourse 培训课程
     * @return 培训考核列表
     */
    List<TrainingAssessment> findByUserAndTrainingCourse(User user, TrainingCourse trainingCourse);

    /**
     * 查找特定时间范围内的考核记录
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 培训考核列表
     */
    List<TrainingAssessment> findByAssessmentDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * 查找未开始的考核
     * 
     * @return 未开始的考核列表
     */
    List<TrainingAssessment> findNotStartedAssessments();

    /**
     * 查找进行中的考核
     * 
     * @return 进行中的考核列表
     */
    List<TrainingAssessment> findInProgressAssessments();

    /**
     * 查找已完成的考核
     * 
     * @return 已完成的考核列表
     */
    List<TrainingAssessment> findCompletedAssessments();

    /**
     * 更新考核状态
     * 
     * @param id 考核ID
     * @param status 状态
     * @return 更新后的考核
     */
    TrainingAssessment updateStatus(Long id, TrainingAssessment.Status status);

    /**
     * 批量更新考核状态
     * 
     * @param ids 考核ID列表
     * @param status 状态
     * @return 影响的行数
     */
    int batchUpdateStatus(List<Long> ids, TrainingAssessment.Status status);

    /**
     * 搜索考核记录
     * 
     * @param keyword 关键词
     * @return 考核记录列表
     */
    List<TrainingAssessment> search(String keyword);

    /**
     * 根据考核类型查找
     * 
     * @param type 考核类型
     * @return 培训考核列表
     */
    List<TrainingAssessment> findByType(TrainingAssessment.Type type);

    /**
     * 获取培训课程的平均考核分数
     * 
     * @param courseId 课程ID
     * @return 平均考核分数
     */
    Double getAverageScoreByCourseId(Long courseId);

    /**
     * 获取用户的平均考核分数
     * 
     * @param userId 用户ID
     * @return 平均考核分数
     */
    Double getAverageScoreByUserId(Long userId);

    /**
     * 查找考核通过的记录
     * 
     * @param passScore 及格分数
     * @return 通过考核的记录列表
     */
    List<TrainingAssessment> findPassedAssessments(Double passScore);
}
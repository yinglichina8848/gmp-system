package com.gmp.training.service;

import com.gmp.training.entity.TrainingCourse;
import com.gmp.training.entity.TrainingRecord;
import com.gmp.training.entity.TrainingSession;
import com.gmp.training.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 培训记录服务接口
 * 
 * @author GMP系统开发团队
 */
public interface TrainingRecordService extends BaseService<TrainingRecord, Long> {

    /**
     * 根据用户查找培训记录
     * 
     * @param user 用户
     * @return 培训记录列表
     */
    List<TrainingRecord> findByUser(User user);

    /**
     * 根据培训场次查找培训记录
     * 
     * @param trainingSession 培训场次
     * @return 培训记录列表
     */
    List<TrainingRecord> findByTrainingSession(TrainingSession trainingSession);

    /**
     * 根据培训课程查找培训记录
     * 
     * @param trainingCourse 培训课程
     * @return 培训记录列表
     */
    List<TrainingRecord> findByTrainingCourse(TrainingCourse trainingCourse);

    /**
     * 根据培训记录状态查找
     * 
     * @param status 状态
     * @return 培训记录列表
     */
    List<TrainingRecord> findByStatus(TrainingRecord.Status status);

    /**
     * 根据用户和培训课程查找培训记录
     * 
     * @param user 用户
     * @param trainingCourse 培训课程
     * @return 培训记录列表
     */
    List<TrainingRecord> findByUserAndTrainingCourse(User user, TrainingCourse trainingCourse);

    /**
     * 根据用户和培训状态查找培训记录
     * 
     * @param user 用户
     * @param status 状态
     * @return 培训记录列表
     */
    List<TrainingRecord> findByUserAndStatus(User user, TrainingRecord.Status status);

    /**
     * 查找特定时间范围内的培训记录
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 培训记录列表
     */
    List<TrainingRecord> findByTrainingDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * 查找未签到的培训记录
     * 
     * @return 未签到的培训记录列表
     */
    List<TrainingRecord> findNotAttendedRecords();

    /**
     * 更新培训记录状态
     * 
     * @param id 记录ID
     * @param status 状态
     * @return 更新后的培训记录
     */
    TrainingRecord updateStatus(Long id, TrainingRecord.Status status);

    /**
     * 批量更新培训记录状态
     * 
     * @param ids 记录ID列表
     * @param status 状态
     * @return 影响的行数
     */
    int batchUpdateStatus(List<Long> ids, TrainingRecord.Status status);

    /**
     * 搜索培训记录
     * 
     * @param keyword 关键词
     * @return 培训记录列表
     */
    List<TrainingRecord> search(String keyword);

    /**
     * 获取用户的培训完成率
     * 
     * @param userId 用户ID
     * @return 培训完成率（0-100）
     */
    Double getUserCompletionRate(Long userId);

    /**
     * 获取培训课程的总体完成率
     * 
     * @param courseId 课程ID
     * @return 总体完成率（0-100）
     */
    Double getCourseCompletionRate(Long courseId);

    /**
     * 根据部门统计培训记录
     * 
     * @param departmentId 部门ID
     * @return 培训记录列表
     */
    List<TrainingRecord> findByDepartmentId(Long departmentId);

    /**
     * 根据岗位统计培训记录
     * 
     * @param positionId 岗位ID
     * @return 培训记录列表
     */
    List<TrainingRecord> findByPositionId(Long positionId);
}
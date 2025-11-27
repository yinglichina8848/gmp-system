package com.gmp.training.repository;

import com.gmp.training.entity.TrainingAssessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * 培训考核数据访问层
 * 
 * @author GMP系统开发团队
 */
@Repository
public interface TrainingAssessmentRepository extends JpaRepository<TrainingAssessment, Long> {

    /**
     * 根据培训场次ID查询考核记录
     * 
     * @param sessionId 培训场次ID
     * @return 考核记录列表
     */
    List<TrainingAssessment> findByTrainingSessionId(Long sessionId);

    /**
     * 根据用户ID查询考核记录
     * 
     * @param userId 用户ID
     * @return 考核记录列表
     */
    List<TrainingAssessment> findByUserId(Long userId);

    /**
     * 根据培训课程ID查询考核记录
     * 
     * @param courseId 培训课程ID
     * @return 考核记录列表
     */
    List<TrainingAssessment> findByTrainingCourseId(Long courseId);

    /**
     * 根据用户ID和培训场次ID查询考核记录
     * 
     * @param userId    用户ID
     * @param sessionId 培训场次ID
     * @return 考核记录
     */
    TrainingAssessment findByUserIdAndTrainingSessionId(Long userId, Long sessionId);

    /**
     * 查询用户在特定培训课程的考核记录
     * 
     * @param userId   用户ID
     * @param courseId 培训课程ID
     * @return 考核记录列表
     */
    List<TrainingAssessment> findByUserIdAndTrainingCourseId(Long userId, Long courseId);

    /**
     * 统计培训场次的考核通过率
     * 
     * @param sessionId 培训场次ID
     * @return 通过率 (0-100之间的数值)
     */
    @Query("SELECT COUNT(CASE WHEN a.score >= 60 THEN 1 END) * 100.0 / COUNT(a) FROM TrainingAssessment a WHERE a.trainingSession.id = :sessionId")
    Double calculatePassRateBySession(@Param("sessionId") Long sessionId);

    /**
     * 查询特定培训场次中未通过考核的用户
     * 
     * @param sessionId 培训场次ID
     * @return 未通过考核的记录列表
     */
    @Query("SELECT a FROM TrainingAssessment a WHERE a.trainingSession.id = :sessionId AND a.score < 60")
    List<TrainingAssessment> findFailedAssessmentsBySession(@Param("sessionId") Long sessionId);

    /**
     * 根据培训课程查询考核记录
     * 
     * @param trainingCourse 培训课程
     * @return 考核记录列表
     */
    List<TrainingAssessment> findByTrainingCourse(com.gmp.training.entity.TrainingCourse trainingCourse);

    /**
     * 根据用户查询考核记录
     * 
     * @param user 用户
     * @return 考核记录列表
     */
    List<TrainingAssessment> findByUser(com.gmp.training.entity.User user);

    /**
     * 根据状态查询考核记录
     * 
     * @param status 考核状态
     * @return 考核记录列表
     */
    List<TrainingAssessment> findByStatus(TrainingAssessment.Status status);

    /**
     * 根据用户和培训课程查询考核记录
     * 
     * @param user           用户
     * @param trainingCourse 培训课程
     * @return 考核记录列表
     */
    List<TrainingAssessment> findByUserAndTrainingCourse(com.gmp.training.entity.User user,
            com.gmp.training.entity.TrainingCourse trainingCourse);

    /**
     * 根据考核日期范围查询考核记录
     * 
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 考核记录列表
     */
    List<TrainingAssessment> findByAssessmentDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * 根据备注内容模糊查询考核记录
     * 
     * @param notes 备注内容
     * @return 考核记录列表
     */
    List<TrainingAssessment> findByNotesContaining(String notes);

    /**
     * 根据考核类型查询考核记录
     * 
     * @param type 考核类型
     * @return 考核记录列表
     */
    List<TrainingAssessment> findByType(TrainingAssessment.Type type);
    
    /**
     * 根据分数大于等于指定值且状态查询考核记录
     * 
     * @param score 分数
     * @param status 状态
     * @return 考核记录列表
     */
    List<TrainingAssessment> findByScoreGreaterThanEqualAndStatus(Double score, TrainingAssessment.Status status);
}
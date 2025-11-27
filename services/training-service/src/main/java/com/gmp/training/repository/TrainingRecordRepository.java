package com.gmp.training.repository;

import com.gmp.training.entity.TrainingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 培训记录数据访问层接口
 * 
 * @author GMP系统开发团队
 */
@Repository
public interface TrainingRecordRepository extends JpaRepository<TrainingRecord, Long>, JpaSpecificationExecutor<TrainingRecord> {

    /**
     * 根据培训场次查找培训记录
     * 
     * @param sessionId 培训场次ID
     * @return 培训记录列表
     */
    List<TrainingRecord> findBySessionId(Long sessionId);

    /**
     * 根据员工查找培训记录
     * 
     * @param participantId 员工ID
     * @return 培训记录列表
     */
    List<TrainingRecord> findByParticipantId(Long participantId);

    /**
     * 根据培训场次和员工查找特定的培训记录
     * 
     * @param sessionId 培训场次ID
     * @param participantId 员工ID
     * @return 培训记录
     */
    Optional<TrainingRecord> findBySessionIdAndParticipantId(Long sessionId, Long participantId);

    /**
     * 根据出勤状态查找培训记录
     * 
     * @param attendanceStatus 出勤状态
     * @return 培训记录列表
     */
    List<TrainingRecord> findByAttendanceStatus(TrainingRecord.AttendanceStatus attendanceStatus);

    /**
     * 根据考核结果查找培训记录
     * 
     * @param assessmentResult 考核结果
     * @return 培训记录列表
     */
    List<TrainingRecord> findByAssessmentResult(TrainingRecord.AssessmentResult assessmentResult);

    /**
     * 查找特定时间范围内的培训记录
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 培训记录列表
     */
    List<TrainingRecord> findByCreatedTimeBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 查找员工在特定课程的所有培训记录
     * 
     * @param participantId 员工ID
     * @param courseId 课程ID
     * @return 培训记录列表
     */
    List<TrainingRecord> findByParticipantIdAndSessionCourseId(Long participantId, Long courseId);

    /**
     * 查找特定培训计划下的培训记录
     * 
     * @param trainingPlanId 培训计划ID
     * @return 培训记录列表
     */
    List<TrainingRecord> findBySessionCourseTrainingPlanId(Long trainingPlanId);

    /**
     * 查找已完成且考核通过的培训记录
     * 
     * @param attendanceStatus 出勤状态
     * @param assessmentResult 考核结果
     * @return 培训记录列表
     */
    List<TrainingRecord> findByAttendanceStatusAndAssessmentResult(TrainingRecord.AttendanceStatus attendanceStatus, TrainingRecord.AssessmentResult assessmentResult);

    /**
     * 统计员工的培训时长
     * 
     * @param participantId 员工ID
     * @return 总培训时长
     */
    Double sumActualTrainingHoursByParticipantId(Long participantId);

    /**
     * 查找员工最新的培训记录
     * 
     * @param participantId 员工ID
     * @param limit 记录数量
     * @return 培训记录列表
     */
    List<TrainingRecord> findTopNByParticipantIdOrderByCreatedTimeDesc(Long participantId, int limit);
}

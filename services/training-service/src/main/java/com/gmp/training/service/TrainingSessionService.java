package com.gmp.training.service;

import com.gmp.training.entity.TrainingSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 培训场次服务接口
 * 
 * @author GMP系统开发团队
 */
public interface TrainingSessionService {

    /**
     * 根据ID查找培训场次
     * 
     * @param id 培训场次ID
     * @return 培训场次信息
     */
    Optional<TrainingSession> findById(Long id);

    /**
     * 保存培训场次
     * 
     * @param trainingSession 培训场次
     * @return 保存后的培训场次
     */
    TrainingSession save(TrainingSession trainingSession);

    /**
     * 删除培训场次
     * 
     * @param id 培训场次ID
     */
    void delete(Long id);

    /**
     * 查找所有培训场次
     * 
     * @param pageable 分页参数
     * @return 培训场次列表
     */
    Page<TrainingSession> findAll(Pageable pageable);

    /**
     * 根据课程ID查找培训场次（带分页）
     * 
     * @param courseId 课程ID
     * @param pageable 分页参数
     * @return 培训场次列表
     */
    Page<TrainingSession> findByCourseId(Long courseId, Pageable pageable);

    /**
     * 根据讲师ID查找培训场次（带分页）
     * 
     * @param lecturerId 讲师ID
     * @param pageable   分页参数
     * @return 培训场次列表
     */
    Page<TrainingSession> findByLecturerId(Long lecturerId, Pageable pageable);

    /**
     * 根据状态查找培训场次
     * 
     * @param status 状态
     * @return 培训场次列表
     */
    List<TrainingSession> findByStatus(TrainingSession.Status status);

    /**
     * 根据状态查找培训场次（带分页）
     * 
     * @param status   培训场次状态
     * @param pageable 分页参数
     * @return 培训场次分页列表
     */
    Page<TrainingSession> findByStatus(TrainingSession.Status status, Pageable pageable);

    /**
     * 根据时间范围查找培训场次
     * 
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 培训场次列表
     */
    List<TrainingSession> findByStartTimeBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 根据培训方式查找培训场次（带分页）
     * 
     * @param method   培训方式
     * @param pageable 分页参数
     * @return 培训场次分页列表
     */
    Page<TrainingSession> findByMethod(TrainingSession.Method method, Pageable pageable);

    /**
     * 根据培训地点查找培训场次（带分页）
     * 
     * @param venue    培训地点
     * @param pageable 分页参数
     * @return 培训场次分页列表
     */
    Page<TrainingSession> findByVenue(String venue, Pageable pageable);

    /**
     * 根据场次日期范围查找培训场次（带分页）
     * 
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @param pageable  分页参数
     * @return 培训场次分页列表
     */
    Page<TrainingSession> findBySessionDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    /**
     * 查找未来的培训场次（带分页）
     * 
     * @param pageable 分页参数
     * @return 培训场次分页列表
     */
    Page<TrainingSession> findFutureSessions(Pageable pageable);

    /**
     * 查找已完成的培训场次（带分页）
     * 
     * @param pageable 分页参数
     * @return 培训场次分页列表
     */
    Page<TrainingSession> findCompletedSessions(Pageable pageable);

    /**
     * 查找进行中的培训场次（带分页）
     * 
     * @param pageable 分页参数
     * @return 培训场次分页列表
     */
    Page<TrainingSession> findOngoingSessions(Pageable pageable);

    /**
     * 更新培训场次状态
     * 
     * @param id     培训场次ID
     * @param status 新状态
     * @return 更新后的培训场次
     */
    TrainingSession updateStatus(Long id, TrainingSession.Status status);

    /**
     * 批量更新培训场次状态
     * 
     * @param ids    培训场次ID列表
     * @param status 新状态
     * @return 更新数量
     */
    int batchUpdateStatus(List<Long> ids, TrainingSession.Status status);

    /**
     * 搜索培训场次（带分页）
     * 
     * @param keyword  搜索关键词
     * @param pageable 分页参数
     * @return 培训场次分页列表
     */
    Page<TrainingSession> search(String keyword, Pageable pageable);

    /**
     * 获取实际出席人数
     * 
     * @param sessionId 培训场次ID
     * @return 实际出席人数
     */
    int getActualAttendance(Long sessionId);

    /**
     * 获取出席率
     * 
     * @param sessionId 培训场次ID
     * @return 出席率（百分比）
     */
    double getAttendanceRate(Long sessionId);
}

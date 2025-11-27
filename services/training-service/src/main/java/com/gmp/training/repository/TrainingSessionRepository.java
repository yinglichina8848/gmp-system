package com.gmp.training.repository;

import com.gmp.training.entity.TrainingSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 培训场次数据访问层接口
 * 
 * @author GMP系统开发团队
 */
@Repository
public interface TrainingSessionRepository
                extends JpaRepository<TrainingSession, Long>, JpaSpecificationExecutor<TrainingSession> {

        /**
         * 根据课程查找培训场次
         * 
         * @param courseId 课程ID
         * @return 培训场次列表
         */
        List<TrainingSession> findByCourseId(Long courseId);

        /**
         * 根据课程查找培训场次（带分页）
         * 
         * @param courseId 课程ID
         * @param pageable 分页参数
         * @return 培训场次分页列表
         */
        Page<TrainingSession> findByCourseId(Long courseId, Pageable pageable);

        /**
         * 根据讲师查找培训场次
         * 
         * @param lecturerId 讲师ID
         * @return 培训场次列表
         */
        List<TrainingSession> findByLecturerId(Long lecturerId);

        /**
         * 根据讲师查找培训场次（带分页）
         * 
         * @param lecturerId 讲师ID
         * @param pageable   分页参数
         * @return 培训场次分页列表
         */
        Page<TrainingSession> findByLecturerId(Long lecturerId, Pageable pageable);

        /**
         * 根据状态查找培训场次
         * 
         * @param status 培训场次状态
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
         * 根据培训方式查找培训场次
         * 
         * @param trainingMethod 培训方式
         * @return 培训场次列表
         */
        List<TrainingSession> findByTrainingMethod(TrainingSession.TrainingMethod trainingMethod);

        /**
         * 查找特定时间范围内的培训场次
         * 
         * @param startDate 开始日期
         * @param endDate   结束日期
         * @return 培训场次列表
         */
        List<TrainingSession> findByStartTimeBetween(LocalDateTime startDate, LocalDateTime endDate);

        /**
         * 查找即将开始的培训场次（指定时间内）
         * 
         * @param now        当前时间
         * @param futureTime 未来时间
         * @param status     状态
         * @return 培训场次列表
         */
        List<TrainingSession> findByStartTimeBetweenAndStatus(LocalDateTime now, LocalDateTime futureTime,
                        TrainingSession.Status status);

        /**
         * 查找已结束但未归档的培训场次
         * 
         * @param now    当前时间
         * @param status 状态
         * @return 培训场次列表
         */
        List<TrainingSession> findByEndTimeBeforeAndStatus(LocalDateTime now, TrainingSession.Status status);

        /**
         * 查找某个地点的培训场次
         * 
         * @param location 培训地点
         * @return 培训场次列表
         */
        List<TrainingSession> findByLocation(String location);

        /**
         * 查找需要提前提醒的培训场次
         * 
         * @param now             当前时间
         * @param reminderTime    提醒时间
         * @param status          状态
         * @param reminderEnabled 是否启用提醒
         * @return 培训场次列表
         */
        List<TrainingSession> findByStartTimeBetweenAndStatusAndReminderEnabled(LocalDateTime now,
                        LocalDateTime reminderTime, TrainingSession.Status status, boolean reminderEnabled);

        /**
         * 查找GMP相关的培训场次
         * 
         * @param gmpRelated 是否GMP相关
         * @return 培训场次列表
         */
        List<TrainingSession> findByGmpRelated(boolean gmpRelated);

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
}

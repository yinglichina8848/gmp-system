package com.gmp.training.repository;

import com.gmp.training.entity.TrainingCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 培训课程数据访问层接口
 * 
 * @author GMP系统开发团队
 */
@Repository
public interface TrainingCourseRepository
                extends JpaRepository<TrainingCourse, Long>, JpaSpecificationExecutor<TrainingCourse> {

        /**
         * 根据课程编码查找课程
         * 
         * @param code 课程编码
         * @return 课程信息
         */
        Optional<TrainingCourse> findByCode(String code);

        /**
         * 根据课程名称查找课程
         * 
         * @param name 课程名称
         * @return 课程列表
         */
        List<TrainingCourse> findByNameContaining(String name);

        /**
         * 根据培训计划查找课程
         * 
         * @param trainingPlanId 培训计划ID
         * @return 课程列表
         */
        List<TrainingCourse> findByTrainingPlanId(Long trainingPlanId);

        /**
         * 根据状态查找课程
         * 
         * @param status 状态
         * @return 课程列表
         */
        List<TrainingCourse> findByStatus(TrainingCourse.Status status);

        /**
         * 根据课程类型查找课程
         * 
         * @param type 课程类型
         * @return 课程列表
         */
        List<TrainingCourse> findByType(TrainingCourse.Type type);

        /**
         * 根据难度级别查找课程
         * 
         * @param difficulty 难度级别
         * @return 课程列表
         */
        List<TrainingCourse> findByDifficulty(TrainingCourse.Difficulty difficulty);

        /**
         * 查找特定时间范围内创建的课程
         * 
         * @param startDate 开始日期
         * @param endDate   结束日期
         * @return 课程列表
         */
        List<TrainingCourse> findByCreatedTimeBetween(LocalDateTime startDate, LocalDateTime endDate);

        /**
         * 查找讲师的课程
         * 
         * @param lecturerId 讲师ID
         * @return 课程列表
         */
        List<TrainingCourse> findByLecturerId(Long lecturerId);

        /**
         * 查找GMP相关的课程
         * 
         * @param gmpRelated 是否GMP相关
         * @return 课程列表
         */
        List<TrainingCourse> findByGmpRelated(boolean gmpRelated);

        /**
         * 检查课程编码是否存在
         * 
         * @param code 课程编码
         * @return 是否存在
         */
        boolean existsByCode(String code);

        /**
         * 查找活跃且未过期的课程
         * 
         * @param status     状态
         * @param expiryDate 过期日期
         * @return 课程列表
         */
        List<TrainingCourse> findByStatusAndExpiryDateAfterOrExpiryDateIsNull(TrainingCourse.Status status,
                        LocalDateTime expiryDate);

        /**
         * 查找非必修课程
         * 
         * @return 课程列表
         */
        List<TrainingCourse> findByIsCompulsoryFalse();

        /**
         * 查找必修课程
         * 
         * @return 课程列表
         */
        List<TrainingCourse> findByIsCompulsoryTrue();

        /**
         * 查找循环课程
         * 
         * @return 课程列表
         */
        List<TrainingCourse> findByIsRecurringTrue();

        /**
         * 根据创建时间范围查找课程
         * 
         * @param startDate 开始日期
         * @param endDate   结束日期
         * @return 课程列表
         */
        List<TrainingCourse> findByCreatedTimeBetween(LocalDateTime startDate, LocalDateTime endDate);

        /**
         * 根据创建日期范围查找课程
         * 
         * @param startDate 开始日期
         * @param endDate   结束日期
         * @return 课程列表
         */
        List<TrainingCourse> findByCreationDateBetween(java.time.LocalDate startDate, java.time.LocalDate endDate);

        /**
         * 根据课程编码、名称或描述模糊查询
         * 
         * @param code        课程编码
         * @param name        课程名称
         * @param description 课程描述
         * @return 课程列表
         */
        List<TrainingCourse> findByCodeContainingOrNameContainingOrDescriptionContaining(String code, String name,
                        String description);

        /**
         * 根据培训方式查询课程
         * 
         * @param method 培训方式
         * @return 课程列表
         */
        List<TrainingCourse> findByMethod(TrainingCourse.Method method);

        /**
       * 根据课程名称查询课程
       * 
       * @param name 课程名称
       * @return 课程Optional对象
       */
      Optional<TrainingCourse> findByName(String name);

        /**
         * 根据培训计划查询课程
         * 
         * @param trainingPlan 培训计划
         * @return 课程列表
         */
        List<TrainingCourse> findByTrainingPlan(com.gmp.training.entity.TrainingPlan trainingPlan);
}

package com.gmp.hr.repository;

import com.gmp.hr.entity.TrainingRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * 培训记录数据访问接口
 * 
 * @author GMP Team
 * @version 1.0.0
 */
@Repository
public interface TrainingRecordRepository extends JpaRepository<TrainingRecord, Long>, JpaSpecificationExecutor<TrainingRecord> {
    
    /**
     * 根据员工ID查找培训记录列表
     * 
     * @param employeeId 员工ID
     * @param pageable 分页参数
     * @return 分页培训记录列表
     */
    Page<TrainingRecord> findByEmployeeIdAndDeletedFalse(Long employeeId, Pageable pageable);
    
    /**
     * 根据培训类型查找培训记录列表
     * 
     * @param trainingType 培训类型
     * @param pageable 分页参数
     * @return 分页培训记录列表
     */
    Page<TrainingRecord> findByTrainingTypeAndDeletedFalse(String trainingType, Pageable pageable);
    
    /**
     * 根据培训日期范围查找培训记录
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 培训记录列表
     */
    List<TrainingRecord> findByStartDateBetweenAndDeletedFalse(LocalDate startDate, LocalDate endDate);
    
    /**
     * 根据培训结果查找培训记录
     * 
     * @param result 培训结果
     * @return 培训记录列表
     */
    List<TrainingRecord> findByResultAndDeletedFalse(String result);
    
    /**
     * 查找指定员工在指定日期范围内的培训记录
     * 
     * @param employeeId 员工ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 培训记录列表
     */
    List<TrainingRecord> findByEmployeeIdAndStartDateBetweenAndDeletedFalse(Long employeeId, LocalDate startDate, LocalDate endDate);
    
    /**
     * 统计员工的培训时长
     * 
     * @param employeeId 员工ID
     * @return 培训总时长
     */
    @Query("SELECT SUM(tr.durationHours) FROM TrainingRecord tr WHERE tr.employeeId = :employeeId AND tr.deleted = false")
    Integer sumTrainingHoursByEmployeeId(Long employeeId);
    
    /**
     * 软删除培训记录
     * 
     * @param id 培训记录ID
     * @return 更新记录数
     */
    @Modifying
    @Query("UPDATE TrainingRecord tr SET tr.deleted = true WHERE tr.id = :id")
    int softDeleteById(Long id);
}
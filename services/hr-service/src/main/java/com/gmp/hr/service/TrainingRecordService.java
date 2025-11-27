package com.gmp.hr.service;

import com.gmp.hr.dto.TrainingRecordDTO;

import java.util.Date;
import java.util.List;

/**
 * 培训记录服务接口
 * 
 * @author GMP Team
 * @version 1.0.0
 */
public interface TrainingRecordService {
    
    /**
     * 创建培训记录
     * 
     * @param trainingRecordDTO 培训记录DTO
     * @return 创建的培训记录DTO
     */
    TrainingRecordDTO createTrainingRecord(TrainingRecordDTO trainingRecordDTO);
    
    /**
     * 根据ID获取培训记录
     * 
     * @param id 培训记录ID
     * @return 培训记录DTO
     */
    TrainingRecordDTO getTrainingRecordById(Long id);
    
    /**
     * 更新培训记录
     * 
     * @param id 培训记录ID
     * @param trainingRecordDTO 培训记录DTO
     * @return 更新后的培训记录DTO
     */
    TrainingRecordDTO updateTrainingRecord(Long id, TrainingRecordDTO trainingRecordDTO);
    
    /**
     * 删除培训记录
     * 
     * @param id 培训记录ID
     */
    void deleteTrainingRecord(Long id);
    
    /**
     * 获取所有培训记录
     * 
     * @return 培训记录DTO列表
     */
    List<TrainingRecordDTO> getAllTrainingRecords();
    
    /**
     * 根据员工ID获取培训记录列表
     * 
     * @param employeeId 员工ID
     * @return 培训记录DTO列表
     */
    List<TrainingRecordDTO> getTrainingRecordsByEmployee(Long employeeId);
    
    /**
     * 根据培训类型获取培训记录列表
     * 
     * @param trainingType 培训类型
     * @return 培训记录DTO列表
     */
    List<TrainingRecordDTO> getTrainingRecordsByType(String trainingType);
    
    /**
     * 根据日期范围查询培训记录
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 培训记录DTO列表
     */
    List<TrainingRecordDTO> getTrainingRecordsByDateRange(Date startDate, Date endDate);
    
    /**
     * 根据考核结果查询培训记录
     * 
     * @param result 考核结果（如：优秀、良好、合格、不合格）
     * @return 培训记录DTO列表
     */
    List<TrainingRecordDTO> getTrainingRecordsByResult(String result);
    
    /**
     * 根据培训机构查询培训记录
     * 
     * @param trainer 培训机构/人员
     * @return 培训记录DTO列表
     */
    List<TrainingRecordDTO> getTrainingRecordsByTrainer(String trainer);
    
    /**
     * 查询指定部门的培训记录
     * 
     * @param departmentId 部门ID
     * @return 培训记录DTO列表
     */
    List<TrainingRecordDTO> getTrainingRecordsByDepartment(Long departmentId);
    
    /**
     * 查询即将进行的培训
     * 
     * @param daysThreshold 天数阈值
     * @return 培训记录DTO列表
     */
    List<TrainingRecordDTO> getUpcomingTrainings(int daysThreshold);
    
    /**
     * 获取员工年度培训总时长
     * 
     * @param employeeId 员工ID
     * @param year 年份
     * @return 培训总时长（小时）
     */
    int getEmployeeTrainingHoursByYear(Long employeeId, int year);
}
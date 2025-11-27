package com.gmp.hr.controller;

import com.gmp.hr.dto.TrainingRecordDTO;
import com.gmp.hr.service.TrainingRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 培训记录管理控制器
 * 
 * @author GMP Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/hr/training-records")
public class TrainingRecordController {

    @Autowired
    private TrainingRecordService trainingRecordService;
    
    /**
     * 创建培训记录
     * 
     * @param trainingRecordDTO 培训记录DTO
     * @return 创建的培训记录DTO
     */
    @PostMapping
    public ResponseEntity<TrainingRecordDTO> createTrainingRecord(@RequestBody TrainingRecordDTO trainingRecordDTO) {
        TrainingRecordDTO createdRecord = trainingRecordService.createTrainingRecord(trainingRecordDTO);
        return new ResponseEntity<>(createdRecord, HttpStatus.CREATED);
    }
    
    /**
     * 根据ID获取培训记录
     * 
     * @param id 培训记录ID
     * @return 培训记录DTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<TrainingRecordDTO> getTrainingRecordById(@PathVariable Long id) {
        TrainingRecordDTO recordDTO = trainingRecordService.getTrainingRecordById(id);
        return ResponseEntity.ok(recordDTO);
    }
    
    /**
     * 更新培训记录
     * 
     * @param id 培训记录ID
     * @param trainingRecordDTO 培训记录DTO
     * @return 更新后的培训记录DTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<TrainingRecordDTO> updateTrainingRecord(@PathVariable Long id, @RequestBody TrainingRecordDTO trainingRecordDTO) {
        TrainingRecordDTO updatedRecord = trainingRecordService.updateTrainingRecord(id, trainingRecordDTO);
        return ResponseEntity.ok(updatedRecord);
    }
    
    /**
     * 删除培训记录
     * 
     * @param id 培训记录ID
     * @return 响应状态
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrainingRecord(@PathVariable Long id) {
        trainingRecordService.deleteTrainingRecord(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * 获取所有培训记录
     * 
     * @return 培训记录DTO列表
     */
    @GetMapping
    public ResponseEntity<List<TrainingRecordDTO>> getAllTrainingRecords() {
        List<TrainingRecordDTO> records = trainingRecordService.getAllTrainingRecords();
        return ResponseEntity.ok(records);
    }
    
    /**
     * 根据员工ID获取培训记录列表
     * 
     * @param employeeId 员工ID
     * @return 培训记录DTO列表
     */
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<TrainingRecordDTO>> getTrainingRecordsByEmployeeId(@PathVariable Long employeeId) {
        List<TrainingRecordDTO> records = trainingRecordService.getTrainingRecordsByEmployeeId(employeeId);
        return ResponseEntity.ok(records);
    }
    
    /**
     * 根据培训类型获取培训记录列表
     * 
     * @param trainingType 培训类型
     * @return 培训记录DTO列表
     */
    @GetMapping("/type/{trainingType}")
    public ResponseEntity<List<TrainingRecordDTO>> getTrainingRecordsByType(@PathVariable String trainingType) {
        List<TrainingRecordDTO> records = trainingRecordService.getTrainingRecordsByType(trainingType);
        return ResponseEntity.ok(records);
    }
    
    /**
     * 根据日期范围获取培训记录列表
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 培训记录DTO列表
     */
    @GetMapping("/date-range")
    public ResponseEntity<List<TrainingRecordDTO>> getTrainingRecordsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<TrainingRecordDTO> records = trainingRecordService.getTrainingRecordsByDateRange(startDate, endDate);
        return ResponseEntity.ok(records);
    }
    
    /**
     * 根据考核结果获取培训记录列表
     * 
     * @param assessmentResult 考核结果
     * @return 培训记录DTO列表
     */
    @GetMapping("/assessment/{assessmentResult}")
    public ResponseEntity<List<TrainingRecordDTO>> getTrainingRecordsByAssessmentResult(@PathVariable String assessmentResult) {
        List<TrainingRecordDTO> records = trainingRecordService.getTrainingRecordsByAssessmentResult(assessmentResult);
        return ResponseEntity.ok(records);
    }
    
    /**
     * 根据培训机构获取培训记录列表
     * 
     * @param trainingInstitution 培训机构
     * @return 培训记录DTO列表
     */
    @GetMapping("/institution/{trainingInstitution}")
    public ResponseEntity<List<TrainingRecordDTO>> getTrainingRecordsByInstitution(@PathVariable String trainingInstitution) {
        List<TrainingRecordDTO> records = trainingRecordService.getTrainingRecordsByInstitution(trainingInstitution);
        return ResponseEntity.ok(records);
    }
    
    /**
     * 根据部门ID获取培训记录列表
     * 
     * @param departmentId 部门ID
     * @return 培训记录DTO列表
     */
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<TrainingRecordDTO>> getTrainingRecordsByDepartmentId(@PathVariable Long departmentId) {
        List<TrainingRecordDTO> records = trainingRecordService.getTrainingRecordsByDepartmentId(departmentId);
        return ResponseEntity.ok(records);
    }
    
    /**
     * 获取即将进行的培训
     * 
     * @param daysThreshold 天数阈值
     * @return 培训记录DTO列表
     */
    @GetMapping("/upcoming")
    public ResponseEntity<List<TrainingRecordDTO>> getUpcomingTrainings(@RequestParam(required = false, defaultValue = "30") int daysThreshold) {
        List<TrainingRecordDTO> records = trainingRecordService.getUpcomingTrainings(daysThreshold);
        return ResponseEntity.ok(records);
    }
    
    /**
     * 获取员工年度培训总时长
     * 
     * @param employeeId 员工ID
     * @param year 年份
     * @return 培训总时长（小时）
     */
    @GetMapping("/annual-duration")
    public ResponseEntity<Integer> getEmployeeAnnualTrainingDuration(@RequestParam Long employeeId, @RequestParam int year) {
        int duration = trainingRecordService.getEmployeeAnnualTrainingDuration(employeeId, year);
        return ResponseEntity.ok(duration);
    }
}
package com.gmp.hr.service.impl;

import com.gmp.hr.dto.TrainingRecordDTO;
import com.gmp.hr.entity.Employee;
import com.gmp.hr.entity.TrainingRecord;
import com.gmp.hr.repository.DepartmentRepository;
import com.gmp.hr.repository.EmployeeRepository;
import com.gmp.hr.repository.TrainingRecordRepository;
import com.gmp.hr.service.TrainingRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 培训记录服务实现类
 * 
 * @author GMP Team
 * @version 1.0.0
 */
@Service
public class TrainingRecordServiceImpl implements TrainingRecordService {

    @Autowired
    private TrainingRecordRepository trainingRecordRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    @Transactional
    public TrainingRecordDTO createTrainingRecord(TrainingRecordDTO trainingRecordDTO) {
        // 验证员工是否存在
        Employee employee = employeeRepository.findById(trainingRecordDTO.getEmployeeId())
                .orElseThrow(() -> new IllegalArgumentException("员工不存在: " + trainingRecordDTO.getEmployeeId()));

        // 验证培训时间（开始时间不能晚于结束时间）
        if (trainingRecordDTO.getStartTime() != null && trainingRecordDTO.getEndTime() != null &&
                trainingRecordDTO.getStartTime().after(trainingRecordDTO.getEndTime())) {
            throw new IllegalArgumentException("培训开始时间不能晚于结束时间");
        }

        // 创建培训记录实体
        TrainingRecord trainingRecord = new TrainingRecord();
        trainingRecord.setEmployee(employee);
        trainingRecord.setTitle(trainingRecordDTO.getTitle());
        trainingRecord.setTrainingType(trainingRecordDTO.getTrainingType());
        trainingRecord.setTrainer(trainingRecordDTO.getTrainer());
        trainingRecord.setStartTime(trainingRecordDTO.getStartTime());
        trainingRecord.setEndTime(trainingRecordDTO.getEndTime());
        trainingRecord.setLocation(trainingRecordDTO.getLocation());
        trainingRecord.setHours(trainingRecordDTO.getHours());
        trainingRecord.setResult(trainingRecordDTO.getResult());
        trainingRecord.setNotes(trainingRecordDTO.getNotes());
        trainingRecord.setIsActive(true);
        trainingRecord.setCreatedBy(trainingRecordDTO.getCreatedBy());
        trainingRecord.setCreatedAt(new Date());

        // 保存培训记录
        trainingRecord = trainingRecordRepository.save(trainingRecord);

        // 转换为DTO并返回
        return convertToDTO(trainingRecord);
    }

    @Override
    public TrainingRecordDTO getTrainingRecordById(Long id) {
        TrainingRecord trainingRecord = trainingRecordRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new IllegalArgumentException("培训记录不存在: " + id));

        return convertToDTO(trainingRecord);
    }

    @Override
    @Transactional
    public TrainingRecordDTO updateTrainingRecord(Long id, TrainingRecordDTO trainingRecordDTO) {
        // 获取培训记录
        TrainingRecord trainingRecord = trainingRecordRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new IllegalArgumentException("培训记录不存在: " + id));

        // 如果需要修改员工
        if (trainingRecordDTO.getEmployeeId() != null &&
                !trainingRecordDTO.getEmployeeId().equals(trainingRecord.getEmployee().getId())) {
            Employee employee = employeeRepository.findById(trainingRecordDTO.getEmployeeId())
                    .orElseThrow(() -> new IllegalArgumentException("员工不存在: " + trainingRecordDTO.getEmployeeId()));
            trainingRecord.setEmployee(employee);
        }

        // 验证培训时间（如果有更新）
        Date startTime = trainingRecordDTO.getStartTime() != null ? trainingRecordDTO.getStartTime()
                : trainingRecord.getStartTime();
        Date endTime = trainingRecordDTO.getEndTime() != null ? trainingRecordDTO.getEndTime()
                : trainingRecord.getEndTime();
        if (startTime != null && endTime != null && startTime.after(endTime)) {
            throw new IllegalArgumentException("培训开始时间不能晚于结束时间");
        }

        // 更新其他字段
        if (trainingRecordDTO.getTitle() != null) {
            trainingRecord.setTitle(trainingRecordDTO.getTitle());
        }
        if (trainingRecordDTO.getTrainingType() != null) {
            trainingRecord.setTrainingType(trainingRecordDTO.getTrainingType());
        }
        if (trainingRecordDTO.getTrainer() != null) {
            trainingRecord.setTrainer(trainingRecordDTO.getTrainer());
        }
        if (trainingRecordDTO.getStartTime() != null) {
            trainingRecord.setStartTime(trainingRecordDTO.getStartTime());
        }
        if (trainingRecordDTO.getEndTime() != null) {
            trainingRecord.setEndTime(trainingRecordDTO.getEndTime());
        }
        if (trainingRecordDTO.getLocation() != null) {
            trainingRecord.setLocation(trainingRecordDTO.getLocation());
        }
        if (trainingRecordDTO.getHours() != null) {
            trainingRecord.setHours(trainingRecordDTO.getHours());
        }
        if (trainingRecordDTO.getResult() != null) {
            trainingRecord.setResult(trainingRecordDTO.getResult());
        }
        if (trainingRecordDTO.getNotes() != null) {
            trainingRecord.setNotes(trainingRecordDTO.getNotes());
        }

        // 设置更新信息
        trainingRecord.setUpdatedBy(trainingRecordDTO.getUpdatedBy());
        trainingRecord.setUpdatedAt(new Date());

        // 保存更新
        trainingRecord = trainingRecordRepository.save(trainingRecord);

        // 转换为DTO并返回
        return convertToDTO(trainingRecord);
    }

    @Override
    @Transactional
    public void deleteTrainingRecord(Long id) {
        // 获取培训记录
        TrainingRecord trainingRecord = trainingRecordRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new IllegalArgumentException("培训记录不存在: " + id));

        // 软删除
        trainingRecord.setIsActive(false);
        trainingRecord.setUpdatedAt(new Date());
        trainingRecordRepository.save(trainingRecord);
    }

    @Override
    public List<TrainingRecordDTO> getAllTrainingRecords() {
        List<TrainingRecord> trainingRecords = trainingRecordRepository.findAllByIsActiveTrue();
        return trainingRecords.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TrainingRecordDTO> getTrainingRecordsByEmployee(Long employeeId) {
        // 验证员工是否存在
        if (!employeeRepository.existsById(employeeId)) {
            throw new IllegalArgumentException("员工不存在: " + employeeId);
        }

        List<TrainingRecord> trainingRecords = trainingRecordRepository.findByEmployeeIdAndIsActiveTrue(employeeId);
        return trainingRecords.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TrainingRecordDTO> getTrainingRecordsByType(String trainingType) {
        List<TrainingRecord> trainingRecords = trainingRecordRepository.findByTrainingTypeAndIsActiveTrue(trainingType);
        return trainingRecords.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TrainingRecordDTO> getTrainingRecordsByDateRange(Date startDate, Date endDate) {
        List<TrainingRecord> trainingRecords = trainingRecordRepository.findByStartTimeBetweenAndIsActiveTrue(startDate,
                endDate);
        return trainingRecords.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TrainingRecordDTO> getTrainingRecordsByResult(String result) {
        List<TrainingRecord> trainingRecords = trainingRecordRepository.findByResultAndIsActiveTrue(result);
        return trainingRecords.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TrainingRecordDTO> getTrainingRecordsByTrainer(String trainer) {
        List<TrainingRecord> trainingRecords = trainingRecordRepository.findByTrainerContainingAndIsActiveTrue(trainer);
        return trainingRecords.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TrainingRecordDTO> getTrainingRecordsByDepartment(Long departmentId) {
        // 验证部门是否存在
        if (!departmentRepository.existsById(departmentId)) {
            throw new IllegalArgumentException("部门不存在: " + departmentId);
        }

        List<TrainingRecord> trainingRecords = trainingRecordRepository
                .findByEmployeeDepartmentIdAndIsActiveTrue(departmentId);
        return trainingRecords.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TrainingRecordDTO> getUpcomingTrainings(int daysThreshold) {
        // 获取当前日期
        Date today = new Date();

        // 计算阈值日期
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, daysThreshold);
        Date thresholdDate = calendar.getTime();

        // 查询即将进行的培训
        List<TrainingRecord> trainingRecords = trainingRecordRepository.findUpcomingTrainings(today, thresholdDate);
        return trainingRecords.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public int getEmployeeTrainingHoursByYear(Long employeeId, int year) {
        // 验证员工是否存在
        if (!employeeRepository.existsById(employeeId)) {
            throw new IllegalArgumentException("员工不存在: " + employeeId);
        }

        // 计算年份的开始和结束日期
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, Calendar.JANUARY, 1, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date startOfYear = calendar.getTime();

        calendar.set(year, Calendar.DECEMBER, 31, 23, 59, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        Date endOfYear = calendar.getTime();

        // 查询员工当年的培训记录并计算总时长
        List<TrainingRecord> trainingRecords = trainingRecordRepository
                .findByEmployeeIdAndStartTimeBetweenAndIsActiveTrue(
                        employeeId, startOfYear, endOfYear);

        return trainingRecords.stream()
                .mapToInt(record -> record.getHours() != null ? record.getHours() : 0)
                .sum();
    }

    /**
     * 将培训记录实体转换为DTO
     * 
     * @param trainingRecord 培训记录实体
     * @return 培训记录DTO
     */
    private TrainingRecordDTO convertToDTO(TrainingRecord trainingRecord) {
        TrainingRecordDTO dto = new TrainingRecordDTO();
        dto.setId(trainingRecord.getId());
        dto.setEmployeeId(trainingRecord.getEmployee().getId());
        dto.setEmployeeName(trainingRecord.getEmployee().getFullName());
        dto.setTitle(trainingRecord.getTitle());
        dto.setTrainingType(trainingRecord.getTrainingType());
        dto.setTrainer(trainingRecord.getTrainer());
        dto.setStartTime(trainingRecord.getStartTime());
        dto.setEndTime(trainingRecord.getEndTime());
        dto.setLocation(trainingRecord.getLocation());
        dto.setHours(trainingRecord.getHours());
        dto.setResult(trainingRecord.getResult());
        dto.setNotes(trainingRecord.getNotes());
        dto.setCreatedAt(trainingRecord.getCreatedAt());
        dto.setCreatedBy(trainingRecord.getCreatedBy());
        dto.setUpdatedAt(trainingRecord.getUpdatedAt());
        dto.setUpdatedBy(trainingRecord.getUpdatedBy());

        // 部门信息已通过员工关联获取

        return dto;
    }
}
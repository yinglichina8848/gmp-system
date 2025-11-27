package com.gmp.training.service.impl;

import com.gmp.training.entity.TrainingCourse;
import com.gmp.training.entity.TrainingRecord;
import com.gmp.training.entity.TrainingSession;
import com.gmp.training.entity.User;
import com.gmp.training.repository.TrainingRecordRepository;
import com.gmp.training.service.TrainingRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 培训记录服务实现类
 * 
 * @author GMP系统开发团队
 */
@Service
@Transactional(readOnly = true)
public class TrainingRecordServiceImpl extends BaseServiceImpl<TrainingRecord, Long, TrainingRecordRepository> implements TrainingRecordService {

    @Autowired
    public TrainingRecordServiceImpl(TrainingRecordRepository repository) {
        super(repository);
    }

    @Override
    public List<TrainingRecord> findByUser(User user) {
        return repository.findByUser(user);
    }

    @Override
    public List<TrainingRecord> findByTrainingSession(TrainingSession trainingSession) {
        return repository.findByTrainingSession(trainingSession);
    }

    @Override
    public List<TrainingRecord> findByTrainingCourse(TrainingCourse trainingCourse) {
        return repository.findByTrainingCourse(trainingCourse);
    }

    @Override
    public List<TrainingRecord> findByStatus(TrainingRecord.Status status) {
        return repository.findByStatus(status);
    }

    @Override
    public List<TrainingRecord> findByUserAndTrainingCourse(User user, TrainingCourse trainingCourse) {
        return repository.findByUserAndTrainingCourse(user, trainingCourse);
    }

    @Override
    public List<TrainingRecord> findByUserAndStatus(User user, TrainingRecord.Status status) {
        return repository.findByUserAndStatus(user, status);
    }

    @Override
    public List<TrainingRecord> findByTrainingDateBetween(LocalDate startDate, LocalDate endDate) {
        return repository.findByTrainingDateBetween(startDate, endDate);
    }

    @Override
    public List<TrainingRecord> findNotAttendedRecords() {
        return repository.findByStatusNot(TrainingRecord.Status.ATTENDED);
    }

    @Override
    @Transactional
    public TrainingRecord updateStatus(Long id, TrainingRecord.Status status) {
        TrainingRecord record = getById(id);
        record.setStatus(status);
        // 如果标记为已签到，更新签到时间
        if (status == TrainingRecord.Status.ATTENDED) {
            record.setAttendanceTime(LocalDateTime.now());
        }
        return repository.save(record);
    }

    @Override
    @Transactional
    public int batchUpdateStatus(List<Long> ids, TrainingRecord.Status status) {
        List<TrainingRecord> records = repository.findAllById(ids);
        records.forEach(record -> {
            record.setStatus(status);
            // 如果标记为已签到，更新签到时间
            if (status == TrainingRecord.Status.ATTENDED) {
                record.setAttendanceTime(LocalDateTime.now());
            }
        });
        repository.saveAll(records);
        return records.size();
    }

    @Override
    public List<TrainingRecord> search(String keyword) {
        // 这里可以根据实际需要扩展搜索条件
        return repository.findByEvaluationNotesContaining(keyword);
    }

    @Override
    public Double getUserCompletionRate(Long userId) {
        // 获取用户的所有培训记录
        List<TrainingRecord> records = repository.findByUserId(userId);
        if (records.isEmpty()) {
            return 0.0;
        }
        
        // 计算已完成的培训记录数量
        long completedCount = records.stream()
                .filter(record -> record.getStatus() == TrainingRecord.Status.COMPLETED)
                .count();
        
        // 计算完成率
        return (double) completedCount / records.size() * 100;
    }

    @Override
    public Double getCourseCompletionRate(Long courseId) {
        // 获取课程的所有培训记录
        List<TrainingRecord> records = repository.findByTrainingCourseId(courseId);
        if (records.isEmpty()) {
            return 0.0;
        }
        
        // 计算已完成的培训记录数量
        long completedCount = records.stream()
                .filter(record -> record.getStatus() == TrainingRecord.Status.COMPLETED)
                .count();
        
        // 计算完成率
        return (double) completedCount / records.size() * 100;
    }

    @Override
    public List<TrainingRecord> findByDepartmentId(Long departmentId) {
        // 实际实现需要关联用户和部门表
        // 这里提供基础实现，具体需要根据实际的数据库设计调整
        return repository.findByUserDepartmentId(departmentId);
    }

    @Override
    public List<TrainingRecord> findByPositionId(Long positionId) {
        // 实际实现需要关联用户和岗位表
        // 这里提供基础实现，具体需要根据实际的数据库设计调整
        return repository.findByUserPositionId(positionId);
    }

    @Override
    @Transactional
    public TrainingRecord save(TrainingRecord record) {
        // 新增培训记录时设置默认状态
        if (record.getId() == null) {
            record.setStatus(TrainingRecord.Status.PENDING); // 默认状态为待参加
        }
        return super.save(record);
    }

    @Override
    protected String getEntityName() {
        return "TrainingRecord";
    }
}
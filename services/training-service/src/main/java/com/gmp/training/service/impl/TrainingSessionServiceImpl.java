package com.gmp.training.service.impl;

import com.gmp.training.entity.TrainingSession;
import com.gmp.training.repository.TrainingSessionRepository;
import com.gmp.training.service.TrainingSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 培训场次服务实现类
 * 
 * @author GMP系统开发团队
 */
@Service
@Transactional
public class TrainingSessionServiceImpl implements TrainingSessionService {

    @Autowired
    private TrainingSessionRepository repository;

    @Override
    @Transactional(readOnly = true)
    public TrainingSession findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public TrainingSession save(TrainingSession trainingSession) {
        return repository.save(trainingSession);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TrainingSession> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TrainingSession> findByCourseId(Long courseId, Pageable pageable) {
        return repository.findByCourseId(courseId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TrainingSession> findByLecturerId(Long lecturerId, Pageable pageable) {
        return repository.findByLecturerId(lecturerId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainingSession> findByStatus(TrainingSession.Status status) {
        return repository.findByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TrainingSession> findByStatus(TrainingSession.Status status, Pageable pageable) {
        return repository.findByStatus(status, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainingSession> findByStartTimeBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return repository.findByStartTimeBetween(startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TrainingSession> findByMethod(TrainingSession.Method method, Pageable pageable) {
        return repository.findByMethod(method, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TrainingSession> findByVenue(String venue, Pageable pageable) {
        return repository.findByVenue(venue, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TrainingSession> findBySessionDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return repository.findBySessionDateBetween(startDate, endDate, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TrainingSession> findFutureSessions(Pageable pageable) {
        return repository.findFutureSessions(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TrainingSession> findCompletedSessions(Pageable pageable) {
        return repository.findCompletedSessions(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TrainingSession> findOngoingSessions(Pageable pageable) {
        return repository.findOngoingSessions(pageable);
    }

    @Override
    public TrainingSession updateStatus(Long id, TrainingSession.Status status) {
        Optional<TrainingSession> optional = repository.findById(id);
        if (optional.isPresent()) {
            TrainingSession session = optional.get();
            session.setStatus(status);
            return repository.save(session);
        }
        throw new RuntimeException("培训场次不存在：" + id);
    }

    @Override
    public int batchUpdateStatus(List<Long> ids, TrainingSession.Status status) {
        // 简化实现，实际可能需要使用JPQL或SQL批量更新
        int count = 0;
        for (Long id : ids) {
            try {
                updateStatus(id, status);
                count++;
            } catch (Exception e) {
                // 记录错误但继续处理其他记录
                e.printStackTrace();
            }
        }
        return count;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TrainingSession> search(String keyword, Pageable pageable) {
        // 简化实现，实际可能需要更复杂的搜索逻辑
        // 这里假设通过状态、地点或方法进行简单搜索
        // 实际应该使用Specification或自定义查询
        return repository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public int getActualAttendance(Long sessionId) {
        // 简化实现，实际应该查询参训人员记录
        return 0;
    }

    @Override
    @Transactional(readOnly = true)
    public double getAttendanceRate(Long sessionId) {
        // 简化实现，实际应该基于实际出勤人数和计划参训人数计算
        return 0.0;
    }
}

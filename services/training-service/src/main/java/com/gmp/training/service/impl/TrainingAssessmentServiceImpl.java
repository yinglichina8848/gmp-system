package com.gmp.training.service.impl;

import com.gmp.training.entity.TrainingAssessment;
import com.gmp.training.entity.TrainingCourse;
import com.gmp.training.entity.User;
import com.gmp.training.repository.TrainingAssessmentRepository;
import com.gmp.training.service.TrainingAssessmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.OptionalDouble;

/**
 * 培训考核服务实现类
 * 
 * @author GMP系统开发团队
 */
@Service
@Transactional(readOnly = true)
public class TrainingAssessmentServiceImpl extends BaseServiceImpl<TrainingAssessment, Long, TrainingAssessmentRepository> implements TrainingAssessmentService {

    @Autowired
    public TrainingAssessmentServiceImpl(TrainingAssessmentRepository repository) {
        super(repository);
    }

    @Override
    public List<TrainingAssessment> findByTrainingCourse(TrainingCourse trainingCourse) {
        return repository.findByTrainingCourse(trainingCourse);
    }

    @Override
    public List<TrainingAssessment> findByUser(User user) {
        return repository.findByUser(user);
    }

    @Override
    public List<TrainingAssessment> findByStatus(TrainingAssessment.Status status) {
        return repository.findByStatus(status);
    }

    @Override
    public List<TrainingAssessment> findByUserAndTrainingCourse(User user, TrainingCourse trainingCourse) {
        return repository.findByUserAndTrainingCourse(user, trainingCourse);
    }

    @Override
    public List<TrainingAssessment> findByAssessmentDateBetween(LocalDate startDate, LocalDate endDate) {
        return repository.findByAssessmentDateBetween(startDate, endDate);
    }

    @Override
    public List<TrainingAssessment> findNotStartedAssessments() {
        return repository.findByStatus(TrainingAssessment.Status.NOT_STARTED);
    }

    @Override
    public List<TrainingAssessment> findInProgressAssessments() {
        return repository.findByStatus(TrainingAssessment.Status.IN_PROGRESS);
    }

    @Override
    public List<TrainingAssessment> findCompletedAssessments() {
        return repository.findByStatus(TrainingAssessment.Status.COMPLETED);
    }

    @Override
    @Transactional
    public TrainingAssessment updateStatus(Long id, TrainingAssessment.Status status) {
        TrainingAssessment assessment = getById(id);
        assessment.setStatus(status);
        // 如果标记为已完成，更新完成时间
        if (status == TrainingAssessment.Status.COMPLETED) {
            assessment.setCompletionDate(LocalDate.now());
        }
        return repository.save(assessment);
    }

    @Override
    @Transactional
    public int batchUpdateStatus(List<Long> ids, TrainingAssessment.Status status) {
        List<TrainingAssessment> assessments = repository.findAllById(ids);
        assessments.forEach(assessment -> {
            assessment.setStatus(status);
            // 如果标记为已完成，更新完成时间
            if (status == TrainingAssessment.Status.COMPLETED) {
                assessment.setCompletionDate(LocalDate.now());
            }
        });
        repository.saveAll(assessments);
        return assessments.size();
    }

    @Override
    public List<TrainingAssessment> search(String keyword) {
        // 这里可以根据实际需要扩展搜索条件
        return repository.findByNotesContaining(keyword);
    }

    @Override
    public List<TrainingAssessment> findByType(TrainingAssessment.Type type) {
        return repository.findByType(type);
    }

    @Override
    public Double getAverageScoreByCourseId(Long courseId) {
        List<TrainingAssessment> assessments = repository.findByTrainingCourseId(courseId);
        OptionalDouble average = assessments.stream()
                .filter(a -> a.getScore() != null)
                .mapToDouble(TrainingAssessment::getScore)
                .average();
        return average.orElse(0.0);
    }

    @Override
    public Double getAverageScoreByUserId(Long userId) {
        List<TrainingAssessment> assessments = repository.findByUserId(userId);
        OptionalDouble average = assessments.stream()
                .filter(a -> a.getScore() != null)
                .mapToDouble(a -> a.getScore().doubleValue())
                .average();
        return average.orElse(0.0);
    }

    @Override
    public List<TrainingAssessment> findPassedAssessments(Double passScore) {
        return repository.findByScoreGreaterThanEqualAndStatus(passScore, TrainingAssessment.Status.COMPLETED);
    }

    @Override
    @Transactional
    public TrainingAssessment save(TrainingAssessment assessment) {
        // 新增考核时设置默认状态
        if (assessment.getId() == null) {
            assessment.setStatus(TrainingAssessment.Status.NOT_STARTED); // 默认状态为未开始
        }
        return super.save(assessment);
    }

    @Override
    protected String getEntityName() {
        return "TrainingAssessment";
    }
}
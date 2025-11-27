package com.gmp.training.service.impl;

import com.gmp.training.entity.TrainingCourse;
import com.gmp.training.entity.TrainingPlan;
import com.gmp.training.repository.TrainingCourseRepository;
import com.gmp.training.service.TrainingCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 培训课程服务实现类
 * 
 * @author GMP系统开发团队
 */
@Service
@Transactional(readOnly = true)
public class TrainingCourseServiceImpl extends BaseServiceImpl<TrainingCourse, Long, TrainingCourseRepository> implements TrainingCourseService {

    @Autowired
    public TrainingCourseServiceImpl(TrainingCourseRepository repository) {
        super(repository);
    }

    @Override
    public Optional<TrainingCourse> findByCode(String code) {
        return repository.findByCode(code);
    }

    @Override
    public Optional<TrainingCourse> findByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public List<TrainingCourse> findByTrainingPlan(TrainingPlan trainingPlan) {
        return repository.findByTrainingPlan(trainingPlan);
    }

    @Override
    public List<TrainingCourse> findByStatus(TrainingCourse.Status status) {
        return repository.findByStatus(status);
    }

    @Override
    public List<TrainingCourse> findByType(TrainingCourse.Type type) {
        return repository.findByType(type);
    }

    @Override
    public List<TrainingCourse> findByMethod(TrainingCourse.Method method) {
        return repository.findByMethod(method);
    }

    @Override
    public List<TrainingCourse> findRecurringCourses() {
        return repository.findByIsRecurringTrue();
    }

    @Override
    public List<TrainingCourse> findCompulsoryCourses() {
        return repository.findByIsCompulsoryTrue();
    }

    @Override
    public List<TrainingCourse> findOptionalCourses() {
        return repository.findByIsCompulsoryFalse();
    }

    @Override
    public List<TrainingCourse> findByCreationDateBetween(LocalDate startDate, LocalDate endDate) {
        return repository.findByCreationDateBetween(startDate, endDate);
    }

    @Override
    @Transactional
    public TrainingCourse updateStatus(Long id, TrainingCourse.Status status) {
        TrainingCourse course = getById(id);
        course.setStatus(status);
        return repository.save(course);
    }

    @Override
    @Transactional
    public int batchUpdateStatus(List<Long> ids, TrainingCourse.Status status) {
        List<TrainingCourse> courses = repository.findAllById(ids);
        courses.forEach(course -> course.setStatus(status));
        repository.saveAll(courses);
        return courses.size();
    }

    @Override
    public List<TrainingCourse> search(String keyword) {
        return repository.findByCodeContainingOrNameContainingOrDescriptionContaining(
                keyword, keyword, keyword);
    }

    @Override
    @Transactional
    public TrainingCourse save(TrainingCourse course) {
        // 新增培训课程时设置默认状态
        if (course.getId() == null) {
            course.setStatus(TrainingCourse.Status.DRAFT); // 默认状态为草稿
            course.setCreationDate(LocalDate.now());
        }
        return super.save(course);
    }

    @Override
    protected String getEntityName() {
        return "TrainingCourse";
    }
}
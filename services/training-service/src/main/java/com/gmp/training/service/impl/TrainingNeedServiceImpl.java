package com.gmp.training.service.impl;

import com.gmp.training.entity.TrainingNeed;
import com.gmp.training.entity.User;
import com.gmp.training.repository.TrainingNeedRepository;
import com.gmp.training.service.TrainingNeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 培训需求服务实现类
 * 
 * @author GMP系统开发团队
 */
@Service
@Transactional(readOnly = true)
public class TrainingNeedServiceImpl extends BaseServiceImpl<TrainingNeed, Long, TrainingNeedRepository> implements TrainingNeedService {

    @Autowired
    public TrainingNeedServiceImpl(TrainingNeedRepository repository) {
        super(repository);
    }

    @Override
    public Optional<TrainingNeed> findByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public List<TrainingNeed> findByDepartmentId(Long departmentId) {
        return repository.findByDepartmentId(departmentId);
    }

    @Override
    public List<TrainingNeed> findByPositionId(Long positionId) {
        return repository.findByPositionId(positionId);
    }

    @Override
    public List<TrainingNeed> findByStatus(TrainingNeed.Status status) {
        return repository.findByStatus(status);
    }

    @Override
    public List<TrainingNeed> findByCreator(User creator) {
        return repository.findByCreator(creator);
    }

    @Override
    public List<TrainingNeed> findBySubmitTimeBetween(LocalDateTime startTime, LocalDateTime endTime) {
        return repository.findBySubmitTimeBetween(startTime, endTime);
    }

    @Override
    public List<TrainingNeed> findByPriority(TrainingNeed.Priority priority) {
        return repository.findByPriority(priority);
    }

    @Override
    public List<TrainingNeed> findApprovedNeeds() {
        return repository.findByStatus(TrainingNeed.Status.APPROVED);
    }

    @Override
    public List<TrainingNeed> findPendingNeeds() {
        return repository.findByStatus(TrainingNeed.Status.PENDING);
    }

    @Override
    @Transactional
    public TrainingNeed updateStatus(Long id, TrainingNeed.Status status, String remark) {
        TrainingNeed need = getById(id);
        need.setStatus(status);
        if (remark != null) {
            need.setRemark(remark);
        }
        return repository.save(need);
    }

    @Override
    @Transactional
    public int batchUpdateStatus(List<Long> ids, TrainingNeed.Status status, String remark) {
        List<TrainingNeed> needs = repository.findAllById(ids);
        needs.forEach(need -> {
            need.setStatus(status);
            if (remark != null) {
                need.setRemark(remark);
            }
        });
        repository.saveAll(needs);
        return needs.size();
    }

    @Override
    public List<TrainingNeed> search(String keyword) {
        // 实现搜索功能，这里使用Repository提供的方法
        return repository.findByNameContainingOrDescriptionContaining(keyword, keyword);
    }

    @Override
    @Transactional
    public TrainingNeed save(TrainingNeed need) {
        // 新增培训需求时设置提交时间
        if (need.getId() == null) {
            need.setSubmitTime(LocalDateTime.now());
            need.setStatus(TrainingNeed.Status.PENDING); // 默认状态为待审批
        }
        return super.save(need);
    }

    @Override
    protected String getEntityName() {
        return "TrainingNeed";
    }
}

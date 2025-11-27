package com.gmp.training.service.impl;

import com.gmp.training.entity.Position;
import com.gmp.training.exception.EntityNotFoundException;
import com.gmp.training.repository.DepartmentRepository;
import com.gmp.training.repository.PositionRepository;
import com.gmp.training.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 岗位服务实现类
 * 
 * @author GMP系统开发团队
 */
@Service
@Transactional(readOnly = true)
public class PositionServiceImpl extends BaseServiceImpl<Position, Long, PositionRepository> implements PositionService {

    private final DepartmentRepository departmentRepository;

    @Autowired
    public PositionServiceImpl(PositionRepository repository, DepartmentRepository departmentRepository) {
        super(repository);
        this.departmentRepository = departmentRepository;
    }

    @Override
    public Optional<Position> findByCode(String code) {
        return repository.findByCode(code);
    }

    @Override
    public Optional<Position> findByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public boolean existsByCode(String code) {
        return repository.existsByCode(code);
    }

    @Override
    public boolean existsByName(String name) {
        return repository.existsByName(name);
    }

    @Override
    public List<Position> findByDepartmentId(Long departmentId) {
        return repository.findByDepartmentId(departmentId);
    }

    @Override
    public List<Position> findByGmpQualification(boolean gmpQualification) {
        return repository.findByGmpQualification(gmpQualification);
    }

    @Override
    public List<Position> findActivePositions() {
        return repository.findByActiveTrue();
    }

    @Override
    @Transactional
    public Position updateStatus(Long id, boolean active) {
        Position position = getById(id);
        position.setActive(active);
        return repository.save(position);
    }

    @Override
    @Transactional
    public Position updateDepartment(Long id, Long departmentId) {
        Position position = getById(id);
        
        if (departmentId != null) {
            departmentRepository.findById(departmentId)
                    .orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + departmentId));
            
            position.setDepartment(null); // 先清除关联
            repository.save(position); // 保存状态
            // 重新设置关联
            position.setDepartment(departmentRepository.getReferenceById(departmentId));
        } else {
            position.setDepartment(null);
        }
        
        return repository.save(position);
    }

    @Override
    @Transactional
    public int batchDisable(List<Long> ids) {
        // 验证所有ID都存在
        List<Position> positions = repository.findAllById(ids);
        if (positions.size() != ids.size()) {
            throw new EntityNotFoundException("Some positions not found");
        }
        
        // 批量禁用
        positions.forEach(position -> position.setActive(false));
        repository.saveAll(positions);
        return positions.size();
    }

    @Override
    @Transactional
    public int batchEnable(List<Long> ids) {
        // 验证所有ID都存在
        List<Position> positions = repository.findAllById(ids);
        if (positions.size() != ids.size()) {
            throw new EntityNotFoundException("Some positions not found");
        }
        
        // 批量启用
        positions.forEach(position -> position.setActive(true));
        repository.saveAll(positions);
        return positions.size();
    }

    @Override
    public List<Position> findPositionsByRequiredTraining(Long trainingTypeId) {
        // 这里需要根据实际的数据模型实现，暂时返回空列表
        // 实际实现可能需要通过关联表查询需要特定培训的岗位
        return List.of();
    }

    @Override
    @Transactional
    public Position save(Position position) {
        // 检查岗位编码、名称是否已存在
        if (position.getId() == null) {
            // 新增岗位时检查
            if (existsByCode(position.getCode())) {
                throw new IllegalArgumentException("Position code already exists: " + position.getCode());
            }
            if (existsByName(position.getName())) {
                throw new IllegalArgumentException("Position name already exists: " + position.getName());
            }
        } else {
            // 更新岗位时，如果编码或名称有变化则检查
            Position existingPosition = getById(position.getId());
            if (!existingPosition.getCode().equals(position.getCode()) && existsByCode(position.getCode())) {
                throw new IllegalArgumentException("Position code already exists: " + position.getCode());
            }
            if (!existingPosition.getName().equals(position.getName()) && existsByName(position.getName())) {
                throw new IllegalArgumentException("Position name already exists: " + position.getName());
            }
        }
        
        return super.save(position);
    }

    @Override
    protected String getEntityName() {
        return "Position";
    }
}

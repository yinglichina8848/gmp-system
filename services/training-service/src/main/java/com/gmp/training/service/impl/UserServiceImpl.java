package com.gmp.training.service.impl;

import com.gmp.training.entity.User;
import com.gmp.training.exception.EntityNotFoundException;
import com.gmp.training.repository.DepartmentRepository;
import com.gmp.training.repository.PositionRepository;
import com.gmp.training.repository.UserRepository;
import com.gmp.training.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 用户服务实现类
 * 
 * @author GMP系统开发团队
 */
@Service
@Transactional(readOnly = true)
public class UserServiceImpl extends BaseServiceImpl<User, Long, UserRepository> implements UserService {

    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository repository, DepartmentRepository departmentRepository,
                          PositionRepository positionRepository, PasswordEncoder passwordEncoder) {
        super(repository);
        this.departmentRepository = departmentRepository;
        this.positionRepository = positionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public Optional<User> findByEmployeeNumber(String employeeNumber) {
        return repository.findByEmployeeNumber(employeeNumber);
    }

    @Override
    public boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public boolean existsByEmployeeNumber(String employeeNumber) {
        return repository.existsByEmployeeNumber(employeeNumber);
    }

    @Override
    public List<User> findByDepartmentId(Long departmentId) {
        return repository.findByDepartmentId(departmentId);
    }

    @Override
    public List<User> findByGmpPersonnel(boolean gmpPersonnel) {
        return repository.findByGmpPersonnel(gmpPersonnel);
    }

    @Override
    public List<User> findByPositionId(Long positionId) {
        // 由于User实体中没有直接的positionId字段，这里需要自定义查询
        // 假设我们有一个根据positionId查找用户的方法
        // 实际实现可能需要根据具体的关联关系调整
        return repository.findAll().stream()
                .filter(user -> user.getPosition() != null && user.getPosition().getId().equals(positionId))
                .toList();
    }

    @Override
    @Transactional
    public User updateStatus(Long id, User.Status status) {
        User user = getById(id);
        user.setStatus(status);
        return repository.save(user);
    }

    @Override
    @Transactional
    public User resetPassword(Long id, String newPassword) {
        User user = getById(id);
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordReset(true);
        // 重置密码过期标志
        user.setCredentialsNonExpired(true);
        return repository.save(user);
    }

    @Override
    @Transactional
    public User updateDepartment(Long id, Long departmentId) {
        User user = getById(id);
        if (departmentId != null) {
            departmentRepository.findById(departmentId)
                    .orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + departmentId));
            user.setDepartment(null); // 先清除关联
            repository.save(user); // 保存状态
            // 重新设置关联
            user.setDepartment(departmentRepository.getReferenceById(departmentId));
        } else {
            user.setDepartment(null);
        }
        return repository.save(user);
    }

    @Override
    @Transactional
    public User updatePosition(Long id, Long positionId) {
        User user = getById(id);
        if (positionId != null) {
            positionRepository.findById(positionId)
                    .orElseThrow(() -> new EntityNotFoundException("Position not found with id: " + positionId));
            user.setPosition(null); // 先清除关联
            repository.save(user); // 保存状态
            // 重新设置关联
            user.setPosition(positionRepository.getReferenceById(positionId));
        } else {
            user.setPosition(null);
        }
        return repository.save(user);
    }

    @Override
    @Transactional
    public int batchDisable(List<Long> ids) {
        // 验证所有ID都存在
        List<User> users = repository.findAllById(ids);
        if (users.size() != ids.size()) {
            throw new EntityNotFoundException("Some users not found");
        }
        // 批量禁用
        users.forEach(user -> user.setStatus(User.Status.INACTIVE));
        repository.saveAll(users);
        return users.size();
    }

    @Override
    @Transactional
    public int batchEnable(List<Long> ids) {
        // 验证所有ID都存在
        List<User> users = repository.findAllById(ids);
        if (users.size() != ids.size()) {
            throw new EntityNotFoundException("Some users not found");
        }
        // 批量启用
        users.forEach(user -> user.setStatus(User.Status.ACTIVE));
        repository.saveAll(users);
        return users.size();
    }

    @Override
    @Transactional
    public User save(User user) {
        // 检查用户名、邮箱、员工编号是否已存在
        if (user.getId() == null) {
            // 新增用户时检查
            if (existsByUsername(user.getUsername())) {
                throw new IllegalArgumentException("Username already exists: " + user.getUsername());
            }
            if (existsByEmail(user.getEmail())) {
                throw new IllegalArgumentException("Email already exists: " + user.getEmail());
            }
            if (existsByEmployeeNumber(user.getEmployeeNumber())) {
                throw new IllegalArgumentException("Employee number already exists: " + user.getEmployeeNumber());
            }
            // 加密密码
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            // 更新用户时，如果密码被修改则加密
            User existingUser = getById(user.getId());
            if (!existingUser.getPassword().equals(user.getPassword())) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
        }
        return super.save(user);
    }

    @Override
    protected String getEntityName() {
        return "User";
    }
}

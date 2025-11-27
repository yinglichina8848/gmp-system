package com.gmp.hr.service.impl;

import com.gmp.hr.dto.EmployeeDTO;
import com.gmp.hr.entity.Employee;
import com.gmp.hr.entity.Department;
import com.gmp.hr.entity.Position;
import com.gmp.hr.mapper.EmployeeMapper;
import com.gmp.hr.repository.EmployeeRepository;
import com.gmp.hr.repository.DepartmentRepository;
import com.gmp.hr.repository.PositionRepository;
import com.gmp.hr.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 员工服务实现类
 * 
 * @author GMP Team
 * @version 1.0.0
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {
    
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final EmployeeMapper employeeMapper;
    
    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                             DepartmentRepository departmentRepository,
                             PositionRepository positionRepository,
                             EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.positionRepository = positionRepository;
        this.employeeMapper = employeeMapper;
    }
    
    @Override
    @Transactional
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        // 验证工号唯一性
        Optional<Employee> existingEmployee = employeeRepository.findByEmployeeCodeAndDeletedFalse(employeeDTO.getEmployeeCode());
        if (existingEmployee.isPresent()) {
            throw new RuntimeException("员工工号已存在");
        }
        
        // 验证邮箱唯一性
        Optional<Employee> existingEmail = employeeRepository.findByEmailAndDeletedFalse(employeeDTO.getEmail());
        if (existingEmail.isPresent()) {
            throw new RuntimeException("邮箱已被使用");
        }
        
        // 验证身份证号唯一性
        Optional<Employee> existingIdCard = employeeRepository.findByIdCardAndDeletedFalse(employeeDTO.getIdCard());
        if (existingIdCard.isPresent()) {
            throw new RuntimeException("身份证号已被使用");
        }
        
        // 创建实体
        Employee employee = employeeMapper.toEntity(employeeDTO);
        
        // 设置关联关系
        if (employeeDTO.getDepartmentId() != null) {
            Department department = departmentRepository.findById(employeeDTO.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("部门不存在"));
            employee.setDepartment(department);
        }
        
        if (employeeDTO.getPositionId() != null) {
            Position position = positionRepository.findById(employeeDTO.getPositionId())
                    .orElseThrow(() -> new RuntimeException("职位不存在"));
            employee.setPosition(position);
        }
        
        // 设置默认值
        employee.setDeleted(false);
        
        // 保存员工
        Employee savedEmployee = employeeRepository.save(employee);
        
        return employeeMapper.toDTO(savedEmployee);
    }
    
    @Override
    @Transactional(readOnly = true)
    public EmployeeDTO getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("员工不存在"));
        
        if (Boolean.TRUE.equals(employee.getDeleted())) {
            throw new RuntimeException("员工不存在");
        }
        
        return employeeMapper.toDTO(employee);
    }
    
    @Override
    @Transactional(readOnly = true)
    public EmployeeDTO getEmployeeByCode(String employeeCode) {
        Employee employee = employeeRepository.findByEmployeeCodeAndDeletedFalse(employeeCode)
                .orElseThrow(() -> new RuntimeException("员工不存在"));
        
        return employeeMapper.toDTO(employee);
    }
    
    @Override
    @Transactional
    public EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("员工不存在"));
        
        if (Boolean.TRUE.equals(employee.getDeleted())) {
            throw new RuntimeException("员工不存在");
        }
        
        // 验证工号唯一性（如果有修改）
        if (!employee.getEmployeeCode().equals(employeeDTO.getEmployeeCode())) {
            Optional<Employee> existingEmployee = employeeRepository.findByEmployeeCodeAndDeletedFalse(employeeDTO.getEmployeeCode());
            if (existingEmployee.isPresent()) {
                throw new RuntimeException("员工工号已存在");
            }
        }
        
        // 验证邮箱唯一性（如果有修改）
        if (!employee.getEmail().equals(employeeDTO.getEmail())) {
            Optional<Employee> existingEmail = employeeRepository.findByEmailAndDeletedFalse(employeeDTO.getEmail());
            if (existingEmail.isPresent()) {
                throw new RuntimeException("邮箱已被使用");
            }
        }
        
        // 验证身份证号唯一性（如果有修改）
        if (!employee.getIdCard().equals(employeeDTO.getIdCard())) {
            Optional<Employee> existingIdCard = employeeRepository.findByIdCardAndDeletedFalse(employeeDTO.getIdCard());
            if (existingIdCard.isPresent()) {
                throw new RuntimeException("身份证号已被使用");
            }
        }
        
        // 更新基本信息
        employeeMapper.updateEntityFromDTO(employeeDTO, employee);
        
        // 更新关联关系
        if (employeeDTO.getDepartmentId() != null) {
            Department department = departmentRepository.findById(employeeDTO.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("部门不存在"));
            employee.setDepartment(department);
        }
        
        if (employeeDTO.getPositionId() != null) {
            Position position = positionRepository.findById(employeeDTO.getPositionId())
                    .orElseThrow(() -> new RuntimeException("职位不存在"));
            employee.setPosition(position);
        }
        
        // 保存更新
        Employee updatedEmployee = employeeRepository.save(employee);
        
        return employeeMapper.toDTO(updatedEmployee);
    }
    
    @Override
    @Transactional
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("员工不存在"));
        
        if (Boolean.TRUE.equals(employee.getDeleted())) {
            throw new RuntimeException("员工不存在");
        }
        
        // 软删除
        employee.setDeleted(true);
        employeeRepository.save(employee);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeDTO> getEmployees(Pageable pageable) {
        Page<Employee> employees = employeeRepository.findAll(pageable);
        return employees.map(employeeMapper::toDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDTO> getEmployeesByDepartment(Long departmentId) {
        List<Employee> employees = employeeRepository.findByDepartmentIdAndDeletedFalse(departmentId);
        return employeeMapper.toDTOList(employees);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDTO> getEmployeesByPosition(Long positionId) {
        List<Employee> employees = employeeRepository.findByPositionIdAndDeletedFalse(positionId);
        return employeeMapper.toDTOList(employees);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDTO> getEmployeesByHireDateRange(LocalDate startDate, LocalDate endDate) {
        List<Employee> employees = employeeRepository.findByHireDateBetweenAndDeletedFalse(startDate, endDate);
        return employeeMapper.toDTOList(employees);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDTO> getEmployeesWithUpcomingDeparture() {
        // 查询30天内即将离职的员工
        LocalDate thirtyDaysLater = LocalDate.now().plusDays(30);
        List<Employee> employees = employeeRepository.findEmployeesWithUpcomingDeparture(thirtyDaysLater);
        return employeeMapper.toDTOList(employees);
    }
}
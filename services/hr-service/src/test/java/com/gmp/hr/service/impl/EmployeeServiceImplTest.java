package com.gmp.hr.service.impl;

import com.gmp.hr.dto.EmployeeDTO;
import com.gmp.hr.entity.Employee;
import com.gmp.hr.exception.BusinessException;
import com.gmp.hr.repository.EmployeeRepository;
import com.gmp.hr.repository.DepartmentRepository;
import com.gmp.hr.repository.PositionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * 员工服务实现类单元测试
 * 
 * @author GMP Team
 * @version 1.0.0
 */
public class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;
    
    @Mock
    private DepartmentRepository departmentRepository;
    
    @Mock
    private PositionRepository positionRepository;
    
    @InjectMocks
    private EmployeeServiceImpl employeeService;
    
    private Employee employee;
    private EmployeeDTO employeeDTO;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // 初始化测试数据
        employee = new Employee();
        employee.setId(1L);
        employee.setEmployeeCode("EMP001");
        employee.setName("张三");
        employee.setGender("男");
        employee.setBirthDate(LocalDate.of(1990, 1, 1));
        employee.setHireDate(LocalDate.of(2020, 1, 1));
        employee.setStatus("在职");
        
        employeeDTO = new EmployeeDTO();
        employeeDTO.setId(1L);
        employeeDTO.setEmployeeCode("EMP001");
        employeeDTO.setName("张三");
        employeeDTO.setGender("男");
        employeeDTO.setBirthDate(LocalDate.of(1990, 1, 1));
        employeeDTO.setHireDate(LocalDate.of(2020, 1, 1));
        employeeDTO.setStatus("在职");
    }
    
    @Test
    void testCreateEmployee_Success() {
        // 准备
        when(employeeRepository.existsByEmployeeCode("EMP001")).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        when(departmentRepository.existsById(anyLong())).thenReturn(true);
        when(positionRepository.existsById(anyLong())).thenReturn(true);
        
        // 执行
        EmployeeDTO result = employeeService.createEmployee(employeeDTO);
        
        // 验证
        assertNotNull(result);
        assertEquals("EMP001", result.getEmployeeCode());
        assertEquals("张三", result.getName());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }
    
    @Test
    void testCreateEmployee_EmployeeCodeExists() {
        // 准备
        when(employeeRepository.existsByEmployeeCode("EMP001")).thenReturn(true);
        
        // 执行和验证
        BusinessException exception = assertThrows(BusinessException.class, 
                () -> employeeService.createEmployee(employeeDTO));
        assertEquals("员工代码 EMP001 已存在", exception.getMessage());
        verify(employeeRepository, never()).save(any(Employee.class));
    }
    
    @Test
    void testGetEmployeeById_Success() {
        // 准备
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        
        // 执行
        EmployeeDTO result = employeeService.getEmployeeById(1L);
        
        // 验证
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("EMP001", result.getEmployeeCode());
    }
    
    @Test
    void testGetEmployeeById_NotFound() {
        // 准备
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());
        
        // 执行和验证
        BusinessException exception = assertThrows(BusinessException.class, 
                () -> employeeService.getEmployeeById(1L));
        assertEquals("员工不存在，ID: 1", exception.getMessage());
    }
    
    @Test
    void testGetEmployeeByCode_Success() {
        // 准备
        when(employeeRepository.findByEmployeeCode("EMP001")).thenReturn(Optional.of(employee));
        
        // 执行
        EmployeeDTO result = employeeService.getEmployeeByCode("EMP001");
        
        // 验证
        assertNotNull(result);
        assertEquals("EMP001", result.getEmployeeCode());
    }
    
    @Test
    void testUpdateEmployee_Success() {
        // 准备
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.existsByEmployeeCodeAndIdNot("EMP001", 1L)).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        when(departmentRepository.existsById(anyLong())).thenReturn(true);
        when(positionRepository.existsById(anyLong())).thenReturn(true);
        
        // 修改DTO
        employeeDTO.setName("李四");
        
        // 执行
        EmployeeDTO result = employeeService.updateEmployee(1L, employeeDTO);
        
        // 验证
        assertNotNull(result);
        assertEquals("李四", result.getName());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }
    
    @Test
    void testDeleteEmployee_Success() {
        // 准备
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        doNothing().when(employeeRepository).deleteById(1L);
        
        // 执行
        employeeService.deleteEmployee(1L);
        
        // 验证
        verify(employeeRepository, times(1)).deleteById(1L);
    }
    
    @Test
    void testGetEmployeesByDepartment_Success() {
        // 准备
        List<Employee> employees = Collections.singletonList(employee);
        when(employeeRepository.findByDepartmentId(1L)).thenReturn(employees);
        
        // 执行
        List<EmployeeDTO> result = employeeService.getEmployeesByDepartment(1L);
        
        // 验证
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("EMP001", result.get(0).getEmployeeCode());
    }
    
    @Test
    void testGetEmployeesByPosition_Success() {
        // 准备
        List<Employee> employees = Collections.singletonList(employee);
        when(employeeRepository.findByPositionId(1L)).thenReturn(employees);
        
        // 执行
        List<EmployeeDTO> result = employeeService.getEmployeesByPosition(1L);
        
        // 验证
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("EMP001", result.get(0).getEmployeeCode());
    }
    
    @Test
    void testGetEmployeesByDateRange_Success() {
        // 准备
        LocalDate startDate = LocalDate.of(2019, 1, 1);
        LocalDate endDate = LocalDate.of(2021, 1, 1);
        List<Employee> employees = Collections.singletonList(employee);
        when(employeeRepository.findByHireDateBetween(startDate, endDate)).thenReturn(employees);
        
        // 执行
        List<EmployeeDTO> result = employeeService.getEmployeesByDateRange(startDate, endDate);
        
        // 验证
        assertNotNull(result);
        assertEquals(1, result.size());
    }
    
    @Test
    void testGetAllEmployees_Success() {
        // 准备
        List<Employee> employees = Collections.singletonList(employee);
        when(employeeRepository.findAll()).thenReturn(employees);
        
        // 执行
        List<EmployeeDTO> result = employeeService.getAllEmployees();
        
        // 验证
        assertNotNull(result);
        assertEquals(1, result.size());
    }
    
    @Test
    void testGetEmployeesByStatus_Success() {
        // 准备
        List<Employee> employees = Collections.singletonList(employee);
        when(employeeRepository.findByStatus("在职")).thenReturn(employees);
        
        // 执行
        List<EmployeeDTO> result = employeeService.getEmployeesByStatus("在职");
        
        // 验证
        assertNotNull(result);
        assertEquals(1, result.size());
    }
    
    @Test
    void testGetEmployeesLeavingSoon_Success() {
        // 准备
        LocalDate now = LocalDate.now();
        LocalDate twoMonthsLater = now.plusMonths(2);
        employee.setContractEndDate(twoMonthsLater);
        List<Employee> employees = Collections.singletonList(employee);
        when(employeeRepository.findEmployeesLeavingSoon(now)).thenReturn(employees);
        
        // 执行
        List<EmployeeDTO> result = employeeService.getEmployeesLeavingSoon();
        
        // 验证
        assertNotNull(result);
        assertEquals(1, result.size());
    }
    
    @Test
    void testFindEmployees_Success() {
        // 准备
        Pageable pageable = PageRequest.of(0, 10);
        List<Employee> employees = Collections.singletonList(employee);
        Page<Employee> employeePage = new PageImpl<>(employees, pageable, employees.size());
        
        when(employeeRepository.findEmployees(anyString(), anyString(), anyLong(), anyLong(), anyString(), pageable))
            .thenReturn(employeePage);
        
        // 执行
        Page<EmployeeDTO> result = employeeService.findEmployees("张", "男", 1L, 1L, "在职", pageable);
        
        // 验证
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }
}
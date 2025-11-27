package com.gmp.hr.service.impl;

import com.gmp.hr.dto.EmployeeDTO;
import com.gmp.hr.entity.Employee;
import com.gmp.hr.repository.EmployeeRepository;
import com.gmp.hr.repository.DepartmentRepository;
import com.gmp.hr.repository.PositionRepository;
import com.gmp.hr.mapper.EmployeeMapper;
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

    @Mock
    private EmployeeMapper employeeMapper;

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
        employee.setDeleted(false);
        employee.setEmail("zhangsan@example.com");
        employee.setIdCard("110101199001011234");

        employeeDTO = new EmployeeDTO();
        employeeDTO.setId(1L);
        employeeDTO.setEmployeeCode("EMP001");
        employeeDTO.setName("张三");
        employeeDTO.setGender("男");
        employeeDTO.setBirthDate(LocalDate.of(1990, 1, 1));
        employeeDTO.setHireDate(LocalDate.of(2020, 1, 1));
        employeeDTO.setStatus("在职");
        employeeDTO.setEmail("zhangsan@example.com");
        employeeDTO.setIdCard("110101199001011234");
    }

    @Test
    void testCreateEmployee_Success() {
        // 准备
        when(employeeRepository.findByEmployeeCodeAndDeletedFalse("EMP001")).thenReturn(Optional.empty());
        when(employeeRepository.findByEmailAndDeletedFalse("zhangsan@example.com")).thenReturn(Optional.empty());
        when(employeeRepository.findByIdCardAndDeletedFalse("110101199001011234")).thenReturn(Optional.empty());
        when(employeeMapper.toEntity(employeeDTO)).thenReturn(employee);
        when(employeeRepository.save(employee)).thenReturn(employee);
        when(employeeMapper.toDTO(employee)).thenReturn(employeeDTO);

        // 执行
        EmployeeDTO result = employeeService.createEmployee(employeeDTO);

        // 验证
        assertNotNull(result);
        assertEquals("EMP001", result.getEmployeeCode());
        assertEquals("张三", result.getName());
        verify(employeeRepository, times(1)).save(employee);
        verify(employeeMapper, times(1)).toEntity(employeeDTO);
        verify(employeeMapper, times(1)).toDTO(employee);
    }

    @Test
    void testCreateEmployee_EmployeeCodeExists() {
        // 准备
        when(employeeRepository.findByEmployeeCodeAndDeletedFalse("EMP001")).thenReturn(Optional.of(employee));

        // 执行和验证
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> employeeService.createEmployee(employeeDTO));
        assertEquals("员工工号已存在", exception.getMessage());
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void testGetEmployeeById_Success() {
        // 准备
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeMapper.toDTO(employee)).thenReturn(employeeDTO);

        // 执行
        EmployeeDTO result = employeeService.getEmployeeById(1L);

        // 验证
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("EMP001", result.getEmployeeCode());
        verify(employeeMapper, times(1)).toDTO(employee);
    }

    @Test
    void testGetEmployeeById_NotFound() {
        // 准备
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        // 执行和验证
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> employeeService.getEmployeeById(1L));
        assertEquals("员工不存在", exception.getMessage());
    }

    @Test
    void testGetEmployeeByCode_Success() {
        // 准备
        when(employeeRepository.findByEmployeeCodeAndDeletedFalse("EMP001")).thenReturn(Optional.of(employee));
        when(employeeMapper.toDTO(employee)).thenReturn(employeeDTO);

        // 执行
        EmployeeDTO result = employeeService.getEmployeeByCode("EMP001");

        // 验证
        assertNotNull(result);
        assertEquals("EMP001", result.getEmployeeCode());
        verify(employeeMapper, times(1)).toDTO(employee);
    }

    @Test
    void testUpdateEmployee_Success() {
        // 准备
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeMapper.updateEntityFromDTO(employeeDTO, employee)).thenReturn(employee);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        when(employeeMapper.toDTO(employee)).thenReturn(employeeDTO);

        // 修改DTO
        employeeDTO.setName("李四");

        // 执行
        EmployeeDTO result = employeeService.updateEmployee(1L, employeeDTO);

        // 验证
        assertNotNull(result);
        verify(employeeMapper, times(1)).updateEntityFromDTO(employeeDTO, employee);
        verify(employeeRepository, times(1)).save(any(Employee.class));
        verify(employeeMapper, times(1)).toDTO(employee);
    }

    @Test
    void testDeleteEmployee_Success() {
        // 准备
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        // 执行
        employeeService.deleteEmployee(1L);

        // 验证
        assertTrue(employee.getDeleted());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void testGetEmployeesByDepartment_Success() {
        // 准备
        List<Employee> employees = Collections.singletonList(employee);
        when(employeeRepository.findByDepartmentIdAndDeletedFalse(1L)).thenReturn(employees);
        when(employeeMapper.toDTOList(employees)).thenReturn(Collections.singletonList(employeeDTO));

        // 执行
        List<EmployeeDTO> result = employeeService.getEmployeesByDepartment(1L);

        // 验证
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("EMP001", result.get(0).getEmployeeCode());
        verify(employeeRepository, times(1)).findByDepartmentIdAndDeletedFalse(1L);
        verify(employeeMapper, times(1)).toDTOList(employees);
    }

    @Test
    void testGetEmployeesByPosition_Success() {
        // 准备
        List<Employee> employees = Collections.singletonList(employee);
        when(employeeRepository.findByPositionIdAndDeletedFalse(1L)).thenReturn(employees);
        when(employeeMapper.toDTOList(employees)).thenReturn(Collections.singletonList(employeeDTO));

        // 执行
        List<EmployeeDTO> result = employeeService.getEmployeesByPosition(1L);

        // 验证
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("EMP001", result.get(0).getEmployeeCode());
        verify(employeeRepository, times(1)).findByPositionIdAndDeletedFalse(1L);
        verify(employeeMapper, times(1)).toDTOList(employees);
    }

    @Test
    void testGetEmployeesByHireDateRange_Success() {
        // 准备
        LocalDate startDate = LocalDate.of(2019, 1, 1);
        LocalDate endDate = LocalDate.of(2021, 1, 1);
        List<Employee> employees = Collections.singletonList(employee);
        when(employeeRepository.findByHireDateBetweenAndDeletedFalse(startDate, endDate)).thenReturn(employees);
        when(employeeMapper.toDTOList(employees)).thenReturn(Collections.singletonList(employeeDTO));

        // 执行
        List<EmployeeDTO> result = employeeService.getEmployeesByHireDateRange(startDate, endDate);

        // 验证
        assertNotNull(result);
        verify(employeeRepository, times(1)).findByHireDateBetweenAndDeletedFalse(startDate, endDate);
        verify(employeeMapper, times(1)).toDTOList(employees);
    }

    @Test
    void testGetEmployeesWithUpcomingDeparture_Success() {
        // 准备
        List<Employee> employees = Collections.singletonList(employee);
        LocalDate thirtyDaysLater = LocalDate.now().plusDays(30);
        when(employeeRepository.findEmployeesWithUpcomingDeparture(thirtyDaysLater)).thenReturn(employees);
        when(employeeMapper.toDTOList(employees)).thenReturn(Collections.singletonList(employeeDTO));

        // 执行
        List<EmployeeDTO> result = employeeService.getEmployeesWithUpcomingDeparture();

        // 验证
        assertNotNull(result);
        verify(employeeRepository, times(1)).findEmployeesWithUpcomingDeparture(thirtyDaysLater);
        verify(employeeMapper, times(1)).toDTOList(employees);
    }
}
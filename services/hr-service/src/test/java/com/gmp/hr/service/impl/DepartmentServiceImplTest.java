package com.gmp.hr.service.impl;

import com.gmp.hr.dto.DepartmentDTO;
import com.gmp.hr.entity.Department;
import com.gmp.hr.exception.BusinessException;
import com.gmp.hr.repository.DepartmentRepository;
import com.gmp.hr.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * 部门服务实现类单元测试
 * 
 * @author GMP Team
 * @version 1.0.0
 */
public class DepartmentServiceImplTest {

    @Mock
    private DepartmentRepository departmentRepository;
    
    @Mock
    private EmployeeRepository employeeRepository;
    
    @InjectMocks
    private DepartmentServiceImpl departmentService;
    
    private Department department;
    private DepartmentDTO departmentDTO;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // 初始化测试数据
        department = new Department();
        department.setId(1L);
        department.setCode("DEP001");
        department.setName("技术部");
        department.setDescription("负责公司技术研发");
        department.setStatus("正常");
        
        departmentDTO = new DepartmentDTO();
        departmentDTO.setId(1L);
        departmentDTO.setCode("DEP001");
        departmentDTO.setName("技术部");
        departmentDTO.setDescription("负责公司技术研发");
        departmentDTO.setStatus("正常");
    }
    
    @Test
    void testCreateDepartment_Success() {
        // 准备
        when(departmentRepository.existsByCode("DEP001")).thenReturn(false);
        when(departmentRepository.save(any(Department.class))).thenReturn(department);
        
        // 执行
        DepartmentDTO result = departmentService.createDepartment(departmentDTO);
        
        // 验证
        assertNotNull(result);
        assertEquals("DEP001", result.getCode());
        assertEquals("技术部", result.getName());
        verify(departmentRepository, times(1)).save(any(Department.class));
    }
    
    @Test
    void testCreateDepartment_CodeExists() {
        // 准备
        when(departmentRepository.existsByCode("DEP001")).thenReturn(true);
        
        // 执行和验证
        BusinessException exception = assertThrows(BusinessException.class, 
                () -> departmentService.createDepartment(departmentDTO));
        assertEquals("部门代码 DEP001 已存在", exception.getMessage());
        verify(departmentRepository, never()).save(any(Department.class));
    }
    
    @Test
    void testGetDepartmentById_Success() {
        // 准备
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        
        // 执行
        DepartmentDTO result = departmentService.getDepartmentById(1L);
        
        // 验证
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("DEP001", result.getCode());
    }
    
    @Test
    void testGetDepartmentById_NotFound() {
        // 准备
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());
        
        // 执行和验证
        BusinessException exception = assertThrows(BusinessException.class, 
                () -> departmentService.getDepartmentById(1L));
        assertEquals("部门不存在，ID: 1", exception.getMessage());
    }
    
    @Test
    void testGetDepartmentByCode_Success() {
        // 准备
        when(departmentRepository.findByCode("DEP001")).thenReturn(Optional.of(department));
        
        // 执行
        DepartmentDTO result = departmentService.getDepartmentByCode("DEP001");
        
        // 验证
        assertNotNull(result);
        assertEquals("DEP001", result.getCode());
    }
    
    @Test
    void testUpdateDepartment_Success() {
        // 准备
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(departmentRepository.existsByCodeAndIdNot("DEP001", 1L)).thenReturn(false);
        when(departmentRepository.save(any(Department.class))).thenReturn(department);
        
        // 修改DTO
        departmentDTO.setName("研发部");
        
        // 执行
        DepartmentDTO result = departmentService.updateDepartment(1L, departmentDTO);
        
        // 验证
        assertNotNull(result);
        assertEquals("研发部", result.getName());
        verify(departmentRepository, times(1)).save(any(Department.class));
    }
    
    @Test
    void testDeleteDepartment_Success() {
        // 准备
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(employeeRepository.existsByDepartmentId(1L)).thenReturn(false);
        when(departmentRepository.existsByParentId(1L)).thenReturn(false);
        doNothing().when(departmentRepository).deleteById(1L);
        
        // 执行
        departmentService.deleteDepartment(1L);
        
        // 验证
        verify(departmentRepository, times(1)).deleteById(1L);
    }
    
    @Test
    void testDeleteDepartment_HasEmployees() {
        // 准备
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(employeeRepository.existsByDepartmentId(1L)).thenReturn(true);
        
        // 执行和验证
        BusinessException exception = assertThrows(BusinessException.class, 
                () -> departmentService.deleteDepartment(1L));
        assertEquals("部门下存在员工，无法删除", exception.getMessage());
        verify(departmentRepository, never()).deleteById(1L);
    }
    
    @Test
    void testDeleteDepartment_HasSubDepartments() {
        // 准备
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(employeeRepository.existsByDepartmentId(1L)).thenReturn(false);
        when(departmentRepository.existsByParentId(1L)).thenReturn(true);
        
        // 执行和验证
        BusinessException exception = assertThrows(BusinessException.class, 
                () -> departmentService.deleteDepartment(1L));
        assertEquals("部门下存在子部门，无法删除", exception.getMessage());
        verify(departmentRepository, never()).deleteById(1L);
    }
    
    @Test
    void testGetAllDepartments_Success() {
        // 准备
        List<Department> departments = Collections.singletonList(department);
        when(departmentRepository.findAll()).thenReturn(departments);
        
        // 执行
        List<DepartmentDTO> result = departmentService.getAllDepartments();
        
        // 验证
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("DEP001", result.get(0).getCode());
    }
    
    @Test
    void testGetSubDepartments_Success() {
        // 准备
        Department subDepartment = new Department();
        subDepartment.setId(2L);
        subDepartment.setCode("DEP002");
        subDepartment.setName("前端组");
        subDepartment.setParentId(1L);
        
        List<Department> subDepartments = Collections.singletonList(subDepartment);
        when(departmentRepository.findByParentId(1L)).thenReturn(subDepartments);
        
        // 执行
        List<DepartmentDTO> result = departmentService.getSubDepartments(1L);
        
        // 验证
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("DEP002", result.get(0).getCode());
    }
    
    @Test
    void testGetTopDepartments_Success() {
        // 准备
        List<Department> topDepartments = Collections.singletonList(department);
        when(departmentRepository.findByParentIdIsNull()).thenReturn(topDepartments);
        
        // 执行
        List<DepartmentDTO> result = departmentService.getTopDepartments();
        
        // 验证
        assertNotNull(result);
        assertEquals(1, result.size());
    }
    
    @Test
    void testGetDepartmentByManagerId_Success() {
        // 准备
        department.setManagerId(100L);
        when(departmentRepository.findByManagerId(100L)).thenReturn(Optional.of(department));
        
        // 执行
        DepartmentDTO result = departmentService.getDepartmentByManagerId(100L);
        
        // 验证
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }
    
    @Test
    void testBuildDepartmentTree_Success() {
        // 准备顶级部门
        Department topDept = new Department();
        topDept.setId(1L);
        topDept.setCode("DEP001");
        topDept.setName("技术部");
        
        // 准备子部门
        Department subDept = new Department();
        subDept.setId(2L);
        subDept.setCode("DEP002");
        subDept.setName("前端组");
        subDept.setParentId(1L);
        
        List<Department> departments = Arrays.asList(topDept, subDept);
        when(departmentRepository.findAll()).thenReturn(departments);
        
        // 执行
        List<DepartmentDTO> result = departmentService.getDepartmentTree();
        
        // 验证
        assertNotNull(result);
        assertEquals(1, result.size()); // 顶级部门只有一个
        assertEquals(1, result.get(0).getChildren().size()); // 有一个子部门
    }
}
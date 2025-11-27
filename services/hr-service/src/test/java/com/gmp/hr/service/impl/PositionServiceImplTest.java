package com.gmp.hr.service.impl;

import com.gmp.hr.dto.PositionDTO;
import com.gmp.hr.entity.Position;
import com.gmp.hr.entity.Department;
import com.gmp.hr.entity.Employee;
import com.gmp.hr.repository.PositionRepository;
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
 * 职位服务实现类单元测试
 * 
 * @author GMP Team
 * @version 1.0.0
 */
public class PositionServiceImplTest {

    @Mock
    private PositionRepository positionRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private PositionServiceImpl positionService;

    private Position position;
    private PositionDTO positionDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // 初始化测试数据
        position = new Position();
        position.setId(1L);
        position.setPositionCode("POS001");
        position.setPositionName("高级开发工程师");
        position.setLevel("P5");
        position.setDeleted(false);

        Department department = new Department();
        department.setId(1L);
        department.setDeleted(false);
        position.setDepartment(department);

        positionDTO = new PositionDTO();
        positionDTO.setId(1L);
        positionDTO.setPositionCode("POS001");
        positionDTO.setPositionName("高级开发工程师");
        positionDTO.setLevel("P5");
        positionDTO.setDepartmentId(1L);
    }

    @Test
    void testCreatePosition_Success() {
        // 准备
        when(positionRepository.findByPositionCodeAndDeletedFalse("POS001")).thenReturn(Optional.empty());
        Department department = new Department();
        department.setId(1L);
        department.setDeleted(false);
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(positionRepository.save(any(Position.class))).thenReturn(position);

        // 执行
        PositionDTO result = positionService.createPosition(positionDTO);

        // 验证
        assertNotNull(result);
        assertEquals("POS001", result.getPositionCode());
        assertEquals("高级开发工程师", result.getPositionName());
        verify(positionRepository, times(1)).save(any(Position.class));
    }

    @Test
    void testCreatePosition_CodeExists() {
        // 准备
        when(positionRepository.findByPositionCodeAndDeletedFalse("POS001")).thenReturn(Optional.of(position));

        // 执行和验证
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> positionService.createPosition(positionDTO));
        assertEquals("职位代码已存在", exception.getMessage());
        verify(positionRepository, never()).save(any(Position.class));
    }

    @Test
    void testCreatePosition_DepartmentNotExists() {
        // 准备
        when(positionRepository.findByPositionCodeAndDeletedFalse("POS001")).thenReturn(Optional.empty());
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());

        // 执行和验证
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> positionService.createPosition(positionDTO));
        assertEquals("部门不存在", exception.getMessage());
        verify(positionRepository, never()).save(any(Position.class));
    }

    @Test
    void testGetPositionById_Success() {
        // 准备
        when(positionRepository.findById(1L)).thenReturn(Optional.of(position));

        // 执行
        PositionDTO result = positionService.getPositionById(1L);

        // 验证
        assertNotNull(result);
        assertEquals("POS001", result.getPositionCode());
        assertEquals("高级开发工程师", result.getPositionName());
    }

    @Test
    void testGetPositionById_NotFound() {
        // 准备
        when(positionRepository.findById(1L)).thenReturn(Optional.empty());

        // 执行和验证
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> positionService.getPositionById(1L));
        assertEquals("职位不存在: 1", exception.getMessage());
    }

    @Test
    void testGetPositionByCode_Success() {
        // 准备
        when(positionRepository.findByPositionCodeAndDeletedFalse("POS001")).thenReturn(Optional.of(position));

        // 执行
        PositionDTO result = positionService.getPositionByCode("POS001");

        // 验证
        assertNotNull(result);
        assertEquals("POS001", result.getPositionCode());
    }

    @Test
    void testUpdatePosition_Success() {
        // 准备
        when(positionRepository.findById(1L)).thenReturn(Optional.of(position));
        Department department = new Department();
        department.setId(1L);
        department.setDeleted(false);
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(positionRepository.save(any(Position.class))).thenReturn(position);

        // 修改DTO
        positionDTO.setPositionName("资深开发工程师");

        // 执行
        PositionDTO result = positionService.updatePosition(1L, positionDTO);

        // 验证
        assertNotNull(result);
        verify(positionRepository, times(1)).save(any(Position.class));
    }

    @Test
    void testDeletePosition_Success() {
        // 准备
        when(positionRepository.findById(1L)).thenReturn(Optional.of(position));
        when(employeeRepository.findByPositionIdAndDeletedFalse(1L)).thenReturn(Collections.emptyList());
        when(positionRepository.save(any(Position.class))).thenReturn(position);

        // 执行
        positionService.deletePosition(1L);

        // 验证
        assertTrue(position.getDeleted());
        verify(positionRepository, times(1)).save(any(Position.class));
    }

    @Test
    void testDeletePosition_HasEmployees() {
        // 准备
        when(positionRepository.findById(1L)).thenReturn(Optional.of(position));
        when(employeeRepository.findByPositionIdAndDeletedFalse(1L))
                .thenReturn(Collections.singletonList(new Employee()));

        // 执行和验证
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> positionService.deletePosition(1L));
        assertEquals("该职位下还有员工，无法删除", exception.getMessage());
    }

    @Test
    void testGetAllPositions_Success() {
        // 准备
        List<Position> positions = Collections.singletonList(position);
        when(positionRepository.findAll()).thenReturn(positions);

        // 执行
        List<PositionDTO> result = positionService.getAllPositions();

        // 验证
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("POS001", result.get(0).getPositionCode());
    }

    @Test
    void testGetPositionsByDepartment_Success() {
        // 准备
        List<Position> positions = Collections.singletonList(position);
        when(positionRepository.findByDepartmentIdAndDeletedFalse(1L)).thenReturn(positions);

        // 执行
        List<PositionDTO> result = positionService.getPositionsByDepartment(1L);

        // 验证
        assertNotNull(result);
        assertEquals(1, result.size());
    }
}
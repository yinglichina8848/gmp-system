package com.gmp.hr.service.impl;

import com.gmp.hr.dto.PositionDTO;
import com.gmp.hr.entity.Position;
import com.gmp.hr.exception.BusinessException;
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
        position.setCode("POS001");
        position.setName("高级开发工程师");
        position.setLevel("P5");
        position.setStatus("启用");
        position.setDepartmentId(1L);
        
        positionDTO = new PositionDTO();
        positionDTO.setId(1L);
        positionDTO.setCode("POS001");
        positionDTO.setName("高级开发工程师");
        positionDTO.setLevel("P5");
        positionDTO.setStatus("启用");
        positionDTO.setDepartmentId(1L);
    }
    
    @Test
    void testCreatePosition_Success() {
        // 准备
        when(positionRepository.existsByCode("POS001")).thenReturn(false);
        when(departmentRepository.existsById(1L)).thenReturn(true);
        when(positionRepository.save(any(Position.class))).thenReturn(position);
        
        // 执行
        PositionDTO result = positionService.createPosition(positionDTO);
        
        // 验证
        assertNotNull(result);
        assertEquals("POS001", result.getCode());
        assertEquals("高级开发工程师", result.getName());
        verify(positionRepository, times(1)).save(any(Position.class));
    }
    
    @Test
    void testCreatePosition_CodeExists() {
        // 准备
        when(positionRepository.existsByCode("POS001")).thenReturn(true);
        
        // 执行和验证
        BusinessException exception = assertThrows(BusinessException.class, 
                () -> positionService.createPosition(positionDTO));
        assertEquals("职位代码 POS001 已存在", exception.getMessage());
        verify(positionRepository, never()).save(any(Position.class));
    }
    
    @Test
    void testCreatePosition_DepartmentNotExists() {
        // 准备
        when(positionRepository.existsByCode("POS001")).thenReturn(false);
        when(departmentRepository.existsById(1L)).thenReturn(false);
        
        // 执行和验证
        BusinessException exception = assertThrows(BusinessException.class, 
                () -> positionService.createPosition(positionDTO));
        assertEquals("关联的部门不存在，ID: 1", exception.getMessage());
        verify(positionRepository, never()).save(any(Position.class));
    }
    
    @Test
    void testGetPositionById_Success() {
        // 准备
        when(positionRepository.findById(1L)).thenReturn(Optional.of(position));
        when(employeeRepository.countByPositionId(1L)).thenReturn(5L);
        
        // 执行
        PositionDTO result = positionService.getPositionById(1L);
        
        // 验证
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("POS001", result.getCode());
        assertEquals(5L, result.getEmployeeCount());
    }
    
    @Test
    void testGetPositionById_NotFound() {
        // 准备
        when(positionRepository.findById(1L)).thenReturn(Optional.empty());
        
        // 执行和验证
        BusinessException exception = assertThrows(BusinessException.class, 
                () -> positionService.getPositionById(1L));
        assertEquals("职位不存在，ID: 1", exception.getMessage());
    }
    
    @Test
    void testGetPositionByCode_Success() {
        // 准备
        when(positionRepository.findByCode("POS001")).thenReturn(Optional.of(position));
        when(employeeRepository.countByPositionId(1L)).thenReturn(5L);
        
        // 执行
        PositionDTO result = positionService.getPositionByCode("POS001");
        
        // 验证
        assertNotNull(result);
        assertEquals("POS001", result.getCode());
    }
    
    @Test
    void testUpdatePosition_Success() {
        // 准备
        when(positionRepository.findById(1L)).thenReturn(Optional.of(position));
        when(positionRepository.existsByCodeAndIdNot("POS001", 1L)).thenReturn(false);
        when(departmentRepository.existsById(1L)).thenReturn(true);
        when(positionRepository.save(any(Position.class))).thenReturn(position);
        
        // 修改DTO
        positionDTO.setName("资深开发工程师");
        
        // 执行
        PositionDTO result = positionService.updatePosition(1L, positionDTO);
        
        // 验证
        assertNotNull(result);
        assertEquals("资深开发工程师", result.getName());
        verify(positionRepository, times(1)).save(any(Position.class));
    }
    
    @Test
    void testDeletePosition_Success() {
        // 准备
        when(positionRepository.findById(1L)).thenReturn(Optional.of(position));
        when(employeeRepository.existsByPositionId(1L)).thenReturn(false);
        doNothing().when(positionRepository).deleteById(1L);
        
        // 执行
        positionService.deletePosition(1L);
        
        // 验证
        verify(positionRepository, times(1)).deleteById(1L);
    }
    
    @Test
    void testDeletePosition_HasEmployees() {
        // 准备
        when(positionRepository.findById(1L)).thenReturn(Optional.of(position));
        when(employeeRepository.existsByPositionId(1L)).thenReturn(true);
        
        // 执行和验证
        BusinessException exception = assertThrows(BusinessException.class, 
                () -> positionService.deletePosition(1L));
        assertEquals("职位下存在员工，无法删除", exception.getMessage());
        verify(positionRepository, never()).deleteById(1L);
    }
    
    @Test
    void testGetAllPositions_Success() {
        // 准备
        List<Position> positions = Collections.singletonList(position);
        when(positionRepository.findAll()).thenReturn(positions);
        when(employeeRepository.countByPositionId(1L)).thenReturn(5L);
        
        // 执行
        List<PositionDTO> result = positionService.getAllPositions();
        
        // 验证
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("POS001", result.get(0).getCode());
    }
    
    @Test
    void testGetPositionsByDepartment_Success() {
        // 准备
        List<Position> positions = Collections.singletonList(position);
        when(positionRepository.findByDepartmentId(1L)).thenReturn(positions);
        when(employeeRepository.countByPositionId(1L)).thenReturn(5L);
        
        // 执行
        List<PositionDTO> result = positionService.getPositionsByDepartment(1L);
        
        // 验证
        assertNotNull(result);
        assertEquals(1, result.size());
    }
    
    @Test
    void testGetPositionsByLevel_Success() {
        // 准备
        List<Position> positions = Collections.singletonList(position);
        when(positionRepository.findByLevel("P5")).thenReturn(positions);
        when(employeeRepository.countByPositionId(1L)).thenReturn(5L);
        
        // 执行
        List<PositionDTO> result = positionService.getPositionsByLevel("P5");
        
        // 验证
        assertNotNull(result);
        assertEquals(1, result.size());
    }
    
    @Test
    void testGetPositionsByStatus_Success() {
        // 准备
        List<Position> positions = Collections.singletonList(position);
        when(positionRepository.findByStatus("启用")).thenReturn(positions);
        when(employeeRepository.countByPositionId(1L)).thenReturn(5L);
        
        // 执行
        List<PositionDTO> result = positionService.getPositionsByStatus("启用");
        
        // 验证
        assertNotNull(result);
        assertEquals(1, result.size());
    }
}
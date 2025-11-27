package com.gmp.equipment.service;

import com.gmp.equipment.dto.EquipmentDTO;
import com.gmp.equipment.dto.PageResultDTO;
import com.gmp.equipment.exception.EquipmentServiceException;
import com.gmp.equipment.mapper.EquipmentMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * EquipmentService的单元测试类
 */
class EquipmentServiceTest {

    @Mock
    private EquipmentMapper equipmentMapper;

    @InjectMocks
    private EquipmentServiceImpl equipmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getById_success() {
        // 准备数据
        Long id = 1L;
        EquipmentDTO equipmentDTO = new EquipmentDTO();
        equipmentDTO.setId(id);
        equipmentDTO.setCode("EQ001");
        equipmentDTO.setName("测试设备");
        
        // 模拟mapper行为
        when(equipmentMapper.selectById(id)).thenReturn(equipmentDTO);
        
        // 调用服务层方法
        EquipmentDTO result = equipmentService.getById(id);
        
        // 验证结果
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("EQ001", result.getCode());
        verify(equipmentMapper, times(1)).selectById(id);
    }

    @Test
    void getById_notFound() {
        // 准备数据
        Long id = 999L;
        
        // 模拟mapper行为 - 返回null表示未找到
        when(equipmentMapper.selectById(id)).thenReturn(null);
        
        // 调用服务层方法并验证异常
        EquipmentServiceException exception = assertThrows(
            EquipmentServiceException.class, 
            () -> equipmentService.getById(id)
        );
        
        // 验证异常信息
        assertTrue(exception.getMessage().contains("设备不存在"));
        verify(equipmentMapper, times(1)).selectById(id);
    }

    @Test
    void getByCode_success() {
        // 准备数据
        String code = "EQ001";
        EquipmentDTO equipmentDTO = new EquipmentDTO();
        equipmentDTO.setId(1L);
        equipmentDTO.setCode(code);
        
        // 模拟mapper行为
        when(equipmentMapper.selectByCode(code)).thenReturn(equipmentDTO);
        
        // 调用服务层方法
        EquipmentDTO result = equipmentService.getByCode(code);
        
        // 验证结果
        assertNotNull(result);
        assertEquals(code, result.getCode());
        verify(equipmentMapper, times(1)).selectByCode(code);
    }

    @Test
    void listPage_success() {
        // 准备数据
        String code = "EQ";
        String name = "测试";
        Long typeId = 1L;
        Integer status = 1;
        Integer pageNum = 1;
        Integer pageSize = 10;
        
        // 模拟总数
        int total = 2;
        when(equipmentMapper.countByConditions(code, name, typeId, status)).thenReturn(total);
        
        // 模拟记录列表
        List<EquipmentDTO> equipmentList = new ArrayList<>();
        EquipmentDTO equipment1 = new EquipmentDTO();
        equipment1.setId(1L);
        equipment1.setCode("EQ001");
        EquipmentDTO equipment2 = new EquipmentDTO();
        equipment2.setId(2L);
        equipment2.setCode("EQ002");
        equipmentList.add(equipment1);
        equipmentList.add(equipment2);
        
        int offset = (pageNum - 1) * pageSize;
        when(equipmentMapper.selectPage(code, name, typeId, status, offset, pageSize)).thenReturn(equipmentList);
        
        // 调用服务层方法
        PageResultDTO<EquipmentDTO> result = equipmentService.listPage(code, name, typeId, status, pageNum, pageSize);
        
        // 验证结果
        assertNotNull(result);
        assertEquals(total, result.getTotal());
        assertEquals(2, result.getRecords().size());
        verify(equipmentMapper, times(1)).countByConditions(code, name, typeId, status);
        verify(equipmentMapper, times(1)).selectPage(code, name, typeId, status, offset, pageSize);
    }

    @Test
    void listByTypeId_success() {
        // 准备数据
        Long typeId = 1L;
        List<EquipmentDTO> equipmentList = new ArrayList<>();
        EquipmentDTO equipment1 = new EquipmentDTO();
        equipment1.setId(1L);
        equipment1.setTypeId(typeId);
        equipmentList.add(equipment1);
        
        // 模拟mapper行为
        when(equipmentMapper.selectByTypeId(typeId)).thenReturn(equipmentList);
        
        // 调用服务层方法
        List<EquipmentDTO> result = equipmentService.listByTypeId(typeId);
        
        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(typeId, result.get(0).getTypeId());
        verify(equipmentMapper, times(1)).selectByTypeId(typeId);
    }

    @Test
    void create_success() {
        // 准备数据
        EquipmentDTO equipmentDTO = new EquipmentDTO();
        equipmentDTO.setCode("EQ001");
        equipmentDTO.setName("测试设备");
        equipmentDTO.setTypeId(1L);
        equipmentDTO.setStatus(1);
        
        // 模拟代码不重复
        when(equipmentMapper.existsByCode(equipmentDTO.getCode(), null)).thenReturn(false);
        // 模拟插入操作
        when(equipmentMapper.insert(equipmentDTO)).thenReturn(1);
        // 模拟返回插入后的对象（包含自增ID）
        EquipmentDTO savedDTO = new EquipmentDTO(equipmentDTO);
        savedDTO.setId(1L);
        when(equipmentMapper.selectById(savedDTO.getId())).thenReturn(savedDTO);
        
        // 调用服务层方法
        EquipmentDTO result = equipmentService.create(equipmentDTO);
        
        // 验证结果
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("EQ001", result.getCode());
        verify(equipmentMapper, times(1)).existsByCode(equipmentDTO.getCode(), null);
        verify(equipmentMapper, times(1)).insert(equipmentDTO);
        verify(equipmentMapper, times(1)).selectById(anyLong());
    }

    @Test
    void create_codeExists() {
        // 准备数据
        EquipmentDTO equipmentDTO = new EquipmentDTO();
        equipmentDTO.setCode("EQ001");
        
        // 模拟代码已存在
        when(equipmentMapper.existsByCode(equipmentDTO.getCode(), null)).thenReturn(true);
        
        // 调用服务层方法并验证异常
        EquipmentServiceException exception = assertThrows(
            EquipmentServiceException.class, 
            () -> equipmentService.create(equipmentDTO)
        );
        
        // 验证异常信息
        assertTrue(exception.getMessage().contains("设备编号已存在"));
        verify(equipmentMapper, times(1)).existsByCode(equipmentDTO.getCode(), null);
        // 不应该调用insert方法
        verify(equipmentMapper, never()).insert(any());
    }

    @Test
    void update_success() {
        // 准备数据
        Long id = 1L;
        EquipmentDTO equipmentDTO = new EquipmentDTO();
        equipmentDTO.setCode("EQ001-UPDATED");
        equipmentDTO.setName("更新后的设备");
        
        // 模拟设备存在
        EquipmentDTO existingDTO = new EquipmentDTO();
        existingDTO.setId(id);
        existingDTO.setCode("EQ001");
        when(equipmentMapper.selectById(id)).thenReturn(existingDTO);
        
        // 模拟代码不重复
        when(equipmentMapper.existsByCode(equipmentDTO.getCode(), id)).thenReturn(false);
        
        // 模拟更新操作
        when(equipmentMapper.update(id, equipmentDTO)).thenReturn(1);
        
        // 模拟返回更新后的对象
        EquipmentDTO updatedDTO = new EquipmentDTO(existingDTO);
        updatedDTO.setCode(equipmentDTO.getCode());
        updatedDTO.setName(equipmentDTO.getName());
        when(equipmentMapper.selectById(id)).thenReturn(updatedDTO);
        
        // 调用服务层方法
        EquipmentDTO result = equipmentService.update(id, equipmentDTO);
        
        // 验证结果
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("EQ001-UPDATED", result.getCode());
        verify(equipmentMapper, times(1)).selectById(id);
        verify(equipmentMapper, times(1)).existsByCode(equipmentDTO.getCode(), id);
        verify(equipmentMapper, times(1)).update(id, equipmentDTO);
    }

    @Test
    void delete_success() {
        // 准备数据
        Long id = 1L;
        
        // 模拟设备存在
        EquipmentDTO equipmentDTO = new EquipmentDTO();
        equipmentDTO.setId(id);
        when(equipmentMapper.selectById(id)).thenReturn(equipmentDTO);
        
        // 模拟删除操作
        when(equipmentMapper.delete(id)).thenReturn(1);
        
        // 调用服务层方法
        equipmentService.delete(id);
        
        // 验证结果
        verify(equipmentMapper, times(1)).selectById(id);
        verify(equipmentMapper, times(1)).delete(id);
    }

    @Test
    void deleteBatch_success() {
        // 准备数据
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);
        
        // 模拟删除操作
        when(equipmentMapper.deleteBatch(ids)).thenReturn(2);
        
        // 调用服务层方法
        equipmentService.deleteBatch(ids);
        
        // 验证结果
        verify(equipmentMapper, times(1)).deleteBatch(ids);
    }
}
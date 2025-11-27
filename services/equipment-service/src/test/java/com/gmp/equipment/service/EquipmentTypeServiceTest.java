package com.gmp.equipment.service;

import com.gmp.equipment.dto.EquipmentTypeDTO;
import com.gmp.equipment.dto.PageResultDTO;
import com.gmp.equipment.exception.EquipmentServiceException;
import com.gmp.equipment.mapper.EquipmentTypeMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * EquipmentTypeService的单元测试类
 */
class EquipmentTypeServiceTest {

    @Mock
    private EquipmentTypeMapper equipmentTypeMapper;

    @InjectMocks
    private EquipmentTypeServiceImpl equipmentTypeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getById_success() {
        // 准备数据
        Long id = 1L;
        EquipmentTypeDTO typeDTO = new EquipmentTypeDTO();
        typeDTO.setId(id);
        typeDTO.setCode("TYPE001");
        typeDTO.setName("测试设备类型");
        
        // 模拟mapper行为
        when(equipmentTypeMapper.selectById(id)).thenReturn(typeDTO);
        
        // 调用服务层方法
        EquipmentTypeDTO result = equipmentTypeService.getById(id);
        
        // 验证结果
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("TYPE001", result.getCode());
        verify(equipmentTypeMapper, times(1)).selectById(id);
    }

    @Test
    void getById_notFound() {
        // 准备数据
        Long id = 999L;
        
        // 模拟mapper行为 - 返回null表示未找到
        when(equipmentTypeMapper.selectById(id)).thenReturn(null);
        
        // 调用服务层方法并验证异常
        EquipmentServiceException exception = assertThrows(
            EquipmentServiceException.class, 
            () -> equipmentTypeService.getById(id)
        );
        
        // 验证异常信息
        assertTrue(exception.getMessage().contains("设备类型不存在"));
        verify(equipmentTypeMapper, times(1)).selectById(id);
    }

    @Test
    void getByCode_success() {
        // 准备数据
        String code = "TYPE001";
        EquipmentTypeDTO typeDTO = new EquipmentTypeDTO();
        typeDTO.setId(1L);
        typeDTO.setCode(code);
        
        // 模拟mapper行为
        when(equipmentTypeMapper.selectByCode(code)).thenReturn(typeDTO);
        
        // 调用服务层方法
        EquipmentTypeDTO result = equipmentTypeService.getByCode(code);
        
        // 验证结果
        assertNotNull(result);
        assertEquals(code, result.getCode());
        verify(equipmentTypeMapper, times(1)).selectByCode(code);
    }

    @Test
    void listPage_success() {
        // 准备数据
        String code = "TYPE";
        String name = "测试";
        Integer status = 1;
        Integer pageNum = 1;
        Integer pageSize = 10;
        
        // 模拟总数
        int total = 2;
        when(equipmentTypeMapper.countByConditions(code, name, status)).thenReturn(total);
        
        // 模拟记录列表
        List<EquipmentTypeDTO> typeList = new ArrayList<>();
        EquipmentTypeDTO type1 = new EquipmentTypeDTO();
        type1.setId(1L);
        type1.setCode("TYPE001");
        EquipmentTypeDTO type2 = new EquipmentTypeDTO();
        type2.setId(2L);
        type2.setCode("TYPE002");
        typeList.add(type1);
        typeList.add(type2);
        
        int offset = (pageNum - 1) * pageSize;
        when(equipmentTypeMapper.selectPage(code, name, status, offset, pageSize)).thenReturn(typeList);
        
        // 调用服务层方法
        PageResultDTO<EquipmentTypeDTO> result = equipmentTypeService.listPage(code, name, status, pageNum, pageSize);
        
        // 验证结果
        assertNotNull(result);
        assertEquals(total, result.getTotal());
        assertEquals(2, result.getRecords().size());
        verify(equipmentTypeMapper, times(1)).countByConditions(code, name, status);
        verify(equipmentTypeMapper, times(1)).selectPage(code, name, status, offset, pageSize);
    }

    @Test
    void listAll_success() {
        // 准备数据
        List<EquipmentTypeDTO> typeList = new ArrayList<>();
        EquipmentTypeDTO type1 = new EquipmentTypeDTO();
        type1.setId(1L);
        type1.setCode("TYPE001");
        EquipmentTypeDTO type2 = new EquipmentTypeDTO();
        type2.setId(2L);
        type2.setCode("TYPE002");
        typeList.add(type1);
        typeList.add(type2);
        
        // 模拟mapper行为
        when(equipmentTypeMapper.selectAll()).thenReturn(typeList);
        
        // 调用服务层方法
        List<EquipmentTypeDTO> result = equipmentTypeService.listAll();
        
        // 验证结果
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(equipmentTypeMapper, times(1)).selectAll();
    }

    @Test
    void create_success() {
        // 准备数据
        EquipmentTypeDTO typeDTO = new EquipmentTypeDTO();
        typeDTO.setCode("TYPE001");
        typeDTO.setName("测试设备类型");
        typeDTO.setStatus(1);
        
        // 模拟代码不重复
        when(equipmentTypeMapper.existsByCode(typeDTO.getCode(), null)).thenReturn(false);
        // 模拟插入操作
        when(equipmentTypeMapper.insert(typeDTO)).thenReturn(1);
        // 模拟返回插入后的对象（包含自增ID）
        EquipmentTypeDTO savedDTO = new EquipmentTypeDTO(typeDTO);
        savedDTO.setId(1L);
        when(equipmentTypeMapper.selectById(savedDTO.getId())).thenReturn(savedDTO);
        
        // 调用服务层方法
        EquipmentTypeDTO result = equipmentTypeService.create(typeDTO);
        
        // 验证结果
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("TYPE001", result.getCode());
        verify(equipmentTypeMapper, times(1)).existsByCode(typeDTO.getCode(), null);
        verify(equipmentTypeMapper, times(1)).insert(typeDTO);
        verify(equipmentTypeMapper, times(1)).selectById(anyLong());
    }

    @Test
    void create_codeExists() {
        // 准备数据
        EquipmentTypeDTO typeDTO = new EquipmentTypeDTO();
        typeDTO.setCode("TYPE001");
        
        // 模拟代码已存在
        when(equipmentTypeMapper.existsByCode(typeDTO.getCode(), null)).thenReturn(true);
        
        // 调用服务层方法并验证异常
        EquipmentServiceException exception = assertThrows(
            EquipmentServiceException.class, 
            () -> equipmentTypeService.create(typeDTO)
        );
        
        // 验证异常信息
        assertTrue(exception.getMessage().contains("设备类型编号已存在"));
        verify(equipmentTypeMapper, times(1)).existsByCode(typeDTO.getCode(), null);
        // 不应该调用insert方法
        verify(equipmentTypeMapper, never()).insert(any());
    }

    @Test
    void update_success() {
        // 准备数据
        Long id = 1L;
        EquipmentTypeDTO typeDTO = new EquipmentTypeDTO();
        typeDTO.setCode("TYPE001-UPDATED");
        typeDTO.setName("更新后的设备类型");
        
        // 模拟设备类型存在
        EquipmentTypeDTO existingDTO = new EquipmentTypeDTO();
        existingDTO.setId(id);
        existingDTO.setCode("TYPE001");
        when(equipmentTypeMapper.selectById(id)).thenReturn(existingDTO);
        
        // 模拟代码不重复
        when(equipmentTypeMapper.existsByCode(typeDTO.getCode(), id)).thenReturn(false);
        
        // 模拟更新操作
        when(equipmentTypeMapper.update(id, typeDTO)).thenReturn(1);
        
        // 模拟返回更新后的对象
        EquipmentTypeDTO updatedDTO = new EquipmentTypeDTO(existingDTO);
        updatedDTO.setCode(typeDTO.getCode());
        updatedDTO.setName(typeDTO.getName());
        when(equipmentTypeMapper.selectById(id)).thenReturn(updatedDTO);
        
        // 调用服务层方法
        EquipmentTypeDTO result = equipmentTypeService.update(id, typeDTO);
        
        // 验证结果
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("TYPE001-UPDATED", result.getCode());
        verify(equipmentTypeMapper, times(1)).selectById(id);
        verify(equipmentTypeMapper, times(1)).existsByCode(typeDTO.getCode(), id);
        verify(equipmentTypeMapper, times(1)).update(id, typeDTO);
    }

    @Test
    void delete_success() {
        // 准备数据
        Long id = 1L;
        
        // 模拟设备类型存在
        EquipmentTypeDTO typeDTO = new EquipmentTypeDTO();
        typeDTO.setId(id);
        when(equipmentTypeMapper.selectById(id)).thenReturn(typeDTO);
        
        // 模拟没有关联的设备
        when(equipmentTypeMapper.countEquipmentByTypeId(id)).thenReturn(0);
        
        // 模拟删除操作
        when(equipmentTypeMapper.delete(id)).thenReturn(1);
        
        // 调用服务层方法
        equipmentTypeService.delete(id);
        
        // 验证结果
        verify(equipmentTypeMapper, times(1)).selectById(id);
        verify(equipmentTypeMapper, times(1)).countEquipmentByTypeId(id);
        verify(equipmentTypeMapper, times(1)).delete(id);
    }

    @Test
    void delete_hasEquipment() {
        // 准备数据
        Long id = 1L;
        
        // 模拟设备类型存在
        EquipmentTypeDTO typeDTO = new EquipmentTypeDTO();
        typeDTO.setId(id);
        when(equipmentTypeMapper.selectById(id)).thenReturn(typeDTO);
        
        // 模拟有关联的设备
        when(equipmentTypeMapper.countEquipmentByTypeId(id)).thenReturn(1);
        
        // 调用服务层方法并验证异常
        EquipmentServiceException exception = assertThrows(
            EquipmentServiceException.class, 
            () -> equipmentTypeService.delete(id)
        );
        
        // 验证异常信息
        assertTrue(exception.getMessage().contains("设备类型下存在设备"));
        verify(equipmentTypeMapper, times(1)).selectById(id);
        verify(equipmentTypeMapper, times(1)).countEquipmentByTypeId(id);
        // 不应该调用delete方法
        verify(equipmentTypeMapper, never()).delete(anyLong());
    }

    @Test
    void deleteBatch_success() {
        // 准备数据
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);
        
        // 模拟删除操作
        when(equipmentTypeMapper.deleteBatch(ids)).thenReturn(2);
        
        // 调用服务层方法
        equipmentTypeService.deleteBatch(ids);
        
        // 验证结果
        verify(equipmentTypeMapper, times(1)).deleteBatch(ids);
    }
}
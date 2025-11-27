package com.gmp.equipment.controller;

import com.gmp.equipment.dto.EquipmentTypeDTO;
import com.gmp.equipment.dto.PageResultDTO;
import com.gmp.equipment.exception.EquipmentServiceException;
import com.gmp.equipment.service.EquipmentTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * EquipmentTypeController的单元测试类
 */
class EquipmentTypeControllerTest {

    @Mock
    private EquipmentTypeService equipmentTypeService;

    @InjectMocks
    private EquipmentTypeController equipmentTypeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getById_success() {
        // 准备数据
        Long id = 1L;
        EquipmentTypeDTO equipmentTypeDTO = new EquipmentTypeDTO();
        equipmentTypeDTO.setId(id);
        equipmentTypeDTO.setName("测试设备类型");
        
        // 模拟服务层行为
        when(equipmentTypeService.getById(id)).thenReturn(equipmentTypeDTO);
        
        // 调用控制器方法
        ResponseEntity<?> response = equipmentTypeController.getById(id);
        
        // 验证结果
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(equipmentTypeService, times(1)).getById(id);
    }

    @Test
    void getById_notFound() {
        // 准备数据
        Long id = 1L;
        
        // 模拟服务层行为，抛出异常
        when(equipmentTypeService.getById(id)).thenThrow(new EquipmentServiceException("404", "设备类型不存在"));
        
        // 调用控制器方法
        ResponseEntity<?> response = equipmentTypeController.getById(id);
        
        // 验证结果
        assertEquals(200, response.getStatusCodeValue()); // 统一响应格式返回200，内部包含错误码
        assertNotNull(response.getBody());
        verify(equipmentTypeService, times(1)).getById(id);
    }

    @Test
    void listPage_success() {
        // 准备数据
        Integer pageNum = 1;
        Integer pageSize = 10;
        
        PageResultDTO<EquipmentTypeDTO> pageResult = new PageResultDTO<>();
        pageResult.setTotal(2);
        List<EquipmentTypeDTO> equipmentTypeList = new ArrayList<>();
        EquipmentTypeDTO type1 = new EquipmentTypeDTO();
        type1.setId(1L);
        type1.setName("类型1");
        EquipmentTypeDTO type2 = new EquipmentTypeDTO();
        type2.setId(2L);
        type2.setName("类型2");
        equipmentTypeList.add(type1);
        equipmentTypeList.add(type2);
        pageResult.setRecords(equipmentTypeList);
        
        // 模拟服务层行为
        when(equipmentTypeService.listPage(pageNum, pageSize)).thenReturn(pageResult);
        
        // 调用控制器方法
        ResponseEntity<?> response = equipmentTypeController.listPage(pageNum, pageSize);
        
        // 验证结果
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(equipmentTypeService, times(1)).listPage(pageNum, pageSize);
    }

    @Test
    void listAll_success() {
        // 准备数据
        List<EquipmentTypeDTO> equipmentTypeList = new ArrayList<>();
        EquipmentTypeDTO type1 = new EquipmentTypeDTO();
        type1.setId(1L);
        type1.setName("类型1");
        EquipmentTypeDTO type2 = new EquipmentTypeDTO();
        type2.setId(2L);
        type2.setName("类型2");
        equipmentTypeList.add(type1);
        equipmentTypeList.add(type2);
        
        // 模拟服务层行为
        when(equipmentTypeService.listAll()).thenReturn(equipmentTypeList);
        
        // 调用控制器方法
        ResponseEntity<?> response = equipmentTypeController.listAll();
        
        // 验证结果
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(equipmentTypeService, times(1)).listAll();
    }

    @Test
    void create_success() {
        // 准备数据
        EquipmentTypeDTO equipmentTypeDTO = new EquipmentTypeDTO();
        equipmentTypeDTO.setName("新设备类型");
        equipmentTypeDTO.setCode("ET001");
        
        EquipmentTypeDTO createdType = new EquipmentTypeDTO();
        createdType.setId(1L);
        createdType.setName("新设备类型");
        createdType.setCode("ET001");
        
        // 模拟服务层行为
        when(equipmentTypeService.create(equipmentTypeDTO)).thenReturn(createdType);
        
        // 调用控制器方法
        ResponseEntity<?> response = equipmentTypeController.create(equipmentTypeDTO);
        
        // 验证结果
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(equipmentTypeService, times(1)).create(equipmentTypeDTO);
    }

    @Test
    void update_success() {
        // 准备数据
        Long id = 1L;
        EquipmentTypeDTO equipmentTypeDTO = new EquipmentTypeDTO();
        equipmentTypeDTO.setName("更新设备类型");
        
        EquipmentTypeDTO updatedType = new EquipmentTypeDTO();
        updatedType.setId(id);
        updatedType.setName("更新设备类型");
        
        // 模拟服务层行为
        when(equipmentTypeService.update(id, equipmentTypeDTO)).thenReturn(updatedType);
        
        // 调用控制器方法
        ResponseEntity<?> response = equipmentTypeController.update(id, equipmentTypeDTO);
        
        // 验证结果
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(equipmentTypeService, times(1)).update(id, equipmentTypeDTO);
    }

    @Test
    void delete_success() {
        // 准备数据
        Long id = 1L;
        
        // 模拟服务层行为
        doNothing().when(equipmentTypeService).delete(id);
        
        // 调用控制器方法
        ResponseEntity<?> response = equipmentTypeController.delete(id);
        
        // 验证结果
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(equipmentTypeService, times(1)).delete(id);
    }

    @Test
    void deleteBatch_success() {
        // 准备数据
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);
        
        // 模拟服务层行为
        doNothing().when(equipmentTypeService).deleteBatch(ids);
        
        // 调用控制器方法
        ResponseEntity<?> response = equipmentTypeController.deleteBatch(ids);
        
        // 验证结果
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(equipmentTypeService, times(1)).deleteBatch(ids);
    }
}
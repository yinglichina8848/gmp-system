package com.gmp.equipment.controller;

import com.gmp.equipment.dto.EquipmentDTO;
import com.gmp.equipment.dto.PageResultDTO;
import com.gmp.equipment.exception.EquipmentServiceException;
import com.gmp.equipment.service.EquipmentService;
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
 * EquipmentController的单元测试类
 */
class EquipmentControllerTest {

    @Mock
    private EquipmentService equipmentService;

    @InjectMocks
    private EquipmentController equipmentController;

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
        equipmentDTO.setName("测试设备");
        
        // 模拟服务层行为
        when(equipmentService.getById(id)).thenReturn(equipmentDTO);
        
        // 调用控制器方法
        ResponseEntity<?> response = equipmentController.getById(id);
        
        // 验证结果
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(equipmentService, times(1)).getById(id);
    }

    @Test
    void getById_notFound() {
        // 准备数据
        Long id = 1L;
        
        // 模拟服务层行为，抛出异常
        when(equipmentService.getById(id)).thenThrow(new EquipmentServiceException("404", "设备不存在"));
        
        // 调用控制器方法
        ResponseEntity<?> response = equipmentController.getById(id);
        
        // 验证结果
        assertEquals(200, response.getStatusCodeValue()); // 统一响应格式返回200，内部包含错误码
        assertNotNull(response.getBody());
        verify(equipmentService, times(1)).getById(id);
    }

    @Test
    void listPage_success() {
        // 准备数据
        Long equipmentTypeId = 1L;
        Integer pageNum = 1;
        Integer pageSize = 10;
        
        PageResultDTO<EquipmentDTO> pageResult = new PageResultDTO<>();
        pageResult.setTotal(2);
        List<EquipmentDTO> equipmentList = new ArrayList<>();
        EquipmentDTO equipment1 = new EquipmentDTO();
        equipment1.setId(1L);
        equipment1.setName("设备1");
        EquipmentDTO equipment2 = new EquipmentDTO();
        equipment2.setId(2L);
        equipment2.setName("设备2");
        equipmentList.add(equipment1);
        equipmentList.add(equipment2);
        pageResult.setRecords(equipmentList);
        
        // 模拟服务层行为
        when(equipmentService.listPage(equipmentTypeId, pageNum, pageSize)).thenReturn(pageResult);
        
        // 调用控制器方法
        ResponseEntity<?> response = equipmentController.listPage(equipmentTypeId, pageNum, pageSize);
        
        // 验证结果
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(equipmentService, times(1)).listPage(equipmentTypeId, pageNum, pageSize);
    }

    @Test
    void create_success() {
        // 准备数据
        EquipmentDTO equipmentDTO = new EquipmentDTO();
        equipmentDTO.setName("新设备");
        equipmentDTO.setCode("E001");
        
        EquipmentDTO createdEquipment = new EquipmentDTO();
        createdEquipment.setId(1L);
        createdEquipment.setName("新设备");
        createdEquipment.setCode("E001");
        
        // 模拟服务层行为
        when(equipmentService.create(equipmentDTO)).thenReturn(createdEquipment);
        
        // 调用控制器方法
        ResponseEntity<?> response = equipmentController.create(equipmentDTO);
        
        // 验证结果
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(equipmentService, times(1)).create(equipmentDTO);
    }

    @Test
    void update_success() {
        // 准备数据
        Long id = 1L;
        EquipmentDTO equipmentDTO = new EquipmentDTO();
        equipmentDTO.setName("更新设备");
        
        EquipmentDTO updatedEquipment = new EquipmentDTO();
        updatedEquipment.setId(id);
        updatedEquipment.setName("更新设备");
        
        // 模拟服务层行为
        when(equipmentService.update(id, equipmentDTO)).thenReturn(updatedEquipment);
        
        // 调用控制器方法
        ResponseEntity<?> response = equipmentController.update(id, equipmentDTO);
        
        // 验证结果
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(equipmentService, times(1)).update(id, equipmentDTO);
    }

    @Test
    void delete_success() {
        // 准备数据
        Long id = 1L;
        
        // 模拟服务层行为
        doNothing().when(equipmentService).delete(id);
        
        // 调用控制器方法
        ResponseEntity<?> response = equipmentController.delete(id);
        
        // 验证结果
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(equipmentService, times(1)).delete(id);
    }

    @Test
    void deleteBatch_success() {
        // 准备数据
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);
        
        // 模拟服务层行为
        doNothing().when(equipmentService).deleteBatch(ids);
        
        // 调用控制器方法
        ResponseEntity<?> response = equipmentController.deleteBatch(ids);
        
        // 验证结果
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(equipmentService, times(1)).deleteBatch(ids);
    }
}
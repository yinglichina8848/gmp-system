package com.gmp.equipment.controller;

import com.gmp.equipment.dto.MaintenanceRecordDTO;
import com.gmp.equipment.dto.PageResultDTO;
import com.gmp.equipment.exception.EquipmentServiceException;
import com.gmp.equipment.service.MaintenanceRecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * MaintenanceRecordController的单元测试类
 */
class MaintenanceRecordControllerTest {

    @Mock
    private MaintenanceRecordService maintenanceRecordService;

    @InjectMocks
    private MaintenanceRecordController maintenanceRecordController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getById_success() {
        // 准备数据
        Long id = 1L;
        MaintenanceRecordDTO recordDTO = new MaintenanceRecordDTO();
        recordDTO.setId(id);
        recordDTO.setMaintenanceNumber("MAINT001");
        
        // 模拟服务层行为
        when(maintenanceRecordService.getById(id)).thenReturn(recordDTO);
        
        // 调用控制器方法
        ResponseEntity<?> response = maintenanceRecordController.getById(id);
        
        // 验证结果
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(maintenanceRecordService, times(1)).getById(id);
    }

    @Test
    void getByMaintenanceNumber_success() {
        // 准备数据
        String maintenanceNumber = "MAINT001";
        MaintenanceRecordDTO recordDTO = new MaintenanceRecordDTO();
        recordDTO.setId(1L);
        recordDTO.setMaintenanceNumber(maintenanceNumber);
        
        // 模拟服务层行为
        when(maintenanceRecordService.getByMaintenanceNumber(maintenanceNumber)).thenReturn(recordDTO);
        
        // 调用控制器方法
        ResponseEntity<?> response = maintenanceRecordController.getByMaintenanceNumber(maintenanceNumber);
        
        // 验证结果
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(maintenanceRecordService, times(1)).getByMaintenanceNumber(maintenanceNumber);
    }

    @Test
    void listPage_success() {
        // 准备数据
        Long equipmentId = 1L;
        Integer pageNum = 1;
        Integer pageSize = 10;
        
        PageResultDTO<MaintenanceRecordDTO> pageResult = new PageResultDTO<>();
        pageResult.setTotal(2);
        List<MaintenanceRecordDTO> recordList = new ArrayList<>();
        MaintenanceRecordDTO record1 = new MaintenanceRecordDTO();
        record1.setId(1L);
        record1.setMaintenanceNumber("MAINT001");
        MaintenanceRecordDTO record2 = new MaintenanceRecordDTO();
        record2.setId(2L);
        record2.setMaintenanceNumber("MAINT002");
        recordList.add(record1);
        recordList.add(record2);
        pageResult.setRecords(recordList);
        
        // 模拟服务层行为
        when(maintenanceRecordService.listPage(equipmentId, pageNum, pageSize)).thenReturn(pageResult);
        
        // 调用控制器方法
        ResponseEntity<?> response = maintenanceRecordController.listPage(equipmentId, pageNum, pageSize);
        
        // 验证结果
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(maintenanceRecordService, times(1)).listPage(equipmentId, pageNum, pageSize);
    }

    @Test
    void listByEquipmentId_success() {
        // 准备数据
        Long equipmentId = 1L;
        List<MaintenanceRecordDTO> recordList = new ArrayList<>();
        MaintenanceRecordDTO record1 = new MaintenanceRecordDTO();
        record1.setId(1L);
        record1.setEquipmentId(equipmentId);
        recordList.add(record1);
        
        // 模拟服务层行为
        when(maintenanceRecordService.listByEquipmentId(equipmentId)).thenReturn(recordList);
        
        // 调用控制器方法
        ResponseEntity<?> response = maintenanceRecordController.listByEquipmentId(equipmentId);
        
        // 验证结果
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(maintenanceRecordService, times(1)).listByEquipmentId(equipmentId);
    }

    @Test
    void listByMaintenancePlanId_success() {
        // 准备数据
        Long maintenancePlanId = 1L;
        List<MaintenanceRecordDTO> recordList = new ArrayList<>();
        MaintenanceRecordDTO record1 = new MaintenanceRecordDTO();
        record1.setId(1L);
        record1.setMaintenancePlanId(maintenancePlanId);
        recordList.add(record1);
        
        // 模拟服务层行为
        when(maintenanceRecordService.listByMaintenancePlanId(maintenancePlanId)).thenReturn(recordList);
        
        // 调用控制器方法
        ResponseEntity<?> response = maintenanceRecordController.listByMaintenancePlanId(maintenancePlanId);
        
        // 验证结果
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(maintenanceRecordService, times(1)).listByMaintenancePlanId(maintenancePlanId);
    }

    @Test
    void listByDateRange_success() throws ParseException {
        // 准备数据
        String startDateStr = "2024-01-01";
        String endDateStr = "2024-12-31";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = sdf.parse(startDateStr);
        Date endDate = sdf.parse(endDateStr);
        
        List<MaintenanceRecordDTO> recordList = new ArrayList<>();
        MaintenanceRecordDTO record1 = new MaintenanceRecordDTO();
        record1.setId(1L);
        recordList.add(record1);
        
        // 模拟服务层行为
        when(maintenanceRecordService.listByDateRange(startDate, endDate)).thenReturn(recordList);
        
        // 调用控制器方法
        ResponseEntity<?> response = maintenanceRecordController.listByDateRange(startDateStr, endDateStr);
        
        // 验证结果
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(maintenanceRecordService, times(1)).listByDateRange(startDate, endDate);
    }

    @Test
    void listByDateRange_invalidDateFormat() throws ParseException {
        // 准备数据 - 无效的日期格式
        String startDateStr = "2024/01/01";
        String endDateStr = "2024/12/31";
        
        // 调用控制器方法
        ResponseEntity<?> response = maintenanceRecordController.listByDateRange(startDateStr, endDateStr);
        
        // 验证结果
        assertEquals(200, response.getStatusCodeValue()); // 统一响应格式返回200，内部包含错误码
        assertNotNull(response.getBody());
        // 因为日期格式错误，不应该调用服务层方法
        verify(maintenanceRecordService, never()).listByDateRange(any(), any());
    }

    @Test
    void checkMaintenanceNumber_success() {
        // 准备数据
        String maintenanceNumber = "MAINT001";
        Long id = null;
        boolean exists = true;
        
        // 模拟服务层行为
        when(maintenanceRecordService.existsByMaintenanceNumber(maintenanceNumber, id)).thenReturn(exists);
        
        // 调用控制器方法
        ResponseEntity<?> response = maintenanceRecordController.checkMaintenanceNumber(maintenanceNumber, id);
        
        // 验证结果
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(maintenanceRecordService, times(1)).existsByMaintenanceNumber(maintenanceNumber, id);
    }

    @Test
    void create_success() {
        // 准备数据
        MaintenanceRecordDTO recordDTO = new MaintenanceRecordDTO();
        recordDTO.setMaintenanceNumber("MAINT001");
        recordDTO.setEquipmentId(1L);
        
        MaintenanceRecordDTO createdRecord = new MaintenanceRecordDTO();
        createdRecord.setId(1L);
        createdRecord.setMaintenanceNumber("MAINT001");
        createdRecord.setEquipmentId(1L);
        
        // 模拟服务层行为
        when(maintenanceRecordService.create(recordDTO)).thenReturn(createdRecord);
        
        // 调用控制器方法
        ResponseEntity<?> response = maintenanceRecordController.create(recordDTO);
        
        // 验证结果
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(maintenanceRecordService, times(1)).create(recordDTO);
    }

    @Test
    void update_success() {
        // 准备数据
        Long id = 1L;
        MaintenanceRecordDTO recordDTO = new MaintenanceRecordDTO();
        recordDTO.setMaintenanceNumber("MAINT001-UPDATED");
        
        MaintenanceRecordDTO updatedRecord = new MaintenanceRecordDTO();
        updatedRecord.setId(id);
        updatedRecord.setMaintenanceNumber("MAINT001-UPDATED");
        
        // 模拟服务层行为
        when(maintenanceRecordService.update(id, recordDTO)).thenReturn(updatedRecord);
        
        // 调用控制器方法
        ResponseEntity<?> response = maintenanceRecordController.update(id, recordDTO);
        
        // 验证结果
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(maintenanceRecordService, times(1)).update(id, recordDTO);
    }

    @Test
    void delete_success() {
        // 准备数据
        Long id = 1L;
        
        // 模拟服务层行为
        doNothing().when(maintenanceRecordService).delete(id);
        
        // 调用控制器方法
        ResponseEntity<?> response = maintenanceRecordController.delete(id);
        
        // 验证结果
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(maintenanceRecordService, times(1)).delete(id);
    }

    @Test
    void deleteBatch_success() {
        // 准备数据
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);
        
        // 模拟服务层行为
        doNothing().when(maintenanceRecordService).deleteBatch(ids);
        
        // 调用控制器方法
        ResponseEntity<?> response = maintenanceRecordController.deleteBatch(ids);
        
        // 验证结果
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(maintenanceRecordService, times(1)).deleteBatch(ids);
    }
}
package com.gmp.equipment.controller;

import com.gmp.equipment.dto.CalibrationRecordDTO;
import com.gmp.equipment.dto.PageResultDTO;
import com.gmp.equipment.exception.EquipmentServiceException;
import com.gmp.equipment.service.CalibrationRecordService;
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
 * CalibrationRecordController的单元测试类
 */
class CalibrationRecordControllerTest {

    @Mock
    private CalibrationRecordService calibrationRecordService;

    @InjectMocks
    private CalibrationRecordController calibrationRecordController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getById_success() {
        // 准备数据
        Long id = 1L;
        CalibrationRecordDTO recordDTO = new CalibrationRecordDTO();
        recordDTO.setId(id);
        recordDTO.setCalibrationNumber("CAL001");
        
        // 模拟服务层行为
        when(calibrationRecordService.getById(id)).thenReturn(recordDTO);
        
        // 调用控制器方法
        ResponseEntity<?> response = calibrationRecordController.getById(id);
        
        // 验证结果
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(calibrationRecordService, times(1)).getById(id);
    }

    @Test
    void getByCalibrationNumber_success() {
        // 准备数据
        String calibrationNumber = "CAL001";
        CalibrationRecordDTO recordDTO = new CalibrationRecordDTO();
        recordDTO.setId(1L);
        recordDTO.setCalibrationNumber(calibrationNumber);
        
        // 模拟服务层行为
        when(calibrationRecordService.getByCalibrationNumber(calibrationNumber)).thenReturn(recordDTO);
        
        // 调用控制器方法
        ResponseEntity<?> response = calibrationRecordController.getByCalibrationNumber(calibrationNumber);
        
        // 验证结果
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(calibrationRecordService, times(1)).getByCalibrationNumber(calibrationNumber);
    }

    @Test
    void listPage_success() {
        // 准备数据
        Long equipmentId = 1L;
        Integer pageNum = 1;
        Integer pageSize = 10;
        
        PageResultDTO<CalibrationRecordDTO> pageResult = new PageResultDTO<>();
        pageResult.setTotal(2);
        List<CalibrationRecordDTO> recordList = new ArrayList<>();
        CalibrationRecordDTO record1 = new CalibrationRecordDTO();
        record1.setId(1L);
        record1.setCalibrationNumber("CAL001");
        CalibrationRecordDTO record2 = new CalibrationRecordDTO();
        record2.setId(2L);
        record2.setCalibrationNumber("CAL002");
        recordList.add(record1);
        recordList.add(record2);
        pageResult.setRecords(recordList);
        
        // 模拟服务层行为
        when(calibrationRecordService.listPage(equipmentId, pageNum, pageSize)).thenReturn(pageResult);
        
        // 调用控制器方法
        ResponseEntity<?> response = calibrationRecordController.listPage(equipmentId, pageNum, pageSize);
        
        // 验证结果
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(calibrationRecordService, times(1)).listPage(equipmentId, pageNum, pageSize);
    }

    @Test
    void listByEquipmentId_success() {
        // 准备数据
        Long equipmentId = 1L;
        List<CalibrationRecordDTO> recordList = new ArrayList<>();
        CalibrationRecordDTO record1 = new CalibrationRecordDTO();
        record1.setId(1L);
        record1.setEquipmentId(equipmentId);
        recordList.add(record1);
        
        // 模拟服务层行为
        when(calibrationRecordService.listByEquipmentId(equipmentId)).thenReturn(recordList);
        
        // 调用控制器方法
        ResponseEntity<?> response = calibrationRecordController.listByEquipmentId(equipmentId);
        
        // 验证结果
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(calibrationRecordService, times(1)).listByEquipmentId(equipmentId);
    }

    @Test
    void listByDateRange_success() throws ParseException {
        // 准备数据
        String startDateStr = "2024-01-01";
        String endDateStr = "2024-12-31";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = sdf.parse(startDateStr);
        Date endDate = sdf.parse(endDateStr);
        
        List<CalibrationRecordDTO> recordList = new ArrayList<>();
        CalibrationRecordDTO record1 = new CalibrationRecordDTO();
        record1.setId(1L);
        recordList.add(record1);
        
        // 模拟服务层行为
        when(calibrationRecordService.listByDateRange(startDate, endDate)).thenReturn(recordList);
        
        // 调用控制器方法
        ResponseEntity<?> response = calibrationRecordController.listByDateRange(startDateStr, endDateStr);
        
        // 验证结果
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(calibrationRecordService, times(1)).listByDateRange(startDate, endDate);
    }

    @Test
    void listByDateRange_invalidDateFormat() throws ParseException {
        // 准备数据 - 无效的日期格式
        String startDateStr = "2024/01/01";
        String endDateStr = "2024/12/31";
        
        // 调用控制器方法
        ResponseEntity<?> response = calibrationRecordController.listByDateRange(startDateStr, endDateStr);
        
        // 验证结果
        assertEquals(200, response.getStatusCodeValue()); // 统一响应格式返回200，内部包含错误码
        assertNotNull(response.getBody());
        // 因为日期格式错误，不应该调用服务层方法
        verify(calibrationRecordService, never()).listByDateRange(any(), any());
    }

    @Test
    void create_success() {
        // 准备数据
        CalibrationRecordDTO recordDTO = new CalibrationRecordDTO();
        recordDTO.setCalibrationNumber("CAL001");
        recordDTO.setEquipmentId(1L);
        
        CalibrationRecordDTO createdRecord = new CalibrationRecordDTO();
        createdRecord.setId(1L);
        createdRecord.setCalibrationNumber("CAL001");
        createdRecord.setEquipmentId(1L);
        
        // 模拟服务层行为
        when(calibrationRecordService.create(recordDTO)).thenReturn(createdRecord);
        
        // 调用控制器方法
        ResponseEntity<?> response = calibrationRecordController.create(recordDTO);
        
        // 验证结果
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(calibrationRecordService, times(1)).create(recordDTO);
    }

    @Test
    void update_success() {
        // 准备数据
        Long id = 1L;
        CalibrationRecordDTO recordDTO = new CalibrationRecordDTO();
        recordDTO.setCalibrationNumber("CAL001-UPDATED");
        
        CalibrationRecordDTO updatedRecord = new CalibrationRecordDTO();
        updatedRecord.setId(id);
        updatedRecord.setCalibrationNumber("CAL001-UPDATED");
        
        // 模拟服务层行为
        when(calibrationRecordService.update(id, recordDTO)).thenReturn(updatedRecord);
        
        // 调用控制器方法
        ResponseEntity<?> response = calibrationRecordController.update(id, recordDTO);
        
        // 验证结果
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(calibrationRecordService, times(1)).update(id, recordDTO);
    }

    @Test
    void delete_success() {
        // 准备数据
        Long id = 1L;
        
        // 模拟服务层行为
        doNothing().when(calibrationRecordService).delete(id);
        
        // 调用控制器方法
        ResponseEntity<?> response = calibrationRecordController.delete(id);
        
        // 验证结果
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(calibrationRecordService, times(1)).delete(id);
    }
}
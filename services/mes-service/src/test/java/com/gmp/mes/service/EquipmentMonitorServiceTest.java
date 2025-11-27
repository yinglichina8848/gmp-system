package com.gmp.mes.service;

import com.gmp.mes.entity.EquipmentMonitor;
import com.gmp.mes.entity.EquipmentStatusRecord;
import com.gmp.mes.repository.EquipmentMonitorRepository;
import com.gmp.mes.repository.EquipmentStatusRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDateTime;
import java.util.*;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * 设备监控服务类的单元测试
 * 
 * @author gmp-system
 */
class EquipmentMonitorServiceTest {

    @Mock
    private EquipmentMonitorRepository equipmentMonitorRepository;

    @Mock
    private EquipmentStatusRecordRepository equipmentStatusRecordRepository;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @InjectMocks
    private EquipmentMonitorService equipmentMonitorService;

    private EquipmentMonitor testEquipment;
    private EquipmentStatusRecord testStatusRecord;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // 创建设备监控对象
        testEquipment = new EquipmentMonitor();
        testEquipment.setId(1L);
        testEquipment.setEquipmentCode("EQP-001");
        testEquipment.setEquipmentName("注塑机A");
        testEquipment.setEquipmentType("注塑机");
        testEquipment.setLocation("车间1");
        testEquipment.setIpAddress("192.168.1.101");
        testEquipment.setStatus(EquipmentMonitor.EquipmentStatus.ONLINE);
        testEquipment.setTemperature(85.5);
        testEquipment.setPressure(120.0);
        testEquipment.setHumidity(45.0);
        testEquipment.setVibration(2.5);
        testEquipment.setAlertThresholdTemperature(100.0);
        testEquipment.setAlertThresholdPressure(150.0);
        testEquipment.setAlertThresholdHumidity(80.0);
        testEquipment.setAlertThresholdVibration(5.0);

        // 创建设备状态记录
        testStatusRecord = new EquipmentStatusRecord();
        testStatusRecord.setId(1L);
        testStatusRecord.setEquipmentCode("EQP-001");
        testStatusRecord.setStatus(EquipmentStatusRecord.EquipmentStatus.ONLINE);
        testStatusRecord.setTemperature(85.5);
        testStatusRecord.setPressure(120.0);
        testStatusRecord.setHumidity(45.0);
        testStatusRecord.setVibration(2);
        testStatusRecord.setOperator("操作员1");
        testStatusRecord.setNotes("正常运行");
    }

    @Test
    void testCreateEquipmentMonitor() {
        when(equipmentMonitorRepository.save(any(EquipmentMonitor.class))).thenReturn(testEquipment);
        when(redisTemplate.opsForValue().set(anyString(), any())).thenReturn(true);

        EquipmentMonitor createdEquipment = equipmentMonitorService.createEquipmentMonitor(testEquipment);

        assertNotNull(createdEquipment);
        assertEquals("EQP-001", createdEquipment.getEquipmentCode());
        verify(equipmentMonitorRepository, times(1)).save(testEquipment);
        verify(redisTemplate.opsForValue(), times(1)).set(anyString(), any());
    }

    @Test
    void testGetEquipmentMonitorById() {
        when(equipmentMonitorRepository.findById(1L)).thenReturn(Optional.of(testEquipment));

        Optional<EquipmentMonitor> foundEquipment = equipmentMonitorService.getEquipmentMonitorById(1L);

        assertTrue(foundEquipment.isPresent());
        assertEquals("EQP-001", foundEquipment.get().getEquipmentId());
        verify(equipmentMonitorRepository, times(1)).findById(1L);
    }

    @Test
    void testGetEquipmentMonitorByEquipmentId() {
        when(equipmentMonitorRepository.findByEquipmentId("EQP-001")).thenReturn(Optional.of(testEquipment));
        when(redisTemplate.opsForValue().get(anyString())).thenReturn(null);
        when(redisTemplate.opsForValue().set(anyString(), any())).thenReturn(true);

        Optional<EquipmentMonitor> foundEquipment = equipmentMonitorService.getEquipmentMonitorByEquipmentId("EQP-001");

        assertTrue(foundEquipment.isPresent());
        assertEquals("注塑机A", foundEquipment.get().getEquipmentName());
        verify(equipmentMonitorRepository, times(1)).findByEquipmentId("EQP-001");
    }

    @Test
    void testGetEquipmentMonitorFromCache() {
        when(redisTemplate.opsForValue().get(anyString())).thenReturn(testEquipment);

        Optional<EquipmentMonitor> foundEquipment = equipmentMonitorService.getEquipmentMonitorByEquipmentId("EQP-001");

        assertTrue(foundEquipment.isPresent());
        assertEquals("EQP-001", foundEquipment.get().getEquipmentId());
        verify(redisTemplate.opsForValue(), times(1)).get(anyString());
        verify(equipmentMonitorRepository, times(0)).findByEquipmentId(anyString());
    }

    @Test
    void testGetAllEquipmentMonitors() {
        List<EquipmentMonitor> equipments = Arrays.asList(testEquipment);
        when(equipmentMonitorRepository.findAll()).thenReturn(equipments);

        List<EquipmentMonitor> allEquipments = equipmentMonitorService.getAllEquipmentMonitors();

        assertNotNull(allEquipments);
        assertEquals(1, allEquipments.size());
        verify(equipmentMonitorRepository, times(1)).findAll();
    }

    @Test
    void testUpdateEquipmentMonitor() {
        when(equipmentMonitorRepository.findById(1L)).thenReturn(Optional.of(testEquipment));
        when(equipmentMonitorRepository.save(any(EquipmentMonitor.class))).thenReturn(testEquipment);
        when(redisTemplate.opsForValue().set(anyString(), any())).thenReturn(true);

        testEquipment.setEquipmentName("注塑机A-更新");
        testEquipment.setStatus(EquipmentMonitor.EquipmentStatus.MAINTENANCE);

        EquipmentMonitor updatedEquipment = equipmentMonitorService.updateEquipmentMonitor(testEquipment);

        assertNotNull(updatedEquipment);
        assertEquals("注塑机A-更新", updatedEquipment.getEquipmentName());
        assertEquals(EquipmentMonitor.EquipmentStatus.MAINTENANCE, updatedEquipment.getStatus());
        verify(equipmentMonitorRepository, times(1)).save(testEquipment);
    }

    @Test
    void testDeleteEquipmentMonitor() {
        when(equipmentMonitorRepository.existsById(1L)).thenReturn(true);
        doNothing().when(equipmentMonitorRepository).deleteById(1L);
        when(redisTemplate.delete(anyString())).thenReturn(true);

        equipmentMonitorService.deleteEquipmentMonitor(1L);

        verify(equipmentMonitorRepository, times(1)).existsById(1L);
        verify(equipmentMonitorRepository, times(1)).deleteById(1L);
        verify(redisTemplate, times(1)).delete(anyString());
    }

    @Test
    void testUpdateEquipmentStatus() {
        when(equipmentMonitorRepository.findByEquipmentId("EQP-001")).thenReturn(Optional.of(testEquipment));
        when(equipmentMonitorRepository.save(any(EquipmentMonitor.class))).thenReturn(testEquipment);
        when(equipmentStatusRecordRepository.save(any(EquipmentStatusRecord.class))).thenReturn(testStatusRecord);
        when(redisTemplate.opsForValue().set(anyString(), any())).thenReturn(true);

        EquipmentMonitor updatedEquipment = equipmentMonitorService.updateEquipmentStatus("EQP-001",
                EquipmentMonitor.EquipmentStatus.STOPPED, "设备停机", "操作员2");

        assertEquals(EquipmentMonitor.EquipmentStatus.STOPPED, updatedEquipment.getStatus());
        verify(equipmentMonitorRepository, times(1)).save(testEquipment);
        verify(equipmentStatusRecordRepository, times(1)).save(any(EquipmentStatusRecord.class));
    }

    @Test
    void testUpdateEquipmentParameters() {
        when(equipmentMonitorRepository.findByEquipmentId("EQP-001")).thenReturn(Optional.of(testEquipment));
        when(equipmentMonitorRepository.save(any(EquipmentMonitor.class))).thenReturn(testEquipment);
        when(redisTemplate.opsForValue().set(anyString(), any())).thenReturn(true);

        Map<String, Double> params = new HashMap<>();
        params.put("temperature", 88.0);
        params.put("pressure", 125.0);

        EquipmentMonitor updatedEquipment = equipmentMonitorService.updateEquipmentParameters("EQP-001", params);

        assertEquals(88.0, updatedEquipment.getTemperature());
        assertEquals(125.0, updatedEquipment.getPressure());
        verify(equipmentMonitorRepository, times(1)).save(testEquipment);
    }

    @Test
    void testCheckEquipmentThresholds_normal() {
        // 正常参数，不触发阈值
        boolean isAlert = equipmentMonitorService.checkEquipmentThresholds(testEquipment);

        assertFalse(isAlert);
    }

    @Test
    void testCheckEquipmentThresholds_overThreshold() {
        // 修改参数使其超过阈值
        testEquipment.setTemperature(105.0); // 超过温度阈值

        boolean isAlert = equipmentMonitorService.checkEquipmentThresholds(testEquipment);

        assertTrue(isAlert);
    }

    @Test
    void testGetEquipmentStatusRecords() {
        List<EquipmentStatusRecord> records = Arrays.asList(testStatusRecord);
        when(equipmentStatusRecordRepository.findByEquipmentIdOrderByRecordTimeDesc("EQP-001", PageRequest.of(0, 10)))
                .thenReturn(records);

        List<EquipmentStatusRecord> foundRecords = equipmentMonitorService.getEquipmentStatusRecords("EQP-001", 0, 10);

        assertNotNull(foundRecords);
        assertEquals(1, foundRecords.size());
        verify(equipmentStatusRecordRepository, times(1))
                .findByEquipmentIdOrderByRecordTimeDesc("EQP-001", PageRequest.of(0, 10));
    }

    @Test
    void testFindEquipmentMonitorsByStatus() {
        List<EquipmentMonitor> equipments = Arrays.asList(testEquipment);
        when(equipmentMonitorRepository.findByStatus(EquipmentMonitor.EquipmentStatus.RUNNING)).thenReturn(equipments);

        List<EquipmentMonitor> foundEquipments = equipmentMonitorService
                .findEquipmentMonitorsByStatus(EquipmentMonitor.EquipmentStatus.RUNNING);

        assertNotNull(foundEquipments);
        assertEquals(1, foundEquipments.size());
        verify(equipmentMonitorRepository, times(1)).findByStatus(EquipmentMonitor.EquipmentStatus.RUNNING);
    }

    @Test
    void testCleanRedisCache() {
        when(redisTemplate.delete(anyString())).thenReturn(true);

        boolean result = equipmentMonitorService.cleanRedisCache("EQP-001");

        assertTrue(result);
        verify(redisTemplate, times(1)).delete(anyString());
    }
}
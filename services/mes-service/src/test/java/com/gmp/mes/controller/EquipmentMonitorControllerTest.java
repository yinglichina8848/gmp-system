package com.gmp.mes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmp.mes.entity.EquipmentMonitor;
import com.gmp.mes.service.EquipmentMonitorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 设备监控控制器的单元测试
 * 
 * @author gmp-system
 */
@WebMvcTest(EquipmentMonitorController.class)
class EquipmentMonitorControllerTest {

    @Mock
    private EquipmentMonitorService equipmentMonitorService;

    @InjectMocks
    private EquipmentMonitorController equipmentMonitorController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private EquipmentMonitor testEquipment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(equipmentMonitorController).build();
        objectMapper = new ObjectMapper();
        
        // 创建设备监控对象
        testEquipment = new EquipmentMonitor();
        testEquipment.setId(1L);
        testEquipment.setEquipmentId("EQP-001");
        testEquipment.setEquipmentName("注塑机A");
        testEquipment.setType("注塑机");
        testEquipment.setLocation("车间1");
        testEquipment.setIpAddress("192.168.1.101");
        testEquipment.setPort(8080);
        testEquipment.setStatus(EquipmentMonitor.EquipmentStatus.RUNNING);
        testEquipment.setTemperature(85.5);
        testEquipment.setPressure(120.0);
        testEquipment.setHumidity(45.0);
        testEquipment.setVibration(2.5);
        testEquipment.setTempThreshold(100.0);
        testEquipment.setPressThreshold(150.0);
        testEquipment.setHumidityThreshold(80.0);
        testEquipment.setVibrationThreshold(5.0);
    }

    @Test
    void testCreateEquipmentMonitor() throws Exception {
        when(equipmentMonitorService.createEquipmentMonitor(any(EquipmentMonitor.class))).thenReturn(testEquipment);
        
        mockMvc.perform(post("/mes/api/equipment/monitor")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testEquipment)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.equipmentId").value("EQP-001"))
                .andExpect(jsonPath("$.equipmentName").value("注塑机A"));
    }

    @Test
    void testGetEquipmentMonitorById() throws Exception {
        when(equipmentMonitorService.getEquipmentMonitorById(1L)).thenReturn(Optional.of(testEquipment));
        
        mockMvc.perform(get("/mes/api/equipment/monitor/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.equipmentId").value("EQP-001"))
                .andExpect(jsonPath("$.status").value("RUNNING"));
    }

    @Test
    void testGetEquipmentMonitorByEquipmentId() throws Exception {
        when(equipmentMonitorService.getEquipmentMonitorByEquipmentId("EQP-001")).thenReturn(Optional.of(testEquipment));
        
        mockMvc.perform(get("/mes/api/equipment/monitor/by-id/EQP-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.equipmentName").value("注塑机A"));
    }

    @Test
    void testGetAllEquipmentMonitors() throws Exception {
        when(equipmentMonitorService.getAllEquipmentMonitors()).thenReturn(Arrays.asList(testEquipment));
        
        mockMvc.perform(get("/mes/api/equipment/monitor"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].equipmentId").value("EQP-001"));
    }

    @Test
    void testUpdateEquipmentMonitor() throws Exception {
        testEquipment.setEquipmentName("注塑机A-更新");
        when(equipmentMonitorService.updateEquipmentMonitor(any(EquipmentMonitor.class))).thenReturn(testEquipment);
        
        mockMvc.perform(put("/mes/api/equipment/monitor")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testEquipment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.equipmentName").value("注塑机A-更新"));
    }

    @Test
    void testDeleteEquipmentMonitor() throws Exception {
        mockMvc.perform(delete("/mes/api/equipment/monitor/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testUpdateEquipmentStatus() throws Exception {
        Map<String, Object> statusUpdate = new HashMap<>();
        statusUpdate.put("status", "MAINTENANCE");
        statusUpdate.put("remark", "设备维护中");
        statusUpdate.put("operator", "操作员1");
        
        when(equipmentMonitorService.updateEquipmentStatus(anyString(), any(), anyString(), anyString()))
                .thenReturn(testEquipment);
        
        mockMvc.perform(patch("/mes/api/equipment/monitor/EQP-001/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(statusUpdate)))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateEquipmentParameters() throws Exception {
        Map<String, Double> parameters = new HashMap<>();
        parameters.put("temperature", 88.0);
        parameters.put("pressure", 125.0);
        
        when(equipmentMonitorService.updateEquipmentParameters(anyString(), any(Map.class)))
                .thenReturn(testEquipment);
        
        mockMvc.perform(patch("/mes/api/equipment/monitor/EQP-001/parameters")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(parameters)))
                .andExpect(status().isOk());
    }

    @Test
    void testFindEquipmentMonitorsByStatus() throws Exception {
        when(equipmentMonitorService.findEquipmentMonitorsByStatus(any()))
                .thenReturn(Arrays.asList(testEquipment));
        
        mockMvc.perform(get("/mes/api/equipment/monitor/status/RUNNING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testCleanRedisCache() throws Exception {
        when(equipmentMonitorService.cleanRedisCache(anyString())).thenReturn(true);
        
        mockMvc.perform(delete("/mes/api/equipment/monitor/EQP-001/cache"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testGetEquipmentStatusRecords() throws Exception {
        mockMvc.perform(get("/mes/api/equipment/monitor/EQP-001/status-records?page=0&size=10"))
                .andExpect(status().isOk());
    }
}
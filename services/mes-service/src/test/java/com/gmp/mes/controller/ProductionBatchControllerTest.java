package com.gmp.mes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmp.mes.entity.ProductionBatch;
import com.gmp.mes.service.ProductionBatchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 生产批次控制器的单元测试
 * 
 * @author gmp-system
 */
@WebMvcTest(ProductionBatchController.class)
class ProductionBatchControllerTest {

    @Mock
    private ProductionBatchService productionBatchService;

    @InjectMocks
    private ProductionBatchController productionBatchController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private ProductionBatch testBatch;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productionBatchController).build();
        objectMapper = new ObjectMapper();
        
        // 创建测试批次对象
        testBatch = new ProductionBatch();
        testBatch.setId(1L);
        testBatch.setBatchNumber("BATCH-2024-001");
        testBatch.setProductCode("PRD-001");
        testBatch.setProductName("产品A");
        testBatch.setEquipmentId("EQP-001");
        testBatch.setEquipmentName("注塑机A");
        testBatch.setPlanQuantity(100);
        testBatch.setActualQuantity(0);
        testBatch.setStatus(ProductionBatch.Status.PENDING);
        testBatch.setCreator("操作员1");
        testBatch.setCreateTime(LocalDateTime.now());
    }

    @Test
    void testCreateProductionBatch() throws Exception {
        when(productionBatchService.createProductionBatch(any(ProductionBatch.class))).thenReturn(testBatch);
        
        mockMvc.perform(post("/mes/api/batches")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testBatch)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.batchNumber").value("BATCH-2024-001"))
                .andExpect(jsonPath("$.productName").value("产品A"));
    }

    @Test
    void testGetProductionBatchById() throws Exception {
        when(productionBatchService.getProductionBatchById(1L)).thenReturn(Optional.of(testBatch));
        
        mockMvc.perform(get("/mes/api/batches/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.batchNumber").value("BATCH-2024-001"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void testGetProductionBatchByBatchNumber() throws Exception {
        when(productionBatchService.getProductionBatchByBatchNumber("BATCH-2024-001")).thenReturn(Optional.of(testBatch));
        
        mockMvc.perform(get("/mes/api/batches/by-number/BATCH-2024-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("产品A"));
    }

    @Test
    void testGetAllProductionBatches() throws Exception {
        List<ProductionBatch> batches = Arrays.asList(testBatch);
        Page<ProductionBatch> page = new PageImpl<>(batches, PageRequest.of(0, 10), batches.size());
        
        when(productionBatchService.getAllProductionBatches(any(PageRequest.class))).thenReturn(page);
        
        mockMvc.perform(get("/mes/api/batches?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].batchNumber").value("BATCH-2024-001"));
    }

    @Test
    void testUpdateProductionBatch() throws Exception {
        testBatch.setProductName("产品A-更新");
        when(productionBatchService.updateProductionBatch(any(ProductionBatch.class))).thenReturn(testBatch);
        
        mockMvc.perform(put("/mes/api/batches")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testBatch)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("产品A-更新"));
    }

    @Test
    void testDeleteProductionBatch() throws Exception {
        mockMvc.perform(delete("/mes/api/batches/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testStartProductionBatch() throws Exception {
        when(productionBatchService.startProductionBatch(anyLong(), anyString())).thenReturn(testBatch);
        
        Map<String, String> startInfo = new HashMap<>();
        startInfo.put("operator", "操作员1");
        
        mockMvc.perform(post("/mes/api/batches/1/start")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(startInfo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    void testPauseProductionBatch() throws Exception {
        when(productionBatchService.pauseProductionBatch(anyLong(), anyString(), anyString())).thenReturn(testBatch);
        
        Map<String, String> pauseInfo = new HashMap<>();
        pauseInfo.put("operator", "操作员1");
        pauseInfo.put("reason", "设备维护");
        
        mockMvc.perform(post("/mes/api/batches/1/pause")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pauseInfo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PAUSED"));
    }

    @Test
    void testResumeProductionBatch() throws Exception {
        when(productionBatchService.resumeProductionBatch(anyLong(), anyString())).thenReturn(testBatch);
        
        Map<String, String> resumeInfo = new HashMap<>();
        resumeInfo.put("operator", "操作员1");
        
        mockMvc.perform(post("/mes/api/batches/1/resume")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resumeInfo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    void testCompleteProductionBatch() throws Exception {
        when(productionBatchService.completeProductionBatch(anyLong(), anyString(), anyInt(), anyString())).thenReturn(testBatch);
        
        Map<String, Object> completeInfo = new HashMap<>();
        completeInfo.put("operator", "操作员1");
        completeInfo.put("actualQuantity", 98);
        completeInfo.put("remark", "生产完成");
        
        mockMvc.perform(post("/mes/api/batches/1/complete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(completeInfo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    void testMarkBatchAsFailed() throws Exception {
        when(productionBatchService.markBatchAsFailed(anyLong(), anyString(), anyString())).thenReturn(testBatch);
        
        Map<String, String> failInfo = new HashMap<>();
        failInfo.put("operator", "操作员1");
        failInfo.put("reason", "设备故障");
        
        mockMvc.perform(post("/mes/api/batches/1/fail")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(failInfo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("FAILED"));
    }

    @Test
    void testUpdateBatchQuantity() throws Exception {
        when(productionBatchService.updateBatchQuantity(anyLong(), anyInt())).thenReturn(testBatch);
        
        Map<String, Integer> quantityInfo = new HashMap<>();
        quantityInfo.put("actualQuantity", 50);
        
        mockMvc.perform(patch("/mes/api/batches/1/quantity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(quantityInfo)))
                .andExpect(status().isOk());
    }

    @Test
    void testSearchProductionBatches() throws Exception {
        List<ProductionBatch> batches = Arrays.asList(testBatch);
        Page<ProductionBatch> page = new PageImpl<>(batches, PageRequest.of(0, 10), batches.size());
        
        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("productCode", "PRD-001");
        searchParams.put("equipmentId", "EQP-001");
        
        when(productionBatchService.searchProductionBatches(any(Map.class), any(PageRequest.class))).thenReturn(page);
        
        mockMvc.perform(post("/mes/api/batches/search?page=0&size=10")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(searchParams)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    @Test
    void testFindBatchesByOrderId() throws Exception {
        List<ProductionBatch> batches = Arrays.asList(testBatch);
        
        when(productionBatchService.findBatchesByOrderId(anyLong())).thenReturn(batches);
        
        mockMvc.perform(get("/mes/api/batches/order/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testFindBatchesByStatus() throws Exception {
        List<ProductionBatch> batches = Arrays.asList(testBatch);
        
        when(productionBatchService.findBatchesByStatus(any())).thenReturn(batches);
        
        mockMvc.perform(get("/mes/api/batches/status/PENDING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
}
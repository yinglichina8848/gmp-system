package com.gmp.mes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmp.mes.entity.BatchOperation;
import com.gmp.mes.service.BatchOperationService;
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
 * 批操作控制器的单元测试
 * 
 * @author gmp-system
 */
@WebMvcTest(BatchOperationController.class)
class BatchOperationControllerTest {

    @Mock
    private BatchOperationService batchOperationService;

    @InjectMocks
    private BatchOperationController batchOperationController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private BatchOperation testOperation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(batchOperationController).build();
        objectMapper = new ObjectMapper();
        
        // 创建测试批操作对象
        testOperation = new BatchOperation();
        testOperation.setId(1L);
        testOperation.setOperationNumber("OP-2024-001");
        testOperation.setOperationName("注塑操作");
        testOperation.setOperationType(BatchOperation.OperationType.INJECTION_MOLDING);
        testOperation.setBatchId(1L);
        testOperation.setBatchNumber("BATCH-2024-001");
        testOperation.setStatus(BatchOperation.Status.PENDING);
        testOperation.setEquipmentId("EQP-001");
        testOperation.setCreator("操作员1");
        testOperation.setCreateTime(LocalDateTime.now());
        
        // 设置操作参数
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("temperature", 220.0);
        parameters.put("pressure", 150.0);
        parameters.put("cycleTime", 30.0);
        testOperation.setParameters(parameters);
    }

    @Test
    void testCreateBatchOperation() throws Exception {
        when(batchOperationService.createBatchOperation(any(BatchOperation.class))).thenReturn(testOperation);
        
        mockMvc.perform(post("/mes/api/operations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testOperation)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.operationNumber").value("OP-2024-001"))
                .andExpect(jsonPath("$.operationName").value("注塑操作"));
    }

    @Test
    void testGetBatchOperationById() throws Exception {
        when(batchOperationService.getBatchOperationById(1L)).thenReturn(Optional.of(testOperation));
        
        mockMvc.perform(get("/mes/api/operations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operationNumber").value("OP-2024-001"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void testGetBatchOperationByOperationNumber() throws Exception {
        when(batchOperationService.getBatchOperationByOperationNumber("OP-2024-001")).thenReturn(Optional.of(testOperation));
        
        mockMvc.perform(get("/mes/api/operations/by-number/OP-2024-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operationName").value("注塑操作"));
    }

    @Test
    void testGetAllBatchOperations() throws Exception {
        List<BatchOperation> operations = Arrays.asList(testOperation);
        Page<BatchOperation> page = new PageImpl<>(operations, PageRequest.of(0, 10), operations.size());
        
        when(batchOperationService.getAllBatchOperations(any(PageRequest.class))).thenReturn(page);
        
        mockMvc.perform(get("/mes/api/operations?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].operationNumber").value("OP-2024-001"));
    }

    @Test
    void testUpdateBatchOperation() throws Exception {
        testOperation.setOperationName("注塑操作-更新");
        when(batchOperationService.updateBatchOperation(any(BatchOperation.class))).thenReturn(testOperation);
        
        mockMvc.perform(put("/mes/api/operations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testOperation)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operationName").value("注塑操作-更新"));
    }

    @Test
    void testDeleteBatchOperation() throws Exception {
        mockMvc.perform(delete("/mes/api/operations/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testStartBatchOperation() throws Exception {
        when(batchOperationService.startBatchOperation(anyLong(), anyString())).thenReturn(testOperation);
        
        Map<String, String> startInfo = new HashMap<>();
        startInfo.put("operator", "操作员1");
        
        mockMvc.perform(post("/mes/api/operations/1/start")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(startInfo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    void testCompleteBatchOperation() throws Exception {
        Map<String, Object> result = new HashMap<>();
        result.put("successCount", 98);
        result.put("failCount", 2);
        result.put("defectRate", 2.0);
        
        when(batchOperationService.completeBatchOperation(anyLong(), anyString(), any(Map.class), anyString())).thenReturn(testOperation);
        
        Map<String, Object> completeInfo = new HashMap<>();
        completeInfo.put("operator", "操作员1");
        completeInfo.put("result", result);
        completeInfo.put("remark", "操作完成");
        
        mockMvc.perform(post("/mes/api/operations/1/complete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(completeInfo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    void testCancelBatchOperation() throws Exception {
        when(batchOperationService.cancelBatchOperation(anyLong(), anyString(), anyString())).thenReturn(testOperation);
        
        Map<String, String> cancelInfo = new HashMap<>();
        cancelInfo.put("operator", "操作员1");
        cancelInfo.put("reason", "计划变更");
        
        mockMvc.perform(post("/mes/api/operations/1/cancel")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cancelInfo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELED"));
    }

    @Test
    void testMarkOperationAsFailed() throws Exception {
        when(batchOperationService.markOperationAsFailed(anyLong(), anyString(), anyString())).thenReturn(testOperation);
        
        Map<String, String> failInfo = new HashMap<>();
        failInfo.put("operator", "操作员1");
        failInfo.put("reason", "设备异常");
        
        mockMvc.perform(post("/mes/api/operations/1/fail")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(failInfo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("FAILED"));
    }

    @Test
    void testUpdateOperationParameters() throws Exception {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("temperature", 225.0);
        parameters.put("pressure", 155.0);
        
        when(batchOperationService.updateOperationParameters(anyLong(), any(Map.class))).thenReturn(testOperation);
        
        mockMvc.perform(patch("/mes/api/operations/1/parameters")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(parameters)))
                .andExpect(status().isOk());
    }

    @Test
    void testAddOperationRemark() throws Exception {
        when(batchOperationService.addOperationRemark(anyLong(), anyString())).thenReturn(testOperation);
        
        Map<String, String> remarkInfo = new HashMap<>();
        remarkInfo.put("remark", "注意事项：操作时需检查模具温度");
        
        mockMvc.perform(patch("/mes/api/operations/1/remark")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(remarkInfo)))
                .andExpect(status().isOk());
    }

    @Test
    void testSearchBatchOperations() throws Exception {
        List<BatchOperation> operations = Arrays.asList(testOperation);
        Page<BatchOperation> page = new PageImpl<>(operations, PageRequest.of(0, 10), operations.size());
        
        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("batchId", 1L);
        searchParams.put("operationType", "INJECTION_MOLDING");
        
        when(batchOperationService.searchBatchOperations(any(Map.class), any(PageRequest.class))).thenReturn(page);
        
        mockMvc.perform(post("/mes/api/operations/search?page=0&size=10")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(searchParams)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    @Test
    void testFindOperationsByBatchId() throws Exception {
        List<BatchOperation> operations = Arrays.asList(testOperation);
        
        when(batchOperationService.findOperationsByBatchId(anyLong())).thenReturn(operations);
        
        mockMvc.perform(get("/mes/api/operations/batch/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testFindOperationsByStatus() throws Exception {
        List<BatchOperation> operations = Arrays.asList(testOperation);
        
        when(batchOperationService.findOperationsByStatus(any())).thenReturn(operations);
        
        mockMvc.perform(get("/mes/api/operations/status/PENDING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testFindOperationsByType() throws Exception {
        List<BatchOperation> operations = Arrays.asList(testOperation);
        
        when(batchOperationService.findOperationsByType(any())).thenReturn(operations);
        
        mockMvc.perform(get("/mes/api/operations/type/INJECTION_MOLDING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
}
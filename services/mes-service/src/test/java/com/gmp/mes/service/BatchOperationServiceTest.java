package com.gmp.mes.service;

import com.gmp.mes.entity.BatchOperation;
import com.gmp.mes.entity.ProductionBatch;
import com.gmp.mes.repository.BatchOperationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 批操作服务类的单元测试
 * 
 * @author gmp-system
 */
class BatchOperationServiceTest {

    @Mock
    private BatchOperationRepository batchOperationRepository;

    @Mock
    private ProductionBatchService productionBatchService;

    @InjectMocks
    private BatchOperationService batchOperationService;

    private BatchOperation testOperation;
    private ProductionBatch testBatch;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // 创建测试批次
        testBatch = new ProductionBatch();
        testBatch.setId(1L);
        testBatch.setBatchNumber("BATCH-2024-001");
        
        // 创建测试操作
        testOperation = new BatchOperation();
        testOperation.setId(1L);
        testOperation.setOperationNumber("OP-2024-001");
        testOperation.setBatchId(1L);
        testOperation.setBatch(testBatch);
        testOperation.setOperationName("注塑成型");
        testOperation.setOperationType(BatchOperation.OperationType.PROCESSING);
        testOperation.setStatus(BatchOperation.OperationStatus.PENDING);
        testOperation.setOperator("操作员1");
        testOperation.setEquipmentId("EQP-001");
        
        // 设置测试参数
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("temperature", 85.5);
        parameters.put("pressure", 120.0);
        parameters.put("time", 300);
        testOperation.setParameters(parameters);
    }

    @Test
    void testCreateBatchOperation() {
        when(productionBatchService.getProductionBatchById(1L)).thenReturn(Optional.of(testBatch));
        when(batchOperationRepository.save(any(BatchOperation.class))).thenReturn(testOperation);
        
        BatchOperation createdOperation = batchOperationService.createBatchOperation(testOperation);
        
        assertNotNull(createdOperation);
        assertEquals("OP-2024-001", createdOperation.getOperationNumber());
        assertEquals(BatchOperation.OperationStatus.PENDING, createdOperation.getStatus());
        verify(batchOperationRepository, times(1)).save(testOperation);
    }

    @Test
    void testGetBatchOperationById() {
        when(batchOperationRepository.findById(1L)).thenReturn(Optional.of(testOperation));
        
        Optional<BatchOperation> foundOperation = batchOperationService.getBatchOperationById(1L);
        
        assertTrue(foundOperation.isPresent());
        assertEquals("OP-2024-001", foundOperation.get().getOperationNumber());
        verify(batchOperationRepository, times(1)).findById(1L);
    }

    @Test
    void testGetBatchOperationByOperationNumber() {
        when(batchOperationRepository.findByOperationNumber("OP-2024-001")).thenReturn(Optional.of(testOperation));
        
        Optional<BatchOperation> foundOperation = batchOperationService.getBatchOperationByOperationNumber("OP-2024-001");
        
        assertTrue(foundOperation.isPresent());
        assertEquals(1L, foundOperation.get().getId());
        verify(batchOperationRepository, times(1)).findByOperationNumber("OP-2024-001");
    }

    @Test
    void testGetAllBatchOperations() {
        List<BatchOperation> operations = Arrays.asList(testOperation);
        when(batchOperationRepository.findAll()).thenReturn(operations);
        
        List<BatchOperation> allOperations = batchOperationService.getAllBatchOperations();
        
        assertNotNull(allOperations);
        assertEquals(1, allOperations.size());
        verify(batchOperationRepository, times(1)).findAll();
    }

    @Test
    void testGetBatchOperationsByBatchId() {
        List<BatchOperation> operations = Arrays.asList(testOperation);
        when(batchOperationRepository.findByBatchId(1L)).thenReturn(operations);
        
        List<BatchOperation> batchOperations = batchOperationService.getBatchOperationsByBatchId(1L);
        
        assertNotNull(batchOperations);
        assertEquals(1, batchOperations.size());
        verify(batchOperationRepository, times(1)).findByBatchId(1L);
    }

    @Test
    void testUpdateBatchOperation() {
        when(batchOperationRepository.findById(1L)).thenReturn(Optional.of(testOperation));
        when(batchOperationRepository.save(any(BatchOperation.class))).thenReturn(testOperation);
        
        testOperation.setOperationName("注塑成型-更新");
        testOperation.setRemark("更新操作说明");
        
        BatchOperation updatedOperation = batchOperationService.updateBatchOperation(testOperation);
        
        assertNotNull(updatedOperation);
        assertEquals("注塑成型-更新", updatedOperation.getOperationName());
        assertEquals("更新操作说明", updatedOperation.getRemark());
        verify(batchOperationRepository, times(1)).save(testOperation);
    }

    @Test
    void testDeleteBatchOperation() {
        when(batchOperationRepository.existsById(1L)).thenReturn(true);
        doNothing().when(batchOperationRepository).deleteById(1L);
        
        batchOperationService.deleteBatchOperation(1L);
        
        verify(batchOperationRepository, times(1)).existsById(1L);
        verify(batchOperationRepository, times(1)).deleteById(1L);
    }

    @Test
    void testStartBatchOperation() {
        when(batchOperationRepository.findById(1L)).thenReturn(Optional.of(testOperation));
        when(batchOperationRepository.save(any(BatchOperation.class))).thenReturn(testOperation);
        
        BatchOperation startedOperation = batchOperationService.startBatchOperation(1L, "操作员2");
        
        assertEquals(BatchOperation.OperationStatus.IN_PROGRESS, startedOperation.getStatus());
        assertNotNull(startedOperation.getActualStartTime());
        assertEquals("操作员2", startedOperation.getOperator());
        verify(batchOperationRepository, times(1)).save(testOperation);
    }

    @Test
    void testCompleteBatchOperation() {
        testOperation.setStatus(BatchOperation.OperationStatus.IN_PROGRESS);
        when(batchOperationRepository.findById(1L)).thenReturn(Optional.of(testOperation));
        when(batchOperationRepository.save(any(BatchOperation.class))).thenReturn(testOperation);
        
        Map<String, Object> results = new HashMap<>();
        results.put("successCount", 50);
        results.put("yieldRate", 98.0);
        
        BatchOperation completedOperation = batchOperationService.completeBatchOperation(1L, results, "操作完成正常");
        
        assertEquals(BatchOperation.OperationStatus.COMPLETED, completedOperation.getStatus());
        assertNotNull(completedOperation.getActualEndTime());
        assertEquals(results, completedOperation.getResults());
        verify(batchOperationRepository, times(1)).save(testOperation);
    }

    @Test
    void testCancelBatchOperation() {
        when(batchOperationRepository.findById(1L)).thenReturn(Optional.of(testOperation));
        when(batchOperationRepository.save(any(BatchOperation.class))).thenReturn(testOperation);
        
        BatchOperation cancelledOperation = batchOperationService.cancelBatchOperation(1L, "取消原因：设备故障");
        
        assertEquals(BatchOperation.OperationStatus.CANCELLED, cancelledOperation.getStatus());
        assertTrue(cancelledOperation.getRemark().contains("取消原因：设备故障"));
        verify(batchOperationRepository, times(1)).save(testOperation);
    }

    @Test
    void testMarkBatchOperationFailed() {
        testOperation.setStatus(BatchOperation.OperationStatus.IN_PROGRESS);
        when(batchOperationRepository.findById(1L)).thenReturn(Optional.of(testOperation));
        when(batchOperationRepository.save(any(BatchOperation.class))).thenReturn(testOperation);
        
        BatchOperation failedOperation = batchOperationService.markBatchOperationFailed(1L, "质量异常", "操作员3");
        
        assertEquals(BatchOperation.OperationStatus.FAILED, failedOperation.getStatus());
        assertTrue(failedOperation.getRemark().contains("质量异常"));
        assertEquals("操作员3", failedOperation.getLastOperator());
        verify(batchOperationRepository, times(1)).save(testOperation);
    }

    @Test
    void testFindBatchOperationsByStatus() {
        List<BatchOperation> operations = Arrays.asList(testOperation);
        when(batchOperationRepository.findByStatus(BatchOperation.OperationStatus.PENDING)).thenReturn(operations);
        
        List<BatchOperation> foundOperations = batchOperationService.findBatchOperationsByStatus(BatchOperation.OperationStatus.PENDING);
        
        assertNotNull(foundOperations);
        assertEquals(1, foundOperations.size());
        verify(batchOperationRepository, times(1)).findByStatus(BatchOperation.OperationStatus.PENDING);
    }

    @Test
    void testGenerateOperationNumber() {
        String operationNumber = batchOperationService.generateOperationNumber();
        
        assertNotNull(operationNumber);
        assertTrue(operationNumber.startsWith("OP-"));
        assertTrue(operationNumber.length() > 10);
    }

    @Test
    void testUpdateOperationParameters() {
        when(batchOperationRepository.findById(1L)).thenReturn(Optional.of(testOperation));
        when(batchOperationRepository.save(any(BatchOperation.class))).thenReturn(testOperation);
        
        Map<String, Object> newParams = new HashMap<>();
        newParams.put("temperature", 88.0);
        newParams.put("pressure", 125.0);
        newParams.put("speed", 150);
        
        BatchOperation updatedOperation = batchOperationService.updateOperationParameters(1L, newParams);
        
        assertEquals(newParams, updatedOperation.getParameters());
        verify(batchOperationRepository, times(1)).save(testOperation);
    }

    @Test
    void testAddOperationRemark() {
        when(batchOperationRepository.findById(1L)).thenReturn(Optional.of(testOperation));
        when(batchOperationRepository.save(any(BatchOperation.class))).thenReturn(testOperation);
        
        BatchOperation updatedOperation = batchOperationService.addOperationRemark(1L, "添加操作备注");
        
        assertTrue(updatedOperation.getRemark().contains("添加操作备注"));
        verify(batchOperationRepository, times(1)).save(testOperation);
    }
}
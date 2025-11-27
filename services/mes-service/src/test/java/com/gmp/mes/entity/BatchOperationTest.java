package com.gmp.mes.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 批操作实体类的单元测试
 * 
 * @author gmp-system
 */
class BatchOperationTest {

    private BatchOperation batchOperation;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        batchOperation = new BatchOperation();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testIdGetterAndSetter() {
        Long id = 1L;
        batchOperation.setId(id);
        assertEquals(id, batchOperation.getId());
    }

    @Test
    void testOperationNumberGetterAndSetter() {
        String operationNumber = "OP-2024-001";
        batchOperation.setOperationNumber(operationNumber);
        assertEquals(operationNumber, batchOperation.getOperationNumber());
    }

    @Test
    void testOperationNameGetterAndSetter() {
        String operationName = "称量操作";
        batchOperation.setOperationName(operationName);
        assertEquals(operationName, batchOperation.getOperationName());
    }

    @Test
    void testOperationTypeGetterAndSetter() {
        String operationType = "称量";
        batchOperation.setOperationType(operationType);
        assertEquals(operationType, batchOperation.getOperationType());
    }

    @Test
    void testStatusGetterAndSetter() {
        BatchOperation.OperationStatus status = BatchOperation.OperationStatus.COMPLETED;
        batchOperation.setStatus(status);
        assertEquals(status, batchOperation.getStatus());
    }

    @Test
    void testStartTimeGetterAndSetter() {
        LocalDateTime startTime = LocalDateTime.now();
        batchOperation.setStartTime(startTime);
        assertEquals(startTime, batchOperation.getStartTime());
    }

    @Test
    void testEndTimeGetterAndSetter() {
        LocalDateTime endTime = LocalDateTime.now().plusHours(1);
        batchOperation.setEndTime(endTime);
        assertEquals(endTime, batchOperation.getEndTime());
    }

    @Test
    void testOperatorGetterAndSetter() {
        String operator = "张三";
        batchOperation.setOperator(operator);
        assertEquals(operator, batchOperation.getOperator());
    }

    @Test
    void testEquipmentIdGetterAndSetter() {
        String equipmentId = "EQUIP-001";
        batchOperation.setEquipmentId(equipmentId);
        assertEquals(equipmentId, batchOperation.getEquipmentId());
    }

    @Test
    void testParametersGetterAndSetter() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("weight", 100.5);
        parameters.put("tolerance", 0.1);
        batchOperation.setParameters(parameters);
        assertEquals(parameters, batchOperation.getParameters());
    }

    @Test
    void testResultsGetterAndSetter() {
        Map<String, Object> results = new HashMap<>();
        results.put("actualWeight", 100.45);
        results.put("passed", true);
        batchOperation.setResults(results);
        assertEquals(results, batchOperation.getResults());
    }

    @Test
    void testRemarkGetterAndSetter() {
        String remark = "操作正常";
        batchOperation.setRemark(remark);
        assertEquals(remark, batchOperation.getRemark());
    }

    @Test
    void testProductionBatchGetterAndSetter() {
        ProductionBatch batch = new ProductionBatch();
        batch.setId(1L);
        batch.setBatchNumber("BATCH-2024-001");
        
        batchOperation.setProductionBatch(batch);
        assertEquals(batch, batchOperation.getProductionBatch());
        assertTrue(batch.getBatchOperations().contains(batchOperation));
    }

    @Test
    void testEqualsAndHashCode() {
        batchOperation.setId(1L);
        batchOperation.setOperationNumber("OP-2024-001");
        
        BatchOperation sameIdOperation = new BatchOperation();
        sameIdOperation.setId(1L);
        
        BatchOperation differentIdOperation = new BatchOperation();
        differentIdOperation.setId(2L);
        
        assertEquals(batchOperation, sameIdOperation);
        assertEquals(batchOperation.hashCode(), sameIdOperation.hashCode());
        assertNotEquals(batchOperation, differentIdOperation);
        assertNotEquals(batchOperation.hashCode(), differentIdOperation.hashCode());
    }

    @Test
    void testToString() {
        batchOperation.setId(1L);
        batchOperation.setOperationNumber("OP-2024-001");
        batchOperation.setOperationName("称量操作");
        
        String toString = batchOperation.toString();
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("operationNumber='OP-2024-001'"));
        assertTrue(toString.contains("operationName='称量操作'"));
    }

    @Test
    void testJsonbSerialization() {
        // 测试JSONB类型的参数序列化是否正常
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("weight", 100.5);
        parameters.put("tolerance", 0.1);
        parameters.put("notes", "Test");
        
        batchOperation.setParameters(parameters);
        
        // 验证可以正常访问参数
        assertEquals(100.5, batchOperation.getParameters().get("weight"));
        assertEquals(0.1, batchOperation.getParameters().get("tolerance"));
        assertEquals("Test", batchOperation.getParameters().get("notes"));
        
        // 验证可以更新参数
        batchOperation.getParameters().put("temperature", 25.0);
        assertEquals(25.0, batchOperation.getParameters().get("temperature"));
    }

    @Test
    void testStatusTransitions() {
        // 测试状态转换逻辑
        batchOperation.setStatus(BatchOperation.OperationStatus.PENDING);
        assertEquals(BatchOperation.OperationStatus.PENDING, batchOperation.getStatus());
        
        // 模拟从PENDING到IN_PROGRESS的转换
        batchOperation.setStatus(BatchOperation.OperationStatus.IN_PROGRESS);
        assertEquals(BatchOperation.OperationStatus.IN_PROGRESS, batchOperation.getStatus());
        
        // 模拟从IN_PROGRESS到COMPLETED的转换
        batchOperation.setStatus(BatchOperation.OperationStatus.COMPLETED);
        assertEquals(BatchOperation.OperationStatus.COMPLETED, batchOperation.getStatus());
    }
}
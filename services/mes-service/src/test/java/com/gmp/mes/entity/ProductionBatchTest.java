package com.gmp.mes.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 生产批次实体类的单元测试
 * 
 * @author gmp-system
 */
class ProductionBatchTest {

    private ProductionBatch productionBatch;

    @BeforeEach
    void setUp() {
        productionBatch = new ProductionBatch();
    }

    @Test
    void testIdGetterAndSetter() {
        Long id = 1L;
        productionBatch.setId(id);
        assertEquals(id, productionBatch.getId());
    }

    @Test
    void testBatchNumberGetterAndSetter() {
        String batchNumber = "BATCH-2024-001";
        productionBatch.setBatchNumber(batchNumber);
        assertEquals(batchNumber, productionBatch.getBatchNumber());
    }

    @Test
    void testProductIdGetterAndSetter() {
        String productId = "PROD-001";
        productionBatch.setProductId(productId);
        assertEquals(productId, productionBatch.getProductId());
    }

    @Test
    void testProductNameGetterAndSetter() {
        String productName = "Test Product";
        productionBatch.setProductName(productName);
        assertEquals(productName, productionBatch.getProductName());
    }

    @Test
    void testEquipmentIdGetterAndSetter() {
        String equipmentId = "EQUIP-001";
        productionBatch.setEquipmentId(equipmentId);
        assertEquals(equipmentId, productionBatch.getEquipmentId());
    }

    @Test
    void testEquipmentNameGetterAndSetter() {
        String equipmentName = "Test Equipment";
        productionBatch.setEquipmentName(equipmentName);
        assertEquals(equipmentName, productionBatch.getEquipmentName());
    }

    @Test
    void testBatchSizeGetterAndSetter() {
        Integer batchSize = 50;
        productionBatch.setBatchSize(batchSize);
        assertEquals(batchSize, productionBatch.getBatchSize());
    }

    @Test
    void testGoodQuantityGetterAndSetter() {
        Integer goodQuantity = 45;
        productionBatch.setGoodQuantity(goodQuantity);
        assertEquals(goodQuantity, productionBatch.getGoodQuantity());
    }

    @Test
    void testRejectQuantityGetterAndSetter() {
        Integer rejectQuantity = 5;
        productionBatch.setRejectQuantity(rejectQuantity);
        assertEquals(rejectQuantity, productionBatch.getRejectQuantity());
    }

    @Test
    void testStatusGetterAndSetter() {
        ProductionBatch.BatchStatus status = ProductionBatch.BatchStatus.IN_PROGRESS;
        productionBatch.setStatus(status);
        assertEquals(status, productionBatch.getStatus());
    }

    @Test
    void testStartTimeGetterAndSetter() {
        LocalDateTime startTime = LocalDateTime.now();
        productionBatch.setStartTime(startTime);
        assertEquals(startTime, productionBatch.getStartTime());
    }

    @Test
    void testEndTimeGetterAndSetter() {
        LocalDateTime endTime = LocalDateTime.now().plusHours(2);
        productionBatch.setEndTime(endTime);
        assertEquals(endTime, productionBatch.getEndTime());
    }

    @Test
    void testOperatorGetterAndSetter() {
        String operator = "张三";
        productionBatch.setOperator(operator);
        assertEquals(operator, productionBatch.getOperator());
    }

    @Test
    void testRemarkGetterAndSetter() {
        String remark = "测试批次";
        productionBatch.setRemark(remark);
        assertEquals(remark, productionBatch.getRemark());
    }

    @Test
    void testProductionOrderGetterAndSetter() {
        ProductionOrder order = new ProductionOrder();
        order.setId(1L);
        order.setOrderNumber("ORD-2024-001");
        
        productionBatch.setProductionOrder(order);
        assertEquals(order, productionBatch.getProductionOrder());
        assertTrue(order.getProductionBatches().contains(productionBatch));
    }

    @Test
    void testBatchOperationsGetterAndSetter() {
        Set<BatchOperation> operations = new HashSet<>();
        BatchOperation operation = new BatchOperation();
        operation.setId(1L);
        operation.setOperationNumber("OP-001");
        operations.add(operation);
        
        productionBatch.setBatchOperations(operations);
        assertEquals(operations, productionBatch.getBatchOperations());
        assertTrue(productionBatch.getBatchOperations().contains(operation));
    }

    @Test
    void testAddBatchOperation() {
        BatchOperation operation = new BatchOperation();
        operation.setId(1L);
        operation.setOperationNumber("OP-001");
        
        productionBatch.addBatchOperation(operation);
        assertTrue(productionBatch.getBatchOperations().contains(operation));
        assertSame(productionBatch, operation.getProductionBatch());
    }

    @Test
    void testRemoveBatchOperation() {
        BatchOperation operation = new BatchOperation();
        operation.setId(1L);
        operation.setOperationNumber("OP-001");
        
        productionBatch.addBatchOperation(operation);
        assertTrue(productionBatch.getBatchOperations().contains(operation));
        
        productionBatch.removeBatchOperation(operation);
        assertFalse(productionBatch.getBatchOperations().contains(operation));
        assertNull(operation.getProductionBatch());
    }

    @Test
    void testEqualsAndHashCode() {
        productionBatch.setId(1L);
        productionBatch.setBatchNumber("BATCH-2024-001");
        
        ProductionBatch sameIdBatch = new ProductionBatch();
        sameIdBatch.setId(1L);
        
        ProductionBatch differentIdBatch = new ProductionBatch();
        differentIdBatch.setId(2L);
        
        assertEquals(productionBatch, sameIdBatch);
        assertEquals(productionBatch.hashCode(), sameIdBatch.hashCode());
        assertNotEquals(productionBatch, differentIdBatch);
        assertNotEquals(productionBatch.hashCode(), differentIdBatch.hashCode());
    }

    @Test
    void testToString() {
        productionBatch.setId(1L);
        productionBatch.setBatchNumber("BATCH-2024-001");
        productionBatch.setProductName("Test Product");
        
        String toString = productionBatch.toString();
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("batchNumber='BATCH-2024-001'"));
        assertTrue(toString.contains("productName='Test Product'"));
    }
}
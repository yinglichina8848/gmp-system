package com.gmp.mes.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 生产订单实体类的单元测试
 * 
 * @author gmp-system
 */
class ProductionOrderTest {

    private ProductionOrder productionOrder;

    @BeforeEach
    void setUp() {
        productionOrder = new ProductionOrder();
    }

    @Test
    void testIdGetterAndSetter() {
        Long id = 1L;
        productionOrder.setId(id);
        assertEquals(id, productionOrder.getId());
    }

    @Test
    void testOrderNumberGetterAndSetter() {
        String orderNumber = "ORD-2024-001";
        productionOrder.setOrderNumber(orderNumber);
        assertEquals(orderNumber, productionOrder.getOrderNumber());
    }

    @Test
    void testProductIdGetterAndSetter() {
        String productId = "PROD-001";
        productionOrder.setProductId(productId);
        assertEquals(productId, productionOrder.getProductId());
    }

    @Test
    void testProductNameGetterAndSetter() {
        String productName = "Test Product";
        productionOrder.setProductName(productName);
        assertEquals(productName, productionOrder.getProductName());
    }

    @Test
    void testQuantityGetterAndSetter() {
        Integer quantity = 100;
        productionOrder.setQuantity(quantity);
        assertEquals(quantity, productionOrder.getQuantity());
    }

    @Test
    void testUnitGetterAndSetter() {
        String unit = "pcs";
        productionOrder.setUnit(unit);
        assertEquals(unit, productionOrder.getUnit());
    }

    @Test
    void testPlanStartTimeGetterAndSetter() {
        LocalDateTime planStartTime = LocalDateTime.now();
        productionOrder.setPlanStartTime(planStartTime);
        assertEquals(planStartTime, productionOrder.getPlanStartTime());
    }

    @Test
    void testPlanEndTimeGetterAndSetter() {
        LocalDateTime planEndTime = LocalDateTime.now().plusDays(1);
        productionOrder.setPlanEndTime(planEndTime);
        assertEquals(planEndTime, productionOrder.getPlanEndTime());
    }

    @Test
    void testActualStartTimeGetterAndSetter() {
        LocalDateTime actualStartTime = LocalDateTime.now();
        productionOrder.setActualStartTime(actualStartTime);
        assertEquals(actualStartTime, productionOrder.getActualStartTime());
    }

    @Test
    void testActualEndTimeGetterAndSetter() {
        LocalDateTime actualEndTime = LocalDateTime.now().plusDays(1);
        productionOrder.setActualEndTime(actualEndTime);
        assertEquals(actualEndTime, productionOrder.getActualEndTime());
    }

    @Test
    void testStatusGetterAndSetter() {
        ProductionOrder.OrderStatus status = ProductionOrder.OrderStatus.COMPLETED;
        productionOrder.setStatus(status);
        assertEquals(status, productionOrder.getStatus());
    }

    @Test
    void testPriorityGetterAndSetter() {
        ProductionOrder.OrderPriority priority = ProductionOrder.OrderPriority.HIGH;
        productionOrder.setPriority(priority);
        assertEquals(priority, productionOrder.getPriority());
    }

    @Test
    void testApplicantGetterAndSetter() {
        String applicant = "张三";
        productionOrder.setApplicant(applicant);
        assertEquals(applicant, productionOrder.getApplicant());
    }

    @Test
    void testApproverGetterAndSetter() {
        String approver = "李四";
        productionOrder.setApprover(approver);
        assertEquals(approver, productionOrder.getApprover());
    }

    @Test
    void testDepartmentGetterAndSetter() {
        String department = "生产部";
        productionOrder.setDepartment(department);
        assertEquals(department, productionOrder.getDepartment());
    }

    @Test
    void testRemarkGetterAndSetter() {
        String remark = "测试订单";
        productionOrder.setRemark(remark);
        assertEquals(remark, productionOrder.getRemark());
    }

    @Test
    void testProductionBatchesGetterAndSetter() {
        Set<ProductionBatch> batches = new HashSet<>();
        ProductionBatch batch = new ProductionBatch();
        batch.setId(1L);
        batch.setBatchNumber("BATCH-001");
        batches.add(batch);
        
        productionOrder.setProductionBatches(batches);
        assertEquals(batches, productionOrder.getProductionBatches());
        assertTrue(productionOrder.getProductionBatches().contains(batch));
    }

    @Test
    void testAddProductionBatch() {
        ProductionBatch batch = new ProductionBatch();
        batch.setId(1L);
        batch.setBatchNumber("BATCH-001");
        
        productionOrder.addProductionBatch(batch);
        assertTrue(productionOrder.getProductionBatches().contains(batch));
        assertSame(productionOrder, batch.getProductionOrder());
    }

    @Test
    void testRemoveProductionBatch() {
        ProductionBatch batch = new ProductionBatch();
        batch.setId(1L);
        batch.setBatchNumber("BATCH-001");
        
        productionOrder.addProductionBatch(batch);
        assertTrue(productionOrder.getProductionBatches().contains(batch));
        
        productionOrder.removeProductionBatch(batch);
        assertFalse(productionOrder.getProductionBatches().contains(batch));
        assertNull(batch.getProductionOrder());
    }

    @Test
    void testEqualsAndHashCode() {
        productionOrder.setId(1L);
        productionOrder.setOrderNumber("ORD-2024-001");
        
        ProductionOrder sameIdOrder = new ProductionOrder();
        sameIdOrder.setId(1L);
        
        ProductionOrder differentIdOrder = new ProductionOrder();
        differentIdOrder.setId(2L);
        
        assertEquals(productionOrder, sameIdOrder);
        assertEquals(productionOrder.hashCode(), sameIdOrder.hashCode());
        assertNotEquals(productionOrder, differentIdOrder);
        assertNotEquals(productionOrder.hashCode(), differentIdOrder.hashCode());
    }

    @Test
    void testToString() {
        productionOrder.setId(1L);
        productionOrder.setOrderNumber("ORD-2024-001");
        productionOrder.setProductName("Test Product");
        
        String toString = productionOrder.toString();
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("orderNumber='ORD-2024-001'"));
        assertTrue(toString.contains("productName='Test Product'"));
    }
}
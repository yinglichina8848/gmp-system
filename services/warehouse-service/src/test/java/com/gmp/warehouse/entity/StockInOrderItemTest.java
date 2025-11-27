package com.gmp.warehouse.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * StockInOrderItem实体类的单元测试
 *
 * @author GMP系统开发团队
 * @version 1.0.0
 * @since 2024-01-01
 */
class StockInOrderItemTest {

    private StockInOrderItem stockInOrderItem;

    @BeforeEach
    void setUp() {
        stockInOrderItem = new StockInOrderItem();
    }

    @Test
    void testSetAndGetId() {
        Long id = 1L;
        stockInOrderItem.setId(id);
        assertEquals(id, stockInOrderItem.getId());
    }

    @Test
    void testSetAndGetStockInOrder() {
        StockInOrder stockInOrder = new StockInOrder();
        stockInOrder.setId(1L);
        stockInOrder.setOrderNo("IN202401010001");
        stockInOrderItem.setStockInOrder(stockInOrder);
        assertEquals(stockInOrder, stockInOrderItem.getStockInOrder());
        assertEquals(1L, stockInOrderItem.getStockInOrder().getId());
    }

    @Test
    void testSetAndGetMaterial() {
        Material material = new Material();
        material.setId(1L);
        material.setCode("MAT001");
        material.setName("测试物料");
        stockInOrderItem.setMaterial(material);
        assertEquals(material, stockInOrderItem.getMaterial());
        assertEquals(1L, stockInOrderItem.getMaterial().getId());
    }

    @Test
    void testSetAndGetQuantity() {
        BigDecimal quantity = new BigDecimal(100);
        stockInOrderItem.setQuantity(quantity);
        assertEquals(quantity, stockInOrderItem.getQuantity());
    }

    @Test
    void testSetAndGetActualQuantity() {
        BigDecimal actualQuantity = new BigDecimal(99);
        stockInOrderItem.setActualQuantity(actualQuantity);
        assertEquals(actualQuantity, stockInOrderItem.getActualQuantity());
    }

    @Test
    void testSetAndGetUnitCost() {
        BigDecimal unitCost = new BigDecimal(10.5);
        stockInOrderItem.setUnitCost(unitCost);
        assertEquals(unitCost, stockInOrderItem.getUnitCost());
    }

    @Test
    void testSetAndGetAmount() {
        BigDecimal amount = new BigDecimal(1050);
        stockInOrderItem.setAmount(amount);
        assertEquals(amount, stockInOrderItem.getAmount());
    }

    @Test
    void testSetAndGetUnit() {
        String unit = "个";
        stockInOrderItem.setUnit(unit);
        assertEquals(unit, stockInOrderItem.getUnit());
    }

    @Test
    void testSetAndGetSpecification() {
        String specification = "规格A";
        stockInOrderItem.setSpecification(specification);
        assertEquals(specification, stockInOrderItem.getSpecification());
    }

    @Test
    void testSetAndGetBatchNo() {
        String batchNo = "BATCH20240101001";
        stockInOrderItem.setBatchNo(batchNo);
        assertEquals(batchNo, stockInOrderItem.getBatchNo());
    }

    @Test
    void testSetAndGetProductionDate() {
        LocalDateTime productionDate = LocalDateTime.now().minusDays(30);
        stockInOrderItem.setProductionDate(productionDate);
        assertEquals(productionDate, stockInOrderItem.getProductionDate());
    }

    @Test
    void testSetAndGetExpiryDate() {
        LocalDateTime expiryDate = LocalDateTime.now().plusDays(365);
        stockInOrderItem.setExpiryDate(expiryDate);
        assertEquals(expiryDate, stockInOrderItem.getExpiryDate());
    }

    @Test
    void testSetAndGetWarehouse() {
        Warehouse warehouse = new Warehouse();
        warehouse.setId(1L);
        warehouse.setName("一号仓库");
        stockInOrderItem.setWarehouse(warehouse);
        assertEquals(warehouse, stockInOrderItem.getWarehouse());
        assertEquals(1L, stockInOrderItem.getWarehouse().getId());
    }

    @Test
    void testSetAndGetLocation() {
        Location location = new Location();
        location.setId(1L);
        location.setName("A区01货架");
        stockInOrderItem.setLocation(location);
        assertEquals(location, stockInOrderItem.getLocation());
        assertEquals(1L, stockInOrderItem.getLocation().getId());
    }

    @Test
    void testSetAndGetQualityStatus() {
        String qualityStatus = "合格";
        stockInOrderItem.setQualityStatus(qualityStatus);
        assertEquals(qualityStatus, stockInOrderItem.getQualityStatus());
    }

    @Test
    void testSetAndGetRemark() {
        String remark = "备注信息";
        stockInOrderItem.setRemark(remark);
        assertEquals(remark, stockInOrderItem.getRemark());
    }

    @Test
    void testSetAndGetCreatedAt() {
        LocalDateTime createdAt = LocalDateTime.now();
        stockInOrderItem.setCreatedAt(createdAt);
        assertEquals(createdAt, stockInOrderItem.getCreatedAt());
    }

    @Test
    void testSetAndGetCreatedBy() {
        String createdBy = "admin";
        stockInOrderItem.setCreatedBy(createdBy);
        assertEquals(createdBy, stockInOrderItem.getCreatedBy());
    }

    @Test
    void testSetAndGetUpdatedAt() {
        LocalDateTime updatedAt = LocalDateTime.now();
        stockInOrderItem.setUpdatedAt(updatedAt);
        assertEquals(updatedAt, stockInOrderItem.getUpdatedAt());
    }

    @Test
    void testSetAndGetUpdatedBy() {
        String updatedBy = "admin";
        stockInOrderItem.setUpdatedBy(updatedBy);
        assertEquals(updatedBy, stockInOrderItem.getUpdatedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        StockInOrderItem item1 = new StockInOrderItem();
        item1.setId(1L);
        
        StockInOrderItem item2 = new StockInOrderItem();
        item2.setId(1L);
        
        StockInOrderItem item3 = new StockInOrderItem();
        item3.setId(2L);

        assertEquals(item1, item2);
        assertNotEquals(item1, item3);
        assertEquals(item1.hashCode(), item2.hashCode());
    }

}
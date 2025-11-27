package com.gmp.warehouse.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * StockOutOrderItem实体类的单元测试
 *
 * @author GMP系统开发团队
 * @version 1.0.0
 * @since 2024-01-01
 */
class StockOutOrderItemTest {

    private StockOutOrderItem stockOutOrderItem;

    @BeforeEach
    void setUp() {
        stockOutOrderItem = new StockOutOrderItem();
    }

    @Test
    void testSetAndGetId() {
        Long id = 1L;
        stockOutOrderItem.setId(id);
        assertEquals(id, stockOutOrderItem.getId());
    }

    @Test
    void testSetAndGetStockOutOrder() {
        StockOutOrder stockOutOrder = new StockOutOrder();
        stockOutOrder.setId(1L);
        stockOutOrder.setOrderNo("OUT202401010001");
        stockOutOrderItem.setStockOutOrder(stockOutOrder);
        assertEquals(stockOutOrder, stockOutOrderItem.getStockOutOrder());
        assertEquals(1L, stockOutOrderItem.getStockOutOrder().getId());
    }

    @Test
    void testSetAndGetMaterial() {
        Material material = new Material();
        material.setId(1L);
        material.setCode("MAT001");
        material.setName("测试物料");
        stockOutOrderItem.setMaterial(material);
        assertEquals(material, stockOutOrderItem.getMaterial());
        assertEquals(1L, stockOutOrderItem.getMaterial().getId());
    }

    @Test
    void testSetAndGetQuantity() {
        BigDecimal quantity = new BigDecimal(100);
        stockOutOrderItem.setQuantity(quantity);
        assertEquals(quantity, stockOutOrderItem.getQuantity());
    }

    @Test
    void testSetAndGetActualQuantity() {
        BigDecimal actualQuantity = new BigDecimal(99);
        stockOutOrderItem.setActualQuantity(actualQuantity);
        assertEquals(actualQuantity, stockOutOrderItem.getActualQuantity());
    }

    @Test
    void testSetAndGetUnitCost() {
        BigDecimal unitCost = new BigDecimal(10.5);
        stockOutOrderItem.setUnitCost(unitCost);
        assertEquals(unitCost, stockOutOrderItem.getUnitCost());
    }

    @Test
    void testSetAndGetAmount() {
        BigDecimal amount = new BigDecimal(1050);
        stockOutOrderItem.setAmount(amount);
        assertEquals(amount, stockOutOrderItem.getAmount());
    }

    @Test
    void testSetAndGetUnit() {
        String unit = "个";
        stockOutOrderItem.setUnit(unit);
        assertEquals(unit, stockOutOrderItem.getUnit());
    }

    @Test
    void testSetAndGetSpecification() {
        String specification = "规格A";
        stockOutOrderItem.setSpecification(specification);
        assertEquals(specification, stockOutOrderItem.getSpecification());
    }

    @Test
    void testSetAndGetBatchNo() {
        String batchNo = "BATCH20240101001";
        stockOutOrderItem.setBatchNo(batchNo);
        assertEquals(batchNo, stockOutOrderItem.getBatchNo());
    }

    @Test
    void testSetAndGetWarehouse() {
        Warehouse warehouse = new Warehouse();
        warehouse.setId(1L);
        warehouse.setName("一号仓库");
        stockOutOrderItem.setWarehouse(warehouse);
        assertEquals(warehouse, stockOutOrderItem.getWarehouse());
        assertEquals(1L, stockOutOrderItem.getWarehouse().getId());
    }

    @Test
    void testSetAndGetLocation() {
        Location location = new Location();
        location.setId(1L);
        location.setName("A区01货架");
        stockOutOrderItem.setLocation(location);
        assertEquals(location, stockOutOrderItem.getLocation());
        assertEquals(1L, stockOutOrderItem.getLocation().getId());
    }

    @Test
    void testSetAndGetInventory() {
        Inventory inventory = new Inventory();
        inventory.setId(1L);
        stockOutOrderItem.setInventory(inventory);
        assertEquals(inventory, stockOutOrderItem.getInventory());
        assertEquals(1L, stockOutOrderItem.getInventory().getId());
    }

    @Test
    void testSetAndGetRemark() {
        String remark = "备注信息";
        stockOutOrderItem.setRemark(remark);
        assertEquals(remark, stockOutOrderItem.getRemark());
    }

    @Test
    void testSetAndGetCreatedAt() {
        LocalDateTime createdAt = LocalDateTime.now();
        stockOutOrderItem.setCreatedAt(createdAt);
        assertEquals(createdAt, stockOutOrderItem.getCreatedAt());
    }

    @Test
    void testSetAndGetCreatedBy() {
        String createdBy = "admin";
        stockOutOrderItem.setCreatedBy(createdBy);
        assertEquals(createdBy, stockOutOrderItem.getCreatedBy());
    }

    @Test
    void testSetAndGetUpdatedAt() {
        LocalDateTime updatedAt = LocalDateTime.now();
        stockOutOrderItem.setUpdatedAt(updatedAt);
        assertEquals(updatedAt, stockOutOrderItem.getUpdatedAt());
    }

    @Test
    void testSetAndGetUpdatedBy() {
        String updatedBy = "admin";
        stockOutOrderItem.setUpdatedBy(updatedBy);
        assertEquals(updatedBy, stockOutOrderItem.getUpdatedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        StockOutOrderItem item1 = new StockOutOrderItem();
        item1.setId(1L);
        
        StockOutOrderItem item2 = new StockOutOrderItem();
        item2.setId(1L);
        
        StockOutOrderItem item3 = new StockOutOrderItem();
        item3.setId(2L);

        assertEquals(item1, item2);
        assertNotEquals(item1, item3);
        assertEquals(item1.hashCode(), item2.hashCode());
    }

}
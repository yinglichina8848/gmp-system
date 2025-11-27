package com.gmp.warehouse.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Inventory实体类的单元测试
 *
 * @author GMP系统开发团队
 * @version 1.0.0
 * @since 2024-01-01
 */
class InventoryTest {

    private Inventory inventory;

    @BeforeEach
    void setUp() {
        inventory = new Inventory();
    }

    @Test
    void testSetAndGetId() {
        Long id = 1L;
        inventory.setId(id);
        assertEquals(id, inventory.getId());
    }

    @Test
    void testSetAndGetMaterial() {
        Material material = new Material();
        material.setId(1L);
        material.setCode("MAT001");
        material.setName("测试物料");
        inventory.setMaterial(material);
        assertEquals(material, inventory.getMaterial());
        assertEquals(1L, inventory.getMaterial().getId());
    }

    @Test
    void testSetAndGetWarehouse() {
        Warehouse warehouse = new Warehouse();
        warehouse.setId(1L);
        warehouse.setName("一号仓库");
        inventory.setWarehouse(warehouse);
        assertEquals(warehouse, inventory.getWarehouse());
        assertEquals(1L, inventory.getWarehouse().getId());
    }

    @Test
    void testSetAndGetLocation() {
        Location location = new Location();
        location.setId(1L);
        location.setName("A区01货架");
        inventory.setLocation(location);
        assertEquals(location, inventory.getLocation());
        assertEquals(1L, inventory.getLocation().getId());
    }

    @Test
    void testSetAndGetCurrentQuantity() {
        BigDecimal currentQuantity = new BigDecimal(1000);
        inventory.setCurrentQuantity(currentQuantity);
        assertEquals(currentQuantity, inventory.getCurrentQuantity());
    }

    @Test
    void testSetAndGetAvailableQuantity() {
        BigDecimal availableQuantity = new BigDecimal(800);
        inventory.setAvailableQuantity(availableQuantity);
        assertEquals(availableQuantity, inventory.getAvailableQuantity());
    }

    @Test
    void testSetAndGetReservedQuantity() {
        BigDecimal reservedQuantity = new BigDecimal(200);
        inventory.setReservedQuantity(reservedQuantity);
        assertEquals(reservedQuantity, inventory.getReservedQuantity());
    }

    @Test
    void testSetAndGetUnitCost() {
        BigDecimal unitCost = new BigDecimal(10.5);
        inventory.setUnitCost(unitCost);
        assertEquals(unitCost, inventory.getUnitCost());
    }

    @Test
    void testSetAndGetTotalCost() {
        BigDecimal totalCost = new BigDecimal(10500);
        inventory.setTotalCost(totalCost);
        assertEquals(totalCost, inventory.getTotalCost());
    }

    @Test
    void testSetAndGetBatchNo() {
        String batchNo = "BATCH20240101001";
        inventory.setBatchNo(batchNo);
        assertEquals(batchNo, inventory.getBatchNo());
    }

    @Test
    void testSetAndGetProductionDate() {
        LocalDateTime productionDate = LocalDateTime.now().minusDays(30);
        inventory.setProductionDate(productionDate);
        assertEquals(productionDate, inventory.getProductionDate());
    }

    @Test
    void testSetAndGetExpiryDate() {
        LocalDateTime expiryDate = LocalDateTime.now().plusDays(365);
        inventory.setExpiryDate(expiryDate);
        assertEquals(expiryDate, inventory.getExpiryDate());
    }

    @Test
    void testSetAndGetSupplier() {
        Supplier supplier = new Supplier();
        supplier.setId(1L);
        supplier.setName("测试供应商");
        inventory.setSupplier(supplier);
        assertEquals(supplier, inventory.getSupplier());
        assertEquals(1L, inventory.getSupplier().getId());
    }

    @Test
    void testSetAndGetLotNo() {
        String lotNo = "LOT001";
        inventory.setLotNo(lotNo);
        assertEquals(lotNo, inventory.getLotNo());
    }

    @Test
    void testSetAndGetQualityStatus() {
        String qualityStatus = "合格";
        inventory.setQualityStatus(qualityStatus);
        assertEquals(qualityStatus, inventory.getQualityStatus());
    }

    @Test
    void testSetAndGetStorageCondition() {
        String storageCondition = "常温干燥";
        inventory.setStorageCondition(storageCondition);
        assertEquals(storageCondition, inventory.getStorageCondition());
    }

    @Test
    void testSetAndGetRemark() {
        String remark = "备注信息";
        inventory.setRemark(remark);
        assertEquals(remark, inventory.getRemark());
    }

    @Test
    void testSetAndGetCreatedAt() {
        LocalDateTime createdAt = LocalDateTime.now();
        inventory.setCreatedAt(createdAt);
        assertEquals(createdAt, inventory.getCreatedAt());
    }

    @Test
    void testSetAndGetCreatedBy() {
        String createdBy = "admin";
        inventory.setCreatedBy(createdBy);
        assertEquals(createdBy, inventory.getCreatedBy());
    }

    @Test
    void testSetAndGetUpdatedAt() {
        LocalDateTime updatedAt = LocalDateTime.now();
        inventory.setUpdatedAt(updatedAt);
        assertEquals(updatedAt, inventory.getUpdatedAt());
    }

    @Test
    void testSetAndGetUpdatedBy() {
        String updatedBy = "admin";
        inventory.setUpdatedBy(updatedBy);
        assertEquals(updatedBy, inventory.getUpdatedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        Inventory inventory1 = new Inventory();
        inventory1.setId(1L);
        
        Inventory inventory2 = new Inventory();
        inventory2.setId(1L);
        
        Inventory inventory3 = new Inventory();
        inventory3.setId(2L);

        assertEquals(inventory1, inventory2);
        assertNotEquals(inventory1, inventory3);
        assertEquals(inventory1.hashCode(), inventory2.hashCode());
    }

}
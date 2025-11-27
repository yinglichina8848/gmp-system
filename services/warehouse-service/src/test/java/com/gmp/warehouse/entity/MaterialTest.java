package com.gmp.warehouse.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Material实体类的单元测试
 *
 * @author GMP系统开发团队
 * @version 1.0.0
 * @since 2024-01-01
 */
class MaterialTest {

    private Material material;

    @BeforeEach
    void setUp() {
        material = new Material();
    }

    @Test
    void testSetAndGetId() {
        Long id = 1L;
        material.setId(id);
        assertEquals(id, material.getId());
    }

    @Test
    void testSetAndGetCode() {
        String code = "MAT001";
        material.setCode(code);
        assertEquals(code, material.getCode());
    }

    @Test
    void testSetAndGetName() {
        String name = "测试物料";
        material.setName(name);
        assertEquals(name, material.getName());
    }

    @Test
    void testSetAndGetDescription() {
        String description = "测试物料描述";
        material.setDescription(description);
        assertEquals(description, material.getDescription());
    }

    @Test
    void testSetAndGetCategory() {
        MaterialCategory category = new MaterialCategory();
        category.setId(1L);
        category.setName("电子元器件");
        material.setCategory(category);
        assertEquals(category, material.getCategory());
        assertEquals(1L, material.getCategory().getId());
    }

    @Test
    void testSetAndGetUnit() {
        String unit = "个";
        material.setUnit(unit);
        assertEquals(unit, material.getUnit());
    }

    @Test
    void testSetAndGetSpecification() {
        String specification = "规格A";
        material.setSpecification(specification);
        assertEquals(specification, material.getSpecification());
    }

    @Test
    void testSetAndGetSupplier() {
        Supplier supplier = new Supplier();
        supplier.setId(1L);
        supplier.setName("测试供应商");
        material.setSupplier(supplier);
        assertEquals(supplier, material.getSupplier());
        assertEquals(1L, material.getSupplier().getId());
    }

    @Test
    void testSetAndGetSafetyStock() {
        BigDecimal safetyStock = new BigDecimal(100);
        material.setSafetyStock(safetyStock);
        assertEquals(safetyStock, material.getSafetyStock());
    }

    @Test
    void testSetAndGetWarningStock() {
        BigDecimal warningStock = new BigDecimal(50);
        material.setWarningStock(warningStock);
        assertEquals(warningStock, material.getWarningStock());
    }

    @Test
    void testSetAndGetBatchManaged() {
        Boolean batchManaged = true;
        material.setBatchManaged(batchManaged);
        assertEquals(batchManaged, material.getBatchManaged());
    }

    @Test
    void testSetAndGetExpiryManaged() {
        Boolean expiryManaged = true;
        material.setExpiryManaged(expiryManaged);
        assertEquals(expiryManaged, material.getExpiryManaged());
    }

    @Test
    void testSetAndGetStorageCondition() {
        String storageCondition = "常温干燥";
        material.setStorageCondition(storageCondition);
        assertEquals(storageCondition, material.getStorageCondition());
    }

    @Test
    void testSetAndGetRemark() {
        String remark = "备注信息";
        material.setRemark(remark);
        assertEquals(remark, material.getRemark());
    }

    @Test
    void testSetAndGetStatus() {
        Integer status = 1;
        material.setStatus(status);
        assertEquals(status, material.getStatus());
    }

    @Test
    void testSetAndGetCreatedAt() {
        LocalDateTime createdAt = LocalDateTime.now();
        material.setCreatedAt(createdAt);
        assertEquals(createdAt, material.getCreatedAt());
    }

    @Test
    void testSetAndGetCreatedBy() {
        String createdBy = "admin";
        material.setCreatedBy(createdBy);
        assertEquals(createdBy, material.getCreatedBy());
    }

    @Test
    void testSetAndGetUpdatedAt() {
        LocalDateTime updatedAt = LocalDateTime.now();
        material.setUpdatedAt(updatedAt);
        assertEquals(updatedAt, material.getUpdatedAt());
    }

    @Test
    void testSetAndGetUpdatedBy() {
        String updatedBy = "admin";
        material.setUpdatedBy(updatedBy);
        assertEquals(updatedBy, material.getUpdatedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        Material material1 = new Material();
        material1.setId(1L);
        material1.setCode("MAT001");

        Material material2 = new Material();
        material2.setId(1L);
        material2.setCode("MAT001");

        Material material3 = new Material();
        material3.setId(2L);
        material3.setCode("MAT002");

        assertEquals(material1, material2);
        assertNotEquals(material1, material3);
        assertEquals(material1.hashCode(), material2.hashCode());
    }

}
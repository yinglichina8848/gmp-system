package com.gmp.warehouse.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Warehouse实体类的单元测试
 *
 * @author GMP系统开发团队
 * @version 1.0.0
 * @since 2024-01-01
 */
class WarehouseTest {

    private Warehouse warehouse;

    @BeforeEach
    void setUp() {
        warehouse = new Warehouse();
    }

    @Test
    void testSetAndGetId() {
        Long id = 1L;
        warehouse.setId(id);
        assertEquals(id, warehouse.getId());
    }

    @Test
    void testSetAndGetCode() {
        String code = "WARE001";
        warehouse.setCode(code);
        assertEquals(code, warehouse.getCode());
    }

    @Test
    void testSetAndGetName() {
        String name = "一号仓库";
        warehouse.setName(name);
        assertEquals(name, warehouse.getName());
    }

    @Test
    void testSetAndGetType() {
        String type = "原料仓";
        warehouse.setType(type);
        assertEquals(type, warehouse.getType());
    }

    @Test
    void testSetAndGetAddress() {
        String address = "北京市海淀区中关村科技园区1号";
        warehouse.setAddress(address);
        assertEquals(address, warehouse.getAddress());
    }

    @Test
    void testSetAndGetManager() {
        String manager = "张三";
        warehouse.setManager(manager);
        assertEquals(manager, warehouse.getManager());
    }

    @Test
    void testSetAndGetPhone() {
        String phone = "13800138000";
        warehouse.setPhone(phone);
        assertEquals(phone, warehouse.getPhone());
    }

    @Test
    void testSetAndGetStorageCondition() {
        String storageCondition = "常温干燥";
        warehouse.setStorageCondition(storageCondition);
        assertEquals(storageCondition, warehouse.getStorageCondition());
    }

    @Test
    void testSetAndGetDescription() {
        String description = "备注信息";
        warehouse.setDescription(description);
        assertEquals(description, warehouse.getDescription());
    }

    @Test
    void testSetAndGetStatus() {
        Integer status = 1;
        warehouse.setStatus(status);
        assertEquals(status, warehouse.getStatus());
    }

    @Test
    void testSetAndGetCreatedAt() {
        LocalDateTime createdAt = LocalDateTime.now();
        warehouse.setCreatedAt(createdAt);
        assertEquals(createdAt, warehouse.getCreatedAt());
    }

    @Test
    void testSetAndGetCreatedBy() {
        String createdBy = "admin";
        warehouse.setCreatedBy(createdBy);
        assertEquals(createdBy, warehouse.getCreatedBy());
    }

    @Test
    void testSetAndGetUpdatedAt() {
        LocalDateTime updatedAt = LocalDateTime.now();
        warehouse.setUpdatedAt(updatedAt);
        assertEquals(updatedAt, warehouse.getUpdatedAt());
    }

    @Test
    void testSetAndGetUpdatedBy() {
        String updatedBy = "admin";
        warehouse.setUpdatedBy(updatedBy);
        assertEquals(updatedBy, warehouse.getUpdatedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        Warehouse warehouse1 = new Warehouse();
        warehouse1.setId(1L);
        warehouse1.setCode("WARE001");

        Warehouse warehouse2 = new Warehouse();
        warehouse2.setId(1L);
        warehouse2.setCode("WARE001");

        Warehouse warehouse3 = new Warehouse();
        warehouse3.setId(2L);
        warehouse3.setCode("WARE002");

        assertEquals(warehouse1, warehouse2);
        assertNotEquals(warehouse1, warehouse3);
        assertEquals(warehouse1.hashCode(), warehouse2.hashCode());
    }
}
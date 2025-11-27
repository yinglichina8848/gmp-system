package com.gmp.warehouse.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Location实体类的单元测试
 *
 * @author GMP系统开发团队
 * @version 1.0.0
 * @since 2024-01-01
 */
class LocationTest {

    private Location location;

    @BeforeEach
    void setUp() {
        location = new Location();
    }

    @Test
    void testSetAndGetId() {
        Long id = 1L;
        location.setId(id);
        assertEquals(id, location.getId());
    }

    @Test
    void testSetAndGetCode() {
        String code = "LOC001";
        location.setCode(code);
        assertEquals(code, location.getCode());
    }

    @Test
    void testSetAndGetName() {
        String name = "A区01货架";
        location.setName(name);
        assertEquals(name, location.getName());
    }

    @Test
    void testSetAndGetWarehouse() {
        Warehouse warehouse = new Warehouse();
        warehouse.setId(1L);
        warehouse.setName("一号仓库");
        location.setWarehouse(warehouse);
        assertEquals(warehouse, location.getWarehouse());
        assertEquals(1L, location.getWarehouse().getId());
    }

    @Test
    void testSetAndGetArea() {
        String area = "A区";
        location.setArea(area);
        assertEquals(area, location.getArea());
    }

    @Test
    void testSetAndGetRow() {
        String row = "01排";
        location.setRow(row);
        assertEquals(row, location.getRow());
    }

    @Test
    void testSetAndGetShelf() {
        String shelf = "01货架";
        location.setShelf(shelf);
        assertEquals(shelf, location.getShelf());
    }

    @Test
    void testSetAndGetLevel() {
        String level = "01层";
        location.setLevel(level);
        assertEquals(level, location.getLevel());
    }

    @Test
    void testSetAndGetCapacity() {
        Integer capacity = 1000;
        location.setCapacity(capacity);
        assertEquals(capacity, location.getCapacity());
    }

    @Test
    void testSetAndGetUsedCapacity() {
        Integer usedCapacity = 500;
        location.setUsedCapacity(usedCapacity);
        assertEquals(usedCapacity, location.getUsedCapacity());
    }

    @Test
    void testSetAndGetLocationType() {
        String locationType = "普通库位";
        location.setLocationType(locationType);
        assertEquals(locationType, location.getLocationType());
    }

    @Test
    void testSetAndGetRemark() {
        String remark = "备注信息";
        location.setRemark(remark);
        assertEquals(remark, location.getRemark());
    }

    @Test
    void testSetAndGetStatus() {
        Integer status = 1;
        location.setStatus(status);
        assertEquals(status, location.getStatus());
    }

    @Test
    void testSetAndGetCreatedAt() {
        LocalDateTime createdAt = LocalDateTime.now();
        location.setCreatedAt(createdAt);
        assertEquals(createdAt, location.getCreatedAt());
    }

    @Test
    void testSetAndGetCreatedBy() {
        String createdBy = "admin";
        location.setCreatedBy(createdBy);
        assertEquals(createdBy, location.getCreatedBy());
    }

    @Test
    void testSetAndGetUpdatedAt() {
        LocalDateTime updatedAt = LocalDateTime.now();
        location.setUpdatedAt(updatedAt);
        assertEquals(updatedAt, location.getUpdatedAt());
    }

    @Test
    void testSetAndGetUpdatedBy() {
        String updatedBy = "admin";
        location.setUpdatedBy(updatedBy);
        assertEquals(updatedBy, location.getUpdatedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        Location location1 = new Location();
        location1.setId(1L);
        location1.setCode("LOC001");

        Location location2 = new Location();
        location2.setId(1L);
        location2.setCode("LOC001");

        Location location3 = new Location();
        location3.setId(2L);
        location3.setCode("LOC002");

        assertEquals(location1, location2);
        assertNotEquals(location1, location3);
        assertEquals(location1.hashCode(), location2.hashCode());
    }

}
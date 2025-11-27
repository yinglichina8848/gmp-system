package com.gmp.warehouse.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * MaterialCategory实体类的单元测试
 *
 * @author GMP系统开发团队
 * @version 1.0.0
 * @since 2024-01-01
 */
class MaterialCategoryTest {

    private MaterialCategory category;

    @BeforeEach
    void setUp() {
        category = new MaterialCategory();
    }

    @Test
    void testSetAndGetId() {
        Long id = 1L;
        category.setId(id);
        assertEquals(id, category.getId());
    }

    @Test
    void testSetAndGetCode() {
        String code = "CAT001";
        category.setCode(code);
        assertEquals(code, category.getCode());
    }

    @Test
    void testSetAndGetName() {
        String name = "电子元器件";
        category.setName(name);
        assertEquals(name, category.getName());
    }

    @Test
    void testSetAndGetDescription() {
        String description = "电子元器件分类";
        category.setDescription(description);
        assertEquals(description, category.getDescription());
    }

    @Test
    void testSetAndGetParent() {
        MaterialCategory parent = new MaterialCategory();
        parent.setId(2L);
        parent.setName("电子产品");
        category.setParent(parent);
        assertEquals(parent, category.getParent());
        assertEquals(2L, category.getParent().getId());
    }

    @Test
    void testSetAndGetChildren() {
        List<MaterialCategory> children = new ArrayList<>();
        MaterialCategory child1 = new MaterialCategory();
        child1.setId(3L);
        child1.setName("电阻电容");
        MaterialCategory child2 = new MaterialCategory();
        child2.setId(4L);
        child2.setName("集成电路");
        children.add(child1);
        children.add(child2);
        
        category.setChildren(children);
        assertEquals(children, category.getChildren());
        assertEquals(2, category.getChildren().size());
        assertEquals("电阻电容", category.getChildren().get(0).getName());
    }

    @Test
    void testSetAndGetLevel() {
        Integer level = 2;
        category.setLevel(level);
        assertEquals(level, category.getLevel());
    }

    @Test
    void testSetAndGetPath() {
        String path = "001/002";
        category.setPath(path);
        assertEquals(path, category.getPath());
    }

    @Test
    void testSetAndGetSortOrder() {
        Integer sortOrder = 1;
        category.setSortOrder(sortOrder);
        assertEquals(sortOrder, category.getSortOrder());
    }

    @Test
    void testSetAndGetRemark() {
        String remark = "备注信息";
        category.setRemark(remark);
        assertEquals(remark, category.getRemark());
    }

    @Test
    void testSetAndGetStatus() {
        Integer status = 1;
        category.setStatus(status);
        assertEquals(status, category.getStatus());
    }

    @Test
    void testSetAndGetCreatedAt() {
        LocalDateTime createdAt = LocalDateTime.now();
        category.setCreatedAt(createdAt);
        assertEquals(createdAt, category.getCreatedAt());
    }

    @Test
    void testSetAndGetCreatedBy() {
        String createdBy = "admin";
        category.setCreatedBy(createdBy);
        assertEquals(createdBy, category.getCreatedBy());
    }

    @Test
    void testSetAndGetUpdatedAt() {
        LocalDateTime updatedAt = LocalDateTime.now();
        category.setUpdatedAt(updatedAt);
        assertEquals(updatedAt, category.getUpdatedAt());
    }

    @Test
    void testSetAndGetUpdatedBy() {
        String updatedBy = "admin";
        category.setUpdatedBy(updatedBy);
        assertEquals(updatedBy, category.getUpdatedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        MaterialCategory category1 = new MaterialCategory();
        category1.setId(1L);
        category1.setCode("CAT001");

        MaterialCategory category2 = new MaterialCategory();
        category2.setId(1L);
        category2.setCode("CAT001");

        MaterialCategory category3 = new MaterialCategory();
        category3.setId(2L);
        category3.setCode("CAT002");

        assertEquals(category1, category2);
        assertNotEquals(category1, category3);
        assertEquals(category1.hashCode(), category2.hashCode());
    }

}
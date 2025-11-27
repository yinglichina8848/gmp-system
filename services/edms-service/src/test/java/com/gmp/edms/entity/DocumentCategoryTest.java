package com.gmp.edms.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * DocumentCategory实体类的单元测试
 * 
 * @author GMP Team
 * @version 1.0.0
 * @since 2023-08-01
 */
public class DocumentCategoryTest {

    private DocumentCategory category;
    private DocumentCategory parentCategory;
    private DocumentCategory childCategory1;
    private DocumentCategory childCategory2;

    @BeforeEach
    public void setUp() {
        // 初始化测试数据
        category = new DocumentCategory();
        category.setId(1L);
        category.setName("技术文档");
        category.setCode("TECH");
        category.setDescription("技术相关文档分类");
        category.setLevel(1);
        category.setOrderNum(1);
        category.setStatus("ACTIVE");
        category.setCreatedBy("admin");
        category.setCreatedTime(new Date());
        category.setUpdatedBy("admin");
        category.setUpdatedTime(new Date());
        category.setIsDeleted(false);
        category.setPath("1");
        category.setFullPath("/技术文档/");
        category.setKeywords("技术,研发,设计");
        category.setIcon("icon-document");
        category.setProperties("{\"color\": \"#007bff\"}");
        category.setExtraInfo("{\"priority\": \"high\"}");
        
        parentCategory = new DocumentCategory();
        parentCategory.setId(10L);
        parentCategory.setName("文档中心");
        parentCategory.setCode("DOCS");
        
        childCategory1 = new DocumentCategory();
        childCategory1.setId(2L);
        childCategory1.setName("API文档");
        childCategory1.setCode("API");
        
        childCategory2 = new DocumentCategory();
        childCategory2.setId(3L);
        childCategory2.setName("用户手册");
        childCategory2.setCode("MANUAL");
    }

    @Test
    public void testCategoryBasicProperties() {
        // 测试基本属性设置和获取
        assertEquals(1L, category.getId());
        assertEquals("技术文档", category.getName());
        assertEquals("TECH", category.getCode());
        assertEquals("技术相关文档分类", category.getDescription());
        assertEquals(1, category.getLevel());
        assertEquals(1, category.getOrderNum());
        assertEquals("ACTIVE", category.getStatus());
        assertEquals("admin", category.getCreatedBy());
        assertEquals("admin", category.getUpdatedBy());
        assertFalse(category.getIsDeleted());
        assertEquals("1", category.getPath());
        assertEquals("/技术文档/", category.getFullPath());
        assertEquals("技术,研发,设计", category.getKeywords());
        assertEquals("icon-document", category.getIcon());
        assertEquals("{\"color\": \"#007bff\"}", category.getProperties());
        assertEquals("{\"priority\": \"high\"}", category.getExtraInfo());
    }

    @Test
    public void testParentCategoryAssociation() {
        // 测试父分类关联
        category.setParent(parentCategory);
        assertEquals(parentCategory, category.getParent());
        assertEquals(10L, category.getParent().getId());
        assertEquals("文档中心", category.getParent().getName());
    }

    @Test
    public void testChildCategoryAssociation() {
        // 测试子分类关联
        List<DocumentCategory> children = new ArrayList<>();
        children.add(childCategory1);
        category.setChildren(children);
        
        assertNotNull(category.getChildren());
        assertEquals(1, category.getChildren().size());
        assertEquals(childCategory1, category.getChildren().get(0));
        
        // 测试添加子分类方法
        category.addChild(childCategory2);
        assertEquals(2, category.getChildren().size());
        assertEquals("用户手册", category.getChildren().get(1).getName());
        
        // 测试移除子分类方法
        category.removeChild(childCategory1);
        assertEquals(1, category.getChildren().size());
        assertEquals("用户手册", category.getChildren().get(0).getName());
    }

    @Test
    public void testSetters() {
        // 测试setter方法
        DocumentCategory updatedCategory = new DocumentCategory();
        updatedCategory.setId(4L);
        updatedCategory.setName("更新的分类");
        updatedCategory.setCode("UPDATED");
        updatedCategory.setDescription("更新后的分类描述");
        updatedCategory.setLevel(2);
        updatedCategory.setOrderNum(2);
        updatedCategory.setStatus("INACTIVE");
        updatedCategory.setIsDeleted(true);
        
        assertEquals(4L, updatedCategory.getId());
        assertEquals("更新的分类", updatedCategory.getName());
        assertEquals("UPDATED", updatedCategory.getCode());
        assertEquals("更新后的分类描述", updatedCategory.getDescription());
        assertEquals(2, updatedCategory.getLevel());
        assertEquals(2, updatedCategory.getOrderNum());
        assertEquals("INACTIVE", updatedCategory.getStatus());
        assertTrue(updatedCategory.getIsDeleted());
    }

    @Test
    public void testNullValues() {
        // 测试空值处理
        DocumentCategory nullCategory = new DocumentCategory();
        assertNull(nullCategory.getId());
        assertNull(nullCategory.getParent());
        assertNull(nullCategory.getChildren());
        
        // 设置null值
        category.setParent(null);
        category.setChildren(null);
        assertNull(category.getParent());
        assertNull(category.getChildren());
    }

    @Test
    public void testAddChildWhenChildrenIsNull() {
        // 测试当children为null时添加子分类
        category.setChildren(null);
        DocumentCategory newChild = new DocumentCategory();
        newChild.setId(5L);
        newChild.setName("新子分类");
        
        category.addChild(newChild);
        assertNotNull(category.getChildren());
        assertEquals(1, category.getChildren().size());
        assertEquals("新子分类", category.getChildren().get(0).getName());
    }

    @Test
    public void testRemoveChildWhenChildrenIsNull() {
        // 测试当children为null时移除子分类
        category.setChildren(null);
        category.removeChild(childCategory1); // 应该安全地处理null情况
        assertNull(category.getChildren());
    }

    @Test
    public void testRemoveNonExistentChild() {
        // 测试移除不存在的子分类
        category.setChildren(new ArrayList<>());
        category.removeChild(childCategory1);
        assertTrue(category.getChildren().isEmpty());
    }

    @Test
    public void testBidirectionalRelationship() {
        // 测试双向关联关系
        category.setParent(parentCategory);
        parentCategory.addChild(category);
        
        assertEquals(parentCategory, category.getParent());
        assertNotNull(parentCategory.getChildren());
        assertTrue(parentCategory.getChildren().contains(category));
    }

    @Test
    public void testDeepHierarchy() {
        // 测试多层级关系
        childCategory1.setParent(category);
        category.addChild(childCategory1);
        
        assertEquals(category, childCategory1.getParent());
        assertTrue(category.getChildren().contains(childCategory1));
        
        // 添加更深层级
        DocumentCategory grandChild = new DocumentCategory();
        grandChild.setId(6L);
        grandChild.setName("子子分类");
        grandChild.setParent(childCategory1);
        childCategory1.addChild(grandChild);
        
        assertEquals(childCategory1, grandChild.getParent());
        assertTrue(childCategory1.getChildren().contains(grandChild));
    }
}

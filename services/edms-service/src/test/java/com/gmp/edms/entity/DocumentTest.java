package com.gmp.edms.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Document实体类的单元测试
 * 
 * @author GMP Team
 * @version 1.0.0
 * @since 2023-08-01
 */
public class DocumentTest {

    private Document document;
    private DocumentCategory category;
    private DocumentVersion version;

    @BeforeEach
    public void setUp() {
        // 初始化测试数据
        document = new Document();
        document.setId(1L);
        document.setDocumentCode("DOC-2023-0001");
        document.setTitle("测试文档");
        document.setDescription("这是一个测试文档");
        document.setDocumentType("PDF");
        document.setAuthorId("user123");
        document.setStatus("ACTIVE");
        document.setApprovalStatus("APPROVED");
        document.setCreatedBy("admin");
        document.setCreatedTime(new Date());
        document.setUpdatedBy("admin");
        document.setUpdatedTime(new Date());
        document.setIsDeleted(false);
        document.setKeywords("测试,文档,示例");
        document.setTags("重要,内部");
        document.setLanguage("zh-CN");
        document.setSourceSystem("GMP-SYSTEM");
        document.setSourceId("SRC-001");
        document.setExtraInfo("{\"priority\": \"high\"}");
        
        category = new DocumentCategory();
        category.setId(1L);
        category.setName("技术文档");
        category.setCode("TECH");
        
        version = new DocumentVersion();
        version.setId(1L);
        version.setVersionNumber("1.0.0");
        version.setFileName("测试文档.pdf");
        version.setFilePath("/documents/tech/test.pdf");
        version.setFileSize(1024L);
        version.setFileType("application/pdf");
        version.setUploaderId("user123");
    }

    @Test
    public void testDocumentBasicProperties() {
        // 测试基本属性设置和获取
        assertEquals(1L, document.getId());
        assertEquals("DOC-2023-0001", document.getDocumentCode());
        assertEquals("测试文档", document.getTitle());
        assertEquals("这是一个测试文档", document.getDescription());
        assertEquals("PDF", document.getDocumentType());
        assertEquals("user123", document.getAuthorId());
        assertEquals("ACTIVE", document.getStatus());
        assertEquals("APPROVED", document.getApprovalStatus());
        assertEquals("admin", document.getCreatedBy());
        assertEquals("admin", document.getUpdatedBy());
        assertFalse(document.getIsDeleted());
        assertEquals("测试,文档,示例", document.getKeywords());
        assertEquals("重要,内部", document.getTags());
        assertEquals("zh-CN", document.getLanguage());
        assertEquals("GMP-SYSTEM", document.getSourceSystem());
        assertEquals("SRC-001", document.getSourceId());
        assertEquals("{\"priority\": \"high\"}", document.getExtraInfo());
    }

    @Test
    public void testCategoryAssociation() {
        // 测试分类关联
        document.setCategory(category);
        assertEquals(category, document.getCategory());
        assertEquals(1L, document.getCategory().getId());
        assertEquals("技术文档", document.getCategory().getName());
    }

    @Test
    public void testDocumentVersionAssociation() {
        // 测试版本关联
        List<DocumentVersion> versions = new ArrayList<>();
        versions.add(version);
        document.setVersions(versions);
        
        assertNotNull(document.getVersions());
        assertEquals(1, document.getVersions().size());
        assertEquals(version, document.getVersions().get(0));
        
        // 测试添加版本方法
        DocumentVersion version2 = new DocumentVersion();
        version2.setId(2L);
        version2.setVersionNumber("1.1.0");
        
        document.addVersion(version2);
        assertEquals(2, document.getVersions().size());
        assertEquals("1.1.0", document.getVersions().get(1).getVersionNumber());
        
        // 测试移除版本方法
        document.removeVersion(version);
        assertEquals(1, document.getVersions().size());
        assertEquals("1.1.0", document.getVersions().get(0).getVersionNumber());
    }

    @Test
    public void testSetters() {
        // 测试setter方法
        Document updatedDoc = new Document();
        updatedDoc.setId(2L);
        updatedDoc.setDocumentCode("DOC-2023-0002");
        updatedDoc.setTitle("更新的测试文档");
        updatedDoc.setDescription("这是更新后的测试文档");
        updatedDoc.setDocumentType("DOCX");
        updatedDoc.setStatus("DRAFT");
        updatedDoc.setApprovalStatus("PENDING");
        updatedDoc.setIsDeleted(true);
        
        assertEquals(2L, updatedDoc.getId());
        assertEquals("DOC-2023-0002", updatedDoc.getDocumentCode());
        assertEquals("更新的测试文档", updatedDoc.getTitle());
        assertEquals("这是更新后的测试文档", updatedDoc.getDescription());
        assertEquals("DOCX", updatedDoc.getDocumentType());
        assertEquals("DRAFT", updatedDoc.getStatus());
        assertEquals("PENDING", updatedDoc.getApprovalStatus());
        assertTrue(updatedDoc.getIsDeleted());
    }

    @Test
    public void testNullValues() {
        // 测试空值处理
        Document nullDoc = new Document();
        assertNull(nullDoc.getId());
        assertNull(nullDoc.getCategory());
        assertNull(nullDoc.getVersions());
        
        // 设置null值
        document.setCategory(null);
        document.setVersions(null);
        assertNull(document.getCategory());
        assertNull(document.getVersions());
    }

    @Test
    public void testAddVersionWhenVersionsIsNull() {
        // 测试当versions为null时添加版本
        document.setVersions(null);
        DocumentVersion newVersion = new DocumentVersion();
        newVersion.setId(3L);
        newVersion.setVersionNumber("2.0.0");
        
        document.addVersion(newVersion);
        assertNotNull(document.getVersions());
        assertEquals(1, document.getVersions().size());
        assertEquals("2.0.0", document.getVersions().get(0).getVersionNumber());
    }

    @Test
    public void testRemoveVersionWhenVersionsIsNull() {
        // 测试当versions为null时移除版本
        document.setVersions(null);
        document.removeVersion(version); // 应该安全地处理null情况
        assertNull(document.getVersions());
    }

    @Test
    public void testRemoveNonExistentVersion() {
        // 测试移除不存在的版本
        document.setVersions(new ArrayList<>());
        document.removeVersion(version);
        assertTrue(document.getVersions().isEmpty());
    }
}

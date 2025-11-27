package com.gmp.edms.repository;

import com.gmp.edms.entity.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class DocumentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DocumentRepository documentRepository;

    @Test
    void testFindByDocCode() {
        // 准备测试数据
        Document document = new Document();
        document.setDocumentNumber("DOC-2024-0101-001");
        document.setTitle("Test Document");
        document.setAuthor("admin");
        
        entityManager.persist(document);
        entityManager.flush();
        
        // 执行测试
        Optional<Document> found = documentRepository.findByDocCode("DOC-2024-0101-001");
        
        // 验证结果
        assertTrue(found.isPresent());
        assertEquals("Test Document", found.get().getTitle());
    }

    @Test
    void testFindByCategoryId() {
        // 准备测试数据
        Document document1 = new Document();
        document1.setCategoryId(1L);
        document1.setTitle("Document 1");
        document1.setAuthor("admin");
        
        Document document2 = new Document();
        document2.setCategoryId(1L);
        document2.setTitle("Document 2");
        document2.setAuthor("admin");
        
        Document document3 = new Document();
        document3.setCategoryId(2L);
        document3.setTitle("Document 3");
        document3.setAuthor("admin");
        
        entityManager.persist(document1);
        entityManager.persist(document2);
        entityManager.persist(document3);
        entityManager.flush();
        
        // 执行测试
        List<Document> documents = documentRepository.findByCategoryIdOrderByUpdatedAtDesc(1L, 0, 10);
        
        // 验证结果
        assertEquals(2, documents.size());
    }

    @Test
    void testFindByStatus() {
        // 准备测试数据
        Document document1 = new Document();
        document1.setStatus("APPROVED");
        document1.setTitle("Approved Document");
        document1.setAuthor("admin");
        
        Document document2 = new Document();
        document2.setStatus("DRAFT");
        document2.setTitle("Draft Document");
        document2.setAuthor("admin");
        
        entityManager.persist(document1);
        entityManager.persist(document2);
        entityManager.flush();
        
        // 执行测试
        List<Document> approvedDocs = documentRepository.findByStatusOrderByUpdatedAtDesc("APPROVED", 0, 10);
        
        // 验证结果
        assertEquals(1, approvedDocs.size());
        assertEquals("Approved Document", approvedDocs.get(0).getTitle());
    }

    @Test
    void testSearchByKeyword() {
        // 准备测试数据
        Document document1 = new Document();
        document1.setTitle("Quality Manual");
        document1.setAuthor("admin");
        
        Document document2 = new Document();
        document2.setTitle("Test Procedure");
        document2.setAuthor("admin");
        
        entityManager.persist(document1);
        entityManager.persist(document2);
        entityManager.flush();
        
        // 执行测试
        List<Document> results = documentRepository.searchByKeyword("Test", 0, 10);
        
        // 验证结果
        assertEquals(1, results.size());
        assertEquals("Test Procedure", results.get(0).getTitle());
    }

    @Test
    void testCountByCategoryId() {
        // 准备测试数据
        Document document1 = new Document();
        document1.setCategoryId(1L);
        document1.setTitle("Doc 1");
        document1.setAuthor("admin");
        
        Document document2 = new Document();
        document2.setCategoryId(1L);
        document2.setTitle("Doc 2");
        document2.setAuthor("admin");
        
        entityManager.persist(document1);
        entityManager.persist(document2);
        entityManager.flush();
        
        // 执行测试
        long count = documentRepository.countByCategoryId(1L);
        
        // 验证结果
        assertEquals(2, count);
    }

    @Test
    void testCountByStatus() {
        // 准备测试数据
        Document document1 = new Document();
        document1.setStatus("APPROVED");
        document1.setTitle("Approved 1");
        document1.setAuthor("admin");
        
        Document document2 = new Document();
        document2.setStatus("APPROVED");
        document2.setTitle("Approved 2");
        document2.setAuthor("admin");
        
        entityManager.persist(document1);
        entityManager.persist(document2);
        entityManager.flush();
        
        // 执行测试
        long count = documentRepository.countByStatus("APPROVED");
        
        // 验证结果
        assertEquals(2, count);
    }
}
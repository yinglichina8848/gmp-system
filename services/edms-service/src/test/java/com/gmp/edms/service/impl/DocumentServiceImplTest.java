package com.gmp.edms.service.impl;

import com.gmp.edms.dto.DocumentCreateDTO;
import com.gmp.edms.dto.DocumentDTO;
import com.gmp.edms.dto.DocumentUpdateDTO;
import com.gmp.edms.entity.Document;
import com.gmp.edms.entity.DocumentCategory;
import com.gmp.edms.repository.DocumentRepository;
import com.gmp.edms.service.DocumentCategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DocumentServiceImplTest {

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private DocumentCategoryService documentCategoryService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private DocumentServiceImpl documentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateDocument() {
        DocumentCreateDTO createDTO = new DocumentCreateDTO();
        createDTO.setTitle("Test Document");
        createDTO.setDocumentType("SOP");
        createDTO.setCategoryId(1L);
        createDTO.setAuthor("admin");
        
        DocumentCategory category = new DocumentCategory();
        category.setId(1L);
        category.setName("Test Category");
        
        Document document = new Document();
        document.setId(1L);
        document.setTitle(createDTO.getTitle());
        
        DocumentDTO documentDTO = new DocumentDTO();
        documentDTO.setId(1L);
        documentDTO.setTitle(createDTO.getTitle());
        
        when(documentCategoryService.getDocumentCategoryById(1L)).thenReturn(category);
        when(documentRepository.save(any(Document.class))).thenReturn(document);
        when(modelMapper.map(document, DocumentDTO.class)).thenReturn(documentDTO);
        
        DocumentDTO result = documentService.createDocument(createDTO);
        
        assertNotNull(result);
        assertEquals("Test Document", result.getTitle());
        verify(documentRepository).save(any(Document.class));
    }

    @Test
    void testGetDocumentById() {
        Long documentId = 1L;
        Document document = new Document();
        document.setId(documentId);
        
        DocumentDTO documentDTO = new DocumentDTO();
        documentDTO.setId(documentId);
        
        when(documentRepository.findById(documentId)).thenReturn(Optional.of(document));
        when(modelMapper.map(document, DocumentDTO.class)).thenReturn(documentDTO);
        
        DocumentDTO result = documentService.getDocumentById(documentId);
        
        assertNotNull(result);
        assertEquals(documentId, result.getId());
    }

    @Test
    void testGetDocumentByDocCode() {
        String docCode = "DOC-2024-0101-001";
        Document document = new Document();
        document.setDocumentNumber(docCode);
        
        DocumentDTO documentDTO = new DocumentDTO();
        documentDTO.setDocumentNumber(docCode);
        
        when(documentRepository.findByDocCode(docCode)).thenReturn(Optional.of(document));
        when(modelMapper.map(document, DocumentDTO.class)).thenReturn(documentDTO);
        
        DocumentDTO result = documentService.getDocumentByDocCode(docCode);
        
        assertNotNull(result);
        assertEquals(docCode, result.getDocumentNumber());
    }

    @Test
    void testUpdateDocument() {
        Long documentId = 1L;
        DocumentUpdateDTO updateDTO = new DocumentUpdateDTO();
        updateDTO.setTitle("Updated Title");
        updateDTO.setCategoryId(2L);
        
        Document document = new Document();
        document.setId(documentId);
        document.setTitle("Old Title");
        
        DocumentCategory category = new DocumentCategory();
        category.setId(2L);
        
        DocumentDTO documentDTO = new DocumentDTO();
        documentDTO.setId(documentId);
        documentDTO.setTitle("Updated Title");
        
        when(documentRepository.findById(documentId)).thenReturn(Optional.of(document));
        when(documentCategoryService.getDocumentCategoryById(2L)).thenReturn(category);
        when(documentRepository.save(document)).thenReturn(document);
        when(modelMapper.map(document, DocumentDTO.class)).thenReturn(documentDTO);
        
        DocumentDTO result = documentService.updateDocument(documentId, updateDTO);
        
        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        verify(documentRepository).save(document);
    }

    @Test
    void testDeleteDocument() {
        Long documentId = 1L;
        Document document = new Document();
        document.setId(documentId);
        
        when(documentRepository.findById(documentId)).thenReturn(Optional.of(document));
        
        documentService.deleteDocument(documentId);
        
        verify(documentRepository).delete(document);
    }

    @Test
    void testUpdateDocumentStatus() {
        Long documentId = 1L;
        String newStatus = "APPROVED";
        
        Document document = new Document();
        document.setId(documentId);
        document.setStatus("DRAFT");
        
        DocumentDTO documentDTO = new DocumentDTO();
        documentDTO.setId(documentId);
        documentDTO.setStatus(newStatus);
        
        when(documentRepository.findById(documentId)).thenReturn(Optional.of(document));
        when(documentRepository.save(document)).thenReturn(document);
        when(modelMapper.map(document, DocumentDTO.class)).thenReturn(documentDTO);
        
        DocumentDTO result = documentService.updateDocumentStatus(documentId, newStatus);
        
        assertNotNull(result);
        assertEquals(newStatus, result.getStatus());
        verify(documentRepository).save(document);
    }

    @Test
    void testGenerateDocCode() {
        // 测试generateDocCode方法
        String docCode = documentService.generateDocCode();
        
        assertNotNull(docCode);
        assertTrue(docCode.startsWith("DOC_"));
        // 验证包含日期部分 (YYYYMMDD格式)
        assertTrue(docCode.matches("DOC_\\d{8}_\\d{4}"));
    }

    @Test
    void testSearchDocuments() {
        // 测试搜索文档方法
        String keyword = "test";
        
        // 调用方法并验证repository调用
        documentService.searchDocuments(keyword, 1, 10);
        
        verify(documentRepository).searchByKeyword(keyword, 0, 10);
    }

    @Test
    void testFindDocumentsByCategory() {
        Long categoryId = 1L;
        
        documentService.findDocumentsByCategory(categoryId, 1, 10);
        
        verify(documentRepository).findByCategoryIdOrderByUpdatedAtDesc(categoryId, 0, 10);
    }
}
package com.gmp.edms.service.impl;

import com.gmp.edms.dto.DocumentCreateDTO;
import com.gmp.edms.dto.DocumentDTO;
import com.gmp.edms.dto.DocumentUpdateDTO;
import com.gmp.edms.entity.Document;
import com.gmp.edms.entity.DocumentCategory;
import com.gmp.edms.repository.DocumentRepository;
import com.gmp.edms.service.DocumentCategoryService;
import com.gmp.edms.service.DocumentSearchService;
import com.gmp.edms.service.FileStorageService;
import com.gmp.edms.event.EventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class DocumentServiceImplTest {

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private DocumentCategoryService documentCategoryService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private DocumentSearchService documentSearchService;

    @Mock
    private FileStorageService fileStorageService;

    @Mock
    private EventPublisher eventPublisher;

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

        // 直接创建Document对象，避免ModelMapper和DocumentCategoryService的模拟问题
        Document document = new Document();
        document.setId(1L);
        document.setTitle(createDTO.getTitle());
        document.setDocumentType(createDTO.getDocumentType());
        document.setAuthor(createDTO.getAuthor());

        // 模拟Repository保存操作
        when(documentRepository.save(any(Document.class))).thenReturn(document);
        
        // 直接返回文档对象，避免类型转换问题
        DocumentDTO result = new DocumentDTO();
        result.setId(document.getId());
        result.setTitle(document.getTitle());
        
        // 模拟modelMapper行为
        when(modelMapper.map(document, DocumentDTO.class)).thenReturn(result);

        DocumentDTO actualResult = documentService.createDocument(createDTO);

        assertNotNull(actualResult);
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

        // 使用DTO对象而不是实体类
        com.gmp.edms.dto.DocumentCategoryDTO category = new com.gmp.edms.dto.DocumentCategoryDTO();
        category.setId(2L);

        DocumentDTO documentDTO = new DocumentDTO();
        documentDTO.setId(documentId);
        documentDTO.setTitle("Updated Title");

        when(documentRepository.findById(documentId)).thenReturn(Optional.of(document));
        when(documentCategoryService.getCategoryById(2L)).thenReturn(category);
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

        documentService.updateDocumentStatus(documentId, newStatus);
        // 验证状态已更新
        assertEquals(newStatus, document.getStatus());
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

        // 使用实际存在的方法进行测试
        when(documentRepository.findByCategoryId(categoryId)).thenReturn(List.of(new Document()));

        // 调用正确的方法名getDocumentsByCategory
        documentService.getDocumentsByCategory(categoryId);

        // 验证repository调用
        verify(documentRepository).findByCategoryId(categoryId);
    }

    @Test
    void testUploadDocumentFile() throws Exception {
        Long documentId = 1L;
        String fileName = "test.txt";
        String fileContent = "Test file content";
        MultipartFile multipartFile = new MockMultipartFile(
                fileName,
                fileName,
                "text/plain",
                fileContent.getBytes()
        );

        Document document = new Document();
        document.setId(documentId);
        document.setTitle("Test Document");

        DocumentDTO documentDTO = new DocumentDTO();
        documentDTO.setId(documentId);
        documentDTO.setTitle(document.getTitle());

        when(documentRepository.findById(documentId)).thenReturn(Optional.of(document));
        when(documentRepository.save(any(Document.class))).thenReturn(document);
        when(modelMapper.map(document, DocumentDTO.class)).thenReturn(documentDTO);

        DocumentDTO result = documentService.uploadDocumentFile(documentId, multipartFile);

        assertNotNull(result);
        verify(documentRepository).save(document);
        verify(fileStorageService).uploadFile(eq(multipartFile), eq("edms-documents"), anyString());
    }

    @Test
    void testUploadDocumentFileWithEmptyFile() {
        Long documentId = 1L;
        MultipartFile emptyFile = new MockMultipartFile(
                "empty.txt",
                "empty.txt",
                "text/plain",
                new byte[0]
        );

        Document document = new Document();
        document.setId(documentId);

        when(documentRepository.findById(documentId)).thenReturn(Optional.of(document));

        Exception exception = assertThrows(Exception.class, () -> {
            documentService.uploadDocumentFile(documentId, emptyFile);
        });

        assertEquals("文件不能为空", exception.getMessage());
    }

    @Test
    void testUploadDocumentFileWithNonExistingDocument() {
        Long documentId = 1L;
        MultipartFile multipartFile = new MockMultipartFile(
                "test.txt",
                "test.txt",
                "text/plain",
                "Test content".getBytes()
        );

        when(documentRepository.findById(documentId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            documentService.uploadDocumentFile(documentId, multipartFile);
        });

        assertEquals("文档不存在: " + documentId, exception.getMessage());
    }

    @Test
    void testDownloadDocumentFile() throws Exception {
        Long documentId = 1L;
        String fileContent = "Test file content";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes());

        Document document = new Document();
        document.setId(documentId);
        document.setFilePath("documents/1/test.txt");

        when(documentRepository.findById(documentId)).thenReturn(Optional.of(document));
        when(fileStorageService.downloadFile(eq("edms-documents"), eq(document.getFilePath()))).thenReturn(inputStream);

        byte[] result = documentService.downloadDocumentFile(documentId);

        assertNotNull(result);
        assertEquals(fileContent, new String(result));
        verify(fileStorageService).downloadFile(eq("edms-documents"), eq(document.getFilePath()));
    }

    @Test
    void testDownloadDocumentFileWithNonExistingDocument() {
        Long documentId = 1L;

        when(documentRepository.findById(documentId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            documentService.downloadDocumentFile(documentId);
        });

        assertEquals("文档不存在: " + documentId, exception.getMessage());
    }

    @Test
    void testDownloadDocumentFileWithoutFilePath() throws Exception {
        Long documentId = 1L;

        Document document = new Document();
        document.setId(documentId);
        document.setFilePath(null); // 没有关联文件

        when(documentRepository.findById(documentId)).thenReturn(Optional.of(document));

        Exception exception = assertThrows(Exception.class, () -> {
            documentService.downloadDocumentFile(documentId);
        });

        assertEquals("文档没有关联文件", exception.getMessage());
    }
}
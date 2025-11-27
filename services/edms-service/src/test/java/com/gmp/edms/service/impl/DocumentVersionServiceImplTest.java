package com.gmp.edms.service.impl;

import com.gmp.edms.dto.DocumentVersionDTO;
import com.gmp.edms.entity.Document;
import com.gmp.edms.entity.DocumentVersion;
import com.gmp.edms.repository.DocumentRepository;
import com.gmp.edms.repository.DocumentVersionRepository;
import com.gmp.edms.service.FileStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DocumentVersionServiceImplTest {

    @Mock
    private DocumentVersionRepository documentVersionRepository;

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private FileStorageService fileStorageService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private DocumentVersionServiceImpl documentVersionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetDocumentVersions() {
        // 创建模拟数据
        Long documentId = 1L;
        Document document = new Document();
        document.setId(documentId);
        
        when(documentRepository.findById(documentId)).thenReturn(Optional.of(document));
        
        // 调用方法
        documentVersionService.getDocumentVersions(documentId);
        
        // 验证调用
        verify(documentVersionRepository).findByDocumentIdOrderByCreatedAtDesc(documentId);
    }

    @Test
    void testGetDocumentVersion() {
        Long versionId = 1L;
        DocumentVersion version = new DocumentVersion();
        version.setId(versionId);
        DocumentVersionDTO versionDTO = new DocumentVersionDTO();
        versionDTO.setId(versionId);
        
        when(documentVersionRepository.findById(versionId)).thenReturn(Optional.of(version));
        when(modelMapper.map(version, DocumentVersionDTO.class)).thenReturn(versionDTO);
        
        DocumentVersionDTO result = documentVersionService.getDocumentVersion(versionId);
        
        assertNotNull(result);
        assertEquals(versionId, result.getId());
    }

    @Test
    void testApproveVersion() {
        Long versionId = 1L;
        String approver = "admin";
        String comments = "Approved";
        
        DocumentVersion version = new DocumentVersion();
        version.setId(versionId);
        
        DocumentVersionDTO versionDTO = new DocumentVersionDTO();
        versionDTO.setId(versionId);
        
        when(documentVersionRepository.findById(versionId)).thenReturn(Optional.of(version));
        when(documentVersionRepository.save(version)).thenReturn(version);
        when(modelMapper.map(version, DocumentVersionDTO.class)).thenReturn(versionDTO);
        
        DocumentVersionDTO result = documentVersionService.approveVersion(versionId, approver, true, comments);
        
        assertNotNull(result);
        assertEquals(approver, version.getApprover());
        assertNotNull(version.getApprovalDate());
        verify(documentVersionRepository).save(version);
    }

    @Test
    void testCompareVersions() throws Exception {
        Long versionId1 = 1L;
        Long versionId2 = 2L;
        
        DocumentVersion version1 = new DocumentVersion();
        version1.setId(versionId1);
        version1.setFileSize(100L);
        version1.setChecksum("same-checksum");
        
        DocumentVersion version2 = new DocumentVersion();
        version2.setId(versionId2);
        version2.setFileSize(100L);
        version2.setChecksum("same-checksum");
        
        when(documentVersionRepository.findById(versionId1)).thenReturn(Optional.of(version1));
        when(documentVersionRepository.findById(versionId2)).thenReturn(Optional.of(version2));
        
        String result = documentVersionService.compareDocumentVersions(1L, versionId1, versionId2);
        
        assertEquals("两个版本完全相同", result);
    }

    @Test
    void testCompareVersionsDifferentSize() throws Exception {
        Long versionId1 = 1L;
        Long versionId2 = 2L;
        
        DocumentVersion version1 = new DocumentVersion();
        version1.setId(versionId1);
        version1.setFileSize(100L);
        version1.setChecksum("checksum1");
        
        DocumentVersion version2 = new DocumentVersion();
        version2.setId(versionId2);
        version2.setFileSize(200L);
        version2.setChecksum("checksum2");
        
        when(documentVersionRepository.findById(versionId1)).thenReturn(Optional.of(version1));
        when(documentVersionRepository.findById(versionId2)).thenReturn(Optional.of(version2));
        
        String result = documentVersionService.compareDocumentVersions(1L, versionId1, versionId2);
        
        assertTrue(result.contains("文件大小不同"));
    }

    @Test
    void testCompareVersionsDifferentChecksum() throws Exception {
        Long versionId1 = 1L;
        Long versionId2 = 2L;
        
        DocumentVersion version1 = new DocumentVersion();
        version1.setId(versionId1);
        version1.setFileSize(100L);
        version1.setChecksum("checksum1");
        
        DocumentVersion version2 = new DocumentVersion();
        version2.setId(versionId2);
        version2.setFileSize(100L);
        version2.setChecksum("checksum2");
        
        when(documentVersionRepository.findById(versionId1)).thenReturn(Optional.of(version1));
        when(documentVersionRepository.findById(versionId2)).thenReturn(Optional.of(version2));
        
        String result = documentVersionService.compareDocumentVersions(1L, versionId1, versionId2);
        
        assertTrue(result.contains("文件内容不同"));
    }

    @Test
    void testCalculateChecksum() throws IOException {
        // 创建模拟的MultipartFile
        byte[] content = "test content".getBytes();
        InputStream inputStream = new ByteArrayInputStream(content);
        
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getInputStream()).thenReturn(inputStream);
        
        // 调用私有方法（通过反射或测试调用）
        String checksum = documentVersionService.calculateChecksum(mockFile);
        
        // 验证校验和不为空
        assertNotNull(checksum);
        assertFalse(checksum.isEmpty());
    }

    @Test
    void testFormatFileSize() {
        // 测试不同大小的文件格式化
        assertEquals("1023 B", documentVersionService.formatFileSize(1023));
        assertEquals("1.00 KB", documentVersionService.formatFileSize(1024));
        assertEquals("1.50 MB", documentVersionService.formatFileSize(1572864));
        assertEquals("2.00 GB", documentVersionService.formatFileSize(2147483648L));
    }

    @Test
    void testDeleteDocumentVersion() throws IOException {
        Long versionId = 1L;
        DocumentVersion version = new DocumentVersion();
        version.setId(versionId);
        version.setFilePath("test/path/file.txt");
        
        Document document = new Document();
        document.setId(1L);
        document.setCurrentVersionId(null); // 不是当前版本
        
        version.setDocument(document);
        
        when(documentVersionRepository.findById(versionId)).thenReturn(Optional.of(version));
        
        documentVersionService.deleteDocumentVersion(versionId);
        
        verify(fileStorageService).deleteFile("test/path/file.txt");
        verify(documentVersionRepository).delete(version);
    }
}
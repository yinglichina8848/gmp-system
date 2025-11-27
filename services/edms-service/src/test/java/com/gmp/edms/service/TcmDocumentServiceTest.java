package com.gmp.edms.service;

import com.gmp.edms.dto.TcmDocumentDTO;
import com.gmp.edms.entity.TcmDocument;
import com.gmp.edms.exception.ResourceNotFoundException;
import com.gmp.edms.repository.TcmDocumentRepository;
import com.gmp.edms.service.impl.TcmDocumentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 中药特色文档服务测试类
 */
public class TcmDocumentServiceTest {

    @Mock
    private TcmDocumentRepository tcmDocumentRepository;

    @InjectMocks
    private TcmDocumentServiceImpl tcmDocumentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTcmDocument() {
        // 准备测试数据
        TcmDocumentDTO dto = new TcmDocumentDTO();
        dto.setTitle("人参炮制标准操作程序");
        dto.setDocumentType("TCM_SOP");
        dto.setHerbName("人参");
        dto.setProcessingMethod("蒸制");
        dto.setAuthor("张药师");

        TcmDocument document = new TcmDocument();
        document.setId(1L);
        document.setDocumentNumber("TCM-DOC-20251127-0001");
        document.setTitle("人参炮制标准操作程序");
        document.setDocumentType("TCM_SOP");
        document.setHerbName("人参");
        document.setProcessingMethod("蒸制");
        document.setAuthor("张药师");
        document.setCreatedAt(LocalDateTime.now());
        document.setUpdatedAt(LocalDateTime.now());

        // 模拟仓库层行为
        when(tcmDocumentRepository.save(any(TcmDocument.class))).thenReturn(document);

        // 执行测试
        TcmDocument result = tcmDocumentService.createTcmDocument(dto);

        // 验证结果
        assertNotNull(result);
        assertEquals("人参炮制标准操作程序", result.getTitle());
        assertEquals("TCM-DOC-20251127-0001", result.getDocumentNumber());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());

        // 验证调用
        verify(tcmDocumentRepository, times(1)).save(any(TcmDocument.class));
    }

    @Test
    public void testUpdateTcmDocument() {
        // 准备测试数据
        TcmDocument existingDocument = new TcmDocument();
        existingDocument.setId(1L);
        existingDocument.setDocumentNumber("TCM-DOC-20251127-0001");
        existingDocument.setTitle("人参炮制标准操作程序");
        existingDocument.setDocumentType("TCM_SOP");
        existingDocument.setHerbName("人参");
        existingDocument.setProcessingMethod("蒸制");
        existingDocument.setAuthor("张药师");
        existingDocument.setCreatedAt(LocalDateTime.now());
        
        TcmDocumentDTO dto = new TcmDocumentDTO();
        dto.setTitle("人参炮制标准操作程序 - 更新版");
        dto.setDocumentType("TCM_SOP");
        dto.setHerbName("人参");
        dto.setProcessingMethod("蒸制");
        dto.setAuthor("张药师");
        dto.setApprover("李主任");

        TcmDocument updatedDocument = new TcmDocument();
        updatedDocument.setId(1L);
        updatedDocument.setDocumentNumber("TCM-DOC-20251127-0001");
        updatedDocument.setTitle("人参炮制标准操作程序 - 更新版");
        updatedDocument.setDocumentType("TCM_SOP");
        updatedDocument.setHerbName("人参");
        updatedDocument.setProcessingMethod("蒸制");
        updatedDocument.setAuthor("张药师");
        updatedDocument.setApprover("李主任");
        updatedDocument.setCreatedAt(existingDocument.getCreatedAt());
        updatedDocument.setUpdatedAt(LocalDateTime.now());

        // 模拟仓库层行为
        when(tcmDocumentRepository.findById(1L)).thenReturn(Optional.of(existingDocument));
        when(tcmDocumentRepository.save(any(TcmDocument.class))).thenReturn(updatedDocument);

        // 执行测试
        TcmDocument result = tcmDocumentService.updateTcmDocument(1L, dto);

        // 验证结果
        assertNotNull(result);
        assertEquals("人参炮制标准操作程序 - 更新版", result.getTitle());
        assertEquals("李主任", result.getApprover());
        assertEquals(existingDocument.getCreatedAt(), result.getCreatedAt()); // 创建时间不应改变
        assertNotNull(result.getUpdatedAt()); // 更新时间应该设置

        // 验证调用
        verify(tcmDocumentRepository, times(1)).findById(1L);
        verify(tcmDocumentRepository, times(1)).save(any(TcmDocument.class));
    }

    @Test
    public void testUpdateTcmDocumentNotFound() {
        // 准备测试数据
        TcmDocumentDTO dto = new TcmDocumentDTO();
        dto.setTitle("人参炮制标准操作规程");

        // 模拟仓库层行为
        when(tcmDocumentRepository.findById(1L)).thenReturn(Optional.empty());

        // 执行测试并验证异常
        assertThrows(ResourceNotFoundException.class, () -> {
            tcmDocumentService.updateTcmDocument(1L, dto);
        });

        // 验证调用
        verify(tcmDocumentRepository, times(1)).findById(1L);
        verify(tcmDocumentRepository, never()).save(any(TcmDocument.class));
    }

    @Test
    public void testGetTcmDocumentById() {
        // 准备测试数据
        TcmDocument document = new TcmDocument();
        document.setId(1L);
        document.setDocumentNumber("TCM-DOC-20251127-0001");
        document.setTitle("人参炮制标准操作规程");

        // 模拟仓库层行为
        when(tcmDocumentRepository.findById(1L)).thenReturn(Optional.of(document));

        // 执行测试
        TcmDocument result = tcmDocumentService.getTcmDocumentById(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("TCM-DOC-20251127-0001", result.getDocumentNumber());
        assertEquals("人参炮制标准操作规程", result.getTitle());

        // 验证调用
        verify(tcmDocumentRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetTcmDocumentByIdNotFound() {
        // 模拟仓库层行为
        when(tcmDocumentRepository.findById(1L)).thenReturn(Optional.empty());

        // 执行测试并验证异常
        assertThrows(ResourceNotFoundException.class, () -> {
            tcmDocumentService.getTcmDocumentById(1L);
        });

        // 验证调用
        verify(tcmDocumentRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetTcmDocumentsByHerbName() {
        // 准备测试数据
        TcmDocument document1 = new TcmDocument();
        document1.setId(1L);
        document1.setDocumentNumber("TCM-DOC-20251127-0001");
        document1.setTitle("人参炮制标准操作程序");
        document1.setHerbName("人参");

        TcmDocument document2 = new TcmDocument();
        document2.setId(2L);
        document2.setDocumentNumber("TCM-DOC-20251127-0002");
        document2.setTitle("人参质量标准");
        document2.setHerbName("人参");

        // 模拟仓库层行为
        when(tcmDocumentRepository.findByHerbName("人参")).thenReturn(Arrays.asList(document1, document2));

        // 执行测试
        var result = tcmDocumentService.getTcmDocumentsByHerbName("人参");

        // 验证结果
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("TCM-DOC-20251127-0001", result.get(0).getDocumentNumber());
        assertEquals("TCM-DOC-20251127-0002", result.get(1).getDocumentNumber());

        // 验证调用
        verify(tcmDocumentRepository, times(1)).findByHerbName("人参");
    }

    @Test
    public void testGetTcmDocuments() {
        // 准备测试数据
        TcmDocument document1 = new TcmDocument();
        document1.setId(1L);
        document1.setDocumentNumber("TCM-DOC-20251127-0001");
        document1.setTitle("人参炮制标准操作规程");
        document1.setHerbName("人参");

        TcmDocument document2 = new TcmDocument();
        document2.setId(2L);
        document2.setDocumentNumber("TCM-DOC-20251127-0002");
        document2.setTitle("当归炮制标准操作规程");
        document2.setHerbName("当归");

        Pageable pageable = PageRequest.of(0, 10);
        Page<TcmDocument> page = new PageImpl<>(Arrays.asList(document1, document2));

        // 模拟仓库层行为
        when(tcmDocumentRepository.findAll(pageable)).thenReturn(page);

        // 执行测试
        Page<TcmDocument> result = tcmDocumentService.getTcmDocuments(pageable);

        // 验证结果
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals("TCM-DOC-20251127-0001", result.getContent().get(0).getDocumentNumber());
        assertEquals("TCM-DOC-20251127-0002", result.getContent().get(1).getDocumentNumber());

        // 验证调用
        verify(tcmDocumentRepository, times(1)).findAll(pageable);
    }

    @Test
    public void testDeleteTcmDocument() {
        // 准备测试数据
        TcmDocument document = new TcmDocument();
        document.setId(1L);
        document.setDocumentNumber("TCM-DOC-20251127-0001");
        document.setTitle("人参炮制标准操作规程");

        // 模拟仓库层行为
        when(tcmDocumentRepository.findById(1L)).thenReturn(Optional.of(document));

        // 执行测试
        tcmDocumentService.deleteTcmDocument(1L);

        // 验证调用
        verify(tcmDocumentRepository, times(1)).findById(1L);
        verify(tcmDocumentRepository, times(1)).delete(document);
    }

    @Test
    public void testDeleteTcmDocumentNotFound() {
        // 模拟仓库层行为
        when(tcmDocumentRepository.findById(1L)).thenReturn(Optional.empty());

        // 执行测试并验证异常
        assertThrows(ResourceNotFoundException.class, () -> {
            tcmDocumentService.deleteTcmDocument(1L);
        });

        // 验证调用
        verify(tcmDocumentRepository, times(1)).findById(1L);
        verify(tcmDocumentRepository, never()).delete(any(TcmDocument.class));
    }
}
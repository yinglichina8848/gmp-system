package com.gmp.edms.service.impl;

import com.gmp.edms.dto.ElectronicSignatureDTO;
import com.gmp.edms.entity.ElectronicSignature;
import com.gmp.edms.repository.ElectronicSignatureRepository;
import com.gmp.edms.service.ElectronicSignatureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 电子签名服务单元测试
 * 测试电子签名的创建、查询、验证和管理功能
 */
class ElectronicSignatureServiceImplTest {

    @Mock
    private ElectronicSignatureRepository signatureRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ElectronicSignatureServiceImpl signatureService;

    private ElectronicSignature testSignature;
    private ElectronicSignatureDTO testSignatureDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // 创建测试数据
        testSignature = new ElectronicSignature();
        testSignature.setId(1L);
        testSignature.setSignatureTime(LocalDateTime.now());
        testSignature.setVerificationResult(true);
        testSignature.setValidityStatus("VALID");
        testSignature.setCreatedAt(LocalDateTime.now());
        testSignature.setUpdatedAt(LocalDateTime.now());

        testSignatureDTO = new ElectronicSignatureDTO();
        testSignatureDTO.setId(1L);
        testSignatureDTO.setDocumentId(100L);
        testSignatureDTO.setDocumentVersionId(200L);
    }

    /**
     * 测试创建电子签名
     */
    @Test
    void testCreateSignature() {
        // 模拟映射和保存操作
        when(modelMapper.map(any(ElectronicSignatureDTO.class), eq(ElectronicSignature.class))).thenReturn(testSignature);
        when(signatureRepository.save(any(ElectronicSignature.class))).thenReturn(testSignature);
        when(modelMapper.map(any(ElectronicSignature.class), eq(ElectronicSignatureDTO.class))).thenReturn(testSignatureDTO);

        // 执行测试
        ElectronicSignatureDTO createdSignature = signatureService.createSignature(testSignatureDTO);

        // 验证结果
        assertNotNull(createdSignature);
        assertEquals(testSignatureDTO.getId(), createdSignature.getId());

        // 验证方法调用
        verify(modelMapper, times(1)).map(any(ElectronicSignatureDTO.class), eq(ElectronicSignature.class));
        verify(signatureRepository, times(1)).save(any(ElectronicSignature.class));
        verify(modelMapper, times(1)).map(any(ElectronicSignature.class), eq(ElectronicSignatureDTO.class));
    }

    /**
     * 测试根据ID获取电子签名
     */
    @Test
    void testGetSignatureById() {
        // 模拟查询和映射操作
        when(signatureRepository.findById(1L)).thenReturn(java.util.Optional.of(testSignature));
        when(modelMapper.map(any(ElectronicSignature.class), eq(ElectronicSignatureDTO.class))).thenReturn(testSignatureDTO);

        // 执行测试
        ElectronicSignatureDTO foundSignature = signatureService.getSignatureById(1L);

        // 验证结果
        assertNotNull(foundSignature);
        assertEquals(testSignature.getId(), foundSignature.getId());

        // 验证方法调用
        verify(signatureRepository, times(1)).findById(1L);
        verify(modelMapper, times(1)).map(any(ElectronicSignature.class), eq(ElectronicSignatureDTO.class));
    }

    /**
     * 测试根据文档ID获取电子签名
     */
    @Test
    void testGetSignaturesByDocumentId() {
        // 模拟查询和映射操作
        when(signatureRepository.findByDocumentId(100L)).thenReturn(List.of(testSignature));
        when(modelMapper.map(any(ElectronicSignature.class), eq(ElectronicSignatureDTO.class))).thenReturn(testSignatureDTO);

        // 执行测试
        List<ElectronicSignatureDTO> signatures = signatureService.getSignaturesByDocumentId(100L);

        // 验证结果
        assertNotNull(signatures);
        assertFalse(signatures.isEmpty());
        assertEquals(1, signatures.size());

        // 验证方法调用
        verify(signatureRepository, times(1)).findByDocumentId(100L);
        verify(modelMapper, times(1)).map(any(ElectronicSignature.class), eq(ElectronicSignatureDTO.class));
    }

    /**
     * 测试根据文档版本ID获取电子签名
     */
    @Test
    void testGetSignaturesByDocumentVersionId() {
        // 模拟查询和映射操作
        when(signatureRepository.findByDocumentVersionId(200L)).thenReturn(List.of(testSignature));
        when(modelMapper.map(any(ElectronicSignature.class), eq(ElectronicSignatureDTO.class))).thenReturn(testSignatureDTO);

        // 执行测试
        List<ElectronicSignatureDTO> signatures = signatureService.getSignaturesByDocumentVersionId(200L);

        // 验证结果
        assertNotNull(signatures);
        assertFalse(signatures.isEmpty());
        assertEquals(1, signatures.size());

        // 验证方法调用
        verify(signatureRepository, times(1)).findByDocumentVersionId(200L);
        verify(modelMapper, times(1)).map(any(ElectronicSignature.class), eq(ElectronicSignatureDTO.class));
    }

    /**
     * 测试验证电子签名
     */
    @Test
    void testVerifySignature() {
        // 模拟查询和保存操作
        when(signatureRepository.findById(1L)).thenReturn(java.util.Optional.of(testSignature));
        when(signatureRepository.save(any(ElectronicSignature.class))).thenReturn(testSignature);

        // 执行测试
        boolean isValid = signatureService.verifySignature(1L);

        // 验证结果
        assertTrue(isValid);

        // 验证方法调用
        verify(signatureRepository, times(1)).findById(1L);
        verify(signatureRepository, times(1)).save(any(ElectronicSignature.class));
    }

    /**
     * 测试批量验证电子签名
     */
    @Test
    void testBatchVerifySignatures() {
        // 模拟查询和保存操作
        when(signatureRepository.findById(1L)).thenReturn(java.util.Optional.of(testSignature));
        when(signatureRepository.findById(2L)).thenReturn(java.util.Optional.of(testSignature));
        when(signatureRepository.save(any(ElectronicSignature.class))).thenReturn(testSignature);

        // 执行测试
        List<Boolean> results = signatureService.batchVerifySignatures(List.of(1L, 2L));

        // 验证结果
        assertNotNull(results);
        assertEquals(2, results.size());
        assertTrue(results.get(0));
        assertTrue(results.get(1));

        // 验证方法调用
        verify(signatureRepository, times(2)).findById(anyLong());
        verify(signatureRepository, times(2)).save(any(ElectronicSignature.class));
    }

    /**
     * 测试删除电子签名
     */
    @Test
    void testDeleteSignature() {
        // 模拟查询和删除操作
        when(signatureRepository.findById(1L)).thenReturn(java.util.Optional.of(testSignature));
        doNothing().when(signatureRepository).deleteById(1L);

        // 执行测试
        signatureService.deleteSignature(1L);

        // 验证方法调用
        verify(signatureRepository, times(1)).findById(1L);
        verify(signatureRepository, times(1)).deleteById(1L);
    }

    /**
     * 测试批量删除电子签名
     */
    @Test
    void testBatchDeleteSignatures() {
        // 模拟删除操作
        doNothing().when(signatureRepository).deleteAllById(anyList());

        // 执行测试
        signatureService.batchDeleteSignatures(List.of(1L, 2L));

        // 验证方法调用
        verify(signatureRepository, times(1)).deleteAllById(anyList());
    }
}

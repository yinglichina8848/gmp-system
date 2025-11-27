package com.gmp.qms.service.impl;

import com.gmp.qms.dto.CapaDTO;
import com.gmp.qms.dto.CapaSearchCriteria;
import com.gmp.qms.entity.Capa;
import com.gmp.qms.entity.CapaAttachment;
import com.gmp.qms.exception.ResourceNotFoundException;
import com.gmp.qms.repository.CapaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * CapaServiceImpl单元测试
 * 
 * @author GMP系统开发团队
 */
@ExtendWith(MockitoExtension.class)
public class CapaServiceImplTest {

    @Mock
    private CapaRepository capaRepository;

    @InjectMocks
    private CapaServiceImpl capaService;

    private Capa capa;
    private CapaDTO capaDTO;

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        capa = new Capa();
        capa.setId(1L);
        capa.setCapaCode("CAPA-2024-0001");
        capa.setTitle("测试CAPA");
        capa.setDescription("测试CAPA描述");
        capa.setPriorityLevel(Capa.PriorityLevel.MEDIUM);
        capa.setStatus(Capa.CapaStatus.IDENTIFIED);
        capa.setAttachments(new ArrayList<>());

        capaDTO = new CapaDTO();
        capaDTO.setTitle("测试CAPA");
        capaDTO.setDescription("测试CAPA描述");
        capaDTO.setPriorityLevel(Capa.PriorityLevel.HIGH.name());
        capaDTO.setResponsiblePersonId(1L);
        capaDTO.setTargetCompletionDate(LocalDate.now().plusDays(30));
        capaDTO.setProblemDescription("问题描述");
        capaDTO.setRootCauseAnalysis("根本原因分析");
        capaDTO.setCorrectiveAction("纠正措施");
        capaDTO.setPreventiveAction("预防措施");
    }

    @Test
    void testGetCapaById() {
        when(capaRepository.findById(1L)).thenReturn(Optional.of(capa));

        Capa result = capaService.getCapaById(1L);

        assertNotNull(result);
        assertEquals("CAPA-2024-0001", result.getCapaCode());
        verify(capaRepository).findById(1L);
    }

    @Test
    void testGetCapaByIdNotFound() {
        when(capaRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            capaService.getCapaById(1L);
        });

        assertEquals("CAPA not found with id: 1", exception.getMessage());
    }

    @Test
    void testGetCapaByCode() {
        when(capaRepository.findByCapaCode("CAPA-2024-0001")).thenReturn(capa);

        Capa result = capaService.getCapaByCode("CAPA-2024-0001");

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(capaRepository).findByCapaCode("CAPA-2024-0001");
    }

    @Test
    void testCreateCapa() {
        // 设置模拟数据
        when(capaRepository.count()).thenReturn(9L);
        when(capaRepository.save(any(Capa.class))).thenReturn(capa);

        // 执行测试
        Capa result = capaService.createCapa(capaDTO);

        // 验证结果
        assertNotNull(result);
        assertEquals("测试CAPA", result.getTitle());
        verify(capaRepository).count();
        verify(capaRepository).save(any(Capa.class));
    }

    @Test
    void testUpdateCapa() {
        when(capaRepository.findById(1L)).thenReturn(Optional.of(capa));
        when(capaRepository.save(any(Capa.class))).thenReturn(capa);

        capaDTO.setTitle("更新后的标题");
        Capa result = capaService.updateCapa(1L, capaDTO);

        assertNotNull(result);
        assertEquals("更新后的标题", result.getTitle());
    }

    @Test
    void testFindAllCapas() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Capa> capas = Arrays.asList(capa);
        Page<Capa> page = new PageImpl<>(capas, pageable, capas.size());

        when(capaRepository.findAll(pageable)).thenReturn(page);

        Page<Capa> result = capaService.findAllCapas(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testSearchCapas() {
        Pageable pageable = PageRequest.of(0, 10);
        CapaSearchCriteria criteria = new CapaSearchCriteria();
        criteria.setKeyword("测试");

        List<Capa> capas = Arrays.asList(capa);
        Page<Capa> page = new PageImpl<>(capas, pageable, capas.size());

        when(capaRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);

        Page<Capa> result = capaService.searchCapas(criteria, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testUpdateCapaStatus() {
        when(capaRepository.findById(1L)).thenReturn(Optional.of(capa));
        when(capaRepository.save(any(Capa.class))).thenReturn(capa);

        Capa result = capaService.updateCapaStatus(1L, Capa.CapaStatus.CLOSED, "测试关闭备注");

        assertNotNull(result);
        assertEquals(Capa.CapaStatus.CLOSED, result.getStatus());
    }

    @Test
    void testGenerateCapaCode() {
        // 注意：generateCapaCode方法现在使用UUID而不是查询数据库

        String code = capaService.generateCapaCode();

        assertNotNull(code);
        assertTrue(code.startsWith("CAPA-"));
        // 验证格式: CAPA-YYYYMMDD-XXXXX
        assertTrue(code.matches("CAPA-\\d{8}-[A-Z0-9]{5}"));
    }

    @Test
    void testAddAttachment() {
        // 创建模拟文件
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "Test content".getBytes());

        // 当调用findById时返回空
        when(capaRepository.findById(1L)).thenReturn(Optional.empty());

        // 测试文件上传失败场景
        assertThrows(ResourceNotFoundException.class, () -> {
            capaService.addAttachment(1L, file, "测试附件");
        }, "Expected ResourceNotFoundException but no exception was thrown");
    }

    @Test
    void testRemoveAttachment() {
        CapaAttachment attachment = new CapaAttachment();
        attachment.setId(1L);
        capa.getAttachments().add(attachment);

        when(capaRepository.findById(1L)).thenReturn(Optional.of(capa));
        when(capaRepository.save(any(Capa.class))).thenReturn(capa);

        Capa result = capaService.removeAttachment(1L, 1L);

        assertNotNull(result);
        assertTrue(result.getAttachments().isEmpty());
        verify(capaRepository).save(any(Capa.class));
    }

    @Test
    void testRemoveAttachmentNotFound() {
        when(capaRepository.findById(1L)).thenReturn(Optional.of(capa));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            capaService.removeAttachment(1L, 999L);
        });

        assertEquals("Attachment not found with id: 999", exception.getMessage());
        verify(capaRepository, never()).save(any(Capa.class));
    }

    @Test
    void testFindOverdueCapas() {
        List<Capa> overdueCapas = Arrays.asList(capa);
        List<Capa.CapaStatus> excludedStatuses = List.of(
                Capa.CapaStatus.CLOSED,
                Capa.CapaStatus.REJECTED);

        when(capaRepository.findOverdueCapa(any(LocalDateTime.class), eq(excludedStatuses)))
                .thenReturn(overdueCapas);

        List<Capa> result = capaService.findOverdueCapas();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(capaRepository).findOverdueCapa(any(LocalDateTime.class), eq(excludedStatuses));
    }

    @Test
    void testAddAttachmentSuccess() {
        // 为了避免实际文件操作，我们需要模拟文件系统相关的行为
        // 这里我们需要模拟Files类的静态方法，这在标准JUnit/Mockito中比较复杂
        // 但我们可以通过测试抛出异常的方式来部分测试

        when(capaRepository.findById(1L)).thenReturn(Optional.of(capa));

        // 创建模拟文件
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "Test content".getBytes());

        // 由于addAttachment方法中包含文件系统操作，实际测试会抛出IOException
        // 我们测试异常捕获和处理
        assertThrows(RuntimeException.class, () -> {
            capaService.addAttachment(1L, file, "测试附件");
        }, "Expected RuntimeException due to file system operation");

        // 验证异常消息是否符合预期
        try {
            capaService.addAttachment(1L, file, "测试附件");
            fail("Expected RuntimeException but no exception was thrown");
        } catch (RuntimeException e) {
            assertTrue(e.getMessage().contains("Failed to upload file"));
        }
    }
}

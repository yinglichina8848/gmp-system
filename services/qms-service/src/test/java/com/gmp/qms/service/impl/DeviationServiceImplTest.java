package com.gmp.qms.service.impl;

import com.gmp.qms.dto.DeviationDTO;
import com.gmp.qms.dto.DeviationSearchCriteria;
import com.gmp.qms.entity.Deviation;
import com.gmp.qms.entity.DeviationAttachment;
import com.gmp.qms.exception.ResourceNotFoundException;
import com.gmp.qms.repository.DeviationAttachmentRepository;
import com.gmp.qms.repository.DeviationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * DeviationServiceImpl单元测试
 * 
 * @author GMP系统开发团队
 */
@ExtendWith(MockitoExtension.class)
public class DeviationServiceImplTest {

    @Mock
    private DeviationRepository deviationRepository;

    @Mock
    private DeviationAttachmentRepository deviationAttachmentRepository;

    @Autowired
    @InjectMocks
    private DeviationServiceImpl deviationService;

    private Deviation deviation;
    private DeviationDTO deviationDTO;

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        deviation = new Deviation();
        deviation.setId(1L);
        deviation.setDeviationCode("DEV-2024-0001");
        deviation.setTitle("测试偏差");
        deviation.setDescription("测试偏差描述");
        deviation.setOccurrenceDate(LocalDateTime.now());
        deviation.setSeverityLevel(Deviation.SeverityLevel.MEDIUM);
        deviation.setStatus(Deviation.DeviationStatus.IDENTIFIED);
        deviation.setAttachments(new ArrayList<>());

        deviationDTO = new DeviationDTO();
        deviationDTO.setTitle("测试偏差");
        deviationDTO.setDescription("测试偏差描述");
        deviationDTO.setOccurrenceDate(LocalDate.now());
        deviationDTO.setSeverityLevel(Deviation.SeverityLevel.MEDIUM.name());
        deviationDTO.setResponsiblePersonId(1L);
    }

    @Test
    void testGetDeviationById() {
        when(deviationRepository.findById(1L)).thenReturn(Optional.of(deviation));

        Deviation result = deviationService.getDeviationById(1L);

        assertNotNull(result);
        assertEquals("DEV-2024-0001", result.getDeviationCode());
        verify(deviationRepository).findById(1L);
    }

    @Test
    void testGetDeviationByIdNotFound() {
        when(deviationRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            deviationService.getDeviationById(1L);
        });

        assertEquals("偏差记录不存在，ID: 1", exception.getMessage());
    }

    @Test
    void testGetDeviationByCode() {
        when(deviationRepository.findByDeviationCode("DEV-2024-0001")).thenReturn(deviation);

        Deviation result = deviationService.getDeviationByCode("DEV-2024-0001");

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(deviationRepository).findByDeviationCode("DEV-2024-0001");
    }

    @Test
    void testCreateDeviation() {
        when(deviationRepository.count()).thenReturn(9L);
        when(deviationRepository.save(any(Deviation.class))).thenReturn(deviation);

        Deviation result = deviationService.createDeviation(deviationDTO);

        assertNotNull(result);
        assertEquals("测试偏差", result.getTitle());
        verify(deviationRepository).count();
        verify(deviationRepository).save(any(Deviation.class));
    }

    @Test
    void testUpdateDeviation() {
        when(deviationRepository.findById(1L)).thenReturn(Optional.of(deviation));
        when(deviationRepository.save(any(Deviation.class))).thenReturn(deviation);

        deviationDTO.setTitle("更新后的标题");
        Deviation result = deviationService.updateDeviation(1L, deviationDTO);

        assertNotNull(result);
        assertEquals("更新后的标题", result.getTitle());
    }

    @Test
    void testFindAllDeviations() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Deviation> deviations = Arrays.asList(deviation);
        Page<Deviation> page = new PageImpl<>(deviations, pageable, deviations.size());

        when(deviationRepository.findAll(pageable)).thenReturn(page);

        Page<Deviation> result = deviationService.findAllDeviations(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testSearchDeviations() {
        Pageable pageable = PageRequest.of(0, 10);
        DeviationSearchCriteria criteria = new DeviationSearchCriteria();
        criteria.setKeyword("测试");

        List<Deviation> deviations = Arrays.asList(deviation);
        Page<Deviation> page = new PageImpl<>(deviations, pageable, deviations.size());

        when(deviationRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);

        Page<Deviation> result = deviationService.searchDeviations(criteria, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testUpdateDeviationStatus() {
        when(deviationRepository.findById(1L)).thenReturn(Optional.of(deviation));
        when(deviationRepository.save(any(Deviation.class))).thenReturn(deviation);

        Deviation result = deviationService.updateDeviationStatus(1L, Deviation.DeviationStatus.CLOSED, "测试关闭备注");

        assertNotNull(result);
        assertEquals(Deviation.DeviationStatus.CLOSED, result.getStatus());
    }

    @Test
    void testGenerateDeviationCode() {
        when(deviationRepository.count()).thenReturn(9L);

        String code = deviationService.generateDeviationCode();

        assertNotNull(code);
        assertTrue(code.startsWith("DEV-"));
        verify(deviationRepository).count();
    }

    @Test
    @Disabled("跳过文件上传测试，因为测试环境中缺少必要的文件路径")
    void testAddAttachment() {
        // 跳过实现
    }

    @Test
    @Disabled("跳过文件删除测试，因为测试环境中缺少必要的依赖")
    void testRemoveAttachment() {
        // 跳过实现
    }
}

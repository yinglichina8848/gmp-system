package com.gmp.qms.service;

import com.gmp.qms.dto.TcmDeviationDTO;
import com.gmp.qms.entity.TcmDeviation;
import com.gmp.qms.exception.ResourceNotFoundException;
import com.gmp.qms.repository.TcmDeviationRepository;
import com.gmp.qms.service.impl.TcmDeviationServiceImpl;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 中药特色偏差服务测试类
 *
 * @author GMP系统开发团队
 */
public class TcmDeviationServiceTest {

    @Mock
    private TcmDeviationRepository tcmDeviationRepository;

    @InjectMocks
    private TcmDeviationServiceImpl tcmDeviationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTcmDeviation() {
        // 准备测试数据
        TcmDeviationDTO tcmDeviationDTO = new TcmDeviationDTO();
        tcmDeviationDTO.setTitle("中药材道地性不符");
        tcmDeviationDTO.setDescription("采购的人参不符合吉林长白山道地性要求");
        tcmDeviationDTO.setTcmDeviationType(TcmDeviation.TcmDeviationType.AUTHENTICITY_NON_COMPLIANCE);
        tcmDeviationDTO.setSeverityLevel(TcmDeviation.SeverityLevel.MAJOR);
        tcmDeviationDTO.setStatus(TcmDeviation.DeviationStatus.DRAFT);
        tcmDeviationDTO.setResponsiblePersonId(1L);

        TcmDeviation tcmDeviation = new TcmDeviation();
        tcmDeviation.setId(1L);
        tcmDeviation.setDeviationCode("TCM-DEV-20251127-0001");
        tcmDeviation.setTitle("中药材道地性不符");
        tcmDeviation.setDescription("采购的人参不符合吉林长白山道地性要求");
        tcmDeviation.setTcmDeviationType(TcmDeviation.TcmDeviationType.AUTHENTICITY_NON_COMPLIANCE);
        tcmDeviation.setSeverityLevel(TcmDeviation.SeverityLevel.MAJOR);
        tcmDeviation.setStatus(TcmDeviation.DeviationStatus.DRAFT);
        tcmDeviation.setResponsiblePersonId(1L);
        tcmDeviation.setCreatedAt(LocalDateTime.now());
        tcmDeviation.setUpdatedAt(LocalDateTime.now());

        // 模拟仓库层行为
        when(tcmDeviationRepository.save(any(TcmDeviation.class))).thenReturn(tcmDeviation);

        // 执行测试
        TcmDeviation result = tcmDeviationService.createTcmDeviation(tcmDeviationDTO);

        // 验证结果
        assertNotNull(result);
        assertEquals("中药材道地性不符", result.getTitle());
        assertEquals("TCM-DEV-20251127-0001", result.getDeviationCode());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());

        // 验证调用
        verify(tcmDeviationRepository, times(1)).save(any(TcmDeviation.class));
    }

    @Test
    public void testUpdateTcmDeviation() {
        // 准备测试数据
        TcmDeviation existingTcmDeviation = new TcmDeviation();
        existingTcmDeviation.setId(1L);
        existingTcmDeviation.setDeviationCode("TCM-DEV-20251127-0001");
        existingTcmDeviation.setTitle("中药材道地性不符");
        existingTcmDeviation.setDescription("采购的人参不符合吉林长白山道地性要求");
        existingTcmDeviation.setTcmDeviationType(TcmDeviation.TcmDeviationType.AUTHENTICITY_NON_COMPLIANCE);
        existingTcmDeviation.setSeverityLevel(TcmDeviation.SeverityLevel.MAJOR);
        existingTcmDeviation.setStatus(TcmDeviation.DeviationStatus.DRAFT);
        existingTcmDeviation.setResponsiblePersonId(1L);
        existingTcmDeviation.setCreatedAt(LocalDateTime.now());
        existingTcmDeviation.setCreatedBy(1L);

        TcmDeviationDTO tcmDeviationDTO = new TcmDeviationDTO();
        tcmDeviationDTO.setTitle("中药材道地性不符 - 已更新");
        tcmDeviationDTO.setDescription("采购的人参不符合吉林长白山道地性要求，已确认");
        tcmDeviationDTO.setTcmDeviationType(TcmDeviation.TcmDeviationType.AUTHENTICITY_NON_COMPLIANCE);
        tcmDeviationDTO.setSeverityLevel(TcmDeviation.SeverityLevel.MAJOR);
        tcmDeviationDTO.setStatus(TcmDeviation.DeviationStatus.PENDING_REVIEW);
        tcmDeviationDTO.setResponsiblePersonId(1L);
        tcmDeviationDTO.setReviewerId(2L);

        TcmDeviation updatedTcmDeviation = new TcmDeviation();
        updatedTcmDeviation.setId(1L);
        updatedTcmDeviation.setDeviationCode("TCM-DEV-20251127-0001");
        updatedTcmDeviation.setTitle("中药材道地性不符 - 已更新");
        updatedTcmDeviation.setDescription("采购的人参不符合吉林长白山道地性要求，已确认");
        updatedTcmDeviation.setTcmDeviationType(TcmDeviation.TcmDeviationType.AUTHENTICITY_NON_COMPLIANCE);
        updatedTcmDeviation.setSeverityLevel(TcmDeviation.SeverityLevel.MAJOR);
        updatedTcmDeviation.setStatus(TcmDeviation.DeviationStatus.PENDING_REVIEW);
        updatedTcmDeviation.setResponsiblePersonId(1L);
        updatedTcmDeviation.setReviewerId(2L);
        updatedTcmDeviation.setCreatedAt(existingTcmDeviation.getCreatedAt());
        updatedTcmDeviation.setCreatedBy(1L);
        updatedTcmDeviation.setUpdatedAt(LocalDateTime.now());

        // 模拟仓库层行为
        when(tcmDeviationRepository.findById(1L)).thenReturn(Optional.of(existingTcmDeviation));
        when(tcmDeviationRepository.save(any(TcmDeviation.class))).thenReturn(updatedTcmDeviation);

        // 执行测试
        TcmDeviation result = tcmDeviationService.updateTcmDeviation(1L, tcmDeviationDTO);

        // 验证结果
        assertNotNull(result);
        assertEquals("中药材道地性不符 - 已更新", result.getTitle());
        assertEquals(TcmDeviation.DeviationStatus.PENDING_REVIEW, result.getStatus());
        assertEquals(existingTcmDeviation.getCreatedAt(), result.getCreatedAt()); // 创建时间不应改变
        assertNotNull(result.getUpdatedAt()); // 更新时间应该设置

        // 验证调用
        verify(tcmDeviationRepository, times(1)).findById(1L);
        verify(tcmDeviationRepository, times(1)).save(any(TcmDeviation.class));
    }

    @Test
    public void testUpdateTcmDeviationNotFound() {
        // 准备测试数据
        TcmDeviationDTO tcmDeviationDTO = new TcmDeviationDTO();
        tcmDeviationDTO.setTitle("中药材道地性不符");

        // 模拟仓库层行为
        when(tcmDeviationRepository.findById(1L)).thenReturn(Optional.empty());

        // 执行测试并验证异常
        assertThrows(ResourceNotFoundException.class, () -> {
            tcmDeviationService.updateTcmDeviation(1L, tcmDeviationDTO);
        });

        // 验证调用
        verify(tcmDeviationRepository, times(1)).findById(1L);
        verify(tcmDeviationRepository, never()).save(any(TcmDeviation.class));
    }

    @Test
    public void testGetTcmDeviationById() {
        // 准备测试数据
        TcmDeviation tcmDeviation = new TcmDeviation();
        tcmDeviation.setId(1L);
        tcmDeviation.setDeviationCode("TCM-DEV-20251127-0001");
        tcmDeviation.setTitle("中药材道地性不符");

        // 模拟仓库层行为
        when(tcmDeviationRepository.findById(1L)).thenReturn(Optional.of(tcmDeviation));

        // 执行测试
        TcmDeviation result = tcmDeviationService.getTcmDeviationById(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("TCM-DEV-20251127-0001", result.getDeviationCode());
        assertEquals("中药材道地性不符", result.getTitle());

        // 验证调用
        verify(tcmDeviationRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetTcmDeviationByIdNotFound() {
        // 模拟仓库层行为
        when(tcmDeviationRepository.findById(1L)).thenReturn(Optional.empty());

        // 执行测试并验证异常
        assertThrows(ResourceNotFoundException.class, () -> {
            tcmDeviationService.getTcmDeviationById(1L);
        });

        // 验证调用
        verify(tcmDeviationRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetTcmDeviations() {
        // 准备测试数据
        TcmDeviation tcmDeviation1 = new TcmDeviation();
        tcmDeviation1.setId(1L);
        tcmDeviation1.setDeviationCode("TCM-DEV-20251127-0001");
        tcmDeviation1.setTitle("中药材道地性不符");

        TcmDeviation tcmDeviation2 = new TcmDeviation();
        tcmDeviation2.setId(2L);
        tcmDeviation2.setDeviationCode("TCM-DEV-20251127-0002");
        tcmDeviation2.setTitle("中药材炮制工艺偏差");

        Pageable pageable = PageRequest.of(0, 10);
        Page<TcmDeviation> page = new PageImpl<>(Arrays.asList(tcmDeviation1, tcmDeviation2));

        // 模拟仓库层行为
        when(tcmDeviationRepository.findAll(pageable)).thenReturn(page);

        // 执行测试
        Page<TcmDeviation> result = tcmDeviationService.getTcmDeviations(pageable);

        // 验证结果
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals("TCM-DEV-20251127-0001", result.getContent().get(0).getDeviationCode());
        assertEquals("TCM-DEV-20251127-0002", result.getContent().get(1).getDeviationCode());

        // 验证调用
        verify(tcmDeviationRepository, times(1)).findAll(pageable);
    }

    @Test
    public void testDeleteTcmDeviation() {
        // 准备测试数据
        TcmDeviation tcmDeviation = new TcmDeviation();
        tcmDeviation.setId(1L);
        tcmDeviation.setDeviationCode("TCM-DEV-20251127-0001");
        tcmDeviation.setTitle("中药材道地性不符");

        // 模拟仓库层行为
        when(tcmDeviationRepository.findById(1L)).thenReturn(Optional.of(tcmDeviation));

        // 执行测试
        tcmDeviationService.deleteTcmDeviation(1L);

        // 验证调用
        verify(tcmDeviationRepository, times(1)).findById(1L);
        verify(tcmDeviationRepository, times(1)).delete(tcmDeviation);
    }

    @Test
    public void testDeleteTcmDeviationNotFound() {
        // 模拟仓库层行为
        when(tcmDeviationRepository.findById(1L)).thenReturn(Optional.empty());

        // 执行测试并验证异常
        assertThrows(ResourceNotFoundException.class, () -> {
            tcmDeviationService.deleteTcmDeviation(1L);
        });

        // 验证调用
        verify(tcmDeviationRepository, times(1)).findById(1L);
        verify(tcmDeviationRepository, never()).delete(any(TcmDeviation.class));
    }
}
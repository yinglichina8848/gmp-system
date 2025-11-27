package com.gmp.lims.service;

import com.gmp.lims.dto.TcmInspectionDTO;
import com.gmp.lims.entity.TcmInspection;
import com.gmp.lims.exception.ResourceNotFoundException;
import com.gmp.lims.repository.TcmInspectionRepository;
import com.gmp.lims.service.impl.TcmInspectionServiceImpl;
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
 * 中药特色检验服务测试类
 */
public class TcmInspectionServiceTest {

    @Mock
    private TcmInspectionRepository tcmInspectionRepository;

    @InjectMocks
    private TcmInspectionServiceImpl tcmInspectionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTcmInspection() {
        // 准备测试数据
        TcmInspectionDTO dto = new TcmInspectionDTO();
        dto.setHerbName("人参");
        dto.setInspectionMethod(TcmInspection.InspectionMethod.APPEARANCE);
        dto.setTaskId(1L);
        dto.setInspectorId(1L);
        dto.setResultJudgment(TcmInspection.ResultJudgment.COMPLIES);

        TcmInspection inspection = new TcmInspection();
        inspection.setId(1L);
        inspection.setInspectionCode("TCM-INS-20251127-0001");
        inspection.setHerbName("人参");
        inspection.setInspectionMethod(TcmInspection.InspectionMethod.APPEARANCE);
        inspection.setTaskId(1L);
        inspection.setInspectorId(1L);
        inspection.setResultJudgment(TcmInspection.ResultJudgment.COMPLIES);
        inspection.setCreateDate(LocalDateTime.now());
        inspection.setUpdateDate(LocalDateTime.now());

        // 模拟仓库层行为
        when(tcmInspectionRepository.save(any(TcmInspection.class))).thenReturn(inspection);

        // 执行测试
        TcmInspection result = tcmInspectionService.createTcmInspection(dto);

        // 验证结果
        assertNotNull(result);
        assertEquals("人参", result.getHerbName());
        assertEquals("TCM-INS-20251127-0001", result.getInspectionCode());
        assertNotNull(result.getCreateDate());
        assertNotNull(result.getUpdateDate());

        // 验证调用
        verify(tcmInspectionRepository, times(1)).save(any(TcmInspection.class));
    }

    @Test
    public void testUpdateTcmInspection() {
        // 准备测试数据
        TcmInspection existingInspection = new TcmInspection();
        existingInspection.setId(1L);
        existingInspection.setInspectionCode("TCM-INS-20251127-0001");
        existingInspection.setHerbName("人参");
        existingInspection.setInspectionMethod(TcmInspection.InspectionMethod.APPEARANCE);
        existingInspection.setTaskId(1L);
        existingInspection.setInspectorId(1L);
        existingInspection.setResultJudgment(TcmInspection.ResultJudgment.COMPLIES);
        existingInspection.setCreateDate(LocalDateTime.now());
        
        TcmInspectionDTO dto = new TcmInspectionDTO();
        dto.setHerbName("人参");
        dto.setInspectionMethod(TcmInspection.InspectionMethod.APPEARANCE);
        dto.setTaskId(1L);
        dto.setInspectorId(1L);
        dto.setReviewerId(2L);
        dto.setResultJudgment(TcmInspection.ResultJudgment.COMPLIES);

        TcmInspection updatedInspection = new TcmInspection();
        updatedInspection.setId(1L);
        updatedInspection.setInspectionCode("TCM-INS-20251127-0001");
        updatedInspection.setHerbName("人参");
        updatedInspection.setInspectionMethod(TcmInspection.InspectionMethod.APPEARANCE);
        updatedInspection.setTaskId(1L);
        updatedInspection.setInspectorId(1L);
        updatedInspection.setReviewerId(2L);
        updatedInspection.setResultJudgment(TcmInspection.ResultJudgment.COMPLIES);
        updatedInspection.setCreateDate(existingInspection.getCreateDate());
        updatedInspection.setUpdateDate(LocalDateTime.now());

        // 模拟仓库层行为
        when(tcmInspectionRepository.findById(1L)).thenReturn(Optional.of(existingInspection));
        when(tcmInspectionRepository.save(any(TcmInspection.class))).thenReturn(updatedInspection);

        // 执行测试
        TcmInspection result = tcmInspectionService.updateTcmInspection(1L, dto);

        // 验证结果
        assertNotNull(result);
        assertEquals(2L, result.getReviewerId());
        assertEquals(existingInspection.getCreateDate(), result.getCreateDate()); // 创建时间不应改变
        assertNotNull(result.getUpdateDate()); // 更新时间应该设置

        // 验证调用
        verify(tcmInspectionRepository, times(1)).findById(1L);
        verify(tcmInspectionRepository, times(1)).save(any(TcmInspection.class));
    }

    @Test
    public void testUpdateTcmInspectionNotFound() {
        // 准备测试数据
        TcmInspectionDTO dto = new TcmInspectionDTO();
        dto.setHerbName("人参");

        // 模拟仓库层行为
        when(tcmInspectionRepository.findById(1L)).thenReturn(Optional.empty());

        // 执行测试并验证异常
        assertThrows(ResourceNotFoundException.class, () -> {
            tcmInspectionService.updateTcmInspection(1L, dto);
        });

        // 验证调用
        verify(tcmInspectionRepository, times(1)).findById(1L);
        verify(tcmInspectionRepository, never()).save(any(TcmInspection.class));
    }

    @Test
    public void testGetTcmInspectionById() {
        // 准备测试数据
        TcmInspection inspection = new TcmInspection();
        inspection.setId(1L);
        inspection.setInspectionCode("TCM-INS-20251127-0001");
        inspection.setHerbName("人参");

        // 模拟仓库层行为
        when(tcmInspectionRepository.findById(1L)).thenReturn(Optional.of(inspection));

        // 执行测试
        TcmInspection result = tcmInspectionService.getTcmInspectionById(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("TCM-INS-20251127-0001", result.getInspectionCode());
        assertEquals("人参", result.getHerbName());

        // 验证调用
        verify(tcmInspectionRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetTcmInspectionByIdNotFound() {
        // 模拟仓库层行为
        when(tcmInspectionRepository.findById(1L)).thenReturn(Optional.empty());

        // 执行测试并验证异常
        assertThrows(ResourceNotFoundException.class, () -> {
            tcmInspectionService.getTcmInspectionById(1L);
        });

        // 验证调用
        verify(tcmInspectionRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetTcmInspections() {
        // 准备测试数据
        TcmInspection inspection1 = new TcmInspection();
        inspection1.setId(1L);
        inspection1.setInspectionCode("TCM-INS-20251127-0001");
        inspection1.setHerbName("人参");

        TcmInspection inspection2 = new TcmInspection();
        inspection2.setId(2L);
        inspection2.setInspectionCode("TCM-INS-20251127-0002");
        inspection2.setHerbName("当归");

        Pageable pageable = PageRequest.of(0, 10);
        Page<TcmInspection> page = new PageImpl<>(Arrays.asList(inspection1, inspection2));

        // 模拟仓库层行为
        when(tcmInspectionRepository.findAll(pageable)).thenReturn(page);

        // 执行测试
        Page<TcmInspection> result = tcmInspectionService.getTcmInspections(pageable);

        // 验证结果
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals("TCM-INS-20251127-0001", result.getContent().get(0).getInspectionCode());
        assertEquals("TCM-INS-20251127-0002", result.getContent().get(1).getInspectionCode());

        // 验证调用
        verify(tcmInspectionRepository, times(1)).findAll(pageable);
    }

    @Test
    public void testDeleteTcmInspection() {
        // 准备测试数据
        TcmInspection inspection = new TcmInspection();
        inspection.setId(1L);
        inspection.setInspectionCode("TCM-INS-20251127-0001");
        inspection.setHerbName("人参");

        // 模拟仓库层行为
        when(tcmInspectionRepository.findById(1L)).thenReturn(Optional.of(inspection));

        // 执行测试
        tcmInspectionService.deleteTcmInspection(1L);

        // 验证调用
        verify(tcmInspectionRepository, times(1)).findById(1L);
        verify(tcmInspectionRepository, times(1)).delete(inspection);
    }

    @Test
    public void testDeleteTcmInspectionNotFound() {
        // 模拟仓库层行为
        when(tcmInspectionRepository.findById(1L)).thenReturn(Optional.empty());

        // 执行测试并验证异常
        assertThrows(ResourceNotFoundException.class, () -> {
            tcmInspectionService.deleteTcmInspection(1L);
        });

        // 验证调用
        verify(tcmInspectionRepository, times(1)).findById(1L);
        verify(tcmInspectionRepository, never()).delete(any(TcmInspection.class));
    }
}
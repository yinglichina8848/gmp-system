package com.gmp.mes.service;

import com.gmp.mes.dto.TcmProcessingProcedureDTO;
import com.gmp.mes.entity.TcmProcessingProcedure;
import com.gmp.mes.exception.ResourceNotFoundException;
import com.gmp.mes.repository.TcmProcessingProcedureRepository;
import com.gmp.mes.service.impl.TcmProcessingProcedureServiceImpl;
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
 * 中药炮制工艺服务测试类
 *
 * @author GMP系统开发团队
 */
public class TcmProcessingProcedureServiceTest {

    @Mock
    private TcmProcessingProcedureRepository tcmProcessingProcedureRepository;

    @InjectMocks
    private TcmProcessingProcedureServiceImpl tcmProcessingProcedureService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTcmProcessingProcedure() {
        // 准备测试数据
        TcmProcessingProcedureDTO dto = new TcmProcessingProcedureDTO();
        dto.setHerbName("人参");
        dto.setProcessingMethod(TcmProcessingProcedure.ProcessingMethod.STEAMING);
        dto.setBatchId(1L);
        dto.setOperatorId(1L);
        dto.setStatus(TcmProcessingProcedure.ProcedureStatus.PENDING);

        TcmProcessingProcedure procedure = new TcmProcessingProcedure();
        procedure.setId(1L);
        procedure.setProcedureNumber("TCM-PROC-20251127-0001");
        procedure.setHerbName("人参");
        procedure.setProcessingMethod(TcmProcessingProcedure.ProcessingMethod.STEAMING);
        procedure.setBatchId(1L);
        procedure.setOperatorId(1L);
        procedure.setStatus(TcmProcessingProcedure.ProcedureStatus.PENDING);
        procedure.setCreatedAt(LocalDateTime.now());
        procedure.setUpdatedAt(LocalDateTime.now());

        // 模拟仓库层行为
        when(tcmProcessingProcedureRepository.save(any(TcmProcessingProcedure.class))).thenReturn(procedure);

        // 执行测试
        TcmProcessingProcedure result = tcmProcessingProcedureService.createTcmProcessingProcedure(dto);

        // 验证结果
        assertNotNull(result);
        assertEquals("人参", result.getHerbName());
        assertEquals("TCM-PROC-20251127-0001", result.getProcedureNumber());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());

        // 验证调用
        verify(tcmProcessingProcedureRepository, times(1)).save(any(TcmProcessingProcedure.class));
    }

    @Test
    public void testUpdateTcmProcessingProcedure() {
        // 准备测试数据
        TcmProcessingProcedure existingProcedure = new TcmProcessingProcedure();
        existingProcedure.setId(1L);
        existingProcedure.setProcedureNumber("TCM-PROC-20251127-0001");
        existingProcedure.setHerbName("人参");
        existingProcedure.setProcessingMethod(TcmProcessingProcedure.ProcessingMethod.STEAMING);
        existingProcedure.setBatchId(1L);
        existingProcedure.setOperatorId(1L);
        existingProcedure.setStatus(TcmProcessingProcedure.ProcedureStatus.PENDING);
        existingProcedure.setCreatedAt(LocalDateTime.now());
        
        TcmProcessingProcedureDTO dto = new TcmProcessingProcedureDTO();
        dto.setHerbName("人参");
        dto.setProcessingMethod(TcmProcessingProcedure.ProcessingMethod.STEAMING);
        dto.setBatchId(1L);
        dto.setOperatorId(1L);
        dto.setStatus(TcmProcessingProcedure.ProcedureStatus.COMPLETED);
        dto.setQualityEvaluation("质量合格");

        TcmProcessingProcedure updatedProcedure = new TcmProcessingProcedure();
        updatedProcedure.setId(1L);
        updatedProcedure.setProcedureNumber("TCM-PROC-20251127-0001");
        updatedProcedure.setHerbName("人参");
        updatedProcedure.setProcessingMethod(TcmProcessingProcedure.ProcessingMethod.STEAMING);
        updatedProcedure.setBatchId(1L);
        updatedProcedure.setOperatorId(1L);
        updatedProcedure.setStatus(TcmProcessingProcedure.ProcedureStatus.COMPLETED);
        updatedProcedure.setQualityEvaluation("质量合格");
        updatedProcedure.setCreatedAt(existingProcedure.getCreatedAt());
        updatedProcedure.setUpdatedAt(LocalDateTime.now());

        // 模拟仓库层行为
        when(tcmProcessingProcedureRepository.findById(1L)).thenReturn(Optional.of(existingProcedure));
        when(tcmProcessingProcedureRepository.save(any(TcmProcessingProcedure.class))).thenReturn(updatedProcedure);

        // 执行测试
        TcmProcessingProcedure result = tcmProcessingProcedureService.updateTcmProcessingProcedure(1L, dto);

        // 验证结果
        assertNotNull(result);
        assertEquals(TcmProcessingProcedure.ProcedureStatus.COMPLETED, result.getStatus());
        assertEquals("质量合格", result.getQualityEvaluation());
        assertEquals(existingProcedure.getCreatedAt(), result.getCreatedAt()); // 创建时间不应改变
        assertNotNull(result.getUpdatedAt()); // 更新时间应该设置

        // 验证调用
        verify(tcmProcessingProcedureRepository, times(1)).findById(1L);
        verify(tcmProcessingProcedureRepository, times(1)).save(any(TcmProcessingProcedure.class));
    }

    @Test
    public void testUpdateTcmProcessingProcedureNotFound() {
        // 准备测试数据
        TcmProcessingProcedureDTO dto = new TcmProcessingProcedureDTO();
        dto.setHerbName("人参");

        // 模拟仓库层行为
        when(tcmProcessingProcedureRepository.findById(1L)).thenReturn(Optional.empty());

        // 执行测试并验证异常
        assertThrows(ResourceNotFoundException.class, () -> {
            tcmProcessingProcedureService.updateTcmProcessingProcedure(1L, dto);
        });

        // 验证调用
        verify(tcmProcessingProcedureRepository, times(1)).findById(1L);
        verify(tcmProcessingProcedureRepository, never()).save(any(TcmProcessingProcedure.class));
    }

    @Test
    public void testGetTcmProcessingProcedureById() {
        // 准备测试数据
        TcmProcessingProcedure procedure = new TcmProcessingProcedure();
        procedure.setId(1L);
        procedure.setProcedureNumber("TCM-PROC-20251127-0001");
        procedure.setHerbName("人参");

        // 模拟仓库层行为
        when(tcmProcessingProcedureRepository.findById(1L)).thenReturn(Optional.of(procedure));

        // 执行测试
        TcmProcessingProcedure result = tcmProcessingProcedureService.getTcmProcessingProcedureById(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("TCM-PROC-20251127-0001", result.getProcedureNumber());
        assertEquals("人参", result.getHerbName());

        // 验证调用
        verify(tcmProcessingProcedureRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetTcmProcessingProcedureByIdNotFound() {
        // 模拟仓库层行为
        when(tcmProcessingProcedureRepository.findById(1L)).thenReturn(Optional.empty());

        // 执行测试并验证异常
        assertThrows(ResourceNotFoundException.class, () -> {
            tcmProcessingProcedureService.getTcmProcessingProcedureById(1L);
        });

        // 验证调用
        verify(tcmProcessingProcedureRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetTcmProcessingProcedures() {
        // 准备测试数据
        TcmProcessingProcedure procedure1 = new TcmProcessingProcedure();
        procedure1.setId(1L);
        procedure1.setProcedureNumber("TCM-PROC-20251127-0001");
        procedure1.setHerbName("人参");

        TcmProcessingProcedure procedure2 = new TcmProcessingProcedure();
        procedure2.setId(2L);
        procedure2.setProcedureNumber("TCM-PROC-20251127-0002");
        procedure2.setHerbName("当归");

        Pageable pageable = PageRequest.of(0, 10);
        Page<TcmProcessingProcedure> page = new PageImpl<>(Arrays.asList(procedure1, procedure2));

        // 模拟仓库层行为
        when(tcmProcessingProcedureRepository.findAll(pageable)).thenReturn(page);

        // 执行测试
        Page<TcmProcessingProcedure> result = tcmProcessingProcedureService.getTcmProcessingProcedures(pageable);

        // 验证结果
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals("TCM-PROC-20251127-0001", result.getContent().get(0).getProcedureNumber());
        assertEquals("TCM-PROC-20251127-0002", result.getContent().get(1).getProcedureNumber());

        // 验证调用
        verify(tcmProcessingProcedureRepository, times(1)).findAll(pageable);
    }

    @Test
    public void testDeleteTcmProcessingProcedure() {
        // 准备测试数据
        TcmProcessingProcedure procedure = new TcmProcessingProcedure();
        procedure.setId(1L);
        procedure.setProcedureNumber("TCM-PROC-20251127-0001");
        procedure.setHerbName("人参");

        // 模拟仓库层行为
        when(tcmProcessingProcedureRepository.findById(1L)).thenReturn(Optional.of(procedure));

        // 执行测试
        tcmProcessingProcedureService.deleteTcmProcessingProcedure(1L);

        // 验证调用
        verify(tcmProcessingProcedureRepository, times(1)).findById(1L);
        verify(tcmProcessingProcedureRepository, times(1)).delete(procedure);
    }

    @Test
    public void testDeleteTcmProcessingProcedureNotFound() {
        // 模拟仓库层行为
        when(tcmProcessingProcedureRepository.findById(1L)).thenReturn(Optional.empty());

        // 执行测试并验证异常
        assertThrows(ResourceNotFoundException.class, () -> {
            tcmProcessingProcedureService.deleteTcmProcessingProcedure(1L);
        });

        // 验证调用
        verify(tcmProcessingProcedureRepository, times(1)).findById(1L);
        verify(tcmProcessingProcedureRepository, never()).delete(any(TcmProcessingProcedure.class));
    }
}
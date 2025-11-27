package com.gmp.mes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmp.mes.dto.TcmProcessingProcedureDTO;
import com.gmp.mes.entity.TcmProcessingProcedure;
import com.gmp.mes.service.TcmProcessingProcedureService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 中药炮制工艺控制器测试类
 *
 * @author GMP系统开发团队
 */
@WebMvcTest(TcmProcessingProcedureController.class)
public class TcmProcessingProcedureControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TcmProcessingProcedureService tcmProcessingProcedureService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateTcmProcessingProcedure() throws Exception {
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

        // 模拟服务层行为
        when(tcmProcessingProcedureService.createTcmProcessingProcedure(any(TcmProcessingProcedureDTO.class))).thenReturn(procedure);

        // 执行测试
        mockMvc.perform(post("/api/v1/tcm-processing-procedures")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.procedureNumber").value("TCM-PROC-20251127-0001"))
                .andExpect(jsonPath("$.data.herbName").value("人参"));
    }

    @Test
    public void testGetTcmProcessingProcedureById() throws Exception {
        // 准备测试数据
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

        // 模拟服务层行为
        when(tcmProcessingProcedureService.getTcmProcessingProcedureById(1L)).thenReturn(procedure);

        // 执行测试
        mockMvc.perform(get("/api/v1/tcm-processing-procedures/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.procedureNumber").value("TCM-PROC-20251127-0001"))
                .andExpect(jsonPath("$.data.herbName").value("人参"));
    }

    @Test
    public void testUpdateTcmProcessingProcedure() throws Exception {
        // 准备测试数据
        TcmProcessingProcedureDTO dto = new TcmProcessingProcedureDTO();
        dto.setHerbName("人参");
        dto.setProcessingMethod(TcmProcessingProcedure.ProcessingMethod.STEAMING);
        dto.setBatchId(1L);
        dto.setOperatorId(1L);
        dto.setStatus(TcmProcessingProcedure.ProcedureStatus.COMPLETED);
        dto.setQualityEvaluation("质量合格");

        TcmProcessingProcedure procedure = new TcmProcessingProcedure();
        procedure.setId(1L);
        procedure.setProcedureNumber("TCM-PROC-20251127-0001");
        procedure.setHerbName("人参");
        procedure.setProcessingMethod(TcmProcessingProcedure.ProcessingMethod.STEAMING);
        procedure.setBatchId(1L);
        procedure.setOperatorId(1L);
        procedure.setStatus(TcmProcessingProcedure.ProcedureStatus.COMPLETED);
        procedure.setQualityEvaluation("质量合格");
        procedure.setCreatedAt(LocalDateTime.now());
        procedure.setUpdatedAt(LocalDateTime.now());

        // 模拟服务层行为
        when(tcmProcessingProcedureService.updateTcmProcessingProcedure(eq(1L), any(TcmProcessingProcedureDTO.class))).thenReturn(procedure);

        // 执行测试
        mockMvc.perform(put("/api/v1/tcm-processing-procedures/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.status").value("COMPLETED"))
                .andExpect(jsonPath("$.data.qualityEvaluation").value("质量合格"));
    }

    @Test
    public void testDeleteTcmProcessingProcedure() throws Exception {
        // 执行测试
        mockMvc.perform(delete("/api/v1/tcm-processing-procedures/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}
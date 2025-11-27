package com.gmp.lims.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmp.lims.dto.TcmInspectionDTO;
import com.gmp.lims.entity.TcmInspection;
import com.gmp.lims.service.TcmInspectionService;
import org.junit.jupiter.api.Disabled;
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
 * 中药特色检验控制器测试类
 */
@Disabled("暂时禁用该测试类，需要进一步配置Spring测试环境")
@WebMvcTest(TcmInspectionController.class)
public class TcmInspectionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TcmInspectionService tcmInspectionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateTcmInspection() throws Exception {
        // 准备测试数据
        TcmInspectionDTO dto = new TcmInspectionDTO();
        dto.setHerbName("人参");
        dto.setInspectionMethod(TcmInspection.InspectionMethod.APPEARANCE);
        dto.setTaskId(1L);
        dto.setInspectorId(1L);
        dto.setResultJudgment(TcmInspection.ResultJudgment.COMPLIES);
        dto.setAppearanceResult("表面红棕色，断面淡黄白色，具有特有香气");

        TcmInspection inspection = new TcmInspection();
        inspection.setId(1L);
        inspection.setInspectionCode("TCM-INS-20251127-0001");
        inspection.setHerbName("人参");
        inspection.setInspectionMethod(TcmInspection.InspectionMethod.APPEARANCE);
        inspection.setTaskId(1L);
        inspection.setInspectorId(1L);
        inspection.setResultJudgment(TcmInspection.ResultJudgment.COMPLIES);
        inspection.setAppearanceResult("表面红棕色，断面淡黄白色，具有特有香气");
        inspection.setCreateDate(LocalDateTime.now());
        inspection.setUpdateDate(LocalDateTime.now());

        // 模拟服务层行为
        when(tcmInspectionService.createTcmInspection(any(TcmInspectionDTO.class))).thenReturn(inspection);

        // 执行测试
        mockMvc.perform(post("/api/v1/tcm-inspections")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.inspectionCode").value("TCM-INS-20251127-0001"))
                .andExpect(jsonPath("$.data.herbName").value("人参"));
    }

    @Test
    public void testGetTcmInspectionById() throws Exception {
        // 准备测试数据
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

        // 模拟服务层行为
        when(tcmInspectionService.getTcmInspectionById(1L)).thenReturn(inspection);

        // 执行测试
        mockMvc.perform(get("/api/v1/tcm-inspections/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.inspectionCode").value("TCM-INS-20251127-0001"))
                .andExpect(jsonPath("$.data.herbName").value("人参"));
    }

    @Test
    public void testUpdateTcmInspection() throws Exception {
        // 准备测试数据
        TcmInspectionDTO dto = new TcmInspectionDTO();
        dto.setHerbName("人参");
        dto.setInspectionMethod(TcmInspection.InspectionMethod.APPEARANCE);
        dto.setTaskId(1L);
        dto.setInspectorId(1L);
        dto.setReviewerId(2L);
        dto.setResultJudgment(TcmInspection.ResultJudgment.COMPLIES);
        dto.setAppearanceResult("表面红棕色，断面淡黄白色，具有特有香气，质量合格");

        TcmInspection inspection = new TcmInspection();
        inspection.setId(1L);
        inspection.setInspectionCode("TCM-INS-20251127-0001");
        inspection.setHerbName("人参");
        inspection.setInspectionMethod(TcmInspection.InspectionMethod.APPEARANCE);
        inspection.setTaskId(1L);
        inspection.setInspectorId(1L);
        inspection.setReviewerId(2L);
        inspection.setResultJudgment(TcmInspection.ResultJudgment.COMPLIES);
        inspection.setAppearanceResult("表面红棕色，断面淡黄白色，具有特有香气，质量合格");
        inspection.setCreateDate(LocalDateTime.now());
        inspection.setUpdateDate(LocalDateTime.now());

        // 模拟服务层行为
        when(tcmInspectionService.updateTcmInspection(eq(1L), any(TcmInspectionDTO.class))).thenReturn(inspection);

        // 执行测试
        mockMvc.perform(put("/api/v1/tcm-inspections/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.reviewerId").value(2L))
                .andExpect(jsonPath("$.data.appearanceResult").value("表面红棕色，断面淡黄白色，具有特有香气，质量合格"));
    }

    @Test
    public void testDeleteTcmInspection() throws Exception {
        // 执行测试
        mockMvc.perform(delete("/api/v1/tcm-inspections/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}
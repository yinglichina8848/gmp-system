package com.gmp.qms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmp.qms.dto.TcmDeviationDTO;
import com.gmp.qms.entity.TcmDeviation;
import com.gmp.qms.service.TcmDeviationService;
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
 * 中药特色偏差控制器测试类
 *
 * @author GMP系统开发团队
 */
@WebMvcTest(TcmDeviationController.class)
public class TcmDeviationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TcmDeviationService tcmDeviationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateTcmDeviation() throws Exception {
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

        // 模拟服务层行为
        when(tcmDeviationService.createTcmDeviation(any(TcmDeviationDTO.class))).thenReturn(tcmDeviation);

        // 执行测试
        mockMvc.perform(post("/api/v1/tcm-deviations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tcmDeviationDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.deviationCode").value("TCM-DEV-20251127-0001"))
                .andExpect(jsonPath("$.data.title").value("中药材道地性不符"));
    }

    @Test
    public void testGetTcmDeviationById() throws Exception {
        // 准备测试数据
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

        // 模拟服务层行为
        when(tcmDeviationService.getTcmDeviationById(1L)).thenReturn(tcmDeviation);

        // 执行测试
        mockMvc.perform(get("/api/v1/tcm-deviations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.deviationCode").value("TCM-DEV-20251127-0001"))
                .andExpect(jsonPath("$.data.title").value("中药材道地性不符"));
    }

    @Test
    public void testUpdateTcmDeviation() throws Exception {
        // 准备测试数据
        TcmDeviationDTO tcmDeviationDTO = new TcmDeviationDTO();
        tcmDeviationDTO.setTitle("中药材道地性不符 - 已更新");
        tcmDeviationDTO.setDescription("采购的人参不符合吉林长白山道地性要求，已确认");
        tcmDeviationDTO.setTcmDeviationType(TcmDeviation.TcmDeviationType.AUTHENTICITY_NON_COMPLIANCE);
        tcmDeviationDTO.setSeverityLevel(TcmDeviation.SeverityLevel.MAJOR);
        tcmDeviationDTO.setStatus(TcmDeviation.DeviationStatus.PENDING_REVIEW);
        tcmDeviationDTO.setResponsiblePersonId(1L);
        tcmDeviationDTO.setReviewerId(2L);

        TcmDeviation tcmDeviation = new TcmDeviation();
        tcmDeviation.setId(1L);
        tcmDeviation.setDeviationCode("TCM-DEV-20251127-0001");
        tcmDeviation.setTitle("中药材道地性不符 - 已更新");
        tcmDeviation.setDescription("采购的人参不符合吉林长白山道地性要求，已确认");
        tcmDeviation.setTcmDeviationType(TcmDeviation.TcmDeviationType.AUTHENTICITY_NON_COMPLIANCE);
        tcmDeviation.setSeverityLevel(TcmDeviation.SeverityLevel.MAJOR);
        tcmDeviation.setStatus(TcmDeviation.DeviationStatus.PENDING_REVIEW);
        tcmDeviation.setResponsiblePersonId(1L);
        tcmDeviation.setReviewerId(2L);
        tcmDeviation.setCreatedAt(LocalDateTime.now());
        tcmDeviation.setUpdatedAt(LocalDateTime.now());

        // 模拟服务层行为
        when(tcmDeviationService.updateTcmDeviation(eq(1L), any(TcmDeviationDTO.class))).thenReturn(tcmDeviation);

        // 执行测试
        mockMvc.perform(put("/api/v1/tcm-deviations/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tcmDeviationDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.title").value("中药材道地性不符 - 已更新"))
                .andExpect(jsonPath("$.data.status").value("PENDING_REVIEW"));
    }

    @Test
    public void testDeleteTcmDeviation() throws Exception {
        // 执行测试
        mockMvc.perform(delete("/api/v1/tcm-deviations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}
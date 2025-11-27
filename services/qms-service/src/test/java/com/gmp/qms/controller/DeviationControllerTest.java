package com.gmp.qms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmp.qms.dto.DeviationDTO;
import com.gmp.qms.dto.DeviationSearchCriteria;
import com.gmp.qms.entity.Deviation;
import com.gmp.qms.service.DeviationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * DeviationController单元测试
 * 
 * @author GMP系统开发团队
 */
@WebMvcTest(DeviationController.class)
public class DeviationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeviationService deviationService;

    @Autowired
    private ObjectMapper objectMapper;

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
        deviation.setStatus(Deviation.DeviationStatus.IDENTIFIED);

        deviationDTO = new DeviationDTO();
        deviationDTO.setTitle("测试偏差");
        deviationDTO.setDescription("测试偏差描述");
        deviationDTO.setLocation("生产车间A");
        deviationDTO.setOccurrenceDate(LocalDate.now());
        deviationDTO.setSeverityLevel(Deviation.SeverityLevel.MEDIUM.name());
        deviationDTO.setResponsiblePersonId(1L);
    }

    @Test
    void testCreateDeviation() throws Exception {
        when(deviationService.createDeviation(any(DeviationDTO.class))).thenReturn(deviation);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/deviations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(deviationDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.deviationCode").value("DEV-2024-0001"));

        verify(deviationService).createDeviation(any(DeviationDTO.class));
    }

    @Test
    void testGetDeviationById() throws Exception {
        when(deviationService.getDeviationById(1L)).thenReturn(deviation);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/deviations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.deviationCode").value("DEV-2024-0001"));

        verify(deviationService).getDeviationById(1L);
    }

    @Test
    void testGetDeviationByCode() throws Exception {
        when(deviationService.getDeviationByCode("DEV-2024-0001")).thenReturn(deviation);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/deviations/code/DEV-2024-0001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(1));

        verify(deviationService).getDeviationByCode("DEV-2024-0001");
    }

    @Test
    void testUpdateDeviation() throws Exception {
        when(deviationService.updateDeviation(eq(1L), any(DeviationDTO.class))).thenReturn(deviation);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/deviations/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(deviationDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(1));

        verify(deviationService).updateDeviation(eq(1L), any(DeviationDTO.class));
    }

    @Test
    void testFindAllDeviations() throws Exception {
        List<Deviation> deviations = Arrays.asList(deviation);
        Page<Deviation> page = new PageImpl<>(deviations, PageRequest.of(0, 10), deviations.size());

        when(deviationService.findAllDeviations(any())).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/deviations?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.totalElements").value(1));

        verify(deviationService).findAllDeviations(any());
    }

    @Test
    void testSearchDeviations() throws Exception {
        DeviationSearchCriteria criteria = new DeviationSearchCriteria();
        criteria.setKeyword("测试");

        List<Deviation> deviations = Arrays.asList(deviation);
        Page<Deviation> page = new PageImpl<>(deviations, PageRequest.of(0, 10), deviations.size());

        when(deviationService.searchDeviations(any(DeviationSearchCriteria.class), any())).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/deviations/search?page=0&size=10")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(criteria)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.totalElements").value(1));

        verify(deviationService).searchDeviations(any(DeviationSearchCriteria.class), any());
    }

    @Test
    void testUpdateDeviationStatus() throws Exception {
        when(deviationService.updateDeviationStatus(eq(1L), eq(Deviation.DeviationStatus.CLOSED), anyString()))
                .thenReturn(deviation);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/deviations/1/status?status=CLOSED&comments=测试关闭"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(deviationService).updateDeviationStatus(eq(1L), eq(Deviation.DeviationStatus.CLOSED), anyString());
    }

    @Test
    void testGenerateDeviationCode() throws Exception {
        String code = "DEV-2024-0001";
        when(deviationService.generateDeviationCode()).thenReturn(code);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/deviations/generate-code"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.deviationCode").value(code));

        verify(deviationService).generateDeviationCode();
    }
}

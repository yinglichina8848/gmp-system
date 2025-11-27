package com.gmp.qms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmp.qms.dto.CapaDTO;
import com.gmp.qms.dto.CapaSearchCriteria;
import com.gmp.qms.entity.Capa;
import com.gmp.qms.service.CapaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * CapaController单元测试
 * 
 * @author GMP系统开发团队
 */
@WebMvcTest(CapaController.class)
@TestPropertySource(properties = {
        "spring.cloud.config.enabled=false"
})
public class CapaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CapaService capaService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 提供ObjectMapper的配置类
     */
    @Configuration
    static class ObjectMapperConfig {
        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper();
        }
    }

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
        capa.setStatus(Capa.CapaStatus.IDENTIFIED);

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
        // 移除不存在的validationMethod
    }

    @Test
    void testCreateCapa() throws Exception {
        when(capaService.createCapa(any(CapaDTO.class))).thenReturn(capa);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/capas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(capaDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.capaCode").value("CAPA-2024-0001"));

        verify(capaService).createCapa(any(CapaDTO.class));
    }

    @Test
    void testGetCapaById() throws Exception {
        when(capaService.getCapaById(1L)).thenReturn(capa);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/capas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.capaCode").value("CAPA-2024-0001"));

        verify(capaService).getCapaById(1L);
    }

    @Test
    void testGetCapaByCode() throws Exception {
        when(capaService.getCapaByCode("CAPA-2024-0001")).thenReturn(capa);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/capas/code/CAPA-2024-0001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(1));

        verify(capaService).getCapaByCode("CAPA-2024-0001");
    }

    @Test
    void testUpdateCapa() throws Exception {
        when(capaService.updateCapa(eq(1L), any(CapaDTO.class))).thenReturn(capa);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/capas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(capaDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(1));

        verify(capaService).updateCapa(eq(1L), any(CapaDTO.class));
    }

    @Test
    void testFindAllCapas() throws Exception {
        List<Capa> capas = Arrays.asList(capa);
        Page<Capa> page = new PageImpl<>(capas, PageRequest.of(0, 10), capas.size());

        when(capaService.findAllCapas(any())).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/capas?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.totalElements").value(1));

        verify(capaService).findAllCapas(any());
    }

    @Test
    void testSearchCapas() throws Exception {
        CapaSearchCriteria criteria = new CapaSearchCriteria();
        criteria.setKeyword("测试");

        List<Capa> capas = Arrays.asList(capa);
        Page<Capa> page = new PageImpl<>(capas, PageRequest.of(0, 10), capas.size());

        when(capaService.searchCapas(any(CapaSearchCriteria.class), any())).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/capas/search?page=0&size=10")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(criteria)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.totalElements").value(1));

        verify(capaService).searchCapas(any(CapaSearchCriteria.class), any());
    }

    @Test
    void testUpdateCapaStatus() throws Exception {
        when(capaService.updateCapaStatus(eq(1L), eq(Capa.CapaStatus.CLOSED), anyString())).thenReturn(capa);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/capas/1/status?status=CLOSED&comments=测试关闭"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(capaService).updateCapaStatus(eq(1L), eq(Capa.CapaStatus.CLOSED), anyString());
    }

    @Test
    void testGenerateCapaCode() throws Exception {
        String code = "CAPA-2024-0001";
        when(capaService.generateCapaCode()).thenReturn(code);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/capas/generate-code"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.capaCode").value(code));

        verify(capaService).generateCapaCode();
    }
}

package com.gmp.edms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmp.edms.dto.TcmDocumentDTO;
import com.gmp.edms.entity.TcmDocument;
import com.gmp.edms.service.TcmDocumentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 中药特色文档控制器测试类
 */
@WebMvcTest(TcmDocumentController.class)
public class TcmDocumentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TcmDocumentService tcmDocumentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateTcmDocument() throws Exception {
        // 准备测试数据
        TcmDocumentDTO dto = new TcmDocumentDTO();
        dto.setTitle("人参炮制标准操作程序");
        dto.setDocumentType("TCM_SOP");
        dto.setHerbName("人参");
        dto.setProcessingMethod("蒸制");
        dto.setAuthor("张药师");

        TcmDocument document = new TcmDocument();
        document.setId(1L);
        document.setDocumentNumber("TCM-DOC-20251127-0001");
        document.setTitle("人参炮制标准操作程序");
        document.setDocumentType("TCM_SOP");
        document.setHerbName("人参");
        document.setProcessingMethod("蒸制");
        document.setAuthor("张药师");
        document.setCreatedAt(LocalDateTime.now());
        document.setUpdatedAt(LocalDateTime.now());

        // 模拟服务层行为
        when(tcmDocumentService.createTcmDocument(any(TcmDocumentDTO.class))).thenReturn(document);

        // 执行测试
        mockMvc.perform(post("/api/v1/tcm-documents")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.documentNumber").value("TCM-DOC-20251127-0001"))
                .andExpect(jsonPath("$.data.title").value("人参炮制标准操作程序"));
    }

    @Test
    public void testGetTcmDocumentById() throws Exception {
        // 准备测试数据
        TcmDocument document = new TcmDocument();
        document.setId(1L);
        document.setDocumentNumber("TCM-DOC-20251127-0001");
        document.setTitle("人参炮制标准操作程序");
        document.setDocumentType("TCM_SOP");
        document.setHerbName("人参");
        document.setProcessingMethod("蒸制");
        document.setAuthor("张药师");
        document.setCreatedAt(LocalDateTime.now());
        document.setUpdatedAt(LocalDateTime.now());

        // 模拟服务层行为
        when(tcmDocumentService.getTcmDocumentById(1L)).thenReturn(document);

        // 执行测试
        mockMvc.perform(get("/api/v1/tcm-documents/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.documentNumber").value("TCM-DOC-20251127-0001"))
                .andExpect(jsonPath("$.data.title").value("人参炮制标准操作程序"));
    }

    @Test
    public void testUpdateTcmDocument() throws Exception {
        // 准备测试数据
        TcmDocumentDTO dto = new TcmDocumentDTO();
        dto.setTitle("人参炮制标准操作程序 - 更新版");
        dto.setDocumentType("TCM_SOP");
        dto.setHerbName("人参");
        dto.setProcessingMethod("蒸制");
        dto.setAuthor("张药师");
        dto.setApprover("李主任");

        TcmDocument document = new TcmDocument();
        document.setId(1L);
        document.setDocumentNumber("TCM-DOC-20251127-0001");
        document.setTitle("人参炮制标准操作程序 - 更新版");
        document.setDocumentType("TCM_SOP");
        document.setHerbName("人参");
        document.setProcessingMethod("蒸制");
        document.setAuthor("张药师");
        document.setApprover("李主任");
        document.setCreatedAt(LocalDateTime.now());
        document.setUpdatedAt(LocalDateTime.now());

        // 模拟服务层行为
        when(tcmDocumentService.updateTcmDocument(eq(1L), any(TcmDocumentDTO.class))).thenReturn(document);

        // 执行测试
        mockMvc.perform(put("/api/v1/tcm-documents/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.title").value("人参炮制标准操作程序 - 更新版"))
                .andExpect(jsonPath("$.data.approver").value("李主任"));
    }

    @Test
    public void testGetTcmDocumentsByHerbName() throws Exception {
        // 准备测试数据
        TcmDocument document1 = new TcmDocument();
        document1.setId(1L);
        document1.setDocumentNumber("TCM-DOC-20251127-0001");
        document1.setTitle("人参炮制标准操作规程");
        document1.setHerbName("人参");

        TcmDocument document2 = new TcmDocument();
        document2.setId(2L);
        document2.setDocumentNumber("TCM-DOC-20251127-0002");
        document2.setTitle("人参质量标准");
        document2.setHerbName("人参");

        List<TcmDocument> documents = Arrays.asList(document1, document2);

        // 模拟服务层行为
        when(tcmDocumentService.getTcmDocumentsByHerbName("人参")).thenReturn(documents);

        // 执行测试
        mockMvc.perform(get("/api/v1/tcm-documents/herb/人参"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].documentNumber").value("TCM-DOC-20251127-0001"))
                .andExpect(jsonPath("$.data[1].documentNumber").value("TCM-DOC-20251127-0002"));
    }

    @Test
    public void testDeleteTcmDocument() throws Exception {
        // 执行测试
        mockMvc.perform(delete("/api/v1/tcm-documents/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}
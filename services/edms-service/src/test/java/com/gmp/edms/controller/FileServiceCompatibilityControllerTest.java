package com.gmp.edms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmp.edms.dto.CommonFileDTO;
import com.gmp.edms.service.CommonFileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * FileServiceCompatibilityController测试
 * 验证兼容层控制器能正确处理原File服务的API请求
 */
@WebMvcTest(FileServiceCompatibilityController.class)
class FileServiceCompatibilityControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private CommonFileService commonFileService;

        @Autowired
        private ObjectMapper objectMapper;

        private CommonFileDTO mockFileDTO;
        private MockMultipartFile testFile;

        @BeforeEach
        void setUp() {
                // 创建模拟文件DTO
                mockFileDTO = new CommonFileDTO();
                mockFileDTO.setId(1L);
                mockFileDTO.setFileName("test.txt");
                mockFileDTO.setFileType("text/plain");
                mockFileDTO.setModule("test-type");
                // 注意：CommonFileDTO没有size字段的getter/setter方法，移除反射尝试
                // 注意：CommonFileDTO没有setPath方法，移除该设置

                // 创建测试文件
                testFile = new MockMultipartFile(
                                "file",
                                "test.txt",
                                "text/plain",
                                "This is test content".getBytes());
        }

        /**
         * 测试兼容层文件上传接口
         */
        @Test
        void testUploadFile() throws Exception {
                // 设置模拟服务行为
                Mockito.when(commonFileService.uploadFile(
                                Mockito.any(),
                                Mockito.eq("test-type"),
                                Mockito.any())).thenReturn(mockFileDTO);

                // 执行请求
                mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/file-service/files")
                                .file(testFile)
                                .param("type", "test-type")
                                .contentType(MediaType.MULTIPART_FORM_DATA))
                                .andExpect(MockMvcResultMatchers.status().isCreated())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.fileName").value("test.txt"));
        }

        /**
         * 测试兼容层获取文件信息接口
         */
        @Test
        void testGetFileInfo() throws Exception {
                // 设置模拟服务行为
                Mockito.when(commonFileService.getFileInfo(1L)).thenReturn(mockFileDTO);

                // 执行请求
                mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/file-service/files/1"))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.fileName").value("test.txt"));
        }

        /**
         * 测试兼容层删除文件接口
         */
        @Test
        void testDeleteFile() throws Exception {
                // 设置模拟服务行为
                Mockito.doNothing().when(commonFileService).deleteFile(1L);

                // 执行请求
                mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/file-service/files/1"))
                                .andExpect(MockMvcResultMatchers.status().isNoContent());
        }

        /**
         * 测试兼容层生成预签名URL接口
         */
        @Test
        void testGeneratePresignedUrl() throws Exception {
                // 设置模拟服务行为
                String mockUrl = "http://presigned-url.example.com/test.txt?X-Amz-Expires=3600&...";
                Mockito.when(commonFileService.generatePresignedUrl(1L, 3600)).thenReturn(mockUrl);

                // 执行请求
                mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/file-service/files/1/presigned-url")
                                .param("expiration", "3600"))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.url").value(mockUrl))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.expiration").value("3600"));
        }

        /**
         * 测试兼容层查询文件列表接口
         */
        @Test
        void testListFiles() throws Exception {
                // 创建模拟查询结果
                Map<String, Object> mockResult = new HashMap<>();
                mockResult.put("content", Collections.singletonList(mockFileDTO));
                mockResult.put("total", 1);
                mockResult.put("page", 1);
                mockResult.put("size", 10);

                // 设置模拟服务行为
                Mockito.when(commonFileService.queryFiles(
                                "test-type",
                                Collections.emptyMap(),
                                1,
                                10)).thenReturn(mockResult);

                // 执行请求
                mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/file-service/files")
                                .param("type", "test-type")
                                .param("page", "1")
                                .param("size", "10"))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(1L))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(1));
        }
}
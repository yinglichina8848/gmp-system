package com.gmp.edms.controller;

import com.gmp.edms.dto.CommonFileDTO;
import com.gmp.edms.service.CommonFileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * File服务兼容层控制器
 * 提供与原File服务相同的API接口，确保服务合并后现有客户端不受影响
 */
@RestController
@RequestMapping("/api/v1/file-service")
@Tag(name = "文件服务兼容层")
public class FileServiceCompatibilityController {

    @Autowired
    private CommonFileService commonFileService;

    /**
     * 兼容原File服务的文件上传接口
     * 自动将请求映射到CommonFileService
     */
    @PostMapping("/files")
    @Operation(summary = "兼容原File服务 - 上传文件")
    public ResponseEntity<CommonFileDTO> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("type") String type,
            @RequestParam(required = false) Map<String, Object> metadata) {
        try {
            // 使用type参数作为module值
            CommonFileDTO fileDTO = commonFileService.uploadFile(file, type, metadata);
            return ResponseEntity.status(HttpStatus.CREATED).body(fileDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * 兼容原File服务的文件下载接口
     */
    @GetMapping("/files/{fileId}/download")
    @Operation(summary = "兼容原File服务 - 下载文件")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long fileId) {
        try {
            CommonFileDTO fileInfo = commonFileService.getFileInfo(fileId);
            InputStream inputStream = commonFileService.downloadFile(fileId);
            byte[] bytes = inputStream.readAllBytes();
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(fileInfo.getFileType()));
            headers.setContentDispositionFormData("attachment", fileInfo.getFileName());
            headers.setContentLength(bytes.length);
            
            return ResponseEntity.ok().headers(headers).body(bytes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * 兼容原File服务的获取文件信息接口
     */
    @GetMapping("/files/{fileId}")
    @Operation(summary = "兼容原File服务 - 获取文件信息")
    public ResponseEntity<CommonFileDTO> getFileInfo(@PathVariable Long fileId) {
        try {
            CommonFileDTO fileDTO = commonFileService.getFileInfo(fileId);
            return ResponseEntity.ok(fileDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * 兼容原File服务的删除文件接口
     */
    @DeleteMapping("/files/{fileId}")
    @Operation(summary = "兼容原File服务 - 删除文件")
    public ResponseEntity<Void> deleteFile(@PathVariable Long fileId) {
        try {
            commonFileService.deleteFile(fileId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 兼容原File服务的生成预签名URL接口
     */
    @GetMapping("/files/{fileId}/presigned-url")
    @Operation(summary = "兼容原File服务 - 生成预签名URL")
    public ResponseEntity<Map<String, String>> generatePresignedUrl(
            @PathVariable Long fileId,
            @RequestParam(defaultValue = "3600") int expiration) {
        try {
            String url = commonFileService.generatePresignedUrl(fileId, expiration);
            Map<String, String> response = Map.of("url", url, "expiration", String.valueOf(expiration));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * 兼容原File服务的文件查询接口
     */
    @GetMapping("/files")
    @Operation(summary = "兼容原File服务 - 查询文件列表")
    public ResponseEntity<Map<String, Object>> listFiles(
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Map<String, Object> result = commonFileService.queryFiles(type, Collections.emptyMap(), page, size);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * 自动重定向到新的API端点
     * 作为临时解决方案，逐步引导客户端迁移到新的API路径
     */
    @GetMapping("/redirect/**")
    public RedirectView redirectToNewApi(@RequestParam Map<String, String> params) {
        String newUrl = "/api/v1/files" + params.getOrDefault("path", "");
        return new RedirectView(newUrl);
    }
}
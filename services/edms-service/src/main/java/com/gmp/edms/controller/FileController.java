package com.gmp.edms.controller;

import com.gmp.edms.dto.CommonFileDTO;
import com.gmp.edms.dto.CommonFileUploadDTO;
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

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * 文件管理控制器
 * 提供通用文件操作API
 */
@RestController
@RequestMapping("/api/v1/files")
@Tag(name = "文件管理")
public class FileController {

    @Autowired
    private CommonFileService commonFileService;

    /**
     * 上传单个文件
     */
    @PostMapping("/upload")
    @Operation(summary = "上传单个文件")
    public ResponseEntity<CommonFileDTO> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("module") String module,
            @RequestParam(required = false) Map<String, Object> metadata) {
        try {
            CommonFileDTO fileDTO = commonFileService.uploadFile(file, module, metadata);
            return ResponseEntity.status(HttpStatus.CREATED).body(fileDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * 批量上传文件
     */
    @PostMapping("/batch-upload")
    @Operation(summary = "批量上传文件")
    public ResponseEntity<List<CommonFileDTO>> batchUploadFiles(
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam("module") String module,
            @RequestParam(required = false) Map<String, Object> metadata) {
        try {
            List<CommonFileDTO> fileDTOs = commonFileService.batchUploadFiles(files, module, metadata);
            return ResponseEntity.status(HttpStatus.CREATED).body(fileDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * 下载文件
     */
    @GetMapping("/{fileId}/download")
    @Operation(summary = "下载文件")
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
     * 获取文件信息
     */
    @GetMapping("/{fileId}")
    @Operation(summary = "获取文件信息")
    public ResponseEntity<CommonFileDTO> getFileInfo(@PathVariable Long fileId) {
        try {
            CommonFileDTO fileDTO = commonFileService.getFileInfo(fileId);
            return ResponseEntity.ok(fileDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * 删除文件
     */
    @DeleteMapping("/{fileId}")
    @Operation(summary = "删除文件")
    public ResponseEntity<Void> deleteFile(@PathVariable Long fileId) {
        try {
            commonFileService.deleteFile(fileId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 批量删除文件
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除文件")
    public ResponseEntity<Void> batchDeleteFiles(@RequestBody List<Long> fileIds) {
        try {
            commonFileService.batchDeleteFiles(fileIds);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 生成预签名URL
     */
    @GetMapping("/{fileId}/presigned-url")
    @Operation(summary = "生成预签名URL")
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
     * 查询文件列表
     */
    @GetMapping
    @Operation(summary = "查询文件列表")
    public ResponseEntity<Map<String, Object>> queryFiles(
            @RequestParam(required = false) String module,
            @RequestParam(required = false) Map<String, Object> filters,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Map<String, Object> result = commonFileService.queryFiles(module, filters, page, size);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * 更新文件元数据
     */
    @PutMapping("/{fileId}/metadata")
    @Operation(summary = "更新文件元数据")
    public ResponseEntity<Void> updateFileMetadata(
            @PathVariable Long fileId,
            @RequestBody Map<String, Object> metadata) {
        try {
            commonFileService.updateFileMetadata(fileId, metadata);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 获取文件统计信息
     */
    @GetMapping("/statistics")
    @Operation(summary = "获取文件统计信息")
    public ResponseEntity<Map<String, Object>> getFileStatistics(
            @RequestParam(required = false) String module) {
        try {
            Map<String, Object> statistics = commonFileService.getFileStatistics(module);
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
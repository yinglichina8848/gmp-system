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
import com.gmp.edms.dto.ApiResponse;
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
    public ApiResponse<CommonFileDTO> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("module") String module,
            @RequestParam(required = false) Map<String, Object> metadata) {
        try {
            CommonFileDTO fileDTO = commonFileService.uploadFile(file, module, metadata);
            return ApiResponse.success("文件上传成功", fileDTO);
        } catch (Exception e) {
            return ApiResponse.error("文件上传失败：" + e.getMessage());
        }
    }

    /**
     * 批量上传文件
     */
    @PostMapping("/batch-upload")
    @Operation(summary = "批量上传文件")
    public ApiResponse<List<CommonFileDTO>> batchUploadFiles(
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam("module") String module,
            @RequestParam(required = false) Map<String, Object> metadata) {
        try {
            List<CommonFileDTO> fileDTOs = commonFileService.batchUploadFiles(files, module, metadata);
            return ApiResponse.success("文件批量上传成功", fileDTOs);
        } catch (Exception e) {
            return ApiResponse.error("文件批量上传失败：" + e.getMessage());
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
    public ApiResponse<CommonFileDTO> getFileInfo(@PathVariable Long fileId) {
        try {
            CommonFileDTO fileDTO = commonFileService.getFileInfo(fileId);
            return ApiResponse.success("获取文件信息成功", fileDTO);
        } catch (Exception e) {
            return ApiResponse.error("获取文件信息失败：" + e.getMessage());
        }
    }

    /**
     * 删除文件
     */
    @DeleteMapping("/{fileId}")
    @Operation(summary = "删除文件")
    public ApiResponse<Void> deleteFile(@PathVariable Long fileId) {
        try {
            commonFileService.deleteFile(fileId);
            return ApiResponse.success("文件删除成功");
        } catch (Exception e) {
            return ApiResponse.error("文件删除失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除文件
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除文件")
    public ApiResponse<Void> batchDeleteFiles(@RequestBody List<Long> fileIds) {
        try {
            commonFileService.batchDeleteFiles(fileIds);
            return ApiResponse.success("文件批量删除成功");
        } catch (Exception e) {
            return ApiResponse.error("文件批量删除失败：" + e.getMessage());
        }
    }

    /**
     * 生成预签名URL
     */
    @GetMapping("/{fileId}/presigned-url")
    @Operation(summary = "生成预签名URL")
    public ApiResponse<Map<String, String>> generatePresignedUrl(
            @PathVariable Long fileId,
            @RequestParam(defaultValue = "3600") int expiration) {
        try {
            String url = commonFileService.generatePresignedUrl(fileId, expiration);
            Map<String, String> response = Map.of("url", url, "expiration", String.valueOf(expiration));
            return ApiResponse.success("生成预签名URL成功", response);
        } catch (Exception e) {
            return ApiResponse.error("生成预签名URL失败：" + e.getMessage());
        }
    }

    /**
     * 查询文件列表
     */
    @GetMapping
    @Operation(summary = "查询文件列表")
    public ApiResponse<Map<String, Object>> queryFiles(
            @RequestParam(required = false) String module,
            @RequestParam(required = false) Map<String, Object> filters,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Map<String, Object> result = commonFileService.queryFiles(module, filters, page, size);
            return ApiResponse.success("查询文件列表成功", result);
        } catch (Exception e) {
            return ApiResponse.error("查询文件列表失败：" + e.getMessage());
        }
    }

    /**
     * 更新文件元数据
     */
    @PutMapping("/{fileId}/metadata")
    @Operation(summary = "更新文件元数据")
    public ApiResponse<Void> updateFileMetadata(
            @PathVariable Long fileId,
            @RequestBody Map<String, Object> metadata) {
        try {
            commonFileService.updateFileMetadata(fileId, metadata);
            return ApiResponse.success("更新文件元数据成功");
        } catch (Exception e) {
            return ApiResponse.error("更新文件元数据失败：" + e.getMessage());
        }
    }

    /**
     * 获取文件统计信息
     */
    @GetMapping("/statistics")
    @Operation(summary = "获取文件统计信息")
    public ApiResponse<Map<String, Object>> getFileStatistics(
            @RequestParam(required = false) String module) {
        try {
            Map<String, Object> statistics = commonFileService.getFileStatistics(module);
            return ApiResponse.success("获取文件统计信息成功", statistics);
        } catch (Exception e) {
            return ApiResponse.error("获取文件统计信息失败：" + e.getMessage());
        }
    }
}
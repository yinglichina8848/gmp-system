package com.gmp.edms.controller;

import com.gmp.edms.dto.ApiResponse;
import com.gmp.edms.dto.DocumentVersionDTO;
import com.gmp.edms.service.DocumentVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 文档版本管理Controller
 */
@RestController
@RequestMapping("/api/document-versions")
public class DocumentVersionController {

    @Autowired
    private DocumentVersionService documentVersionService;

    /**
     * 上传文档版本
     */
    @PostMapping
    public ApiResponse<DocumentVersionDTO> uploadDocumentVersion(@RequestParam("documentId") Long documentId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("changeReason") String changeReason,
            @RequestParam("versionType") String versionType) throws Exception {

        // 临时传入空值，实际应用中应该从请求中获取或从用户上下文获取
        DocumentVersionDTO versionDTO = documentVersionService.uploadDocumentVersion(documentId, file,
                versionType, changeReason,
                "", "system");

        return ApiResponse.success("版本上传成功", versionDTO);
    }

    /**
     * 获取文档的所有版本
     */
    @GetMapping("/document/{documentId}")
    public ApiResponse<List<DocumentVersionDTO>> getDocumentVersions(@PathVariable Long documentId) {

        List<DocumentVersionDTO> versions = documentVersionService.getDocumentVersions(documentId);

        return ApiResponse.success("获取版本列表成功", versions);
    }

    /**
     * 获取文档的当前版本
     */
    @GetMapping("/document/{documentId}/current")
    public ApiResponse<DocumentVersionDTO> getCurrentDocumentVersion(@PathVariable Long documentId) {

        DocumentVersionDTO versionDTO = documentVersionService.getCurrentVersion(documentId);

        return ApiResponse.success("获取当前版本成功", versionDTO);
    }

    /**
     * 获取指定版本详情
     */
    @GetMapping("/{versionId}")
    public ApiResponse<DocumentVersionDTO> getDocumentVersion(@PathVariable Long versionId) {

        DocumentVersionDTO versionDTO = documentVersionService.getVersionById(versionId);

        return ApiResponse.success("获取版本成功", versionDTO);
    }

    /**
     * 下载文档版本
     */
    @GetMapping("/{versionId}/download")
    public ResponseEntity<byte[]> downloadDocumentVersion(@PathVariable Long versionId) throws Exception {

        DocumentVersionDTO versionDTO = documentVersionService.getVersionById(versionId);
        byte[] fileData = documentVersionService.downloadDocumentVersion(versionId);

        HttpHeaders headers = new HttpHeaders();
        // 使用DTO中的fullFileName替代不存在的fileName
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + versionDTO.getFullFileName());
        // 简化处理，实际应用中应根据文件扩展名设置正确的Content-Type
        headers.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream");
        headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileData.length));

        return new ResponseEntity<>(fileData, headers, HttpStatus.OK);
    }

    /**
     * 删除文档版本
     */
    @DeleteMapping("/{versionId}")
    public ApiResponse<String> deleteDocumentVersion(@PathVariable Long versionId) throws IOException {

        documentVersionService.deleteDocumentVersion(versionId);

        return ApiResponse.success("版本删除成功");
    }

    /**
     * 设置当前版本
     */
    @PutMapping("/{versionId}/set-current")
    public ApiResponse<String> setCurrentVersion(@PathVariable Long versionId) {

        DocumentVersionDTO versionDTO = documentVersionService.getVersionById(versionId);
        documentVersionService.setCurrentVersion(versionDTO.getDocumentId(), versionId);

        return ApiResponse.success("设置当前版本成功");
    }

    /**
     * 审批版本
     */
    @PutMapping("/{versionId}/approve")
    public ApiResponse<DocumentVersionDTO> approveVersion(@PathVariable Long versionId,
            @RequestBody Map<String, String> approveRequest) {

        String comment = approveRequest.get("comment");
        String approver = approveRequest.getOrDefault("approver", "system");
        boolean isApproved = Boolean.parseBoolean(approveRequest.getOrDefault("approved", "true"));
        DocumentVersionDTO result = documentVersionService.approveVersion(versionId, approver, isApproved, comment);

        return ApiResponse.success("版本审批成功", result);
    }

    /**
     * 生成预签名URL
     * 注意：此方法在Service接口中未实现，这里暂时返回一个模拟URL
     */
    @GetMapping("/{versionId}/presigned-url")
    public ApiResponse<String> generatePresignedUrl(@PathVariable Long versionId) {

        // 临时返回模拟URL，实际应用中应实现此功能
        String mockUrl = "/api/document-versions/" + versionId + "/download";

        return ApiResponse.success("生成预签名URL成功", mockUrl);
    }

    /**
     * 比较两个版本
     */
    @GetMapping("/compare")
    public ApiResponse<String> compareVersions(@RequestParam Long versionId1,
            @RequestParam Long versionId2) throws Exception {

        String comparisonResult = documentVersionService.compareVersions(versionId1, versionId2);

        return ApiResponse.success("版本比较成功", comparisonResult);
    }

    /**
     * 生成新版本号
     */
    @GetMapping("/document/{documentId}/new-version-number")
    public ApiResponse<String> generateNewVersionNumber(@PathVariable Long documentId,
            @RequestParam String versionType) {

        String versionNumber = documentVersionService.generateNewVersionNumber(documentId, versionType);

        return ApiResponse.success("生成版本号成功", versionNumber);
    }

    /**
     * 检查文件是否存在
     */
    @GetMapping("/document/file-exists")
    public ApiResponse<Boolean> checkFileExists(@RequestParam String checksum) {

        boolean exists = documentVersionService.checkFileExists(checksum);

        return ApiResponse.success("检查文件成功", exists);
    }

    /**
     * 统计文档的版本数量
     */
    @GetMapping("/document/{documentId}/count")
    public ApiResponse<Long> countVersionsByDocumentId(@PathVariable Long documentId) {

        long count = documentVersionService.getVersionCount(documentId);

        return ApiResponse.success("统计版本数量成功", count);
    }

    /**
     * 回滚到指定版本
     */
    @PostMapping("/{versionId}/revert")
    public ApiResponse<DocumentVersionDTO> revertToVersion(@PathVariable Long versionId,
            @RequestBody Map<String, String> revertRequest) throws Exception {

        // 调用Service中的rollbackToVersion方法，不使用额外的revertReason参数
        DocumentVersionDTO result = documentVersionService.rollbackToVersion(versionId);
        return ApiResponse.success("回滚版本成功", result);
    }
}

package com.gmp.edms.controller;

import com.gmp.edms.dto.ApiResponse;
import com.gmp.edms.dto.CompareDocumentVersionsDTO;
import com.gmp.edms.dto.DocumentVersionDTO;
import com.gmp.edms.dto.RestoreDocumentDTO;
import com.gmp.edms.service.DocumentVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Field;
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
        // 使用反射获取fullFileName字段
        String fullFileName = getFieldValue(versionDTO, "fullFileName");
        if (fullFileName == null || fullFileName.isEmpty()) {
            fullFileName = "document_version_" + versionId + ".bin";
        }
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fullFileName);
        // 简化处理，实际应用中应根据文件扩展名设置正确的Content-Type
        headers.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream");
        headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileData.length));

        return new ResponseEntity<>(fileData, headers, HttpStatus.OK);
    }

    /**
     * 删除文档版本
     */
    @DeleteMapping("/{versionId}")
    public ApiResponse<Void> deleteDocumentVersion(@PathVariable Long versionId) {
        try {
            documentVersionService.deleteDocumentVersion(versionId);
            return ApiResponse.success("版本删除成功");
        } catch (Exception e) {
            return ApiResponse.error("版本删除失败: " + e.getMessage());
        }
    }

    /**
     * 设置当前版本
     */
    @PutMapping("/{versionId}/set-current")
    public ApiResponse<Void> setCurrentVersion(@PathVariable Long versionId) {

        DocumentVersionDTO versionDTO = documentVersionService.getVersionById(versionId);
        // 使用反射获取documentId字段
        Long documentId = getFieldValue(versionDTO, "documentId");
        if (documentId == null) {
            throw new IllegalArgumentException("Document ID not found in version");
        }
        documentVersionService.setCurrentVersion(documentId, versionId);

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

    // 此方法与下面的compareDocumentVersions功能相似，已移除

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
            @RequestBody Map<String, String> revertRequest) {
        try {
            // 调用Service中的rollbackToVersion方法，不使用额外的revertReason参数
            DocumentVersionDTO result = documentVersionService.rollbackToVersion(versionId);
            return ApiResponse.success("回滚版本成功", result);
        } catch (Exception e) {
            return ApiResponse.error("版本回滚失败: " + e.getMessage());
        }
    }

    @PostMapping("/restore")
    public ApiResponse<Void> restoreDocumentVersion(@RequestBody RestoreDocumentDTO restoreDTO) {
        try {
            documentVersionService.restoreDocumentVersion(getFieldValue(restoreDTO, "documentId"),
                    getFieldValue(restoreDTO, "versionId"));
            return ApiResponse.success("文档版本恢复成功");
        } catch (Exception e) {
            return ApiResponse.error("文档版本恢复失败: " + e.getMessage());
        }
    }

    // 反射辅助方法已在文件末尾定义，此处移除重复定义

    @PostMapping("/compare")
    public ApiResponse<String> compareDocumentVersions(@RequestBody CompareDocumentVersionsDTO compareDTO) {
        try {
            String comparisonResult = documentVersionService.compareDocumentVersions(
                    getFieldValue(compareDTO, "documentId"),
                    getFieldValue(compareDTO, "fromVersionId"),
                    getFieldValue(compareDTO, "toVersionId"));
            return ApiResponse.success("文档版本对比完成", comparisonResult);
        } catch (Exception e) {
            return ApiResponse.error("文档版本对比失败: " + e.getMessage());
        }
    }

    /**
     * 使用反射安全地获取对象字段值
     */
    @SuppressWarnings("unchecked")
    private <T> T getFieldValue(Object object, String fieldName) {
        if (object == null || fieldName == null || fieldName.isEmpty()) {
            return null;
        }

        Class<?> clazz = object.getClass();
        while (clazz != null) {
            try {
                Field field = findField(clazz, fieldName);
                if (field != null) {
                    field.setAccessible(true);
                    return (T) field.get(object);
                }
            } catch (Exception e) {
                // 忽略异常，继续查找父类
            }
            clazz = clazz.getSuperclass();
        }
        return null;
    }

    /**
     * 查找字段，包括私有字段
     */
    private Field findField(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }
}
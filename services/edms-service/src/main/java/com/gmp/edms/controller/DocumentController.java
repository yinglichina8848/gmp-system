package com.gmp.edms.controller;

import com.gmp.edms.dto.ApiResponse;
import com.gmp.edms.dto.DocumentDTO;
import com.gmp.edms.dto.DocumentQueryDTO;
import com.gmp.edms.dto.DocumentUpdateDTO;
import com.gmp.edms.dto.PageRequestDTO;
import com.gmp.edms.dto.PageResponseDTO;
import com.gmp.edms.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文档管理Controller
 */
@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    /**
     * 创建文档
     */
    @PostMapping
    public ApiResponse<DocumentDTO> createDocument(@RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam("description") String description,
            @RequestParam("author") String author,
            @RequestParam("keywords") String keywords,
            @RequestParam("tags") String tags,
            @RequestParam("changeReason") String changeReason,
            @RequestParam("versionType") String versionType) throws IOException {

        DocumentDTO documentDTO = documentService.createDocument(file, title, categoryId, description,
                author, keywords, tags, changeReason, versionType);

        return ApiResponse.success("文档创建成功", documentDTO);
    }

    @PostMapping("/upload")
    public ApiResponse<DocumentDTO> createDocumentWithFile(
            @RequestPart("document") DocumentCreateDTO documentCreateDTO,
            @RequestPart("file") MultipartFile file) {
        try {
            DocumentDTO documentDTO = documentService.createDocumentWithFile(documentCreateDTO, file);
            return ApiResponse.success("文档上传成功", documentDTO);
        } catch (Exception e) {
            return ApiResponse.error("文档上传失败", e.getMessage());
        }
    }

    @PostMapping("/{id}/upload")
    public ApiResponse<DocumentDTO> uploadDocumentFile(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file) {
        try {
            DocumentDTO documentDTO = documentService.uploadDocumentFile(id, file);
            return ApiResponse.success("文件上传成功", documentDTO);
        } catch (Exception e) {
            return ApiResponse.error("文件上传失败", e.getMessage());
        }
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<ByteArrayResource> downloadDocumentFile(@PathVariable Long id) {
        try {
            DocumentDTO documentDTO = documentService.getDocumentById(id);
            byte[] fileContent = documentService.downloadDocumentFile(id);

            ByteArrayResource resource = new ByteArrayResource(fileContent);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=" + documentDTO.getFilePath()
                                    .substring(documentDTO.getFilePath().lastIndexOf('/') + 1))
                    .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileContent.length))
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 更新文档信息
     */
    @PutMapping("/{id}")
    public ApiResponse<DocumentDTO> updateDocument(@PathVariable Long id,
            @RequestBody DocumentUpdateDTO documentUpdateDTO) {

        DocumentDTO documentDTO = documentService.updateDocument(id, documentUpdateDTO);

        return ApiResponse.success("文档更新成功", documentDTO);
    }

    /**
     * 删除文档
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteDocument(@PathVariable Long id) {

        documentService.deleteDocument(id);

        return ApiResponse.success("文档删除成功");
    }

    /**
     * 批量删除文档
     */
    @DeleteMapping("/batch")
    public ApiResponse<Void> batchDeleteDocuments(@RequestBody List<Long> ids) {

        documentService.batchDeleteDocuments(ids);

        return ApiResponse.success("文档批量删除成功");
    }

    /**
     * 获取文档详情
     */
    @GetMapping("/{id}")
    public ApiResponse<DocumentDTO> getDocumentById(@PathVariable Long id) {

        DocumentDTO documentDTO = documentService.getDocumentById(id);

        return ApiResponse.success("获取文档成功", documentDTO);
    }

    /**
     * 根据文档编号获取文档
     */
    @GetMapping("/code/{code}")
    public ApiResponse<DocumentDTO> getDocumentByCode(@PathVariable String code) {

        DocumentDTO documentDTO = documentService.getDocumentByCode(code);

        return ApiResponse.success("获取文档成功", documentDTO);
    }

    /**
     * 分页查询文档
     */
    @GetMapping("/page")
    public ApiResponse<PageResponseDTO<DocumentDTO>> queryDocuments(PageRequestDTO pageRequest,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String status) {

        PageResponseDTO<DocumentDTO> pageResponse = documentService.queryDocuments(pageRequest, keyword, categoryId,
                status);

        return ApiResponse.success("查询文档成功", pageResponse);
    }

    /**
     * 获取分类下的文档列表
     */
    @GetMapping("/category/{categoryId}")
    public ApiResponse<List<DocumentDTO>> getDocumentsByCategory(@PathVariable Long categoryId) {

        List<DocumentDTO> documents = documentService.getDocumentsByCategory(categoryId);

        return ApiResponse.success("获取分类文档成功", documents);
    }

    /**
     * 更新文档状态
     */
    @PutMapping("/{id}/status")
    public ApiResponse<DocumentDTO> updateDocumentStatus(@PathVariable Long id,
            @RequestParam String status) {

        DocumentDTO documentDTO = documentService.updateDocumentStatus(id, status);

        return ApiResponse.success("文档状态更新成功", documentDTO);
    }

    /**
     * 搜索文档
     */
    @GetMapping("/search")
    public ApiResponse<PageResponseDTO<DocumentDTO>> searchDocuments(PageRequestDTO pageRequest,
            @RequestParam String keyword) {

        PageResponseDTO<DocumentDTO> pageResponse = documentService.searchDocuments(pageRequest, keyword);

        return ApiResponse.success("搜索文档成功", pageResponse);
    }

    /**
     * 高级搜索文档
     */
    @GetMapping("/advanced-search")
    public ApiResponse<PageResponseDTO<DocumentDTO>> advancedSearch(PageRequestDTO pageRequest,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) String approvalStatus) {

        // 直接调用接口定义的方法，传递所需参数
        PageResponseDTO<DocumentDTO> pageResponse = documentService.advancedSearch(pageRequest, keyword,
                categoryId, status, departmentId,
                approvalStatus);

        return ApiResponse.success("高级搜索文档成功", pageResponse);
    }

    /**
     * 获取用户权限的文档列表
     */
    @GetMapping("/user/{userId}")
    public ApiResponse<List<DocumentDTO>> getUserDocuments(@PathVariable Long userId) {

        // 简化为接口定义的方法，使用username作为参数
        List<DocumentDTO> documents = documentService.getUserAccessibleDocuments(userId.toString());

        return ApiResponse.success("获取用户文档成功", documents);
    }

    /**
     * 审批文档
     */
    @PutMapping("/{id}/approve")
    public ApiResponse<DocumentDTO> approveDocument(@PathVariable Long id,
            @RequestBody Map<String, String> approveRequest) {

        String approveStatus = approveRequest.get("approveStatus");
        String comment = approveRequest.get("comment");

        DocumentDTO documentDTO = documentService.approveDocument(id, approveStatus, comment);

        return ApiResponse.success("文档审批成功", documentDTO);
    }

    /**
     * 获取文档统计信息
     */
    // 注意：文档统计方法已从接口中移除，暂时注释掉
    /*
     * @GetMapping("/stats")
     * public ApiResponse<Map<String, Object>> getDocumentStats() {
     * 
     * // 这里可以实现简单的统计逻辑
     * Map<String, Object> stats = new HashMap<>();
     * // 后续可以根据需要补充统计功能
     * 
     * return ApiResponse.success("获取统计信息成功", stats);
     * }
     */
}

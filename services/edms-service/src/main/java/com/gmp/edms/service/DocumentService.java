package com.gmp.edms.service;

import com.gmp.edms.dto.DocumentCreateDTO;
import com.gmp.edms.dto.DocumentDTO;
import com.gmp.edms.dto.DocumentUpdateDTO;
import com.gmp.edms.dto.PageRequestDTO;
import com.gmp.edms.dto.PageResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 文档服务接口
 */
public interface DocumentService {

    /**
     * 创建文档
     */
    DocumentDTO createDocument(DocumentCreateDTO documentCreateDTO);

    /**
     * 创建带文件的文档
     */
    DocumentDTO createDocumentWithFile(DocumentCreateDTO documentCreateDTO, MultipartFile file) throws Exception;

    /**
     * 为文档上传文件
     */
    DocumentDTO uploadDocumentFile(Long documentId, MultipartFile file) throws Exception;

    /**
     * 下载文档文件
     */
    byte[] downloadDocumentFile(Long documentId) throws Exception;

    /**
     * 更新文档
     */
    DocumentDTO updateDocument(Long id, DocumentUpdateDTO documentUpdateDTO);

    /**
     * 删除文档
     */
    void deleteDocument(Long id);

    /**
     * 批量删除文档
     */
    void batchDeleteDocuments(List<Long> ids);

    /**
     * 根据ID获取文档
     */
    DocumentDTO getDocumentById(Long id);

    /**
     * 根据文档编号获取文档
     */
    DocumentDTO getDocumentByDocCode(String docCode);

    /**
     * 分页查询文档
     */
    PageResponseDTO<DocumentDTO> queryDocuments(PageRequestDTO pageRequest, Map<String, Object> filters);

    /**
     * 根据分类ID查询文档
     */
    List<DocumentDTO> getDocumentsByCategory(Long categoryId);

    /**
     * 更新文档状态
     */
    void updateDocumentStatus(Long id, String status);

    /**
     * 搜索文档
     */
    List<DocumentDTO> searchDocuments(String keyword, Integer pageNo, Integer pageSize);

    /**
     * 搜索文档（兼容单参数调用）
     */
    default List<DocumentDTO> searchDocuments(String keyword) {
        return searchDocuments(keyword, 1, 10);
    }

    /**
     * 高级搜索文档
     */
    PageResponseDTO<DocumentDTO> advancedSearch(PageRequestDTO pageRequest, String keyword,
            Long categoryId, String status,
            Long departmentId, String approvalStatus);

    /**
     * 获取用户权限的文档列表
     */
    List<DocumentDTO> getUserAccessibleDocuments(String username);

    /**
     * 文档审批
     */
    DocumentDTO approveDocument(Long id, String approvalStatus, String comments);
}

package com.gmp.edms.service;

import com.gmp.edms.dto.DocumentDTO;
import com.gmp.edms.dto.DocumentQueryDTO;

import java.util.List;

public interface DocumentSearchService {

    /**
     * 全文搜索文档
     * @param query 搜索关键词
     * @return 文档列表
     */
    List<DocumentDTO> fullTextSearch(String query);

    /**
     * 高级搜索文档
     * @param queryDTO 搜索条件
     * @return 文档列表
     */
    List<DocumentDTO> advancedSearch(DocumentQueryDTO queryDTO);

    /**
     * 索引文档
     * @param documentDTO 文档DTO
     */
    void indexDocument(DocumentDTO documentDTO);

    /**
     * 更新文档索引
     * @param documentDTO 文档DTO
     */
    void updateDocumentIndex(DocumentDTO documentDTO);

    /**
     * 删除文档索引
     * @param documentId 文档ID
     */
    void deleteDocumentIndex(Long documentId);

    /**
     * 批量索引文档
     * @param documentDTOs 文档DTO列表
     */
    void bulkIndexDocuments(List<DocumentDTO> documentDTOs);
}

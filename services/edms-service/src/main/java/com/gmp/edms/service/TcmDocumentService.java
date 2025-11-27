package com.gmp.edms.service;

import com.gmp.edms.dto.TcmDocumentDTO;
import com.gmp.edms.entity.TcmDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 中药特色文档服务接口
 */
public interface TcmDocumentService {

    /**
     * 创建新的中药特色文档
     *
     * @param tcmDocumentDTO 中药特色文档信息DTO
     * @return 创建的中药特色文档实体
     */
    TcmDocument createTcmDocument(TcmDocumentDTO tcmDocumentDTO);

    /**
     * 更新中药特色文档
     *
     * @param id 中药特色文档ID
     * @param tcmDocumentDTO 更新的中药特色文档信息
     * @return 更新后的中药特色文档实体
     */
    TcmDocument updateTcmDocument(Long id, TcmDocumentDTO tcmDocumentDTO);

    /**
     * 根据ID查询中药特色文档
     *
     * @param id 中药特色文档ID
     * @return 中药特色文档实体
     */
    TcmDocument getTcmDocumentById(Long id);

    /**
     * 根据文档编号查询中药特色文档
     *
     * @param documentNumber 文档编号
     * @return 中药特色文档实体
     */
    TcmDocument getTcmDocumentByNumber(String documentNumber);

    /**
     * 根据中药材名称查询中药特色文档列表
     *
     * @param herbName 中药材名称
     * @return 中药特色文档列表
     */
    List<TcmDocument> getTcmDocumentsByHerbName(String herbName);

    /**
     * 分页查询中药特色文档列表
     *
     * @param pageable 分页参数
     * @return 中药特色文档分页列表
     */
    Page<TcmDocument> getTcmDocuments(Pageable pageable);

    /**
     * 删除中药特色文档
     *
     * @param id 中药特色文档ID
     */
    void deleteTcmDocument(Long id);
}
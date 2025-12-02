package com.gmp.edms.service;

import com.gmp.edms.dto.ElectronicSignatureDTO;

import java.util.List;

/**
 * 电子签名服务
 */
public interface ElectronicSignatureService {

    /**
     * 创建电子签名
     * 
     * @param signatureDTO 电子签名DTO
     * @return 电子签名DTO
     */
    ElectronicSignatureDTO createSignature(ElectronicSignatureDTO signatureDTO);

    /**
     * 根据ID获取电子签名
     * 
     * @param id 电子签名ID
     * @return 电子签名DTO
     */
    ElectronicSignatureDTO getSignatureById(Long id);

    /**
     * 根据文档ID获取电子签名列表
     * 
     * @param documentId 文档ID
     * @return 电子签名DTO列表
     */
    List<ElectronicSignatureDTO> getSignaturesByDocumentId(Long documentId);

    /**
     * 根据文档版本ID获取电子签名列表
     * 
     * @param documentVersionId 文档版本ID
     * @return 电子签名DTO列表
     */
    List<ElectronicSignatureDTO> getSignaturesByDocumentVersionId(Long documentVersionId);

    /**
     * 验证电子签名
     * 
     * @param signatureId 电子签名ID
     * @return 验证结果
     */
    boolean verifySignature(Long signatureId);

    /**
     * 批量验证电子签名
     * 
     * @param signatureIds 电子签名ID列表
     * @return 验证结果列表
     */
    List<Boolean> batchVerifySignatures(List<Long> signatureIds);

    /**
     * 删除电子签名
     * 
     * @param id 电子签名ID
     */
    void deleteSignature(Long id);

    /**
     * 批量删除电子签名
     * 
     * @param ids 电子签名ID列表
     */
    void batchDeleteSignatures(List<Long> ids);
}

package com.gmp.edms.repository;

import com.gmp.edms.entity.ElectronicSignature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 电子签名仓库
 */
@Repository
public interface ElectronicSignatureRepository extends JpaRepository<ElectronicSignature, Long> {

    /**
     * 根据文档ID查询电子签名
     * 
     * @param documentId 文档ID
     * @return 电子签名列表
     */
    List<ElectronicSignature> findByDocumentId(Long documentId);

    /**
     * 根据文档版本ID查询电子签名
     * 
     * @param documentVersionId 文档版本ID
     * @return 电子签名列表
     */
    List<ElectronicSignature> findByDocumentVersionId(Long documentVersionId);

    /**
     * 根据签名者ID查询电子签名
     * 
     * @param signerId 签名者ID
     * @return 电子签名列表
     */
    List<ElectronicSignature> findBySignerId(String signerId);

    /**
     * 根据文档ID和签名类型查询电子签名
     * 
     * @param documentId    文档ID
     * @param signatureType 签名类型
     * @return 电子签名列表
     */
    List<ElectronicSignature> findByDocumentIdAndSignatureType(Long documentId, String signatureType);

    /**
     * 根据文档版本ID和签名类型查询电子签名
     * 
     * @param documentVersionId 文档版本ID
     * @param signatureType     签名类型
     * @return 电子签名列表
     */
    List<ElectronicSignature> findByDocumentVersionIdAndSignatureType(Long documentVersionId, String signatureType);
}

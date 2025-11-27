package com.gmp.edms.repository;

import com.gmp.edms.entity.DocumentVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 文档版本Repository接口
 */
@Repository
public interface DocumentVersionRepository extends JpaRepository<DocumentVersion, Long>, JpaSpecificationExecutor<DocumentVersion> {
    
    /**
     * 根据文档ID查询所有版本
     */
    List<DocumentVersion> findByDocumentIdOrderByVersionNumberDesc(Long documentId);
    
    /**
     * 查询文档的当前版本
     */
    Optional<DocumentVersion> findByDocumentIdAndIsCurrentTrue(Long documentId);
    
    /**
     * 根据文档ID和版本号查询版本
     */
    Optional<DocumentVersion> findByDocumentIdAndVersionNumber(Long documentId, String versionNumber);
    
    /**
     * 根据文件路径查询版本
     */
    Optional<DocumentVersion> findByFilePath(String filePath);
    
    /**
     * 根据校验和查询版本
     */
    Optional<DocumentVersion> findByChecksum(String checksum);
    
    /**
     * 统计文档的版本数量
     */
    long countByDocumentId(Long documentId);
    
    /**
     * 根据作者查询版本
     */
    List<DocumentVersion> findByAuthor(String author);
    
    /**
     * 查询指定文档的最新版本号
     */
    @Query("SELECT MAX(dv.versionNumber) FROM DocumentVersion dv WHERE dv.documentId = :documentId")
    Optional<String> findLatestVersionNumberByDocumentId(@Param("documentId") Long documentId);
    
    /**
     * 查询指定日期范围内的版本
     */
    @Query("SELECT dv FROM DocumentVersion dv WHERE dv.createdAt BETWEEN :startDate AND :endDate")
    List<DocumentVersion> findByCreatedAtBetween(@Param("startDate") java.time.LocalDateTime startDate, @Param("endDate") java.time.LocalDateTime endDate);
    
    /**
     * 根据版本类型查询版本
     */
    List<DocumentVersion> findByVersionType(String versionType);
    
    /**
     * 根据文档ID和排除的版本ID查询最新版本
     */
    Optional<DocumentVersion> findFirstByDocumentIdAndIdNotOrderByCreatedAtDesc(Long documentId, Long excludedId);
    
    List<DocumentVersion> findByDocumentIdOrderByCreatedAtDesc(Long documentId);
}

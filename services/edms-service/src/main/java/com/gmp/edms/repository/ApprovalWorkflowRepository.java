package com.gmp.edms.repository;

import com.gmp.edms.entity.ApprovalWorkflow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 审批工作流Repository接口
 */
@Repository
public interface ApprovalWorkflowRepository extends JpaRepository<ApprovalWorkflow, Long> {

    /**
     * 根据工作流编码查找工作流
     */
    Optional<ApprovalWorkflow> findByWorkflowCode(String workflowCode);

    /**
     * 根据文档类型查找工作流
     */
    List<ApprovalWorkflow> findByDocumentType(String documentType);

    /**
     * 根据文档类型和活跃状态查找工作流
     */
    List<ApprovalWorkflow> findByDocumentTypeAndIsActive(String documentType, Boolean isActive);

    /**
     * 查找所有活跃的工作流
     */
    List<ApprovalWorkflow> findByIsActiveTrue();

    /**
     * 根据创建者查找工作流
     */
    List<ApprovalWorkflow> findByCreatedBy(String createdBy);

    /**
     * 根据工作流名称模糊查询
     */
    List<ApprovalWorkflow> findByWorkflowNameContaining(String workflowName);

    /**
     * 统计指定文档类型的工作流数量
     */
    long countByDocumentTypeAndIsActive(String documentType, Boolean isActive);

    /**
     * 查找指定版本的工作流
     */
    List<ApprovalWorkflow> findByVersion(String version);

    /**
     * 根据多个条件查询工作流
     */
    @Query("SELECT w FROM ApprovalWorkflow w WHERE " +
           "(:workflowCode IS NULL OR w.workflowCode LIKE %:workflowCode%) AND " +
           "(:workflowName IS NULL OR w.workflowName LIKE %:workflowName%) AND " +
           "(:documentType IS NULL OR w.documentType = :documentType) AND " +
           "(:isActive IS NULL OR w.isActive = :isActive) AND " +
           "(:createdBy IS NULL OR w.createdBy = :createdBy)")
    List<ApprovalWorkflow> findByMultipleConditions(
            @Param("workflowCode") String workflowCode,
            @Param("workflowName") String workflowName,
            @Param("documentType") String documentType,
            @Param("isActive") Boolean isActive,
            @Param("createdBy") String createdBy
    );

    /**
     * 检查工作流编码是否存在
     */
    boolean existsByWorkflowCode(String workflowCode);
}
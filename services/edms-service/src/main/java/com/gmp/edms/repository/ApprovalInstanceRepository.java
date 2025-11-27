package com.gmp.edms.repository;

import com.gmp.edms.entity.ApprovalInstance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 审批实例Repository接口
 */
@Repository
public interface ApprovalInstanceRepository extends JpaRepository<ApprovalInstance, Long> {

    /**
     * 根据工作流ID查找审批实例
     */
    List<ApprovalInstance> findByWorkflowId(Long workflowId);

    /**
     * 根据文档ID查找审批实例
     */
    List<ApprovalInstance> findByDocumentId(Long documentId);

    /**
     * 根据发起人查找审批实例
     */
    List<ApprovalInstance> findByInitiator(String initiator);

    /**
     * 根据状态查找审批实例
     */
    List<ApprovalInstance> findByStatus(String status);

    /**
     * 根据当前步骤查找审批实例
     */
    List<ApprovalInstance> findByCurrentStep(String currentStep);

    /**
     * 根据优先级查找审批实例
     */
    List<ApprovalInstance> findByPriority(String priority);

    /**
     * 查找用户的待办审批任务
     */
    @Query("SELECT ai FROM ApprovalInstance ai WHERE ai.currentStep IN " +
           "(SELECT stepName FROM ApprovalStep WHERE approverId = :userId) " +
           "AND ai.status = 'IN_PROGRESS'")
    List<ApprovalInstance> findPendingTasksByUser(@Param("userId") String userId);

    /**
     * 根据多个条件查询审批实例
     */
    @Query("SELECT ai FROM ApprovalInstance ai WHERE " +
           "(:workflowId IS NULL OR ai.workflowId = :workflowId) AND " +
           "(:documentId IS NULL OR ai.documentId = :documentId) AND " +
           "(:initiator IS NULL OR ai.initiator = :initiator) AND " +
           "(:status IS NULL OR ai.status = :status) AND " +
           "(:currentStep IS NULL OR ai.currentStep = :currentStep) AND " +
           "(:priority IS NULL OR ai.priority = :priority)")
    Page<ApprovalInstance> findByMultipleConditions(
            @Param("workflowId") Long workflowId,
            @Param("documentId") Long documentId,
            @Param("initiator") String initiator,
            @Param("status") String status,
            @Param("currentStep") String currentStep,
            @Param("priority") String priority,
            Pageable pageable
    );

    /**
     * 查找即将到期的审批实例
     */
    @Query("SELECT ai FROM ApprovalInstance ai WHERE ai.status = 'IN_PROGRESS' " +
           "AND ai.deadline BETWEEN :startTime AND :endTime")
    List<ApprovalInstance> findExpiringSoon(@Param("startTime") LocalDateTime startTime,
                                          @Param("endTime") LocalDateTime endTime);

    /**
     * 查找已超时的审批实例
     */
    @Query("SELECT ai FROM ApprovalInstance ai WHERE ai.status = 'IN_PROGRESS' " +
           "AND ai.deadline < :currentTime")
    List<ApprovalInstance> findOverdue(@Param("currentTime") LocalDateTime currentTime);

    /**
     * 统计指定状态的审批实例数量
     */
    long countByStatus(String status);

    /**
     * 统计发起人的审批实例数量
     */
    long countByInitiator(String initiator);

    /**
     * 查找指定时间范围内的审批实例
     */
    @Query("SELECT ai FROM ApprovalInstance ai WHERE ai.startedAt BETWEEN :startTime AND :endTime")
    List<ApprovalInstance> findByStartedAtBetween(@Param("startTime") LocalDateTime startTime,
                                                  @Param("endTime") LocalDateTime endTime);

    /**
     * 查找指定时间范围内完成的审批实例
     */
    @Query("SELECT ai FROM ApprovalInstance ai WHERE ai.completedAt BETWEEN :startTime AND :endTime " +
           "AND ai.status IN ('APPROVED', 'REJECTED')")
    List<ApprovalInstance> findByCompletedAtBetween(@Param("startTime") LocalDateTime startTime,
                                                   @Param("endTime") LocalDateTime endTime);

    /**
     * 获取最近的审批实例
     */
    List<ApprovalInstance> findTop10ByOrderByStartedAtDesc();

    /**
     * 获取指定文档的最新审批实例
     */
    Optional<ApprovalInstance> findTopByDocumentIdOrderByStartedAtDesc(Long documentId);
}
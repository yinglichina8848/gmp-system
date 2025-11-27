package com.gmp.edms.service;

import com.gmp.edms.dto.ApprovalInstanceDTO;
import com.gmp.edms.dto.ApprovalWorkflowDTO;
import com.gmp.edms.dto.ApprovalStepDTO;
import com.gmp.edms.entity.ApprovalInstance;
import com.gmp.edms.entity.ApprovalWorkflow;
import com.gmp.edms.entity.Document;

import java.util.List;

/**
 * 审批工作流服务接口
 * 实现设计文档中的工作流引擎功能
 */
public interface ApprovalWorkflowService {

    /**
     * 启动审批流程
     * @param document 文档
     * @param workflowCode 工作流编码
     * @param initiator 发起人
     * @return 审批实例
     */
    ApprovalInstanceDTO startApprovalProcess(Document document, String workflowCode, String initiator);

    /**
     * 执行审批决策
     * @param instanceId 审批实例ID
     * @param userId 用户ID
     * @param decision 审批决策
     * @param comments 审批意见
     * @return 执行结果
     */
    boolean executeApprovalStep(Long instanceId, String userId, String decision, String comments);

    /**
     * 获取审批实例详情
     * @param instanceId 实例ID
     * @return 审批实例
     */
    ApprovalInstanceDTO getApprovalInstance(Long instanceId);

    /**
     * 获取用户的待办审批任务
     * @param userId 用户ID
     * @return 待办任务列表
     */
    List<ApprovalInstanceDTO> getPendingTasks(String userId);

    /**
     * 获取文档的审批历史
     * @param documentId 文档ID
     * @return 审批历史
     */
    List<ApprovalInstanceDTO> getDocumentApprovalHistory(Long documentId);

    /**
     * 创建审批工作流模板
     * @param workflowDTO 工作流DTO
     * @return 创建的工作流
     */
    ApprovalWorkflowDTO createWorkflow(ApprovalWorkflowDTO workflowDTO);

    /**
     * 获取所有可用的工作流模板
     * @return 工作流列表
     */
    List<ApprovalWorkflowDTO> getAllWorkflows();

    /**
     * 根据文档类型获取适用的工作流
     * @param documentType 文档类型
     * @return 工作流列表
     */
    List<ApprovalWorkflowDTO> getWorkflowsByDocumentType(String documentType);

    /**
     * 撤回审批流程
     * @param instanceId 实例ID
     * @param userId 用户ID
     * @param reason 撤回原因
     * @return 撤回结果
     */
    boolean withdrawApproval(Long instanceId, String userId, String reason);

    /**
     * 转办审批任务
     * @param instanceId 实例ID
     * @param fromUserId 原审批人
     * @param toUserId 新审批人
     * @param reason 转办原因
     * @return 转办结果
     */
    boolean transferApproval(Long instanceId, String fromUserId, String toUserId, String reason);

    /**
     * 获取审批进度
     * @param instanceId 实例ID
     * @return 当前步骤和进度信息
     */
    ApprovalStepDTO getCurrentStep(Long instanceId);

    /**
     * 催办审批任务
     * @param instanceId 实例ID
     * @param userId 催办人
     * @return 催办结果
     */
    boolean urgeApproval(Long instanceId, String userId);
}
package com.gmp.edms.controller;

import com.gmp.edms.dto.ApiResponse;
import com.gmp.edms.dto.ApprovalInstanceDTO;
import com.gmp.edms.dto.ApprovalStepDTO;
import com.gmp.edms.dto.ApprovalWorkflowDTO;
import com.gmp.edms.service.ApprovalWorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 审批工作流Controller
 * 实现设计文档中的审批流程API
 */
@RestController
@RequestMapping("/api/edms/approvals")
public class ApprovalWorkflowController {

    @Autowired
    private ApprovalWorkflowService approvalWorkflowService;

    /**
     * 启动审批流程
     */
    @PostMapping("/start")
    public ApiResponse<ApprovalInstanceDTO> startApprovalProcess(@RequestBody Map<String, Object> request) {
        Long documentId = Long.valueOf(request.get("documentId").toString());
        String workflowCode = request.get("workflowCode").toString();
        String initiator = request.get("initiator").toString();

        // 这里需要先获取文档对象，简化实现
        ApprovalInstanceDTO instance = approvalWorkflowService.startApprovalProcess(null, workflowCode, initiator);
        return ApiResponse.success("审批流程启动成功", instance);
    }

    /**
     * 执行审批决策
     */
    @PostMapping("/{instanceId}/step/{stepId}/action")
    public ApiResponse<Boolean> executeApprovalStep(@PathVariable Long instanceId,
                                                     @PathVariable String stepId,
                                                     @RequestBody Map<String, String> request) {
        String userId = request.get("userId");
        String action = request.get("action"); // APPROVE, REJECT
        String comments = request.get("comments");

        boolean result = approvalWorkflowService.executeApprovalStep(instanceId, userId, action, comments);
        return ApiResponse.success("审批执行成功", result);
    }

    /**
     * 获取审批实例详情
     */
    @GetMapping("/{instanceId}")
    public ApiResponse<ApprovalInstanceDTO> getApprovalInstance(@PathVariable Long instanceId) {
        ApprovalInstanceDTO instance = approvalWorkflowService.getApprovalInstance(instanceId);
        return ApiResponse.success("获取审批实例成功", instance);
    }

    /**
     * 获取用户的待办审批任务
     */
    @GetMapping("/pending/{userId}")
    public ApiResponse<List<ApprovalInstanceDTO>> getPendingTasks(@PathVariable String userId) {
        List<ApprovalInstanceDTO> tasks = approvalWorkflowService.getPendingTasks(userId);
        return ApiResponse.success("获取待办任务成功", tasks);
    }

    /**
     * 获取文档的审批历史
     */
    @GetMapping("/history/{documentId}")
    public ApiResponse<List<ApprovalInstanceDTO>> getDocumentApprovalHistory(@PathVariable Long documentId) {
        List<ApprovalInstanceDTO> history = approvalWorkflowService.getDocumentApprovalHistory(documentId);
        return ApiResponse.success("获取审批历史成功", history);
    }

    /**
     * 创建审批工作流模板
     */
    @PostMapping("/workflows")
    public ApiResponse<ApprovalWorkflowDTO> createWorkflow(@RequestBody ApprovalWorkflowDTO workflowDTO) {
        ApprovalWorkflowDTO workflow = approvalWorkflowService.createWorkflow(workflowDTO);
        return ApiResponse.success("创建工作流成功", workflow);
    }

    /**
     * 获取所有可用的工作流模板
     */
    @GetMapping("/workflows")
    public ApiResponse<List<ApprovalWorkflowDTO>> getAllWorkflows() {
        List<ApprovalWorkflowDTO> workflows = approvalWorkflowService.getAllWorkflows();
        return ApiResponse.success("获取工作流列表成功", workflows);
    }

    /**
     * 根据文档类型获取适用的工作流
     */
    @GetMapping("/workflows/by-document-type/{documentType}")
    public ApiResponse<List<ApprovalWorkflowDTO>> getWorkflowsByDocumentType(@PathVariable String documentType) {
        List<ApprovalWorkflowDTO> workflows = approvalWorkflowService.getWorkflowsByDocumentType(documentType);
        return ApiResponse.success("获取适用工作流成功", workflows);
    }

    /**
     * 撤回审批流程
     */
    @PutMapping("/{instanceId}/withdraw")
    public ApiResponse<Boolean> withdrawApproval(@PathVariable Long instanceId,
                                                 @RequestBody Map<String, String> request) {
        String userId = request.get("userId");
        String reason = request.get("reason");

        boolean result = approvalWorkflowService.withdrawApproval(instanceId, userId, reason);
        return ApiResponse.success("撤回审批成功", result);
    }

    /**
     * 转办审批任务
     */
    @PutMapping("/{instanceId}/transfer")
    public ApiResponse<Boolean> transferApproval(@PathVariable Long instanceId,
                                                @RequestBody Map<String, String> request) {
        String fromUserId = request.get("fromUserId");
        String toUserId = request.get("toUserId");
        String reason = request.get("reason");

        boolean result = approvalWorkflowService.transferApproval(instanceId, fromUserId, toUserId, reason);
        return ApiResponse.success("转办审批成功", result);
    }

    /**
     * 获取审批进度
     */
    @GetMapping("/{instanceId}/progress")
    public ApiResponse<ApprovalStepDTO> getCurrentStep(@PathVariable Long instanceId) {
        ApprovalStepDTO currentStep = approvalWorkflowService.getCurrentStep(instanceId);
        return ApiResponse.success("获取审批进度成功", currentStep);
    }

    /**
     * 催办审批任务
     */
    @PostMapping("/{instanceId}/urge")
    public ApiResponse<Boolean> urgeApproval(@PathVariable Long instanceId,
                                             @RequestBody Map<String, String> request) {
        String userId = request.get("userId");

        boolean result = approvalWorkflowService.urgeApproval(instanceId, userId);
        return ApiResponse.success("催办成功", result);
    }
}
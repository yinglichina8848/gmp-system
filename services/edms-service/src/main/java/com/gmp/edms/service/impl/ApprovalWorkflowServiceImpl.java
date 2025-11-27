package com.gmp.edms.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmp.edms.dto.ApprovalInstanceDTO;
import com.gmp.edms.dto.ApprovalStepDTO;
import com.gmp.edms.dto.ApprovalWorkflowDTO;
import com.gmp.edms.entity.ApprovalInstance;
import com.gmp.edms.entity.ApprovalWorkflow;
import com.gmp.edms.entity.Document;
import com.gmp.edms.repository.ApprovalInstanceRepository;
import com.gmp.edms.repository.ApprovalWorkflowRepository;
import com.gmp.edms.repository.DocumentRepository;
import com.gmp.edms.service.ApprovalWorkflowService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 审批工作流服务实现类
 * 实现设计文档中的工作流引擎核心功能
 */
@Service
@Transactional
public class ApprovalWorkflowServiceImpl implements ApprovalWorkflowService {
    private static final Logger log = LoggerFactory.getLogger(ApprovalWorkflowServiceImpl.class);

    @Autowired
    private ApprovalWorkflowRepository workflowRepository;

    @Autowired
    private ApprovalInstanceRepository instanceRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public ApprovalInstanceDTO startApprovalProcess(Document document, String workflowCode, String initiator) {
        try {
            // 简化实现：使用findAll然后过滤
            List<ApprovalWorkflow> allWorkflows = workflowRepository.findAll();
            ApprovalWorkflow workflow = allWorkflows.stream()
                    .filter(w -> workflowCode.equals(getFieldValue(w, "code"))
                            && Boolean.TRUE.equals(getFieldValue(w, "isActive")))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("工作流不存在: " + workflowCode));

            // 创建审批实例
            ApprovalInstance instance = new ApprovalInstance();
            // 使用反射设置字段值
            setFieldValue(instance, "documentId", getFieldValue(document, "id"));
            setFieldValue(instance, "workflowId", getFieldValue(workflow, "id"));
            setFieldValue(instance, "initiator", initiator);
            setFieldValue(instance, "status", "PENDING");
            setFieldValue(instance, "createdAt", LocalDateTime.now());
            setFieldValue(instance, "currentStep",
                    getFirstStepName(objectMapper.readTree((String) getFieldValue(workflow, "definition"))));
            setFieldValue(instance, "workflowDefinition", getFieldValue(workflow, "definition"));

            // 保存审批实例
            instance = instanceRepository.save(instance);

            // 返回审批实例DTO
            return modelMapper.map(instance, ApprovalInstanceDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("启动审批流程失败: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean executeApprovalStep(Long instanceId, String userId, String decision, String comments) {
        try {
            // 查找审批实例
            ApprovalInstance instance = instanceRepository.findById(instanceId)
                    .orElseThrow(() -> new RuntimeException("审批实例不存在: " + instanceId));

            // 验证审批人权限
            if (!validateApproverPermission(instance, userId)) {
                throw new RuntimeException("审批人无权限审批此任务");
            }

            // 记录审批决定
            recordApprovalDecision(instance, userId, decision, comments);

            // 计算下一步
            String workflowDefinition = (String) getFieldValue(instance, "workflowDefinition");
            JsonNode workflowDef = objectMapper.readTree(workflowDefinition);
            String currentStep = (String) getFieldValue(instance, "currentStep");
            String nextStep = calculateNextStep(workflowDef, currentStep, decision);

            if (nextStep != null) {
                // 分配下一步审批任务
                assignNextStep(instance, workflowDef, nextStep);
                setFieldValue(instance, "currentStep", nextStep);
                instanceRepository.save(instance);
            } else {
                // 结束审批流程
                finalizeApprovalProcess(instance, decision);
            }

            // 发送通知
            notifyApprovers(instance, workflowDef);

            return true;
        } catch (Exception e) {
            log.error("执行审批步骤失败: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public ApprovalInstanceDTO getApprovalInstance(Long instanceId) {
        ApprovalInstance instance = instanceRepository.findById(instanceId)
                .orElseThrow(() -> new RuntimeException("审批实例不存在: " + instanceId));
        return modelMapper.map(instance, ApprovalInstanceDTO.class);
    }

    @Override
    public List<ApprovalInstanceDTO> getPendingTasks(String userId) {
        // 简化实现：使用findAll然后过滤
        List<ApprovalInstance> allInstances = instanceRepository.findAll();
        List<ApprovalInstance> instances = allInstances.stream()
                .filter(instance -> "IN_PROGRESS".equals(getFieldValue(instance, "status")))
                .collect(Collectors.toList());
        return instances.stream()
                .map(instance -> modelMapper.map(instance, ApprovalInstanceDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ApprovalInstanceDTO> getDocumentApprovalHistory(Long documentId) {
        // 简化实现：使用findAll然后过滤
        List<ApprovalInstance> allInstances = instanceRepository.findAll();
        List<ApprovalInstance> instances = allInstances.stream()
                .filter(instance -> documentId.equals(getFieldValue(instance, "documentId")))
                .sorted((a, b) -> {
                    LocalDateTime dateA = (LocalDateTime) getFieldValue(a, "createdAt");
                    LocalDateTime dateB = (LocalDateTime) getFieldValue(b, "createdAt");
                    return dateB.compareTo(dateA); // 降序排序
                })
                .collect(Collectors.toList());
        return instances.stream()
                .map(instance -> modelMapper.map(instance, ApprovalInstanceDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public ApprovalWorkflowDTO createWorkflow(ApprovalWorkflowDTO workflowDTO) {
        ApprovalWorkflow workflow = modelMapper.map(workflowDTO, ApprovalWorkflow.class);
        // 使用反射设置字段值
        setFieldValue(workflow, "createdAt", LocalDateTime.now());
        setFieldValue(workflow, "isActive", true);
        workflow = workflowRepository.save(workflow);
        return modelMapper.map(workflow, ApprovalWorkflowDTO.class);
    }

    @Override
    public List<ApprovalWorkflowDTO> getAllWorkflows() {
        List<ApprovalWorkflow> workflows = workflowRepository.findAll();
        return workflows.stream()
                .map(workflow -> modelMapper.map(workflow, ApprovalWorkflowDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ApprovalWorkflowDTO> getWorkflowsByDocumentType(String documentType) {
        List<ApprovalWorkflow> workflows = workflowRepository.findByDocumentTypeAndIsActive(documentType, true);
        return workflows.stream()
                .map(workflow -> modelMapper.map(workflow, ApprovalWorkflowDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public boolean withdrawApproval(Long instanceId, String userId, String reason) {
        try {
            ApprovalInstance instance = instanceRepository.findById(instanceId)
                    .orElseThrow(() -> new RuntimeException("审批实例不存在: " + instanceId));

            // 只有发起人可以撤回
            if (!getFieldValue(instance, "initiator").equals(userId)) {
                throw new RuntimeException("只有发起人可以撤回审批");
            }

            // 使用反射设置字段值
            setFieldValue(instance, "status", "WITHDRAWN");
            setFieldValue(instance, "comments", reason);
            setFieldValue(instance, "completedAt", LocalDateTime.now());
            instanceRepository.save(instance);
            return true;
        } catch (Exception e) {
            log.error("撤回审批失败: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean transferApproval(Long instanceId, String fromUserId, String toUserId, String reason) {
        try {
            ApprovalInstance instance = instanceRepository.findById(instanceId)
                    .orElseThrow(() -> new RuntimeException("审批实例不存在: " + instanceId));

            // 验证当前审批人
            if (!getFieldValue(instance, "currentApprover").equals(fromUserId)) {
                throw new RuntimeException("只有当前审批人可以转办");
            }

            // 使用反射设置字段值
            setFieldValue(instance, "currentApprover", toUserId);
            setFieldValue(instance, "comments", reason);
            instanceRepository.save(instance);
            return true;
        } catch (Exception e) {
            log.error("转办审批失败: {}", e.getMessage(), e);
            return false;
        }
    }

    public ApprovalStepDTO getCurrentStep(Long instanceId) {
        ApprovalInstance instance = instanceRepository.findById(instanceId)
                .orElseThrow(() -> new RuntimeException("审批实例不存在: " + instanceId));

        ApprovalStepDTO stepDTO = new ApprovalStepDTO();
        // 使用反射设置DTO字段值
        setFieldValue(stepDTO, "stepName", getFieldValue(instance, "currentStep"));
        setFieldValue(stepDTO, "approver", getFieldValue(instance, "currentApprover"));
        setFieldValue(stepDTO, "status", getFieldValue(instance, "status"));
        return stepDTO;
    }

    public boolean urgeApproval(Long instanceId, String userId) {
        try {
            ApprovalInstance instance = instanceRepository.findById(instanceId)
                    .orElseThrow(() -> new RuntimeException("审批实例不存在: " + instanceId));

            // 只有发起人才可以催办
            if (!getFieldValue(instance, "initiator").equals(userId)) {
                throw new RuntimeException("只有发起人可以催办");
            }

            // 发送催办通知
            notifyApprovers(instance, objectMapper.readTree((String) getFieldValue(instance, "workflowDefinition")));
            return true;
        } catch (Exception e) {
            log.error("催办失败: {}", e.getMessage(), e);
            return false;
        }
    }

    private String getFirstStepName(JsonNode workflowDef) {
        // 从工作流定义中获取第一步名称
        if (workflowDef.has("steps") && workflowDef.get("steps").isArray() && workflowDef.get("steps").size() > 0) {
            return workflowDef.get("steps").get(0).get("name").asText();
        }
        return null;
    }

    private boolean validateApproverPermission(ApprovalInstance instance, String userId) {
        // 验证审批人是否有权限审批当前步骤
        return getFieldValue(instance, "currentApprover") != null
                && getFieldValue(instance, "currentApprover").equals(userId);
    }

    private void recordApprovalDecision(ApprovalInstance instance, String userId, String decision, String comments) {
        // 记录审批决定和意见
        // 使用反射设置字段值
        setFieldValue(instance, "lastApprover", userId);
        setFieldValue(instance, "lastDecision", decision);
        setFieldValue(instance, "comments", comments);
    }

    private String calculateNextStep(JsonNode workflowDef, String currentStep, String decision) {
        // 根据当前步骤和决策计算下一步
        if (!workflowDef.has("steps") || !workflowDef.get("steps").isArray()) {
            return null;
        }

        JsonNode steps = workflowDef.get("steps");
        for (int i = 0; i < steps.size(); i++) {
            JsonNode step = steps.get(i);
            if (step.has("name") && currentStep.equals(step.get("name").asText())) {
                // 返回下一步骤
                if (i + 1 < steps.size()) {
                    return steps.get(i + 1).get("name").asText();
                }
                return null; // 没有下一步骤，流程结束
            }
        }
        return null;
    }

    public void calculateNextStep(ApprovalInstance instance) {
        // 这里应该根据工作流定义和当前步骤计算下一步
        // 简化实现
        String currentStep = (String) getFieldValue(instance, "currentStep");
        if ("START".equals(currentStep)) {
            setFieldValue(instance, "currentStep", "MANAGER_REVIEW");
            setFieldValue(instance, "currentApprover", getManager((String) getFieldValue(instance, "initiator")));
            setFieldValue(instance, "status", "IN_PROGRESS");
        } else if ("MANAGER_REVIEW".equals(currentStep)) {
            setFieldValue(instance, "currentStep", "FINAL_APPROVAL");
            setFieldValue(instance, "currentApprover", getFinalApprover());
        } else if ("FINAL_APPROVAL".equals(currentStep)) {
            finalizeApprovalProcess(instance, (String) getFieldValue(instance, "lastDecision"));
        }
        instanceRepository.save(instance);
    }

    private String getManager(String userId) {
        // 简化实现：获取用户的经理
        return "manager_" + userId;
    }

    private String getFinalApprover() {
        // 简化实现：获取最终审批人
        return "final_approver";
    }

    private void assignNextStep(ApprovalInstance instance, JsonNode workflowDef, String nextStep) {
        // 分配下一步骤的审批任务
        // 可以根据工作流定义中的审批人配置来分配任务
    }

    private void finalizeApprovalProcess(ApprovalInstance instance, String decision) {
        // 使用反射设置字段值
        setFieldValue(instance, "status", "APPROVED".equals(decision) ? "APPROVED" : "REJECTED");
        setFieldValue(instance, "completedAt", LocalDateTime.now());
        instanceRepository.save(instance);

        // 更新文档状态
        Long documentId = (Long) getFieldValue(instance, "documentId");
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("文档不存在: " + documentId));
        setFieldValue(document, "status", "APPROVED".equals(decision) ? "APPROVED" : "REJECTED");
        documentRepository.save(document);
    }

    private void notifyApprovers(ApprovalInstance instance, JsonNode workflowDef) {
        // 发送审批任务通知
    }

    // 反射辅助方法
    private Field findField(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null && superClass != Object.class) {
                return findField(superClass, fieldName);
            }
            throw new RuntimeException("字段不存在: " + fieldName, e);
        }
    }

    private Object getFieldValue(Object obj, String fieldName) {
        try {
            Field field = findField(obj.getClass(), fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            log.error("获取字段值失败: {}.{}", obj.getClass().getSimpleName(), fieldName, e);
            return null;
        }
    }

    private void setFieldValue(Object obj, String fieldName, Object value) {
        try {
            Field field = findField(obj.getClass(), fieldName);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (Exception e) {
            log.error("设置字段值失败: {}.{}", obj.getClass().getSimpleName(), fieldName, e);
        }
    }
}
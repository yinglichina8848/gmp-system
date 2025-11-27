package com.gmp.qms.service;

import com.gmp.qms.dto.*;

import java.util.List;
import java.util.Map;

/**
 * MCP系统集成服务接口，定义QMS系统与其他系统之间的集成方法
 * 
 * @author GMP系统开发团队
 */
public interface McpIntegrationService {
    
    // ========== 与EDMS系统集成 ==========
    
    /**
     * 从EDMS系统获取文档信息
     * @param documentId 文档ID
     * @return 文档信息
     */
    EdmsDocumentDTO getDocumentFromEdms(String documentId);
    
    /**
     * 在EDMS系统中创建文档
     * @param documentDTO 文档信息
     * @return 创建的文档ID
     */
    String createDocumentInEdms(EdmsDocumentDTO documentDTO);
    
    /**
     * 更新EDMS系统中的文档
     * @param documentId 文档ID
     * @param documentDTO 更新的文档信息
     */
    void updateDocumentInEdms(String documentId, EdmsDocumentDTO documentDTO);
    
    /**
     * 从EDMS系统搜索文档
     * @param criteria 搜索条件
     * @return 文档列表
     */
    List<EdmsDocumentDTO> searchDocumentsInEdms(Map<String, Object> criteria);
    
    // ========== 与培训管理系统集成 ==========
    
    /**
     * 获取员工培训记录
     * @param employeeId 员工ID
     * @return 培训记录列表
     */
    List<TrainingRecordDTO> getEmployeeTrainingRecords(String employeeId);
    
    /**
     * 创建培训需求
     * @param trainingNeedDTO 培训需求信息
     * @return 创建的培训需求ID
     */
    String createTrainingNeed(TrainingNeedDTO trainingNeedDTO);
    
    /**
     * 检查员工是否已完成指定培训
     * @param employeeId 员工ID
     * @param trainingCode 培训代码
     * @return 是否已完成培训
     */
    boolean isTrainingCompleted(String employeeId, String trainingCode);
    
    // ========== 与设备管理系统集成 ==========
    
    /**
     * 获取设备信息
     * @param equipmentId 设备ID
     * @return 设备信息
     */
    EquipmentDTO getEquipmentInfo(String equipmentId);
    
    /**
     * 获取设备维护记录
     * @param equipmentId 设备ID
     * @return 维护记录列表
     */
    List<MaintenanceRecordDTO> getEquipmentMaintenanceRecords(String equipmentId);
    
    // ========== 与MES系统集成 ==========
    
    /**
     * 获取生产批次信息
     * @param batchNumber 批次号
     * @return 批次信息
     */
    BatchInfoDTO getBatchInfo(String batchNumber);
    
    /**
     * 获取生产工艺参数
     * @param processId 工艺ID
     * @return 工艺参数
     */
    ProcessParameterDTO getProcessParameters(String processId);
    
    // ========== 与LIMS系统集成 ==========
    
    /**
     * 获取测试结果
     * @param sampleId 样品ID
     * @return 测试结果
     */
    TestResultDTO getTestResults(String sampleId);
    
    /**
     * 获取质量标准
     * @param standardCode 标准代码
     * @return 质量标准
     */
    QualityStandardDTO getQualityStandard(String standardCode);
    
    // ========== 与ERP系统集成 ==========
    
    /**
     * 获取供应商信息
     * @param supplierId 供应商ID
     * @return 供应商信息
     */
    SupplierDTO getSupplierInfo(String supplierId);
    
    /**
     * 获取物料信息
     * @param materialCode 物料代码
     * @return 物料信息
     */
    MaterialDTO getMaterialInfo(String materialCode);
    
    // ========== 通用集成方法 ==========
    
    /**
     * 向其他系统发送通知
     * @param notificationDTO 通知信息
     * @return 是否发送成功
     */
    boolean sendNotification(NotificationDTO notificationDTO);
    
    /**
     * 从其他系统获取配置信息
     * @param systemName 系统名称
     * @param configKey 配置键
     * @return 配置值
     */
    Object getSystemConfig(String systemName, String configKey);
    
    /**
     * 执行跨系统事务
     * @param transactionDTO 事务信息
     * @return 事务执行结果
     */
    TransactionResultDTO executeCrossSystemTransaction(TransactionDTO transactionDTO);
}

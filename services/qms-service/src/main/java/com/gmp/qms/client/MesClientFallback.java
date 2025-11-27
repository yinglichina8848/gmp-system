package com.gmp.qms.client;

import com.gmp.qms.dto.BatchInfoDTO;
import com.gmp.qms.dto.NotificationDTO;
import com.gmp.qms.dto.ProcessParameterDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * MES系统客户端降级类，实现服务降级机制
 * 当MES系统不可用时，提供默认响应，确保QMS系统正常运行
 * 
 * @author GMP系统开发团队
 */
@Component
@Slf4j
public class MesClientFallback implements MesClient {
    
    @Override
    public BatchInfoDTO getBatchInfo(String batchNumber) {
        log.warn("[MES系统降级] 获取批次信息失败，批次号: {}", batchNumber);
        
        // 返回默认批次对象，包含基本信息
        BatchInfoDTO batchInfoDTO = new BatchInfoDTO();
        batchInfoDTO.setBatchNumber(batchNumber);
        batchInfoDTO.setProductName("[服务暂不可用] 产品信息");
        batchInfoDTO.setStatus("UNKNOWN");
        
        recordFallbackRequest("getBatchInfo", batchNumber);
        return batchInfoDTO;
    }
    
    @Override
    public ProcessParameterDTO getProcessParameters(String processId) {
        log.warn("[MES系统降级] 获取工艺参数失败，工艺ID: {}", processId);
        
        // 返回默认工艺参数对象
        ProcessParameterDTO parameterDTO = new ProcessParameterDTO();
        parameterDTO.setProcessId(processId);
        parameterDTO.setProcessName("[服务暂不可用] 工艺信息");
        
        recordFallbackRequest("getProcessParameters", processId);
        return parameterDTO;
    }
    
    @Override
    public void sendNotification(NotificationDTO notificationDTO) {
        log.warn("[MES系统降级] 发送通知失败");
        saveToRetryQueue("sendNotification", notificationDTO);
        recordFallbackRequest("sendNotification", notificationDTO.getTitle());
    }
    
    @Override
    public String getBatchStatus(String batchNumber) {
        log.warn("[MES系统降级] 获取批次状态失败，批次号: {}", batchNumber);
        recordFallbackRequest("getBatchStatus", batchNumber);
        return "UNKNOWN";
    }
    
    @Override
    public ProcessParameterDTO getBatchProcessExecution(String batchNumber) {
        log.warn("[MES系统降级] 获取批次工艺执行记录失败，批次号: {}", batchNumber);
        
        // 返回默认工艺参数对象
        ProcessParameterDTO parameterDTO = new ProcessParameterDTO();
        parameterDTO.setProcessId(batchNumber); // 使用批次号作为临时ID
        parameterDTO.setProcessName("[服务暂不可用] 批次工艺执行信息");
        
        recordFallbackRequest("getBatchProcessExecution", batchNumber);
        return parameterDTO;
    }
    
    /**
     * 记录降级请求信息
     * 
     * @param operation 操作类型
     * @param identifier 标识符
     */
    private void recordFallbackRequest(String operation, String identifier) {
        log.warn("[MES系统降级记录] 操作: {}, 标识: {}", operation, identifier);
    }
    
    /**
     * 保存到重试队列
     * 
     * @param operation 操作类型
     * @param data 数据对象
     */
    private void saveToRetryQueue(String operation, Object data) {
        log.info("[MES系统重试队列] 添加操作: {}, 数据: {}", operation, data);
        // TODO: 实现真正的重试队列存储逻辑
    }
}

package com.gmp.qms.client;

import com.gmp.qms.dto.NotificationDTO;
import com.gmp.qms.dto.QualityStandardDTO;
import com.gmp.qms.dto.TestResultDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * LIMS系统客户端降级类，实现服务降级机制
 * 当LIMS系统不可用时，提供默认响应，确保QMS系统正常运行
 * 
 * @author GMP系统开发团队
 */
@Component
@Slf4j
public class LimsClientFallback implements LimsClient {
    
    @Override
    public TestResultDTO getTestResults(String sampleId) {
        log.warn("[LIMS系统降级] 获取测试结果失败，样品ID: {}", sampleId);
        
        // 返回默认测试结果对象
        TestResultDTO testResultDTO = new TestResultDTO();
        testResultDTO.setSampleId(sampleId);
        testResultDTO.setStatus("UNKNOWN");
        testResultDTO.setDescription("[服务暂不可用] 测试结果无法获取");
        
        recordFallbackRequest("getTestResults", sampleId);
        return testResultDTO;
    }
    
    @Override
    public QualityStandardDTO getQualityStandard(String standardCode) {
        log.warn("[LIMS系统降级] 获取质量标准失败，标准代码: {}", standardCode);
        
        // 返回默认质量标准对象
        QualityStandardDTO standardDTO = new QualityStandardDTO();
        standardDTO.setCode(standardCode);
        standardDTO.setName("[服务暂不可用] 质量标准");
        
        recordFallbackRequest("getQualityStandard", standardCode);
        return standardDTO;
    }
    
    @Override
    public void sendNotification(NotificationDTO notificationDTO) {
        log.warn("[LIMS系统降级] 发送通知失败");
        saveToRetryQueue("sendNotification", notificationDTO);
        recordFallbackRequest("sendNotification", notificationDTO.getTitle());
    }
    
    @Override
    public TestResultDTO getSampleInfo(String sampleId) {
        log.warn("[LIMS系统降级] 获取样品信息失败，样品ID: {}", sampleId);
        
        // 返回默认样品信息对象
        TestResultDTO testResultDTO = new TestResultDTO();
        testResultDTO.setSampleId(sampleId);
        testResultDTO.setSampleName("[服务暂不可用] 样品信息");
        
        recordFallbackRequest("getSampleInfo", sampleId);
        return testResultDTO;
    }
    
    @Override
    public boolean isSampleTestCompleted(String sampleId) {
        log.warn("[LIMS系统降级] 检查样品测试完成状态失败，样品ID: {}", sampleId);
        recordFallbackRequest("isSampleTestCompleted", sampleId);
        
        // 默认返回false，避免在不确定的情况下误判
        return false;
    }
    
    /**
     * 记录降级请求信息
     * 
     * @param operation 操作类型
     * @param identifier 标识符
     */
    private void recordFallbackRequest(String operation, String identifier) {
        log.warn("[LIMS系统降级记录] 操作: {}, 标识: {}", operation, identifier);
    }
    
    /**
     * 保存到重试队列
     * 
     * @param operation 操作类型
     * @param data 数据对象
     */
    private void saveToRetryQueue(String operation, Object data) {
        log.info("[LIMS系统重试队列] 添加操作: {}, 数据: {}", operation, data);
        // TODO: 实现真正的重试队列存储逻辑
    }
}

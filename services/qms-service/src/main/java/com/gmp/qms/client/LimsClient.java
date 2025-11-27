package com.gmp.qms.client;

import com.gmp.qms.dto.NotificationDTO;
import com.gmp.qms.dto.QualityStandardDTO;
import com.gmp.qms.dto.TestResultDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * LIMS系统Feign客户端，定义与LIMS系统的通信接口
 * 
 * @author GMP系统开发团队
 */
@FeignClient(name = "lims-service", url = "${feign.client.lims.url}", fallback = LimsClientFallback.class)
public interface LimsClient {
    
    /**
     * 获取测试结果
     * 
     * @param sampleId 样品ID
     * @return 测试结果信息
     */
    @GetMapping("/api/samples/{sampleId}/test-results")
    TestResultDTO getTestResults(@PathVariable("sampleId") String sampleId);
    
    /**
     * 获取质量标准
     * 
     * @param standardCode 标准代码
     * @return 质量标准信息
     */
    @GetMapping("/api/standards/{standardCode}")
    QualityStandardDTO getQualityStandard(@PathVariable("standardCode") String standardCode);
    
    /**
     * 发送通知到LIMS系统
     * 
     * @param notificationDTO 通知信息
     */
    @PostMapping("/api/notifications")
    void sendNotification(@RequestBody NotificationDTO notificationDTO);
    
    /**
     * 获取样品信息
     * 
     * @param sampleId 样品ID
     * @return 测试结果（包含样品信息）
     */
    @GetMapping("/api/samples/{sampleId}")
    TestResultDTO getSampleInfo(@PathVariable("sampleId") String sampleId);
    
    /**
     * 检查样品测试是否完成
     * 
     * @param sampleId 样品ID
     * @return 是否已完成测试
     */
    @GetMapping("/api/samples/{sampleId}/completed")
    boolean isSampleTestCompleted(@PathVariable("sampleId") String sampleId);
}

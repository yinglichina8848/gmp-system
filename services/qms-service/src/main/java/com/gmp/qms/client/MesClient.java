package com.gmp.qms.client;

import com.gmp.qms.dto.BatchInfoDTO;
import com.gmp.qms.dto.NotificationDTO;
import com.gmp.qms.dto.ProcessParameterDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * MES系统Feign客户端，定义与MES系统的通信接口
 * 
 * @author GMP系统开发团队
 */
@FeignClient(name = "mes-service", url = "${feign.client.mes.url}", fallback = MesClientFallback.class)
public interface MesClient {
    
    /**
     * 获取批次信息
     * 
     * @param batchNumber 批次号
     * @return 批次信息
     */
    @GetMapping("/api/batches/{batchNumber}")
    BatchInfoDTO getBatchInfo(@PathVariable("batchNumber") String batchNumber);
    
    /**
     * 获取工艺参数
     * 
     * @param processId 工艺ID
     * @return 工艺参数信息
     */
    @GetMapping("/api/processes/{processId}/parameters")
    ProcessParameterDTO getProcessParameters(@PathVariable("processId") String processId);
    
    /**
     * 发送通知到MES系统
     * 
     * @param notificationDTO 通知信息
     */
    @PostMapping("/api/notifications")
    void sendNotification(@RequestBody NotificationDTO notificationDTO);
    
    /**
     * 获取批次生产状态
     * 
     * @param batchNumber 批次号
     * @return 生产状态
     */
    @GetMapping("/api/batches/{batchNumber}/status")
    String getBatchStatus(@PathVariable("batchNumber") String batchNumber);
    
    /**
     * 获取批次的工艺执行记录
     * 
     * @param batchNumber 批次号
     * @return 工艺参数信息
     */
    @GetMapping("/api/batches/{batchNumber}/process-execution")
    ProcessParameterDTO getBatchProcessExecution(@PathVariable("batchNumber") String batchNumber);
}

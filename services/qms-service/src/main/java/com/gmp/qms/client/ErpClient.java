package com.gmp.qms.client;

import com.gmp.qms.dto.MaterialDTO;
import com.gmp.qms.dto.NotificationDTO;
import com.gmp.qms.dto.SupplierDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * ERP系统Feign客户端，定义与ERP系统的通信接口
 * 
 * @author GMP系统开发团队
 */
@FeignClient(name = "erp-service", url = "${feign.client.erp.url}", fallback = ErpClientFallback.class)
public interface ErpClient {
    
    /**
     * 获取供应商信息
     * 
     * @param supplierId 供应商ID
     * @return 供应商信息
     */
    @GetMapping("/api/suppliers/{supplierId}")
    SupplierDTO getSupplier(@PathVariable("supplierId") String supplierId);
    
    /**
     * 获取物料信息
     * 
     * @param materialCode 物料代码
     * @return 物料信息
     */
    @GetMapping("/api/materials/{materialCode}")
    MaterialDTO getMaterial(@PathVariable("materialCode") String materialCode);
    
    /**
     * 发送通知到ERP系统
     * 
     * @param notificationDTO 通知信息
     */
    @PostMapping("/api/notifications")
    void sendNotification(@RequestBody NotificationDTO notificationDTO);
    
    /**
     * 检查供应商是否合格
     * 
     * @param supplierId 供应商ID
     * @return 供应商是否合格
     */
    @GetMapping("/api/suppliers/{supplierId}/qualified")
    boolean isSupplierQualified(@PathVariable("supplierId") String supplierId);
    
    /**
     * 获取物料批次信息
     * 
     * @param materialCode 物料代码
     * @param batchNumber 批次号
     * @return 物料信息（包含批次信息）
     */
    @GetMapping("/api/materials/{materialCode}/batches/{batchNumber}")
    MaterialDTO getMaterialBatch(@PathVariable("materialCode") String materialCode, 
                               @PathVariable("batchNumber") String batchNumber);
}

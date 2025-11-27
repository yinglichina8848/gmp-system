package com.gmp.qms.client;

import com.gmp.qms.dto.MaterialDTO;
import com.gmp.qms.dto.NotificationDTO;
import com.gmp.qms.dto.SupplierDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * ERP系统客户端降级实现，当ERP服务不可用时提供备用功能
 * 
 * @author GMP系统开发团队
 */
@Component
public class ErpClientFallback implements ErpClient {

    private static final Logger log = LoggerFactory.getLogger(ErpClientFallback.class);

    @Override
    public SupplierDTO getSupplier(String supplierId) {
        log.warn("ERP服务调用降级: getSupplier，supplierId={}", supplierId);
        recordFallbackRequest("getSupplier", supplierId);
        SupplierDTO defaultSupplier = new SupplierDTO();
        defaultSupplier.setSupplierId(supplierId);
        defaultSupplier.setSupplierName("未知供应商");
        defaultSupplier.setStatus("降级数据");
        return defaultSupplier;
    }

    @Override
    public MaterialDTO getMaterial(String materialCode) {
        log.warn("ERP服务调用降级: getMaterial，materialCode={}", materialCode);
        recordFallbackRequest("getMaterial", materialCode);
        MaterialDTO defaultMaterial = new MaterialDTO();
        defaultMaterial.setMaterialCode(materialCode);
        defaultMaterial.setMaterialName("未知物料");
        defaultMaterial.setStatus("降级数据");
        return defaultMaterial;
    }

    @Override
    public void sendNotification(NotificationDTO notificationDTO) {
        log.warn("ERP服务调用降级: sendNotification，通知内容={}", notificationDTO);
        recordFallbackRequest("sendNotification", notificationDTO.toString());
        // 记录失败的通知，可在后续重试
        saveToRetryQueue("ERP_NOTIFICATION", notificationDTO);
    }

    @Override
    public boolean isSupplierQualified(String supplierId) {
        log.warn("ERP服务调用降级: isSupplierQualified，supplierId={}", supplierId);
        recordFallbackRequest("isSupplierQualified", supplierId);
        // 降级策略：默认返回false，表示需要进一步验证
        return false;
    }

    @Override
    public MaterialDTO getMaterialBatch(String materialCode, String batchNumber) {
        log.warn("ERP服务调用降级: getMaterialBatch，materialCode={}, batchNumber={}", materialCode, batchNumber);
        recordFallbackRequest("getMaterialBatch", materialCode + "," + batchNumber);
        MaterialDTO defaultMaterial = new MaterialDTO();
        defaultMaterial.setMaterialCode(materialCode);
        defaultMaterial.setBatchNumber(batchNumber);
        defaultMaterial.setMaterialName("未知物料批次");
        defaultMaterial.setStatus("降级数据");
        return defaultMaterial;
    }

    /**
     * 记录降级请求信息，用于监控和分析
     */
    private void recordFallbackRequest(String methodName, String params) {
        // 这里可以实现记录降级请求的逻辑，例如保存到数据库或发送到监控系统
        log.info("降级请求记录: 方法={}, 参数={}", methodName, params);
    }

    /**
     * 保存请求到重试队列，后续可进行重试
     */
    private <T> void saveToRetryQueue(String queueType, T data) {
        // 这里可以实现将请求保存到重试队列的逻辑
        log.info("保存到重试队列: 类型={}, 数据={}", queueType, data);
    }
}
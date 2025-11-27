package com.gmp.qms.client;

import com.gmp.qms.dto.EdmsDocumentDTO;
import com.gmp.qms.dto.NotificationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * EDMS系统Feign客户端，定义与EDMS系统的通信接口
 * 
 * @author GMP系统开发团队
 */
@FeignClient(name = "edms-service", url = "${feign.client.edms.url}", fallback = EdmsClientFallback.class)
public interface EdmsClient {
    
    /**
     * 获取文档详情
     * 
     * @param documentId 文档ID
     * @return 文档信息
     */
    @GetMapping("/api/documents/{documentId}")
    EdmsDocumentDTO getDocument(@PathVariable("documentId") String documentId);
    
    /**
     * 创建新文档
     * 
     * @param documentDTO 文档信息
     * @return 创建的文档信息
     */
    @PostMapping("/api/documents")
    EdmsDocumentDTO createDocument(@RequestBody EdmsDocumentDTO documentDTO);
    
    /**
     * 更新文档
     * 
     * @param documentId 文档ID
     * @param documentDTO 文档信息
     */
    @PutMapping("/api/documents/{documentId}")
    void updateDocument(@PathVariable("documentId") String documentId, @RequestBody EdmsDocumentDTO documentDTO);
    
    /**
     * 根据条件搜索文档
     * 
     * @param criteria 搜索条件
     * @return 文档列表
     */
    @PostMapping("/api/documents/search")
    List<EdmsDocumentDTO> searchDocuments(@RequestBody Map<String, Object> criteria);
    
    /**
     * 发送通知到EDMS系统
     * 
     * @param notificationDTO 通知信息
     */
    @PostMapping("/api/notifications")
    void sendNotification(@RequestBody NotificationDTO notificationDTO);
    
    /**
     * 检查文档状态
     * 
     * @param documentId 文档ID
     * @return 文档状态
     */
    @GetMapping("/api/documents/{documentId}/status")
    String getDocumentStatus(@PathVariable("documentId") String documentId);
    
    /**
     * 获取文档的版本历史
     * 
     * @param documentId 文档ID
     * @return 版本历史列表
     */
    @GetMapping("/api/documents/{documentId}/versions")
    List<EdmsDocumentDTO> getDocumentVersions(@PathVariable("documentId") String documentId);
}

package com.gmp.edms.service;

import com.gmp.edms.dto.DocumentVersionDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文档版本服务接口
 */
public interface DocumentVersionService {
    
    /**
     * 上传新版本文档
     */
    DocumentVersionDTO uploadDocumentVersion(Long documentId, MultipartFile file, 
                                           String versionType, String changeReason, 
                                           String changeSummary, String author) throws Exception;
    
    /**
     * 获取文档版本列表
     */
    List<DocumentVersionDTO> getDocumentVersions(Long documentId);
    
    /**
     * 获取文档当前版本
     */
    DocumentVersionDTO getCurrentVersion(Long documentId);
    
    /**
     * 根据版本ID获取版本信息
     */
    DocumentVersionDTO getVersionById(Long versionId);
    
    /**
     * 下载文档版本
     */
    byte[] downloadDocumentVersion(Long versionId) throws Exception;
    
    /**
     * 删除文档版本
     */
    void deleteDocumentVersion(Long versionId) throws Exception;
    
    /**
     * 设置文档当前版本
     */
    void setCurrentVersion(Long documentId, Long versionId);
    
    /**
     * 审批文档版本
     */
    DocumentVersionDTO approveVersion(Long versionId, String approver, boolean isApproved, String comments);
    
    /**
     * 比较两个版本的差异
     */
    String compareVersions(Long versionId1, Long versionId2) throws Exception;
    
    /**
     * 生成新版本号
     */
    String generateNewVersionNumber(Long documentId, String versionType);
    
    /**
     * 检查文件是否已存在（基于校验和）
     */
    boolean checkFileExists(String checksum);
    
    /**
     * 获取版本历史统计
     */
    long getVersionCount(Long documentId);
    
    /**
     * 回滚到指定版本
     */
    DocumentVersionDTO rollbackToVersion(Long versionId) throws Exception;
    
    /**
     * 恢复文档到指定版本
     */
    void restoreDocumentVersion(Long documentId, Long versionId) throws Exception;
    
    /**
     * 比较文档版本（详细比较）
     */
    String compareDocumentVersions(Long documentId, Long fromVersionId, Long toVersionId) throws Exception;
}

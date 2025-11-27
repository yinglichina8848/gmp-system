package com.gmp.edms.service;

import java.util.Set;

/**
 * 文档权限服务接口
 * 实现设计文档中的复杂权限控制架构
 */
public interface DocumentPermissionService {

    /**
     * 检查用户对文档的访问权限
     * 
     * @param userId     用户ID
     * @param documentId 文档ID
     * @param permission 权限类型
     * @return 是否有权限
     */
    boolean hasPermission(String userId, Long documentId, PermissionType permission);

    /**
     * 检查用户对文档的创建权限
     * 
     * @param userId     用户ID
     * @param categoryId 分类ID
     * @return 是否有权限
     */
    boolean hasCreatePermission(String userId, Long categoryId);

    /**
     * 获取用户对文档的所有权限
     * 
     * @param userId     用户ID
     * @param documentId 文档ID
     * @return 权限集合
     */
    Set<PermissionType> getUserPermissions(String userId, Long documentId);

    /**
     * 检查用户是否可以审批指定文档
     * 
     * @param userId     用户ID
     * @param documentId 文档ID
     * @return 是否可以审批
     */
    boolean canApprove(String userId, Long documentId);

    /**
     * 检查用户是否可以查看文档内容
     * 
     * @param userId     用户ID
     * @param documentId 文档ID
     * @return 是否可以查看
     */
    boolean canView(String userId, Long documentId);

    /**
     * 检查用户是否可以编辑文档
     * 
     * @param userId     用户ID
     * @param documentId 文档ID
     * @return 是否可以编辑
     */
    boolean canEdit(String userId, Long documentId);

    /**
     * 检查用户是否可以删除文档
     * 
     * @param userId     用户ID
     * @param documentId 文档ID
     * @return 是否可以删除
     */
    boolean canDelete(String userId, Long documentId);

    /**
     * 检查用户是否可以下载文档
     * 
     * @param userId     用户ID
     * @param documentId 文档ID
     * @return 是否可以下载
     */
    boolean canDownload(String userId, Long documentId);

    /**
     * 获取用户可访问的文档ID列表
     * 
     * @param userId     用户ID
     * @param permission 权限类型
     * @return 文档ID列表
     */
    Set<Long> getAccessibleDocumentIds(String userId, PermissionType permission);

    /**
     * 基于时间窗口检查权限
     * 
     * @param userId      用户ID
     * @param documentId  文档ID
     * @param permission  权限类型
     * @param currentTime 当前时间
     * @return 是否有权限
     */
    boolean hasPermissionWithTimeWindow(String userId, Long documentId, PermissionType permission,
            java.time.LocalDateTime currentTime);

    /**
     * 基于地理位置检查权限
     * 
     * @param userId     用户ID
     * @param documentId 文档ID
     * @param permission 权限类型
     * @param location   地理位置
     * @return 是否有权限
     */
    boolean hasPermissionWithLocation(String userId, Long documentId, PermissionType permission, String location);

    /**
     * 权限类型枚举
     */
    enum PermissionType {
        VIEW("查看"),
        EDIT("编辑"),
        DELETE("删除"),
        DOWNLOAD("下载"),
        APPROVE("审批"),
        PUBLISH("发布"),
        WITHDRAW("撤回"),
        SHARE("分享"),
        PRINT("打印"),
        COPY("复制");

        private final String description;

        PermissionType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
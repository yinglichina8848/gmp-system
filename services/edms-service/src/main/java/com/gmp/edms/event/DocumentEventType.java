package com.gmp.edms.event;

/**
 * 文档事件类型枚举
 */
public enum DocumentEventType {

    /**
     * 文档创建
     */
    DOCUMENT_CREATED,

    /**
     * 文档更新
     */
    DOCUMENT_UPDATED,

    /**
     * 文档删除
     */
    DOCUMENT_DELETED,

    /**
     * 文档上传
     */
    DOCUMENT_UPLOADED,

    /**
     * 文档下载
     */
    DOCUMENT_DOWNLOADED,

    /**
     * 文档审批开始
     */
    APPROVAL_STARTED,

    /**
     * 文档审批通过
     */
    APPROVAL_APPROVED,

    /**
     * 文档审批拒绝
     */
    APPROVAL_REJECTED,

    /**
     * 文档审批撤回
     */
    APPROVAL_WITHDRAWN,

    /**
     * 文档版本创建
     */
    VERSION_CREATED,

    /**
     * 文档状态变更
     */
    STATUS_CHANGED,

    /**
     * 文档权限变更
     */
    PERMISSION_CHANGED
}

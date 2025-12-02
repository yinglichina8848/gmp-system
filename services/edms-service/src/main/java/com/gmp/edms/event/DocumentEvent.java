package com.gmp.edms.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 文档事件模型
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentEvent {

    /**
     * 事件类型
     */
    private DocumentEventType eventType;

    /**
     * 文档ID
     */
    private Long documentId;

    /**
     * 文档名称
     */
    private String documentName;

    /**
     * 操作用户
     */
    private String operator;

    /**
     * 事件时间
     */
    private LocalDateTime eventTime;

    /**
     * 事件描述
     */
    private String description;

    /**
     * 事件数据
     */
    private Object data;
}

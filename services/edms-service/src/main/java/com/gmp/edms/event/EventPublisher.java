package com.gmp.edms.event;

/**
 * 事件发布器接口
 */
public interface EventPublisher {

    /**
     * 发布文档事件
     * 
     * @param event 文档事件
     */
    void publishDocumentEvent(DocumentEvent event);

    /**
     * 发布通用事件
     * 
     * @param exchange   交换机名称
     * @param routingKey 路由键
     * @param event      事件对象
     */
    void publishEvent(String exchange, String routingKey, Object event);
}

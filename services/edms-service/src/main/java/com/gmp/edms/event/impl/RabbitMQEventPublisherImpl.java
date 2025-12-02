package com.gmp.edms.event.impl;

import com.gmp.edms.event.DocumentEvent;
import com.gmp.edms.event.EventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * RabbitMQ事件发布器实现
 */
@Component
@RequiredArgsConstructor
public class RabbitMQEventPublisherImpl implements EventPublisher {

    private final RabbitTemplate rabbitTemplate;

    /**
     * 文档事件交换机名称
     */
    private static final String DOCUMENT_EVENT_EXCHANGE = "edms.document.events";

    /**
     * 文档事件路由键前缀
     */
    private static final String DOCUMENT_EVENT_ROUTING_KEY_PREFIX = "document.";

    @Override
    public void publishDocumentEvent(DocumentEvent event) {
        // 构建路由键
        String routingKey = DOCUMENT_EVENT_ROUTING_KEY_PREFIX + event.getEventType().name().toLowerCase();

        // 发布事件
        rabbitTemplate.convertAndSend(DOCUMENT_EVENT_EXCHANGE, routingKey, event);
    }

    @Override
    public void publishEvent(String exchange, String routingKey, Object event) {
        // 发布通用事件
        rabbitTemplate.convertAndSend(exchange, routingKey, event);
    }
}

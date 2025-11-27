package com.gmp.qms.service.messaging;

import com.gmp.qms.config.McpMessageQueueConfig;
import com.gmp.qms.config.McpPerformanceConfig;
import com.gmp.qms.monitoring.McpMonitoringService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * MCP消息服务，用于处理系统间的消息发送操作
 * 
 * @author GMP系统开发团队
 */
@Service
public class McpMessageService {
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    @Autowired
    private McpPerformanceConfig performanceConfig;
    
    @Autowired
    private McpMonitoringService monitoringService;
    
    /**
     * 发送消息到EDMS系统
     */
    public String sendToEdms(Object payload, String operation) {
        return sendMessage("edms", McpMessageQueueConfig.ROUTING_KEY_PREFIX + "to.edms", payload, operation);
    }
    
    /**
     * 发送消息到MES系统
     */
    public String sendToMes(Object payload, String operation) {
        return sendMessage("mes", McpMessageQueueConfig.ROUTING_KEY_PREFIX + "to.mes", payload, operation);
    }
    
    /**
     * 发送消息到LIMS系统
     */
    public String sendToLims(Object payload, String operation) {
        return sendMessage("lims", McpMessageQueueConfig.ROUTING_KEY_PREFIX + "to.lims", payload, operation);
    }
    
    /**
     * 发送消息到ERP系统
     */
    public String sendToErp(Object payload, String operation) {
        return sendMessage("erp", McpMessageQueueConfig.ROUTING_KEY_PREFIX + "to.erp", payload, operation);
    }
    
    /**
     * 发送消息到培训系统
     */
    public String sendToTraining(Object payload, String operation) {
        return sendMessage("training", McpMessageQueueConfig.ROUTING_KEY_PREFIX + "to.training", payload, operation);
    }
    
    /**
     * 发送消息到设备系统
     */
    public String sendToEquipment(Object payload, String operation) {
        return sendMessage("equipment", McpMessageQueueConfig.ROUTING_KEY_PREFIX + "to.equipment", payload, operation);
    }
    
    /**
     * 发送优先级消息
     */
    public String sendPriorityMessage(String targetSystem, Object payload, String operation, int priority) {
        String routingKey = McpMessageQueueConfig.ROUTING_KEY_PREFIX + "to." + targetSystem;
        
        // 创建消息包装器
        Map<String, Object> messageWrapper = createMessageWrapper(payload, operation);
        
        // 生成消息ID
        String messageId = messageWrapper.get("messageId").toString();
        
        // 开始计时
        McpMonitoringService.McpTimer timer = monitoringService.startTimer("mq_send_priority_" + targetSystem);
        
        try {
            // 发送优先级消息
            rabbitTemplate.convertAndSend(
                McpMessageQueueConfig.GMP_MCP_EXCHANGE,
                routingKey,
                messageWrapper,
                message -> {
                    message.getMessageProperties().setPriority(Math.min(priority, 10)); // 最大优先级为10
                    message.getMessageProperties().setMessageId(messageId);
                    message.getMessageProperties().setContentType("application/json");
                    message.getMessageProperties().setTimestamp(new java.util.Date());
                    return message;
                }
            );
            
            timer.complete(true);
            return messageId;
        } catch (Exception e) {
            timer.complete(false);
            throw new RuntimeException("Failed to send priority message to " + targetSystem, e);
        }
    }
    
    /**
     * 发送延迟消息
     */
    public String sendDelayedMessage(String targetSystem, Object payload, String operation, long delayMs) {
        String routingKey = McpMessageQueueConfig.ROUTING_KEY_PREFIX + "to." + targetSystem;
        
        // 创建消息包装器
        Map<String, Object> messageWrapper = createMessageWrapper(payload, operation);
        
        // 生成消息ID
        String messageId = messageWrapper.get("messageId").toString();
        
        // 开始计时
        McpMonitoringService.McpTimer timer = monitoringService.startTimer("mq_send_delayed_" + targetSystem);
        
        try {
            // 发送延迟消息（通过TTL实现）
            rabbitTemplate.convertAndSend(
                McpMessageQueueConfig.GMP_MCP_EXCHANGE,
                routingKey,
                messageWrapper,
                message -> {
                    message.getMessageProperties().setDelay((int) delayMs);
                    message.getMessageProperties().setMessageId(messageId);
                    message.getMessageProperties().setContentType("application/json");
                    message.getMessageProperties().setTimestamp(new java.util.Date());
                    return message;
                }
            );
            
            timer.complete(true);
            return messageId;
        } catch (Exception e) {
            timer.complete(false);
            throw new RuntimeException("Failed to send delayed message to " + targetSystem, e);
        }
    }
    
    /**
     * 发送带回调的消息
     */
    public String sendMessageWithCallback(String targetSystem, Object payload, String operation, MessageCallback callback) {
        String routingKey = McpMessageQueueConfig.ROUTING_KEY_PREFIX + "to." + targetSystem;
        
        // 创建消息包装器
        Map<String, Object> messageWrapper = createMessageWrapper(payload, operation);
        
        // 生成消息ID
        String messageId = messageWrapper.get("messageId").toString();
        
        // 开始计时
        McpMonitoringService.McpTimer timer = monitoringService.startTimer("mq_send_callback_" + targetSystem);
        
        try {
            // 设置确认回调
            rabbitTemplate.convertAndSend(
                McpMessageQueueConfig.GMP_MCP_EXCHANGE,
                routingKey,
                messageWrapper,
                message -> {
                    message.getMessageProperties().setMessageId(messageId);
                    message.getMessageProperties().setContentType("application/json");
                    message.getMessageProperties().setTimestamp(new java.util.Date());
                    
                    // 设置自定义确认回调
                    rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
                        if (callback != null) {
                            if (ack) {
                                callback.onSuccess(messageId);
                            } else {
                                callback.onFailure(messageId, cause);
                            }
                        }
                    });
                    
                    return message;
                }
            );
            
            timer.complete(true);
            return messageId;
        } catch (Exception e) {
            timer.complete(false);
            if (callback != null) {
                callback.onFailure(messageId, e.getMessage());
            }
            return messageId; // 即使失败也返回消息ID用于跟踪
        }
    }
    
    /**
     * 通用消息发送方法
     */
    private String sendMessage(String targetSystem, String routingKey, Object payload, String operation) {
        // 创建消息包装器
        Map<String, Object> messageWrapper = createMessageWrapper(payload, operation);
        
        // 生成消息ID
        String messageId = messageWrapper.get("messageId").toString();
        
        // 开始计时
        McpMonitoringService.McpTimer timer = monitoringService.startTimer("mq_send_" + targetSystem);
        
        try {
            // 发送消息
            rabbitTemplate.convertAndSend(
                McpMessageQueueConfig.GMP_MCP_EXCHANGE,
                routingKey,
                messageWrapper,
                message -> {
                    message.getMessageProperties().setMessageId(messageId);
                    message.getMessageProperties().setContentType("application/json");
                    message.getMessageProperties().setTimestamp(new java.util.Date());
                    return message;
                }
            );
            
            timer.complete(true);
            return messageId;
        } catch (Exception e) {
            timer.complete(false);
            throw new RuntimeException("Failed to send message to " + targetSystem, e);
        }
    }
    
    /**
     * 创建消息包装器，添加必要的元数据
     */
    private Map<String, Object> createMessageWrapper(Object payload, String operation) {
        Map<String, Object> wrapper = new HashMap<>();
        
        // 生成唯一消息ID
        wrapper.put("messageId", UUID.randomUUID().toString());
        
        // 添加发送时间
        wrapper.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        
        // 添加来源系统
        wrapper.put("sourceSystem", "QMS");
        
        // 添加操作类型
        wrapper.put("operation", operation);
        
        // 添加消息体
        wrapper.put("payload", payload);
        
        // 添加重试次数
        wrapper.put("retryCount", 0);
        
        return wrapper;
    }
    
    /**
     * 消息回调接口
     */
    public interface MessageCallback {
        /**
         * 消息发送成功回调
         */
        void onSuccess(String messageId);
        
        /**
         * 消息发送失败回调
         */
        void onFailure(String messageId, String error);
    }
}

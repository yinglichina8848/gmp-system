package com.gmp.qms.listener;

import com.gmp.qms.monitoring.McpMonitoringService;
import com.gmp.qms.service.messaging.McpMessageService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * MCP消息监听器，用于接收和处理来自其他系统的消息
 * 
 * @author GMP系统开发团队
 */
@Component
public class McpMessageListener {
    
    @Autowired
    private McpMonitoringService monitoringService;
    
    // 消息处理器注册表
    private final Map<String, Consumer<Map<String, Object>>> messageHandlers = new ConcurrentHashMap<>();
    
    /**
     * 监听来自EDMS系统的消息
     */
    @RabbitListener(queues = "qms.edms.queue")
    public void listenEdmsMessages(Map<String, Object> message) {
        processMessage("edms", message);
    }
    
    /**
     * 监听来自MES系统的消息
     */
    @RabbitListener(queues = "qms.mes.queue")
    public void listenMesMessages(Map<String, Object> message) {
        processMessage("mes", message);
    }
    
    /**
     * 监听来自LIMS系统的消息
     */
    @RabbitListener(queues = "qms.lims.queue")
    public void listenLimsMessages(Map<String, Object> message) {
        processMessage("lims", message);
    }
    
    /**
     * 监听来自ERP系统的消息
     */
    @RabbitListener(queues = "qms.erp.queue")
    public void listenErpMessages(Map<String, Object> message) {
        processMessage("erp", message);
    }
    
    /**
     * 监听来自培训系统的消息
     */
    @RabbitListener(queues = "qms.training.queue")
    public void listenTrainingMessages(Map<String, Object> message) {
        processMessage("training", message);
    }
    
    /**
     * 监听来自设备系统的消息
     */
    @RabbitListener(queues = "qms.equipment.queue")
    public void listenEquipmentMessages(Map<String, Object> message) {
        processMessage("equipment", message);
    }
    
    /**
     * 监听死信队列中的失败消息
     */
    @RabbitListener(queues = "qms.mcp.dead.letter.queue")
    public void listenDeadLetterMessages(Map<String, Object> message) {
        String messageId = message.getOrDefault("messageId", "unknown").toString();
        String sourceSystem = message.getOrDefault("sourceSystem", "unknown").toString();
        String operation = message.getOrDefault("operation", "unknown").toString();
        Integer retryCount = (Integer) message.getOrDefault("retryCount", 0);
        
        // 记录死信消息
        monitoringService.incrementCounter("mq_dead_letter", sourceSystem);
        
        // 这里可以实现死信处理逻辑，如记录到失败表、告警等
        System.err.println("Dead letter message received: " + messageId + 
                " from " + sourceSystem + 
                " operation: " + operation + 
                " retryCount: " + retryCount);
    }
    
    /**
     * 注册消息处理器
     * @param operation 操作类型
     * @param handler 消息处理函数
     */
    public void registerMessageHandler(String operation, Consumer<Map<String, Object>> handler) {
        messageHandlers.put(operation, handler);
    }
    
    /**
     * 注销消息处理器
     * @param operation 操作类型
     */
    public void unregisterMessageHandler(String operation) {
        messageHandlers.remove(operation);
    }
    
    /**
     * 处理接收到的消息
     */
    private void processMessage(String sourceSystem, Map<String, Object> message) {
        String messageId = message.getOrDefault("messageId", "unknown").toString();
        String operation = message.getOrDefault("operation", "unknown").toString();
        
        // 开始计时
        McpMonitoringService.McpTimer timer = monitoringService.startTimer("mq_receive_" + sourceSystem);
        
        try {
            // 记录接收计数
            monitoringService.incrementCounter("mq_receive", sourceSystem);
            
            // 查找对应的处理器
            Consumer<Map<String, Object>> handler = messageHandlers.get(operation);
            
            if (handler != null) {
                // 使用注册的处理器处理消息
                handler.accept(message);
                timer.complete(true);
            } else {
                // 默认处理逻辑
                handleDefaultMessage(sourceSystem, message);
                timer.complete(true);
            }
        } catch (Exception e) {
            // 记录错误
            monitoringService.incrementCounter("mq_process_error", sourceSystem);
            timer.complete(false);
            
            // 这里可以实现重试逻辑，或者将消息发送到死信队列
            throw new RuntimeException("Failed to process message " + messageId + " from " + sourceSystem, e);
        }
    }
    
    /**
     * 默认消息处理逻辑
     */
    private void handleDefaultMessage(String sourceSystem, Map<String, Object> message) {
        // 记录未处理的消息
        System.out.println("Received message from " + sourceSystem + 
                ", messageId: " + message.getOrDefault("messageId", "unknown") + 
                ", operation: " + message.getOrDefault("operation", "unknown"));
        
        // 这里可以实现一些通用的消息处理逻辑
        // 例如日志记录、基本验证等
    }
    
    /**
     * 获取当前注册的消息处理器数量
     */
    public int getRegisteredHandlersCount() {
        return messageHandlers.size();
    }
    
    /**
     * 检查是否有针对特定操作的处理器
     */
    public boolean hasHandlerForOperation(String operation) {
        return messageHandlers.containsKey(operation);
    }
}

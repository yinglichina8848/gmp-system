package com.gmp.qms.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MCP消息队列配置，实现系统间的异步通信机制
 * 
 * @author GMP系统开发团队
 */
@Configuration
public class McpMessageQueueConfig {
    
    @Autowired
    private McpPerformanceConfig performanceConfig;
    
    // 队列名称常量
    public static final String QMS_TO_EDMS_QUEUE = "gmp.qms.to.edms.queue";
    public static final String QMS_TO_MES_QUEUE = "gmp.qms.to.mes.queue";
    public static final String QMS_TO_LIMS_QUEUE = "gmp.qms.to.lims.queue";
    public static final String QMS_TO_ERP_QUEUE = "gmp.qms.to.erp.queue";
    public static final String QMS_TO_TRAINING_QUEUE = "gmp.qms.to.training.queue";
    public static final String QMS_TO_EQUIPMENT_QUEUE = "gmp.qms.to.equipment.queue";
    
    // 交换机名称
    public static final String GMP_MCP_EXCHANGE = "gmp.mcp.exchange";
    
    // 路由键前缀
    public static final String ROUTING_KEY_PREFIX = "gmp.mcp.";
    
    /**
     * 创建主交换机
     */
    @Bean
    public DirectExchange gmpMcpExchange() {
        return new DirectExchange(GMP_MCP_EXCHANGE, true, false);
    }
    
    /**
     * 创建QMS到EDMS的队列
     */
    @Bean
    public Queue qmsToEdmsQueue() {
        McpPerformanceConfig.MessageQueue config = performanceConfig.getMessageQueue();
        return QueueBuilder.durable(QMS_TO_EDMS_QUEUE)
            .withArgument("x-dead-letter-exchange", GMP_MCP_EXCHANGE)
            .withArgument("x-dead-letter-routing-key", "dead.letter." + QMS_TO_EDMS_QUEUE)
            .withArgument("x-message-ttl", config.getMessageTtl())
            .withArgument("x-max-priority", config.getMaxPriority())
            .build();
    }
    
    /**
     * 创建QMS到MES的队列
     */
    @Bean
    public Queue qmsToMesQueue() {
        McpPerformanceConfig.MessageQueue config = performanceConfig.getMessageQueue();
        return QueueBuilder.durable(QMS_TO_MES_QUEUE)
            .withArgument("x-dead-letter-exchange", GMP_MCP_EXCHANGE)
            .withArgument("x-dead-letter-routing-key", "dead.letter." + QMS_TO_MES_QUEUE)
            .withArgument("x-message-ttl", config.getMessageTtl())
            .withArgument("x-max-priority", config.getMaxPriority())
            .build();
    }
    
    /**
     * 创建QMS到LIMS的队列
     */
    @Bean
    public Queue qmsToLimsQueue() {
        McpPerformanceConfig.MessageQueue config = performanceConfig.getMessageQueue();
        return QueueBuilder.durable(QMS_TO_LIMS_QUEUE)
            .withArgument("x-dead-letter-exchange", GMP_MCP_EXCHANGE)
            .withArgument("x-dead-letter-routing-key", "dead.letter." + QMS_TO_LIMS_QUEUE)
            .withArgument("x-message-ttl", config.getMessageTtl())
            .withArgument("x-max-priority", config.getMaxPriority())
            .build();
    }
    
    /**
     * 创建QMS到ERP的队列
     */
    @Bean
    public Queue qmsToErpQueue() {
        McpPerformanceConfig.MessageQueue config = performanceConfig.getMessageQueue();
        return QueueBuilder.durable(QMS_TO_ERP_QUEUE)
            .withArgument("x-dead-letter-exchange", GMP_MCP_EXCHANGE)
            .withArgument("x-dead-letter-routing-key", "dead.letter." + QMS_TO_ERP_QUEUE)
            .withArgument("x-message-ttl", config.getMessageTtl())
            .withArgument("x-max-priority", config.getMaxPriority())
            .build();
    }
    
    /**
     * 创建QMS到培训系统的队列
     */
    @Bean
    public Queue qmsToTrainingQueue() {
        McpPerformanceConfig.MessageQueue config = performanceConfig.getMessageQueue();
        return QueueBuilder.durable(QMS_TO_TRAINING_QUEUE)
            .withArgument("x-dead-letter-exchange", GMP_MCP_EXCHANGE)
            .withArgument("x-dead-letter-routing-key", "dead.letter." + QMS_TO_TRAINING_QUEUE)
            .withArgument("x-message-ttl", config.getMessageTtl())
            .withArgument("x-max-priority", config.getMaxPriority())
            .build();
    }
    
    /**
     * 创建QMS到设备系统的队列
     */
    @Bean
    public Queue qmsToEquipmentQueue() {
        McpPerformanceConfig.MessageQueue config = performanceConfig.getMessageQueue();
        return QueueBuilder.durable(QMS_TO_EQUIPMENT_QUEUE)
            .withArgument("x-dead-letter-exchange", GMP_MCP_EXCHANGE)
            .withArgument("x-dead-letter-routing-key", "dead.letter." + QMS_TO_EQUIPMENT_QUEUE)
            .withArgument("x-message-ttl", config.getMessageTtl())
            .withArgument("x-max-priority", config.getMaxPriority())
            .build();
    }
    
    /**
     * 绑定所有队列到交换机
     */
    @Bean
    public Declarables bindings() {
        return new Declarables(
            BindingBuilder.bind(qmsToEdmsQueue()).to(gmpMcpExchange()).with(ROUTING_KEY_PREFIX + "to.edms"),
            BindingBuilder.bind(qmsToMesQueue()).to(gmpMcpExchange()).with(ROUTING_KEY_PREFIX + "to.mes"),
            BindingBuilder.bind(qmsToLimsQueue()).to(gmpMcpExchange()).with(ROUTING_KEY_PREFIX + "to.lims"),
            BindingBuilder.bind(qmsToErpQueue()).to(gmpMcpExchange()).with(ROUTING_KEY_PREFIX + "to.erp"),
            BindingBuilder.bind(qmsToTrainingQueue()).to(gmpMcpExchange()).with(ROUTING_KEY_PREFIX + "to.training"),
            BindingBuilder.bind(qmsToEquipmentQueue()).to(gmpMcpExchange()).with(ROUTING_KEY_PREFIX + "to.equipment")
        );
    }
    
    /**
     * 配置消息转换器
     */
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    
    /**
     * 配置RabbitTemplate
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        template.setConfirmCallback((correlationData, ack, cause) -> {
            if (!ack) {
                // 记录消息确认失败
                System.err.println("Message delivery failed: " + cause);
            }
        });
        template.setReturnsCallback(returned -> {
            // 记录被退回的消息
            System.err.println("Message returned: " + returned.getMessage() + ", reason: " + returned.getReplyText());
        });
        return template;
    }
    
    /**
     * 配置消息监听器容器工厂
     */
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter());
        factory.setConcurrentConsumers(performanceConfig.getMessageQueue().getConcurrentConsumers());
        factory.setMaxConcurrentConsumers(performanceConfig.getMessageQueue().getMaxConcurrentConsumers());
        factory.setPrefetchCount(performanceConfig.getMessageQueue().getPrefetchCount());
        factory.setDefaultRequeueRejected(false); // 失败消息不重新入队，使用死信队列
        return factory;
    }
}

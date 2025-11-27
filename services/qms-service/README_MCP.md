# MCP集成文档 - QMS系统

## 1. 概述

本文档详细介绍了QMS（质量管理系统）中MCP（Model Context Protocol）集成的实现、配置和使用方法。MCP协议是系统间通信的核心机制，通过统一的接口和安全认证确保各系统间数据交换的安全性和高效性。

## 2. MCP架构

### 2.1 核心组件

QMS系统的MCP集成包含以下核心组件：

- **安全认证模块**：负责MCP请求的身份验证和权限控制
- **服务集成接口**：提供与其他系统（EDMS、MES、LIMS、ERP等）的集成接口
- **性能优化模块**：包含缓存、断路器、异步执行等性能优化机制
- **监控系统**：收集和展示MCP调用的性能指标和健康状态
- **消息队列**：实现系统间的异步通信

### 2.2 数据流

```
外部系统 → MCP认证 → 服务接口 → 业务处理 → 响应/消息通知 → 外部系统
```

## 3. 安全认证

### 3.1 认证机制

QMS系统使用`McpAuthenticationToken`进行身份验证，支持以下认证方式：

- 令牌认证（推荐）
- 基本认证
- 系统证书认证

### 3.2 权限管理

权限通过基于角色的访问控制（RBAC）实现，主要权限包括：

| 权限 | 说明 | 适用系统 |
|------|------|----------|
| mcp:read | 读取QMS数据 | 所有集成系统 |
| mcp:write | 写入QMS数据 | EDMS, MES |
| mcp:admin | 管理权限 | 管理员系统 |

### 3.3 安全最佳实践

- 所有MCP通信必须使用HTTPS加密
- 令牌有效期最长为24小时
- 敏感操作需要二次验证
- 定期审计访问日志

## 4. 集成接口

### 4.1 RESTful API

QMS系统提供以下MCP RESTful接口：

#### 4.1.1 文档管理接口

```
GET /api/mcp/documents/{documentId}
POST /api/mcp/documents
PUT /api/mcp/documents/{documentId}
DELETE /api/mcp/documents/{documentId}
```

#### 4.1.2 批次管理接口

```
GET /api/mcp/batches/{batchId}
POST /api/mcp/batches
GET /api/mcp/batches/search
```

#### 4.1.3 检测数据接口

```
GET /api/mcp/inspections/{inspectionId}
POST /api/mcp/inspections
```

### 4.2 数据传输对象（DTOs）

系统使用以下DTO进行数据交换：

- `DocumentDTO`：文档信息
- `BatchInfoDTO`：批次信息
- `ProcessParameterDTO`：工艺参数
- `MaintenanceRecordDTO`：维护记录

## 5. 消息队列集成

### 5.1 队列配置

QMS系统使用RabbitMQ作为消息中间件，配置了以下队列：

| 队列名称 | 用途 | 目标系统 |
|---------|------|----------|
| qms.edms.queue | 与EDMS系统通信 | EDMS |
| qms.mes.queue | 与MES系统通信 | MES |
| qms.lims.queue | 与LIMS系统通信 | LIMS |
| qms.erp.queue | 与ERP系统通信 | ERP |
| qms.training.queue | 与培训系统通信 | Training |
| qms.equipment.queue | 与设备系统通信 | Equipment |
| qms.mcp.dead.letter.queue | 死信队列 | 内部处理 |

### 5.2 消息格式

消息使用JSON格式，包含以下字段：

```json
{
  "messageId": "唯一消息ID",
  "timestamp": "发送时间",
  "sourceSystem": "发送系统",
  "operation": "操作类型",
  "payload": {"业务数据"},
  "retryCount": 重试次数
}
```

### 5.3 消息发送示例

```java
// 发送消息到EDMS系统
Map<String, Object> payload = new HashMap<>();
payload.put("documentId", "DOC-001");
payload.put("status", "APPROVED");

String messageId = mcpMessageService.sendToEdms(payload, "DOCUMENT_STATUS_UPDATE");
```

## 6. 性能优化

### 6.1 缓存策略

系统实现了多级缓存机制：

- 本地内存缓存：适用于频繁访问的数据
- Redis分布式缓存：适用于跨实例共享的数据
- 结果缓存：缓存MCP调用结果，减少重复计算

缓存配置可在`McpPerformanceConfig`中调整。

### 6.2 断路器模式

使用Resilience4J实现断路器模式，配置参数：

- 失败阈值：50%
- 滑动窗口大小：100次调用
- 熔断时间：30秒
- 半开状态调用数：10次

### 6.3 异步执行

系统提供异步执行服务`McpAsyncExecutionService`，支持：

- 批量异步调用
- 并行处理
- 带超时的异步执行
- 带重试机制的异步执行

## 7. 监控与日志

### 7.1 性能指标

系统收集以下MCP性能指标：

- 调用次数（总次数、成功、失败）
- 平均响应时间
- 最大/最小响应时间
- 错误率
- 缓存命中率

### 7.2 监控API

访问以下API获取监控数据：

```
GET /api/mcp/metrics       # 系统整体性能指标
GET /api/mcp/metrics/tools/{toolName}  # 特定工具性能指标
GET /api/mcp/metrics/resources/{resourceName}  # 特定资源性能指标
GET /api/mcp/health        # 系统健康状态
```

### 7.3 日志格式

MCP日志包含以下关键字段：

- `mcp_trace_id`：追踪ID
- `source_system`：来源系统
- `operation`：操作类型
- `duration_ms`：执行时间
- `status`：执行状态

## 8. 故障排除

### 8.1 常见问题

| 问题 | 可能原因 | 解决方案 |
|------|---------|----------|
| 认证失败 | 令牌过期或无效 | 重新获取令牌 |
| 权限不足 | 缺少必要权限 | 检查权限配置 |
| 断路器触发 | 目标系统异常 | 检查目标系统状态，等待断路器重置 |
| 消息发送失败 | 队列连接问题 | 检查RabbitMQ连接 |
| 性能下降 | 缓存配置不当 | 调整缓存策略，增加资源 |

### 8.2 诊断工具

- 监控控制台：查看实时性能指标
- 日志分析：分析系统日志
- 健康检查API：检查系统状态

## 9. 开发与集成指南

### 9.1 集成步骤

1. 在目标系统中实现MCP客户端
2. 配置认证信息
3. 实现业务逻辑
4. 配置消息处理
5. 进行集成测试

### 9.2 代码示例

#### 服务集成示例

```java
// 创建MCP客户端
McpClient client = new McpClientBuilder()
    .withBaseUrl("http://qms-system/api/mcp")
    .withAuthToken("your-auth-token")
    .build();

// 调用文档接口
DocumentDTO document = client.getDocument("DOC-001");

// 处理响应
if (document != null) {
    // 业务逻辑
}
```

#### 消息处理示例

```java
// 注册消息处理器
mcpMessageListener.registerMessageHandler("DOCUMENT_APPROVED", message -> {
    // 处理文档批准消息
    Map<String, Object> payload = (Map<String, Object>) message.get("payload");
    String documentId = payload.get("documentId").toString();
    
    // 执行业务逻辑
    documentService.updateDocumentStatus(documentId, "APPROVED");
});
```

## 10. 版本控制

### 10.1 当前版本

- 主要版本：1.x
- 最新版本：1.2.0

### 10.2 版本兼容性

- 1.x版本向下兼容0.x版本
- 主要版本升级（如1.x到2.x）可能不兼容

### 10.3 升级指南

版本升级时请参考升级文档，主要包括：

1. 备份现有配置
2. 检查兼容性说明
3. 升级依赖包
4. 更新API调用
5. 测试集成功能

## 11. 联系与支持

如有问题或需要支持，请联系：

- 技术支持团队：support@gmp-system.com
- 集成专家：integration@gmp-system.com
- 紧急支持：emergency@gmp-system.com

## 12. 附录

### 12.1 错误代码

| 代码 | 描述 | 建议操作 |
|------|------|----------|
| 4001 | 认证失败 | 检查认证信息 |
| 4002 | 权限不足 | 申请必要权限 |
| 4003 | 参数错误 | 检查参数格式 |
| 5001 | 内部错误 | 联系技术支持 |
| 5002 | 外部系统错误 | 检查外部系统状态 |
| 5003 | 超时错误 | 增加超时时间或重试 |

### 12.2 配置参数

主要配置参数可在`application.yml`中设置：

```yaml
mcp:
  security:
    token-validity: 86400  # 令牌有效期（秒）
  performance:
    cache-ttl: 3600        # 缓存有效期（秒）
    max-concurrent-calls: 100  # 最大并发调用数
  messaging:
    retry-count: 3         # 消息重试次数
    retry-interval: 1000   # 重试间隔（毫秒）
```

---

本文档最后更新时间：2023年12月15日
版本：1.0

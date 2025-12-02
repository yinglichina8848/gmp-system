# MES Service 详细设计文档

## 1. 概述

MES（Manufacturing Execution System）Service 是 GMP 系统中的核心服务之一，负责生产执行管理、批次管理、设备监控和中药炮制工艺管理等功能。本文档详细描述了 MES Service 的设计架构、核心功能、数据模型和 API 接口。

## 2. 系统架构

### 2.1 技术栈
- Spring Boot 3.2.5
- Java 17
- Spring Data JPA
- PostgreSQL
- Redis
- RabbitMQ

### 2.2 模块划分

| 模块 | 职责 | 主要组件 |
|------|------|----------|
| 生产订单管理 | 生产订单的创建、执行和跟踪 | ProductionOrderController, ProductionOrderService |
| 生产批次管理 | 生产批次的创建、执行和跟踪 | ProductionBatchController, ProductionBatchService |
| 批次操作管理 | 批次操作记录管理 | BatchOperationController, BatchOperationService |
| 设备监控管理 | 设备状态监控和记录 | EquipmentMonitorController, EquipmentMonitorService |
| 中药炮制工艺管理 | 中药炮制工艺管理 | TcmProcessingProcedureController, TcmProcessingProcedureService |

### 2.3 核心类图

```
+-------------------+        +-------------------+
| ProductionOrder   |        | ProductionBatch   |
+-------------------+        +-------------------+
| - id              |        | - id              |
| - orderCode       |        | - batchCode       |
| - productName     |        | - productionOrderId |<------| - id              |
| - productCode     |        | - batchStatus     |        | - equipmentId     |
| - plannedQuantity |        | - startDate       |        | - monitorTime     |
| - actualQuantity  |        | - endDate         |        | - status          |
| - orderStatus     |        | - createdAt       |        | - temperature     |
| - createdAt       |        | - updatedAt       |        | - pressure        |
| - updatedAt       |        +-------------------+        | - humidity        |
+-------------------+                ^                   | - createdAt       |
        ^                           |                   | - updatedAt       |
        |                           |                   +-------------------+
+-------------------+        +-------------------+
| BatchOperation    |        | TcmProcessingProcedure |
+-------------------+        +-------------------+
| - id              |        | - id              |
| - batchId         |<-------| - procedureName   |
| - operationType   |        | - procedureCode   |
| - operationTime   |        | - processingSteps |
| - operator        |        | - temperatureRange |
| - parameters      |        | - duration        |
| - results         |        | - createdAt       |
| - createdAt       |        | - updatedAt       |
| - updatedAt       |        +-------------------+
+-------------------+
```

## 3. 核心功能设计

### 3.1 生产订单管理

#### 3.1.1 功能描述
- 生产订单的创建、查询、更新和删除
- 生产订单状态管理
- 生产订单执行跟踪
- 生产订单统计分析

#### 3.1.2 核心 API

| API 路径 | 方法 | 功能描述 |
|----------|------|----------|
| /api/mes/production-orders | POST | 创建生产订单 |
| /api/mes/production-orders | GET | 查询生产订单列表 |
| /api/mes/production-orders/{id} | GET | 查询生产订单详情 |
| /api/mes/production-orders/{id} | PUT | 更新生产订单 |
| /api/mes/production-orders/{id} | DELETE | 删除生产订单 |
| /api/mes/production-orders/{id}/status | PUT | 更新生产订单状态 |

### 3.2 生产批次管理

#### 3.2.1 功能描述
- 生产批次的创建、查询、更新和删除
- 生产批次状态管理
- 生产批次执行跟踪
- 生产批次与生产订单关联

#### 3.2.2 核心 API

| API 路径 | 方法 | 功能描述 |
|----------|------|----------|
| /api/mes/production-batches | POST | 创建生产批次 |
| /api/mes/production-batches | GET | 查询生产批次列表 |
| /api/mes/production-batches/{id} | GET | 查询生产批次详情 |
| /api/mes/production-batches/{id} | PUT | 更新生产批次 |
| /api/mes/production-batches/{id} | DELETE | 删除生产批次 |
| /api/mes/production-batches/{id}/status | PUT | 更新生产批次状态 |
| /api/mes/production-batches/order/{orderId} | GET | 查询订单关联的批次 |

### 3.3 批次操作管理

#### 3.3.1 功能描述
- 批次操作记录的创建、查询、更新和删除
- 批次操作参数记录
- 批次操作结果记录
- 批次操作与生产批次关联

#### 3.3.2 核心 API

| API 路径 | 方法 | 功能描述 |
|----------|------|----------|
| /api/mes/batch-operations | POST | 创建批次操作记录 |
| /api/mes/batch-operations | GET | 查询批次操作记录列表 |
| /api/mes/batch-operations/{id} | GET | 查询批次操作记录详情 |
| /api/mes/batch-operations/{id} | PUT | 更新批次操作记录 |
| /api/mes/batch-operations/{id} | DELETE | 删除批次操作记录 |
| /api/mes/batch-operations/batch/{batchId} | GET | 查询批次关联的操作记录 |

### 3.4 设备监控管理

#### 3.4.1 功能描述
- 设备状态实时监控
- 设备状态历史记录管理
- 设备参数（温度、压力、湿度等）监控
- 设备异常报警

#### 3.4.2 核心 API

| API 路径 | 方法 | 功能描述 |
|----------|------|----------|
| /api/mes/equipment-monitors | POST | 创建设备监控记录 |
| /api/mes/equipment-monitors | GET | 查询设备监控记录列表 |
| /api/mes/equipment-monitors/{id} | GET | 查询设备监控记录详情 |
| /api/mes/equipment-monitors/equipment/{equipmentId} | GET | 查询设备关联的监控记录 |
| /api/mes/equipment-monitors/equipment/{equipmentId}/latest | GET | 查询设备最新监控记录 |

### 3.5 中药炮制工艺管理

#### 3.5.1 功能描述
- 中药炮制工艺的创建、查询、更新和删除
- 中药炮制工艺步骤管理
- 中药炮制工艺参数管理
- 中药炮制工艺与生产批次关联

#### 3.5.2 核心 API

| API 路径 | 方法 | 功能描述 |
|----------|------|----------|
| /api/mes/tcm-processing-procedures | POST | 创建中药炮制工艺 |
| /api/mes/tcm-processing-procedures | GET | 查询中药炮制工艺列表 |
| /api/mes/tcm-processing-procedures/{id} | GET | 查询中药炮制工艺详情 |
| /api/mes/tcm-processing-procedures/{id} | PUT | 更新中药炮制工艺 |
| /api/mes/tcm-processing-procedures/{id} | DELETE | 删除中药炮制工艺 |

## 4. 数据模型设计

### 4.1 ProductionOrder（生产订单）

| 字段名 | 数据类型 | 约束 | 描述 |
|--------|----------|------|------|
| id | Long | PRIMARY KEY | 生产订单ID |
| orderCode | String | UNIQUE | 订单编码 |
| productName | String | NOT NULL | 产品名称 |
| productCode | String | NOT NULL | 产品编码 |
| plannedQuantity | BigDecimal | NOT NULL | 计划数量 |
| actualQuantity | BigDecimal | | 实际数量 |
| orderStatus | String | NOT NULL | 订单状态（PENDING, IN_PROGRESS, COMPLETED, CANCELLED） |
| createdAt | LocalDateTime | NOT NULL | 创建时间 |
| updatedAt | LocalDateTime | NOT NULL | 更新时间 |

### 4.2 ProductionBatch（生产批次）

| 字段名 | 数据类型 | 约束 | 描述 |
|--------|----------|------|------|
| id | Long | PRIMARY KEY | 生产批次ID |
| batchCode | String | UNIQUE | 批次编码 |
| productionOrderId | Long | FOREIGN KEY | 生产订单ID |
| batchStatus | String | NOT NULL | 批次状态（PENDING, IN_PROGRESS, COMPLETED, CANCELLED） |
| startDate | LocalDateTime | | 开始时间 |
| endDate | LocalDateTime | | 结束时间 |
| createdAt | LocalDateTime | NOT NULL | 创建时间 |
| updatedAt | LocalDateTime | NOT NULL | 更新时间 |

### 4.3 BatchOperation（批次操作）

| 字段名 | 数据类型 | 约束 | 描述 |
|--------|----------|------|------|
| id | Long | PRIMARY KEY | 批次操作ID |
| batchId | Long | FOREIGN KEY | 生产批次ID |
| operationType | String | NOT NULL | 操作类型 |
| operationTime | LocalDateTime | NOT NULL | 操作时间 |
| operator | String | NOT NULL | 操作人 |
| parameters | JSONB | | 操作参数 |
| results | JSONB | | 操作结果 |
| remarks | String | | 备注 |
| createdAt | LocalDateTime | NOT NULL | 创建时间 |
| updatedAt | LocalDateTime | NOT NULL | 更新时间 |

### 4.4 EquipmentMonitor（设备监控）

| 字段名 | 数据类型 | 约束 | 描述 |
|--------|----------|------|------|
| id | Long | PRIMARY KEY | 设备监控ID |
| equipmentId | Long | FOREIGN KEY | 设备ID |
| monitorTime | LocalDateTime | NOT NULL | 监控时间 |
| status | String | NOT NULL | 设备状态（RUNNING, IDLE, MAINTENANCE, FAULT） |
| temperature | BigDecimal | | 温度 |
| pressure | BigDecimal | | 压力 |
| humidity | BigDecimal | | 湿度 |
| vibration | BigDecimal | | 振动 |
| createdAt | LocalDateTime | NOT NULL | 创建时间 |
| updatedAt | LocalDateTime | NOT NULL | 更新时间 |

### 4.5 EquipmentStatusRecord（设备状态记录）

| 字段名 | 数据类型 | 约束 | 描述 |
|--------|----------|------|------|
| id | Long | PRIMARY KEY | 设备状态记录ID |
| equipmentId | Long | FOREIGN KEY | 设备ID |
| status | String | NOT NULL | 设备状态 |
| startTime | LocalDateTime | NOT NULL | 开始时间 |
| endTime | LocalDateTime | | 结束时间 |
| createdAt | LocalDateTime | NOT NULL | 创建时间 |
| updatedAt | LocalDateTime | NOT NULL | 更新时间 |

### 4.6 TcmProcessingProcedure（中药炮制工艺）

| 字段名 | 数据类型 | 约束 | 描述 |
|--------|----------|------|------|
| id | Long | PRIMARY KEY | 中药炮制工艺ID |
| procedureName | String | NOT NULL | 工艺名称 |
| procedureCode | String | UNIQUE | 工艺编码 |
| processingSteps | JSONB | NOT NULL | 工艺步骤 |
| temperatureRange | String | | 温度范围 |
| pressureRange | String | | 压力范围 |
| duration | Integer | | 持续时间（分钟） |
| humidityRange | String | | 湿度范围 |
| createdAt | LocalDateTime | NOT NULL | 创建时间 |
| updatedAt | LocalDateTime | NOT NULL | 更新时间 |

## 5. API 接口设计

### 5.1 生产订单管理 API

#### 5.1.1 创建生产订单
- **URL**: `/api/mes/production-orders`
- **方法**: `POST`
- **请求体**: `ProductionOrderDTO`
- **响应**: `ResponseDTO<ProductionOrderDTO>`

#### 5.1.2 查询生产订单列表
- **URL**: `/api/mes/production-orders`
- **方法**: `GET`
- **请求参数**: 分页参数、查询条件
- **响应**: `ResponseDTO<PageResultDTO<ProductionOrderDTO>>`

#### 5.1.3 查询生产订单详情
- **URL**: `/api/mes/production-orders/{id}`
- **方法**: `GET`
- **响应**: `ResponseDTO<ProductionOrderDTO>`

#### 5.1.4 更新生产订单
- **URL**: `/api/mes/production-orders/{id}`
- **方法**: `PUT`
- **请求体**: `ProductionOrderDTO`
- **响应**: `ResponseDTO<ProductionOrderDTO>`

#### 5.1.5 删除生产订单
- **URL**: `/api/mes/production-orders/{id}`
- **方法**: `DELETE`
- **响应**: `ResponseDTO<Void>`

#### 5.1.6 更新生产订单状态
- **URL**: `/api/mes/production-orders/{id}/status`
- **方法**: `PUT`
- **请求体**: `StatusUpdateDTO`
- **响应**: `ResponseDTO<ProductionOrderDTO>`

### 5.2 生产批次管理 API

#### 5.2.1 创建生产批次
- **URL**: `/api/mes/production-batches`
- **方法**: `POST`
- **请求体**: `ProductionBatchDTO`
- **响应**: `ResponseDTO<ProductionBatchDTO>`

#### 5.2.2 查询生产批次列表
- **URL**: `/api/mes/production-batches`
- **方法**: `GET`
- **请求参数**: 分页参数、查询条件
- **响应**: `ResponseDTO<PageResultDTO<ProductionBatchDTO>>`

#### 5.2.3 查询生产批次详情
- **URL**: `/api/mes/production-batches/{id}`
- **方法**: `GET`
- **响应**: `ResponseDTO<ProductionBatchDTO>`

#### 5.2.4 更新生产批次
- **URL**: `/api/mes/production-batches/{id}`
- **方法**: `PUT`
- **请求体**: `ProductionBatchDTO`
- **响应**: `ResponseDTO<ProductionBatchDTO>`

#### 5.2.5 删除生产批次
- **URL**: `/api/mes/production-batches/{id}`
- **方法**: `DELETE`
- **响应**: `ResponseDTO<Void>`

#### 5.2.6 更新生产批次状态
- **URL**: `/api/mes/production-batches/{id}/status`
- **方法**: `PUT`
- **请求体**: `StatusUpdateDTO`
- **响应**: `ResponseDTO<ProductionBatchDTO>`

#### 5.2.7 查询订单关联的批次
- **URL**: `/api/mes/production-batches/order/{orderId}`
- **方法**: `GET`
- **请求参数**: 分页参数
- **响应**: `ResponseDTO<PageResultDTO<ProductionBatchDTO>>`

### 5.3 批次操作管理 API

#### 5.3.1 创建批次操作记录
- **URL**: `/api/mes/batch-operations`
- **方法**: `POST`
- **请求体**: `BatchOperationDTO`
- **响应**: `ResponseDTO<BatchOperationDTO>`

#### 5.3.2 查询批次操作记录列表
- **URL**: `/api/mes/batch-operations`
- **方法**: `GET`
- **请求参数**: 分页参数、查询条件
- **响应**: `ResponseDTO<PageResultDTO<BatchOperationDTO>>`

#### 5.3.3 查询批次操作记录详情
- **URL**: `/api/mes/batch-operations/{id}`
- **方法**: `GET`
- **响应**: `ResponseDTO<BatchOperationDTO>`

#### 5.3.4 更新批次操作记录
- **URL**: `/api/mes/batch-operations/{id}`
- **方法**: `PUT`
- **请求体**: `BatchOperationDTO`
- **响应**: `ResponseDTO<BatchOperationDTO>`

#### 5.3.5 删除批次操作记录
- **URL**: `/api/mes/batch-operations/{id}`
- **方法**: `DELETE`
- **响应**: `ResponseDTO<Void>`

#### 5.3.6 查询批次关联的操作记录
- **URL**: `/api/mes/batch-operations/batch/{batchId}`
- **方法**: `GET`
- **请求参数**: 分页参数
- **响应**: `ResponseDTO<PageResultDTO<BatchOperationDTO>>`

### 5.4 设备监控管理 API

#### 5.4.1 创建设备监控记录
- **URL**: `/api/mes/equipment-monitors`
- **方法**: `POST`
- **请求体**: `EquipmentMonitorDTO`
- **响应**: `ResponseDTO<EquipmentMonitorDTO>`

#### 5.4.2 查询设备监控记录列表
- **URL**: `/api/mes/equipment-monitors`
- **方法**: `GET`
- **请求参数**: 分页参数、查询条件
- **响应**: `ResponseDTO<PageResultDTO<EquipmentMonitorDTO>>`

#### 5.4.3 查询设备监控记录详情
- **URL**: `/api/mes/equipment-monitors/{id}`
- **方法**: `GET`
- **响应**: `ResponseDTO<EquipmentMonitorDTO>`

#### 5.4.4 查询设备关联的监控记录
- **URL**: `/api/mes/equipment-monitors/equipment/{equipmentId}`
- **方法**: `GET`
- **请求参数**: 分页参数
- **响应**: `ResponseDTO<PageResultDTO<EquipmentMonitorDTO>>`

#### 5.4.5 查询设备最新监控记录
- **URL**: `/api/mes/equipment-monitors/equipment/{equipmentId}/latest`
- **方法**: `GET`
- **响应**: `ResponseDTO<EquipmentMonitorDTO>`

### 5.5 中药炮制工艺管理 API

#### 5.5.1 创建中药炮制工艺
- **URL**: `/api/mes/tcm-processing-procedures`
- **方法**: `POST`
- **请求体**: `TcmProcessingProcedureDTO`
- **响应**: `ResponseDTO<TcmProcessingProcedureDTO>`

#### 5.5.2 查询中药炮制工艺列表
- **URL**: `/api/mes/tcm-processing-procedures`
- **方法**: `GET`
- **请求参数**: 分页参数、查询条件
- **响应**: `ResponseDTO<PageResultDTO<TcmProcessingProcedureDTO>>`

#### 5.5.3 查询中药炮制工艺详情
- **URL**: `/api/mes/tcm-processing-procedures/{id}`
- **方法**: `GET`
- **响应**: `ResponseDTO<TcmProcessingProcedureDTO>`

#### 5.5.4 更新中药炮制工艺
- **URL**: `/api/mes/tcm-processing-procedures/{id}`
- **方法**: `PUT`
- **请求体**: `TcmProcessingProcedureDTO`
- **响应**: `ResponseDTO<TcmProcessingProcedureDTO>`

#### 5.5.5 删除中药炮制工艺
- **URL**: `/api/mes/tcm-processing-procedures/{id}`
- **方法**: `DELETE`
- **响应**: `ResponseDTO<Void>`

## 6. 安全设计

### 6.1 认证与授权
- 使用 JWT 令牌进行认证
- 基于角色的访问控制（RBAC）
- 细粒度的权限控制

### 6.2 数据安全
- 敏感数据加密存储
- 数据访问日志记录
- 定期数据备份

## 7. 性能优化

### 7.1 缓存策略
- 使用 Redis 缓存生产订单和批次信息
- 缓存设备监控数据
- 缓存查询结果

### 7.2 数据库优化
- 合理设计索引
- 优化查询语句
- 使用分页查询
- 批量插入和更新

## 8. 监控与日志

### 8.1 系统监控
- 使用 Prometheus 监控系统性能
- 使用 Grafana 可视化监控数据

### 8.2 日志管理
- 使用 ELK Stack 集中管理日志
- 记录详细的操作日志
- 记录错误日志和异常信息

## 9. 测试策略

### 9.1 单元测试
- 测试核心业务逻辑
- 测试数据访问层
- 测试服务层

### 9.2 集成测试
- 测试 API 接口
- 测试服务间通信
- 测试数据库交互

### 9.3 系统测试
- 测试完整的业务流程
- 测试系统性能
- 测试系统安全性

## 10. 部署与运维

### 10.1 部署方式
- Docker 容器化部署
- Kubernetes 集群部署
- CI/CD 自动化部署

### 10.2 运维管理
- 自动化运维脚本
- 故障应急预案
- 定期系统维护

## 11. 版本控制

| 版本 | 日期 | 描述 | 作者 |
|------|------|------|------|
| 1.0 | 2025-11-28 | 初始版本 | GMP 开发团队 |
| 1.1 | YYYY-MM-DD | 更新内容 | GMP 开发团队 |

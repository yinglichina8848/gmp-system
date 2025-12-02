# Equipment Service 详细设计文档

## 1. 概述

Equipment Service 是 GMP 系统中的核心服务之一，负责设备管理、校准记录、维护计划和维护记录等功能。本文档详细描述了 Equipment Service 的设计架构、核心功能、数据模型和 API 接口。

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
| 设备管理 | 设备基本信息管理 | EquipmentController, EquipmentService |
| 设备类型管理 | 设备类型维护 | EquipmentTypeController, EquipmentTypeService |
| 校准记录管理 | 设备校准记录管理 | CalibrationRecordController, CalibrationRecordService |
| 维护计划管理 | 设备维护计划管理 | MaintenancePlanController, MaintenancePlanService |
| 维护记录管理 | 设备维护记录管理 | MaintenanceRecordController, MaintenanceRecordService |

### 2.3 核心类图

```
+-------------------+        +-------------------+
| Equipment         |        | EquipmentType     |
+-------------------+        +-------------------+
| - id              |        | - id              |
| - equipmentCode   |        | - typeName        |
| - equipmentName   |        | - description     |
| - equipmentTypeId |<------>| - createdAt       |
| - status          |        | - updatedAt       |
| - purchaseDate    |        +-------------------+
| - installationDate|                ^
| - location        |                |
| - manufacturer    |                |
| - model           |                |
| - serialNumber    |                |
| - createdAt       |                |
| - updatedAt       |                |
+-------------------+                |
        ^                           |
        |                           |
+-------------------+        +-------------------+
| CalibrationRecord |        | MaintenancePlan   |
+-------------------+        +-------------------+
| - id              |        | - id              |
| - equipmentId     |<-------| - equipmentId     |
| - calibrationDate |        | - planName        |
| - nextCalibrationDate|     | - maintenanceType |
| - calibrationResult|     | - cycleType       |
| - calibratedBy    |     | - cycleValue      |
| - createdAt       |     | - startDate       |
| - updatedAt       |     | - status          |
+-------------------+     | - createdAt       |
        ^                 | - updatedAt       |
        |                 +-------------------+
        |                         ^
        |                         |
+-------------------+        +-------------------+
| MaintenanceRecord |        | MaintenanceType   |
+-------------------+        +-------------------+
| - id              |        +-------------------+
| - equipmentId     |
| - maintenancePlanId |
| - maintenanceDate |
| - maintenanceType |
| - maintenanceContent |
| - performedBy     |
| - createdAt       |
| - updatedAt       |
+-------------------+
```

## 3. 核心功能设计

### 3.1 设备管理

#### 3.1.1 功能描述
- 设备基本信息的增删改查
- 设备状态管理
- 设备查询与分页

#### 3.1.2 核心 API

| API 路径 | 方法 | 功能描述 |
|----------|------|----------|
| /api/equipment | POST | 创建设备 |
| /api/equipment | GET | 查询设备列表 |
| /api/equipment/{id} | GET | 查询设备详情 |
| /api/equipment/{id} | PUT | 更新设备信息 |
| /api/equipment/{id} | DELETE | 删除设备 |
| /api/equipment/{id}/status | PUT | 更新设备状态 |

### 3.2 设备类型管理

#### 3.2.1 功能描述
- 设备类型的增删改查
- 设备类型分类管理

#### 3.2.2 核心 API

| API 路径 | 方法 | 功能描述 |
|----------|------|----------|
| /api/equipment-type | POST | 创建设备类型 |
| /api/equipment-type | GET | 查询设备类型列表 |
| /api/equipment-type/{id} | GET | 查询设备类型详情 |
| /api/equipment-type/{id} | PUT | 更新设备类型 |
| /api/equipment-type/{id} | DELETE | 删除设备类型 |

### 3.3 校准记录管理

#### 3.3.1 功能描述
- 校准记录的增删改查
- 校准计划提醒
- 校准结果管理

#### 3.3.2 核心 API

| API 路径 | 方法 | 功能描述 |
|----------|------|----------|
| /api/calibration-record | POST | 创建校准记录 |
| /api/calibration-record | GET | 查询校准记录列表 |
| /api/calibration-record/{id} | GET | 查询校准记录详情 |
| /api/calibration-record/{id} | PUT | 更新校准记录 |
| /api/calibration-record/{id} | DELETE | 删除校准记录 |
| /api/calibration-record/equipment/{equipmentId} | GET | 查询设备校准记录 |

### 3.4 维护计划管理

#### 3.4.1 功能描述
- 维护计划的增删改查
- 维护计划执行提醒
- 维护计划状态管理

#### 3.4.2 核心 API

| API 路径 | 方法 | 功能描述 |
|----------|------|----------|
| /api/maintenance-plan | POST | 创建维护计划 |
| /api/maintenance-plan | GET | 查询维护计划列表 |
| /api/maintenance-plan/{id} | GET | 查询维护计划详情 |
| /api/maintenance-plan/{id} | PUT | 更新维护计划 |
| /api/maintenance-plan/{id} | DELETE | 删除维护计划 |
| /api/maintenance-plan/equipment/{equipmentId} | GET | 查询设备维护计划 |

### 3.5 维护记录管理

#### 3.5.1 功能描述
- 维护记录的增删改查
- 维护记录统计分析
- 维护记录与维护计划关联

#### 3.5.2 核心 API

| API 路径 | 方法 | 功能描述 |
|----------|------|----------|
| /api/maintenance-record | POST | 创建维护记录 |
| /api/maintenance-record | GET | 查询维护记录列表 |
| /api/maintenance-record/{id} | GET | 查询维护记录详情 |
| /api/maintenance-record/{id} | PUT | 更新维护记录 |
| /api/maintenance-record/{id} | DELETE | 删除维护记录 |
| /api/maintenance-record/equipment/{equipmentId} | GET | 查询设备维护记录 |

## 4. 数据模型设计

### 4.1 Equipment（设备）

| 字段名 | 数据类型 | 约束 | 描述 |
|--------|----------|------|------|
| id | Long | PRIMARY KEY | 设备ID |
| equipmentCode | String | UNIQUE | 设备编码 |
| equipmentName | String | NOT NULL | 设备名称 |
| equipmentTypeId | Long | FOREIGN KEY | 设备类型ID |
| status | String | NOT NULL | 设备状态（AVAILABLE, IN_USE, MAINTENANCE, CALIBRATION, OUT_OF_SERVICE） |
| purchaseDate | LocalDate | | 购买日期 |
| installationDate | LocalDate | | 安装日期 |
| location | String | | 设备位置 |
| manufacturer | String | | 制造商 |
| model | String | | 型号 |
| serialNumber | String | | 序列号 |
| createdAt | LocalDateTime | NOT NULL | 创建时间 |
| updatedAt | LocalDateTime | NOT NULL | 更新时间 |

### 4.2 EquipmentType（设备类型）

| 字段名 | 数据类型 | 约束 | 描述 |
|--------|----------|------|------|
| id | Long | PRIMARY KEY | 设备类型ID |
| typeName | String | NOT NULL | 设备类型名称 |
| description | String | | 设备类型描述 |
| createdAt | LocalDateTime | NOT NULL | 创建时间 |
| updatedAt | LocalDateTime | NOT NULL | 更新时间 |

### 4.3 CalibrationRecord（校准记录）

| 字段名 | 数据类型 | 约束 | 描述 |
|--------|----------|------|------|
| id | Long | PRIMARY KEY | 校准记录ID |
| equipmentId | Long | FOREIGN KEY | 设备ID |
| calibrationDate | LocalDate | NOT NULL | 校准日期 |
| nextCalibrationDate | LocalDate | NOT NULL | 下次校准日期 |
| calibrationResult | String | NOT NULL | 校准结果（PASSED, FAILED, PARTIAL_PASS） |
| calibratedBy | String | NOT NULL | 校准人员 |
| remarks | String | | 备注 |
| createdAt | LocalDateTime | NOT NULL | 创建时间 |
| updatedAt | LocalDateTime | NOT NULL | 更新时间 |

### 4.4 MaintenancePlan（维护计划）

| 字段名 | 数据类型 | 约束 | 描述 |
|--------|----------|------|------|
| id | Long | PRIMARY KEY | 维护计划ID |
| equipmentId | Long | FOREIGN KEY | 设备ID |
| planName | String | NOT NULL | 计划名称 |
| maintenanceType | String | NOT NULL | 维护类型（PREVENTIVE, CORRECTIVE, PREDICTIVE） |
| cycleType | String | NOT NULL | 周期类型（DAILY, WEEKLY, MONTHLY, QUARTERLY, YEARLY） |
| cycleValue | Integer | NOT NULL | 周期值 |
| startDate | LocalDate | NOT NULL | 开始日期 |
| endDate | LocalDate | | 结束日期 |
| status | String | NOT NULL | 状态（ACTIVE, INACTIVE, COMPLETED） |
| createdAt | LocalDateTime | NOT NULL | 创建时间 |
| updatedAt | LocalDateTime | NOT NULL | 更新时间 |

### 4.5 MaintenanceRecord（维护记录）

| 字段名 | 数据类型 | 约束 | 描述 |
|--------|----------|------|------|
| id | Long | PRIMARY KEY | 维护记录ID |
| equipmentId | Long | FOREIGN KEY | 设备ID |
| maintenancePlanId | Long | FOREIGN KEY | 维护计划ID |
| maintenanceDate | LocalDate | NOT NULL | 维护日期 |
| maintenanceType | String | NOT NULL | 维护类型 |
| maintenanceContent | String | NOT NULL | 维护内容 |
| performedBy | String | NOT NULL | 执行人员 |
| result | String | NOT NULL | 维护结果（SUCCESS, FAILURE, PARTIAL_SUCCESS） |
| remarks | String | | 备注 |
| createdAt | LocalDateTime | NOT NULL | 创建时间 |
| updatedAt | LocalDateTime | NOT NULL | 更新时间 |

## 5. API 接口设计

### 5.1 设备管理 API

#### 5.1.1 创建设备
- **URL**: `/api/equipment`
- **方法**: `POST`
- **请求体**: `EquipmentDTO`
- **响应**: `ResponseDTO<EquipmentDTO>`

#### 5.1.2 查询设备列表
- **URL**: `/api/equipment`
- **方法**: `GET`
- **请求参数**: 分页参数、查询条件
- **响应**: `ResponseDTO<PageResultDTO<EquipmentDTO>>`

#### 5.1.3 查询设备详情
- **URL**: `/api/equipment/{id}`
- **方法**: `GET`
- **响应**: `ResponseDTO<EquipmentDTO>`

#### 5.1.4 更新设备
- **URL**: `/api/equipment/{id}`
- **方法**: `PUT`
- **请求体**: `EquipmentDTO`
- **响应**: `ResponseDTO<EquipmentDTO>`

#### 5.1.5 删除设备
- **URL**: `/api/equipment/{id}`
- **方法**: `DELETE`
- **响应**: `ResponseDTO<Void>`

#### 5.1.6 更新设备状态
- **URL**: `/api/equipment/{id}/status`
- **方法**: `PUT`
- **请求体**: `EquipmentStatusUpdateDTO`
- **响应**: `ResponseDTO<EquipmentDTO>`

### 5.2 设备类型管理 API

#### 5.2.1 创建设备类型
- **URL**: `/api/equipment-type`
- **方法**: `POST`
- **请求体**: `EquipmentTypeDTO`
- **响应**: `ResponseDTO<EquipmentTypeDTO>`

#### 5.2.2 查询设备类型列表
- **URL**: `/api/equipment-type`
- **方法**: `GET`
- **响应**: `ResponseDTO<List<EquipmentTypeDTO>>`

#### 5.2.3 查询设备类型详情
- **URL**: `/api/equipment-type/{id}`
- **方法**: `GET`
- **响应**: `ResponseDTO<EquipmentTypeDTO>`

#### 5.2.4 更新设备类型
- **URL**: `/api/equipment-type/{id}`
- **方法**: `PUT`
- **请求体**: `EquipmentTypeDTO`
- **响应**: `ResponseDTO<EquipmentTypeDTO>`

#### 5.2.5 删除设备类型
- **URL**: `/api/equipment-type/{id}`
- **方法**: `DELETE`
- **响应**: `ResponseDTO<Void>`

### 5.3 校准记录管理 API

#### 5.3.1 创建校准记录
- **URL**: `/api/calibration-record`
- **方法**: `POST`
- **请求体**: `CalibrationRecordDTO`
- **响应**: `ResponseDTO<CalibrationRecordDTO>`

#### 5.3.2 查询校准记录列表
- **URL**: `/api/calibration-record`
- **方法**: `GET`
- **请求参数**: 分页参数、查询条件
- **响应**: `ResponseDTO<PageResultDTO<CalibrationRecordDTO>>`

#### 5.3.3 查询校准记录详情
- **URL**: `/api/calibration-record/{id}`
- **方法**: `GET`
- **响应**: `ResponseDTO<CalibrationRecordDTO>`

#### 5.3.4 更新校准记录
- **URL**: `/api/calibration-record/{id}`
- **方法**: `PUT`
- **请求体**: `CalibrationRecordDTO`
- **响应**: `ResponseDTO<CalibrationRecordDTO>`

#### 5.3.5 删除校准记录
- **URL**: `/api/calibration-record/{id}`
- **方法**: `DELETE`
- **响应**: `ResponseDTO<Void>`

#### 5.3.6 查询设备校准记录
- **URL**: `/api/calibration-record/equipment/{equipmentId}`
- **方法**: `GET`
- **请求参数**: 分页参数
- **响应**: `ResponseDTO<PageResultDTO<CalibrationRecordDTO>>`

### 5.4 维护计划管理 API

#### 5.4.1 创建维护计划
- **URL**: `/api/maintenance-plan`
- **方法**: `POST`
- **请求体**: `MaintenancePlanDTO`
- **响应**: `ResponseDTO<MaintenancePlanDTO>`

#### 5.4.2 查询维护计划列表
- **URL**: `/api/maintenance-plan`
- **方法**: `GET`
- **请求参数**: 分页参数、查询条件
- **响应**: `ResponseDTO<PageResultDTO<MaintenancePlanDTO>>`

#### 5.4.3 查询维护计划详情
- **URL**: `/api/maintenance-plan/{id}`
- **方法**: `GET`
- **响应**: `ResponseDTO<MaintenancePlanDTO>`

#### 5.4.4 更新维护计划
- **URL**: `/api/maintenance-plan/{id}`
- **方法**: `PUT`
- **请求体**: `MaintenancePlanDTO`
- **响应**: `ResponseDTO<MaintenancePlanDTO>`

#### 5.4.5 删除维护计划
- **URL**: `/api/maintenance-plan/{id}`
- **方法**: `DELETE`
- **响应**: `ResponseDTO<Void>`

#### 5.4.6 查询设备维护计划
- **URL**: `/api/maintenance-plan/equipment/{equipmentId}`
- **方法**: `GET`
- **响应**: `ResponseDTO<List<MaintenancePlanDTO>>`

### 5.5 维护记录管理 API

#### 5.5.1 创建维护记录
- **URL**: `/api/maintenance-record`
- **方法**: `POST`
- **请求体**: `MaintenanceRecordDTO`
- **响应**: `ResponseDTO<MaintenanceRecordDTO>`

#### 5.5.2 查询维护记录列表
- **URL**: `/api/maintenance-record`
- **方法**: `GET`
- **请求参数**: 分页参数、查询条件
- **响应**: `ResponseDTO<PageResultDTO<MaintenanceRecordDTO>>`

#### 5.5.3 查询维护记录详情
- **URL**: `/api/maintenance-record/{id}`
- **方法**: `GET`
- **响应**: `ResponseDTO<MaintenanceRecordDTO>`

#### 5.5.4 更新维护记录
- **URL**: `/api/maintenance-record/{id}`
- **方法**: `PUT`
- **请求体**: `MaintenanceRecordDTO`
- **响应**: `ResponseDTO<MaintenanceRecordDTO>`

#### 5.5.5 删除维护记录
- **URL**: `/api/maintenance-record/{id}`
- **方法**: `DELETE`
- **响应**: `ResponseDTO<Void>`

#### 5.5.6 查询设备维护记录
- **URL**: `/api/maintenance-record/equipment/{equipmentId}`
- **方法**: `GET`
- **请求参数**: 分页参数
- **响应**: `ResponseDTO<PageResultDTO<MaintenanceRecordDTO>>`

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
- 使用 Redis 缓存设备基本信息
- 缓存设备类型数据
- 缓存查询结果

### 7.2 数据库优化
- 合理设计索引
- 优化查询语句
- 使用分页查询

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

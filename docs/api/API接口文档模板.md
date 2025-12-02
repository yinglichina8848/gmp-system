# GMP系统 API 接口文档

## 1. 概述

本文档描述了GMP系统的API接口规范，包括认证授权、用户管理、文档管理、设备管理、生产执行管理等功能的API接口。

## 2. 认证与授权

### 2.1 认证方式
- 使用 JWT（JSON Web Token）进行认证
- 认证成功后，在请求头中携带 `Authorization: Bearer {token}` 进行后续请求

### 2.2 授权方式
- 基于角色的访问控制（RBAC）
- 不同角色拥有不同的权限

## 3. API 接口规范

### 3.1 通用响应格式

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {}
}
```

### 3.2 错误响应格式

```json
{
  "code": 400,
  "message": "错误信息",
  "data": null
}
```

### 3.3 分页响应格式

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "total": 100,
    "page": 1,
    "size": 10,
    "pages": 10,
    "list": [
      // 数据列表
    ]
  }
}
```

## 4. 服务 API 接口

### 4.1 Auth Service API

#### 4.1.1 认证授权 API

| API 路径 | 方法 | 功能描述 | 认证要求 | 请求体 | 响应体 |
|----------|------|----------|----------|--------|--------|
| /api/auth/login | POST | 用户登录 | 否 | `LoginRequest` | `LoginResponse` |
| /api/auth/logout | POST | 用户登出 | 是 | 无 | `ApiResponse` |
| /api/auth/refresh | POST | 刷新令牌 | 否 | `TokenRefreshRequest` | `TokenResponse` |
| /api/auth/verify | GET | 验证令牌 | 是 | 无 | `ApiResponse` |

#### 4.1.2 用户管理 API

| API 路径 | 方法 | 功能描述 | 认证要求 | 请求体 | 响应体 |
|----------|------|----------|----------|--------|--------|
| /api/auth/users | GET | 查询用户列表 | 是（管理员） | 无 | `ApiResponse<Page<UserDTO>>` |
| /api/auth/users/{id} | GET | 查询用户详情 | 是（管理员） | 无 | `ApiResponse<UserDTO>` |
| /api/auth/users | POST | 创建用户 | 是（管理员） | `CreateUserRequest` | `ApiResponse<UserDTO>` |
| /api/auth/users/{id} | PUT | 更新用户 | 是（管理员） | `UpdateUserRequest` | `ApiResponse<UserDTO>` |
| /api/auth/users/{id} | DELETE | 删除用户 | 是（管理员） | 无 | `ApiResponse` |

#### 4.1.3 权限管理 API

| API 路径 | 方法 | 功能描述 | 认证要求 | 请求体 | 响应体 |
|----------|------|----------|----------|--------|--------|
| /api/auth/roles | GET | 查询角色列表 | 是（管理员） | 无 | `ApiResponse<List<RoleDTO>>` |
| /api/auth/roles | POST | 创建角色 | 是（管理员） | `CreateRoleRequest` | `ApiResponse<RoleDTO>` |
| /api/auth/users/{id}/roles | POST | 分配角色 | 是（管理员） | `AssignRoleRequest` | `ApiResponse` |
| /api/auth/permissions | GET | 查询权限列表 | 是（管理员） | 无 | `ApiResponse<List<PermissionDTO>>` |

### 4.2 EDMS Service API

#### 4.2.1 文档管理 API

| API 路径 | 方法 | 功能描述 | 认证要求 | 请求体 | 响应体 |
|----------|------|----------|----------|--------|--------|
| /api/edms/documents | GET | 查询文档列表 | 是 | 无 | `ApiResponse<Page<DocumentDTO>>` |
| /api/edms/documents/{id} | GET | 查询文档详情 | 是 | 无 | `ApiResponse<DocumentDTO>` |
| /api/edms/documents | POST | 创建文档 | 是 | `CreateDocumentRequest` | `ApiResponse<DocumentDTO>` |
| /api/edms/documents/{id} | PUT | 更新文档 | 是 | `UpdateDocumentRequest` | `ApiResponse<DocumentDTO>` |
| /api/edms/documents/{id} | DELETE | 删除文档 | 是 | 无 | `ApiResponse` |
| /api/edms/documents/{id}/content | GET | 获取文档内容 | 是 | 无 | `byte[]` |
| /api/edms/documents/{id}/content | PUT | 更新文档内容 | 是 | `MultipartFile` | `ApiResponse` |

#### 4.2.2 工作流审批 API

| API 路径 | 方法 | 功能描述 | 认证要求 | 请求体 | 响应体 |
|----------|------|----------|----------|--------|--------|
| /api/edms/approvals/start | POST | 启动审批流程 | 是 | `StartApprovalRequest` | `ApiResponse<ApprovalInstanceDTO>` |
| /api/edms/approvals/{id}/step/{stepId}/action | POST | 执行审批决策 | 是 | `ApprovalActionRequest` | `ApiResponse<ApprovalInstanceDTO>` |
| /api/edms/approvals/{id} | GET | 获取审批实例 | 是 | 无 | `ApiResponse<ApprovalInstanceDTO>` |
| /api/edms/approvals/pending/{userId} | GET | 获取待办任务 | 是 | 无 | `ApiResponse<List<ApprovalTaskDTO>>` |
| /api/edms/approvals/history/{documentId} | GET | 获取审批历史 | 是 | 无 | `ApiResponse<List<ApprovalHistoryDTO>>` |
| /api/edms/approvals/{id}/withdraw | PUT | 撤回审批 | 是 | 无 | `ApiResponse` |

#### 4.2.3 全文搜索 API

| API 路径 | 方法 | 功能描述 | 认证要求 | 请求体 | 响应体 |
|----------|------|----------|----------|--------|--------|
| /api/edms/search/fulltext | GET | 全文搜索 | 是 | 无 | `ApiResponse<List<DocumentDTO>>` |
| /api/edms/search/structured | GET | 结构化搜索 | 是 | 无 | `ApiResponse<List<DocumentDTO>>` |
| /api/edms/search/advanced | POST | 高级搜索 | 是 | `AdvancedSearchRequest` | `ApiResponse<List<DocumentDTO>>` |

### 4.3 Equipment Service API

#### 4.3.1 设备管理 API

| API 路径 | 方法 | 功能描述 | 认证要求 | 请求体 | 响应体 |
|----------|------|----------|----------|--------|--------|
| /api/equipment | GET | 查询设备列表 | 是 | 无 | `ApiResponse<Page<EquipmentDTO>>` |
| /api/equipment/{id} | GET | 查询设备详情 | 是 | 无 | `ApiResponse<EquipmentDTO>` |
| /api/equipment | POST | 创建设备 | 是 | `CreateEquipmentRequest` | `ApiResponse<EquipmentDTO>` |
| /api/equipment/{id} | PUT | 更新设备 | 是 | `UpdateEquipmentRequest` | `ApiResponse<EquipmentDTO>` |
| /api/equipment/{id} | DELETE | 删除设备 | 是 | 无 | `ApiResponse` |
| /api/equipment/{id}/status | PUT | 更新设备状态 | 是 | `EquipmentStatusUpdateDTO` | `ApiResponse<EquipmentDTO>` |

#### 4.3.2 设备类型 API

| API 路径 | 方法 | 功能描述 | 认证要求 | 请求体 | 响应体 |
|----------|------|----------|----------|--------|--------|
| /api/equipment-type | GET | 查询设备类型列表 | 是 | 无 | `ApiResponse<List<EquipmentTypeDTO>>` |
| /api/equipment-type/{id} | GET | 查询设备类型详情 | 是 | 无 | `ApiResponse<EquipmentTypeDTO>` |
| /api/equipment-type | POST | 创建设备类型 | 是 | `CreateEquipmentTypeRequest` | `ApiResponse<EquipmentTypeDTO>` |
| /api/equipment-type/{id} | PUT | 更新设备类型 | 是 | `UpdateEquipmentTypeRequest` | `ApiResponse<EquipmentTypeDTO>` |
| /api/equipment-type/{id} | DELETE | 删除设备类型 | 是 | 无 | `ApiResponse` |

#### 4.3.3 校准记录 API

| API 路径 | 方法 | 功能描述 | 认证要求 | 请求体 | 响应体 |
|----------|------|----------|----------|--------|--------|
| /api/calibration-record | GET | 查询校准记录列表 | 是 | 无 | `ApiResponse<Page<CalibrationRecordDTO>>` |
| /api/calibration-record/{id} | GET | 查询校准记录详情 | 是 | 无 | `ApiResponse<CalibrationRecordDTO>` |
| /api/calibration-record | POST | 创建校准记录 | 是 | `CreateCalibrationRecordRequest` | `ApiResponse<CalibrationRecordDTO>` |
| /api/calibration-record/{id} | PUT | 更新校准记录 | 是 | `UpdateCalibrationRecordRequest` | `ApiResponse<CalibrationRecordDTO>` |
| /api/calibration-record/{id} | DELETE | 删除校准记录 | 是 | 无 | `ApiResponse` |
| /api/calibration-record/equipment/{equipmentId} | GET | 查询设备校准记录 | 是 | 无 | `ApiResponse<Page<CalibrationRecordDTO>>` |

#### 4.3.4 维护计划 API

| API 路径 | 方法 | 功能描述 | 认证要求 | 请求体 | 响应体 |
|----------|------|----------|----------|--------|--------|
| /api/maintenance-plan | GET | 查询维护计划列表 | 是 | 无 | `ApiResponse<Page<MaintenancePlanDTO>>` |
| /api/maintenance-plan/{id} | GET | 查询维护计划详情 | 是 | 无 | `ApiResponse<MaintenancePlanDTO>` |
| /api/maintenance-plan | POST | 创建维护计划 | 是 | `CreateMaintenancePlanRequest` | `ApiResponse<MaintenancePlanDTO>` |
| /api/maintenance-plan/{id} | PUT | 更新维护计划 | 是 | `UpdateMaintenancePlanRequest` | `ApiResponse<MaintenancePlanDTO>` |
| /api/maintenance-plan/{id} | DELETE | 删除维护计划 | 是 | 无 | `ApiResponse` |
| /api/maintenance-plan/equipment/{equipmentId} | GET | 查询设备维护计划 | 是 | 无 | `ApiResponse<List<MaintenancePlanDTO>>` |

#### 4.3.5 维护记录 API

| API 路径 | 方法 | 功能描述 | 认证要求 | 请求体 | 响应体 |
|----------|------|----------|----------|--------|--------|
| /api/maintenance-record | GET | 查询维护记录列表 | 是 | 无 | `ApiResponse<Page<MaintenanceRecordDTO>>` |
| /api/maintenance-record/{id} | GET | 查询维护记录详情 | 是 | 无 | `ApiResponse<MaintenanceRecordDTO>` |
| /api/maintenance-record | POST | 创建维护记录 | 是 | `CreateMaintenanceRecordRequest` | `ApiResponse<MaintenanceRecordDTO>` |
| /api/maintenance-record/{id} | PUT | 更新维护记录 | 是 | `UpdateMaintenanceRecordRequest` | `ApiResponse<MaintenanceRecordDTO>` |
| /api/maintenance-record/{id} | DELETE | 删除维护记录 | 是 | 无 | `ApiResponse` |
| /api/maintenance-record/equipment/{equipmentId} | GET | 查询设备维护记录 | 是 | 无 | `ApiResponse<Page<MaintenanceRecordDTO>>` |

### 4.4 MES Service API

#### 4.4.1 生产订单 API

| API 路径 | 方法 | 功能描述 | 认证要求 | 请求体 | 响应体 |
|----------|------|----------|----------|--------|--------|
| /api/mes/production-orders | GET | 查询生产订单列表 | 是 | 无 | `ApiResponse<Page<ProductionOrderDTO>>` |
| /api/mes/production-orders/{id} | GET | 查询生产订单详情 | 是 | 无 | `ApiResponse<ProductionOrderDTO>` |
| /api/mes/production-orders | POST | 创建生产订单 | 是 | `CreateProductionOrderRequest` | `ApiResponse<ProductionOrderDTO>` |
| /api/mes/production-orders/{id} | PUT | 更新生产订单 | 是 | `UpdateProductionOrderRequest` | `ApiResponse<ProductionOrderDTO>` |
| /api/mes/production-orders/{id} | DELETE | 删除生产订单 | 是 | 无 | `ApiResponse` |
| /api/mes/production-orders/{id}/status | PUT | 更新生产订单状态 | 是 | `StatusUpdateDTO` | `ApiResponse<ProductionOrderDTO>` |

#### 4.4.2 生产批次 API

| API 路径 | 方法 | 功能描述 | 认证要求 | 请求体 | 响应体 |
|----------|------|----------|----------|--------|--------|
| /api/mes/production-batches | GET | 查询生产批次列表 | 是 | 无 | `ApiResponse<Page<ProductionBatchDTO>>` |
| /api/mes/production-batches/{id} | GET | 查询生产批次详情 | 是 | 无 | `ApiResponse<ProductionBatchDTO>` |
| /api/mes/production-batches | POST | 创建生产批次 | 是 | `CreateProductionBatchRequest` | `ApiResponse<ProductionBatchDTO>` |
| /api/mes/production-batches/{id} | PUT | 更新生产批次 | 是 | `UpdateProductionBatchRequest` | `ApiResponse<ProductionBatchDTO>` |
| /api/mes/production-batches/{id} | DELETE | 删除生产批次 | 是 | 无 | `ApiResponse` |
| /api/mes/production-batches/{id}/status | PUT | 更新生产批次状态 | 是 | `StatusUpdateDTO` | `ApiResponse<ProductionBatchDTO>` |
| /api/mes/production-batches/order/{orderId} | GET | 查询订单关联的批次 | 是 | 无 | `ApiResponse<Page<ProductionBatchDTO>>` |

#### 4.4.3 批次操作 API

| API 路径 | 方法 | 功能描述 | 认证要求 | 请求体 | 响应体 |
|----------|------|----------|----------|--------|--------|
| /api/mes/batch-operations | GET | 查询批次操作记录列表 | 是 | 无 | `ApiResponse<Page<BatchOperationDTO>>` |
| /api/mes/batch-operations/{id} | GET | 查询批次操作记录详情 | 是 | 无 | `ApiResponse<BatchOperationDTO>` |
| /api/mes/batch-operations | POST | 创建批次操作记录 | 是 | `CreateBatchOperationRequest` | `ApiResponse<BatchOperationDTO>` |
| /api/mes/batch-operations/{id} | PUT | 更新批次操作记录 | 是 | `UpdateBatchOperationRequest` | `ApiResponse<BatchOperationDTO>` |
| /api/mes/batch-operations/{id} | DELETE | 删除批次操作记录 | 是 | 无 | `ApiResponse` |
| /api/mes/batch-operations/batch/{batchId} | GET | 查询批次关联的操作记录 | 是 | 无 | `ApiResponse<Page<BatchOperationDTO>>` |

#### 4.4.4 设备监控 API

| API 路径 | 方法 | 功能描述 | 认证要求 | 请求体 | 响应体 |
|----------|------|----------|----------|--------|--------|
| /api/mes/equipment-monitors | GET | 查询设备监控记录列表 | 是 | 无 | `ApiResponse<Page<EquipmentMonitorDTO>>` |
| /api/mes/equipment-monitors/{id} | GET | 查询设备监控记录详情 | 是 | 无 | `ApiResponse<EquipmentMonitorDTO>` |
| /api/mes/equipment-monitors | POST | 创建设备监控记录 | 是 | `CreateEquipmentMonitorRequest` | `ApiResponse<EquipmentMonitorDTO>` |
| /api/mes/equipment-monitors/equipment/{equipmentId} | GET | 查询设备关联的监控记录 | 是 | 无 | `ApiResponse<Page<EquipmentMonitorDTO>>` |
| /api/mes/equipment-monitors/equipment/{equipmentId}/latest | GET | 查询设备最新监控记录 | 是 | 无 | `ApiResponse<EquipmentMonitorDTO>` |

#### 4.4.5 中药炮制工艺 API

| API 路径 | 方法 | 功能描述 | 认证要求 | 请求体 | 响应体 |
|----------|------|----------|----------|--------|--------|
| /api/mes/tcm-processing-procedures | GET | 查询中药炮制工艺列表 | 是 | 无 | `ApiResponse<Page<TcmProcessingProcedureDTO>>` |
| /api/mes/tcm-processing-procedures/{id} | GET | 查询中药炮制工艺详情 | 是 | 无 | `ApiResponse<TcmProcessingProcedureDTO>` |
| /api/mes/tcm-processing-procedures | POST | 创建中药炮制工艺 | 是 | `CreateTcmProcessingProcedureRequest` | `ApiResponse<TcmProcessingProcedureDTO>` |
| /api/mes/tcm-processing-procedures/{id} | PUT | 更新中药炮制工艺 | 是 | `UpdateTcmProcessingProcedureRequest` | `ApiResponse<TcmProcessingProcedureDTO>` |
| /api/mes/tcm-processing-procedures/{id} | DELETE | 删除中药炮制工艺 | 是 | 无 | `ApiResponse` |

## 5. 数据模型

### 5.1 认证授权相关模型

#### 5.1.1 LoginRequest

```json
{
  "username": "admin",
  "password": "Admin@123"
}
```

#### 5.1.2 LoginResponse

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": 1,
    "username": "admin",
    "name": "管理员",
    "email": "admin@example.com",
    "roles": ["ADMIN"]
  }
}
```

### 5.2 用户管理相关模型

#### 5.2.1 UserDTO

```json
{
  "id": 1,
  "username": "admin",
  "name": "管理员",
  "email": "admin@example.com",
  "phone": "13800138000",
  "status": "ACTIVE",
  "roles": ["ADMIN"],
  "createdAt": "2025-01-01T00:00:00",
  "updatedAt": "2025-01-01T00:00:00"
}
```

#### 5.2.2 CreateUserRequest

```json
{
  "username": "testuser",
  "password": "Test@123",
  "name": "测试用户",
  "email": "test@example.com",
  "phone": "13800138001",
  "roles": ["USER"]
}
```

### 5.3 文档管理相关模型

#### 5.3.1 DocumentDTO

```json
{
  "id": 1,
  "documentCode": "DOC-2025-0001",
  "documentName": "测试文档",
  "documentType": "SOP",
  "status": "DRAFT",
  "confidentialityLevel": "NORMAL",
  "version": "1.0",
  "author": "admin",
  "createdAt": "2025-01-01T00:00:00",
  "updatedAt": "2025-01-01T00:00:00"
}
```

#### 5.3.2 CreateDocumentRequest

```json
{
  "documentName": "测试文档",
  "documentType": "SOP",
  "confidentialityLevel": "NORMAL",
  "description": "测试文档描述"
}
```

## 6. 错误码定义

| 错误码 | 描述 |
|--------|------|
| 200 | 操作成功 |
| 400 | 请求参数错误 |
| 401 | 未授权 |
| 403 | 权限不足 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |
| 501 | 功能未实现 |
| 502 | 网关错误 |
| 503 | 服务不可用 |
| 504 | 网关超时 |

## 7. 安全规范

### 7.1 数据传输安全
- 使用 HTTPS 进行数据传输
- 敏感数据加密传输

### 7.2 数据存储安全
- 敏感数据加密存储
- 定期数据备份

### 7.3 访问控制
- 基于角色的访问控制
- 细粒度的权限控制
- 定期权限审查

## 8. 版本控制

| 版本 | 日期 | 描述 | 作者 |
|------|------|------|------|
| 1.0 | 2025-11-28 | 初始版本 | GMP开发团队 |
| 1.1 | YYYY-MM-DD | 更新内容 | GMP开发团队 |

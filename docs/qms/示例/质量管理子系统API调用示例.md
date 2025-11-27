# 质量管理子系统API调用示例

## 1. 文档概述

本文档提供质量管理子系统(QMS)的API调用示例，帮助开发人员理解如何与QMS进行集成和交互。示例包括偏差管理、CAPA管理、变更控制等核心模块的API调用方式。

## 2. API调用基础设置

### 2.1 认证与授权

调用QMS API前，需要先获取JWT令牌：

```bash
# 获取JWT令牌示例
curl -X POST 'http://localhost:8080/auth-sys/api/auth/login' \
  -H 'Content-Type: application/json' \
  -d '{"username": "admin", "password": "admin123"}'
```

成功响应示例：
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expiresIn": 3600,
  "user": {
    "id": 1,
    "username": "admin",
    "roles": ["ADMIN", "QMS_MANAGER"]
  }
}
```

### 2.2 通用请求头设置

所有API调用都需要在请求头中包含以下内容：

```bash
-H 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...'
-H 'Content-Type: application/json'
-H 'Accept: application/json'
```

## 3. 偏差管理API示例

### 3.1 创建偏差报告

```bash
# 创建偏差报告
curl -X POST 'http://localhost:8080/qms/api/deviations' \
  -H 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...' \
  -H 'Content-Type: application/json' \
  -d '{
    "title": "产品规格偏差",
    "description": "批次P2023061501的含量测试结果超出允许范围",
    "deviationType": "PRODUCT_QUALITY",
    "severity": "MEDIUM",
    "discoveryDate": "2023-06-15T08:30:00Z",
    "affectedBatch": "P2023061501",
    "reporter": "张三",
    "department": "质量控制部"
  }'
```

成功响应示例：
```json
{
  "id": "DEV20230615001",
  "title": "产品规格偏差",
  "description": "批次P2023061501的含量测试结果超出允许范围",
  "status": "OPEN",
  "createdAt": "2023-06-15T10:15:30Z",
  "updatedAt": "2023-06-15T10:15:30Z",
  "createdBy": "admin"
}
```

### 3.2 查询偏差列表

```bash
# 查询偏差列表（带分页和筛选）
curl -X GET 'http://localhost:8080/qms/api/deviations?page=0&size=10&status=OPEN&severity=HIGH&startDate=2023-06-01'
  -H 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...'
```

成功响应示例：
```json
{
  "content": [
    {
      "id": "DEV20230610001",
      "title": "设备异常",
      "status": "OPEN",
      "severity": "HIGH",
      "createdAt": "2023-06-10T14:20:00Z"
    },
    {
      "id": "DEV20230612001",
      "title": "原材料不合格",
      "status": "OPEN",
      "severity": "HIGH",
      "createdAt": "2023-06-12T09:15:00Z"
    }
  ],
  "totalElements": 2,
  "totalPages": 1,
  "number": 0,
  "size": 10
}
```

## 4. CAPA管理API示例

### 4.1 创建CAPA计划

```bash
# 创建CAPA计划
curl -X POST 'http://localhost:8080/qms/api/capas' \
  -H 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...' \
  -H 'Content-Type: application/json' \
  -d '{
    "title": "解决产品含量偏差问题",
    "description": "针对批次P2023061501的含量偏差，制定并实施纠正和预防措施",
    "relatedDeviationId": "DEV20230615001",
    "rootCause": "设备校准偏差",
    "correctiveActions": ["重新校准测试设备", "增加日常校准频率"],
    "preventiveActions": ["建立设备校准监控系统", "定期培训操作人员"],
    "targetCompletionDate": "2023-07-15T23:59:59Z",
    "assignee": "李四"
  }'
```

成功响应示例：
```json
{
  "id": "CAPA20230615001",
  "title": "解决产品含量偏差问题",
  "status": "PLANNED",
  "createdAt": "2023-06-15T11:30:00Z",
  "createdBy": "admin"
}
```

### 4.2 更新CAPA状态

```bash
# 更新CAPA状态为已完成
curl -X PUT 'http://localhost:8080/qms/api/capas/CAPA20230615001/status' \
  -H 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...' \
  -H 'Content-Type: application/json' \
  -d '{"status": "COMPLETED", "completionNotes": "所有措施已实施并验证有效"}'
```

成功响应示例：
```json
{
  "id": "CAPA20230615001",
  "status": "COMPLETED",
  "completionDate": "2023-07-10T16:45:00Z",
  "updatedAt": "2023-07-10T16:45:00Z"
}
```

## 5. 变更控制API示例

### 5.1 提交变更请求

```bash
# 提交变更请求
curl -X POST 'http://localhost:8080/qms/api/changes' \
  -H 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...' \
  -H 'Content-Type: application/json' \
  -d '{
    "title": "更新生产工艺参数",
    "description": "优化产品A的干燥温度参数，提高产品稳定性",
    "changeType": "PROCESS",
    "impactLevel": "MEDIUM",
    "affectedSystems": ["生产系统", "质量控制系统"],
    "proposedImplementationDate": "2023-08-01T00:00:00Z",
    "submitter": "王五",
    "justification": "当前参数导致产品水分含量波动较大"
  }'
```

成功响应示例：
```json
{
  "id": "CHG20230618001",
  "title": "更新生产工艺参数",
  "status": "PENDING_REVIEW",
  "createdAt": "2023-06-18T13:20:00Z",
  "createdBy": "admin"
}
```

### 5.2 审批变更请求

```bash
# 审批变更请求
curl -X POST 'http://localhost:8080/qms/api/changes/CHG20230618001/approve' \
  -H 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...' \
  -H 'Content-Type: application/json' \
  -d '{"approvalLevel": "QA_MANAGER", "comments": "同意变更，建议在小批量生产中验证效果"}'
```

成功响应示例：
```json
{
  "id": "CHG20230618001",
  "status": "APPROVED",
  "approvalDate": "2023-06-20T10:30:00Z",
  "approvedBy": "admin"
}
```

## 6. 文件上传下载示例

### 6.1 上传附件

```bash
# 上传偏差报告附件
curl -X POST 'http://localhost:8080/qms/api/deviations/DEV20230615001/attachments' \
  -H 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...' \
  -H 'Content-Type: multipart/form-data' \
  -F 'file=@/path/to/test_report.pdf' \
  -F 'description=含量测试报告'
```

成功响应示例：
```json
{
  "id": "ATT20230615001",
  "fileName": "test_report.pdf",
  "fileSize": 102400,
  "fileType": "application/pdf",
  "description": "含量测试报告",
  "uploadDate": "2023-06-15T14:25:00Z",
  "uploadedBy": "admin"
}
```

### 6.2 下载附件

```bash
# 下载附件
curl -X GET 'http://localhost:8080/qms/api/attachments/ATT20230615001/download' \
  -H 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...' \
  -o test_report_downloaded.pdf
```

## 7. 错误处理示例

### 7.1 认证失败

```bash
# 无效令牌示例
curl -X GET 'http://localhost:8080/qms/api/deviations' \
  -H 'Authorization: Bearer invalid_token'
```

错误响应示例：
```json
{
  "timestamp": "2023-06-15T10:20:30Z",
  "status": 401,
  "error": "Unauthorized",
  "message": "Invalid JWT token",
  "path": "/qms/api/deviations"
}
```

### 7.2 数据验证失败

```bash
# 缺少必填字段示例
curl -X POST 'http://localhost:8080/qms/api/deviations' \
  -H 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...' \
  -H 'Content-Type: application/json' \
  -d '{"description": "缺少标题字段的偏差报告"}'
```

错误响应示例：
```json
{
  "timestamp": "2023-06-15T10:25:45Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed: Title is required",
  "path": "/qms/api/deviations",
  "validationErrors": [
    {
      "field": "title",
      "message": "Title is required"
    }
  ]
}
```

## 8. 集成开发建议

1. **错误处理机制**：实现统一的错误处理逻辑，根据HTTP状态码和错误信息进行相应处理
2. **重试策略**：对于网络临时故障，实现指数退避重试策略
3. **超时设置**：为API调用设置合理的超时时间，避免长时间阻塞
4. **日志记录**：记录API调用的详细信息，包括请求参数、响应状态和时间消耗
5. **权限检查**：在调用前验证用户权限，避免不必要的API调用

## 9. 参考资料

- [质量管理子系统API实现文档](../实现/质量管理子系统API实现文档.md)
- [RESTful API最佳实践](https://restfulapi.net/)
- [OAuth 2.0和JWT认证指南](https://auth0.com/docs/)
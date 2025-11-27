# 质量管理子系统API实现文档

## 1. 文档概述

本文档详细描述GMP系统中质量管理子系统(QMS)的API实现细节，包括API端点定义、请求/响应格式、错误处理机制、安全认证等。本文档是开发团队进行API集成和前端开发的重要依据。

## 2. API架构

### 2.1 整体架构

QMS子系统API采用RESTful风格设计，遵循以下架构原则：

- 资源导向设计
- 标准HTTP方法使用
- 统一资源标识符(URI)规范
- 标准状态码使用
- 一致的错误处理机制

### 2.2 技术实现

- 框架：Spring Boot 2.7.x
- API文档：Swagger/OpenAPI 3.0
- 安全：Spring Security + JWT
- 数据格式：JSON
- 版本控制：URL路径版本化(v1)

## 3. 安全认证

### 3.1 JWT认证流程

```
1. 客户端请求认证令牌：POST /api/auth/login
2. 认证子系统验证凭据并返回JWT令牌
3. 客户端在后续请求中在Authorization头中携带JWT令牌
4. QMS子系统验证JWT令牌有效性并授权访问
```

### 3.2 API安全配置

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.oauth2ResourceServer().jwt();
        
        http.authorizeRequests()
            // 偏差管理权限配置
            .antMatchers("/api/v1/deviations").hasAnyRole("QUALITY_MANAGER", "QA_SPECIALIST")
            .antMatchers("/api/v1/deviations/**/approve").hasRole("QUALITY_MANAGER")
            
            // CAPA管理权限配置
            .antMatchers("/api/v1/capas").hasAnyRole("QUALITY_MANAGER", "QA_SPECIALIST")
            .antMatchers("/api/v1/capas/**/verify").hasRole("QA_SPECIALIST")
            
            // 其他API权限配置...
            
            .anyRequest().authenticated();
    }
}
```

## 4. 通用请求/响应格式

### 4.1 分页请求参数

```
GET /api/v1/deviations?page=0&size=10&sort=createdAt,desc
```

- page: 页码，从0开始
- size: 每页记录数
- sort: 排序字段和方向，格式为field,direction

### 4.2 分页响应格式

```json
{
  "content": [
    {
      "id": 1,
      "code": "DEV-2023-001",
      "title": "温度异常偏差",
      "status": "NEW"
    },
    // 更多记录...
  ],
  "pageable": {
    "sort": {
      "sorted": true,
      "unsorted": false,
      "empty": false
    },
    "pageSize": 10,
    "pageNumber": 0,
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "totalPages": 5,
  "totalElements": 45,
  "last": false,
  "size": 10,
  "number": 0,
  "sort": {
    "sorted": true,
    "unsorted": false,
    "empty": false
  },
  "first": true,
  "numberOfElements": 10,
  "empty": false
}
```

### 4.3 错误响应格式

```json
{
  "timestamp": "2023-08-10T12:34:56.789Z",
  "status": 404,
  "error": "Not Found",
  "message": "偏差记录不存在，ID: 123",
  "path": "/api/v1/deviations/123"
}
```

## 5. 偏差管理API

### 5.1 偏差列表查询

```
GET /api/v1/deviations
```

**查询参数：**
- code: 偏差编号（模糊匹配）
- title: 偏差标题（模糊匹配）
- status: 偏差状态
- severity: 严重程度
- type: 偏差类型
- startDate: 开始日期（格式：YYYY-MM-DD）
- endDate: 结束日期（格式：YYYY-MM-DD）

**响应：** 200 OK

```json
{
  "content": [
    {
      "id": 1,
      "code": "DEV-2023-001",
      "title": "温度异常偏差",
      "description": "生产车间温度超出规定范围",
      "type": "SYSTEM",
      "severity": "MINOR",
      "status": "NEW",
      "occurrenceDate": "2023-07-20T08:30:00Z",
      "location": "生产车间A区",
      "reporter": {
        "id": 101,
        "username": "zhangwei",
        "fullName": "张伟"
      },
      "createdAt": "2023-07-20T09:15:00Z"
    }
  ],
  "pageable": {...},
  "totalPages": 1,
  "totalElements": 1,
  "last": true,
  "size": 10,
  "number": 0,
  "sort": {...},
  "first": true,
  "numberOfElements": 1,
  "empty": false
}
```

### 5.2 创建偏差

```
POST /api/v1/deviations
```

**请求体：**

```json
{
  "title": "温度异常偏差",
  "description": "生产车间温度超出规定范围",
  "type": "SYSTEM",
  "severity": "MINOR",
  "occurrenceDate": "2023-07-20T08:30:00Z",
  "location": "生产车间A区",
  "relatedBatchId": "BATCH-2023-0001",
  "relatedProductId": "PROD-001"
}
```

**响应：** 201 Created

```json
{
  "id": 1,
  "code": "DEV-2023-001",
  "title": "温度异常偏差",
  "status": "NEW",
  "reporter": {
    "id": 101,
    "username": "zhangwei",
    "fullName": "张伟"
  },
  "createdAt": "2023-07-20T09:15:00Z"
}
```

### 5.3 获取偏差详情

```
GET /api/v1/deviations/{id}
```

**响应：** 200 OK

```json
{
  "id": 1,
  "code": "DEV-2023-001",
  "title": "温度异常偏差",
  "description": "生产车间温度超出规定范围",
  "type": "SYSTEM",
  "severity": "MINOR",
  "status": "UNDER_INVESTIGATION",
  "occurrenceDate": "2023-07-20T08:30:00Z",
  "location": "生产车间A区",
  "reporter": {
    "id": 101,
    "username": "zhangwei",
    "fullName": "张伟"
  },
  "investigator": {
    "id": 102,
    "username": "lihua",
    "fullName": "李华"
  },
  "investigationSummary": "已确认温度传感器故障导致读数异常",
  "rootCause": "温度传感器校准过期",
  "actions": [
    {
      "id": 101,
      "description": "更换温度传感器并重新校准",
      "responsible": {
        "id": 103,
        "username": "wangming",
        "fullName": "王明"
      },
      "dueDate": "2023-07-25T17:00:00Z",
      "status": "IN_PROGRESS"
    }
  ],
  "attachments": [
    {
      "id": 201,
      "name": "温度记录.pdf",
      "contentType": "application/pdf",
      "fileSize": 102400,
      "uploadedBy": {
        "id": 101,
        "username": "zhangwei",
        "fullName": "张伟"
      },
      "uploadedAt": "2023-07-20T10:30:00Z"
    }
  ],
  "comments": [
    {
      "id": 301,
      "content": "已安排人员进行调查",
      "author": {
        "id": 102,
        "username": "lihua",
        "fullName": "李华"
      },
      "createdAt": "2023-07-20T14:20:00Z"
    }
  ],
  "createdAt": "2023-07-20T09:15:00Z",
  "updatedAt": "2023-07-20T14:20:00Z"
}
```

### 5.4 更新偏差信息

```
PUT /api/v1/deviations/{id}
```

**请求体：**

```json
{
  "title": "更新后的偏差标题",
  "description": "更新后的偏差描述",
  "investigationSummary": "更新后的调查摘要",
  "rootCause": "更新后的根本原因"
}
```

**响应：** 200 OK

```json
{
  "id": 1,
  "code": "DEV-2023-001",
  "title": "更新后的偏差标题",
  "status": "UNDER_INVESTIGATION",
  "updatedAt": "2023-07-21T10:00:00Z"
}
```

### 5.5 提交偏差调查

```
POST /api/v1/deviations/{id}/submit
```

**请求体：**

```json
{
  "investigatorId": 102,
  "investigationSummary": "初步调查结果",
  "rootCause": "可能的根本原因"
}
```

**响应：** 200 OK

```json
{
  "id": 1,
  "status": "UNDER_INVESTIGATION",
  "investigator": {
    "id": 102,
    "username": "lihua",
    "fullName": "李华"
  },
  "updatedAt": "2023-07-21T11:00:00Z"
}
```

### 5.6 批准偏差处理

```
POST /api/v1/deviations/{id}/approve
```

**请求体：**

```json
{
  "approvalComments": "措施合理，同意实施",
  "actions": [
    {
      "description": "更换温度传感器",
      "responsibleId": 103,
      "dueDate": "2023-07-25T17:00:00Z"
    },
    {
      "description": "校准所有温度传感器",
      "responsibleId": 104,
      "dueDate": "2023-07-30T17:00:00Z"
    }
  ]
}
```

**响应：** 200 OK

```json
{
  "id": 1,
  "status": "IN_PROGRESS",
  "approver": {
    "id": 105,
    "username": "zhaolin",
    "fullName": "赵琳"
  },
  "actions": [
    {
      "id": 101,
      "description": "更换温度传感器",
      "status": "ASSIGNED"
    },
    {
      "id": 102,
      "description": "校准所有温度传感器",
      "status": "ASSIGNED"
    }
  ],
  "updatedAt": "2023-07-22T13:00:00Z"
}
```

### 5.7 关闭偏差

```
POST /api/v1/deviations/{id}/close
```

**请求体：**

```json
{
  "conclusion": "所有纠正措施已完成，有效性得到验证，偏差可以关闭",
  "effectivenessAssessment": "EFFECTIVE"
}
```

**响应：** 200 OK

```json
{
  "id": 1,
  "status": "CLOSED",
  "closedAt": "2023-07-31T16:00:00Z",
  "updatedAt": "2023-07-31T16:00:00Z"
}
```

## 6. CAPA管理API

### 6.1 CAPA列表查询

```
GET /api/v1/capas
```

**查询参数：**
- code: CAPA编号（模糊匹配）
- title: CAPA标题（模糊匹配）
- status: CAPA状态
- ownerId: 负责人ID
- startDate: 开始日期
- endDate: 结束日期

**响应：** 200 OK（分页格式）

### 6.2 创建CAPA

```
POST /api/v1/capas
```

**请求体：**

```json
{
  "title": "温度传感器校准计划",
  "description": "针对多起温度异常偏差的预防措施",
  "ownerId": 103,
  "targetCompletionDate": "2023-08-31T17:00:00Z",
  "deviationId": 1,
  "actions": [
    {
      "type": "PREVENTIVE",
      "description": "制定温度传感器校准SOP",
      "responsibleId": 106,
      "dueDate": "2023-08-10T17:00:00Z"
    },
    {
      "type": "PREVENTIVE",
      "description": "实施定期校准计划",
      "responsibleId": 103,
      "dueDate": "2023-08-15T17:00:00Z"
    }
  ]
}
```

**响应：** 201 Created

### 6.3 更新CAPA措施状态

```
PUT /api/v1/capas/{capaId}/actions/{actionId}/status
```

**请求体：**

```json
{
  "status": "COMPLETED",
  "completionComments": "已按计划完成校准SOP制定"
}
```

**响应：** 200 OK

### 6.4 验证CAPA有效性

```
POST /api/v1/capas/{id}/verify
```

**请求体：**

```json
{
  "verifierId": 102,
  "result": "YES",
  "comments": "CAPA措施有效，未再发生类似问题"
}
```

**响应：** 200 OK

## 7. 变更控制API

### 7.1 变更申请列表

```
GET /api/v1/change-controls
```

**查询参数：**
- code: 变更编号
- title: 变更标题
- status: 变更状态
- type: 变更类型
- riskLevel: 风险等级

**响应：** 200 OK（分页格式）

### 7.2 创建变更申请

```
POST /api/v1/change-controls
```

**请求体：**

```json
{
  "title": "生产工艺参数优化",
  "description": "优化产品A的干燥温度参数",
  "type": "PROCESS",
  "riskLevel": "MEDIUM",
  "impactAssessment": "可能影响产品水分含量",
  "implementationPlan": "分阶段实施，先小批量验证"
}
```

**响应：** 201 Created

### 7.3 审核变更

```
POST /api/v1/change-controls/{id}/review
```

**请求体：**

```json
{
  "reviewerId": 105,
  "reviewResult": "APPROVED",
  "reviewComments": "变更理由充分，风险控制措施合理"
}
```

**响应：** 200 OK

### 7.4 批准变更

```
POST /api/v1/change-controls/{id}/approve
```

**请求体：**

```json
{
  "approverId": 107,
  "approvalResult": "APPROVED",
  "approvalComments": "同意实施，注意监控产品质量",
  "implementationDate": "2023-08-10T08:00:00Z"
}
```

**响应：** 200 OK

## 8. 审计管理API

### 8.1 审计计划列表

```
GET /api/v1/audits
```

**查询参数：**
- code: 审计编号
- title: 审计标题
- type: 审计类型
- status: 审计状态
- leadAuditorId: 主审ID

**响应：** 200 OK（分页格式）

### 8.2 创建审计计划

```
POST /api/v1/audits
```

**请求体：**

```json
{
  "title": "2023年度质量体系内部审计",
  "type": "INTERNAL",
  "scope": "生产部、质量部、仓储部",
  "standard": "ISO 9001:2015",
  "plannedStartDate": "2023-09-10T08:00:00Z",
  "plannedEndDate": "2023-09-15T17:00:00Z",
  "leadAuditorId": 108,
  "auditTeam": [102, 105, 109]
}
```

**响应：** 201 Created

### 8.3 添加审计发现

```
POST /api/v1/audits/{auditId}/findings
```

**请求体：**

```json
{
  "description": "未按规定保存原始数据记录",
  "severity": "MINOR",
  "referenceClause": "4.14.2",
  "auditorId": 102,
  "findingDate": "2023-09-12T10:30:00Z",
  "targetCompletionDate": "2023-09-30T17:00:00Z"
}
```

**响应：** 201 Created

## 9. 投诉管理API

### 9.1 投诉列表

```
GET /api/v1/complaints
```

**查询参数：**
- code: 投诉编号
- title: 投诉标题
- status: 投诉状态
- priority: 优先级
- productId: 产品ID
- customerName: 客户名称

**响应：** 200 OK（分页格式）

### 9.2 创建投诉记录

```
POST /api/v1/complaints
```

**请求体：**

```json
{
  "title": "产品外观缺陷投诉",
  "description": "客户反馈产品包装有破损",
  "source": "EMAIL",
  "priority": "HIGH",
  "customerName": "ABC公司",
  "customerContact": "contact@abc.com",
  "productId": "PROD-002",
  "batchNumber": "BATCH-2023-0005",
  "receivedDate": "2023-08-01T14:30:00Z"
}
```

**响应：** 201 Created

## 10. 文件上传下载API

### 10.1 上传文件

```
POST /api/v1/attachments
```

**请求体：** multipart/form-data

- file: 文件内容
- entityType: 关联实体类型（如deviation, capa, changeControl）
- entityId: 关联实体ID

**响应：** 201 Created

```json
{
  "id": 201,
  "name": "附件名称.pdf",
  "contentType": "application/pdf",
  "fileSize": 102400,
  "uploadedBy": {
    "id": 101,
    "username": "zhangwei",
    "fullName": "张伟"
  },
  "uploadedAt": "2023-08-10T15:30:00Z"
}
```

### 10.2 下载文件

```
GET /api/v1/attachments/{id}/download
```

**响应：** 文件流，Content-Type设置为文件的MIME类型

## 11. 错误处理

### 11.1 错误码定义

| 错误码 | HTTP状态码 | 描述 |
|-------|------------|------|
| ERR_001 | 400 | 请求参数错误 |
| ERR_002 | 401 | 未授权访问 |
| ERR_003 | 403 | 权限不足 |
| ERR_004 | 404 | 资源不存在 |
| ERR_005 | 409 | 资源冲突 |
| ERR_006 | 422 | 业务规则验证失败 |
| ERR_500 | 500 | 服务器内部错误 |
| ERR_501 | 501 | 功能未实现 |

### 11.2 自定义异常处理

```java
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.NOT_FOUND.value(),
            "Not Found",
            ex.getMessage(),
            getCurrentRequestUri()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(ValidationException ex) {
        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            "Validation Error",
            ex.getMessage(),
            getCurrentRequestUri()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    
    // 其他异常处理方法...
}
```

## 12. API版本控制

系统采用URL路径版本化策略，如`/api/v1/deviations`。当API发生破坏性变更时，将创建新版本API，如`/api/v2/deviations`，同时保留旧版本API以确保向后兼容性。

## 13. 接口限流

为防止API滥用，系统实现了基于令牌桶算法的接口限流机制：

```java
@Configuration
public class RateLimiterConfig {
    
    @Bean
    public KeyResolver userKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getHeaders().getFirst("Authorization"));
    }
    
    @Bean
    public RedisRateLimiter redisRateLimiter() {
        return new RedisRateLimiter(10, 20); // 每秒允许10个请求，令牌桶容量20
    }
}
```

## 14. 文档历史

| 版本 | 修改日期 | 修改人 | 修改内容 |
|------|----------|--------|----------|
| 1.0  | 2023-08-01 | QMS团队 | 初始版本 |
| 1.1  | 2023-08-08 | QMS团队 | 增加变更控制和审计管理API |
| 1.2  | 2023-08-15 | QMS团队 | 更新错误处理和安全认证章节 |

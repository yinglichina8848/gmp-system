# GMP环境下人事管理子系统的设计与实现
## 系统设计

## 📋 文档信息

| 属性 | 值 |
|------|---|
| 文档标题 | GMP环境下人事管理子系统的设计与实现_系统设计 |
| 版本号 | v0.1.0-draft |
| 创建日期 | 2025年11月20日 |
| 更新日期 | 2025年11月20日 |
| 作者 | 毕业设计团队 |
| 状态 | 草稿 |

## 1️⃣ 系统设计原则

### 1.1 GMP合规性原则

GMP环境下的人事管理子系统设计必须首先确保系统的GMP合规性，具体原则包括：

1. **数据完整性**：确保所有与GMP相关的人员管理数据真实、准确、完整、一致和可追溯
2. **权限控制**：实施严格的访问控制和权限管理，确保只有授权人员才能访问和操作系统
3. **审计追踪**：记录所有关键操作和数据变更，提供完整的审计追踪功能
4. **验证支持**：系统设计支持计算机化系统验证（CSV）
5. **生命周期管理**：支持系统的全生命周期管理，包括设计、开发、测试、部署、维护和退役

### 1.2 架构设计原则

在系统架构设计方面，我们遵循以下原则：

1. **微服务架构**：采用微服务架构，将系统拆分为多个独立的服务，便于独立开发、测试、部署和扩展
2. **模块化设计**：系统采用模块化设计，每个模块具有清晰的边界和职责
3. **松耦合高内聚**：服务之间通过API进行通信，保持松耦合，内部实现高内聚
4. **可扩展性**：系统设计支持水平扩展，能够根据业务需求扩展系统容量
5. **高可用性**：系统设计支持高可用性，减少系统宕机时间
6. **安全性**：系统设计考虑安全性，包括身份认证、授权、数据加密等

### 1.3 用户体验原则

在用户体验设计方面，我们遵循以下原则：

1. **简洁明了**：界面设计简洁明了，符合用户习惯
2. **一致性**：提供统一的操作风格和视觉体验
3. **高效便捷**：操作流程简单直观，支持批量操作和快捷操作
4. **响应迅速**：系统响应迅速，提供良好的交互体验
5. **可访问性**：系统设计考虑不同用户的需求，提供良好的可访问性

## 2️⃣ 系统架构设计

### 2.1 总体架构

GMP环境下人事管理子系统采用分层架构设计，从下到上依次为：基础设施层、数据访问层、业务服务层、API网关层和前端展示层。

**系统总体架构图**：

```
+--------------------------------------------+
|               前端展示层                    |
|  (React + Ant Design + Redux)              |
+--------------------------------------------+
|               API网关层                    |
| (Spring Cloud Gateway + OAuth2)            |
+--------------------------------------------+
|               业务服务层                    |
|  +---------------+  +------------------+   |
|  | 员工管理服务   |  |  培训管理服务    |   |
|  +---------------+  +------------------+   |
|  +---------------+  +------------------+   |
|  | 考勤管理服务   |  | 资质证书管理服务  |   |
|  +---------------+  +------------------+   |
|  +---------------+  +------------------+   |
|  | 组织架构服务   |  | GMP合规管理服务  |   |
|  +---------------+  +------------------+   |
|  +---------------+  +------------------+   |
|  | 系统管理服务   |  |  通知服务        |   |
|  +---------------+  +------------------+   |
+--------------------------------------------+
|               数据访问层                    |
|  (MyBatis + Redis + Elasticsearch)         |
+--------------------------------------------+
|               基础设施层                    |
| +---------+  +----------+  +------------+  |
| |  MySQL  |  |  Redis   |  | 消息队列    |  |
| +---------+  +----------+  +------------+  |
| +--------------+  +------------+            |
| | 分布式日志系统 |  | 服务注册发现 |          |
| +--------------+  +------------+            |
+--------------------------------------------+
```

各层的主要职责如下：

1. **基础设施层**：提供系统运行所需的基础设施，包括数据库、缓存、消息队列、分布式日志系统、服务注册发现等

2. **数据访问层**：负责与数据库和缓存的交互，提供统一的数据访问接口

3. **业务服务层**：实现系统的核心业务逻辑，包括员工管理、培训管理、考勤管理、资质证书管理、组织架构管理、GMP合规管理和系统管理等服务

4. **API网关层**：负责请求路由、权限验证、负载均衡、限流等功能，提供统一的API入口

5. **前端展示层**：提供用户界面，与用户进行交互，展示系统信息和接收用户操作

### 2.2 微服务架构设计

系统采用微服务架构，将业务功能拆分为多个独立的服务。每个服务都有自己的数据库，通过API进行通信。

**微服务划分**：

1. **员工管理服务（employee-service）**：
   - 职责：管理员工基础信息、岗位管理、合同管理、离职管理等
   - 技术栈：Spring Boot + MyBatis + MySQL

2. **培训管理服务（training-service）**：
   - 职责：管理培训课程、培训计划、培训记录、培训效果评估等
   - 技术栈：Spring Boot + MyBatis + MySQL

3. **考勤管理服务（attendance-service）**：
   - 职责：管理打卡数据、考勤异常处理、考勤统计分析、GMP活动关联等
   - 技术栈：Spring Boot + MyBatis + MySQL

4. **资质证书管理服务（certificate-service）**：
   - 职责：管理证书类型、证书信息、有效期管理、统计分析等
   - 技术栈：Spring Boot + MyBatis + MySQL

5. **组织架构服务（organization-service）**：
   - 职责：管理部门、岗位、组织结构图、岗位编制等
   - 技术栈：Spring Boot + MyBatis + MySQL

6. **GMP合规管理服务（gmp-compliance-service）**：
   - 职责：管理资质验证、动态权限控制、审计日志、合规性报告等
   - 技术栈：Spring Boot + MyBatis + MySQL + Elasticsearch

7. **系统管理服务（system-service）**：
   - 职责：管理用户、角色、权限、系统配置、日志等
   - 技术栈：Spring Boot + MyBatis + MySQL

8. **通知服务（notification-service）**：
   - 职责：提供邮件、短信、站内信等通知功能
   - 技术栈：Spring Boot + RabbitMQ

**服务间通信**：

- **同步通信**：使用RESTful API进行同步通信，基于HTTP协议
- **异步通信**：使用消息队列进行异步通信，如RabbitMQ、Kafka等
- **服务注册与发现**：使用Eureka或Consul进行服务注册与发现
- **配置管理**：使用Spring Cloud Config进行分布式配置管理

### 2.3 技术栈选择

| 类别 | 技术/框架 | 版本 | 选型理由 |
|------|-----------|------|----------|
| 后端框架 | Spring Boot | 2.7.x | 轻量级、快速开发、自动配置 |
| 微服务框架 | Spring Cloud | 2021.x | 提供完整的微服务解决方案 |
| API网关 | Spring Cloud Gateway | 3.x | 性能优异、功能丰富的API网关 |
| 服务注册发现 | Nacos | 2.x | 阿里开源、功能强大、性能优异 |
| 配置中心 | Nacos Config | 2.x | 与Nacos集成、支持动态配置 |
| 断路器 | Resilience4j | 1.7.x | 轻量级、功能全面的断路器库 |
| 分布式追踪 | SkyWalking | 8.x | 开源、高性能的分布式追踪系统 |
| ORM框架 | MyBatis-Plus | 3.5.x | 简化MyBatis开发、功能增强 |
| 数据库 | MySQL | 8.0.x | 成熟稳定、性能优异的关系型数据库 |
| 缓存 | Redis | 6.x | 高性能的内存数据库、支持多种数据结构 |
| 搜索引擎 | Elasticsearch | 7.x | 高性能的全文搜索引擎 |
| 消息队列 | RabbitMQ | 3.8.x | 可靠的消息队列、支持多种协议 |
| 认证授权 | Spring Security + JWT | 5.x | 强大的安全框架、无状态认证 |
| 前端框架 | React | 18.x | 组件化、虚拟DOM、性能优异 |
| UI组件库 | Ant Design | 5.x | 企业级UI组件库、设计规范统一 |
| 状态管理 | Redux Toolkit | 1.8.x | 简化Redux开发、性能优化 |
| 路由 | React Router | 6.x | 功能完善的React路由库 |
| HTTP客户端 | Axios | 0.27.x | 易用的HTTP客户端库 |
| 构建工具 | Webpack | 5.x | 强大的模块打包工具 |
| 容器化 | Docker | 20.x | 轻量级容器技术、简化部署 |
| 容器编排 | Kubernetes | 1.23.x | 强大的容器编排平台 |

## 3️⃣ 数据模型设计

### 3.1 实体关系模型

系统的核心实体包括：员工、部门、岗位、考勤记录、培训课程、培训记录、资质证书、GMP活动、用户、角色、权限和审计日志等。这些实体之间存在复杂的关联关系。

**主要实体关系**：

1. **员工（Employee）**
   - 与部门（Department）：多对一关系，一个员工属于一个部门
   - 与岗位（Position）：多对一关系，一个员工担任一个岗位
   - 与考勤记录（AttendanceRecord）：一对多关系，一个员工有多个考勤记录
   - 与培训记录（TrainingRecord）：一对多关系，一个员工有多个培训记录
   - 与资质证书（QualificationCertificate）：一对多关系，一个员工有多个证书
   - 与GMP活动（GMPActivity）：多对多关系，一个员工参与多个GMP活动

2. **部门（Department）**
   - 与部门（Department）：自关联，支持多级部门结构
   - 与岗位（Position）：一对多关系，一个部门有多个岗位

3. **岗位（Position）**
   - 与培训课程（TrainingCourse）：多对多关系，一个岗位需要多个培训课程
   - 与资质证书（QualificationCertificate）：多对多关系，一个岗位需要多个资质证书

4. **用户（User）**
   - 与员工（Employee）：一对一关系，一个用户对应一个员工
   - 与角色（Role）：多对多关系，一个用户有多个角色

5. **角色（Role）**
   - 与权限（Permission）：多对多关系，一个角色有多个权限

6. **审计日志（AuditLog）**
   - 与用户（User）：多对一关系，一个审计日志对应一个操作用户

### 3.2 数据库表结构设计

以下是系统的主要数据库表结构设计：

#### 3.2.1 员工表（employee）

| 字段名 | 数据类型 | 约束 | 描述 |
|--------|----------|------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 员工ID |
| employee_code | VARCHAR(50) | UNIQUE, NOT NULL | 员工工号 |
| name | VARCHAR(100) | NOT NULL | 姓名 |
| gender | VARCHAR(10) | NOT NULL | 性别 |
| birth_date | DATE | NOT NULL | 出生日期 |
| id_card_no | VARCHAR(20) | UNIQUE, NOT NULL | 身份证号 |
| phone_number | VARCHAR(20) | UNIQUE, NOT NULL | 手机号 |
| email | VARCHAR(100) | UNIQUE | 邮箱 |
| entry_date | DATE | NOT NULL | 入职日期 |
| status | VARCHAR(20) | NOT NULL | 状态（在职/离职/试用等） |
| department_id | BIGINT | FOREIGN KEY | 部门ID |
| position_id | BIGINT | FOREIGN KEY | 岗位ID |
| created_by | VARCHAR(50) | NOT NULL | 创建人 |
| created_time | DATETIME | NOT NULL | 创建时间 |
| updated_by | VARCHAR(50) | NOT NULL | 更新人 |
| updated_time | DATETIME | NOT NULL | 更新时间 |
| version | INT | NOT NULL, DEFAULT 1 | 版本号 |

#### 3.2.2 部门表（department）

| 字段名 | 数据类型 | 约束 | 描述 |
|--------|----------|------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 部门ID |
| department_code | VARCHAR(50) | UNIQUE, NOT NULL | 部门编码 |
| department_name | VARCHAR(100) | NOT NULL | 部门名称 |
| parent_id | BIGINT | FOREIGN KEY | 父部门ID |
| level | INT | NOT NULL | 部门级别 |
| description | VARCHAR(500) | | 部门描述 |
| gmp_area | VARCHAR(100) | | GMP功能区域 |
| created_by | VARCHAR(50) | NOT NULL | 创建人 |
| created_time | DATETIME | NOT NULL | 创建时间 |
| updated_by | VARCHAR(50) | NOT NULL | 更新人 |
| updated_time | DATETIME | NOT NULL | 更新时间 |

#### 3.2.3 岗位表（position）

| 字段名 | 数据类型 | 约束 | 描述 |
|--------|----------|------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 岗位ID |
| position_code | VARCHAR(50) | UNIQUE, NOT NULL | 岗位编码 |
| position_name | VARCHAR(100) | NOT NULL | 岗位名称 |
| department_id | BIGINT | FOREIGN KEY | 部门ID |
| responsibilities | TEXT | | 岗位职责 |
| requirements | TEXT | | 岗位要求 |
| gmp_responsibilities | TEXT | | GMP职责 |
| headcount | INT | NOT NULL, DEFAULT 1 | 编制人数 |
| created_by | VARCHAR(50) | NOT NULL | 创建人 |
| created_time | DATETIME | NOT NULL | 创建时间 |
| updated_by | VARCHAR(50) | NOT NULL | 更新人 |
| updated_time | DATETIME | NOT NULL | 更新时间 |

#### 3.2.4 考勤记录表（attendance_record）

| 字段名 | 数据类型 | 约束 | 描述 |
|--------|----------|------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 记录ID |
| employee_id | BIGINT | FOREIGN KEY, NOT NULL | 员工ID |
| check_date | DATE | NOT NULL | 考勤日期 |
| check_in_time | DATETIME | | 签到时间 |
| check_out_time | DATETIME | | 签退时间 |
| work_hours | DECIMAL(5,2) | | 工作时长 |
| status | VARCHAR(20) | NOT NULL | 状态（正常/迟到/早退/缺勤等） |
| gmp_activity_id | BIGINT | FOREIGN KEY | GMP活动ID |
| remark | VARCHAR(500) | | 备注 |
| created_by | VARCHAR(50) | NOT NULL | 创建人 |
| created_time | DATETIME | NOT NULL | 创建时间 |
| updated_by | VARCHAR(50) | NOT NULL | 更新人 |
| updated_time | DATETIME | NOT NULL | 更新时间 |

#### 3.2.5 培训课程表（training_course）

| 字段名 | 数据类型 | 约束 | 描述 |
|--------|----------|------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 课程ID |
| course_code | VARCHAR(50) | UNIQUE, NOT NULL | 课程编码 |
| course_name | VARCHAR(200) | NOT NULL | 课程名称 |
| course_type | VARCHAR(50) | NOT NULL | 课程类型 |
| duration | INT | NOT NULL | 课程时长（小时） |
| content | TEXT | | 课程内容 |
| trainer | VARCHAR(100) | | 讲师 |
| is_gmp_required | BOOLEAN | NOT NULL, DEFAULT FALSE | 是否GMP必修 |
| created_by | VARCHAR(50) | NOT NULL | 创建人 |
| created_time | DATETIME | NOT NULL | 创建时间 |
| updated_by | VARCHAR(50) | NOT NULL | 更新人 |
| updated_time | DATETIME | NOT NULL | 更新时间 |

#### 3.2.6 培训记录表（training_record）

| 字段名 | 数据类型 | 约束 | 描述 |
|--------|----------|------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 记录ID |
| employee_id | BIGINT | FOREIGN KEY, NOT NULL | 员工ID |
| course_id | BIGINT | FOREIGN KEY, NOT NULL | 课程ID |
| training_date | DATE | NOT NULL | 培训日期 |
| score | DECIMAL(5,2) | | 考核成绩 |
| result | VARCHAR(20) | NOT NULL | 培训结果（通过/不通过） |
| evaluation | TEXT | | 培训评估 |
| certificate_no | VARCHAR(100) | | 培训证书编号 |
| created_by | VARCHAR(50) | NOT NULL | 创建人 |
| created_time | DATETIME | NOT NULL | 创建时间 |
| updated_by | VARCHAR(50) | NOT NULL | 更新人 |
| updated_time | DATETIME | NOT NULL | 更新时间 |

#### 3.2.7 资质证书表（qualification_certificate）

| 字段名 | 数据类型 | 约束 | 描述 |
|--------|----------|------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 证书ID |
| employee_id | BIGINT | FOREIGN KEY, NOT NULL | 员工ID |
| certificate_type | VARCHAR(100) | NOT NULL | 证书类型 |
| certificate_name | VARCHAR(200) | NOT NULL | 证书名称 |
| certificate_no | VARCHAR(100) | NOT NULL | 证书编号 |
| issuing_authority | VARCHAR(200) | NOT NULL | 颁发机构 |
| issue_date | DATE | NOT NULL | 颁发日期 |
| expiry_date | DATE | | 有效期至 |
| status | VARCHAR(20) | NOT NULL | 状态（有效/过期/复审中） |
| file_path | VARCHAR(500) | | 证书文件路径 |
| created_by | VARCHAR(50) | NOT NULL | 创建人 |
| created_time | DATETIME | NOT NULL | 创建时间 |
| updated_by | VARCHAR(50) | NOT NULL | 更新人 |
| updated_time | DATETIME | NOT NULL | 更新时间 |

#### 3.2.8 GMP活动表（gmp_activity）

| 字段名 | 数据类型 | 约束 | 描述 |
|--------|----------|------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 活动ID |
| activity_code | VARCHAR(50) | UNIQUE, NOT NULL | 活动编码 |
| activity_name | VARCHAR(200) | NOT NULL | 活动名称 |
| activity_type | VARCHAR(50) | NOT NULL | 活动类型 |
| start_time | DATETIME | NOT NULL | 开始时间 |
| end_time | DATETIME | NOT NULL | 结束时间 |
| location | VARCHAR(200) | | 活动地点 |
| description | TEXT | | 活动描述 |
| created_by | VARCHAR(50) | NOT NULL | 创建人 |
| created_time | DATETIME | NOT NULL | 创建时间 |
| updated_by | VARCHAR(50) | NOT NULL | 更新人 |
| updated_time | DATETIME | NOT NULL | 更新时间 |

#### 3.2.9 用户表（user）

| 字段名 | 数据类型 | 约束 | 描述 |
|--------|----------|------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 用户ID |
| username | VARCHAR(50) | UNIQUE, NOT NULL | 用户名 |
| password | VARCHAR(255) | NOT NULL | 密码（加密存储） |
| employee_id | BIGINT | UNIQUE, FOREIGN KEY | 员工ID |
| status | VARCHAR(20) | NOT NULL | 状态（启用/禁用） |
| last_login_time | DATETIME | | 最后登录时间 |
| created_by | VARCHAR(50) | NOT NULL | 创建人 |
| created_time | DATETIME | NOT NULL | 创建时间 |
| updated_by | VARCHAR(50) | NOT NULL | 更新人 |
| updated_time | DATETIME | NOT NULL | 更新时间 |

#### 3.2.10 角色表（role）

| 字段名 | 数据类型 | 约束 | 描述 |
|--------|----------|------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 角色ID |
| role_code | VARCHAR(50) | UNIQUE, NOT NULL | 角色编码 |
| role_name | VARCHAR(100) | NOT NULL | 角色名称 |
| description | VARCHAR(500) | | 角色描述 |
| created_by | VARCHAR(50) | NOT NULL | 创建人 |
| created_time | DATETIME | NOT NULL | 创建时间 |
| updated_by | VARCHAR(50) | NOT NULL | 更新人 |
| updated_time | DATETIME | NOT NULL | 更新时间 |

#### 3.2.11 权限表（permission）

| 字段名 | 数据类型 | 约束 | 描述 |
|--------|----------|------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 权限ID |
| permission_code | VARCHAR(50) | UNIQUE, NOT NULL | 权限编码 |
| permission_name | VARCHAR(100) | NOT NULL | 权限名称 |
| permission_type | VARCHAR(20) | NOT NULL | 权限类型 |
| resource_id | VARCHAR(100) | | 资源ID |
| description | VARCHAR(500) | | 权限描述 |
| created_by | VARCHAR(50) | NOT NULL | 创建人 |
| created_time | DATETIME | NOT NULL | 创建时间 |
| updated_by | VARCHAR(50) | NOT NULL | 更新人 |
| updated_time | DATETIME | NOT NULL | 更新时间 |

#### 3.2.12 审计日志表（audit_log）

| 字段名 | 数据类型 | 约束 | 描述 |
|--------|----------|------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 日志ID |
| user_id | BIGINT | FOREIGN KEY | 用户ID |
| username | VARCHAR(50) | NOT NULL | 用户名 |
| operation_time | DATETIME | NOT NULL | 操作时间 |
| ip_address | VARCHAR(50) | NOT NULL | IP地址 |
| operation_type | VARCHAR(50) | NOT NULL | 操作类型 |
| module_name | VARCHAR(100) | NOT NULL | 模块名称 |
| business_id | VARCHAR(100) | | 业务ID |
| old_value | TEXT | | 操作前值 |
| new_value | TEXT | | 操作后值 |
| operation_result | VARCHAR(20) | NOT NULL | 操作结果（成功/失败） |
| error_message | TEXT | | 错误信息 |

### 3.3 数据访问层设计

数据访问层负责与数据库和缓存的交互，提供统一的数据访问接口。我们使用MyBatis-Plus作为ORM框架，简化数据库操作。

**数据访问层设计要点**：

1. **Repository模式**：采用Repository模式，将数据访问逻辑封装在Repository接口中

2. **BaseRepository**：提供通用的数据访问方法，如增删改查、分页查询等

3. **自定义查询**：支持通过注解或XML配置自定义SQL查询

4. **缓存集成**：集成Redis缓存，提高查询性能

5. **事务管理**：支持声明式事务管理，确保数据一致性

6. **动态SQL**：支持动态SQL构建，适应复杂查询需求

## 4️⃣ 核心功能模块设计

### 4.1 组织架构管理模块

组织架构管理模块负责管理企业的部门结构、岗位设置和汇报关系，是人事管理的基础。

**模块架构**：

```
organization-service
├── controller
│   ├── DepartmentController.java
│   ├── PositionController.java
│   └── OrganizationChartController.java
├── service
│   ├── DepartmentService.java
│   ├── PositionService.java
│   └── OrganizationChartService.java
├── repository
│   ├── DepartmentRepository.java
│   └── PositionRepository.java
├── model
│   ├── Department.java
│   ├── Position.java
│   └── dto
│       ├── DepartmentDTO.java
│       └── PositionDTO.java
└── config
    └── SwaggerConfig.java
```

**关键功能实现**：

1. **部门管理**：
   - 支持树形结构的部门管理，包括新增、修改、删除、查询部门
   - 支持部门与GMP功能区域的关联
   - 实现部门移动、合并等高级功能

2. **岗位管理**：
   - 支持岗位的新增、修改、删除、查询
   - 支持岗位与部门的关联
   - 支持岗位与GMP职责的关联
   - 实现岗位权限模板管理

3. **组织结构图**：
   - 提供可视化的组织结构图展示
   - 支持按部门、岗位等维度查看
   - 支持导出组织结构图

### 4.2 员工信息管理模块

员工信息管理模块负责管理员工的基本信息、职业发展、人事异动等，是人事管理的核心。

**模块架构**：

```
employee-service
├── controller
│   ├── EmployeeController.java
│   ├── ContractController.java
│   └── TransferController.java
├── service
│   ├── EmployeeService.java
│   ├── ContractService.java
│   └── TransferService.java
├── repository
│   ├── EmployeeRepository.java
│   ├── ContractRepository.java
│   └── TransferRecordRepository.java
├── model
│   ├── Employee.java
│   ├── Contract.java
│   ├── TransferRecord.java
│   └── dto
│       ├── EmployeeDTO.java
│       └── ContractDTO.java
└── config
    └── SwaggerConfig.java
```

**关键功能实现**：

1. **员工基础信息管理**：
   - 支持员工信息的录入、修改、查询和维护
   - 实现员工档案的导入导出
   - 支持员工照片管理

2. **员工岗位管理**：
   - 支持员工与岗位的关联
   - 实现员工调岗、晋升、降职等人事异动管理
   - 记录员工岗位变动历史

3. **员工合同管理**：
   - 支持劳动合同的管理，包括签订、续签、变更、解除
   - 实现合同到期提醒
   - 支持合同文件管理

4. **员工离职管理**：
   - 支持离职流程管理
   - 实现离职交接管理
   - 自动处理离职员工权限撤销

### 4.3 考勤管理模块

考勤管理模块负责记录员工的出勤情况，并支持与GMP生产活动关联，实现生产活动的人员参与可追溯。

**模块架构**：

```
attendance-service
├── controller
│   ├── AttendanceController.java
│   ├── LeaveController.java
│   └── GmpActivityAttendanceController.java
├── service
│   ├── AttendanceService.java
│   ├── LeaveService.java
│   └── GmpActivityAttendanceService.java
├── repository
│   ├── AttendanceRecordRepository.java
│   ├── LeaveRecordRepository.java
│   └── GmpActivityAttendanceRepository.java
├── model
│   ├── AttendanceRecord.java
│   ├── LeaveRecord.java
│   └── dto
│       ├── AttendanceDTO.java
│       └── LeaveDTO.java
└── config
    └── SwaggerConfig.java
```

**关键功能实现**：

1. **打卡管理**：
   - 支持多种打卡方式的数据接入
   - 实现打卡数据的自动处理和异常检测
   - 支持自定义考勤规则配置

2. **考勤异常处理**：
   - 支持加班、请假、调休等申请和审批流程
   - 实现异常考勤的申诉和处理

3. **考勤统计分析**：
   - 实现多维度的考勤统计和报表生成
   - 支持考勤数据的趋势分析

4. **GMP活动关联**：
   - 实现考勤记录与GMP生产活动的关联
   - 支持GMP活动参与人员的追溯查询

### 4.4 培训管理模块

培训管理模块负责管理培训课程、培训计划、培训记录等，确保员工具备必要的知识和技能，能够胜任本职工作。

**模块架构**：

```
training-service
├── controller
│   ├── CourseController.java
│   ├── TrainingPlanController.java
│   └── TrainingRecordController.java
├── service
│   ├── CourseService.java
│   ├── TrainingPlanService.java
│   └── TrainingRecordService.java
├── repository
│   ├── TrainingCourseRepository.java
│   ├── TrainingPlanRepository.java
│   └── TrainingRecordRepository.java
├── model
│   ├── TrainingCourse.java
│   ├── TrainingPlan.java
│   ├── TrainingRecord.java
│   └── dto
│       ├── CourseDTO.java
│       └── TrainingRecordDTO.java
└── config
    └── SwaggerConfig.java
```

**关键功能实现**：

1. **培训课程管理**：
   - 支持培训课程的创建和管理
   - 实现课程分类和课程材料管理

2. **培训计划管理**：
   - 支持年度培训计划的制定和管理
   - 实现基于岗位、资质的自动培训需求分析
   - 支持培训计划的执行跟踪

3. **培训记录管理**：
   - 记录员工培训参与情况和考核结果
   - 实现培训签到和效果评估
   - 支持培训证书生成和管理

4. **培训提醒**：
   - 实现培训计划提醒
   - 支持培训到期预警

### 4.5 资质证书管理模块

资质证书管理模块负责管理员工的专业资质证书，确保员工具备必要的专业资质，能够从事特定的工作。

**模块架构**：

```
certificate-service
├── controller
│   ├── CertificateTypeController.java
│   └── CertificateController.java
├── service
│   ├── CertificateTypeService.java
│   └── CertificateService.java
├── repository
│   ├── CertificateTypeRepository.java
│   └── QualificationCertificateRepository.java
├── model
│   ├── CertificateType.java
│   ├── QualificationCertificate.java
│   └── dto
│       ├── CertificateTypeDTO.java
│       └── CertificateDTO.java
└── config
    └── SwaggerConfig.java
```

**关键功能实现**：

1. **证书类型管理**：
   - 支持证书类型的创建和管理
   - 实现证书有效期、复审周期等属性设置

2. **证书信息管理**：
   - 记录员工证书信息和扫描件
   - 支持证书信息的录入、修改、查询

3. **证书有效期管理**：
   - 实现证书有效期跟踪和到期提醒
   - 支持证书延期、复审管理
   - 当证书过期时，触发权限调整事件

4. **证书统计分析**：
   - 实现证书持有情况统计和分析
   - 支持证书到期预警报告

### 4.6 GMP合规性管理模块

GMP合规性管理模块是本系统的核心特色，负责实现基于资质和培训的动态权限控制，记录关键操作审计日志，支持合规性报告生成。

**模块架构**：

```
gmp-compliance-service
├── controller
│   ├── QualificationVerificationController.java
│   ├── DynamicPermissionController.java
│   ├── AuditLogController.java
│   └── ComplianceReportController.java
├── service
│   ├── QualificationVerificationService.java
│   ├── DynamicPermissionService.java
│   ├── AuditLogService.java
│   └── ComplianceReportService.java
├── repository
│   ├── AuditLogRepository.java
│   └── ComplianceCheckRepository.java
├── model
│   ├── AuditLog.java
│   ├── ComplianceCheck.java
│   └── dto
│       ├── AuditLogDTO.java
│       └── ComplianceReportDTO.java
├── aspect
│   └── AuditLogAspect.java
└── config
    └── SwaggerConfig.java
```

**关键功能实现**：

1. **资质验证**：
   - 实现员工资质的自动验证
   - 支持关键岗位人员资质的特殊验证

2. **动态权限管理**：
   - 基于员工资质和培训记录，实现动态权限控制
   - 当资质过期或培训未完成时，自动调整权限
   - 支持权限变更审批流程

3. **审计日志管理**：
   - 通过AOP实现无侵入的审计日志记录
   - 支持审计日志的查询、导出和分析
   - 确保审计日志的完整性和不可篡改性

4. **合规性报告**：
   - 支持生成符合GMP要求的各类报告
   - 实现报告的自定义和导出

5. **合规性检查**：
   - 定期进行合规性检查
   - 支持问题的跟踪和整改

## 5️⃣ 系统安全设计

### 5.1 身份认证与授权设计

系统采用Spring Security + JWT实现身份认证和授权，确保只有授权用户才能访问系统。

**身份认证流程**：

1. 用户通过用户名和密码登录系统
2. 系统验证用户身份，生成JWT令牌
3. 客户端在后续请求中携带JWT令牌
4. 系统验证JWT令牌的有效性，确认用户身份

**授权机制**：

1. 采用RBAC授权模型，基于角色和权限进行访问控制
2. 实现细粒度的权限控制，包括功能权限、数据权限等
3. 结合GMP要求，实现基于资质和培训的动态权限控制

### 5.2 数据安全设计

系统采用多种技术确保数据安全，包括数据加密、访问控制、数据备份等。

**数据加密**：

1. 敏感数据（如身份证号、手机号等）加密存储
2. 密码采用BCrypt等安全算法加密
3. 数据传输采用HTTPS加密

**访问控制**：

1. 实施严格的访问控制策略
2. 支持敏感操作的双人授权
3. 定期审查用户权限

**数据备份与恢复**：

1. 数据库每日全量备份
2. 关键数据实时增量备份
3. 备份数据异地存储
4. 定期进行数据恢复演练

### 5.3 审计日志设计

系统实现完善的审计日志功能，记录所有关键操作和数据变更，确保操作的可追溯性。

**审计日志内容**：

1. 操作人信息（用户ID、用户名）
2. 操作时间
3. 操作类型
4. 操作模块
5. 业务ID
6. 操作前后值
7. 操作结果
8. IP地址

**审计日志存储**：

1. 审计日志存储在独立的表中
2. 支持审计日志的归档和清理
3. 确保审计日志的完整性和不可篡改性

## 6️⃣ 本章小结

本章详细介绍了GMP环境下人事管理子系统的设计，包括系统设计原则、系统架构设计、数据模型设计、核心功能模块设计和系统安全设计。

系统采用微服务架构，将业务功能拆分为多个独立的服务，便于独立开发、测试、部署和扩展。数据模型设计充分考虑了GMP对人员管理的特殊要求，支持员工、部门、岗位、培训、资质证书等核心实体的管理。核心功能模块设计详细阐述了各模块的架构和关键功能实现，特别是GMP合规性管理模块，实现了基于资质和培训的动态权限控制、操作审计追踪等特色功能。

在系统安全设计方面，采用Spring Security + JWT实现身份认证和授权，结合RBAC模型和动态权限控制，确保系统的安全性和GMP合规性。同时，通过数据加密、访问控制、数据备份等技术，确保数据的安全和完整性。

系统设计充分考虑了GMP合规要求，为后续的系统实现奠定了坚实的基础。在系统实现过程中，我们将严格按照设计文档进行开发，确保系统能够满足GMP环境下人事管理的特殊需求。

---

*文档版本：v0.1.0-draft*
*审核状态：待审核*
*下次更新：根据设计调整*
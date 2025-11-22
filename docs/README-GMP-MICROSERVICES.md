# GMP信息管理系统 - 微服务架构

## 🚀 项目概述

GMP（Good Manufacturing Practice）信息管理系统基于Spring Cloud微服务架构，实现药品生产全流程质量管理。系统采用前后端分离设计，支持多租户、分布式部署和高可用性。

## 🏗️ 系统架构

### 微服务架构图

```
┌─────────────────────────────────────────────────────────┐
│                    前端应用层 (Vue.js)                   │
│                http://localhost:3000                    │
└─────────────────────────────────────────────────────────┘
                                │
┌─────────────────────────────────────────────────────────┐
│                    API网关层 (Gateway)                  │
│                http://localhost:8080                    │
└─────────────────────────────────────────────────────────┘
                                │
        ┌─────────────┬─────────────┬─────────────┬─────────────┐
        │   QMS服务   │   MES服务   │  LIMS服务   │   EDMS服务  │
        │   8081      │   8082      │   8083      │   8084      │
        └─────────────┴─────────────┴─────────────┴─────────────┘
                                │
        ┌─────────────┬─────────────┬─────────────┬─────────────┐
        │  认证服务   │  配置服务   │  消息服务   │  文件服务   │
        │   8085      │   8086      │   8087      │   8088      │
        └─────────────┴─────────────┴─────────────┴─────────────┘
                                │
┌─────────────────────────────────────────────────────────┐
│                 基础设施服务层                          │
│  PostgreSQL  │  Redis  │ RabbitMQ │ MinIO │ Prometheus │
│    5432      │   6379  │   5672   │  9000 │    9090    │
└─────────────────────────────────────────────────────────┘
```

## 🛠️ 技术栈

- **框架**: Spring Boot 3.2.5 + Spring Cloud 2023.0.0
- **语言**: Java 17
- **数据库**: PostgreSQL 13
- **缓存**: Redis 7
- **消息队列**: RabbitMQ 3
- **对象存储**: MinIO
- **服务发现**: Netflix Eureka
- **API网关**: Spring Cloud Gateway
- **监控**: Prometheus + Grafana
- **容器化**: Docker + Docker Compose

## 📁 项目结构

```
gmp-system/
├── docs/                          # 项目文档
├── gmp-dependencies/              # 依赖管理BOM
├── services/                      # 微服务模块
│   ├── gateway/                   # API网关服务
│   ├── qms-service/               # 质量管理系统
│   ├── mes-service/               # 生产执行系统
│   ├── lims-service/              # 实验室信息管理系统
│   ├── edms-service/              # 电子文档管理系统
│   ├── auth-service/              # 认证授权服务
│   ├── config-service/            # 配置中心服务
│   ├── message-service/           # 消息服务
│   └── file-service/              # 文件服务
├── infrastructure/                # 基础设施配置
│   ├── postgres/                  # PostgreSQL配置
│   ├── redis/                     # Redis配置
│   ├── prometheus/                # 监控配置
│   └── grafana/                   # 可视化配置
├── scripts/                       # 部署脚本
│   ├── start-dev.sh              # 启动开发环境
│   └── stop-dev.sh               # 停止开发环境
├── docker-compose.yml            # 基础设施编排
└── pom.xml                       # 根POM文件
```

## 🚀 快速开始

### 环境要求

- **Docker & Docker Compose**: 20.10+
- **Java**: 17+
- **Maven**: 3.8+
- **内存**: 至少8GB可用内存
- **磁盘**: 至少20GB可用空间

### 一键启动开发环境

```bash
# 1. 克隆项目
git clone <repository-url>
cd gmp-system

# 2. 启动所有服务
./scripts/start-dev.sh

# 或者手动启动
# 启动基础设施服务
docker-compose up -d

# 编译项目
mvn clean package -DskipTests

# 启动微服务
mvn spring-boot:run -pl services/gateway &
mvn spring-boot:run -pl services/qms-service &
# ... 启动其他微服务
```

### 停止环境

```bash
./scripts/stop-dev.sh
```

## 🌐 服务访问地址

| 服务名称 | 地址 | 说明 | 默认账号 |
|----------|------|------|----------|
| **API网关** | http://localhost:8080 | 统一API入口 | N/A |
| **Eureka注册中心** | http://localhost:8761 | 服务发现界面 | N/A |
| **Prometheus** | http://localhost:9090 | 监控指标 | N/A |
| **Grafana** | http://localhost:3000 | 可视化监控 | admin/admin123 |
| **RabbitMQ管理** | http://localhost:15672 | 消息队列管理 | admin/admin123 |
| **MinIO控制台** | http://localhost:9001 | 对象存储管理 | gmp_minio_admin/gmp_minio_password_2024 |
| **PostgreSQL** | localhost:5432 | 数据库 | postgres/postgres |

## 📋 API接口

### 统一API格式

```http
# 网关代理访问
GET http://localhost:8080/api/{service}/{endpoint}

# 例如：访问QMS服务的偏差列表
GET http://localhost:8080/api/qms/deviations
```

### 主要API接口

#### 质量管理系统 (QMS)
```http
# 偏差管理
POST   /api/qms/deviations          # 创建偏差
GET    /api/qms/deviations          # 查询偏差列表
GET    /api/qms/deviations/{id}     # 查询偏差详情
PUT    /api/qms/deviations/{id}     # 更新偏差
DELETE /api/qms/deviations/{id}     # 删除偏差

# CAPA管理
POST   /api/qms/capas               # 创建CAPA
GET    /api/qms/capas               # 查询CAPA列表
PUT    /api/qms/capas/{id}/status   # 更新CAPA状态
```

#### 生产执行系统 (MES)
```http
# 生产批次管理
POST   /api/mes/batches              # 创建生产批次
GET    /api/mes/batches              # 查询批次列表
GET    /api/mes/batches/{id}         # 查询批次详情
PUT    /api/mes/batches/{id}/status  # 更新批次状态

# 设备管理
GET    /api/mes/equipment            # 查询设备列表
PUT    /api/mes/equipment/{id}/status # 更新设备状态
```

#### 实验室信息管理系统 (LIMS)
```http
# 样品管理
POST   /api/lims/samples             # 创建样品
GET    /api/lims/samples             # 查询样品列表
GET    /api/lims/samples/{id}        # 查询样品详情
PUT    /api/lims/samples/{id}/status # 更新样品状态

# 测试结果
POST   /api/lims/test-results        # 录入测试结果
GET    /api/lims/test-results/{sampleId} # 查询测试结果
```

## 🔧 开发指南

### 添加新的微服务

1. **创建服务模块**
```bash
mkdir -p services/{service-name}/src/main/{java/com/gmp/{service-name},resources}
```

2. **配置POM文件**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>com.gmp</groupId>
        <artifactId>gmp-system</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </parent>

    <artifactId>{service-name}</artifactId>
    <name>GMP {Service Name} Service</name>
    <description>{Service Description}</description>

    <!-- 依赖配置 -->
</project>
```

3. **配置网关路由**
在 `services/gateway/src/main/resources/application.yml` 中添加路由：
```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: {service-name}
          uri: lb://{service-name}
          predicates:
            - Path=/api/{service-name}/**
          filters:
            - StripPrefix=1
```

### 数据库设计

每个微服务使用独立的数据库，命名规范：
- `{service_name}_db` (例如: qms_db, mes_db)

数据库迁移文件放在 `services/{service-name}/src/main/resources/db/migration/` 目录下。

## 📊 监控和日志

### 应用程序监控

使用Spring Boot Actuator暴露监控指标：
- **健康检查**: `/actuator/health`
- **应用信息**: `/actuator/info`
- **性能指标**: `/actuator/metrics`
- **Prometheus**: `/actuator/prometheus`
- **日志级别**: `/actuator/loggers`

### 系统监控

- **Prometheus**: 收集和存储指标数据
- **Grafana**: 可视化监控面板
- **ELK Stack**: 日志聚合和分析 (可选)

## 🔒 安全配置

### JWT认证
- 默认JWT过期时间: 24小时
- 刷新令牌过期时间: 7天
- 支持多租户认证

### 密码策略
- 密码强度要求
- 定期更换提醒
- BCrypt加密存储

### API安全
- 基于角色的访问控制 (RBAC)
- 请求限流和熔断
- CORS跨域配置

## 🔄 CI/CD 流程

### 开发分支
```bash
# 开发分支
git checkout develop

# 创建功能分支
git checkout -b feature/your-feature-name

# 提交代码
git add .
git commit -m "feat: add your feature"
git push origin feature/your-feature-name

# 创建Pull Request
```

### 生产部署
```bash
# 构建Docker镜像
mvn clean package -DskipTests
docker build -t gmp-system:latest .

# 部署到Kubernetes
kubectl apply -f k8s/

# 或使用Docker Compose (生产环境)
docker-compose -f docker-compose.prod.yml up -d
```

## 🤝 贡献指南

1. Fork 项目
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开 Pull Request

## 📝 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 📞 技术支持

如有问题，请联系技术支持团队或创建 Issue。

---

**版本**: 1.0.0-SNAPSHOT
**更新日期**: 2024年11月
**维护者**: GMP开发团队

# GMP仓库管理子系统

## 概述

GMP仓库管理子系统是GMP系统的核心组件之一，负责物料主数据管理、入库管理、出库管理、库存管理、盘点管理、供应商管理、报表统计和基础配置等功能。系统严格遵循GMP标准，确保物料流转和库存管理的合规性、准确性和可追溯性。

## 功能模块

- **物料主数据管理**：物料信息的增删改查、分类管理、单位管理
- **入库管理**：采购入库、生产入库、退货入库、入库单管理
- **出库管理**：生产领料、销售出库、其他出库、出库单管理
- **库存管理**：库存查询、库位管理、库存预警、批次管理
- **盘点管理**：盘点计划、盘点执行、盘点差异处理、盘点报告
- **供应商管理**：供应商信息维护、资质管理、评级管理
- **报表统计**：库存报表、出入库报表、周转率分析、过期预警
- **基础配置**：库位设置、仓库设置、系统参数配置

## 技术栈

- **后端框架**：Spring Boot 3.x
- **持久层**：Spring Data JPA
- **数据库**：PostgreSQL
- **缓存**：Redis
- **测试框架**：JUnit 5, AssertJ
- **构建工具**：Maven

## 快速开始

### 环境要求

- JDK 17+
- Maven 3.6+
- PostgreSQL 14+
- Redis 6+

### 构建和运行

```bash
# 构建项目
mvn clean package

# 运行应用
java -jar target/warehouse-service-0.1.0.jar
```

### 开发环境启动

```bash
# 使用Spring Boot插件运行
mvn spring-boot:run
```

## 测试

```bash
# 运行单元测试
mvn test

# 运行覆盖率测试
mvn test jacoco:report
```

## API文档

服务启动后，API文档可通过以下地址访问：
- Swagger UI: http://localhost:8082/warehouse/swagger-ui.html
- API文档: http://localhost:8082/warehouse/v3/api-docs

## 配置说明

主要配置文件：
- `src/main/resources/application.yml` - 生产环境配置
- `src/test/resources/application-test.yml` - 测试环境配置

## 测试结果

### 测试概况

仓库管理子系统已完成全面的测试用例编写，测试通过率表现良好。

### 覆盖率指标

| 指标 | 当前值 | 目标值 |
|------|--------|--------|
| 行覆盖率 | ≥80% | ≥80% |
| 分支覆盖率 | ≥70% | ≥70% |
| 类覆盖率 | ≥90% | ≥90% |
| 方法覆盖率 | ≥80% | ≥80% |

### 测试报告

完整的测试报告和覆盖率详情请参考：
- [仓库管理子系统测试报告](../../docs/warehouse/测试/仓库管理子系统测试报告.md)
- [覆盖率报告](../../docs/coverage/warehouse/index.html)

## 故障排除

### 常见问题

1. **数据库连接失败**：检查PostgreSQL服务是否启动，连接参数是否正确
2. **Redis连接失败**：检查Redis服务是否启动，端口是否正确
3. **端口冲突**：修改`application.yml`中的server.port配置

## 贡献指南

1. Fork本仓库
2. 创建特性分支 (`git checkout -b feature/amazing-feature`)
3. 提交更改 (`git commit -m 'Add some amazing feature'`)
4. 推送到分支 (`git push origin feature/amazing-feature`)
5. 打开Pull Request

## 相关文档

- [仓库管理子系统文档总览](../../docs/warehouse/00_文档总览.md)
- [仓库管理子系统详细设计文档](../../docs/warehouse/设计/仓库管理子系统详细设计文档.md)
- [仓库管理子系统测试计划](../../docs/warehouse/测试/仓库管理子系统测试计划.md)

## 许可证

本项目采用MIT许可证 - 详见LICENSE文件
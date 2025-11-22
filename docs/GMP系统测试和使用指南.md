# GMP信息管理系统 - 测试和使用指南

## 📋 文档导航

- [🏠 返回主页](../index.html)
- [👥 用户权限管理](./user-management.html)
- [🔍 接口文档](./api-documentation.html)

---

## 🎯 快速开始

### 🔧 系统要求
- **Java**: 17+
- **Maven**: 3.8+
- **Docker**: 20.10+ (可选)
- **Browser**: Chrome/Firefox/Safari

### 🚀 一键启动
```bash
# 克隆项目（如果没有）
git clone <repository-url>
cd gmp-system

# 启动所有服务
./scripts/start-dev.sh
```

## 🧪 测试覆盖率

### 📊 代码覆盖率报告

#### 当前覆盖率统计
```
整体覆盖率: 21.6% (指令) | 36% (行) | 47% (方法)
实体层: 27% (指令) | 62% (类)
服务层: 16% (指令) | 0% (分支覆盖)
配置层: 19% (指令) | 24% (DTO层)
```

#### 🔬 集成测试覆盖
```
🎫 用户认证流程: 8个测试场景
🔐 权限验证: 实时权限检查
👤 角色管理: 角色权限集成
💚 系统监控: 健康检查接口
📋 数据获取: 用户权限列表
```

#### 查看覆盖率报告
1. **生成本地报告**:
   ```bash
   cd services/auth-service
   mvn test jacoco:report
   ```

2. **打开报告**:
   - 浏览器访问: `services/auth-service/target/site/jacoco/index.html`
   - 或查看: `docs/coverage/auth-service/index.html`

### 🧪 运行单元测试

#### 运行所有测试
```bash
# 直接运行auth-service的测试
cd services/auth-service
mvn test

# 运行实体层测试
mvn test -Dtest=UserTest,RoleTest,PermissionTest,UserRoleTest

# 运行特定配置测试
mvn test -Dtest=SecurityConfigTest
```

#### 测试结果示例
```bash
[INFO] Tests run: 37, Failures: 0, Errors: 0, Skipped: 0
[INFO] 37 successful tests
[INFO]
[INFO] JaCoCo coverage report: generate-report
[INFO] HTML report: target/site/jacoco/index.html
```

### 🔌 自动化集成测试

#### 📜 运行集成测试脚本
```bash
# 运行完整测试套件 (推荐)
./scripts/run-integration-tests.sh

# 只运行单元测试
./scripts/run-integration-tests.sh unit

# 只运行集成测试
./scripts/run-integration-tests.sh integration

# 生成覆盖率报告
./scripts/run-integration-tests.sh coverage
```

#### 📊 集成测试执行结果
```bash
🚀 GMP系统认证集成测试开始
📁 切换到认证服务目录: /home/liying/gmp-system/services/auth-service
🎪 开始运行完整测试套件
# GMP信息管理系统 - 测试和使用指南

## 📋 文档导航

- [🏠 返回主页](../index.html)
- [👥 用户权限管理](./user-management.html)
- [🔍 接口文档](./api-documentation.html)

---

## 🎯 快速开始

### 🔧 系统要求
- **Java**: 17+
- **Maven**: 3.8+
- **Docker**: 20.10+ (可选)
- **Browser**: Chrome/Firefox/Safari

### 🚀 一键启动
```bash
# 克隆项目（如果没有）
git clone <repository-url>
cd gmp-system

# 启动所有服务
./scripts/start-dev.sh
```

## 🧪 测试覆盖率

### 📊 代码覆盖率报告

#### 当前覆盖率统计
```
整体覆盖率: 21.6% (指令) | 36% (行) | 47% (方法)
实体层: 27% (指令) | 62% (类)
服务层: 16% (指令) | 0% (分支覆盖)
配置层: 19% (指令) | 24% (DTO层)
```

#### 🔬 集成测试覆盖
```
🎫 用户认证流程: 8个测试场景
🔐 权限验证: 实时权限检查
👤 角色管理: 角色权限集成
💚 系统监控: 健康检查接口
📋 数据获取: 用户权限列表
```

#### 查看覆盖率报告
1. **生成本地报告**:
   ```bash
   cd services/auth-service
   mvn test jacoco:report
   ```

2. **打开报告**:
   - 浏览器访问: `services/auth-service/target/site/jacoco/index.html`
   - 或查看: `docs/coverage/auth-service/index.html`

🔨 编译认证服务项目...
✅ 项目编译成功
🧪 运行单元测试...
✅ 单元测试通过 (用时: 8s)
🔗 运行认证集成测试...
{
  "username": "admin@gmp.com",
  "password": "password123"
}
```
**响应示例**:
```json
{
  "success": true,
  "message": "登录成功",
  "code": "200",
  "data": {
    "accessToken": "eyJ...",
    "refreshToken": "refresh-...",
    "username": "admin@gmp.com",
    "permissions": ["READ_USER", "WRITE_POST"]
  }
}
```

#### 🔍 权限检查
```bash
GET /api/auth/check/{username}/permission?permission=READ_USER
```

**响应示例**:
```json
{
  "success": true,
  "message": "权限检查完成",
  "code": "200",
  "data": {
    "username": "admin@gmp.com",
    "permission": "READ_USER",
    "hasPermission": true
  }
}
```

#### 🏷️ 角色检查
```bash
GET /api/auth/check/{username}/role?role=ADMIN
```

**响应示例**:
```json
{
  "success": true,
  "message": "角色检查完成",
  "code": "200",
  "data": {
    "username": "admin@gmp.com",
    "role": "ADMIN",
    "hasRole": true
  }
}
```

#### 👥 获取用户列表
```bash
GET /api/auth/users
```

#### 🔑 获取用户权限
```bash
GET /api/auth/permissions/{username}
```

#### 🏷️ 获取用户角色
```bash
GET /api/auth/roles/{username}
```

### 🩺 健康检查
```bash
GET /api/auth/health
```

## 📊 监控与日志

### 🎨 Grafana监控面板
```
http://localhost:3000
用户名: admin
密码: admin
```

### 📈 Prometheus监控
```
http://localhost:9090
```

### 🔍 RabbitMQ管理界面
```
http://localhost:15672
用户名: guest
密码: guest
```

### 📝 系统日志
```bash
# 查看auth-service日志
tail -f logs/auth-service.log

# 查看所有服务日志
./scripts/show-logs.sh
```

## 🔧 开发工具

### 🧪 JaCoCo覆盖率工具
```bash
# 生成覆盖率报告
mvn clean test jacoco:report

# 检查覆盖率质量门槛
mvn jacoco:check
```

### 📚 Doxygen文档
```bash
# 生成API文档
mvn doxygen:report

# 查看源码文档
open docs/doxygen/html/index.html
```

### 🐳 Docker容器管理
```bash
# 启动所有基础设施
docker-compose up -d

# 查看服务状态
docker-compose ps

# 停止所有服务
docker-compose down
```

## 🐛 故障排除

### 🔧 常见问题

#### 1. 服务启动失败
```bash
# 检查端口是否被占用
lsof -i :8081

# 清理可能有问题的容器
docker-compose down -v
docker-compose up -d
```

#### 2. 测试执行失败
```bash
# 清理测试缓存
mvn clean test

# 运行特定测试类
mvn test -Dtest=UserTest
```

#### 3. 页面访问异常
- 检查服务是否启动: `docker-compose ps`
- 检查浏览器控制台是否有错误
- 确保API地址正确: `http://localhost:8081`

#### 4. 数据库连接问题
```bash
# 检查PostgreSQL容器
docker logs gmp_postgresql

# 数据库连接信息
Host: localhost:5432
Database: gmp_system
User: postgres
Password: gmp_admin_2024
```

### 📞 获取帮助

#### 📧 联系支持
- **技术支持**: GMP开发团队
- **邮箱**: admin@gmp-system.com
- **文档**: [项目Wiki](../../wiki)

#### 🔍 资源链接
- [Spring Boot官方文档](https://spring.io/projects/spring-boot)
- [JaCoCo覆盖率工具](https://www.jacoco.org/jacoco/)
- [PostgreSQL文档](https://www.postgresql.org/docs/)
- [Redis中文文档](https://redis.com.cn/documentation.html)

---

## 🎯 总结

### ✅ GMP系统优势
1. **现代化架构**: Spring Boot 3.x + 微服务架构
2. **完善测试**: JaCoCo代码覆盖率监控
3. **企业级安全**: JWT + BCrypt认证授权
4. **容器化部署**: Docker + Docker Compose
5. **监控完整**: Prometheus + Grafana监控面板
6. **文档齐全**: API文档 + 使用指南

### 🚀 快速体验
1. **启动服务**: `./scripts/start-dev.sh`
2. **访问主页**: `index.html`
3. **测试权限**: `user-management.html`
4. **查看监控**: `http://localhost:3000`

---

*📅 最后更新: 2025年11月20日 | 版本: 0.2.5*

[🏠 返回主页](../index.html) | [👥 权限管理](./user-management.html)

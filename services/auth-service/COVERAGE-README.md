# GMP认证服务 - 代码覆盖率测试指南

## 📊 概述

本文档介绍如何使用JaCoCo代码覆盖率工具测试GMP认证服务的代码覆盖率，确保代码质量符合企业标准。

## 🎯 覆盖率目标

| 指标 | 目标值 | 说明 |
|------|--------|------|
| 行覆盖率 | ≥80% | 代码行的执行覆盖率 |
| 分支覆盖率 | ≥70% | 条件分支的覆盖率 |
| 类覆盖率 | ≥90% | 类的覆盖率 |
| 方法覆盖率 | ≥80% | 方法的覆盖率 |
| 指令覆盖率 | ≥75% | 字节码指令的覆盖率 |
| 圈复杂度覆盖率 | ≥70% | 复杂条件的覆盖率 |

## 🛠️ 环境配置

### 技术栈
- **JaCoCo**: 0.8.8 - 代码覆盖率工具
- **JUnit 5**: 单元测试框架
- **AssertJ**: 流畅断言库
- **H2 Database**: 测试内存数据库
- **Redis**: 测试缓存数据库（DB 15）

### 配置说明

#### 测试配置文件
- `src/test/resources/application-test.yml`: 测试环境配置
- `src/test/resources/jacoco-rules.properties`: 覆盖率质量阈值

#### Maven插件配置
- JaCoCo插件: 自动注入覆盖率代理
- Surefire插件: 执行单元测试
- Failsafe插件: 执行集成测试

## 🚀 运行测试

### 方法1: 使用自动化脚本（推荐）

```bash
# 从项目根目录运行
./scripts/run-coverage-tests.sh
```

### 方法2: 手动运行Maven命令

```bash
# 进入auth-service目录
cd services/auth-service

# 运行测试并生成报告
mvn clean test jacoco:report

# 查看报告（浏览器打开）
open target/site/jacoco/index.html
```

### 方法3: 分步骤执行

```bash
# 1. 清理项目
mvn clean

# 2. 编译代码
mvn compile

# 3. 只运行测试
mvn test

# 4. 生成覆盖率报告
mvn jacoco:report

# 5. 生成聚合报告（如果配置了）
mvn jacoco:merge jacoco:report-aggregate
```

## 📋 测试报告

### 报告位置

运行测试后，生成以下报告文件：

```
auth-service/target/
├── jacoco.exec                    # 原始覆盖率数据
├── site/jacoco/
│   └── index.html                # HTML报告
└── jacoco/aggregate-report/
    └── index.html                # 聚合报告（如果配置）
```

### 报告内容

HTML报告包含以下信息：

- **覆盖率概览**: 整体覆盖率统计
- **包级别覆盖率**: 按包划分的覆盖率
- **类级别覆盖率**: 每个类的详细覆盖率
- **源码高亮**: 显示未覆盖的代码行
- **分支覆盖率**: 条件分支的执行情况

### 查看实际覆盖率报告

要查看最新的覆盖率测试报告，请点击以下链接：

- [GMP系统覆盖率报告汇总](../../coverage/index.html)
- [认证服务覆盖率报告](../../coverage/auth-service/index.html)

## 🔍 测试用例说明

### 实体类测试 (`entity/*.java`)

#### User实体测试
```java
// 测试用户实体业务逻辑
UserTest.java - 测试用户状态、密码过期、登录尝试等
```

#### 主要测试点
- 用户状态枚举验证
- 登录尝试计数逻辑
- 账户锁定机制
- 密码过期检测
- 审计字段验证

### 配置类测试 (`config/*.java`)

#### JWT配置测试
```java
// 测试JWT令牌生成和验证
JwtConfigTest.java - 测试令牌生命周期管理
```

#### 主要测试点
- JWT令牌生成和解析
- 令牌过期检查
- 令牌刷新逻辑
- 用户信息提取
- 异常处理验证

### 服务层测试 (`service/*.java`)

#### 认证服务测试
```java
// 预留给后续实现
AuthServiceTest.java
AuthServiceImplTest.java
```

#### 计划测试点
- 用户登录认证
- 令牌验证和刷新
- 权限检查逻辑
- 缓存机制验证

### 存储层测试 (`repository/*.java`)

```java
// 预留给顺序后续实现
UserRepositoryTest.java
OperationLogRepositoryTest.java
```

## 📊 质量检查

### 覆盖率阈值检查

如果测试未达到覆盖率阈值，构建会失败：

```bash
# 检查具体阈值
mvn jacoco:check
```

### 自定义阈值规则

修改 `jacoco-rules.properties` 文件调整阈值：

```properties
# 示例：调整行覆盖率到85%
jacoco.line.coverage.min=85
jacoco.line.coverage.action=HALT
```

**注意**: `action=HALT` 会导致构建失败，`action=WARN` 只生成警告。

## 🔧 故障排除

### 常见问题

#### 1. JaCoCo报告为空
**原因**: 测试未使用JaCoCo代理
**解决方案**: 确保使用 `mvn test jacoco:report` 而不是仅 `mvn test`

#### 2. Redis连接错误
**原因**: 测试环境Redis未运行
**解决方案**: 启动Redis服务或注释掉Redis相关测试

#### 3. H2数据库错误
**原因**: 多个测试同时访问内存数据库
**解决方案**: 每个测试使用独立的数据库URL

#### 4. 覆盖率低于阈值
**原因**: 测试不完整
**解决方案**: 添加更多测试用例覆盖边界情况

### 日志调试

增加测试日志级别查看详情：

```yaml
logging:
  level:
    com.gmp.auth: DEBUG
    org.jacoco: DEBUG
```

## 🚀 CI/CD集成

### GitHub Actions示例

```yaml
name: Code Coverage
on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'

      - name: Run Tests with Coverage
        working-directory: services/auth-service
        run: mvn clean test jacoco:report

      - name: Upload Coverage Reports
        uses: actions/upload-artifact@v3
        with:
          name: coverage-reports
          path: services/auth-service/target/site/jacoco/
```

### Jenkins集成

```groovy
stage('Code Coverage') {
    steps {
        dir('services/auth-service') {
            sh 'mvn clean test jacoco:report'
            publishCoverage adapters: [jacocoAdapter('target/site/jacoco/jacoco.xml')]
        }
    }
}
```

## 📈 最佳实践

### 编写高质量测试

#### 1. 测试命名规范
```java
@Test
void should_当某种情况时_期望某种结果() {
    // Given - 前置条件
    // When - 执行操作
    // Then - 验证结果
}
```

#### 2. 使用@Testcontainers
对于需要外部依赖的测试，考虑使用Testcontainers：

```java
@SpringBootTest
@Testcontainers
class AuthServiceIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:13");

    @Container
    static RedisContainer redis = new RedisContainer();

    // 测试代码...
}
```

#### 3. Mock外部依赖
```java
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthServiceImpl authService;

    // 测试代码...
}
```

## 📊 覆盖率报告解读

### 覆盖率指标说明

- **行覆盖率 (Line Coverage)**: 已执行的代码行比例
- **分支覆盖率 (Branch Coverage)**: 已执行的分支（如if/else）比例
- **圈复杂度 (Cyclomatic Complexity)**: 代码路径的复杂性

### 提高覆盖率的方法

1. **添加边界测试**: 测试异常情况
2. **参数化测试**: `@ParameterizedTest`
3. **测试私有方法**: 必要时使用反射
4. **集成测试**: 覆盖完整的业务流程

## 🎯 目标达成检查

运行以下命令检查是否达到覆盖率目标：

```bash
# 查看详细覆盖率统计
mvn jacoco:report

# 检查是否通过质量阈值
mvn jacoco:check
```

## 📞 技术支持

如需帮助，请参考：
- [JaCoCo官方文档](https://www.jacoco.org/jacoco/trunk/doc/)
- [JUnit 5用户指南](https://junit.org/junit5/docs/current/user-guide/)
- 项目README文档

---

**文档版本**: 1.0
**更新日期**: 2025-11-19
**适用于**: GMP认证服务 v0.2.4+

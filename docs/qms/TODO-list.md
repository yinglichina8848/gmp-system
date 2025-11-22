# QMS子系统实现TODO清单 (为AI开发者准备)

## 🎯 总体目标
实现完整的GMP质量管理系统( QMS )微服务，包括偏差管理、CAPA管理、变更控制和审计管理四个核心模块。

## 📋 实现步骤总览

### Phase 1: 项目基础设置 (完成标志: 所有依赖和配置准备就绪)
- [ ] 1.1 创建QMS微服务基础结构
- [ ] 1.2 配置数据库连接和迁移
- [ ] 1.3 配置缓存和消息队列
- [ ] 1.4 配置安全和权限验证

### Phase 2: 核心数据模型 (完成标志: 所有实体类和Repository完成)
- [ ] 2.1 设计和实现偏差管理数据模型
- [ ] 2.2 设计和实现CAPA管理数据模型
- [ ] 2.3 设计和实现变更控制数据模型
- [ ] 2.4 设计和实现审计管理数据模型
- [ ] 2.5 创建枚举类和常量定义

### Phase 3: 业务服务层 (完成标志: 所有业务逻辑实现完成)
- [ ] 3.1 实现偏差管理业务服务
- [ ] 3.2 实现CAPA管理业务服务
- [ ] 3.3 实现变更控制业务服务
- [ ] 3.4 实现审计管理业务服务
- [ ] 3.5 实现业务规则引擎

### Phase 4: REST API层 (完成标志: 所有API接口完成并可测试)
- [ ] 4.1 实现偏差管理REST控制器
- [ ] 4.2 实现CAPA管理REST控制器
- [ ] 4.3 实现变更控制REST控制器
- [ ] 4.4 实现审计管理REST控制器
- [ ] 4.5 统一异常处理和响应格式

### Phase 5: 单元测试 (完成标志: 单测覆盖率≥85%)
- [ ] 5.1 编写实体类单元测试
- [ ] 5.2 编写业务服务层单元测试
- [ ] 5.3 编写控制器层单元测试
- [ ] 5.4 编写数据访问层单元测试
- [ ] 5.5 生成单元测试覆盖率报告

### Phase 6: 集成测试 (完成标志: 集成测试通过且覆盖关键业务流程)
- [ ] 6.1 设置测试环境和容器化测试
- [ ] 6.2 编写数据库集成测试
- [ ] 6.3 编写API集成测试
- [ ] 6.4 编写端到端业务流程测试
- [ ] 6.5 生成集成测试报告

### Phase 7: 业务测试场景 (完成标志: 所有GMP合规场景验证完成)
- [ ] 7.1 偏差报告和处理全流程测试
- [ ] 7.2 CAPA闭环管理流程测试
- [ ] 7.3 变更控制审批流程测试
- [ ] 7.4 审计执行和整改流程测试
- [ ] 7.5 GMP合规性验证

---

## 📝 详细实现指南

### Phase 1: 项目基础设置

#### 1.1 创建QMS微服务基础结构
**输入**: `services/qms-service/pom.xml` (已存在)
**实现步骤**:
1. 验证现有的Maven依赖配置是否完整
2. 按业务模块创建Java包结构:
   ```
   src/main/java/com/gmp/qms/
   ├── config/
   ├── controller/
   ├── service/
   ├── repository/
   ├── entity/
   ├── dto/
   ├── exception/
   └── enums/
   ```
3. 创建主应用类 `QmsApplication.java` (已有)
4. 创建配置文件 `application.yml` 和 `application-dev.yml`

**验证标准**: 项目能成功编译通过

#### 1.2 配置数据库连接和迁移
**相关文件**: 
- `infrastructure/postgres/init/03-init-qms-db.sql` (需要创建)
- `src/main/resources/application.yml`

**实现步骤**:
1. 创建QMS数据库初始化脚本，包括所有数据表
2. 配置Spring Data JPA和PostgreSQL连接
3. 配置Flyway数据库迁移脚本
4. 验证数据库连接正常

**SQL脚本内容示例**:
```sql
CREATE DATABASE qms_db;
-- deviations表, capas表, change_controls表, audits表等
-- 索引和约束配置
```

#### 1.3 配置缓存和消息队列
**实现步骤**:
1. 配置Redis缓存
2. 配置RabbitMQ消息队列和交换器
3. 创建缓存配置类 `RedisConfig.java`
4. 创建消息队列配置类 `RabbitConfig.java`

#### 1.4 配置安全和权限验证
**实现步骤**:
1. 配置Spring Security与JWT集成
2. 创建OAuth2资源服务器配置
3. 实现基于角色的权限访问控制
4. 创建权限验证注解

### Phase 2: 核心数据模型

#### 2.1 偏差管理数据模型
**关键类**:
- `Deviation.java` - 实体类
- `DeviationStatus.java` - 状态枚举
- `DeviationSeverity.java` - 严重程度枚举
- `DeviationRepository.java` - 数据访问接口
- `DeviationAttachment.java` - 附件实体

**具体要求**:
1. 实现完整的JPA实体映射
2. 配置审计字段 (@CreatedDate, @LastModifiedDate)
3. 实现关联关系映射
4. 配置校验注解 (@NotNull, @Size等)

#### 2.2 CAPA管理数据模型
**关键类**:
- `Capa.java` - CAPA实体
- `CapaAction.java` - 行动项实体
- `CapaPriority.java` - 优先级枚举
- `CapaRepository.java` & `CapaActionRepository.java`

**业务规则**:
- CAPA必须关联来源 (偏差、审计、投诉等)
- 支持多个行动项跟踪
- 需要记录有效性验证结果

#### 2.5 创建枚举类和常量定义
**枚举类**:
```java
public enum DeviationSeverity { LOW, MEDIUM, HIGH, CRITICAL }
public enum DeviationStatus { OPEN, IN_ANALYSIS, IN_CORRECTION, CLOSED }
public enum CapaPriority { LOW, MEDIUM, HIGH, CRITICAL }
public enum ChangeType { PROCESS, EQUIPMENT, DOCUMENT, SUPPLIER, SYSTEM }
public enum AuditType { INTERNAL, EXTERNAL, SUPPLIER, SPECIAL }
```

### Phase 3: 业务服务层

#### 3.1 实现偏差管理业务服务
**关键组件**:
- `DeviationService.java` - 接口
- `DeviationServiceImpl.java` - 实现类
- `DeviationBusinessRules.java` - 业务规则引擎

**核心方法**:
```java
// 创建偏差
DeviationDto createDeviation(CreateDeviationRequest request);
// 更新偏差状态
DeviationDto updateDeviationStatus(Long id, DeviationStatus status);
// 分配负责人
DeviationDto assignDeviation(Long id, String assignee);
// 上传附件
DeviationAttachment uploadAttachment(Long deviationId, MultipartFile file);
```

**业务逻辑**:
1. 自动生成偏差编号 (DEV-YYYY-MMDD-001)
2. 根据严重程度自动计算截止时间
3. 发送短信通知质量经理 (CRITICAL级别)
4. 自动分配质量工程师处理

#### 3.2 实现CAPA管理业务服务
**核心业务流程**:
1. **根本原因分析**: 鱼骨图法识别根本原因
2. **措施制定**: 制定纠正和预防措施
3. **执行跟踪**: 跟踪行动项完成情况
4. **有效性验证**: 通过试验或观察验证措施有效性

#### 3.5 实现业务规则引擎
**用途**: 封装所有业务规则，提供统一的规则验证和计算服务
```java
@Service
public class QmsBusinessRuleEngine {
    // 自动编号生成
    public String generateNumber(String prefix, LocalDate date);
    // 截止时间计算
    public LocalDateTime calculateDueDate(Object severity);
    // 自动分配规则
    public String autoAssign(Object entity);
}
```

### Phase 4: REST API层

#### 4.1 实现偏差管理REST控制器
**API接口列表**:
```
POST   /api/qms/deviations           - 创建偏差
GET    /api/qms/deviations/{id}      - 获取偏差详情
GET    /api/qms/deviations           - 分页查询偏差
PUT    /api/qms/deviations/{id}      - 更新偏差
PATCH  /api/qms/deviations/{id}/status - 更新状态
POST   /api/qms/deviations/{id}/attachments - 上传附件
```

**控制器实现要点**:
1. 使用Spring Validation进行输入验证
2. 统一异常处理 (@RestControllerAdvice)
3. 分页查询支持 (Pageable)
4. 文件上传多部分请求处理

#### 4.5 统一异常处理和响应格式
**全局异常处理器**:
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(ValidationException e) {
        // 返回字段验证错误信息
    }
}
```

**统一响应格式**:
```json
{
    "success": true,
    "code": 200,
    "message": "操作成功",
    "data": { ... },
    "timestamp": "2024-01-01T00:00:00Z"
}
```

### Phase 5: 单元测试

#### 5.1 编写实体类单元测试
**测试内容**:
- JPA注解配置验证
- 验证器注解功能
- Equals和HashCode方法
- ToString方法

**示例测试**:
```java
@Test
public void testDeviationEntityValidation() {
    Deviation deviation = new Deviation();
    // 测试必填字段验证
    Set<ConstraintViolation<Deviation>> violations = validator.validate(deviation);
    assertFalse(violations.isEmpty());
}
```

#### 5.2 编写业务服务层单元测试
**Mock策略**:
- 使用Mockito进行依赖mock
- 重点测试业务逻辑分支
- 验证规则引擎调用
- 测试异常处理

**示例测试**:
```java
@ExtendWith(MockitoExtension.class)
class DeviationServiceImplTest {

    @Mock private DeviationRepository repository;
    @Mock private BusinessRuleEngine ruleEngine;

    @Test
    void createDeviation_ShouldGenerateNumberAndCalculateDueDate() {
        // Given
        CreateDeviationRequest request = createValidRequest();
        when(ruleEngine.generateNumber(any(), any())).thenReturn("DEV-2024-0101-001");

        // When
        DeviationDto result = service.createDeviation(request);

        // Then
        assertNotNull(result.getDeviationNumber());
        verify(ruleEngine).calculateDueDate(any());
    }
}
```

#### 5.5 生成单元测试覆盖率报告
**工具配置**:
1. 配置Jacoco插件在pom.xml
2. 执行覆盖率测试: `mvn test jacoco:report`
3. 配置覆盖率规则 (jacoco-rules.properties)
4. 目标覆盖率: 类覆盖率≥90%, 分支覆盖率≥80%

### Phase 6: 集成测试

#### 6.1 设置测试环境和容器化测试
**使用Testcontainers**:
```java
@SpringBootTest
@Testcontainers
class QmsIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("qms_test")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
    }
}
```

#### 6.2 编写数据库集成测试
**测试内容**:
- 数据持久化操作
- 关联查询功能
- 事务完整性
- 级联操作

#### 6.3 编写API集成测试
**使用RestAssured**:
```java
@Test
void createDeviation_ShouldReturn201_WhenValidRequest() {
    given()
        .contentType(ContentType.JSON)
        .body(createValidDeviationJson())
    .when()
        .post("/api/qms/deviations")
    .then()
        .statusCode(201)
        .body("success", equalTo(true))
        .body("data.deviationNumber", notNullValue());
}
```

#### 6.4 编写端到端业务流程测试
**全流程测试场景**:
1. 创建偏差 → 查询偏差 → 更新状态 → 关闭偏差
2. 创建CAPA → 添加行动项 → 完成行动项 → 验证有效性
3. 变更申请 → 评审审批 → 执行变更 → 验证效果

### Phase 7: 业务测试场景

#### 7.1 偏差报告和处理全流程测试
**测试场景** (用自然语言描述，模拟实际业务操作):

**场景1: 生产偏差报告流程**
1. 生产操作员发现提取工序温度超标5℃
2. 登录QMS系统，点击"报告偏差"
3. 填写偏差信息: 标题"提取温度超标", 描述详细情况, 选择严重程度HIGH
4. 系统自动生成编号DEV-2024-0101-001, 计算截止时间24小时
5. 上传设备截图作为附件
6. 创建成功后，短信通知质量工程师立即处理

**场景2: 质量工程师处理流程**
1. 质量工程师收到通知，查看偏差详情
2. 评估根本原因: 温度传感器故障
3. 制定纠正措施: 更换传感器并重新验证
4. 状态更新为"纠正中"
5. 系统自动通知操作员执行纠正措施

#### 7.2 CAPA闭环管理流程测试
**业务场景**:
- 来源: 来自生产偏差
- 根本原因: 设备维护不及时
- 纠正措施: 建立定期维护制度
- 预防措施: 添加设备预警系统
- 验证方式: 观察6个月维护记录

#### 7.5 GMP合规性验证
**验证要点**:
- 所有操作有审计追踪
- 数据电子签名完善
- 权限控制严格 (数据隔离)
- 操作日志完整
- 电子化记录符合FDA 21 CFR Part 11

---

## 📊 测试指标和标准

### 质量门禁 (Quality Gates)
- [ ] **单元测试覆盖率**: 类覆盖率 ≥ 90%, 分支覆盖率 ≥ 80%
- [ ] **集成测试通过率**: ≥ 95%
- [ ] **性能测试**: API响应时间 < 500ms (95分位)
- [ ] **静态代码分析**: SonarQube质量门禁通过
- [ ] **安全扫描**: 无高危安全漏洞

### 监控指标
- [ ] 应用健康检查端点正常
- [ ] JVM内存使用率 < 80%
- [ ] 数据库连接池使用率 < 80%
- [ ] 消息队列积压 < 1000条

### 合规检查
- [ ] 数据审计日志完整
- [ ] 用户操作记录可追溯
- [ ] 权限控制严格
- [ ] 数据备份策略到位

---

## 🛠️ 开发环境准备

### 必需软件
- JDK 17+
- Maven 3.6+
- PostgreSQL 13+
- Redis 6+
- RabbitMQ 3.8+
- Docker & Docker Compose

### 开发工具
- IntelliJ IDEA 或 VS Code
- Postman 用于API测试
- DBeaver 用于数据库管理
- Git 用于版本控制

### 本地环境启动
```bash
# 启动基础服务
docker-compose up postgres redis rabbitmq

# 启动QMS服务
cd services/qms-service
mvn spring-boot:run

# 运行测试
mvn test                    # 单元测试
mvn verify -P integration   # 集成测试
mvn test jacoco:report      # 覆盖率报告
```

---

## 🚀 实施建议

### 开发顺序建议
1. **从数据模型开始**: 先建立坚实的数据基础
2. **分模块并行开发**: 四个核心模块可以并行开发
3. **先服务层再API层**: 业务逻辑先行，确保API层只处理HTTP协议
4. **测试贯穿始终**: 每个功能完成即编写相应测试
5. **持续集成**: 使用GitHub Actions进行自动化测试

### 时间估算
- Phase 1-2: 1周 (基础建设)
- Phase 3-4: 2周 (核心功能)
- Phase 5: 1周 (单元测试)
- Phase 6: 1周 (集成测试)
- Phase 7: 1周 (业务验证)

### 风险控制
- **技术风险**: 多花时间在实体关系设计上
- **测试风险**: 重视集成测试，确保模块间协作正常
- **合规风险**: GMP要求严格，重点关注审计追踪
- **性能风险**: 注意数据库查询优化，大表分页处理

---

**此TODO清单由AI专用格式编写，指导AI开发者系统性完成QMS子系统的完整实现。**

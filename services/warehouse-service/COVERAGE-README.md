# GMP仓库管理子系统 - 代码覆盖率测试指南

## 📊 概述

本文档介绍如何使用JaCoCo代码覆盖率工具测试GMP仓库管理子系统的代码覆盖率，确保代码质量符合企业标准。

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
# 进入warehouse-service目录
cd /home/liying/gmp-system/services/warehouse-service

# 运行单元测试并生成覆盖率报告
mvn clean test jacoco:report
```

### 方法3: 运行特定测试类

```bash
# 运行特定测试类
mvn test -Dtest=WarehouseServiceImplTest
```

## 📈 查看覆盖率报告

运行测试后，覆盖率报告将生成在以下目录：

```
/home/liying/gmp-system/services/warehouse-service/target/site/jacoco/
```

打开`index.html`文件即可查看详细的覆盖率报告。

## 📋 覆盖率报告说明

### 报告结构
- **项目总览**: 显示整体覆盖率指标
- **包覆盖率**: 按包查看覆盖率
- **类覆盖率**: 按类查看覆盖率
- **源代码覆盖率**: 查看具体源代码的覆盖情况

### 覆盖率指标说明
- **行覆盖率**: 已执行的代码行数占总代码行数的百分比
- **分支覆盖率**: 已执行的条件分支占总分支数的百分比
- **类覆盖率**: 已执行的类占总类数的百分比
- **方法覆盖率**: 已执行的方法占总方法数的百分比
- **指令覆盖率**: 已执行的字节码指令占总指令数的百分比
- **圈复杂度覆盖率**: 已执行的独立路径占总独立路径数的百分比

## 🔍 提高覆盖率的策略

1. **单元测试优先**: 为每个类和方法编写单元测试
2. **边界条件测试**: 测试边界值、空值、异常情况
3. **分支覆盖**: 确保所有条件分支都被测试
4. **集成测试**: 测试组件间的交互
5. **参数化测试**: 使用不同的参数组合测试方法

## ⚠️ 常见问题与解决方案

### 1. 覆盖率报告为空
- **问题**: 运行测试后没有生成覆盖率数据
- **解决方案**: 检查JaCoCo插件配置是否正确，确保测试正常运行

### 2. 特定类/方法未被覆盖
- **问题**: 某些类或方法在覆盖率报告中显示为0%覆盖
- **解决方案**: 检查测试是否正确调用了这些类和方法，可能需要添加额外的测试用例

### 3. 覆盖率低于阈值
- **问题**: 运行`mvn jacoco:check`时失败，提示覆盖率低于阈值
- **解决方案**: 查看覆盖率报告，针对未覆盖的代码添加测试用例

### 4. 测试环境配置问题
- **问题**: 测试在本地运行正常，但在CI环境中失败
- **解决方案**: 确保测试环境配置与CI环境一致，特别是数据库和Redis连接配置

## 📝 测试最佳实践

1. **测试命名规范**: 使用清晰的命名，如`testMethodName_Scenario_ExpectedResult`
2. **测试隔离**: 每个测试方法应独立，不依赖于其他测试方法的状态
3. **模拟依赖**: 使用Mockito等工具模拟外部依赖
4. **断言精确**: 使用具体的断言，避免使用过于宽泛的断言
5. **测试可读性**: 编写清晰、简洁的测试代码，添加适当的注释

## 📊 覆盖率趋势跟踪

建议定期运行覆盖率测试，跟踪覆盖率的变化趋势。可以将覆盖率报告集成到CI/CD流程中，确保覆盖率不会随着代码的更新而下降。

## 📋 附录: 覆盖率检查命令

```bash
# 运行覆盖率检查
mvn jacoco:check

# 生成详细的覆盖率报告
mvn jacoco:report

# 运行测试并生成覆盖率报告
mvn clean test jacoco:report
```

## 🚀 附录: 示例测试代码

```java
@SpringBootTest
@ActiveProfiles("test")
class WarehouseServiceImplTest {

    @Autowired
    private WarehouseService warehouseService;

    @MockBean
    private MaterialRepository materialRepository;

    @Test
    void testGetMaterialById() {
        // Given
        Long materialId = 1L;
        Material material = new Material();
        material.setId(materialId);
        material.setName("Test Material");
        
        when(materialRepository.findById(materialId)).thenReturn(Optional.of(material));
        
        // When
        Material result = warehouseService.getMaterialById(materialId);
        
        // Then
        assertNotNull(result);
        assertEquals("Test Material", result.getName());
        verify(materialRepository).findById(materialId);
    }
}
```

---

通过定期运行代码覆盖率测试，可以持续监控和提高代码质量，确保仓库管理子系统的稳定性和可靠性。
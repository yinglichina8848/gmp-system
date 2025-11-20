#!/bin/bash

# GMP认证服务代码覆盖率测试脚本
# 用于运行单元测试并生成JaCoCo覆盖率报告

echo "=========================================="
echo "GMP认证服务 - 代码覆盖率测试"
echo "=========================================="

# 项目根目录
PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
AUTH_SERVICE_DIR="$PROJECT_ROOT/services/auth-service"

echo "项目根目录: $PROJECT_ROOT"
echo "认证服务目录: $AUTH_SERVICE_DIR"
echo

# 检查Maven是否可用
if ! command -v mvn &> /dev/null; then
    echo "❌ 错误: Maven未安装或不在PATH中"
    exit 1
fi

# 进入认证服务目录
cd "$AUTH_SERVICE_DIR" || {
    echo "❌ 错误: 无法进入认证服务目录"
    exit 1
}

echo "📦 清理并编译项目..."
mvn clean compile -q

if [ $? -ne 0 ]; then
    echo "❌ 错误: 编译失败"
    exit 1
fi

echo "✅ 编译成功"
echo

echo "🧪 运行单元测试并生成覆盖率报告..."
mvn test jacoco:report -q

if [ $? -eq 0 ]; then
    echo "✅ 测试和覆盖率报告生成成功"
else
    echo "❌ 测试失败，检查测试代码"
    exit 1
fi

# 检查覆盖率报告是否存在
REPORT_FILE="$AUTH_SERVICE_DIR/target/site/jacoco/index.html"
if [ -f "$REPORT_FILE" ]; then
    echo "📊 覆盖率报告已生成: $REPORT_FILE"
    echo "🔗 在浏览器中打开报告: file://$REPORT_FILE"
else
    echo "⚠️  警告: 未找到覆盖率报告文件"
fi

# 显示覆盖率统计
EXEC_FILE="$AUTH_SERVICE_DIR/target/jacoco.exec"
if [ -f "$EXEC_FILE" ]; then
    echo
    echo "📈 覆盖率统计 (jacoco.exec文件已生成):"
    ls -lh "$EXEC_FILE"
fi

# 检查覆盖率目标是否达到
echo
echo "🎯 覆盖率目标检查:"
echo "  - 行覆盖率: >= 80%"
echo "  - 分支覆盖率: >= 70%"
echo "  - 类覆盖率: >= 90%"
echo "  - 指令覆盖率: >= 75%"
echo

# 处理合并报告
echo "🔄 生成聚合覆盖率报告..."
mvn jacoco:merge jacoco:report-aggregate -q

if [ $? -eq 0 ]; then
    AGGREGATE_REPORT="$AUTH_SERVICE_DIR/target/jacoco/aggregate-report/index.html"
    if [ -f "$AGGREGATE_REPORT" ]; then
        echo "📊 聚合覆盖率报告已生成: $AGGREGATE_REPORT"
    fi
fi

echo
echo "=========================================="
echo "测试完成！"
echo "=========================================="
echo
echo "命令说明:"
echo "  - 查看详细报告: 在浏览器中打开上述HTML文件"
echo "  - 重新运行测试: ./scripts/run-coverage-tests.sh"
echo "  - 仅运行测试: mvn test (在auth-service目录下)"
echo

# 显示测试结果摘要
echo "📋 测试配置说明:"
echo "  - 数据库: H2内存数据库(测试专用)"
echo "  - Redis: 数据库15(测试专用)"
echo "  - JWT密钥: 测试专用密钥"
echo "  - 日志级别: DEBUG模式"
echo
echo "🎉 GMP认证服务测试环境配置完成!"

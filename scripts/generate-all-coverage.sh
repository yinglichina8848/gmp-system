#!/bin/bash

# GMP系统服务代码覆盖率报告生成脚本
# 专注于处理auth-service等关键服务并生成JaCoCo覆盖率报告

echo "=========================================="
echo "GMP系统 - 服务代码覆盖率报告生成"
echo "=========================================="

# 项目根目录
PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
SERVICES_DIR="$PROJECT_ROOT/services"

# 创建聚合报告目录
AGGREGATE_DIR="$PROJECT_ROOT/docs/coverage"
mkdir -p "$AGGREGATE_DIR"

echo "项目根目录: $PROJECT_ROOT"
echo "服务目录: $SERVICES_DIR"
echo "聚合报告目录: $AGGREGATE_DIR"
echo

# 检查Maven是否可用
if ! command -v mvn &> /dev/null; then
    echo "❌ 错误: Maven未安装或不在PATH中"
    exit 1
fi

# 专注于处理auth-service和其他存在的服务
echo "ℹ️  专注处理已存在且可构建的服务"
echo

# 定义要处理的关键服务列表（优先处理auth-service）
key_services=(
    "auth-service"
    # 可以在这里添加其他要优先处理的服务
)

successful_services=()
failed_services=()

# 处理关键服务
for service_name in "${key_services[@]}"; do
    service="$SERVICES_DIR/$service_name"
    if [ -d "$service" ] && [ -f "$service/pom.xml" ]; then
        echo "\n=========================================="
        echo "🔍 正在处理关键服务: $service_name"
        echo "=========================================="
        
        # 进入服务目录
        cd "$service" || {
            echo "❌ 错误: 无法进入服务目录 $service_name"
            failed_services+=("$service_name")
            continue
        }
        
        echo "🧪 运行单元测试并生成覆盖率报告..."
        
        # 运行测试并生成覆盖率报告（auth-service已配置JaCoCo）
        mvn test jacoco:report -q
        
        if [ $? -eq 0 ]; then
            echo "✅ 测试和覆盖率报告生成成功 - $service_name"
            successful_services+=("$service_name")
            
            # 检查覆盖率报告是否存在
            REPORT_FILE="$service/target/site/jacoco/index.html"
            if [ -f "$REPORT_FILE" ]; then
                echo "📊 覆盖率报告已生成: $REPORT_FILE"
                
                # 复制报告到聚合目录
                mkdir -p "$AGGREGATE_DIR/$service_name"
                cp -r "$service/target/site/jacoco/"* "$AGGREGATE_DIR/$service_name/"
                echo "📋 报告已复制到聚合目录: $AGGREGATE_DIR/$service_name/index.html"
            else
                echo "⚠️  警告: 未找到覆盖率报告文件 - $service_name"
            fi
        else
            echo "❌ 错误: 测试失败 - $service_name"
            failed_services+=("$service_name")
        fi
    else
        echo "ℹ️  服务 $service_name 不存在或不是有效的Maven项目，跳过处理"
    fi
done

# 可选：扫描其他可能存在的服务
echo "\n=========================================="
echo "🔍 扫描其他可能存在的服务..."
echo "=========================================="

for service in "$SERVICES_DIR"/*; do
    if [ -d "$service" ] && [ -f "$service/pom.xml" ]; then
        service_name=$(basename "$service")
        
        # 检查是否已经处理过这个服务
        skip=false
        for processed in "${key_services[@]}" "${successful_services[@]}" "${failed_services[@]}"; do
            if [ "$processed" == "$service_name" ]; then
                skip=true
                break
            fi
        done
        
        if [ "$skip" = true ]; then
            continue
        fi
        
        echo "\n=========================================="
        echo "🔍 正在处理服务: $service_name"
        echo "=========================================="
        
        # 进入服务目录
        cd "$service" || {
            echo "❌ 错误: 无法进入服务目录 $service_name"
            failed_services+=("$service_name")
            continue
        }
        
        # 检查是否已配置JaCoCo
        if ! grep -q "jacoco-maven-plugin" pom.xml; then
            echo "⚠️  警告: $service_name 未配置JaCoCo，跳过处理"
            continue
        fi
        
        # 尝试运行测试并生成覆盖率报告，但不立即失败
        echo "🧪 尝试生成覆盖率报告..."
        mvn test jacoco:report -q 2>/dev/null
        
        if [ $? -eq 0 ]; then
            echo "✅ 成功生成覆盖率报告 - $service_name"
            successful_services+=("$service_name")
            
            # 复制报告
            REPORT_FILE="$service/target/site/jacoco/index.html"
            if [ -f "$REPORT_FILE" ]; then
                mkdir -p "$AGGREGATE_DIR/$service_name"
                cp -r "$service/target/site/jacoco/"* "$AGGREGATE_DIR/$service_name/"
                echo "📋 报告已复制到聚合目录"
            fi
        else
            echo "⚠️  无法为 $service_name 生成覆盖率报告，跳过处理"
        fi
    fi
done

# 创建简单的聚合报告索引页面
echo "\n=========================================="
echo "📋 生成聚合报告索引页面..."

cat > "$AGGREGATE_DIR/index.html" << EOF
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>GMP系统 - 覆盖率报告汇总</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            background-color: #f5f5f5;
        }
        h1 {
            color: #333;
        }
        .summary {
            background-color: white;
            padding: 15px;
            border-radius: 5px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            margin-bottom: 20px;
        }
        .service-list {
            background-color: white;
            padding: 15px;
            border-radius: 5px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .service-item {
            padding: 10px 0;
            border-bottom: 1px solid #eee;
        }
        .service-item:last-child {
            border-bottom: none;
        }
        .success {
            color: #4CAF50;
        }
        .fail {
            color: #F44336;
        }
        a {
            color: #2196F3;
            text-decoration: none;
        }
        a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <h1>GMP系统 - 覆盖率报告汇总</h1>
    <div class="summary">
        <h2>测试结果摘要</h2>
        <p>总服务数: $(( ${#successful_services[@]} + ${#failed_services[@]} ))</p>
        <p>成功服务数: <span class="success">${#successful_services[@]}</span></p>
        <p>失败服务数: <span class="fail">${#failed_services[@]}</span></p>
    </div>
    <div class="service-list">
        <h2>服务覆盖率报告</h2>
        <h3>成功服务:</h3>
        <div>
EOF

for service in "${successful_services[@]}"; do
    echo "            <div class='service-item'><span class='success'>✅</span> <a href='$service/index.html' target='_blank'>$service</a></div>" >> "$AGGREGATE_DIR/index.html"
done

echo "        </div>" >> "$AGGREGATE_DIR/index.html"

if [ ${#failed_services[@]} -gt 0 ]; then
    echo "        <h3>失败服务:</h3>" >> "$AGGREGATE_DIR/index.html"
    echo "        <div>" >> "$AGGREGATE_DIR/index.html"
    for service in "${failed_services[@]}"; do
        echo "            <div class='service-item'><span class='fail'>❌</span> $service</div>" >> "$AGGREGATE_DIR/index.html"
    done
    echo "        </div>" >> "$AGGREGATE_DIR/index.html"
fi

echo "    </div>" >> "$AGGREGATE_DIR/index.html"
echo "</body>" >> "$AGGREGATE_DIR/index.html"
echo "</html>" >> "$AGGREGATE_DIR/index.html"

echo "✅ 聚合报告索引页面已生成: $AGGREGATE_DIR/index.html"
echo "\n=========================================="
echo "📊 覆盖率报告生成结果摘要"
echo "=========================================="
echo "成功服务: ${#successful_services[@]}"
if [ ${#successful_services[@]} -gt 0 ]; then
    echo "  - ${successful_services[*]}"
fi
echo "失败服务: ${#failed_services[@]}"
if [ ${#failed_services[@]} -gt 0 ]; then
    echo "  - ${failed_services[*]}"
fi
echo "\n聚合报告索引: file://$AGGREGATE_DIR/index.html"
echo "\n🎉 覆盖率报告生成完成!"
#!/bin/bash

# GMP系统开发环境停止脚本

echo "🛑 停止GMP系统开发环境..."

# 定义颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

# 停止微服务进程
echo -e "${BLUE}停止微服务进程...${NC}"

# 定义服务名称和端口对应关系
declare -A services=(
    ["config-service"]="8086"
    ["auth-service"]="8085"
    ["gateway"]="8080"
    ["qms-service"]="8081"
    ["mes-service"]="8082"
    ["lims-service"]="8083"
    ["edms-service"]="8084"
    ["message-service"]="8087"
    ["file-service"]="8088"
)

# 停止Java进程
for service in "${!services[@]}"; do
    port=${services[$service]}
    pid=$(lsof -ti:$port 2>/dev/null)
    if [ ! -z "$pid" ]; then
        echo -e "${BLUE}停止${service} (PID: $pid, Port: $port)...${NC}"
        kill -TERM $pid 2>/dev/null || true
        sleep 3
        # 强制杀死如果还没停止
        if kill -0 $pid 2>/dev/null; then
            echo -e "${YELLOW}强制停止${service}...${NC}"
            kill -KILL $pid 2>/dev/null || true
        fi
    fi
done

# 停止Docker容器
echo -e "${BLUE}停止Docker基础设施服务...${NC}"
if command -v docker-compose &> /dev/null; then
    docker-compose down
else
    docker compose down
fi

# 清理日志文件
echo -e "${BLUE}清理临时文件...${NC}"
rm -rf logs/*.log
rm -rf tmp/*

echo -e "${GREEN}✅ GMP系统开发环境已停止${NC}"
echo -e "${YELLOW}💡 提示: 如需完全清理数据卷，请运行: docker volume prune${NC}"

#!/bin/bash

# GMP系统开发环境启动脚本
# 此脚本将自动启动所有基础服务和微服务

set -e

echo "🚀 启动GMP系统开发环境..."

# 定义颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 检查Docker是否安装
if ! command -v docker &> /dev/null; then
    echo -e "${RED}❌ Docker 未安装，请先安装Docker${NC}"
    exit 1
fi

# 检查Docker Compose是否安装
if ! command -v docker-compose &> /dev/null && ! docker compose version &> /dev/null; then
    echo -e "${RED}❌ Docker Compose 未安装，请先安装Docker Compose${NC}"
    exit 1
fi

# 检查Java 17是否安装
if ! command -v java &> /dev/null || ! java -version 2>&1 | grep -q "17"; then
    echo -e "${YELLOW}⚠️  警告: 未检测到Java 17，请确保已正确安装${NC}"
fi

# 创建必要的目录
echo -e "${BLUE}📁 创建必要的目录...${NC}"
mkdir -p infrastructure/postgres/init
mkdir -p infrastructure/redis
mkdir -p infrastructure/prometheus
mkdir -p infrastructure/grafana/dashboards
mkdir -p infrastructure/grafana/provisioning/datasources
mkdir -p infrastructure/grafana/provisioning/dashboards
mkdir -p logs

# 步骤1: 启动基础设施服务
echo -e "${BLUE}🏗️  启动基础设施服务 (PostgreSQL, Redis, RabbitMQ, MinIO, Prometheus, Grafana)...${NC}"
if command -v docker-compose &> /dev/null; then
    docker-compose up -d
else
    docker compose up -d
fi

# 等待基础设施服务启动
echo -e "${YELLOW}⏳ 等待基础设施服务启动...${NC}"
sleep 30

# 检查服务健康状态
echo -e "${BLUE}🔍 检查基础设施服务健康状态...${NC}"

# 检查PostgreSQL
if docker ps | grep -q gmp-postgres; then
    echo -e "${GREEN}✅ PostgreSQL 运行正常${NC}"
else
    echo -e "${RED}❌ PostgreSQL 启动失败${NC}"
fi

# 检查Redis
if docker ps | grep -q gmp-redis; then
    echo -e "${GREEN}✅ Redis 运行正常${NC}"
else
    echo -e "${RED}❌ Redis 启动失败${NC}"
fi

# 检查RabbitMQ
if docker ps | grep -q gmp-rabbitmq; then
    echo -e "${GREEN}✅ RabbitMQ 运行正常${NC}"
else
    echo -e "${RED}❌ RabbitMQ 启动失败${NC}"
fi

# 检查MinIO
if docker ps | grep -q gmp-minio; then
    echo -e "${GREEN}✅ MinIO 运行正常${NC}"
else
    echo -e "${RED}❌ MinIO 启动失败${NC}"
fi

# 步骤2: 编译微服务
echo -e "${BLUE}🔨 编译微服务项目...${NC}"
if command -v mvn &> /dev/null; then
    mvn clean compile -DskipTests
    echo -e "${GREEN}✅ Maven 编译完成${NC}"
else
    echo -e "${YELLOW}⚠️  未检测到Maven，请手动编译项目${NC}"
fi

# 步骤3: 启动微服务
echo -e "${BLUE}🚀 启动微服务...${NC}"

# 启动Config Service (如果存在)
if [ -d "services/config-service" ]; then
    echo -e "${BLUE}启动配置中心服务...${NC}"
    nohup java -jar services/config-service/target/*.jar > logs/config-service.log 2>&1 &
    sleep 10
fi

# 启动Auth Service
if [ -d "services/auth-service" ]; then
    echo -e "${BLUE}启动认证服务...${NC}"
    nohup java -jar services/auth-service/target/*.jar > logs/auth-service.log 2>&1 &
    sleep 10
fi

# 启动API Gateway
if [ -d "services/gateway" ]; then
    echo -e "${BLUE}启动API网关...${NC}"
    nohup java -jar services/gateway/target/*.jar > logs/gateway.log 2>&1 &
    sleep 10
fi

# 启动QMS Service
if [ -d "services/qms-service" ]; then
    echo -e "${BLUE}启动质量管理系统服务...${NC}"
    nohup java -jar services/qms-service/target/*.jar > logs/qms-service.log 2>&1 &
    sleep 10
fi

# 启动其他服务 (MES, LIMS, EDMS, Message, File)
for service in mes-service lims-service edms-service message-service file-service; do
    if [ -d "services/$service" ]; then
        echo -e "${BLUE}启动${service}...${NC}"
        nohup java -jar services/$service/target/*.jar > logs/$service.log 2>&1 &
        sleep 5
    fi
done

echo -e "${GREEN}🎉 GMP系统开发环境启动完成!${NC}"
echo ""
echo -e "${BLUE}📋 服务访问地址:${NC}"
echo -e "${BLUE}  API网关:        http://localhost:8080${NC}"
echo -e "${BLUE}  Eureka注册中心: http://localhost:8761${NC}"
echo -e "${BLUE}  Prometheus:     http://localhost:9090${NC}"
echo -e "${BLUE}  Grafana:        http://localhost:3000 (admin/admin123)${NC}"
echo -e "${BLUE}  RabbitMQ管理界面: http://localhost:15672 (admin/admin123)${NC}"
echo -e "${BLUE}  MinIO控制台:    http://localhost:9001 (gmp_minio_admin/gmp_minio_password_2024)${NC}"
echo ""
echo -e "${YELLOW}💡 提示:${NC}"
echo -e "${YELLOW}  - 查看日志: tail -f logs/*.log${NC}"
echo -e "${YELLOW}  - 停止服务: ./scripts/stop-dev.sh${NC}"
echo -e "${YELLOW}  - 重启服务: ./scripts/restart-dev.sh${NC}"

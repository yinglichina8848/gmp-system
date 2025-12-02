#!/bin/bash

# 设置环境变量避免密码输入
export PGPASSWORD="postgres"

# 设置颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${YELLOW}=== 开始自动化测试执行 ===${NC}"

# 1. 检查数据库连接
echo -e "${YELLOW}[1/5] 检查数据库连接...${NC}"
if psql -h localhost -U postgres -d auth_db -c "SELECT 1;" > /dev/null 2>&1; then
    echo -e "${GREEN}✓ 数据库连接成功${NC}"
else
    echo -e "${RED}✗ 数据库连接失败${NC}"
    exit 1
fi

# 2. 检查sys_users表结构
echo -e "${YELLOW}[2/5] 检查sys_users表结构...${NC}"
psql -h localhost -U postgres -d auth_db -c "
SELECT column_name, data_type, is_nullable 
FROM information_schema.columns 
WHERE table_name = 'sys_users' 
ORDER BY ordinal_position;
"

# 3. 检查mfa相关字段是否存在
echo -e "${YELLOW}[3/5] 检查MFA相关字段...${NC}"
psql -h localhost -U postgres -d auth_db -c "
SELECT column_name, data_type, is_nullable 
FROM information_schema.columns 
WHERE table_name = 'sys_users' 
AND column_name LIKE 'mfa%';
"

# 4. 如果mfa_enabled字段不存在，则添加
echo -e "${YELLOW}[4/5] 检查并添加缺失的MFA字段...${NC}"
MFA_FIELDS_EXIST=$(psql -h localhost -U postgres -d auth_db -t -c "
SELECT COUNT(*) FROM information_schema.columns 
WHERE table_name = 'sys_users' AND column_name = 'mfa_enabled';
" | tr -d ' ')

if [ "$MFA_FIELDS_EXIST" -eq "0" ]; then
    echo -e "${YELLOW}添加mfa_enabled字段...${NC}"
    psql -h localhost -U postgres -d auth_db -c "
    ALTER TABLE sys_users ADD COLUMN mfa_enabled BOOLEAN NOT NULL DEFAULT false;
    "
    echo -e "${GREEN}✓ mfa_enabled字段添加成功${NC}"
else
    echo -e "${GREEN}✓ mfa_enabled字段已存在${NC}"
fi

# 5. 运行测试并收集覆盖率数据
echo -e "${YELLOW}[5/5] 运行认证服务测试...${NC}"
cd /home/liying/gmp-system/services/auth-service

# 清理之前的测试结果
echo "清理之前的测试结果..."
rm -rf target/surefire-reports/* target/site/jacoco/*

# 运行测试并生成覆盖率报告
echo "运行测试套件..."
mvn clean test jacoco:report

# 检查测试结果
if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ 测试执行成功${NC}"
    
    # 显示覆盖率报告位置
    echo -e "${GREEN}覆盖率报告生成在: target/site/jacoco/index.html${NC}"
    
    # 显示测试失败统计
    FAILED_TESTS=$(find target/surefire-reports -name "*.txt" -exec grep -l "FAILED" {} \; | wc -l)
    echo -e "${GREEN}失败的测试数量: $FAILED_TESTS${NC}"
    
    # 显示详细的测试结果
    echo -e "${YELLOW}测试结果摘要:${NC}"
    find target/surefire-reports -name "*.txt" -exec echo "=== {} ===" \; -exec tail -5 {} \;
else
    echo -e "${RED}✗ 测试执行失败${NC}"
    
    # 显示详细的错误信息
    echo -e "${RED}错误详情:${NC}"
    find target/surefire-reports -name "*.txt" -exec echo "=== {} ===" \; -exec tail -10 {} \;
    exit 1
fi

echo -e "${GREEN}=== 自动化测试执行完成 ===${NC}"
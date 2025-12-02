#!/bin/bash

# PostgreSQL测试脚本 - 使用postgres作为密码
# 避免交互式密码输入

echo "开始执行数据库测试脚本..."

# 设置环境变量避免密码提示
export PGPASSWORD="postgres"

# 检查数据库连接
echo "1. 检查数据库连接..."
psql -h localhost -U postgres -d postgres -c "SELECT version();"

# 检查auth_db数据库是否存在
echo "2. 检查auth_db数据库..."
psql -h localhost -U postgres -d postgres -c "SELECT datname FROM pg_database WHERE datname = 'auth_db';"

# 连接到auth_db并检查sys_users表结构
echo "3. 检查sys_users表结构..."
psql -h localhost -U postgres -d auth_db -c "\d sys_users"

# 检查sys_users表是否包含mfa_enabled字段
echo "4. 检查mfa_enabled字段是否存在..."
psql -h localhost -U postgres -d auth_db -c "SELECT column_name FROM information_schema.columns WHERE table_name = 'sys_users' AND column_name = 'mfa_enabled';"

# 如果mfa_enabled字段不存在，则添加它
echo "5. 检查是否需要添加mfa_enabled字段..."
psql -h localhost -U postgres -d auth_db -c "
DO \$\$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_name = 'sys_users' AND column_name = 'mfa_enabled') THEN
        ALTER TABLE sys_users ADD COLUMN mfa_enabled BOOLEAN NOT NULL DEFAULT false;
        RAISE NOTICE 'mfa_enabled字段已添加';
    ELSE
        RAISE NOTICE 'mfa_enabled字段已存在';
    END IF;
END
\$\$;
"

# 检查其他MFA相关字段
echo "6. 检查其他MFA相关字段..."
psql -h localhost -U postgres -d auth_db -c "
SELECT column_name, data_type, is_nullable, column_default
FROM information_schema.columns 
WHERE table_name = 'sys_users' 
AND column_name IN ('mfa_secret_key', 'mfa_last_verified', 'mfa_recovery_codes');
"

# 验证表结构
echo "7. 验证sys_users表完整结构..."
psql -h localhost -U postgres -d auth_db -c "
SELECT column_name, data_type, is_nullable, column_default
FROM information_schema.columns 
WHERE table_name = 'sys_users' 
ORDER BY ordinal_position;
"

# 测试数据插入
echo "8. 测试数据插入功能..."
psql -h localhost -U postgres -d auth_db -c "
INSERT INTO sys_users (username, password_hash, email, user_status, mfa_enabled) 
VALUES ('test_user', 'test_password', 'test@example.com', 'ACTIVE', false)
ON CONFLICT (username) DO NOTHING;
"

# 验证数据插入
echo "9. 验证测试数据..."
psql -h localhost -U postgres -d auth_db -c "
SELECT username, email, user_status, mfa_enabled FROM sys_users WHERE username = 'test_user';
"

# 清理测试数据
echo "10. 清理测试数据..."
psql -h localhost -U postgres -d auth_db -c "
DELETE FROM sys_users WHERE username = 'test_user';
"

echo "数据库测试脚本执行完成！"

# 清除密码环境变量
unset PGPASSWORD
-- GMP系统数据库检查脚本
-- 用于验证数据库和表结构是否正确创建

\echo '========== GMP系统数据库检查 =========='
\echo ''

-- 检查所有数据库是否存在
\echo '检查数据库存在性:'
SELECT datname FROM pg_database WHERE datname IN ('qms_db', 'mes_db', 'lims_db', 'edms_db', 'auth_db', 'config_db', 'message_db', 'file_db');
\echo ''

-- 切换到auth_db数据库进行表结构检查
\c auth_db;
\echo '切换到auth_db数据库，检查表结构...'
\echo ''

-- 检查核心表是否存在
\echo '检查核心表是否存在:'
SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' AND table_type = 'BASE TABLE'
ORDER BY table_name;
\echo ''

-- 检查每个表的记录数
\echo '检查核心表记录数:'
SELECT 'sys_users' AS table_name, COUNT(*) AS record_count FROM sys_users;
SELECT 'sys_roles' AS table_name, COUNT(*) AS record_count FROM sys_roles;
SELECT 'sys_permissions' AS table_name, COUNT(*) AS record_count FROM sys_permissions;
SELECT 'sys_user_roles' AS table_name, COUNT(*) AS record_count FROM sys_user_roles;
SELECT 'role_permissions' AS table_name, COUNT(*) AS record_count FROM role_permissions;
SELECT 'sys_organizations' AS table_name, COUNT(*) AS record_count FROM sys_organizations;
SELECT 'sys_user_org_roles' AS table_name, COUNT(*) AS record_count FROM sys_user_org_roles;
\echo ''

-- 检查管理员用户
\echo '检查管理员用户:'
SELECT id, username, full_name, user_status FROM sys_users WHERE username = 'admin';
\echo ''

-- 检查用户角色分配
\echo '检查用户角色分配:'
SELECT u.username, r.role_code, r.role_name 
FROM sys_users u 
JOIN sys_user_roles ur ON u.id = ur.user_id 
JOIN sys_roles r ON ur.role_id = r.id
ORDER BY u.username, r.role_code;
\echo ''

-- 检查组织数据
\echo '检查组织数据:'
SELECT id, name, code, type, level FROM sys_organizations ORDER BY level, id;
\echo ''

-- 检查用户组织角色关联
\echo '检查用户组织角色关联:'
SELECT u.username, o.code AS org_code, o.name AS org_name, r.role_code 
FROM sys_user_org_roles uor
JOIN sys_users u ON uor.user_id = u.id
JOIN sys_organizations o ON uor.organization_id = o.id
JOIN sys_roles r ON uor.role_id = r.id
ORDER BY u.username, o.code;
\echo ''

-- 检查权限分配
\echo '检查权限分配:'
SELECT r.role_code, COUNT(p.id) as permission_count
FROM sys_roles r
LEFT JOIN role_permissions rp ON r.id = rp.role_id
LEFT JOIN sys_permissions p ON rp.permission_id = p.id
GROUP BY r.role_code
ORDER BY r.role_code;
\echo ''

-- 检查视图
\echo '检查数据库视图:'
SELECT table_name FROM information_schema.views WHERE table_schema = 'public';
\echo ''

-- 检查函数
\echo '检查数据库函数:'
SELECT routine_name FROM information_schema.routines WHERE routine_schema = 'public' AND routine_type = 'FUNCTION';
\echo ''

-- 检查触发器
\echo '检查数据库触发器:'
SELECT trigger_name, event_object_table FROM information_schema.triggers WHERE trigger_schema = 'public';
\echo ''

-- 检查用户权限
\echo '检查gmp_user用户权限:'
SELECT grantee, table_name, privilege_type 
FROM information_schema.role_table_grants 
WHERE grantee = 'gmp_user' AND table_schema = 'public'
ORDER BY table_name, privilege_type;
\echo ''

\echo '========== 数据库检查完成 =========='
\echo '如果所有检查都显示了预期的数据，则数据库初始化成功。'
\echo '如果有任何表不存在或数据缺失，请运行 integrated_db_init.sql 脚本重新初始化数据库。'
\echo '示例用户密码为: Example123 (除admin用户密码为: Admin123!)'

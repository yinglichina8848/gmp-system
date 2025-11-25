-- GMP系统示例性用户数据
-- 此脚本添加各类角色的示例性用户及其关联数据

-- 确保在auth_db数据库中执行
\c auth_db;

BEGIN;

-- 添加更多示例用户
INSERT INTO sys_users (username, password_hash, email, mobile, full_name, user_status, last_login_time, created_at, created_by) 
VALUES 
-- GMP管理员用户
('gmp_admin', '$2a$10$eXVjaGFuZ2VkLmJvb2tzLnBhc3N3b3JkLmZvcm0uZG9uZXR0cnk$mJ8Xh3rD7Cj1D3F3tV8BjO6H5K6L7M8N9O0P1Q2R3S4T5U6V7W8', 'gmp_admin@example.com', '13800138001', '王GMP', 'ACTIVE', NULL, NOW(), 1),
-- 质量管理员用户
('quality_admin', '$2a$10$eXVjaGFuZ2VkLmJvb2tzLnBhc3N3b3JkLmZvcm0uZG9uZXR0cnk$mJ8Xh3rD7Cj1D3F3tV8BjO6H5K6L7M8N9O0P1Q2R3S4T5U6V7W8', 'quality_admin@example.com', '13800138002', '李质量', 'ACTIVE', NULL, NOW(), 1),
-- 生产管理员用户
('production_admin', '$2a$10$eXVjaGFuZ2VkLmJvb2tzLnBhc3N3b3JkLmZvcm0uZG9uZXR0cnk$mJ8Xh3rD7Cj1D3F3tV8BjO6H5K6L7M8N9O0P1Q2R3S4T5U6V7W8', 'production_admin@example.com', '13800138003', '张生产', 'ACTIVE', NULL, NOW(), 1),
-- 实验室管理员用户
('lab_admin', '$2a$10$eXVjaGFuZ2VkLmJvb2tzLnBhc3N3b3JkLmZvcm0uZG9uZXR0cnk$mJ8Xh3rD7Cj1D3F3tV8BjO6H5K6L7M8N9O0P1Q2R3S4T5U6V7W8', 'lab_admin@example.com', '13800138004', '赵实验', 'ACTIVE', NULL, NOW(), 1),
-- 普通操作员用户
('operator1', '$2a$10$eXVjaGFuZ2VkLmJvb2tzLnBhc3N3b3JkLmZvcm0uZG9uZXR0cnk$mJ8Xh3rD7Cj1D3F3tV8BjO6H5K6L7M8N9O0P1Q2R3S4T5U6V7W8', 'operator1@example.com', '13800138005', '操作员小王', 'ACTIVE', NULL, NOW(), 1),
-- 普通操作员用户2
('operator2', '$2a$10$eXVjaGFuZ2VkLmJvb2tzLnBhc3N3b3JkLmZvcm0uZG9uZXR0cnk$mJ8Xh3rD7Cj1D3F3tV8BjO6H5K6L7M8N9O0P1Q2R3S4T5U6V7W8', 'operator2@example.com', '13800138006', '操作员小李', 'ACTIVE', NULL, NOW(), 1)
ON CONFLICT (username) DO NOTHING;

-- 为用户分配角色
-- 根据role_code分配，避免硬编码ID

-- 为GMP管理员用户分配角色
INSERT INTO sys_user_roles (user_id, role_id, is_active, assigned_at, assigned_by) 
SELECT u.id, r.id, true, NOW(), 1 
FROM sys_users u, sys_roles r
WHERE u.username = 'gmp_admin' AND r.role_code = 'ROLE_GMP_ADMIN'
ON CONFLICT (user_id, role_id) DO NOTHING;

-- 为质量管理员用户分配角色
INSERT INTO sys_user_roles (user_id, role_id, is_active, assigned_at, assigned_by) 
SELECT u.id, r.id, true, NOW(), 1 
FROM sys_users u, sys_roles r
WHERE u.username = 'quality_admin' AND r.role_code = 'ROLE_QMS_MANAGER'
ON CONFLICT (user_id, role_id) DO NOTHING;

-- 为生产管理员用户分配角色
INSERT INTO sys_user_roles (user_id, role_id, is_active, assigned_at, assigned_by) 
SELECT u.id, r.id, true, NOW(), 1 
FROM sys_users u, sys_roles r
WHERE u.username = 'production_admin' AND r.role_code = 'ROLE_MES_MANAGER'
ON CONFLICT (user_id, role_id) DO NOTHING;

-- 为实验室管理员用户分配角色
INSERT INTO sys_user_roles (user_id, role_id, is_active, assigned_at, assigned_by) 
SELECT u.id, r.id, true, NOW(), 1 
FROM sys_users u, sys_roles r
WHERE u.username = 'lab_admin' AND r.role_code = 'ROLE_LIMS_MANAGER'
ON CONFLICT (user_id, role_id) DO NOTHING;

-- 为操作员用户分配角色
INSERT INTO sys_user_roles (user_id, role_id, is_active, assigned_at, assigned_by) 
SELECT u.id, r.id, true, NOW(), 1 
FROM sys_users u, sys_roles r
WHERE u.username IN ('operator1', 'operator2') AND r.role_code = 'ROLE_OPERATOR'
ON CONFLICT (user_id, role_id) DO NOTHING;

-- 创建组织数据（如果还没有）
INSERT INTO sys_organizations (name, code, type, description, parent_id, created_at, created_by) 
VALUES 
('总公司', 'HQ', 'COMPANY', '公司总部', NULL, NOW(), 'system'),
('生产一部', 'PROD1', 'DEPARTMENT', '第一生产部门', 1, NOW(), 'system'),
('生产二部', 'PROD2', 'DEPARTMENT', '第二生产部门', 1, NOW(), 'system'),
('质量管理部', 'QA', 'DEPARTMENT', '质量管理部门', 1, NOW(), 'system'),
('实验室', 'LAB', 'DEPARTMENT', '测试实验室', 1, NOW(), 'system')
ON CONFLICT (code) DO NOTHING;

-- 为用户分配组织角色关系
-- GMP管理员分配到总公司
INSERT INTO sys_user_org_roles (user_id, organization_id, role_id, status, valid_from, valid_to, assigned_by, created_at) 
SELECT u.id, o.id, 2, 'APPROVED', NOW(), NOW() + INTERVAL '365 days', 1, NOW() 
FROM sys_users u, sys_organizations o 
WHERE u.username = 'gmp_admin' AND o.code = 'HQ' 
ON CONFLICT (user_id, organization_id, role_id) DO NOTHING;

-- 质量管理员分配到质量管理部
INSERT INTO sys_user_org_roles (user_id, organization_id, role_id, status, valid_from, valid_to, assigned_by, created_at) 
SELECT u.id, o.id, 3, 'APPROVED', NOW(), NOW() + INTERVAL '365 days', 1, NOW() 
FROM sys_users u, sys_organizations o 
WHERE u.username = 'quality_admin' AND o.code = 'QA' 
ON CONFLICT (user_id, organization_id, role_id) DO NOTHING;

-- 生产管理员分配到生产一部
INSERT INTO sys_user_org_roles (user_id, organization_id, role_id, status, valid_from, valid_to, assigned_by, created_at) 
SELECT u.id, o.id, 4, 'APPROVED', NOW(), NOW() + INTERVAL '365 days', 1, NOW() 
FROM sys_users u, sys_organizations o 
WHERE u.username = 'production_admin' AND o.code = 'PROD1' 
ON CONFLICT (user_id, organization_id, role_id) DO NOTHING;

-- 实验室管理员分配到实验室
INSERT INTO sys_user_org_roles (user_id, organization_id, role_id, status, valid_from, valid_to, assigned_by, created_at) 
SELECT u.id, o.id, 5, 'APPROVED', NOW(), NOW() + INTERVAL '365 days', 1, NOW() 
FROM sys_users u, sys_organizations o 
WHERE u.username = 'lab_admin' AND o.code = 'LAB' 
ON CONFLICT (user_id, organization_id, role_id) DO NOTHING;

-- 操作员分配到不同部门
INSERT INTO sys_user_org_roles (user_id, organization_id, role_id, status, valid_from, valid_to, assigned_by, created_at) 
SELECT u.id, o.id, 6, 'APPROVED', NOW(), NOW() + INTERVAL '365 days', 1, NOW() 
FROM sys_users u, sys_organizations o 
WHERE (u.username = 'operator1' AND o.code = 'PROD1') OR (u.username = 'operator2' AND o.code = 'PROD2') 
ON CONFLICT (user_id, organization_id, role_id) DO NOTHING;

-- 为各角色分配子系统访问权限
-- 假设子系统权限ID从100开始
-- 系统管理子系统权限
INSERT INTO sys_permissions (code, name, description, category, resource_type, resource_id, action, created_at, created_by) 
VALUES 
('SYSTEM_ACCESS', '系统管理访问权限', '访问系统管理子系统', 'SUBSYSTEM', 'SYSTEM', 'SYSTEM_MANAGEMENT', 'ACCESS', NOW(), 'system'),
('GMP_ACCESS', 'GMP管理访问权限', '访问GMP管理子系统', 'SUBSYSTEM', 'SYSTEM', 'GMP_MANAGEMENT', 'ACCESS', NOW(), 'system'),
('QUALITY_ACCESS', '质量管理访问权限', '访问质量管理子系统', 'SUBSYSTEM', 'SYSTEM', 'QUALITY_MANAGEMENT', 'ACCESS', NOW(), 'system'),
('PRODUCTION_ACCESS', '生产管理访问权限', '访问生产管理子系统', 'SUBSYSTEM', 'SYSTEM', 'PRODUCTION_MANAGEMENT', 'ACCESS', NOW(), 'system'),
('LAB_ACCESS', '实验室管理访问权限', '访问实验室管理子系统', 'SUBSYSTEM', 'SYSTEM', 'LAB_MANAGEMENT', 'ACCESS', NOW(), 'system')
ON CONFLICT (code) DO NOTHING;

-- 为各角色分配子系统访问权限
-- 系统管理员拥有所有子系统访问权限
INSERT INTO role_permissions (role_id, permission_id, is_active, created_at) 
SELECT r.id, p.id, true, NOW() 
FROM sys_roles r, sys_permissions p 
WHERE r.code = 'ADMIN' AND p.code IN ('SYSTEM_ACCESS', 'GMP_ACCESS', 'QUALITY_ACCESS', 'PRODUCTION_ACCESS', 'LAB_ACCESS') 
ON CONFLICT (role_id, permission_id) DO NOTHING;

-- GMP管理员拥有系统管理和GMP管理权限
INSERT INTO role_permissions (role_id, permission_id, is_active, created_at) 
SELECT r.id, p.id, true, NOW() 
FROM sys_roles r, sys_permissions p 
WHERE r.code = 'GMP_ADMIN' AND p.code IN ('SYSTEM_ACCESS', 'GMP_ACCESS') 
ON CONFLICT (role_id, permission_id) DO NOTHING;

-- 质量管理员拥有质量管理权限
INSERT INTO role_permissions (role_id, permission_id, is_active, created_at) 
SELECT r.id, p.id, true, NOW() 
FROM sys_roles r, sys_permissions p 
WHERE r.code = 'QUALITY_ADMIN' AND p.code = 'QUALITY_ACCESS' 
ON CONFLICT (role_id, permission_id) DO NOTHING;

-- 生产管理员拥有生产管理权限
INSERT INTO role_permissions (role_id, permission_id, is_active, created_at) 
SELECT r.id, p.id, true, NOW() 
FROM sys_roles r, sys_permissions p 
WHERE r.code = 'PRODUCTION_ADMIN' AND p.code = 'PRODUCTION_ACCESS' 
ON CONFLICT (role_id, permission_id) DO NOTHING;

-- 实验室管理员拥有实验室管理权限
INSERT INTO role_permissions (role_id, permission_id, is_active, created_at) 
SELECT r.id, p.id, true, NOW() 
FROM sys_roles r, sys_permissions p 
WHERE r.code = 'LAB_ADMIN' AND p.code = 'LAB_ACCESS' 
ON CONFLICT (role_id, permission_id) DO NOTHING;

-- 操作员拥有生产管理和实验室管理权限（根据所在部门）
INSERT INTO role_permissions (role_id, permission_id, is_active, created_at) 
SELECT r.id, p.id, true, NOW() 
FROM sys_roles r, sys_permissions p 
WHERE r.code = 'OPERATOR' AND p.code IN ('PRODUCTION_ACCESS', 'LAB_ACCESS') 
ON CONFLICT (role_id, permission_id) DO NOTHING;

COMMIT;

-- 输出执行结果
SELECT '示例性用户数据初始化完成' AS result;
SELECT '用户名' as username, '密码' as password, '角色' as role FROM sys_users LIMIT 0 
UNION ALL
SELECT 'admin', 'Admin123!', '系统管理员' 
UNION ALL
SELECT 'gmp_admin', 'Example123', 'GMP管理员' 
UNION ALL
SELECT 'quality_admin', 'Example123', '质量管理员' 
UNION ALL
SELECT 'production_admin', 'Example123', '生产管理员' 
UNION ALL
SELECT 'lab_admin', 'Example123', '实验室管理员' 
UNION ALL
SELECT 'operator1', 'Example123', '操作员(生产一部)' 
UNION ALL
SELECT 'operator2', 'Example123', '操作员(生产二部)';

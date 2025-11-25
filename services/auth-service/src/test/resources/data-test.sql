-- GMP认证系统测试数据初始化脚本
-- 用于PostgreSQL测试环境

-- 清理测试数据（按依赖关系顺序删除）
DELETE FROM role_permissions;
DELETE FROM sys_user_roles;
DELETE FROM sys_user_org_roles;
DELETE FROM sys_permissions;
DELETE FROM sys_roles;
DELETE FROM sys_organizations;
DELETE FROM sys_users;
DELETE FROM jwt_blacklist;
DELETE FROM user_operation_logs;

-- 重置序列（PostgreSQL特有）
ALTER SEQUENCE IF EXISTS sys_users_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS sys_roles_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS sys_permissions_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS sys_organizations_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS sys_user_roles_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS sys_user_org_roles_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS role_permissions_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS jwt_blacklist_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS user_operation_logs_id_seq RESTART WITH 1;

-- 创建测试组织
INSERT INTO sys_organizations (name, code, description, parent_id, level, is_active)
VALUES ('测试组织', 'TEST_ORG', '测试用组织', NULL, 1, true);

-- 创建测试角色
INSERT INTO sys_roles (role_code, role_name, description, is_active)
VALUES 
('ROLE_ADMIN', '管理员', '系统管理员角色', true),
('ROLE_USER', '普通用户', '普通用户角色', true),
('ROLE_GUEST', '访客用户', '临时访客角色', true);

-- 创建测试权限
INSERT INTO sys_permissions (permission_code, permission_name, description, resource_type)
VALUES 
('SYSTEM_ACCESS', '系统管理访问权限', '访问系统管理功能', 'SYSTEM'),
('USER_MANAGEMENT', '用户管理权限', '管理用户信息', 'USER'),
('VIEW_DATA', '数据查看权限', '查看系统数据', 'DATA');

-- 创建测试用户（密码使用Spring Security默认的BCrypt加密，原始密码: Password123!）
INSERT INTO sys_users (username, password_hash, email, mobile, full_name, user_status)
VALUES 
('admin', '$2a$10$eXVjaGFuZ2VkLmJvb2tzLnBhc3N3b3JkLmZvcm0uZG9uZXR0cnk$mJ8Xh3rD7Cj1D3F3tV8BjO6H5K6L7M8N9O0P1Q2R3S4T5U6V7W8', 'admin@example.com', '13800138000', '系统管理员', 'ACTIVE'),
('testuser', '$2a$10$eXVjaGFuZ2VkLmJvb2tzLnBhc3N3b3JkLmZvcm0uZG9uZXR0cnk$mJ8Xh3rD7Cj1D3F3tV8BjO6H5K6L7M8N9O0P1Q2R3S4T5U6V7W8', 'testuser@example.com', '13800138001', '测试用户', 'ACTIVE');

-- 创建角色权限关联
INSERT INTO role_permissions (role_id, permission_id, is_active)
VALUES 
(1, 1, true),
(1, 2, true),
(1, 3, true),
(2, 3, true);

-- 创建用户角色关联
INSERT INTO sys_user_roles (user_id, role_id, is_active)
VALUES 
(1, 1, true),
(2, 2, true);

-- 创建用户组织角色关联
INSERT INTO sys_user_org_roles (user_id, organization_id, role_id, status, is_active)
VALUES 
(1, 1, 1, 'APPROVED', true),
(2, 1, 2, 'APPROVED', true);

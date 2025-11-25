-- GMP认证系统示例数据准备脚本
-- 用于测试环境的数据初始化

-- 切换到auth_db数据库
\c auth_db;

-- ===================================================================
-- 创建更多示例用户数据
-- ===================================================================

-- 创建GMP管理员用户
INSERT INTO sys_users (
    username, email, full_name, password_hash,
    user_status, created_by
) VALUES (
    'gmp_admin',
    'gmp_admin@gmp-system.com',
    'GMP系统管理员',
    '$2b$12$gYHtbNJUOSD/VdzW2V3ZJ.7nQT/DJupfYlW7wd.7D/OtiQd3f2AOW', -- admin123
    'ACTIVE',
    1
) ON CONFLICT (username) DO NOTHING;

-- 创建质量管理角色用户
INSERT INTO sys_users (
    username, email, full_name, password_hash,
    user_status, created_by
) VALUES (
    'qms_manager',
    'qms_manager@gmp-system.com',
    '质量管理员',
    '$2b$12$gYHtbNJUOSD/VdzW2V3ZJ.7nQT/DJupfYlW7wd.7D/OtiQd3f2AOW', -- admin123
    'ACTIVE',
    1
) ON CONFLICT (username) DO NOTHING;

-- 创建生产管理角色用户
INSERT INTO sys_users (
    username, email, full_name, password_hash,
    user_status, created_by
) VALUES (
    'mes_manager',
    'mes_manager@gmp-system.com',
    '生产管理员',
    '$2b$12$gYHtbNJUOSD/VdzW2V3ZJ.7nQT/DJupfYlW7wd.7D/OtiQd3f2AOW', -- admin123
    'ACTIVE',
    1
) ON CONFLICT (username) DO NOTHING;

-- 创建实验室管理角色用户
INSERT INTO sys_users (
    username, email, full_name, password_hash,
    user_status, created_by
) VALUES (
    'lims_manager',
    'lims_manager@gmp-system.com',
    '实验室管理员',
    '$2b$12$gYHtbNJUOSD/VdzW2V3ZJ.7nQT/DJupfYlW7wd.7D/OtiQd3f2AOW', -- admin123
    'ACTIVE',
    1
) ON CONFLICT (username) DO NOTHING;

-- 创建操作员角色用户
INSERT INTO sys_users (
    username, email, full_name, password_hash,
    user_status, created_by
) VALUES (
    'operator1',
    'operator1@gmp-system.com',
    '操作员张三',
    '$2b$12$gYHtbNJUOSD/VdzW2V3ZJ.7nQT/DJupfYlW7wd.7D/OtiQd3f2AOW', -- admin123
    'ACTIVE',
    1
) ON CONFLICT (username) DO NOTHING;

INSERT INTO sys_users (
    username, email, full_name, password_hash,
    user_status, created_by
) VALUES (
    'operator2',
    'operator2@gmp-system.com',
    '操作员李四',
    '$2b$12$gYHtbNJUOSD/VdzW2V3ZJ.7nQT/DJupfYlW7wd.7D/OtiQd3f2AOW', -- admin123
    'ACTIVE',
    1
) ON CONFLICT (username) DO NOTHING;

-- ===================================================================
-- 为用户分配角色
-- ===================================================================

-- 为GMP管理员分配角色
INSERT INTO sys_user_roles (user_id, role_id, is_active, assigned_by)
SELECT u.id, r.id, TRUE, 1
FROM sys_users u, sys_roles r
WHERE u.username = 'gmp_admin' AND r.role_code = 'ROLE_GMP_ADMIN'
ON CONFLICT (user_id, role_id) DO NOTHING;

-- 为质量管理角色分配角色
INSERT INTO sys_user_roles (user_id, role_id, is_active, assigned_by)
SELECT u.id, r.id, TRUE, 1
FROM sys_users u, sys_roles r
WHERE u.username = 'qms_manager' AND r.role_code = 'ROLE_QMS_MANAGER'
ON CONFLICT DO NOTHING;

-- 为生产管理角色分配角色
INSERT INTO user_roles (user_id, role_id, is_active, assigned_by)
SELECT u.id, r.id, TRUE, 1
FROM sys_users u, sys_roles r
WHERE u.username = 'mes_manager' AND r.role_code = 'ROLE_MES_MANAGER'
ON CONFLICT DO NOTHING;

-- 为实验室管理角色分配角色
INSERT INTO user_roles (user_id, role_id, is_active, assigned_by)
SELECT u.id, r.id, TRUE, 1
FROM sys_users u, sys_roles r
WHERE u.username = 'lims_manager' AND r.role_code = 'ROLE_LIMS_MANAGER'
ON CONFLICT DO NOTHING;

-- 为操作员分配角色
INSERT INTO user_roles (user_id, role_id, is_active, assigned_by)
SELECT u.id, r.id, TRUE, 1
FROM sys_users u, sys_roles r
WHERE (u.username = 'operator1' OR u.username = 'operator2') AND r.role_code = 'ROLE_OPERATOR'
ON CONFLICT DO NOTHING;

-- ===================================================================
-- 创建一些额外的权限以测试子系统访问控制
-- ===================================================================

-- 为各子系统创建访问权限
INSERT INTO sys_permissions (permission_code, permission_name, resource_type, resource_url, http_method, description)
VALUES 
('PERMISSION_QMS_ACCESS', 'QMS系统访问权限', 'MENU', '/qms/**', 'ALL', '访问质量管理子系统的权限'),
('PERMISSION_MES_ACCESS', 'MES系统访问权限', 'MENU', '/mes/**', 'ALL', '访问生产执行子系统的权限'),
('PERMISSION_LIMS_ACCESS', 'LIMS系统访问权限', 'MENU', '/lims/**', 'ALL', '访问实验室管理子系统的权限'),
('PERMISSION_EDMS_ACCESS', 'EDMS系统访问权限', 'MENU', '/edms/**', 'ALL', '访问文档管理子系统的权限')
ON CONFLICT (permission_code) DO NOTHING;

-- 将这些权限分配给相应的角色
INSERT INTO role_permissions (role_id, permission_id, is_active, granted_by)
SELECT r.id, p.id, TRUE, 1
FROM sys_roles r, sys_permissions p
WHERE r.role_code = 'ROLE_SYSTEM_ADMIN' AND p.permission_code IN (
    'PERMISSION_QMS_ACCESS', 
    'PERMISSION_MES_ACCESS', 
    'PERMISSION_LIMS_ACCESS', 
    'PERMISSION_EDMS_ACCESS'
) ON CONFLICT DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id, is_active, granted_by)
SELECT r.id, p.id, TRUE, 1
FROM sys_roles r, sys_permissions p
WHERE r.role_code = 'ROLE_GMP_ADMIN' AND p.permission_code IN (
    'PERMISSION_QMS_ACCESS', 
    'PERMISSION_MES_ACCESS', 
    'PERMISSION_LIMS_ACCESS', 
    'PERMISSION_EDMS_ACCESS'
) ON CONFLICT DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id, is_active, granted_by)
SELECT r.id, p.id, TRUE, 1
FROM sys_roles r, sys_permissions p
WHERE r.role_code = 'ROLE_QMS_MANAGER' AND p.permission_code = 'PERMISSION_QMS_ACCESS'
ON CONFLICT DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id, is_active, granted_by)
SELECT r.id, p.id, TRUE, 1
FROM sys_roles r, sys_permissions p
WHERE r.role_code = 'ROLE_MES_MANAGER' AND p.permission_code = 'PERMISSION_MES_ACCESS'
ON CONFLICT DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id, is_active, granted_by)
SELECT r.id, p.id, TRUE, 1
FROM sys_roles r, sys_permissions p
WHERE r.role_code = 'ROLE_LIMS_MANAGER' AND p.permission_code = 'PERMISSION_LIMS_ACCESS'
ON CONFLICT DO NOTHING;

-- 操作员只分配部分子系统访问权限进行测试
INSERT INTO role_permissions (role_id, permission_id, is_active, granted_by)
SELECT r.id, p.id, TRUE, 1
FROM sys_roles r, sys_permissions p
WHERE r.role_code = 'ROLE_OPERATOR' AND p.permission_code IN ('PERMISSION_QMS_ACCESS', 'PERMISSION_MES_ACCESS')
ON CONFLICT DO NOTHING;

-- ===================================================================
-- 创建组织数据用于测试
-- ===================================================================
CREATE TABLE IF NOT EXISTS sys_organizations (
    id BIGSERIAL PRIMARY KEY,
    org_code VARCHAR(50) NOT NULL UNIQUE,
    org_name VARCHAR(200) NOT NULL,
    parent_id BIGINT,
    level INTEGER DEFAULT 1,
    is_active BOOLEAN DEFAULT TRUE,
    created_by BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version INTEGER DEFAULT 1,
    FOREIGN KEY (parent_id) REFERENCES sys_organizations(id)
);

-- 创建组织数据
INSERT INTO sys_organizations (org_code, org_name, parent_id, level, is_active, created_by)
VALUES 
('ORG_ROOT', '公司总部', NULL, 1, TRUE, 1),
('ORG_QUALITY', '质量管理部', 1, 2, TRUE, 1),
('ORG_PRODUCTION', '生产部', 1, 2, TRUE, 1),
('ORG_LAB', '实验室', 1, 2, TRUE, 1),
('ORG_LOGISTICS', '物流部', 1, 2, TRUE, 1)
ON CONFLICT (org_code) DO NOTHING;

-- 创建用户组织角色关联表
CREATE TABLE IF NOT EXISTS user_organization_roles (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    organization_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    assigned_by BIGINT,
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES sys_users(id),
    FOREIGN KEY (organization_id) REFERENCES sys_organizations(id),
    FOREIGN KEY (role_id) REFERENCES sys_roles(id),
    UNIQUE(user_id, organization_id, role_id)
);

-- 为用户分配组织角色
INSERT INTO user_organization_roles (user_id, organization_id, role_id, is_active, assigned_by)
SELECT u.id, o.id, r.id, TRUE, 1
FROM sys_users u, sys_organizations o, sys_roles r
WHERE u.username = 'admin' AND o.org_code = 'ORG_ROOT' AND r.role_code = 'ROLE_SYSTEM_ADMIN'
ON CONFLICT DO NOTHING;

INSERT INTO user_organization_roles (user_id, organization_id, role_id, is_active, assigned_by)
SELECT u.id, o.id, r.id, TRUE, 1
FROM sys_users u, sys_organizations o, sys_roles r
WHERE u.username = 'qms_manager' AND o.org_code = 'ORG_QUALITY' AND r.role_code = 'ROLE_QMS_MANAGER'
ON CONFLICT DO NOTHING;

INSERT INTO user_organization_roles (user_id, organization_id, role_id, is_active, assigned_by)
SELECT u.id, o.id, r.id, TRUE, 1
FROM sys_users u, sys_organizations o, sys_roles r
WHERE u.username = 'mes_manager' AND o.org_code = 'ORG_PRODUCTION' AND r.role_code = 'ROLE_MES_MANAGER'
ON CONFLICT DO NOTHING;

INSERT INTO user_organization_roles (user_id, organization_id, role_id, is_active, assigned_by)
SELECT u.id, o.id, r.id, TRUE, 1
FROM sys_users u, sys_organizations o, sys_roles r
WHERE u.username = 'lims_manager' AND o.org_code = 'ORG_LAB' AND r.role_code = 'ROLE_LIMS_MANAGER'
ON CONFLICT DO NOTHING;

INSERT INTO user_organization_roles (user_id, organization_id, role_id, is_active, assigned_by)
SELECT u.id, o.id, r.id, TRUE, 1
FROM sys_users u, sys_organizations o, sys_roles r
WHERE (u.username = 'operator1' OR u.username = 'operator2') 
  AND o.org_code IN ('ORG_QUALITY', 'ORG_PRODUCTION') 
  AND r.role_code = 'ROLE_OPERATOR'
ON CONFLICT DO NOTHING;

-- ===================================================================
-- 验证数据
-- ===================================================================

-- 检查用户数据
SELECT username, email, full_name, user_status FROM sys_users;

-- 检查用户角色分配
SELECT u.username, r.role_code, r.role_name 
FROM sys_users u 
JOIN user_roles ur ON u.id = ur.user_id 
JOIN sys_roles r ON ur.role_id = r.id;

-- 检查组织数据
SELECT org_code, org_name, level FROM sys_organizations;

-- 检查用户组织角色关联
SELECT u.username, o.org_code, r.role_code 
FROM user_organization_roles uor
JOIN sys_users u ON uor.user_id = u.id
JOIN sys_organizations o ON uor.organization_id = o.id
JOIN sys_roles r ON uor.role_id = r.id;

-- 检查权限分配
SELECT r.role_code, COUNT(p.id) as permission_count
FROM sys_roles r
LEFT JOIN role_permissions rp ON r.id = rp.role_id
LEFT JOIN sys_permissions p ON rp.permission_id = p.id
GROUP BY r.role_code;

-- 输出数据准备完成信息
RAISE NOTICE 'GMP系统示例数据准备完成';
RAISE NOTICE '创建了6个不同角色的示例用户，密码均为: admin123';
RAISE NOTICE '创建了5个组织单位';
RAISE NOTICE '配置了用户、角色、组织的关联关系';
RAISE NOTICE '配置了各子系统的访问权限';

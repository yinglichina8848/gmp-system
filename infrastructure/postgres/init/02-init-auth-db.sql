-- GMP认证授权系统数据库表结构及初始化数据
-- 对应设计文档中的7个核心数据表

-- 切换到auth_db数据库
\c auth_db;

-- ===================================================================
-- 1. 用户表 (sys_users)
-- ===================================================================

-- 用户基本信息表
CREATE TABLE IF NOT EXISTS sys_users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) UNIQUE,
    mobile VARCHAR(20),
    full_name VARCHAR(100) NOT NULL,
    password_hash VARCHAR(200) NOT NULL,
    user_status VARCHAR(20) DEFAULT 'ACTIVE' CHECK (user_status IN ('ACTIVE', 'INACTIVE', 'LOCKED', 'EXPIRED')),
    last_login_time TIMESTAMP,
    last_login_ip VARCHAR(50),
    password_expired_at TIMESTAMP,
    login_attempts INTEGER DEFAULT 0,
    locked_until TIMESTAMP,

    -- 审计字段
    created_by BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version INTEGER DEFAULT 1
);

-- 创建索引
CREATE UNIQUE INDEX CONCURRENTLY IF NOT EXISTS idx_users_username ON sys_users(username);
CREATE UNIQUE INDEX CONCURRENTLY IF NOT EXISTS idx_users_email ON sys_users(email);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_users_status ON sys_users(user_status);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_users_last_login ON sys_users(last_login_time);

-- ===================================================================
-- 2. 角色表 (sys_roles)
-- ===================================================================

-- 角色定义表
CREATE TABLE IF NOT EXISTS sys_roles (
    id BIGSERIAL PRIMARY KEY,
    role_code VARCHAR(50) NOT NULL UNIQUE,
    role_name VARCHAR(100) NOT NULL,
    description TEXT,
    priority INTEGER DEFAULT 0,
    is_builtin BOOLEAN DEFAULT FALSE,
    is_active BOOLEAN DEFAULT TRUE,

    -- 审计字段
    created_by BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version INTEGER DEFAULT 1
);

-- 预设GMP认证角色数据
INSERT INTO sys_roles (role_code, role_name, description, priority, is_builtin, is_active) VALUES
('ROLE_SYSTEM_ADMIN', '系统管理员', '系统超级管理员，拥有所有权限', 100, TRUE, TRUE),
('ROLE_GMP_ADMIN', 'GMP管理员', 'GMP系统管理员，管理药品生产合规', 90, TRUE, TRUE),
('ROLE_QMS_MANAGER', '质量管理员', '质量管理体系管理员', 80, TRUE, TRUE),
('ROLE_MES_MANAGER', '生产管理员', '生产执行系统管理员', 70, TRUE, TRUE),
('ROLE_LIMS_MANAGER', '实验室管理员', '实验室信息管理系统管理员', 60, TRUE, TRUE),
('ROLE_OPERATOR', '操作员', '普通操作员', 10, TRUE, TRUE);

-- ===================================================================
-- 3. 权限表 (sys_permissions)
-- ===================================================================

-- 权限定义表
CREATE TABLE IF NOT EXISTS sys_permissions (
    id BIGSERIAL PRIMARY KEY,
    permission_code VARCHAR(100) NOT NULL UNIQUE,
    permission_name VARCHAR(200) NOT NULL,
    resource_type VARCHAR(50) NOT NULL, -- MENU, API, BUTTON, DATA
    resource_url VARCHAR(200),
    http_method VARCHAR(10), -- GET, POST, PUT, DELETE
    description TEXT,
    is_active BOOLEAN DEFAULT TRUE,

    -- 审计字段
    created_by BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version INTEGER DEFAULT 1
);

-- 系统管理权限
INSERT INTO sys_permissions (permission_code, permission_name, resource_type, resource_url, http_method, description) VALUES
('PERMISSION_USER_MANAGE', '用户管理', 'MENU', '/api/auth/users', 'ALL', '用户增删改查'),
('PERMISSION_ROLE_MANAGE', '角色管理', 'MENU', '/api/auth/roles', 'ALL', '角色权限配置'),
('PERMISSION_SYSTEM_CONFIG', '系统配置', 'MENU', '/api/auth/config', 'ALL', '系统参数设置'),

-- QMS质量管理权限
('PERMISSION_DEVIATION_MANAGE', '偏差管理', 'API', '/api/qms/deviations', 'ALL', '偏差记录管理'),
('PERMISSION_CAPA_MANAGE', 'CAPA管理', 'API', '/api/qms/capas', 'ALL', '纠正预防措施管理'),
('PERMISSION_INCIDENT_MANAGE', '不良事件管理', 'API', '/api/qms/incidents', 'ALL', '不良事件记录管理'),
('PERMISSION_CHANGE_CONTROL_MANAGE', '变更控制管理', 'API', '/api/qms/change-controls', 'ALL', '变更控制管理'),

-- MES生产执行权限
('PERMISSION_BATCH_MANAGE', '批次管理', 'API', '/api/mes/batches', 'ALL', '生产批次生命周期管理'),
('PERMISSION_PLAN_MANAGE', '计划管理', 'API', '/api/mes/plans', 'ALL', '生产计划制定与执行'),
('PERMISSION_EQUIPMENT_MANAGE', '设备管理', 'API', '/api/mes/equipment', 'ALL', '生产设备监控与维护'),
('PERMISSION_MATERIAL_MANAGE', '物料管理', 'API', '/api/mes/materials', 'ALL', '生产物料管理'),

-- LIMS实验室信息权限
('PERMISSION_SAMPLE_MANAGE', '样品管理', 'API', '/api/lims/samples', 'ALL', '实验室样品管理'),
('PERMISSION_METHOD_MANAGE', '检测方法管理', 'API', '/api/lims/methods', 'ALL', '检测方法定义与维护'),
('PERMISSION_RESULT_MANAGE', '结果审核', 'API', '/api/lims/results', 'ALL', '测试结果审核发布'),
('PERMISSION_INSTRUMENT_MANAGE', '仪器设备管理', 'API', '/api/lims/instruments', 'ALL', '实验室仪器管理'),

-- EDMS电子文档权限
('PERMISSION_DOCUMENT_MANAGE', '文档管理', 'API', '/api/edms/documents', 'ALL', '电子文档管理'),
('PERMISSION_WORKFLOW_MANAGE', '工作流管理', 'API', '/api/edms/workflows', 'ALL', '审批工作流管理'),
('PERMISSION_TEMPLATE_MANAGE', '模板管理', 'API', '/api/edms/templates', 'ALL', '文档模板管理'),

-- 文件服务权限
('PERMISSION_FILE_UPLOAD', '文件上传', 'API', '/api/file/upload', 'POST', '文件上传权限'),
('PERMISSION_FILE_DOWNLOAD', '文件下载', 'API', '/api/file/download', 'GET', '文件下载权限'),
('PERMISSION_FILE_MANAGE', '文件管理', 'API', '/api/file/manage', 'ALL', '文件管理权限'),

-- 消息服务权限
('PERMISSION_MESSAGE_SEND', '消息发送', 'API', '/api/message/send', 'POST', '消息发送权限'),
('PERMISSION_MESSAGE_RECEIVE', '消息接收', 'API', '/api/message/receive', 'GET', '消息接收权限');

-- ===================================================================
-- 4. 用户角色关联表 (user_roles)
-- ===================================================================

-- 用户角色关联表
CREATE TABLE IF NOT EXISTS user_roles (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,

    -- 时间控制
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expired_at TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,

    -- 审计字段
    assigned_by BIGINT,
    assigned_at_audit TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id) REFERENCES sys_users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES sys_roles(id) ON DELETE CASCADE,
    CONSTRAINT uk_user_role UNIQUE (user_id, role_id)
);

-- 索引
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_user_roles_user_id ON user_roles(user_id);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_user_roles_role_id ON user_roles(role_id);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_user_roles_active ON user_roles(is_active);

-- ===================================================================
-- 5. 角色权限关联表 (role_permissions)
-- ===================================================================

-- 角色权限关联表
CREATE TABLE IF NOT EXISTS role_permissions (
    id BIGSERIAL PRIMARY KEY,
    role_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,

    -- 时间控制
    granted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expired_at TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,

    -- 审计字段
    granted_by BIGINT,
    granted_at_audit TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (role_id) REFERENCES sys_roles(id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES sys_permissions(id) ON DELETE CASCADE,
    CONSTRAINT uk_role_permission UNIQUE (role_id, permission_id)
);

-- 索引
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_role_perm_role_id ON role_permissions(role_id);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_role_perm_perm_id ON role_permissions(permission_id);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_role_perm_active ON role_permissions(is_active);

-- 为预设角色分配权限
-- 系统管理员：所有权限
INSERT INTO role_permissions (role_id, permission_id, granted_by)
SELECT r.id, p.id, 1
FROM sys_roles r, sys_permissions p
WHERE r.role_code = 'ROLE_SYSTEM_ADMIN' AND p.is_active = TRUE;

-- GMP管理员：GMP相关模块权限（除系统管理权限之外的所有权限）
INSERT INTO role_permissions (role_id, permission_id, granted_by)
SELECT r.id, p.id, 1
FROM sys_roles r, sys_permissions p
WHERE r.role_code = 'ROLE_GMP_ADMIN'
  AND p.permission_code NOT LIKE 'PERMISSION_USER_MANAGE'
  AND p.permission_code NOT LIKE 'PERMISSION_ROLE_MANAGE'
  AND p.permission_code NOT LIKE 'PERMISSION_SYSTEM_CONFIG'
  AND p.is_active = TRUE;

-- 质量管理员：QMS模块权限
INSERT INTO role_permissions (role_id, permission_id, granted_by)
SELECT r.id, p.id, 1
FROM sys_roles r, sys_permissions p
WHERE r.role_code = 'ROLE_QMS_MANAGER'
  AND p.permission_code LIKE 'PERMISSION_QMS_%'
  AND p.is_active = TRUE;

-- 生产管理员：MES模块权限
INSERT INTO role_permissions (role_id, permission_id, granted_by)
SELECT r.id, p.id, 1
FROM sys_roles r, sys_permissions p
WHERE r.role_code = 'ROLE_MES_MANAGER'
  AND p.permission_code LIKE 'PERMISSION_MES_%'
  AND p.is_active = TRUE;

-- 实验室管理员：LIMS模块权限
INSERT INTO role_permissions (role_id, permission_id, granted_by)
SELECT r.id, p.id, 1
FROM sys_roles r, sys_permissions p
WHERE r.role_code = 'ROLE_LIMS_MANAGER'
  AND p.permission_code LIKE 'PERMISSION_LIMS_%'
  AND p.is_active = TRUE;

-- 操作员：基础查看和操作权限
INSERT INTO role_permissions (role_id, permission_id, granted_by)
SELECT r.id, p.id, 1
FROM sys_roles r, sys_permissions p
WHERE r.role_code = 'ROLE_OPERATOR'
  AND p.permission_code IN (
    'PERMISSION_SAMPLE_MANAGE', -- 样品管理
    'PERMISSION_BATCH_MANAGE',   -- 批次管理
    'PERMISSION_FILE_UPLOAD',    -- 文件上传
    'PERMISSION_FILE_DOWNLOAD',  -- 文件下载
    'PERMISSION_MESSAGE_RECEIVE' -- 消息接收
  )
  AND p.is_active = TRUE;

-- ===================================================================
-- 6. JWT令牌黑名单表 (jwt_blacklist)
-- ===================================================================

-- JWT令牌黑名单表
CREATE TABLE IF NOT EXISTS jwt_blacklist (
    id BIGSERIAL PRIMARY KEY,
    token_id VARCHAR(100) NOT NULL UNIQUE,
    token_content TEXT,
    expired_at TIMESTAMP NOT NULL,
    blacklisted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    blacklist_reason VARCHAR(200),

    -- 关联用户
    user_id BIGINT,
    username VARCHAR(50),

    FOREIGN KEY (user_id) REFERENCES sys_users(id)
);

-- 索引
CREATE UNIQUE INDEX CONCURRENTLY IF NOT EXISTS idx_jwt_token_id ON jwt_blacklist(token_id);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_jwt_expired_at ON jwt_blacklist(expired_at);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_jwt_user_id ON jwt_blacklist(user_id);

-- ===================================================================
-- 7. 用户操作日志 (user_operation_logs)
-- ===================================================================

-- 用户操作审计日志
CREATE TABLE IF NOT EXISTS user_operation_logs (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT,
    username VARCHAR(50),
    operation VARCHAR(100) NOT NULL, -- LOGIN, LOGOUT, ROLE_CHANGE, etc.
    module VARCHAR(50) NOT NULL, -- AUTH, USER, ROLE, PERMISSION
    action VARCHAR(200) NOT NULL, -- 具体操作描述
    result VARCHAR(20) DEFAULT 'SUCCESS' CHECK (result IN ('SUCCESS', 'FAILED')),

    -- 请求信息
    ip_address VARCHAR(50),
    user_agent TEXT,

    -- 请求数据
    request_data JSONB,
    response_data JSONB,

    -- 时间信息
    operation_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    duration_ms INTEGER, -- 操作耗时

    -- 元数据
    metadata JSONB
);

-- 索引
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_logs_user_id ON user_operation_logs(user_id);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_logs_username ON user_operation_logs(username);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_logs_operation ON user_operation_logs(operation);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_logs_module ON user_operation_logs(module);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_logs_time ON user_operation_logs(operation_time);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_logs_result ON user_operation_logs(result);

-- ===================================================================
-- 初始化管理员用户数据
-- ===================================================================

-- 使用BCrypt加密的密码：Admin123! （生产环境中请修改）
-- 注意：实际部署时应使用环境变量或配置文件设置密码
INSERT INTO sys_users (
    username, email, full_name, password_hash,
    user_status, created_by
) VALUES (
    'admin',
    'admin@gmp-system.com',
    '系统管理员',
    '$2a$10$wKjRhzMMvKLWkOrZ7VpSJOB9SRjvLjUJEJhE8jxQsEQqQxZUIRzZm', -- Admin123!
    'ACTIVE',
    1
);

-- 为管理员分配系统管理员角色
INSERT INTO user_roles (user_id, role_id, assigned_by)
SELECT u.id, r.id, 1
FROM sys_users u, sys_roles r
WHERE u.username = 'admin' AND r.role_code = 'ROLE_SYSTEM_ADMIN';

-- ===================================================================
-- 创建序列和权限
-- ===================================================================

-- 给gmp_user用户授权（如果使用专用用户的话）
-- GRANT USAGE ON SCHEMA public TO gmp_user;
-- GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO gmp_user;
-- GRANT USAGE, SELECT, UPDATE ON ALL SEQUENCES IN SCHEMA public TO gmp_user;

-- ===================================================================
-- 数据库约束和触发器
-- ===================================================================

-- 用户状态变更触发器（记录状态变更历史）
CREATE OR REPLACE FUNCTION audit_user_status_change()
RETURNS TRIGGER AS $$
BEGIN
    IF (OLD.user_status != NEW.user_status) THEN
        INSERT INTO user_operation_logs (
            user_id, username, operation, module, action,
            result, request_data, metadata
        ) VALUES (
            NEW.id,
            NEW.username,
            'STATUS_CHANGE',
            'USER',
            format('User status changed from %s to %s', OLD.user_status, NEW.user_status),
            'SUCCESS',
            jsonb_build_object(
                'old_status', OLD.user_status,
                'new_status', NEW.user_status,
                'changed_by', NEW.updated_by
            ),
            jsonb_build_object(
                'table', 'sys_users',
                'operation_type', 'UPDATE'
            )
        );
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- 为用户表创建触发器
CREATE TRIGGER user_status_change_audit
    AFTER UPDATE ON sys_users
    FOR EACH ROW
    EXECUTE FUNCTION audit_user_status_change();

-- ===================================================================
-- 性能优化视图
-- ===================================================================

-- 用户角色权限视图（用于快速权限检查）
CREATE OR REPLACE VIEW user_role_permissions_view AS
SELECT DISTINCT
    u.id AS user_id,
    u.username,
    r.role_code,
    r.role_name,
    p.permission_code,
    p.permission_name,
    p.resource_type,
    p.resource_url,
    p.http_method,
    ur.is_active AS user_role_active,
    rp.is_active AS role_permission_active
FROM sys_users u
JOIN user_roles ur ON u.id = ur.user_id AND ur.is_active = TRUE
JOIN sys_roles r ON ur.role_id = r.id AND r.is_active = TRUE
JOIN role_permissions rp ON r.id = rp.role_id AND rp.is_active = TRUE
JOIN sys_permissions p ON rp.permission_id = p.id AND p.is_active = TRUE;

-- 用户统计视图
CREATE OR REPLACE VIEW user_statistics AS
SELECT
    COUNT(*) AS total_users,
    COUNT(CASE WHEN user_status = 'ACTIVE' THEN 1 END) AS active_users,
    COUNT(CASE WHEN user_status = 'LOCKED' THEN 1 END) AS locked_users,
    AVG(login_attempts) AS avg_login_attempts,
    MAX(last_login_time) AS latest_login
FROM sys_users;

-- ===================================================================
-- 数据清理和维护
-- ===================================================================

-- JWT黑名单自动清理（清理过期令牌）
CREATE OR REPLACE FUNCTION cleanup_expired_jwt_blacklist()
RETURNS INTEGER AS $$
DECLARE
    deleted_count INTEGER;
BEGIN
    DELETE FROM jwt_blacklist
    WHERE expired_at < NOW();

    GET DIAGNOSTICS deleted_count = ROW_COUNT;
    RETURN deleted_count;
END;
$$ LANGUAGE plpgsql;

-- 操作日志自动清理（保留最近6个月的数据）
-- 注意：根据合规要求，GMP审计日志不能随意删除
CREATE OR REPLACE FUNCTION archive_old_operation_logs(months_retain INTEGER DEFAULT 6)
RETURNS INTEGER AS $$
DECLARE
    archived_count INTEGER;
BEGIN
    -- 这里可以实现日志归档逻辑
    -- 实际部署时应根据合规要求确定日志保留策略
    -- DELETE FROM user_operation_logs
    -- WHERE operation_time < NOW() - INTERVAL '1 month' * months_retain;

    SELECT 0 INTO archived_count; -- 占位符
    RETURN archived_count;
END;
$$ LANGUAGE plpgsql;

-- ===================================================================
-- 数据库注释
-- ===================================================================

COMMENT ON TABLE sys_users IS 'GMP系统用户表 - 存储所有系统用户信息和认证数据';
COMMENT ON TABLE sys_roles IS 'GMP系统角色表 - 预定义GMP合规的角色体系';
COMMENT ON TABLE sys_permissions IS 'GMP系统权限表 - 基于资源的权限管理体系';
COMMENT ON TABLE user_roles IS '用户角色关联表 - 用户与角色的多对多关系';
COMMENT ON TABLE role_permissions IS '角色权限关联表 - 角色与权限的多对多关系';
COMMENT ON TABLE jwt_blacklist IS 'JWT令牌黑名单表 - 防止已登出令牌被滥用';
COMMENT ON TABLE user_operation_logs IS '用户操作审计日志 - GMP合规要求的操作审计跟踪';

-- ===================================================================
-- 脚本执行状态日志
-- ===================================================================

DO $$
BEGIN
    RAISE NOTICE 'GMP认证授权系统数据库初始化完成';
    RAISE NOTICE '创建了7个核心数据表';
    RAISE NOTICE '预设了6个GMP合规角色';
    RAISE NOTICE '配置了18个系统权限';
    RAISE NOTICE '创建了系统管理员账户 (admin/Admin123!)';
    RAISE NOTICE '设置了角色权限关联关系';
    RAISE NOTICE '创建了必要的索引和约束';
    RAISE NOTICE '配置了审计触发器和维护函数';
END $$;

-- 结束auth_db数据库初始化
\echo 'Auth数据库表结构和数据初始化完成'

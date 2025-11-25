-- 创建子系统表
CREATE TABLE IF NOT EXISTS subsystems (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    subsystem_code VARCHAR(50) NOT NULL UNIQUE,
    subsystem_name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    access_url VARCHAR(255),
    icon_path VARCHAR(255),
    sort_order INTEGER,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    is_gmp_critical BOOLEAN NOT NULL DEFAULT FALSE,
    created_by VARCHAR(50),
    created_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(50),
    updated_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 创建子系统权限关联表
CREATE TABLE IF NOT EXISTS subsystem_permissions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    subsystem_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    access_level INTEGER NOT NULL,
    is_default BOOLEAN NOT NULL DEFAULT FALSE,
    created_by VARCHAR(50),
    created_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(50),
    updated_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (subsystem_id) REFERENCES subsystems(id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES permissions(id) ON DELETE CASCADE,
    UNIQUE KEY uk_subsystem_permission (subsystem_id, permission_id)
);

-- 创建索引以优化查询性能
CREATE INDEX idx_subsystem_code ON subsystems(subsystem_code);
CREATE INDEX idx_subsystem_enabled ON subsystems(enabled);
CREATE INDEX idx_subsystem_permissions_subsystem ON subsystem_permissions(subsystem_id);
CREATE INDEX idx_subsystem_permissions_permission ON subsystem_permissions(permission_id);

-- 插入初始子系统数据
INSERT INTO subsystems (subsystem_code, subsystem_name, description, access_url, icon_path, sort_order, enabled, is_gmp_critical)
VALUES 
('AUTH', '认证授权系统', 'GMP平台的认证与授权中心', '/auth', 'icon-auth', 1, TRUE, TRUE),
('QMS', '质量管理系统', 'GMP质量管理子系统', '/qms', 'icon-qms', 2, TRUE, TRUE),
('MES', '制造执行系统', '生产制造管理子系统', '/mes', 'icon-mes', 3, TRUE, TRUE),
('LIMS', '实验室信息系统', '实验室数据管理子系统', '/lims', 'icon-lims', 4, TRUE, TRUE),
('DOC', '文档管理系统', 'GMP文档控制子系统', '/doc', 'icon-doc', 5, TRUE, TRUE),
('ERP', '企业资源计划', '企业资源管理子系统', '/erp', 'icon-erp', 6, TRUE, FALSE),
('EHS', '环境健康安全', '环境与职业健康安全子系统', '/ehs', 'icon-ehs', 7, TRUE, FALSE)
ON DUPLICATE KEY UPDATE 
subsystem_name = VALUES(subsystem_name),
description = VALUES(description),
access_url = VALUES(access_url),
icon_path = VALUES(icon_path),
sort_order = VALUES(sort_order),
enabled = VALUES(enabled),
is_gmp_critical = VALUES(is_gmp_critical);

-- 为角色分配默认的子系统权限
-- 注意：这里需要先确保permissions表中已存在相关权限
-- 建议在应用启动时通过代码为现有角色分配子系统权限
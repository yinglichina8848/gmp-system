-- GMP系统用户权限管理模板数据初始化脚本
-- 版本: v1.0.0
-- 创建日期: 2025-11-20
-- 作者: GMP系统开发团队

-- 开始事务
BEGIN TRANSACTION;

-- 注意: 执行此脚本前请确保已经创建了所有必要的表结构

-- ==========================================================================
-- 1. 组织结构数据初始化
-- ==========================================================================

-- 插入组织结构
INSERT INTO organization (name, code, description, parent_id, level, created_by, updated_by) VALUES
('GMP制药集团', 'GMP_GROUP', 'GMP制药集团总部', NULL, 1, 'system', 'system'),
('华北分公司', 'NORTH_BRANCH', '华北地区分公司', 1, 2, 'system', 'system'),
('华东分公司', 'EAST_BRANCH', '华东地区分公司', 1, 2, 'system', 'system'),
('华南分公司', 'SOUTH_BRANCH', '华南地区分公司', 1, 2, 'system', 'system');

-- ==========================================================================
-- 2. 部门数据初始化
-- ==========================================================================

-- 总部部门
INSERT INTO department (name, code, description, org_id, parent_dept_id, level, created_by, updated_by) VALUES
('质量管理部', 'QA_DEPT', '负责质量体系管理和产品质量控制', 1, NULL, 1, 'system', 'system'),
('生产管理部', 'PROD_DEPT', '负责生产计划和生产过程管理', 1, NULL, 1, 'system', 'system'),
('设备管理部', 'EQUIP_DEPT', '负责设备维护和设备验证', 1, NULL, 1, 'system', 'system'),
('研发部', 'R&D_DEPT', '负责产品研发和工艺开发', 1, NULL, 1, 'system', 'system'),
('物料管理部', 'MATERIAL_DEPT', '负责物料采购和库存管理', 1, NULL, 1, 'system', 'system'),
('人力资源部', 'HR_DEPT', '负责人力资源管理和培训', 1, NULL, 1, 'system', 'system'),
('IT运维部', 'IT_DEPT', '负责信息系统维护和支持', 1, NULL, 1, 'system', 'system'),
('安全环保部', 'EHS_DEPT', '负责安全管理和环境保护', 1, NULL, 1, 'system', 'system'),
('法规合规部', 'COMPLIANCE_DEPT', '负责法规事务和合规管理', 1, NULL, 1, 'system', 'system');

-- 质量管理部子部门
INSERT INTO department (name, code, description, org_id, parent_dept_id, level, created_by, updated_by) VALUES
('质量控制实验室', 'QC_LAB', '负责产品和物料的检验', 1, 1, 2, 'system', 'system'),
('质量保证组', 'QA_GROUP', '负责质量保证和体系维护', 1, 1, 2, 'system', 'system'),
('验证管理组', 'VALIDATION_GROUP', '负责验证工作管理', 1, 1, 2, 'system', 'system');

-- 生产管理部子部门
INSERT INTO department (name, code, description, org_id, parent_dept_id, level, created_by, updated_by) VALUES
('固体制剂车间', 'SOLID_WORKSHOP', '负责固体制剂生产', 1, 2, 2, 'system', 'system'),
('液体制剂车间', 'LIQUID_WORKSHOP', '负责液体制剂生产', 1, 2, 2, 'system', 'system'),
('无菌制剂车间', 'STERILE_WORKSHOP', '负责无菌制剂生产', 1, 2, 2, 'system', 'system');

-- 分公司部门示例
INSERT INTO department (name, code, description, org_id, parent_dept_id, level, created_by, updated_by) VALUES
('华北质量管理部', 'NC_QA_DEPT', '华北分公司质量管理部门', 2, NULL, 1, 'system', 'system'),
('华北生产管理部', 'NC_PROD_DEPT', '华北分公司生产管理部门', 2, NULL, 1, 'system', 'system');

-- ==========================================================================
-- 3. 角色数据初始化
-- ==========================================================================

-- 系统级角色
INSERT INTO role (name, code, description, type, is_temp, created_by, updated_by) VALUES
('系统管理员', 'SYS_ADMIN', '系统最高权限管理员', 'SYSTEM', false, 'system', 'system'),
('安全审计员', 'SECURITY_AUDITOR', '负责系统安全审计', 'SYSTEM', false, 'system', 'system'),
('数据管理员', 'DATA_ADMIN', '负责数据管理和维护', 'SYSTEM', false, 'system', 'system'),
('普通用户', 'NORMAL_USER', '系统基础用户', 'NORMAL', false, 'system', 'system'),
('访客用户', 'GUEST_USER', '临时访问用户', 'TEMP', true, 'system', 'system'),
('系统监控员', 'SYS_MONITOR', '负责系统监控和性能分析', 'SYSTEM', false, 'system', 'system');

-- QMS子系统角色
INSERT INTO role (name, code, description, type, is_temp, created_by, updated_by) VALUES
('QMS管理员', 'QMS_ADMIN', '质量管理系统管理员', 'NORMAL', false, 'system', 'system'),
('QA经理', 'QA_MANAGER', '质量保证经理', 'NORMAL', false, 'system', 'system'),
('QA专员', 'QA_SPECIALIST', '质量保证专员', 'NORMAL', false, 'system', 'system'),
('QC经理', 'QC_MANAGER', '质量控制经理', 'NORMAL', false, 'system', 'system'),
('QC检验员', 'QC_INSPECTOR', '质量控制检验员', 'NORMAL', false, 'system', 'system'),
('偏差管理员', 'DEVIA_MANAGER', '偏差管理专员', 'NORMAL', false, 'system', 'system'),
('CAPA管理员', 'CAPA_MANAGER', 'CAPA管理专员', 'NORMAL', false, 'system', 'system');

-- MES子系统角色
INSERT INTO role (name, code, description, type, is_temp, created_by, updated_by) VALUES
('MES管理员', 'MES_ADMIN', '制造执行系统管理员', 'NORMAL', false, 'system', 'system'),
('生产经理', 'PROD_MANAGER', '生产部门经理', 'NORMAL', false, 'system', 'system'),
('生产主管', 'PROD_SUPERVISOR', '生产车间主管', 'NORMAL', false, 'system', 'system'),
('操作员', 'OPERATOR', '生产一线操作员', 'NORMAL', false, 'system', 'system'),
('批记录审核员', 'BATCH_REVIEWER', '批记录审核人员', 'NORMAL', false, 'system', 'system');

-- EAMS子系统角色
INSERT INTO role (name, code, description, type, is_temp, created_by, updated_by) VALUES
('EAMS管理员', 'EAMS_ADMIN', '设备资产管理系统管理员', 'NORMAL', false, 'system', 'system'),
('设备经理', 'EQUIP_MANAGER', '设备管理部门经理', 'NORMAL', false, 'system', 'system'),
('设备工程师', 'EQUIP_ENGINEER', '设备工程师', 'NORMAL', false, 'system', 'system'),
('维护技术员', 'MAINTENANCE_TECH', '设备维护技术员', 'NORMAL', false, 'system', 'system'),
('校准管理员', 'CALIBRATION_MANAGER', '仪器校准管理人员', 'NORMAL', false, 'system', 'system');

-- 研发子系统角色
INSERT INTO role (name, code, description, type, is_temp, created_by, updated_by) VALUES
('研发管理员', 'R&D_ADMIN', '研发管理系统管理员', 'NORMAL', false, 'system', 'system'),
('研发经理', 'R&D_MANAGER', '研发部门经理', 'NORMAL', false, 'system', 'system'),
('研发工程师', 'R&D_ENGINEER', '研发工程师', 'NORMAL', false, 'system', 'system'),
('研发技术员', 'R&D_TECH', '研发技术员', 'NORMAL', false, 'system', 'system');

-- 物料管理子系统角色
INSERT INTO role (name, code, description, type, is_temp, created_by, updated_by) VALUES
('物料管理员', 'MATERIAL_ADMIN', '物料管理系统管理员', 'NORMAL', false, 'system', 'system'),
('仓库经理', 'WAREHOUSE_MANAGER', '仓库管理经理', 'NORMAL', false, 'system', 'system'),
('仓库管理员', 'WAREHOUSE_CLERK', '仓库管理员', 'NORMAL', false, 'system', 'system'),
('采购专员', 'PURCHASING_OFFICER', '采购专员', 'NORMAL', false, 'system', 'system');

-- 合规子系统角色
INSERT INTO role (name, code, description, type, is_temp, created_by, updated_by) VALUES
('合规管理员', 'COMPLIANCE_ADMIN', '合规管理系统管理员', 'NORMAL', false, 'system', 'system'),
('法规专员', 'REGULATORY_OFFICER', '法规事务专员', 'NORMAL', false, 'system', 'system'),
('GMP培训管理员', 'GMP_TRAINING_ADMIN', 'GMP培训管理人员', 'NORMAL', false, 'system', 'system');

-- ==========================================================================
-- 4. 角色层级关系初始化
-- ==========================================================================

INSERT INTO role_hierarchy (parent_role_id, child_role_id) VALUES
-- 系统级角色层级
(1, 2), -- 系统管理员 > 安全审计员
(1, 3), -- 系统管理员 > 数据管理员
(1, 6), -- 系统管理员 > 系统监控员
(2, 4), -- 安全审计员 > 普通用户
(3, 4), -- 数据管理员 > 普通用户
(6, 4), -- 系统监控员 > 普通用户

-- QMS角色层级
(7, 8),  -- QMS管理员 > QA经理
(7, 9),  -- QMS管理员 > QA专员
(7, 10), -- QMS管理员 > QC经理
(7, 11), -- QMS管理员 > QC检验员
(8, 9),  -- QA经理 > QA专员
(10, 11),-- QC经理 > QC检验员

-- MES角色层级
(15, 16), -- MES管理员 > 生产经理
(15, 17), -- MES管理员 > 生产主管
(15, 18), -- MES管理员 > 操作员
(16, 17), -- 生产经理 > 生产主管
(17, 18); -- 生产主管 > 操作员

-- ==========================================================================
-- 5. 权限数据初始化
-- ==========================================================================

-- 系统管理权限组
INSERT INTO permission (code, name, description, resource_type, action, group_name, created_by, updated_by) VALUES
-- 用户管理权限
('USER_CREATE', '创建用户', '允许创建新用户', 'USER', 'CREATE', '系统管理', 'system', 'system'),
('USER_READ', '查看用户', '允许查看用户信息', 'USER', 'READ', '系统管理', 'system', 'system'),
('USER_UPDATE', '修改用户', '允许修改用户信息', 'USER', 'UPDATE', '系统管理', 'system', 'system'),
('USER_DELETE', '删除用户', '允许删除用户', 'USER', 'DELETE', '系统管理', 'system', 'system'),
('USER_DISABLE', '禁用用户', '允许禁用用户账号', 'USER', 'DISABLE', '系统管理', 'system', 'system'),

-- 角色管理权限
('ROLE_CREATE', '创建角色', '允许创建新角色', 'ROLE', 'CREATE', '系统管理', 'system', 'system'),
('ROLE_READ', '查看角色', '允许查看角色信息', 'ROLE', 'READ', '系统管理', 'system', 'system'),
('ROLE_UPDATE', '修改角色', '允许修改角色信息', 'ROLE', 'UPDATE', '系统管理', 'system', 'system'),
('ROLE_DELETE', '删除角色', '允许删除角色', 'ROLE', 'DELETE', '系统管理', 'system', 'system'),

-- 权限管理权限
('PERM_CREATE', '创建权限', '允许创建新权限', 'PERMISSION', 'CREATE', '系统管理', 'system', 'system'),
('PERM_READ', '查看权限', '允许查看权限信息', 'PERMISSION', 'READ', '系统管理', 'system', 'system'),
('PERM_UPDATE', '修改权限', '允许修改权限信息', 'PERMISSION', 'UPDATE', '系统管理', 'system', 'system'),
('PERM_DELETE', '删除权限', '允许删除权限', 'PERMISSION', 'DELETE', '系统管理', 'system', 'system'),

-- 组织管理权限
('ORG_CREATE', '创建组织', '允许创建新组织', 'ORGANIZATION', 'CREATE', '系统管理', 'system', 'system'),
('ORG_READ', '查看组织', '允许查看组织信息', 'ORGANIZATION', 'READ', '系统管理', 'system', 'system'),
('ORG_UPDATE', '修改组织', '允许修改组织信息', 'ORGANIZATION', 'UPDATE', '系统管理', 'system', 'system'),
('ORG_DELETE', '删除组织', '允许删除组织', 'ORGANIZATION', 'DELETE', '系统管理', 'system', 'system'),

-- 部门管理权限
('DEPT_CREATE', '创建部门', '允许创建新部门', 'DEPARTMENT', 'CREATE', '系统管理', 'system', 'system'),
('DEPT_READ', '查看部门', '允许查看部门信息', 'DEPARTMENT', 'READ', '系统管理', 'system', 'system'),
('DEPT_UPDATE', '修改部门', '允许修改部门信息', 'DEPARTMENT', 'UPDATE', '系统管理', 'system', 'system'),
('DEPT_DELETE', '删除部门', '允许删除部门', 'DEPARTMENT', 'DELETE', '系统管理', 'system', 'system'),

-- 审计日志权限
('AUDIT_LOG_READ', '查看审计日志', '允许查看系统审计日志', 'AUDIT_LOG', 'READ', '系统管理', 'system', 'system'),
('AUDIT_LOG_EXPORT', '导出审计日志', '允许导出系统审计日志', 'AUDIT_LOG', 'EXPORT', '系统管理', 'system', 'system'),

-- 系统配置权限
('SYS_CONFIG_READ', '查看系统配置', '允许查看系统配置', 'SYS_CONFIG', 'READ', '系统管理', 'system', 'system'),
('SYS_CONFIG_UPDATE', '修改系统配置', '允许修改系统配置', 'SYS_CONFIG', 'UPDATE', '系统管理', 'system', 'system'),

-- GMP特殊权限
('ELECTRONIC_SIGN', '电子签名', '允许使用电子签名功能', 'GMP_FUNCTION', 'SIGN', 'GMP权限', 'system', 'system'),
('GMP_DATA_ACCESS', 'GMP数据访问', '允许访问GMP相关数据', 'GMP_DATA', 'ACCESS', 'GMP权限', 'system', 'system'),
('VALIDATION_EXECUTE', '执行验证', '允许执行验证活动', 'VALIDATION', 'EXECUTE', 'GMP权限', 'system', 'system'),
('BATCH_REVIEW', '批记录审核', '允许审核生产批记录', 'BATCH_RECORD', 'REVIEW', 'GMP权限', 'system', 'system'),

-- QMS模块权限
('DOC_CONTROL', '文档控制', '允许控制质量管理文档', 'QMS_DOCUMENT', 'CONTROL', '质量管理', 'system', 'system'),
('DEVIA_REPORT', '偏差报告', '允许报告质量偏差', 'QMS_DEVIATION', 'REPORT', '质量管理', 'system', 'system'),
('DEVIA_INVESTIGATE', '偏差调查', '允许调查质量偏差', 'QMS_DEVIATION', 'INVESTIGATE', '质量管理', 'system', 'system'),
('DEVIA_CLOSE', '偏差关闭', '允许关闭质量偏差', 'QMS_DEVIATION', 'CLOSE', '质量管理', 'system', 'system'),
('CAPA_CREATE', '创建CAPA', '允许创建CAPA', 'QMS_CAPA', 'CREATE', '质量管理', 'system', 'system'),
('CAPA_IMPLEMENT', '实施CAPA', '允许实施CAPA', 'QMS_CAPA', 'IMPLEMENT', '质量管理', 'system', 'system'),
('CAPA_VERIFY', '验证CAPA', '允许验证CAPA有效性', 'QMS_CAPA', 'VERIFY', '质量管理', 'system', 'system'),
('CHANGE_CONTROL', '变更控制', '允许管理变更控制', 'QMS_CHANGE', 'CONTROL', '质量管理', 'system', 'system'),
('QUALITY_EVENT_REPORT', '质量事件报告', '允许报告质量事件', 'QMS_EVENT', 'REPORT', '质量管理', 'system', 'system'),

-- MES模块权限
('PROD_ORDER_CREATE', '创建生产订单', '允许创建生产订单', 'MES_ORDER', 'CREATE', '生产管理', 'system', 'system'),
('PROD_ORDER_EXECUTE', '执行生产订单', '允许执行生产订单', 'MES_ORDER', 'EXECUTE', '生产管理', 'system', 'system'),
('BATCH_RECORD_CREATE', '创建批记录', '允许创建批记录', 'MES_BATCH', 'CREATE', '生产管理', 'system', 'system'),
('BATCH_RECORD_UPDATE', '更新批记录', '允许更新批记录', 'MES_BATCH', 'UPDATE', '生产管理', 'system', 'system'),
('EQUIP_USE_LOG', '设备使用记录', '允许记录设备使用情况', 'MES_EQUIP_USE', 'LOG', '生产管理', 'system', 'system'),
('MATERIAL_ISSUE', '物料发放', '允许发放生产物料', 'MES_MATERIAL', 'ISSUE', '生产管理', 'system', 'system'),
('QUALITY_CHECK_EXECUTE', '执行质量检查', '允许执行生产过程质量检查', 'MES_QUALITY_CHECK', 'EXECUTE', '生产管理', 'system', 'system'),

-- EAMS模块权限
('EQUIP_REGISTER', '设备注册', '允许注册新设备', 'EAMS_EQUIP', 'REGISTER', '设备管理', 'system', 'system'),
('MAINTENANCE_PLAN', '维护计划', '允许制定设备维护计划', 'EAMS_MAINTENANCE', 'PLAN', '设备管理', 'system', 'system'),
('MAINTENANCE_EXECUTE', '执行维护', '允许执行设备维护', 'EAMS_MAINTENANCE', 'EXECUTE', '设备管理', 'system', 'system'),
('CALIBRATION_SCHEDULE', '校准计划', '允许制定校准计划', 'EAMS_CALIBRATION', 'SCHEDULE', '设备管理', 'system', 'system'),
('CALIBRATION_EXECUTE', '执行校准', '允许执行设备校准', 'EAMS_CALIBRATION', 'EXECUTE', '设备管理', 'system', 'system'),
('EQUIP_VALIDATE', '设备验证', '允许执行设备验证', 'EAMS_VALIDATION', 'EXECUTE', '设备管理', 'system', 'system'),

-- 研发模块权限
('R&D_PROJECT_CREATE', '创建研发项目', '允许创建研发项目', 'R&D_PROJECT', 'CREATE', '研发管理', 'system', 'system'),
('R&D_PROJECT_UPDATE', '更新研发项目', '允许更新研发项目', 'R&D_PROJECT', 'UPDATE', '研发管理', 'system', 'system'),
('EXPERIMENT_RECORD', '实验记录', '允许记录实验数据', 'R&D_EXPERIMENT', 'RECORD', '研发管理', 'system', 'system'),
('FORMULATION_DESIGN', '处方设计', '允许进行处方设计', 'R&D_FORMULATION', 'DESIGN', '研发管理', 'system', 'system'),

-- 物料管理模块权限
('MATERIAL_RECEIVE', '物料接收', '允许接收物料', 'MATERIAL_RECEIPT', 'RECEIVE', '物料管理', 'system', 'system'),
('MATERIAL_INSPECT', '物料检验', '允许检验物料', 'MATERIAL_INSPECTION', 'INSPECT', '物料管理', 'system', 'system'),
('MATERIAL_STORAGE', '物料入库', '允许物料入库', 'MATERIAL_STORAGE', 'STORE', '物料管理', 'system', 'system'),
('MATERIAL_ISSUE', '物料发放', '允许发放物料', 'MATERIAL_ISSUE', 'ISSUE', '物料管理', 'system', 'system'),
('INVENTORY_CHECK', '库存盘点', '允许进行库存盘点', 'MATERIAL_INVENTORY', 'CHECK', '物料管理', 'system', 'system'),

-- 合规模块权限
('TRAINING_MANAGE', '培训管理', '允许管理培训活动', 'COMPLIANCE_TRAINING', 'MANAGE', '合规管理', 'system', 'system'),
('REGULATORY_SUBMIT', '法规提交', '允许提交法规文件', 'COMPLIANCE_REGULATORY', 'SUBMIT', '合规管理', 'system', 'system'),
('GMP_AUDIT_PLAN', 'GMP审计计划', '允许制定GMP审计计划', 'COMPLIANCE_GMP_AUDIT', 'PLAN', '合规管理', 'system', 'system');

-- ==========================================================================
-- 6. 角色权限关联数据初始化
-- ==========================================================================

-- 系统管理员权限关联 (全部权限)
INSERT INTO role_permission (role_id, perm_id) 
SELECT 1, perm_id FROM permission;

-- 安全审计员权限关联
INSERT INTO role_permission (role_id, perm_id) 
SELECT 2, perm_id FROM permission 
WHERE code IN ('USER_READ', 'ROLE_READ', 'PERM_READ', 'ORG_READ', 'DEPT_READ', 'AUDIT_LOG_READ', 'AUDIT_LOG_EXPORT');

-- 数据管理员权限关联
INSERT INTO role_permission (role_id, perm_id) 
SELECT 3, perm_id FROM permission 
WHERE code IN ('USER_READ', 'USER_UPDATE', 'ROLE_READ', 'PERM_READ', 'ORG_READ', 'ORG_UPDATE', 'DEPT_READ', 'DEPT_UPDATE');

-- 系统监控员权限关联
INSERT INTO role_permission (role_id, perm_id) 
SELECT 6, perm_id FROM permission 
WHERE code IN ('AUDIT_LOG_READ', 'SYS_CONFIG_READ');

-- QMS管理员权限关联
INSERT INTO role_permission (role_id, perm_id) 
SELECT 7, perm_id FROM permission 
WHERE group_name IN ('质量管理', 'GMP权限') OR 
      code IN ('USER_READ', 'ROLE_READ', 'PERM_READ', 'ELECTRONIC_SIGN', 'GMP_DATA_ACCESS');

-- QA经理权限关联
INSERT INTO role_permission (role_id, perm_id) 
SELECT 8, perm_id FROM permission 
WHERE code IN ('DOC_CONTROL', 'DEVIA_INVESTIGATE', 'DEVIA_CLOSE', 'CAPA_CREATE', 'CAPA_VERIFY', 
               'CHANGE_CONTROL', 'QUALITY_EVENT_REPORT', 'ELECTRONIC_SIGN', 'GMP_DATA_ACCESS', 'VALIDATION_EXECUTE');

-- QC经理权限关联
INSERT INTO role_permission (role_id, perm_id) 
SELECT 10, perm_id FROM permission 
WHERE code IN ('DOC_CONTROL', 'ELECTRONIC_SIGN', 'GMP_DATA_ACCESS', 'VALIDATION_EXECUTE');

-- MES管理员权限关联
INSERT INTO role_permission (role_id, perm_id) 
SELECT 15, perm_id FROM permission 
WHERE group_name IN ('生产管理', 'GMP权限') OR 
      code IN ('USER_READ', 'ROLE_READ', 'PERM_READ', 'ELECTRONIC_SIGN', 'GMP_DATA_ACCESS');

-- 生产经理权限关联
INSERT INTO role_permission (role_id, perm_id) 
SELECT 16, perm_id FROM permission 
WHERE code IN ('PROD_ORDER_CREATE', 'PROD_ORDER_EXECUTE', 'BATCH_REVIEW', 
               'ELECTRONIC_SIGN', 'GMP_DATA_ACCESS');

-- 生产主管权限关联
INSERT INTO role_permission (role_id, perm_id) 
SELECT 17, perm_id FROM permission 
WHERE code IN ('PROD_ORDER_EXECUTE', 'BATCH_RECORD_UPDATE', 'EQUIP_USE_LOG', 'MATERIAL_ISSUE',
               'QUALITY_CHECK_EXECUTE', 'ELECTRONIC_SIGN', 'GMP_DATA_ACCESS');

-- 操作员权限关联
INSERT INTO role_permission (role_id, perm_id) 
SELECT 18, perm_id FROM permission 
WHERE code IN ('BATCH_RECORD_UPDATE', 'EQUIP_USE_LOG', 'QUALITY_CHECK_EXECUTE', 'ELECTRONIC_SIGN');

-- ==========================================================================
-- 7. 示例用户数据初始化
-- ==========================================================================

-- 注意: 密码均为 'Password123!' 的加密值 (示例使用，实际部署应使用真实密码加密)
INSERT INTO "user" (username, password_hash, full_name, email, phone_number, dept_id, status, hire_date, created_by, updated_by) VALUES
-- 系统级用户
('admin', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW', '系统管理员', 'admin@example.com', '13800138000', 7, 'ACTIVE', '2023-01-01', 'system', 'system'),
('security_auditor', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW', '安全审计员', 'security@example.com', '13800138001', 7, 'ACTIVE', '2023-01-02', 'system', 'system'),
('data_admin', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW', '数据管理员', 'data_admin@example.com', '13800138002', 7, 'ACTIVE', '2023-01-03', 'system', 'system'),
('sys_monitor', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW', '系统监控员', 'monitor@example.com', '13800138003', 7, 'ACTIVE', '2023-01-04', 'system', 'system'),

-- QMS相关用户
('qa_manager', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW', 'QA经理张三', 'qa_manager@example.com', '13800138010', 1, 'ACTIVE', '2023-02-01', 'system', 'system'),
('qa_specialist', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW', 'QA专员李四', 'qa_specialist@example.com', '13800138011', 1, 'ACTIVE', '2023-02-02', 'system', 'system'),
('qc_manager', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW', 'QC经理王五', 'qc_manager@example.com', '13800138012', 10, 'ACTIVE', '2023-02-03', 'system', 'system'),
('qc_inspector', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW', 'QC检验员赵六', 'qc_inspector@example.com', '13800138013', 10, 'ACTIVE', '2023-02-04', 'system', 'system'),

-- MES相关用户
('prod_manager', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW', '生产经理孙七', 'prod_manager@example.com', '13800138020', 2, 'ACTIVE', '2023-03-01', 'system', 'system'),
('prod_supervisor', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW', '生产主管周八', 'prod_supervisor@example.com', '13800138021', 13, 'ACTIVE', '2023-03-02', 'system', 'system'),
('operator_01', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW', '操作员吴九', 'operator_01@example.com', '13800138022', 13, 'ACTIVE', '2023-03-03', 'system', 'system'),
('operator_02', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW', '操作员郑十', 'operator_02@example.com', '13800138023', 14, 'ACTIVE', '2023-03-04', 'system', 'system'),

-- 其他部门示例用户
('equipment_manager', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW', '设备经理钱十一', 'equipment_manager@example.com', '13800138030', 3, 'ACTIVE', '2023-04-01', 'system', 'system'),
('warehouse_manager', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW', '仓库经理孙十二', 'warehouse_manager@example.com', '13800138040', 5, 'ACTIVE', '2023-05-01', 'system', 'system'),
('hr_manager', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW', 'HR经理李十三', 'hr_manager@example.com', '13800138050', 6, 'ACTIVE', '2023-06-01', 'system', 'system');

-- ==========================================================================
-- 8. 用户角色关联数据初始化
-- ==========================================================================

-- 系统用户角色关联
INSERT INTO user_role (user_id, role_id, active, assigned_by) VALUES
(1, 1, true, 'system'),  -- admin -> 系统管理员
(2, 2, true, 'system'),  -- security_auditor -> 安全审计员
(3, 3, true, 'system'),  -- data_admin -> 数据管理员
(4, 6, true, 'system'),  -- sys_monitor -> 系统监控员

-- QMS用户角色关联
(5, 8, true, 'system'),  -- qa_manager -> QA经理
(5, 7, true, 'system'),  -- qa_manager -> QMS管理员 (同时拥有)
(6, 9, true, 'system'),  -- qa_specialist -> QA专员
(7, 10, true, 'system'), -- qc_manager -> QC经理
(8, 11, true, 'system'), -- qc_inspector -> QC检验员

-- MES用户角色关联
(9, 16, true, 'system'),  -- prod_manager -> 生产经理
(9, 15, true, 'system'),  -- prod_manager -> MES管理员 (同时拥有)
(10, 17, true, 'system'), -- prod_supervisor -> 生产主管
(11, 18, true, 'system'), -- operator_01 -> 操作员
(12, 18, true, 'system'), -- operator_02 -> 操作员

-- 其他用户角色关联
(13, 22, true, 'system'), -- equipment_manager -> 设备经理
(14, 32, true, 'system'), -- warehouse_manager -> 仓库经理
(15, 4, true, 'system');  -- hr_manager -> 普通用户

-- ==========================================================================
-- 9. 用户详细信息初始化
-- ==========================================================================

INSERT INTO user_profile (user_id, additional_info, position, photo_url) VALUES
(1, '{"employeeId": "EMP00001", "company": "GMP制药集团"}', 'IT系统主管', 'https://example.com/photos/admin.jpg'),
(5, '{"employeeId": "EMP00101", "certification": "GMP认证", "department": "质量管理部"}', 'QA部门经理', 'https://example.com/photos/qa_manager.jpg'),
(7, '{"employeeId": "EMP00103", "certification": "QC认证", "department": "质量控制实验室"}', 'QC部门经理', 'https://example.com/photos/qc_manager.jpg'),
(9, '{"employeeId": "EMP00201", "department": "生产管理部"}', '生产部经理', 'https://example.com/photos/prod_manager.jpg');

-- ==========================================================================
-- 提交事务
-- ==========================================================================

COMMIT TRANSACTION;

-- 初始化完成提示
SELECT 'GMP系统用户权限管理模板数据初始化完成！' AS "Status";

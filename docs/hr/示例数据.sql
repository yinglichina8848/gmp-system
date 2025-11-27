-- 人事管理系统示例数据
-- 兼容认证授权和审计要求

-- 创建示例数据的事务
START TRANSACTION;

-- 1. 认证系统相关示例数据 (auth_前缀表)

-- 用户表示例数据
INSERT INTO auth_users (id, username, password_hash, email, full_name, status, created_at, updated_at) VALUES
('001', 'admin', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW', 'admin@example.com', '系统管理员', 'ACTIVE', NOW(), NOW()),
('002', 'hr_manager', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW', 'hr_manager@example.com', 'HR经理', 'ACTIVE', NOW(), NOW()),
('003', 'hr_staff', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW', 'hr_staff@example.com', 'HR专员', 'ACTIVE', NOW(), NOW()),
('004', 'department_manager', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW', 'department_manager@example.com', '部门经理', 'ACTIVE', NOW(), NOW()),
('005', 'employee', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW', 'employee@example.com', '普通员工', 'ACTIVE', NOW(), NOW());

-- 角色表示例数据
INSERT INTO auth_roles (id, name, description, created_at, updated_at) VALUES
('101', 'SYSTEM_ADMIN', '系统管理员角色', NOW(), NOW()),
('102', 'HR_MANAGER', '人力资源经理角色', NOW(), NOW()),
('103', 'HR_STAFF', '人力资源专员角色', NOW(), NOW()),
('104', 'DEPARTMENT_MANAGER', '部门经理角色', NOW(), NOW()),
('105', 'EMPLOYEE', '普通员工角色', NOW(), NOW());

-- 权限表示例数据
INSERT INTO auth_permissions (id, code, name, description, resource_type, action, created_at, updated_at) VALUES
-- 员工管理相关权限
('201', 'employee:create', '创建员工', '允许创建新员工', 'employee', 'create', NOW(), NOW()),
('202', 'employee:read', '查看员工', '允许查看员工信息', 'employee', 'read', NOW(), NOW()),
('203', 'employee:update', '更新员工', '允许更新员工信息', 'employee', 'update', NOW(), NOW()),
('204', 'employee:delete', '删除员工', '允许删除员工', 'employee', 'delete', NOW(), NOW()),
('205', 'employee:list', '列出员工', '允许列出所有员工', 'employee', 'list', NOW(), NOW()),
-- 部门管理相关权限
('206', 'department:create', '创建部门', '允许创建新部门', 'department', 'create', NOW(), NOW()),
('207', 'department:read', '查看部门', '允许查看部门信息', 'department', 'read', NOW(), NOW()),
('208', 'department:update', '更新部门', '允许更新部门信息', 'department', 'update', NOW(), NOW()),
('209', 'department:delete', '删除部门', '允许删除部门', 'department', 'delete', NOW(), NOW()),
-- 考勤管理相关权限
('210', 'attendance:record', '记录考勤', '允许记录考勤数据', 'attendance', 'record', NOW(), NOW()),
('211', 'attendance:approve', '审批考勤', '允许审批考勤异常', 'attendance', 'approve', NOW(), NOW()),
('212', 'attendance:report', '考勤报表', '允许查看考勤报表', 'attendance', 'report', NOW(), NOW()),
-- 培训管理相关权限
('213', 'training:create', '创建培训', '允许创建培训课程', 'training', 'create', NOW(), NOW()),
('214', 'training:participate', '参加培训', '允许报名参加培训', 'training', 'participate', NOW(), NOW()),
-- 绩效管理相关权限
('215', 'performance:review', '绩效评估', '允许进行绩效评估', 'performance', 'review', NOW(), NOW()),
('216', 'performance:view', '查看绩效', '允许查看绩效结果', 'performance', 'view', NOW(), NOW()),
-- 系统管理相关权限
('217', 'system:config', '系统配置', '允许配置系统参数', 'system', 'config', NOW(), NOW()),
('218', 'system:audit', '审计日志', '允许查看系统审计日志', 'system', 'audit', NOW(), NOW());

-- 角色-权限关联表
INSERT INTO auth_role_permissions (role_id, permission_id) VALUES
-- 系统管理员拥有所有权限
('101', '201'), ('101', '202'), ('101', '203'), ('101', '204'), ('101', '205'),
('101', '206'), ('101', '207'), ('101', '208'), ('101', '209'),
('101', '210'), ('101', '211'), ('101', '212'),
('101', '213'), ('101', '214'),
('101', '215'), ('101', '216'),
('101', '217'), ('101', '218'),
-- HR经理权限
('102', '201'), ('102', '202'), ('102', '203'), ('102', '204'), ('102', '205'),
('102', '206'), ('102', '207'), ('102', '208'), ('102', '209'),
('102', '210'), ('102', '211'), ('102', '212'),
('102', '213'), ('102', '214'),
('102', '215'), ('102', '216'),
('102', '218'),
-- HR专员权限
('103', '202'), ('103', '203'), ('103', '205'),
('103', '207'),
('103', '210'), ('103', '212'),
('103', '214'),
('103', '216'),
-- 部门经理权限
('104', '202'), ('104', '205'),
('104', '207'),
('104', '211'), ('104', '212'),
('104', '216'),
-- 普通员工权限
('105', '202'),
('105', '214'),
('105', '216');

-- 用户-角色关联表
INSERT INTO auth_user_roles (user_id, role_id) VALUES
('001', '101'),  -- 系统管理员拥有系统管理员角色
('002', '102'),  -- HR经理拥有HR经理角色
('003', '103'),  -- HR专员拥有HR专员角色
('004', '104'),  -- 部门经理拥有部门经理角色
('005', '105');  -- 普通员工拥有普通员工角色

-- 2. 人事系统相关示例数据 (hr_前缀表)

-- 国家/地区表
INSERT INTO hr_countries (id, name, code, status, created_at, updated_at) VALUES
('301', '中国', 'CN', 'ACTIVE', NOW(), NOW()),
('302', '美国', 'US', 'ACTIVE', NOW(), NOW()),
('303', '日本', 'JP', 'ACTIVE', NOW(), NOW());

-- 教育程度表
INSERT INTO hr_education_levels (id, name, code, status, created_at, updated_at) VALUES
('401', '高中', 'HIGH_SCHOOL', 'ACTIVE', NOW(), NOW()),
('402', '专科', 'ASSOCIATE', 'ACTIVE', NOW(), NOW()),
('403', '本科', 'BACHELOR', 'ACTIVE', NOW(), NOW()),
('404', '硕士', 'MASTER', 'ACTIVE', NOW(), NOW()),
('405', '博士', 'DOCTOR', 'ACTIVE', NOW(), NOW());

-- 婚姻状况表
INSERT INTO hr_marital_statuses (id, name, code, status, created_at, updated_at) VALUES
('501', '未婚', 'SINGLE', 'ACTIVE', NOW(), NOW()),
('502', '已婚', 'MARRIED', 'ACTIVE', NOW(), NOW()),
('503', '离婚', 'DIVORCED', 'ACTIVE', NOW(), NOW());

-- 职级表
INSERT INTO hr_job_levels (id, name, code, description, status, created_at, updated_at) VALUES
('601', '初级', 'JUNIOR', '初级职位', 'ACTIVE', NOW(), NOW()),
('602', '中级', 'MID', '中级职位', 'ACTIVE', NOW(), NOW()),
('603', '高级', 'SENIOR', '高级职位', 'ACTIVE', NOW(), NOW()),
('604', '专家', 'EXPERT', '专家职位', 'ACTIVE', NOW(), NOW()),
('605', '管理层', 'MANAGEMENT', '管理职位', 'ACTIVE', NOW(), NOW());

-- 部门表
INSERT INTO hr_departments (id, name, code, parent_id, manager_id, description, status, created_at, updated_at) VALUES
('701', '人力资源部', 'HR', NULL, '002', '负责公司人力资源管理', 'ACTIVE', NOW(), NOW()),
('702', '技术部', 'TECH', NULL, '004', '负责产品研发和技术支持', 'ACTIVE', NOW(), NOW()),
('703', '市场部', 'MARKETING', NULL, NULL, '负责市场推广和品牌建设', 'ACTIVE', NOW(), NOW()),
('704', '财务部', 'FINANCE', NULL, NULL, '负责财务管理和会计核算', 'ACTIVE', NOW(), NOW()),
('705', '行政部', 'ADMIN', NULL, NULL, '负责公司行政事务', 'ACTIVE', NOW(), NOW());

-- 职位表
INSERT INTO hr_positions (id, name, code, job_level_id, description, responsibilities, requirements, status, created_at, updated_at) VALUES
('801', 'HR经理', 'HR_MANAGER', '605', '负责人力资源部门管理', '制定HR战略，管理团队', '5年以上HR经验', 'ACTIVE', NOW(), NOW()),
('802', 'HR专员', 'HR_STAFF', '602', '负责日常人力资源事务', '招聘、培训、考勤管理', '2年以上HR经验', 'ACTIVE', NOW(), NOW()),
('803', '技术总监', 'CTO', '605', '负责技术团队管理', '技术战略规划，团队建设', '10年以上技术经验', 'ACTIVE', NOW(), NOW()),
('804', '高级开发工程师', 'SENIOR_DEV', '603', '负责核心模块开发', '代码开发，技术指导', '5年以上开发经验', 'ACTIVE', NOW(), NOW()),
('805', '前端开发工程师', 'FRONTEND_DEV', '602', '负责前端开发', '页面开发，用户交互', '3年以上前端经验', 'ACTIVE', NOW(), NOW());

-- 员工表
INSERT INTO hr_employees (id, user_id, employee_code, first_name, last_name, gender, birth_date, id_card_number, phone_number, email, address, department_id, position_id, country_id, education_level_id, marital_status_id, status, hire_date, termination_date, created_by, updated_by, created_at, updated_at) VALUES
('901', '001', 'EMP001', '系统', '管理员', 'M', '1980-01-01', '110101198001010001', '13800138001', 'admin@example.com', '北京市海淀区', '701', '801', '301', '403', '502', 'ACTIVE', '2020-01-01', NULL, '001', '001', NOW(), NOW()),
('902', '002', 'EMP002', '张三', '李四', 'M', '1985-05-15', '110101198505150002', '13800138002', 'hr_manager@example.com', '上海市浦东新区', '701', '801', '301', '403', '502', 'ACTIVE', '2020-03-15', NULL, '001', '001', NOW(), NOW()),
('903', '003', 'EMP003', '王五', '赵六', 'F', '1990-08-20', '110101199008200003', '13800138003', 'hr_staff@example.com', '广州市天河区', '701', '802', '301', '403', '501', 'ACTIVE', '2021-02-10', NULL, '002', '002', NOW(), NOW()),
('904', '004', 'EMP004', '陈七', '周八', 'M', '1982-11-30', '110101198211300004', '13800138004', 'department_manager@example.com', '深圳市南山区', '702', '803', '301', '404', '502', 'ACTIVE', '2019-12-01', NULL, '001', '001', NOW(), NOW()),
('905', '005', 'EMP005', '吴九', '郑十', 'M', '1995-03-05', '110101199503050005', '13800138005', 'employee@example.com', '杭州市西湖区', '702', '805', '301', '403', '501', 'ACTIVE', '2022-01-15', NULL, '003', '003', NOW(), NOW());

-- 更新部门表的manager_id
UPDATE hr_departments SET manager_id = '902' WHERE id = '701';
UPDATE hr_departments SET manager_id = '904' WHERE id = '702';

-- 员工档案表
INSERT INTO hr_employee_files (id, employee_id, file_name, file_path, file_type, file_size, description, created_by, updated_by, created_at, updated_at) VALUES
('1001', '901', '系统管理员简历.pdf', '/files/employees/901/resume.pdf', 'application/pdf', 1024000, '系统管理员简历', '001', '001', NOW(), NOW()),
('1002', '902', '张三照片.jpg', '/files/employees/902/photo.jpg', 'image/jpeg', 512000, 'HR经理照片', '002', '002', NOW(), NOW()),
('1003', '903', '王五入职登记表.xlsx', '/files/employees/903/entry_form.xlsx', 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet', 204800, 'HR专员入职登记表', '002', '002', NOW(), NOW());

-- 合同表
INSERT INTO hr_contracts (id, employee_id, contract_number, start_date, end_date, contract_type, contract_content, status, created_by, updated_by, created_at, updated_at) VALUES
('1101', '901', 'CONTRACT001', '2020-01-01', '2025-12-31', '永久合同', '无固定期限劳动合同', 'ACTIVE', '001', '001', NOW(), NOW()),
('1102', '902', 'CONTRACT002', '2020-03-15', '2025-03-14', '固定期限合同', '五年期固定期限劳动合同', 'ACTIVE', '001', '001', NOW(), NOW()),
('1103', '903', 'CONTRACT003', '2021-02-10', '2024-02-09', '固定期限合同', '三年期固定期限劳动合同', 'ACTIVE', '002', '002', NOW(), NOW()),
('1104', '904', 'CONTRACT004', '2019-12-01', '2024-11-30', '固定期限合同', '五年期固定期限劳动合同', 'ACTIVE', '001', '001', NOW(), NOW()),
('1105', '905', 'CONTRACT005', '2022-01-15', '2025-01-14', '固定期限合同', '三年期固定期限劳动合同', 'ACTIVE', '003', '003', NOW(), NOW());

-- 资质证书表
INSERT INTO hr_qualifications (id, employee_id, qualification_name, certificate_number, issuing_authority, issue_date, expire_date, status, created_by, updated_by, created_at, updated_at) VALUES
('1201', '901', 'PMP认证', 'PMP20200001', 'PMI', '2020-06-01', '2023-05-31', 'EXPIRED', '001', '001', NOW(), NOW()),
('1202', '902', '人力资源管理师', 'HRM20190001', '人力资源和社会保障部', '2019-12-15', '2024-12-14', 'VALID', '001', '001', NOW(), NOW()),
('1203', '904', '系统架构师', 'SAA20180001', '工业和信息化部', '2018-10-01', '2023-09-30', 'EXPIRED', '001', '001', NOW(), NOW()),
('1204', '905', 'AWS认证开发者', 'AWSCD20210001', 'Amazon Web Services', '2021-08-15', '2024-08-14', 'VALID', '003', '003', NOW(), NOW());

-- 考勤规则表
INSERT INTO hr_attendance_rules (id, name, description, check_in_time, check_out_time, late_threshold, early_leave_threshold, overtime_threshold, status, created_at, updated_at) VALUES
('1301', '标准工作时间', '周一至周五标准工作时间', '09:00:00', '18:00:00', '09:15:00', '17:45:00', '18:30:00', 'ACTIVE', NOW(), NOW()),
('1302', '弹性工作时间', '弹性工作制', '09:30:00', '18:30:00', '10:00:00', '18:00:00', '19:00:00', 'ACTIVE', NOW(), NOW());

-- 考勤记录表
INSERT INTO hr_attendance_records (id, employee_id, record_date, check_in_time, check_out_time, attendance_status, work_type, working_hours, exception_type, exception_reason, created_by, updated_by, created_at, updated_at) VALUES
('1401', '901', '2023-06-01', '08:50:00', '18:10:00', 'PRESENT', 'REGULAR', 9.33, NULL, NULL, '001', '001', NOW(), NOW()),
('1402', '902', '2023-06-01', '09:05:00', '17:55:00', 'PRESENT', 'REGULAR', 8.83, NULL, NULL, '001', '001', NOW(), NOW()),
('1403', '903', '2023-06-01', '09:20:00', '18:20:00', 'LATE', 'REGULAR', 8.67, 'LATE_ARRIVAL', '交通拥堵', '002', '002', NOW(), NOW()),
('1404', '904', '2023-06-01', '08:45:00', '19:00:00', 'PRESENT', 'OVERTIME', 10.25, NULL, NULL, '001', '001', NOW(), NOW()),
('1405', '905', '2023-06-01', '09:00:00', '17:30:00', 'EARLY_LEAVE', 'REGULAR', 8.50, 'EARLY_DEPARTURE', '个人原因', '003', '003', NOW(), NOW()),
('1406', '901', '2023-06-02', '08:55:00', '18:05:00', 'PRESENT', 'REGULAR', 9.17, NULL, NULL, '001', '001', NOW(), NOW()),
('1407', '902', '2023-06-02', '09:10:00', '18:10:00', 'PRESENT', 'REGULAR', 9.00, NULL, NULL, '001', '001', NOW(), NOW()),
('1408', '903', '2023-06-02', '08:55:00', '18:00:00', 'PRESENT', 'REGULAR', 9.08, NULL, NULL, '002', '002', NOW(), NOW()),
('1409', '904', '2023-06-02', '09:00:00', '18:30:00', 'PRESENT', 'OVERTIME', 9.50, NULL, NULL, '001', '001', NOW(), NOW()),
('1410', '905', '2023-06-02', '09:05:00', '18:15:00', 'PRESENT', 'REGULAR', 9.17, NULL, NULL, '003', '003', NOW(), NOW());

-- 培训课程表
INSERT INTO hr_training_courses (id, course_name, code, trainer, content, duration_hours, location, status, created_at, updated_at) VALUES
('1501', 'GMP基础知识培训', 'TRAIN001', '张三', 'GMP法规要求、质量管理体系', 8, '会议室A', 'COMPLETED', NOW(), NOW()),
('1502', '新员工入职培训', 'TRAIN002', '李四', '公司文化、规章制度、安全培训', 16, '培训室', 'IN_PROGRESS', NOW(), NOW()),
('1503', '领导力提升课程', 'TRAIN003', '外部讲师', '团队管理、沟通技巧、决策能力', 24, '培训中心', 'PLANNED', NOW(), NOW()),
('1504', '技术创新工作坊', 'TRAIN004', '王五', '新技术趋势、创新思维、项目实践', 12, '实验室', 'PLANNED', NOW(), NOW());

-- 培训记录表
INSERT INTO hr_training_records (id, employee_id, course_id, completion_date, grade, completion_status, created_by, updated_by, created_at, updated_at) VALUES
('1601', '901', '1501', '2023-05-10', '优秀', 'COMPLETED', '002', '002', NOW(), NOW()),
('1602', '902', '1501', '2023-05-10', '优秀', 'COMPLETED', '002', '002', NOW(), NOW()),
('1603', '903', '1501', '2023-05-10', '良好', 'COMPLETED', '002', '002', NOW(), NOW()),
('1604', '904', '1501', '2023-05-10', '良好', 'COMPLETED', '002', '002', NOW(), NOW()),
('1605', '905', '1502', NULL, NULL, 'IN_PROGRESS', '003', '003', NOW(), NOW()),
('1606', '901', '1503', NULL, NULL, 'PENDING', '002', '002', NOW(), NOW()),
('1607', '904', '1503', NULL, NULL, 'PENDING', '002', '002', NOW(), NOW()),
('1608', '904', '1504', NULL, NULL, 'PENDING', '002', '002', NOW(), NOW()),
('1609', '905', '1504', NULL, NULL, 'PENDING', '003', '003', NOW(), NOW());

-- 绩效评估表
INSERT INTO hr_performance_reviews (id, employee_id, reviewer_id, review_cycle, review_date, score, comments, status, created_by, updated_by, created_at, updated_at) VALUES
('1701', '901', '901', '2022年度', '2023-01-15', 95.5, '工作表现优秀，系统运维稳定', 'COMPLETED', '001', '001', NOW(), NOW()),
('1702', '902', '901', '2022年度', '2023-01-20', 92.0, 'HR工作全面，团队管理有效', 'COMPLETED', '001', '001', NOW(), NOW()),
('1703', '903', '902', '2022年度', '2023-01-25', 88.5, '工作认真负责，需提升专业技能', 'COMPLETED', '002', '002', NOW(), NOW()),
('1704', '904', '901', '2022年度', '2023-01-18', 94.0, '技术领导力强，项目交付及时', 'COMPLETED', '001', '001', NOW(), NOW()),
('1705', '905', '904', '2022年度', '2023-01-30', 85.0, '工作积极，需加强代码质量', 'COMPLETED', '003', '003', NOW(), NOW()),
('1706', '901', '901', '2023年上半年', '2023-07-10', NULL, NULL, 'PENDING', '001', '001', NOW(), NOW()),
('1707', '902', '901', '2023年上半年', '2023-07-15', NULL, NULL, 'PENDING', '001', '001', NOW(), NOW());

-- 员工审计日志表
INSERT INTO hr_employee_audits (id, employee_id, operation_type, field_name, old_value, new_value, operator_id, operation_time, ip_address) VALUES
('1801', '901', 'CREATE', NULL, NULL, '系统管理员', '001', NOW(), '192.168.1.100'),
('1802', '902', 'CREATE', NULL, NULL, 'HR经理', '001', NOW(), '192.168.1.100'),
('1803', '902', 'UPDATE', 'phone_number', '13800138002', '13800138006', '002', NOW(), '192.168.1.101'),
('1804', '903', 'CREATE', NULL, NULL, 'HR专员', '002', NOW(), '192.168.1.101'),
('1805', '904', 'CREATE', NULL, NULL, '技术总监', '001', NOW(), '192.168.1.100'),
('1806', '905', 'CREATE', NULL, NULL, '前端开发工程师', '003', NOW(), '192.168.1.102'),
('1807', '905', 'UPDATE', 'position_id', '805', '804', '004', NOW(), '192.168.1.103'),
('1808', '901', 'LOGIN', NULL, NULL, '用户登录', '001', NOW(), '192.168.1.100'),
('1809', '902', 'LOGIN', NULL, NULL, '用户登录', '002', NOW(), '192.168.1.101'),
('1810', '903', 'LOGIN', NULL, NULL, '用户登录', '003', NOW(), '192.168.1.102');

-- 提交事务
COMMIT;

-- 注释说明：
-- 1. 密码说明：所有用户的密码均为 'password' 的 bcrypt 加密值
-- 2. 示例数据展示了完整的用户-角色-权限体系
-- 3. 包含了员工基本信息、考勤、培训、绩效等完整的人事管理示例数据
-- 4. 提供了审计日志示例，满足GMP合规要求
-- 5. 数据之间保持一致性，特别是user_id和employee_id的关联
-- 6. 可以根据实际需求扩展更多示例数据

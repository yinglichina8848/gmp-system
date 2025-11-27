-- 创建通用文件表
CREATE TABLE IF NOT EXISTS common_file (
    id BIGSERIAL PRIMARY KEY,
    file_name VARCHAR(500) NOT NULL,
    file_type VARCHAR(200),
    file_size BIGINT,
    file_path VARCHAR(1000) NOT NULL,
    checksum VARCHAR(128),
    bucket_name VARCHAR(100),
    module VARCHAR(100) NOT NULL,
    created_by VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    metadata JSONB,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    version INTEGER DEFAULT 0
);

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_common_file_module ON common_file(module);
CREATE INDEX IF NOT EXISTS idx_common_file_status ON common_file(status);
CREATE INDEX IF NOT EXISTS idx_common_file_checksum ON common_file(checksum);
CREATE INDEX IF NOT EXISTS idx_common_file_created_by ON common_file(created_by);
CREATE INDEX IF NOT EXISTS idx_common_file_created_at ON common_file(created_at);
CREATE INDEX IF NOT EXISTS idx_common_file_file_name ON common_file(file_name);

-- 创建文档版本表（如果不存在）
CREATE TABLE IF NOT EXISTS document_versions (
    id BIGSERIAL PRIMARY KEY,
    document_id BIGINT REFERENCES documents(id) ON DELETE CASCADE,
    version_number VARCHAR(20) NOT NULL,
    version_type VARCHAR(20) DEFAULT 'REVISION',
    change_reason TEXT,
    change_summary TEXT,
    author VARCHAR(100) NOT NULL,
    reviewer VARCHAR(100),
    approver VARCHAR(100),
    approval_date TIMESTAMP,
    file_path VARCHAR(500),
    file_size BIGINT,
    checksum VARCHAR(128),
    is_current BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'DRAFT'
);

-- 创建文档版本表索引
CREATE INDEX IF NOT EXISTS idx_document_versions_document_id ON document_versions(document_id);
CREATE INDEX IF NOT EXISTS idx_document_versions_version_number ON document_versions(version_number);
CREATE INDEX IF NOT EXISTS idx_document_versions_is_current ON document_versions(is_current);
CREATE INDEX IF NOT EXISTS idx_document_versions_author ON document_versions(author);
CREATE INDEX IF NOT EXISTS idx_document_versions_status ON document_versions(status);

-- 创建文档分类表（如果不存在）
CREATE TABLE IF NOT EXISTS document_categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    code VARCHAR(50) UNIQUE NOT NULL,
    description TEXT,
    parent_id BIGINT REFERENCES document_categories(id),
    level INTEGER DEFAULT 1,
    sort_order INTEGER DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100)
);

-- 创建文档分类表索引
CREATE INDEX IF NOT EXISTS idx_document_categories_parent_id ON document_categories(parent_id);
CREATE INDEX IF NOT EXISTS idx_document_categories_code ON document_categories(code);
CREATE INDEX IF NOT EXISTS idx_document_categories_level ON document_categories(level);
CREATE INDEX IF NOT EXISTS idx_document_categories_is_active ON document_categories(is_active);

-- 创建审批流程表（如果不存在）
CREATE TABLE IF NOT EXISTS approval_workflows (
    id BIGSERIAL PRIMARY KEY,
    workflow_code VARCHAR(50) UNIQUE NOT NULL,
    workflow_name VARCHAR(200) NOT NULL,
    document_type VARCHAR(50),
    workflow_definition TEXT,
    version VARCHAR(20) DEFAULT '1.0',
    is_active BOOLEAN DEFAULT TRUE,
    created_by VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建审批流程表索引
CREATE INDEX IF NOT EXISTS idx_approval_workflows_code ON approval_workflows(workflow_code);
CREATE INDEX IF NOT EXISTS idx_approval_workflows_document_type ON approval_workflows(document_type);
CREATE INDEX IF NOT EXISTS idx_approval_workflows_is_active ON approval_workflows(is_active);

-- 创建审批实例表（如果不存在）
CREATE TABLE IF NOT EXISTS approval_instances (
    id BIGSERIAL PRIMARY KEY,
    workflow_id BIGINT REFERENCES approval_workflows(id),
    document_id BIGINT REFERENCES documents(id),
    initiator VARCHAR(100) NOT NULL,
    current_step VARCHAR(50),
    status VARCHAR(20) DEFAULT 'IN_PROGRESS',
    priority VARCHAR(20) DEFAULT 'NORMAL',
    started_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP,
    deadline TIMESTAMP,
    comments TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建审批实例表索引
CREATE INDEX IF NOT EXISTS idx_approval_instances_workflow_id ON approval_instances(workflow_id);
CREATE INDEX IF NOT EXISTS idx_approval_instances_document_id ON approval_instances(document_id);
CREATE INDEX IF NOT EXISTS idx_approval_instances_status ON approval_instances(status);
CREATE INDEX IF NOT EXISTS idx_approval_instances_initiator ON approval_instances(initiator);
CREATE INDEX IF NOT EXISTS idx_approval_instances_current_step ON approval_instances(current_step);

-- 创建文档访问日志表（如果不存在）
CREATE TABLE IF NOT EXISTS document_access_logs (
    id BIGSERIAL PRIMARY KEY,
    document_id BIGINT REFERENCES documents(id),
    document_version_id BIGINT REFERENCES document_versions(id),
    user_id VARCHAR(100) NOT NULL,
    user_name VARCHAR(200),
    user_department VARCHAR(100),
    access_type VARCHAR(20) NOT NULL,
    access_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    session_id VARCHAR(50),
    ip_address VARCHAR(45),
    user_agent TEXT,
    success BOOLEAN DEFAULT TRUE,
    error_message VARCHAR(500)
);

-- 创建文档访问日志表索引
CREATE INDEX IF NOT EXISTS idx_document_access_logs_document_id ON document_access_logs(document_id);
CREATE INDEX IF NOT EXISTS idx_document_access_logs_user_id ON document_access_logs(user_id);
CREATE INDEX IF NOT EXISTS idx_document_access_logs_access_type ON document_access_logs(access_type);
CREATE INDEX IF NOT EXISTS idx_document_access_logs_access_time ON document_access_logs(access_time);

-- 创建电子签名记录表（如果不存在）
CREATE TABLE IF NOT EXISTS electronic_signatures (
    id BIGSERIAL PRIMARY KEY,
    document_id BIGINT REFERENCES documents(id),
    document_version_id BIGINT REFERENCES document_versions(id),
    signer_id VARCHAR(100) NOT NULL,
    signer_name VARCHAR(200),
    signer_role VARCHAR(100),
    signature_type VARCHAR(20) NOT NULL,
    signature_data TEXT,
    signature_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ip_address VARCHAR(45),
    certificate_info TEXT,
    validity_status VARCHAR(20) DEFAULT 'VALID',
    verification_result BOOLEAN DEFAULT TRUE
);

-- 创建电子签名记录表索引
CREATE INDEX IF NOT EXISTS idx_electronic_signatures_document_id ON electronic_signatures(document_id);
CREATE INDEX IF NOT EXISTS idx_electronic_signatures_signer_id ON electronic_signatures(signer_id);
CREATE INDEX IF NOT EXISTS idx_electronic_signatures_signature_type ON electronic_signatures(signature_type);
CREATE INDEX IF NOT EXISTS idx_electronic_signatures_signature_time ON electronic_signatures(signature_time);

-- 创建文档合规记录表（如果不存在）
CREATE TABLE IF NOT EXISTS document_compliance_logs (
    id BIGSERIAL PRIMARY KEY,
    document_id BIGINT REFERENCES documents(id),
    compliance_type VARCHAR(50) NOT NULL,
    compliance_standard VARCHAR(200),
    check_result VARCHAR(20) NOT NULL,
    checked_by VARCHAR(100),
    checked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    notes TEXT,
    corrective_action TEXT,
    follow_up_date TIMESTAMP
);

-- 创建文档合规记录表索引
CREATE INDEX IF NOT EXISTS idx_document_compliance_logs_document_id ON document_compliance_logs(document_id);
CREATE INDEX IF NOT EXISTS idx_document_compliance_logs_compliance_type ON document_compliance_logs(compliance_type);
CREATE INDEX IF NOT EXISTS idx_document_compliance_logs_check_result ON document_compliance_logs(check_result);
CREATE INDEX IF NOT EXISTS idx_document_compliance_logs_checked_at ON document_compliance_logs(checked_at);

-- 创建文件上传记录表（如果不存在）
CREATE TABLE IF NOT EXISTS file_upload_logs (
    id BIGSERIAL PRIMARY KEY,
    file_id BIGINT REFERENCES common_file(id),
    upload_session_id VARCHAR(100),
    chunk_index INTEGER,
    chunk_size BIGINT,
    total_chunks INTEGER,
    upload_status VARCHAR(50) DEFAULT 'IN_PROGRESS',
    error_message TEXT,
    upload_start_time TIMESTAMP,
    upload_end_time TIMESTAMP,
    ip_address VARCHAR(45),
    user_agent TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建文件上传记录表索引
CREATE INDEX IF NOT EXISTS idx_file_upload_logs_file_id ON file_upload_logs(file_id);
CREATE INDEX IF NOT EXISTS idx_file_upload_logs_session_id ON file_upload_logs(upload_session_id);
CREATE INDEX IF NOT EXISTS idx_file_upload_logs_status ON file_upload_logs(upload_status);

-- 添加触发器以自动更新 updated_at 字段
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- 为相关表添加触发器
CREATE TRIGGER update_common_file_updated_at BEFORE UPDATE ON common_file 
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_document_versions_updated_at BEFORE UPDATE ON document_versions 
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_document_categories_updated_at BEFORE UPDATE ON document_categories 
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_approval_workflows_updated_at BEFORE UPDATE ON approval_workflows 
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_approval_instances_updated_at BEFORE UPDATE ON approval_instances 
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- 插入默认数据
INSERT INTO document_categories (name, code, description, level, created_by) VALUES
('根分类', 'ROOT', '文档根分类', 1, 'system'),
('SOP文档', 'SOP', '标准操作规程', 2, 'system'),
('技术规范', 'SPEC', '技术规范文档', 2, 'system'),
('质量记录', 'RECORD', '质量记录文档', 2, 'system'),
('操作手册', 'MANUAL', '操作手册文档', 2, 'system')
ON CONFLICT (code) DO NOTHING;

-- 插入默认审批流程
INSERT INTO approval_workflows (workflow_code, workflow_name, document_type, workflow_definition, created_by) VALUES
('SOP_APPROVAL', 'SOP文档审批流程', 'SOP', '{"steps":[{"name":"起草","type":"START"},{"name":"技术审核","type":"APPROVAL"},{"name":"质量审核","type":"APPROVAL"},{"name":"发布","type":"END"}]}', 'system'),
('SPEC_APPROVAL', '技术规范审批流程', 'SPEC', '{"steps":[{"name":"起草","type":"START"},{"name":"技术评审","type":"APPROVAL"},{"name":"标准化审核","type":"APPROVAL"},{"name":"发布","type":"END"}]}', 'system')
ON CONFLICT (workflow_code) DO NOTHING;
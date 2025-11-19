-- GMP 系统数据库初始化脚本
-- 创建各个微服务的数据库

-- QMS (质量管理系统) 数据库
CREATE DATABASE qms_db;
GRANT ALL PRIVILEGES ON DATABASE qms_db TO postgres;

-- MES (生产执行系统) 数据库
CREATE DATABASE mes_db;
GRANT ALL PRIVILEGES ON DATABASE mes_db TO postgres;

-- LIMS (实验室信息管理系统) 数据库
CREATE DATABASE lims_db;
GRANT ALL PRIVILEGES ON DATABASE lims_db TO postgres;

-- EDMS (电子文档管理系统) 数据库
CREATE DATABASE edms_db;
GRANT ALL PRIVILEGES ON DATABASE edms_db TO postgres;

-- Auth (认证授权服务) 数据库
CREATE DATABASE auth_db;
GRANT ALL PRIVILEGES ON DATABASE auth_db TO postgres;

-- Config (配置中心) 数据库
CREATE DATABASE config_db;
GRANT ALL PRIVILEGES ON DATABASE config_db TO postgres;

-- Message (消息服务) 数据库
CREATE DATABASE message_db;
GRANT ALL PRIVILEGES ON DATABASE message_db TO postgres;

-- File (文件服务) 数据库
CREATE DATABASE file_db;
GRANT ALL PRIVILEGES ON DATABASE file_db TO postgres;

-- 创建GMP专用用户
CREATE USER gmp_user WITH PASSWORD 'gmp_password_2024';
GRANT CONNECT ON DATABASE qms_db TO gmp_user;
GRANT CONNECT ON DATABASE mes_db TO gmp_user;
GRANT CONNECT ON DATABASE lims_db TO gmp_user;
GRANT CONNECT ON DATABASE edms_db TO gmp_user;
GRANT CONNECT ON DATABASE auth_db TO gmp_user;
GRANT CONNECT ON DATABASE config_db TO gmp_user;
GRANT CONNECT ON DATABASE message_db TO gmp_user;
GRANT CONNECT ON DATABASE file_db TO gmp_user;

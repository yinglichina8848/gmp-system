# EDMS服务变更日志

## 2024-07-17 - 文件服务集成版本

### 主要变更

- **服务合并**: 原独立的File服务已集成到EDMS服务中，不再作为独立微服务存在
- **存储实现**: 采用MinIO客户端实现对象存储功能
- **API兼容性**: 保留原File服务API的兼容层，确保现有系统无缝迁移

### 详细技术变更

#### 1. 新增组件

- `MinioConfig`: MinIO客户端配置类
- `MinioFileStorageServiceImpl`: MinIO文件存储实现
- `FileServiceCompatibilityController`: 原File服务API兼容层控制器
- `FileStorageService`: 文件存储服务接口

#### 2. 配置更新

- 新增MinIO连接配置（endpoint、accessKey、secretKey、bucketName）
- 新增Eureka服务注册配置，确保服务发现功能正常
- 优化文件存储路径结构，采用"模块/分类/时间戳_UUID.扩展名"格式

#### 3. 依赖更新

- 添加MinIO客户端依赖
- 添加Spring Cloud Discovery Client依赖
- 更新Spring Boot版本至3.2.5
- 更新Spring Cloud版本至2023.0.3

#### 4. API变更

- **新增兼容层接口**: `/api/v1/file-compatibility/*`
  - 文件上传: `POST /api/v1/file-compatibility/upload`
  - 文件下载: `GET /api/v1/file-compatibility/download/{fileId}`
  - 文件删除: `DELETE /api/v1/file-compatibility/delete/{fileId}`
  - 获取文件信息: `GET /api/v1/file-compatibility/info/{fileId}`
  - 生成预签名URL: `GET /api/v1/file-compatibility/presigned-url/{fileId}`
  - 批量上传: `POST /api/v1/file-compatibility/batch-upload`
  - 文件列表查询: `GET /api/v1/file-compatibility/list`

#### 5. 测试更新

- 新增`MinioFileStorageServiceIntegrationTest`: 测试MinIO文件存储功能
- 新增`DocumentStorageIntegrationTest`: 测试文档与文件存储集成功能
- 更新`CommonFileServiceIntegrationTest`: 适配内部文件存储服务
- 更新`FileServiceCompatibilityControllerTest`: 测试兼容层接口

### 升级指南

#### 服务端升级

1. 停止原File服务
2. 启动更新后的EDMS服务
3. 确保Eureka服务注册正常

#### 客户端适配

1. 无需修改现有代码，兼容层接口保持与原File服务API一致
2. 长期规划：逐步迁移到EDMS直接调用，去除兼容层依赖

### 性能影响

- **优势**: 减少服务间调用，降低网络延迟
- **资源使用**: EDMS服务内存占用增加约10-15%
- **部署**: 减少了微服务数量，简化了系统架构和部署复杂度
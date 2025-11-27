# EDMS服务架构文档

## 1. 服务概述

EDMS (Enterprise Document Management System) 服务是GMP系统的核心组件，负责企业级文档和文件管理。本服务已完成与原File服务的合并，现在统一提供文件存储、文档管理和版本控制功能。

## 2. 服务架构

### 2.1 核心组件

- **控制器层**：处理HTTP请求，提供RESTful API接口
- **服务层**：实现业务逻辑，协调各组件工作
- **数据访问层**：负责数据持久化操作
- **存储层**：使用Minio进行对象存储，存储实际文件内容
- **兼容层**：确保原File服务的API请求能够正常工作

### 2.2 架构图

```
+------------------+     +------------------+     +------------------+
|                  |     |                  |     |                  |
|  API 客户端      +---->+  EDMS 控制器层   +---->+  服务层          |
|                  |     |                  |     |                  |
+------------------+     +------------------+     +------------------+
                                                           |
                                                           v
+------------------+     +------------------+     +------------------+
|                  |     |                  |     |                  |
|  Minio对象存储   +<----+  存储适配器      +<----+  数据访问层      |
|                  |     |                  |     |                  |
+------------------+     +------------------+     +------------------+
```

## 3. 模块结构

```
com.gmp.edms
├── controller/         # 控制器层
│   ├── DocumentController.java            # 文档管理控制器
│   ├── DocumentCategoryController.java    # 文档分类控制器
│   ├── DocumentVersionController.java     # 文档版本控制器
│   ├── FileController.java                # 通用文件管理控制器
│   └── FileServiceCompatibilityController.java # File服务兼容层控制器
├── dto/                # 数据传输对象
│   ├── DocumentDTO.java                   # 文档DTO
│   ├── DocumentCategoryDTO.java           # 文档分类DTO
│   ├── DocumentVersionDTO.java            # 文档版本DTO
│   ├── CommonFileDTO.java                 # 通用文件DTO
│   └── CommonFileUploadDTO.java           # 文件上传DTO
├── entity/             # 实体类
│   ├── Document.java                      # 文档实体
│   ├── DocumentCategory.java              # 文档分类实体
│   ├── DocumentVersion.java               # 文档版本实体
│   └── CommonFile.java                    # 通用文件实体
├── repository/         # 数据访问层
│   ├── DocumentRepository.java            # 文档仓库
│   ├── DocumentCategoryRepository.java    # 文档分类仓库
│   ├── DocumentVersionRepository.java     # 文档版本仓库
│   └── CommonFileRepository.java          # 通用文件仓库
└── service/            # 服务层
    ├── DocumentService.java               # 文档服务接口
    ├── DocumentCategoryService.java       # 文档分类服务接口
    ├── DocumentVersionService.java        # 文档版本服务接口
    ├── FileStorageService.java            # 文件存储服务接口
    ├── CommonFileService.java             # 通用文件服务接口
    └── impl/                              # 服务实现
        ├── DocumentServiceImpl.java       # 文档服务实现
        ├── DocumentCategoryServiceImpl.java # 文档分类服务实现
        ├── DocumentVersionServiceImpl.java # 文档版本服务实现
        ├── MinioFileStorageServiceImpl.java # Minio文件存储服务实现
        └── CommonFileServiceImpl.java     # 通用文件服务实现
```

## 4. API接口

### 4.1 通用文件管理API

| 路径 | 方法 | 描述 | 模块 |
|------|------|------|------|
| `/api/v1/files/upload` | POST | 上传单个文件 | 文件管理 |
| `/api/v1/files/batch-upload` | POST | 批量上传文件 | 文件管理 |
| `/api/v1/files/{fileId}/download` | GET | 下载文件 | 文件管理 |
| `/api/v1/files/{fileId}` | GET | 获取文件信息 | 文件管理 |
| `/api/v1/files/{fileId}` | DELETE | 删除文件 | 文件管理 |
| `/api/v1/files/batch` | DELETE | 批量删除文件 | 文件管理 |
| `/api/v1/files/{fileId}/presigned-url` | GET | 生成预签名URL | 文件管理 |
| `/api/v1/files` | GET | 查询文件列表 | 文件管理 |
| `/api/v1/files/{fileId}/metadata` | PUT | 更新文件元数据 | 文件管理 |
| `/api/v1/files/statistics` | GET | 获取文件统计信息 | 文件管理 |

### 4.2 File服务兼容API

| 路径 | 方法 | 描述 | 对应原File服务API |
|------|------|------|------------------|
| `/api/v1/file-service/files` | POST | 上传文件 | `/api/v1/files` |
| `/api/v1/file-service/files/{fileId}/download` | GET | 下载文件 | `/api/v1/files/{fileId}/download` |
| `/api/v1/file-service/files/{fileId}` | GET | 获取文件信息 | `/api/v1/files/{fileId}` |
| `/api/v1/file-service/files/{fileId}` | DELETE | 删除文件 | `/api/v1/files/{fileId}` |
| `/api/v1/file-service/files/{fileId}/presigned-url` | GET | 生成预签名URL | `/api/v1/files/{fileId}/presigned-url` |
| `/api/v1/file-service/files` | GET | 查询文件列表 | `/api/v1/files` |
| `/api/v1/file-service/redirect/**` | GET | 自动重定向到新API | 不适用 |

### 4.3 文档管理API

| 路径 | 方法 | 描述 | 模块 |
|------|------|------|------|
| `/api/v1/documents` | POST | 创建文档 | 文档管理 |
| `/api/v1/documents/{id}` | GET | 获取文档信息 | 文档管理 |
| `/api/v1/documents/{id}` | PUT | 更新文档 | 文档管理 |
| `/api/v1/documents/{id}` | DELETE | 删除文档 | 文档管理 |
| `/api/v1/documents` | GET | 查询文档列表 | 文档管理 |

### 4.4 文档版本API

| 路径 | 方法 | 描述 | 模块 |
|------|------|------|------|
| `/api/v1/documents/{documentId}/versions` | POST | 上传新版本 | 版本管理 |
| `/api/v1/documents/{documentId}/versions` | GET | 获取版本列表 | 版本管理 |
| `/api/v1/documents/{documentId}/versions/current` | GET | 获取当前版本 | 版本管理 |

## 5. 文件存储结构

### 5.1 Minio存储结构

- **通用文件路径**：`{module}/{yyyyMMdd}/{uuid}/{fileName}`
- **文档文件路径**：`documents/{documentId}/{version}/{fileName}`
- **存储策略**：按模块和日期进行分桶和目录组织，提高查询效率

### 5.2 数据库存储

- **CommonFile表**：存储通用文件元数据
- **Document表**：存储文档基本信息
- **DocumentVersion表**：存储文档版本信息
- **DocumentCategory表**：存储文档分类信息

## 6. 性能优化

- **文件预签名URL**：减少服务器中转，提高文件访问效率
- **批量操作API**：支持批量上传和删除，减少请求次数
- **模块分离**：按模块组织文件，提高查询性能
- **元数据索引**：为常用查询字段建立索引
- **缓存策略**：缓存文件元数据和统计信息

## 7. 安全措施

- **访问控制**：基于JWT的身份认证
- **文件权限**：支持按模块和用户设置访问权限
- **文件校验**：使用MD5校验确保文件完整性
- **HTTPS传输**：所有API请求使用HTTPS加密
- **预签名URL过期**：临时访问URL设置合理的过期时间

## 8. 服务迁移说明

### 8.1 File服务迁移

- **API兼容**：通过兼容层确保原File服务API继续可用
- **数据迁移**：原File服务的文件将自动迁移到新的存储结构
- **客户端适配**：建议客户端逐步迁移到新的API路径 `/api/v1/files`

### 8.2 迁移时间表

- **兼容层支持期**：提供12个月的API兼容支持
- **完全迁移截止**：12个月后将停止支持原File服务API路径

## 9. 监控与运维

- **日志记录**：记录所有文件操作日志
- **性能监控**：监控文件上传/下载性能
- **存储监控**：监控存储容量使用情况
- **错误告警**：配置关键错误的告警机制

## 10. 未来规划

- **分布式存储**：支持多区域存储部署
- **CDN集成**：优化大文件访问速度
- **文件预览**：支持常用文件格式的在线预览
- **文件加密**：支持敏感文件的加密存储
- **AI分类**：使用AI技术自动对文件进行分类和标签管理
package com.gmp.edms.service.impl;

import com.gmp.edms.dto.DocumentVersionDTO;
import com.gmp.edms.entity.Document;
import com.gmp.edms.entity.DocumentVersion;
import com.gmp.edms.repository.DocumentRepository;
import com.gmp.edms.repository.DocumentVersionRepository;
import com.gmp.edms.service.DocumentVersionService;
import com.gmp.edms.service.FileStorageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 文档版本服务实现
 */
@Service
public class DocumentVersionServiceImpl implements DocumentVersionService {
    private static final Logger log = LoggerFactory.getLogger(DocumentVersionServiceImpl.class);

    @Autowired
    private DocumentVersionRepository documentVersionRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public DocumentVersionDTO uploadDocumentVersion(Long documentId, MultipartFile file,
            String versionType, String changeReason,
            String changeSummary, String author) throws Exception {
        // 检查文件是否存在
        if (file == null || file.isEmpty()) {
            throw new Exception("文件不能为空");
        }

        // 计算文件校验和
        String checksum = calculateChecksum(file);

        // 检查文件是否已经上传过
        if (checkFileExists(checksum)) {
            throw new Exception("文件内容已存在");
        }

        // 生成新版本号
        String newVersionNumber = generateNewVersionNumber(documentId, versionType);

        // 创建文件保存路径
        String fileName = file.getOriginalFilename();
        String filePath = "versions/" + documentId + "/" + newVersionNumber + "/" + fileName;

        // 创建文档版本实体
        DocumentVersion documentVersion = new DocumentVersion();
        setFieldValue(documentVersion, "documentId", documentId);
        setFieldValue(documentVersion, "versionNumber", newVersionNumber);
        setFieldValue(documentVersion, "fileName", fileName);
        setFieldValue(documentVersion, "fileType", file.getContentType());
        setFieldValue(documentVersion, "fileSize", file.getSize());
        setFieldValue(documentVersion, "filePath", filePath);
        setFieldValue(documentVersion, "checksum", checksum);
        setFieldValue(documentVersion, "changeReason", changeReason);
        setFieldValue(documentVersion, "createdBy", author);
        setFieldValue(documentVersion, "createdTime", LocalDateTime.now());
        setFieldValue(documentVersion, "isCurrent", true);

        // 保存文档版本
        DocumentVersion savedVersion = documentVersionRepository.save(documentVersion);

        // 上传文件到存储服务
        fileStorageService.uploadFile(file, "edms-documents", "versions/" + documentId + "/" + newVersionNumber);

        // 更新其他版本为非当前版本
        List<DocumentVersion> otherVersions = documentVersionRepository
                .findByDocumentIdOrderByVersionNumberDesc(documentId);
        for (DocumentVersion version : otherVersions) {
            if (!getFieldValue(version, "id").equals(getFieldValue(savedVersion, "id"))) {
                setFieldValue(version, "isCurrent", false);
                documentVersionRepository.save(version);
            }
        }

        return modelMapper.map(savedVersion, DocumentVersionDTO.class);
    }

    @Override
    public List<DocumentVersionDTO> getDocumentVersions(Long documentId) {
        // 获取文档的所有版本
        List<DocumentVersion> versions = documentVersionRepository.findByDocumentIdOrderByCreatedAtDesc(documentId);

        // 转换为DTO并返回
        return versions.stream()
                .map(version -> {
                    DocumentVersionDTO dto = modelMapper.map(version, DocumentVersionDTO.class);
                    setFieldValue(dto, "formattedFileSize", formatFileSize((Long) getFieldValue(version, "fileSize")));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public DocumentVersionDTO getCurrentVersion(Long documentId) {
        // 查找文档的当前版本
        DocumentVersion version = documentVersionRepository.findByDocumentIdAndIsCurrentTrue(documentId)
                .orElseThrow(() -> new RuntimeException("文档没有设置当前版本: " + documentId));

        // 转换为DTO并返回
        DocumentVersionDTO dto = modelMapper.map(version, DocumentVersionDTO.class);
        setFieldValue(dto, "formattedFileSize", formatFileSize((Long) getFieldValue(version, "fileSize")));

        return dto;
    }

    public DocumentVersionDTO getCurrentDocumentVersion(Long documentId) {
        // 查找文档的当前版本
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("文档不存在: " + documentId));

        if (getFieldValue(document, "currentVersionId") == null) {
            throw new RuntimeException("文档还没有版本");
        }

        DocumentVersion version = documentVersionRepository.findById((Long) getFieldValue(document, "currentVersionId"))
                .orElseThrow(() -> new RuntimeException("当前版本不存在"));

        // 转换为DTO并返回
        DocumentVersionDTO dto = modelMapper.map(version, DocumentVersionDTO.class);
        setFieldValue(dto, "formattedFileSize", formatFileSize((Long) getFieldValue(version, "fileSize")));

        return dto;
    }

    @Override
    public DocumentVersionDTO getVersionById(Long versionId) {
        // 查找指定的版本
        DocumentVersion version = documentVersionRepository.findById(versionId)
                .orElseThrow(() -> new RuntimeException("版本不存在: " + versionId));

        // 转换为DTO并返回
        DocumentVersionDTO dto = modelMapper.map(version, DocumentVersionDTO.class);
        setFieldValue(dto, "formattedFileSize", formatFileSize((Long) getFieldValue(version, "fileSize")));

        return dto;
    }

    @Override
    public byte[] downloadDocumentVersion(Long versionId) throws Exception {
        // 查找指定的版本
        DocumentVersion version = documentVersionRepository.findById(versionId)
                .orElseThrow(() -> new RuntimeException("版本不存在: " + versionId));

        // 下载文件
        try (InputStream inputStream = fileStorageService.downloadFile("edms-documents",
                (String) getFieldValue(version, "filePath"))) {
            return IOUtils.toByteArray(inputStream);
        }
    }

    @Override
    @Transactional
    public void deleteDocumentVersion(Long versionId) throws Exception {
        // 查找指定的版本
        DocumentVersion version = documentVersionRepository.findById(versionId)
                .orElseThrow(() -> new RuntimeException("版本不存在: " + versionId));

        // 获取文档信息
        Document document = documentRepository.findById((Long) getFieldValue(version, "documentId"))
                .orElseThrow(() -> new RuntimeException("文档不存在"));

        // 检查是否是当前版本
        if (getFieldValue(document, "currentVersionId") != null &&
                getFieldValue(document, "currentVersionId").equals(versionId)) {
            // 如果是当前版本，查找上一个版本
            Optional<DocumentVersion> previousVersionOpt = documentVersionRepository
                    .findFirstByDocumentIdAndIdNotOrderByCreatedAtDesc(
                            (Long) getFieldValue(version, "documentId"), versionId);

            if (previousVersionOpt.isPresent()) {
                // 更新当前版本为上一个版本
                DocumentVersion previousVersion = previousVersionOpt.get();
                setFieldValue(document, "currentVersionId", getFieldValue(previousVersion, "id"));
                setFieldValue(document, "currentVersionNumber", getFieldValue(previousVersion, "versionNumber"));
            } else {
                // 如果没有其他版本，清空当前版本信息
                setFieldValue(document, "currentVersionId", null);
                setFieldValue(document, "currentVersionNumber", null);
            }

            documentRepository.save(document);
        }

        // 删除文件
        try {
            fileStorageService.deleteFile("", (String) getFieldValue(version, "filePath"));
        } catch (Exception e) {
            throw new RuntimeException("删除文件失败: " + e.getMessage(), e);
        }

        // 删除版本记录
        documentVersionRepository.delete(version);
    }

    // 计算文件大小总和
    private Long sumFileSize(List<DocumentVersion> versions) {
        return versions.stream()
                .mapToLong(version -> (Long) getFieldValue(version, "fileSize"))
                .sum();
    }

    @Override
    @Transactional
    public void setCurrentVersion(Long documentId, Long versionId) {
        // 验证文档和版本是否存在
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("文档不存在: " + documentId));

        DocumentVersion version = documentVersionRepository.findById(versionId)
                .orElseThrow(() -> new RuntimeException("版本不存在: " + versionId));

        // 验证版本是否属于该文档
        if (!getFieldValue(version, "documentId").equals(documentId)) {
            throw new RuntimeException("版本不属于该文档");
        }

        // 更新文档的当前版本
        setFieldValue(document, "currentVersionId", versionId);
        setFieldValue(document, "currentVersionNumber", getFieldValue(version, "versionNumber"));
        setFieldValue(document, "updatedAt", LocalDateTime.now());
        documentRepository.save(document);
    }

    /**
     * 审批文档版本
     */
    @Override
    @Transactional
    public DocumentVersionDTO approveVersion(Long versionId, String approver, boolean isApproved, String comments) {
        DocumentVersion version = documentVersionRepository.findById(versionId)
                .orElseThrow(() -> new RuntimeException("版本不存在: " + versionId));

        setFieldValue(version, "approver", approver);
        setFieldValue(version, "approvalDate", LocalDateTime.now());
        setFieldValue(version, "comments", comments);

        // 保存版本
        version = documentVersionRepository.save(version);

        // 转换为DTO并返回
        return modelMapper.map(version, DocumentVersionDTO.class);
    }

    /**
     * 生成预签名URL用于文件访问
     * 注意：此方法目前不是接口定义的一部分
     */
    public String generatePresignedUrl(Long versionId) {
        // 查找指定的版本
        DocumentVersion version = documentVersionRepository.findById(versionId)
                .orElseThrow(() -> new RuntimeException("版本不存在: " + versionId));

        try {
            // 生成预签名URL，使用默认桶和60秒过期时间
            return fileStorageService.generatePresignedUrl("", (String) getFieldValue(version, "filePath"), 60);
        } catch (Exception e) {
            throw new RuntimeException("生成预签名URL失败: " + e.getMessage(), e);
        }
    }

    @Override
    public String generateNewVersionNumber(Long documentId, String versionType) {
        // 获取最新版本号
        Optional<String> latestVersionOpt = documentVersionRepository.findLatestVersionNumberByDocumentId(documentId);

        if (latestVersionOpt.isPresent()) {
            // 解析版本号并递增
            String latestVersion = latestVersionOpt.get();
            try {
                // 假设版本号格式为 "1.0.0"
                String[] parts = latestVersion.split("\\.");
                if (parts.length == 3) {
                    int major = Integer.parseInt(parts[0]);
                    int minor = Integer.parseInt(parts[1]);
                    int patch = Integer.parseInt(parts[2]);

                    // 根据版本类型递增不同部分
                    if ("MAJOR".equals(versionType)) {
                        major += 1;
                        minor = 0;
                        patch = 0;
                    } else if ("MINOR".equals(versionType)) {
                        minor += 1;
                        patch = 0;
                    } else {
                        patch += 1;
                    }

                    return major + "." + minor + "." + patch;
                }
            } catch (NumberFormatException e) {
                // 版本号格式不正确，重新开始
            }
        }

        // 如果没有版本或格式不正确，从 1.0.0 开始
        return "1.0.0";
    }

    @Override
    public boolean checkFileExists(String checksum) {
        return documentVersionRepository.findByChecksum(checksum).isPresent();
    }

    @Override
    public long getVersionCount(Long documentId) {
        return documentVersionRepository.countByDocumentId(documentId);
    }

    @Override
    public String compareVersions(Long versionId1, Long versionId2) throws Exception {
        DocumentVersion version1 = documentVersionRepository.findById(versionId1)
                .orElseThrow(() -> new RuntimeException("版本1不存在: " + versionId1));
        DocumentVersion version2 = documentVersionRepository.findById(versionId2)
                .orElseThrow(() -> new RuntimeException("版本2不存在: " + versionId2));

        // 简单的版本比较逻辑
        if (!getFieldValue(version1, "fileSize").equals(getFieldValue(version2, "fileSize"))) {
            return "文件大小不同: " + getFieldValue(version1, "fileSize") + " vs " + getFieldValue(version2, "fileSize");
        }
        if (!getFieldValue(version1, "checksum").equals(getFieldValue(version2, "checksum"))) {
            return "文件内容不同（校验和不匹配）";
        }

        return "两个版本完全相同";
    }

    @Override
    @Transactional
    public DocumentVersionDTO rollbackToVersion(Long versionId) throws Exception {
        // 查找要回滚到的版本
        DocumentVersion version = documentVersionRepository.findById(versionId)
                .orElseThrow(() -> new RuntimeException("版本不存在: " + versionId));

        // 下载该版本的文件内容
        byte[] fileContent;
        try (InputStream inputStream = fileStorageService.downloadFile("edms-documents",
                (String) getFieldValue(version, "filePath"))) {
            fileContent = IOUtils.toByteArray(inputStream);
        }

        // 生成新的版本号
        String newVersionNumber = generateNewVersionNumber((Long) getFieldValue(version, "documentId"), "MINOR");

        // 创建临时文件进行上传
        String fileName = (String) getFieldValue(version, "fileName");
        Path tempFile = Files.createTempFile("reverted_", "." + getFileExtension(fileName));
        try {
            Files.write(tempFile, fileContent);

            // 创建MultipartFile对象
            MultipartFile multipartFile = new MockMultipartFile(
                    fileName,
                    fileName,
                    "application/pdf", // 使用默认类型
                    Files.newInputStream(tempFile));

            // 上传新版本 - 使用正确的参数顺序
            return uploadDocumentVersion(
                    (Long) getFieldValue(version, "documentId"),
                    multipartFile,
                    "MINOR",
                    "系统回滚操作",
                    "回滚到版本 " + getFieldValue(version, "versionNumber"),
                    (String) getFieldValue(version, "createdBy"));
        } finally {
            // 删除临时文件
            Files.deleteIfExists(tempFile);
        }
    }

    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return ""; // 或者返回一个默认扩展名
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }

    /**
     * 计算文件哈希值
     */
    protected String calculateChecksum(MultipartFile file) throws IOException {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            try (InputStream inputStream = file.getInputStream()) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    md.update(buffer, 0, bytesRead);
                }
            }

            byte[] hashBytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IOException("SHA-256算法不可用", e);
        }
    }

    /**
     * 格式化文件大小
     */
    protected String formatFileSize(long size) {
        if (size <= 0)
            return "0 B";

        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));

        return String.format("%.2f %s", size / Math.pow(1024, digitGroups), units[digitGroups]);
    }

    /**
     * 模拟MultipartFile类，用于创建临时文件上传
     */

    private static class MockMultipartFile implements MultipartFile {
        private final String name;
        private final String originalFilename;
        private final String contentType;
        private final byte[] content;
        private final java.io.InputStream inputStream;

        public MockMultipartFile(String name, String originalFilename, String contentType,
                java.io.InputStream inputStream) throws IOException {
            this.name = name;
            this.originalFilename = originalFilename;
            this.contentType = contentType;
            this.inputStream = inputStream;
            this.content = inputStream.readAllBytes();
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getOriginalFilename() {
            return originalFilename;
        }

        @Override
        public String getContentType() {
            return contentType;
        }

        @Override
        public boolean isEmpty() {
            return content.length == 0;
        }

        @Override
        public long getSize() {
            return content.length;
        }

        @Override
        public byte[] getBytes() throws IOException {
            return content;
        }

        @Override
        public java.io.InputStream getInputStream() throws IOException {
            return new java.io.ByteArrayInputStream(content);
        }

        @Override
        public void transferTo(java.io.File dest) throws IOException, IllegalStateException {
            Files.write(dest.toPath(), content);
        }
    }

    /**
     * 使用反射获取对象的字段值
     */
    private Object getFieldValue(Object obj, String fieldName) {
        if (obj == null || fieldName == null) {
            return null;
        }

        try {
            java.lang.reflect.Field field = findField(obj.getClass(), fieldName);
            if (field != null) {
                field.setAccessible(true);
                return field.get(obj);
            }
            // 尝试通过getter方法获取
            String getterName = "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
            try {
                java.lang.reflect.Method getter = obj.getClass().getMethod(getterName);
                return getter.invoke(obj);
            } catch (NoSuchMethodException e) {
                // 如果getter不存在，返回null
                return null;
            }
        } catch (Exception e) {
            // 反射访问失败，返回null
            return null;
        }
    }

    /**
     * 使用反射设置对象的字段值
     */
    private void setFieldValue(Object obj, String fieldName, Object value) {
        if (obj == null || fieldName == null) {
            return;
        }

        try {
            java.lang.reflect.Field field = findField(obj.getClass(), fieldName);
            if (field != null) {
                field.setAccessible(true);
                field.set(obj, value);
            } else {
                // 尝试通过setter方法设置
                String setterName = "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
                try {
                    Class<?> fieldType = value != null ? value.getClass() : Object.class;
                    java.lang.reflect.Method setter = obj.getClass().getMethod(setterName, fieldType);
                    setter.invoke(obj, value);
                } catch (NoSuchMethodException e) {
                    // 如果setter不存在，尝试查找匹配参数类型的setter
                    java.lang.reflect.Method[] methods = obj.getClass().getMethods();
                    for (java.lang.reflect.Method method : methods) {
                        if (method.getName().equals(setterName) && method.getParameterCount() == 1) {
                            method.invoke(obj, value);
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            // 反射设置失败，静默忽略
        }
    }

    /**
     * 查找字段，包括父类中的字段
     */
    private java.lang.reflect.Field findField(Class<?> clazz, String fieldName) {
        Class<?> currentClass = clazz;
        while (currentClass != null && currentClass != Object.class) {
            try {
                return currentClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                currentClass = currentClass.getSuperclass();
            }
        }
        return null;
    }

    public Map<String, Long> calculateUsageStatistics() {
        // 统计文档使用情况
        Map<String, Long> stats = new HashMap<>();
        stats.put("totalDocuments", documentVersionRepository.count());
        List<DocumentVersion> allVersions = documentVersionRepository.findAll();
        Long totalSize = sumFileSize(allVersions);
        stats.put("totalSize", totalSize);
        return stats;
    }

    @Override
    @Transactional
    public void restoreDocumentVersion(Long documentId, Long versionId) throws Exception {
        // 查找文档
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("文档不存在: " + documentId));

        // 查找要恢复到的版本
        DocumentVersion targetVersion = documentVersionRepository.findById(versionId)
                .orElseThrow(() -> new RuntimeException("版本不存在: " + versionId));

        // 验证版本属于该文档
        if (!targetVersion.getDocumentId().equals(documentId)) {
            throw new RuntimeException("版本不属于指定文档");
        }

        // 更新文档的当前版本信息
        setFieldValue(document, "currentVersion", getFieldValue(targetVersion, "versionNumber"));
        setFieldValue(document, "currentVersionId", versionId);
        setFieldValue(document, "updatedAt", LocalDateTime.now());

        documentRepository.save(document);
    }

    @Override
    public String compareDocumentVersions(Long documentId, Long fromVersionId, Long toVersionId) throws Exception {
        // 验证文档存在
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("文档不存在: " + documentId));

        // 查找两个版本
        DocumentVersion fromVersion = documentVersionRepository.findById(fromVersionId)
                .orElseThrow(() -> new RuntimeException("源版本不存在: " + fromVersionId));
        DocumentVersion toVersion = documentVersionRepository.findById(toVersionId)
                .orElseThrow(() -> new RuntimeException("目标版本不存在: " + toVersionId));

        // 验证版本都属于该文档
        if (!fromVersion.getDocumentId().equals(documentId) || !toVersion.getDocumentId().equals(documentId)) {
            throw new RuntimeException("版本不属于指定文档");
        }

        // 构建比较结果
        StringBuilder comparison = new StringBuilder();
        comparison.append("=== 文档版本比较报告 ===\n");
        comparison.append("文档ID: ").append(documentId).append("\n");
        comparison.append("文档标题: ").append(getFieldValue(document, "title")).append("\n\n");

        comparison.append("源版本信息:\n");
        comparison.append("  版本号: ").append(getFieldValue(fromVersion, "versionNumber")).append("\n");
        comparison.append("  文件名: ").append(getFieldValue(fromVersion, "fileName")).append("\n");
        comparison.append("  文件大小: ").append(formatFileSize((Long) getFieldValue(fromVersion, "fileSize")))
                .append("\n");
        comparison.append("  创建时间: ").append(getFieldValue(fromVersion, "createdTime")).append("\n");
        comparison.append("  创建者: ").append(getFieldValue(fromVersion, "createdBy")).append("\n");
        comparison.append("  变更原因: ").append(getFieldValue(fromVersion, "changeReason")).append("\n\n");

        comparison.append("目标版本信息:\n");
        comparison.append("  版本号: ").append(getFieldValue(toVersion, "versionNumber")).append("\n");
        comparison.append("  文件名: ").append(getFieldValue(toVersion, "fileName")).append("\n");
        comparison.append("  文件大小: ").append(formatFileSize((Long) getFieldValue(toVersion, "fileSize"))).append("\n");
        comparison.append("  创建时间: ").append(getFieldValue(toVersion, "createdTime")).append("\n");
        comparison.append("  创建者: ").append(getFieldValue(toVersion, "createdBy")).append("\n");
        comparison.append("  变更原因: ").append(getFieldValue(toVersion, "changeReason")).append("\n\n");

        // 比较文件内容
        if (((Long) getFieldValue(fromVersion, "fileSize")).equals(getFieldValue(toVersion, "fileSize")) &&
                ((String) getFieldValue(fromVersion, "checksum")).equals(getFieldValue(toVersion, "checksum"))) {
            comparison.append("文件内容: 完全相同\n");
        } else {
            comparison.append("文件内容: 存在差异\n");
            comparison.append("  文件大小差异: ")
                    .append((Long) getFieldValue(toVersion, "fileSize") - (Long) getFieldValue(fromVersion, "fileSize"))
                    .append(" 字节\n");
            if (!((String) getFieldValue(fromVersion, "checksum")).equals(getFieldValue(toVersion, "checksum"))) {
                comparison.append("  文件校验和不同，内容已修改\n");
            }
        }

        return comparison.toString();
    }

}
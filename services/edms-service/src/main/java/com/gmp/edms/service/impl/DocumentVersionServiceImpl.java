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

/**
 * 文档版本服务实现
 */
@Service
public class DocumentVersionServiceImpl implements DocumentVersionService {

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
        documentVersion.setDocumentId(documentId);
        documentVersion.setVersionNumber(newVersionNumber);
        documentVersion.setFileName(fileName);
        documentVersion.setFileType(file.getContentType());
        documentVersion.setFileSize(file.getSize());
        documentVersion.setFilePath(filePath);
        documentVersion.setChecksum(checksum);
        documentVersion.setChangeReason(changeReason);
        documentVersion.setCreatedBy(author);
        documentVersion.setCreatedTime(LocalDateTime.now());
        documentVersion.setIsCurrent(true);

        // 保存文档版本
        DocumentVersion savedVersion = documentVersionRepository.save(documentVersion);

        // 上传文件到存储服务
        fileStorageService.uploadFile(file, "edms-documents", "versions/" + documentId + "/" + newVersionNumber);

        // 更新其他版本为非当前版本
        List<DocumentVersion> otherVersions = documentVersionRepository
                .findByDocumentIdOrderByVersionNumberDesc(documentId);
        for (DocumentVersion version : otherVersions) {
            if (!version.getId().equals(savedVersion.getId())) {
                version.setIsCurrent(false);
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
                    dto.setFormattedFileSize(formatFileSize(version.getFileSize()));
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
        dto.setFormattedFileSize(formatFileSize(version.getFileSize()));

        return dto;
    }

    @Override
    public DocumentVersionDTO getCurrentDocumentVersion(Long documentId) {
        // 查找文档的当前版本
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("文档不存在: " + documentId));

        if (document.getCurrentVersionId() == null) {
            throw new RuntimeException("文档还没有版本");
        }

        DocumentVersion version = documentVersionRepository.findById(document.getCurrentVersionId())
                .orElseThrow(() -> new RuntimeException("当前版本不存在"));

        // 转换为DTO并返回
        DocumentVersionDTO dto = modelMapper.map(version, DocumentVersionDTO.class);
        dto.setFormattedFileSize(formatFileSize(version.getFileSize()));

        return dto;
    }

    @Override
    public DocumentVersionDTO getVersionById(Long versionId) {
        // 查找指定的版本
        DocumentVersion version = documentVersionRepository.findById(versionId)
                .orElseThrow(() -> new RuntimeException("版本不存在: " + versionId));

        // 转换为DTO并返回
        DocumentVersionDTO dto = modelMapper.map(version, DocumentVersionDTO.class);
        dto.setFormattedFileSize(formatFileSize(version.getFileSize()));

        return dto;
    }

    @Override
    public byte[] downloadDocumentVersion(Long versionId) throws Exception {
        // 查找指定的版本
        DocumentVersion version = documentVersionRepository.findById(versionId)
                .orElseThrow(() -> new RuntimeException("版本不存在: " + versionId));

        // 下载文件
        try (InputStream inputStream = fileStorageService.downloadFile("edms-documents", version.getFilePath())) {
            return IOUtils.toByteArray(inputStream);
        }
    }

    @Override
    @Transactional
    public void deleteDocumentVersion(Long versionId) throws IOException {
        // 查找指定的版本
        DocumentVersion version = documentVersionRepository.findById(versionId)
                .orElseThrow(() -> new RuntimeException("版本不存在: " + versionId));

        // 获取文档信息
        Document document = documentRepository.findById(version.getDocumentId())
                .orElseThrow(() -> new RuntimeException("文档不存在"));

        // 检查是否是当前版本
        if (document.getCurrentVersionId() != null && document.getCurrentVersionId().equals(versionId)) {
            // 如果是当前版本，查找上一个版本
            Optional<DocumentVersion> previousVersionOpt = documentVersionRepository
                    .findFirstByDocumentIdAndIdNotOrderByCreatedAtDesc(
                            version.getDocumentId(), versionId);

            if (previousVersionOpt.isPresent()) {
                // 更新当前版本为上一个版本
                DocumentVersion previousVersion = previousVersionOpt.get();
                document.setCurrentVersionId(previousVersion.getId());
                document.setCurrentVersionNumber(previousVersion.getVersionNumber());
            } else {
                // 如果没有其他版本，清空当前版本信息
                document.setCurrentVersionId(null);
                document.setCurrentVersionNumber(null);
            }

            documentRepository.save(document);
        }

        // 删除文件
        try {
            fileStorageService.deleteFile("", version.getFilePath());
        } catch (Exception e) {
            throw new RuntimeException("删除文件失败: " + e.getMessage(), e);
        }

        // 删除版本记录
        documentVersionRepository.delete(version);
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
        if (!version.getDocumentId().equals(documentId)) {
            throw new RuntimeException("版本不属于该文档");
        }

        // 更新文档的当前版本
        document.setCurrentVersionId(versionId);
        document.setCurrentVersionNumber(version.getVersionNumber());
        document.setUpdatedAt(LocalDateTime.now());
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

        version.setApprover(approver);
        version.setApprovalDate(LocalDateTime.now());
        version.setComments(comments);

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
            return fileStorageService.generatePresignedUrl("", version.getFilePath(), 60);
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
        if (version1.getFileSize() != version2.getFileSize()) {
            return "文件大小不同: " + version1.getFileSize() + " vs " + version2.getFileSize();
        }
        if (!version1.getChecksum().equals(version2.getChecksum())) {
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
        try (InputStream inputStream = fileStorageService.downloadFile("edms-documents", version.getFilePath())) {
            fileContent = IOUtils.toByteArray(inputStream);
        }

        // 生成新的版本号
        String newVersionNumber = generateNewVersionNumber(version.getDocumentId(), "MINOR");

        // 创建临时文件进行上传
        String fileName = version.getFileName();
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
                    version.getDocumentId(),
                    multipartFile,
                    "MINOR",
                    "系统回滚操作",
                    "回滚到版本 " + version.getVersionNumber(),
                    version.getCreatedBy());
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
}

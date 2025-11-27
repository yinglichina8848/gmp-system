package com.gmp.edms.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmp.edms.dto.CommonFileDTO;
import com.gmp.edms.dto.CommonFileUploadDTO;
import com.gmp.edms.entity.CommonFile;
import com.gmp.edms.repository.CommonFileRepository;
import com.gmp.edms.service.CommonFileService;
import com.gmp.edms.service.FileStorageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 通用文件管理服务实现
 */
@Service
public class CommonFileServiceImpl implements CommonFileService {
    
    @Autowired
    private CommonFileRepository commonFileRepository;
    
    @Autowired
    private FileStorageService fileStorageService;
    
    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Value("${minio.default-bucket}")
    private String defaultBucket;
    
    @Value("${minio.common-files-bucket:common-files}")
    private String commonFilesBucket;
    
    @Override
    @Transactional
    public CommonFileDTO uploadFile(MultipartFile file, String module, Map<String, Object> metadata) throws Exception {
        // 验证文件
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("文件不能为空");
        }
        
        // 验证模块名称
        if (module == null || module.trim().isEmpty()) {
            throw new IllegalArgumentException("模块名称不能为空");
        }
        
        // 计算文件校验和
        String checksum = calculateChecksum(file);
        
        // 检查文件是否已存在
        Optional<CommonFile> existingFile = commonFileRepository.findByChecksum(checksum);
        if (existingFile.isPresent()) {
            // 文件已存在，可以选择返回已存在的文件信息或抛出异常
            return convertToDTO(existingFile.get());
        }
        
        // 生成文件路径
        String fileName = file.getOriginalFilename();
        String filePath = generateFilePath(module, fileName);
        
        // 创建文件实体
        CommonFile commonFile = new CommonFile();
        commonFile.setFileName(fileName);
        commonFile.setFileType(file.getContentType());
        commonFile.setFileSize(file.getSize());
        commonFile.setFilePath(filePath);
        commonFile.setChecksum(checksum);
        commonFile.setBucketName(commonFilesBucket);
        commonFile.setModule(module);
        commonFile.setCreatedBy("system"); // 实际应用中应该从认证信息获取
        
        // 设置元数据
        if (metadata != null && !metadata.isEmpty()) {
            commonFile.setMetadata(objectMapper.valueToTree(metadata));
        }
        
        // 保存文件信息到数据库
        commonFile = commonFileRepository.save(commonFile);
        
        try {
            // 上传文件到存储服务
            fileStorageService.uploadFile(file, commonFilesBucket, filePath);
            
            // 转换为DTO并返回
            return convertToDTO(commonFile);
        } catch (Exception e) {
            // 如果文件上传失败，删除数据库记录
            commonFileRepository.delete(commonFile);
            throw e;
        }
    }
    
    @Override
    @Transactional
    public List<CommonFileDTO> batchUploadFiles(List<MultipartFile> files, String module, Map<String, Object> metadata) throws Exception {
        List<CommonFileDTO> result = new ArrayList<>();
        
        if (files == null || files.isEmpty()) {
            return result;
        }
        
        for (MultipartFile file : files) {
            try {
                CommonFileDTO fileDTO = uploadFile(file, module, metadata);
                result.add(fileDTO);
            } catch (Exception e) {
                // 记录错误但继续处理其他文件
                System.err.println("文件上传失败: " + file.getOriginalFilename() + ", 错误: " + e.getMessage());
            }
        }
        
        return result;
    }
    
    @Override
    public InputStream downloadFile(Long fileId) throws Exception {
        CommonFile file = getFileById(fileId);
        return fileStorageService.downloadFile(file.getBucketName(), file.getFilePath());
    }
    
    @Override
    public CommonFileDTO getFileInfo(Long fileId) throws Exception {
        CommonFile file = getFileById(fileId);
        return convertToDTO(file);
    }
    
    @Override
    @Transactional
    public void deleteFile(Long fileId) throws Exception {
        CommonFile file = getFileById(fileId);
        
        // 先删除存储中的文件
        fileStorageService.deleteFile(file.getBucketName(), file.getFilePath());
        
        // 再删除数据库记录
        commonFileRepository.delete(file);
    }
    
    @Override
    @Transactional
    public void batchDeleteFiles(List<Long> fileIds) throws Exception {
        if (fileIds == null || fileIds.isEmpty()) {
            return;
        }
        
        List<CommonFile> files = commonFileRepository.findByIdIn(fileIds);
        
        for (CommonFile file : files) {
            try {
                // 删除存储中的文件
                fileStorageService.deleteFile(file.getBucketName(), file.getFilePath());
                
                // 删除数据库记录
                commonFileRepository.delete(file);
            } catch (Exception e) {
                // 记录错误但继续处理其他文件
                System.err.println("文件删除失败: " + file.getId() + ", 错误: " + e.getMessage());
            }
        }
    }
    
    @Override
    public String generatePresignedUrl(Long fileId, int expiration) throws Exception {
        CommonFile file = getFileById(fileId);
        return fileStorageService.generatePresignedUrl(file.getBucketName(), file.getFilePath(), expiration);
    }
    
    @Override
    public Map<String, Object> queryFiles(String module, Map<String, Object> filters, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<CommonFile> filePage;
        
        // 根据过滤条件构建查询
        if (filters != null && filters.containsKey("keyword")) {
            String keyword = (String) filters.get("keyword");
            filePage = commonFileRepository.searchByFileName(keyword, pageable);
        } else if (module != null && !module.isEmpty()) {
            if (filters != null && filters.containsKey("status")) {
                String status = (String) filters.get("status");
                filePage = commonFileRepository.findByModuleAndStatus(module, status, pageable);
            } else {
                filePage = commonFileRepository.findByModule(module, pageable);
            }
        } else {
            filePage = commonFileRepository.findByStatusOrderByCreatedAtDesc("ACTIVE", pageable);
        }
        
        List<CommonFileDTO> fileDTOs = filePage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        Map<String, Object> result = new HashMap<>();
        result.put("content", fileDTOs);
        result.put("totalElements", filePage.getTotalElements());
        result.put("totalPages", filePage.getTotalPages());
        result.put("pageNumber", filePage.getNumber() + 1);
        result.put("pageSize", filePage.getSize());
        
        return result;
    }
    
    @Override
    @Transactional
    public void updateFileMetadata(Long fileId, Map<String, Object> metadata) throws Exception {
        CommonFile file = getFileById(fileId);
        
        if (metadata != null && !metadata.isEmpty()) {
            file.setMetadata(objectMapper.valueToTree(metadata));
            commonFileRepository.save(file);
        }
    }
    
    @Override
    public Map<String, Object> getFileStatistics(String module) {
        Map<String, Object> statistics = new HashMap<>();
        
        // 获取文件数量统计
        List<Object[]> countStats = commonFileRepository.countByModule();
        Map<String, Long> moduleCountMap = new HashMap<>();
        for (Object[] stat : countStats) {
            moduleCountMap.put((String) stat[0], (Long) stat[1]);
        }
        statistics.put("countByModule", moduleCountMap);
        
        // 获取文件大小统计
        List<Object[]> sizeStats = commonFileRepository.sumFileSizeByModule();
        Map<String, Long> moduleSizeMap = new HashMap<>();
        for (Object[] stat : sizeStats) {
            moduleSizeMap.put((String) stat[0], (Long) stat[1]);
        }
        statistics.put("sizeByModule", moduleSizeMap);
        
        // 如果指定了模块，获取该模块的详细统计
        if (module != null && !module.isEmpty()) {
            long fileCount = commonFileRepository.countByModuleAndStatus(module, "ACTIVE");
            statistics.put("currentModuleCount", fileCount);
        }
        
        return statistics;
    }
    
    /**
     * 获取文件实体
     */
    private CommonFile getFileById(Long fileId) throws Exception {
        return commonFileRepository.findById(fileId)
                .orElseThrow(() -> new Exception("文件不存在: " + fileId));
    }
    
    /**
     * 计算文件校验和
     */
    private String calculateChecksum(MultipartFile file) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        byte[] bytes = file.getBytes();
        digest.update(bytes);
        byte[] hash = digest.digest();
        
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }
    
    /**
     * 生成文件路径
     */
    private String generateFilePath(String module, String originalFileName) {
        // 生成基于日期的目录结构
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        // 生成唯一文件名
        String uniqueFileName = UUID.randomUUID().toString() + "-" + originalFileName;
        return module + "/" + datePath + "/" + uniqueFileName;
    }
    
    /**
     * 转换为DTO
     */
    private CommonFileDTO convertToDTO(CommonFile file) {
        CommonFileDTO dto = modelMapper.map(file, CommonFileDTO.class);
        
        // 格式化文件大小
        dto.setFormattedFileSize(formatFileSize(file.getFileSize()));
        
        // 转换元数据
        JsonNode metadata = file.getMetadata();
        if (metadata != null) {
            dto.setMetadata(objectMapper.convertValue(metadata, Map.class));
        }
        
        return dto;
    }
    
    /**
     * 格式化文件大小
     */
    private String formatFileSize(Long size) {
        if (size == null) return "0 B";
        
        if (size < 1024) return size + " B";
        else if (size < 1024 * 1024) return String.format("%.2f KB", size / 1024.0);
        else if (size < 1024 * 1024 * 1024) return String.format("%.2f MB", size / (1024.0 * 1024));
        else return String.format("%.2f GB", size / (1024.0 * 1024 * 1024));
    }
}
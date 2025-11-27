package com.gmp.edms.service.impl;

import com.gmp.edms.dto.DocumentCreateDTO;
import com.gmp.edms.dto.DocumentDTO;
import com.gmp.edms.dto.DocumentUpdateDTO;
import com.gmp.edms.dto.PageRequestDTO;
import com.gmp.edms.dto.PageResponseDTO;
import com.gmp.edms.entity.Document;
import com.gmp.edms.entity.DocumentCategory;
import com.gmp.edms.repository.DocumentRepository;
import com.gmp.edms.service.DocumentCategoryService;
import com.gmp.edms.service.DocumentService;
import com.gmp.edms.service.FileStorageService;
import org.apache.commons.io.IOUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 文档服务实现
 */
@Service
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private DocumentCategoryService documentCategoryService;
    
    @Autowired
    private FileStorageService fileStorageService;

    @Override
    @Transactional
    public DocumentDTO createDocument(DocumentCreateDTO documentCreateDTO) {
        // 生成文档编号
        String docCode = generateDocCode();

        // 创建文档实体
        Document document = modelMapper.map(documentCreateDTO, Document.class);
        document.setDocumentNumber(docCode);
        document.setStatus("DRAFT");
        document.setCreatedAt(LocalDateTime.now());
        document.setUpdatedAt(LocalDateTime.now());

        // 保存文档
        document = documentRepository.save(document);

        // 转换为DTO并返回
        return modelMapper.map(document, DocumentDTO.class);
    }
    
    @Override
    @Transactional
    public DocumentDTO createDocumentWithFile(DocumentCreateDTO documentCreateDTO, MultipartFile file) throws Exception {
        // 创建文档
        DocumentDTO documentDTO = createDocument(documentCreateDTO);
        
        // 上传文件
        return uploadDocumentFile(documentDTO.getId(), file);
    }
    
    @Override
    @Transactional
    public DocumentDTO uploadDocumentFile(Long documentId, MultipartFile file) throws Exception {
        // 检查文件是否存在
        if (file == null || file.isEmpty()) {
            throw new Exception("文件不能为空");
        }
        
        // 查找文档
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("文档不存在: " + documentId));
        
        // 计算文件校验和
        String checksum = calculateChecksum(file);
        
        // 创建文件保存路径
        String fileName = file.getOriginalFilename();
        String filePath = "documents/" + documentId + "/" + fileName;
        
        // 上传文件到存储服务
        fileStorageService.uploadFile(file, "edms-documents", "documents/" + documentId);
        
        // 更新文档信息
        document.setFilePath(filePath);
        document.setFileSize(file.getSize());
        document.setContentType(file.getContentType());
        document.setChecksum(checksum);
        document.setUpdatedAt(LocalDateTime.now());
        
        // 保存文档
        document = documentRepository.save(document);
        
        // 转换为DTO并返回
        return modelMapper.map(document, DocumentDTO.class);
    }
    
    @Override
    public byte[] downloadDocumentFile(Long documentId) throws Exception {
        // 查找文档
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("文档不存在: " + documentId));
        
        // 检查文档是否有文件
        if (document.getFilePath() == null || document.getFilePath().isEmpty()) {
            throw new Exception("文档没有关联文件");
        }
        
        // 下载文件
        try (InputStream inputStream = fileStorageService.downloadFile("edms-documents", document.getFilePath())) {
            return IOUtils.toByteArray(inputStream);
        }
    }
    
    /**
     * 计算文件校验和
     */
    private String calculateChecksum(MultipartFile file) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        try (InputStream inputStream = file.getInputStream()) {
            byte[] buffer = new byte[8192];
            int read;
            while ((read = inputStream.read(buffer)) > 0) {
                md.update(buffer, 0, read);
            }
        }
        
        byte[] hashBytes = md.digest();
        StringBuilder hexString = new StringBuilder();
        for (byte hashByte : hashBytes) {
            String hex = Integer.toHexString(0xff & hashByte);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    @Override
    @Transactional
    public DocumentDTO updateDocument(Long id, DocumentUpdateDTO documentUpdateDTO) {
        // 查找文档
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("文档不存在: " + id));

        // 更新文档信息
        if (documentUpdateDTO.getTitle() != null) {
            document.setTitle(documentUpdateDTO.getTitle());
        }
        if (documentUpdateDTO.getDocumentType() != null) {
            document.setDocumentType(documentUpdateDTO.getDocumentType());
        }
        if (documentUpdateDTO.getCategoryId() != null) {
            // 直接设置categoryId，不使用documentCategoryService.findById
            document.setCategoryId(documentUpdateDTO.getCategoryId());
        }
        if (documentUpdateDTO.getTemplateId() != null) {
            document.setTemplateId(documentUpdateDTO.getTemplateId());
        }
        if (documentUpdateDTO.getOwnerDepartment() != null) {
            document.setOwnerDepartment(documentUpdateDTO.getOwnerDepartment());
        }
        document.setUpdatedAt(LocalDateTime.now());

        // 保存更新
        document = documentRepository.save(document);

        // 转换为DTO并返回
        return modelMapper.map(document, DocumentDTO.class);
    }

    @Override
    @Transactional
    public void deleteDocument(Long id) {
        // 检查文档是否存在
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("文档不存在: " + id));

        // 删除文档（可以考虑软删除）
        documentRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void batchDeleteDocuments(List<Long> ids) {
        // 批量删除文档
        documentRepository.deleteAllById(ids);
    }

    @Override
    public DocumentDTO getDocumentById(Long id) {
        // 查找文档
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("文档不存在: " + id));

        // 转换为DTO并返回
        return modelMapper.map(document, DocumentDTO.class);
    }

    @Override
    public DocumentDTO getDocumentByDocCode(String docCode) {
        // 根据文档编号查找文档
        Document document = documentRepository.findByDocCode(docCode)
                .orElseThrow(() -> new RuntimeException("文档不存在: " + docCode));

        // 转换为DTO并返回
        return modelMapper.map(document, DocumentDTO.class);
    }

    @Override
    public PageResponseDTO<DocumentDTO> queryDocuments(PageRequestDTO pageRequest, Map<String, Object> filters) {
        // 构建分页请求
        Sort sort = pageRequest.getSortOrder().equalsIgnoreCase("desc") ? Sort.by(pageRequest.getSortBy()).descending()
                : Sort.by(pageRequest.getSortBy()).ascending();

        Pageable pageable = PageRequest.of(pageRequest.getPageNo() - 1, pageRequest.getPageSize(), sort);

        // 构建查询条件并执行查询
        Page<Document> page;
        if (filters != null && !filters.isEmpty()) {
            // 可以根据需要构建复杂查询
            String status = (String) filters.get("status");
            if (status != null) {
                page = documentRepository.findByStatus(status, pageable);
            } else {
                page = documentRepository.findAll(pageable);
            }
        } else {
            page = documentRepository.findAll(pageable);
        }

        // 转换为DTO并返回
        List<DocumentDTO> documentDTOs = page.getContent().stream()
                .map(document -> modelMapper.map(document, DocumentDTO.class))
                .collect(Collectors.toList());

        return new PageResponseDTO<>(documentDTOs, page.getTotalElements(),
                pageRequest.getPageNo(), pageRequest.getPageSize());
    }

    @Override
    public List<DocumentDTO> getDocumentsByCategory(Long categoryId) {
        // 根据分类ID查询文档
        List<Document> documents = documentRepository.findByCategoryId(categoryId);

        // 转换为DTO并返回
        return documents.stream()
                .map(document -> modelMapper.map(document, DocumentDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateDocumentStatus(Long id, String status) {
        // 查找文档
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("文档不存在: " + id));

        // 更新状态
        document.setStatus(status);
        document.setUpdatedAt(LocalDateTime.now());

        // 保存更新
        documentRepository.save(document);
    }

    @Override
    public List<DocumentDTO> searchDocuments(String keyword) {
        // 根据关键字搜索文档
        List<Document> documents = documentRepository.findByTitleContaining(keyword);

        // 转换为DTO并返回
        return documents.stream()
                .map(document -> modelMapper.map(document, DocumentDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public PageResponseDTO<DocumentDTO> advancedSearch(PageRequestDTO pageRequest, String keyword,
            Long categoryId, String status,
            Long departmentId, String approvalStatus) {
        // 构建分页请求
        Sort sort = pageRequest.getSortOrder().equalsIgnoreCase("desc") ? Sort.by(pageRequest.getSortBy()).descending()
                : Sort.by(pageRequest.getSortBy()).ascending();

        Pageable pageable = PageRequest.of(pageRequest.getPageNo() - 1, pageRequest.getPageSize(), sort);

        // 执行高级搜索
        Page<Document> page = documentRepository.advancedSearch(
                keyword, categoryId, status, departmentId, approvalStatus, pageable);

        // 转换为DTO并返回
        List<DocumentDTO> documentDTOs = page.getContent().stream()
                .map(document -> modelMapper.map(document, DocumentDTO.class))
                .collect(Collectors.toList());

        return new PageResponseDTO<>(documentDTOs, page.getTotalElements(),
                pageRequest.getPageNo(), pageRequest.getPageSize());
    }

    @Override
    public List<DocumentDTO> getUserAccessibleDocuments(String username) {
        // 查询用户可访问的文档
        List<Document> documents = documentRepository.findDocumentsByUserPermission(username);

        // 转换为DTO并返回
        return documents.stream()
                .map(document -> modelMapper.map(document, DocumentDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DocumentDTO approveDocument(Long id, String approvalStatus, String comments) {
        // 查找文档
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("文档不存在: " + id));

        // 更新状态
        if ("APPROVED".equals(approvalStatus)) {
            document.setStatus("APPROVED");
        }
        document.setUpdatedAt(LocalDateTime.now());

        // 保存更新
        document = documentRepository.save(document);

        // 转换为DTO并返回
        return modelMapper.map(document, DocumentDTO.class);
    }

    /**
     * 生成文档编号
     */
    protected String generateDocCode() {
        // 生成格式为 DOC_YYYYMMDD_0001 的文档编号
        String dateStr = LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
        String prefix = "DOC_" + dateStr + "_";

        // 查找当天最后一个文档编号
        List<Document> documents = documentRepository.findAll();
        long maxNum = documents.stream()
                .filter(doc -> doc.getDocumentNumber().startsWith(prefix))
                .mapToLong(doc -> {
                    String numStr = doc.getDocumentNumber().substring(prefix.length());
                    try {
                        return Long.parseLong(numStr);
                    } catch (NumberFormatException e) {
                        return 0;
                    }
                })
                .max()
                .orElse(0);

        // 生成新编号
        return prefix + String.format("%04d", maxNum + 1);
    }
}

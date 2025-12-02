package com.gmp.edms.service.impl;

import com.gmp.edms.dto.DocumentCreateDTO;
import com.gmp.edms.dto.DocumentDTO;
import com.gmp.edms.dto.DocumentUpdateDTO;
import com.gmp.edms.dto.PageRequestDTO;
import com.gmp.edms.dto.PageResponseDTO;
import com.gmp.edms.entity.Document;
import com.gmp.edms.entity.DocumentCategory;
import com.gmp.edms.repository.DocumentRepository;
import com.gmp.edms.event.DocumentEvent;
import com.gmp.edms.event.DocumentEventType;
import com.gmp.edms.event.EventPublisher;
import com.gmp.edms.service.DocumentCategoryService;
import com.gmp.edms.service.DocumentSearchService;
import com.gmp.edms.service.DocumentService;
import com.gmp.edms.service.FileStorageService;
import org.apache.commons.io.IOUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
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

    @Autowired
    private DocumentSearchService documentSearchService;

    @Autowired
    private EventPublisher eventPublisher;

    @Override
    @Transactional
    @CacheEvict(value = "documents", allEntries = true)
    public DocumentDTO createDocument(DocumentCreateDTO documentCreateDTO) {
        // 生成文档编号
        String docCode = generateDocCode();

        // 创建文档实体
        Document document = modelMapper.map(documentCreateDTO, Document.class);
        setFieldValue(document, "documentNumber", docCode);
        setFieldValue(document, "status", "DRAFT");
        setFieldValue(document, "createdAt", LocalDateTime.now());
        setFieldValue(document, "updatedAt", LocalDateTime.now());

        // 保存文档
        document = documentRepository.save(document);

        // 转换为DTO
        DocumentDTO documentDTO = modelMapper.map(document, DocumentDTO.class);

        // 索引到Elasticsearch
        documentSearchService.indexDocument(documentDTO);

        // 发布文档创建事件
        DocumentEvent event = new DocumentEvent();
        event.setEventType(DocumentEventType.DOCUMENT_CREATED);
        event.setDocumentId(documentDTO.getId());
        event.setDocumentName(documentDTO.getDocumentName());
        event.setOperator("system"); // 实际应从上下文获取当前用户
        event.setEventTime(LocalDateTime.now());
        event.setDescription("文档创建成功");
        event.setData(documentDTO);
        eventPublisher.publishDocumentEvent(event);

        // 返回DTO
        return documentDTO;
    }

    @Override
    @Transactional
    public DocumentDTO createDocumentWithFile(DocumentCreateDTO documentCreateDTO, MultipartFile file)
            throws Exception {
        // 创建文档
        DocumentDTO documentDTO = createDocument(documentCreateDTO);

        // 上传文件
        return uploadDocumentFile((Long) getFieldValue(documentDTO, "id"), file);
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
        setFieldValue(document, "filePath", filePath);
        setFieldValue(document, "fileSize", file.getSize());
        setFieldValue(document, "contentType", file.getContentType());
        setFieldValue(document, "checksum", checksum);
        setFieldValue(document, "updatedAt", LocalDateTime.now());

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
        String filePath = (String) getFieldValue(document, "filePath");
        if (filePath == null || filePath.isEmpty()) {
            throw new Exception("文档没有关联文件");
        }

        // 下载文件
        try (InputStream inputStream = fileStorageService.downloadFile("edms-documents", filePath)) {
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
    @CacheEvict(value = "documents", key = "#id")
    public DocumentDTO updateDocument(Long id, DocumentUpdateDTO documentUpdateDTO) {
        // 查找文档
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("文档不存在: " + id));

        // 更新文档信息
        Object title = getFieldValue(documentUpdateDTO, "title");
        if (title != null) {
            setFieldValue(document, "title", title);
        }
        Object documentType = getFieldValue(documentUpdateDTO, "documentType");
        if (documentType != null) {
            setFieldValue(document, "documentType", documentType);
        }
        Object categoryId = getFieldValue(documentUpdateDTO, "categoryId");
        if (categoryId != null) {
            // 直接设置categoryId
            setFieldValue(document, "categoryId", categoryId);
        }
        Object templateId = getFieldValue(documentUpdateDTO, "templateId");
        if (templateId != null) {
            setFieldValue(document, "templateId", templateId);
        }
        Object ownerDepartment = getFieldValue(documentUpdateDTO, "ownerDepartment");
        if (ownerDepartment != null) {
            setFieldValue(document, "ownerDepartment", ownerDepartment);
        }
        setFieldValue(document, "updatedAt", LocalDateTime.now());

        // 保存更新
        document = documentRepository.save(document);

        // 转换为DTO
        DocumentDTO documentDTO = modelMapper.map(document, DocumentDTO.class);

        // 更新Elasticsearch索引
        documentSearchService.updateDocumentIndex(documentDTO);

        // 发布文档更新事件
        DocumentEvent event = new DocumentEvent();
        event.setEventType(DocumentEventType.DOCUMENT_UPDATED);
        event.setDocumentId(documentDTO.getId());
        event.setDocumentName(documentDTO.getDocumentName());
        event.setOperator("system"); // 实际应从上下文获取当前用户
        event.setEventTime(LocalDateTime.now());
        event.setDescription("文档更新成功");
        event.setData(documentDTO);
        eventPublisher.publishDocumentEvent(event);

        // 返回DTO
        return documentDTO;
    }

    @Override
    @Transactional
    @CacheEvict(value = "documents", key = "#id")
    public void deleteDocument(Long id) {
        // 检查文档是否存在
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("文档不存在: " + id));

        // 删除文档（可以考虑软删除）
        documentRepository.deleteById(id);

        // 删除Elasticsearch索引
        documentSearchService.deleteDocumentIndex(id);

        // 发布文档删除事件
        DocumentEvent event = new DocumentEvent();
        event.setEventType(DocumentEventType.DOCUMENT_DELETED);
        event.setDocumentId(id);
        event.setDocumentName((String) getFieldValue(document, "documentName"));
        event.setOperator("system"); // 实际应从上下文获取当前用户
        event.setEventTime(LocalDateTime.now());
        event.setDescription("文档删除成功");
        event.setData(null);
        eventPublisher.publishDocumentEvent(event);
    }

    @Override
    @Transactional
    @CacheEvict(value = "documents", allEntries = true)
    public void batchDeleteDocuments(List<Long> ids) {
        // 批量删除文档
        documentRepository.deleteAllById(ids);

        // 批量删除Elasticsearch索引
        for (Long id : ids) {
            documentSearchService.deleteDocumentIndex(id);
        }
    }

    @Override
    @Cacheable(value = "documents", key = "#id")
    public DocumentDTO getDocumentById(Long id) {
        // 查找文档
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("文档不存在: " + id));

        // 转换为DTO并返回
        return modelMapper.map(document, DocumentDTO.class);
    }

    @Override
    @Cacheable(value = "documents", key = "#docCode")
    public DocumentDTO getDocumentByDocCode(String docCode) {
        // 根据文档编号查找文档
        Document document = documentRepository.findByDocCode(docCode)
                .orElseThrow(() -> new RuntimeException("文档不存在: " + docCode));

        // 转换为DTO并返回
        return modelMapper.map(document, DocumentDTO.class);
    }

    @Override
    public PageResponseDTO<DocumentDTO> queryDocuments(PageRequestDTO pageRequest, Map<String, Object> filters) {
        // 使用反射获取分页参数
        String sortOrder = (String) getFieldValue(pageRequest, "sortOrder", "desc");
        String sortBy = (String) getFieldValue(pageRequest, "sortBy", "id");
        Integer pageNo = (Integer) getFieldValue(pageRequest, "pageNo", 1);
        Integer pageSize = (Integer) getFieldValue(pageRequest, "pageSize", 10);

        // 构建分页请求
        Sort sort = sortOrder.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

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
                .map(doc -> modelMapper.map(doc, DocumentDTO.class))
                .collect(Collectors.toList());

        return new PageResponseDTO<>(documentDTOs, page.getTotalElements(), pageNo, pageSize);
    }

    @Override
    public List<DocumentDTO> getDocumentsByCategory(Long categoryId) {
        // 根据分类ID查询文档
        List<Document> documents = documentRepository.findByCategoryId(categoryId);

        // 转换为DTO并返回
        return documents.stream()
                .map(doc -> modelMapper.map(doc, DocumentDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateDocumentStatus(Long id, String status) {
        // 查找文档
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("文档不存在: " + id));

        // 更新状态
        setFieldValue(document, "status", status);
        setFieldValue(document, "updatedAt", LocalDateTime.now());

        // 保存更新
        documentRepository.save(document);
    }

    @Override
    public List<DocumentDTO> searchDocuments(String keyword, Integer pageNo, Integer pageSize) {
        // 根据关键字搜索文档，支持分页
        List<Document> documents = documentRepository.searchByKeyword(keyword, (pageNo - 1) * pageSize, pageSize);

        // 转换为DTO并返回
        return documents.stream()
                .map(doc -> modelMapper.map(doc, DocumentDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public PageResponseDTO<DocumentDTO> advancedSearch(PageRequestDTO pageRequest, String keyword,
            Long categoryId, String status,
            Long departmentId, String approvalStatus) {
        // 使用反射获取分页参数
        String sortOrder = (String) getFieldValue(pageRequest, "sortOrder", "desc");
        String sortBy = (String) getFieldValue(pageRequest, "sortBy", "id");
        Integer pageNo = (Integer) getFieldValue(pageRequest, "pageNo", 1);
        Integer pageSize = (Integer) getFieldValue(pageRequest, "pageSize", 10);

        // 构建分页请求
        Sort sort = sortOrder.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

        // 执行高级搜索
        Page<Document> page = documentRepository.advancedSearch(
                keyword, categoryId, status, departmentId, approvalStatus, pageable);

        // 转换为DTO并返回
        List<DocumentDTO> documentDTOs = page.getContent().stream()
                .map(doc -> modelMapper.map(doc, DocumentDTO.class))
                .collect(Collectors.toList());

        return new PageResponseDTO<>(documentDTOs, page.getTotalElements(), pageNo, pageSize);
    }

    @Override
    public List<DocumentDTO> getUserAccessibleDocuments(String username) {
        // 查询用户可访问的文档
        List<Document> documents = documentRepository.findDocumentsByUserPermission(username);

        // 转换为DTO并返回
        return documents.stream()
                .map(doc -> modelMapper.map(doc, DocumentDTO.class))
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
            setFieldValue(document, "status", "APPROVED");
        }
        setFieldValue(document, "updatedAt", LocalDateTime.now());

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
                .filter(doc -> {
                    try {
                        String docNum = (String) getFieldValue(doc, "documentNumber");
                        return docNum != null && docNum.startsWith(prefix);
                    } catch (Exception e) {
                        return false;
                    }
                })
                .mapToLong(doc -> {
                    try {
                        String docNum = (String) getFieldValue(doc, "documentNumber");
                        String numStr = docNum.substring(prefix.length());
                        return Long.parseLong(numStr);
                    } catch (Exception e) {
                        return 0;
                    }
                })
                .max()
                .orElse(0);

        // 生成新编号
        return prefix + String.format("%04d", maxNum + 1);
    }

    // 反射工具方法
    private Object getFieldValue(Object obj, String fieldName) {
        return getFieldValue(obj, fieldName, null);
    }

    private Object getFieldValue(Object obj, String fieldName, Object defaultValue) {
        if (obj == null) {
            return defaultValue;
        }

        try {
            Field field = findField(obj.getClass(), fieldName);
            if (field != null) {
                field.setAccessible(true);
                return field.get(obj);
            }
        } catch (Exception e) {
            // 忽略异常，返回默认值
        }
        return defaultValue;
    }

    private void setFieldValue(Object obj, String fieldName, Object value) {
        if (obj == null) {
            return;
        }

        try {
            Field field = findField(obj.getClass(), fieldName);
            if (field != null) {
                field.setAccessible(true);
                field.set(obj, value);
            }
        } catch (Exception e) {
            // 忽略异常
        }
    }

    private Field findField(Class<?> clazz, String fieldName) {
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
}
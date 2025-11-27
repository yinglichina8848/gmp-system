package com.gmp.qms.service.impl;

import com.gmp.qms.dto.CapaDTO;
import com.gmp.qms.dto.CapaSearchCriteria;
import com.gmp.qms.entity.Capa;
import com.gmp.qms.entity.CapaAttachment;
import com.gmp.qms.exception.ResourceNotFoundException;
import com.gmp.qms.repository.CapaRepository;
import com.gmp.qms.service.CapaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * CAPA管理服务实现类
 * 
 * @author GMP系统开发团队
 */
@Service
@RequiredArgsConstructor
public class CapaServiceImpl implements CapaService {

    private final CapaRepository capaRepository;
    // 文件存储路径，实际应用中应配置在配置文件中
    private static final String UPLOAD_DIR = "/opt/gmp-system/uploads/capas/";

    @Override
    @Transactional
    public Capa createCapa(CapaDTO capaDTO) {
        Capa capa = new Capa();
        BeanUtils.copyProperties(capaDTO, capa);
        
        // 生成CAPA编号
        capa.setCapaCode(generateCapaCode());
        
        // 设置初始状态
        capa.setStatus(Capa.CapaStatus.IDENTIFIED);
        
        return capaRepository.save(capa);
    }

    @Override
    @Transactional
    public Capa updateCapa(Long id, CapaDTO capaDTO) {
        Capa capa = getCapaById(id);
        BeanUtils.copyProperties(capaDTO, capa, "id", "capaCode", "createdAt", "createdBy", "status");
        return capaRepository.save(capa);
    }

    @Override
    public Capa getCapaById(Long id) {
        return capaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CAPA not found with id: " + id));
    }

    @Override
    public Capa getCapaByCode(String capaCode) {
        return capaRepository.findByCapaCode(capaCode);
    }

    @Override
    public Page<Capa> findAllCapas(Pageable pageable) {
        return capaRepository.findAll(pageable);
    }

    @Override
    public Page<Capa> searchCapas(CapaSearchCriteria criteria, Pageable pageable) {
        Specification<Capa> specification = buildCapaSpecification(criteria);
        return capaRepository.findAll(specification, pageable);
    }

    @Override
    @Transactional
    public Capa updateCapaStatus(Long id, Capa.CapaStatus status, String comments) {
        Capa capa = getCapaById(id);
        capa.setStatus(status);
        // 这里可以添加状态变更历史记录
        return capaRepository.save(capa);
    }

    @Override
    @Transactional
    public Capa addAttachment(Long capaId, MultipartFile file, String description) {
        Capa capa = getCapaById(capaId);
        
        try {
            // 确保上传目录存在
            Path uploadPath = Paths.get(UPLOAD_DIR + capaId + "/");
            Files.createDirectories(uploadPath);
            
            // 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf('.')) : "";
            String fileName = UUID.randomUUID() + extension;
            
            // 保存文件
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);
            
            // 创建附件记录
            CapaAttachment attachment = new CapaAttachment();
            attachment.setCapa(capa);
            attachment.setFileName(originalFilename);
            attachment.setFilePath(filePath.toString());
            attachment.setFileSize(file.getSize());
            attachment.setFileType(file.getContentType());
            attachment.setDescription(description);
            
            capa.getAttachments().add(attachment);
            return capaRepository.save(capa);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public Capa removeAttachment(Long capaId, Long attachmentId) {
        Capa capa = getCapaById(capaId);
        
        boolean removed = capa.getAttachments().removeIf(attachment -> 
                attachment.getId().equals(attachmentId)
        );
        
        if (!removed) {
            throw new ResourceNotFoundException("Attachment not found with id: " + attachmentId);
        }
        
        return capaRepository.save(capa);
    }

    @Override
    public List<Capa> findOverdueCapas() {
        LocalDateTime now = LocalDateTime.now();
        List<Capa.CapaStatus> excludedStatuses = List.of(
                Capa.CapaStatus.CLOSED,
                Capa.CapaStatus.REJECTED
        );
        return capaRepository.findOverdueCapa(now, excludedStatuses);
    }

    @Override
    public String generateCapaCode() {
        // 生成格式：CAPA-YYYYMMDD-XXXXX
        String datePrefix = "CAPA-" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "-";
        
        // 获取当前记录数并递增作为编号后缀
        long count = capaRepository.count();
        // 格式化为5位数字，不足前补0
        String sequenceNumber = String.format("%05d", count + 1);
        return datePrefix + sequenceNumber;
    }

    /**
     * 构建CAPA查询条件
     */
    private Specification<Capa> buildCapaSpecification(CapaSearchCriteria criteria) {
        return (root, query, criteriaBuilder) -> {
            Specification<Capa> spec = Specification.where(null);
            
            // 这里可以根据criteria构建复杂的查询条件
            
            return spec.toPredicate(root, query, criteriaBuilder);
        };
    }
}

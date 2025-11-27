package com.gmp.qms.service.impl;

import com.gmp.qms.dto.ChangeControlDTO;
import com.gmp.qms.dto.ChangeControlSearchCriteria;
import com.gmp.qms.entity.ChangeControl;
import com.gmp.qms.entity.ChangeControlAttachment;
import com.gmp.qms.exception.ResourceNotFoundException;
import com.gmp.qms.repository.ChangeControlRepository;
import com.gmp.qms.service.ChangeControlService;
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
 * 变更控制服务实现类
 * 
 * @author GMP系统开发团队
 */
@Service
@RequiredArgsConstructor
public class ChangeControlServiceImpl implements ChangeControlService {

    private final ChangeControlRepository changeControlRepository;
    // 文件存储路径，实际应用中应配置在配置文件中
    private static final String UPLOAD_DIR = "/opt/gmp-system/uploads/change-controls/";

    @Override
    @Transactional
    public ChangeControl createChangeControl(ChangeControlDTO changeControlDTO) {
        ChangeControl changeControl = new ChangeControl();
        BeanUtils.copyProperties(changeControlDTO, changeControl);
        
        // 生成变更编号
        changeControl.setChangeCode(generateChangeCode());
        
        // 设置初始状态
        changeControl.setStatus(ChangeControl.ChangeStatus.PROPOSED);
        
        return changeControlRepository.save(changeControl);
    }

    @Override
    @Transactional
    public ChangeControl updateChangeControl(Long id, ChangeControlDTO changeControlDTO) {
        ChangeControl changeControl = getChangeControlById(id);
        BeanUtils.copyProperties(changeControlDTO, changeControl, "id", "changeCode", "createdAt", "createdBy", "status");
        return changeControlRepository.save(changeControl);
    }

    @Override
    public ChangeControl getChangeControlById(Long id) {
        return changeControlRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Change Control not found with id: " + id));
    }

    @Override
    public ChangeControl getChangeControlByCode(String changeCode) {
        return changeControlRepository.findByChangeCode(changeCode);
    }

    @Override
    public Page<ChangeControl> findAllChangeControls(Pageable pageable) {
        return changeControlRepository.findAll(pageable);
    }

    @Override
    public Page<ChangeControl> searchChangeControls(ChangeControlSearchCriteria criteria, Pageable pageable) {
        Specification<ChangeControl> specification = buildChangeControlSpecification(criteria);
        return changeControlRepository.findAll(specification, pageable);
    }

    @Override
    @Transactional
    public ChangeControl updateChangeControlStatus(Long id, ChangeControl.ChangeStatus status, String comments) {
        ChangeControl changeControl = getChangeControlById(id);
        changeControl.setStatus(status);
        // 这里可以添加状态变更历史记录
        return changeControlRepository.save(changeControl);
    }

    @Override
    @Transactional
    public ChangeControl addAttachment(Long changeControlId, MultipartFile file, String description) {
        ChangeControl changeControl = getChangeControlById(changeControlId);
        
        try {
            // 确保上传目录存在
            Path uploadPath = Paths.get(UPLOAD_DIR + changeControlId + "/");
            Files.createDirectories(uploadPath);
            
            // 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf('.')) : "";
            String fileName = UUID.randomUUID() + extension;
            
            // 保存文件
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);
            
            // 创建附件记录
            ChangeControlAttachment attachment = new ChangeControlAttachment();
            attachment.setChangeControl(changeControl);
            attachment.setFileName(originalFilename);
            attachment.setFilePath(filePath.toString());
            attachment.setFileSize(file.getSize());
            attachment.setFileType(file.getContentType());
            attachment.setDescription(description);
            
            changeControl.getAttachments().add(attachment);
            return changeControlRepository.save(changeControl);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public ChangeControl removeAttachment(Long changeControlId, Long attachmentId) {
        ChangeControl changeControl = getChangeControlById(changeControlId);
        
        boolean removed = changeControl.getAttachments().removeIf(attachment -> 
                attachment.getId().equals(attachmentId)
        );
        
        if (!removed) {
            throw new ResourceNotFoundException("Attachment not found with id: " + attachmentId);
        }
        
        return changeControlRepository.save(changeControl);
    }

    @Override
    public List<ChangeControl> findOverdueChangeControls() {
        LocalDateTime now = LocalDateTime.now();
        List<ChangeControl.ChangeStatus> excludedStatuses = List.of(
                ChangeControl.ChangeStatus.COMPLETED,
                ChangeControl.ChangeStatus.CANCELLED
        );
        return changeControlRepository.findOverdueChangeControls(now, excludedStatuses);
    }

    @Override
    public String generateChangeCode() {
        // 生成格式：CC-YYYYMMDD-XXXXX
        String datePrefix = "CC-" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "-";
        
        // 这里可以添加数据库查询逻辑获取当天最大编号并递增
        // 暂时返回一个简单的模拟编号
        return datePrefix + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
    }

    /**
     * 构建变更控制查询条件
     */
    private Specification<ChangeControl> buildChangeControlSpecification(ChangeControlSearchCriteria criteria) {
        return (root, query, criteriaBuilder) -> {
            Specification<ChangeControl> spec = Specification.where(null);
            
            // 这里可以根据criteria构建复杂的查询条件
            
            return spec.toPredicate(root, query, criteriaBuilder);
        };
    }
}

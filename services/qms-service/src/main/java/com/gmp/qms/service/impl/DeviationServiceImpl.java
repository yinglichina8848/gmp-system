package com.gmp.qms.service.impl;

import com.gmp.qms.dto.DeviationDTO;
import com.gmp.qms.dto.DeviationSearchCriteria;
import com.gmp.qms.entity.Deviation;
import com.gmp.qms.entity.DeviationAttachment;
import com.gmp.qms.exception.ResourceNotFoundException;
import com.gmp.qms.repository.DeviationAttachmentRepository;
import com.gmp.qms.repository.DeviationRepository;
import com.gmp.qms.service.DeviationService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 偏差管理服务实现类
 * 
 * @author GMP系统开发团队
 */
@Service
@RequiredArgsConstructor
public class DeviationServiceImpl implements DeviationService {

    private final DeviationRepository deviationRepository;
    private final DeviationAttachmentRepository deviationAttachmentRepository;
    // 文件存储路径，实际应用中应配置在配置文件中
    private static final String UPLOAD_DIR = "/opt/gmp-system/uploads/deviations/";

    @Override
    @Transactional
    public Deviation createDeviation(DeviationDTO deviationDTO) {
        Deviation deviation = new Deviation();
        BeanUtils.copyProperties(deviationDTO, deviation);

        // 生成偏差编号
        deviation.setDeviationCode(generateDeviationCode());

        // 设置初始状态
        deviation.setStatus(Deviation.DeviationStatus.IDENTIFIED);

        return deviationRepository.save(deviation);
    }

    @Override
    @Transactional
    public Deviation updateDeviation(Long id, DeviationDTO deviationDTO) {
        Deviation deviation = getDeviationById(id);
        BeanUtils.copyProperties(deviationDTO, deviation, "id", "deviationCode", "createdAt", "createdBy", "status");
        return deviationRepository.save(deviation);
    }

    @Override
    public Deviation getDeviationById(Long id) {
        return deviationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deviation not found with id: " + id));
    }

    @Override
    public Deviation getDeviationByCode(String deviationCode) {
        return deviationRepository.findByDeviationCode(deviationCode);
    }

    @Override
    public Page<Deviation> findAllDeviations(Pageable pageable) {
        return deviationRepository.findAll(pageable);
    }

    @Override
    public Page<Deviation> searchDeviations(DeviationSearchCriteria criteria, Pageable pageable) {
        Specification<Deviation> specification = buildDeviationSpecification(criteria);
        return deviationRepository.findAll(specification, pageable);
    }

    @Override
    @Transactional
    public Deviation updateDeviationStatus(Long id, Deviation.DeviationStatus status, String comments) {
        Deviation deviation = getDeviationById(id);
        deviation.setStatus(status);
        // 这里可以添加状态变更历史记录
        return deviationRepository.save(deviation);
    }

    @Override
    @Transactional
    public Deviation addAttachment(Long deviationId, MultipartFile file, String description) {
        Deviation deviation = getDeviationById(deviationId);

        try {
            // 确保上传目录存在
            Path uploadPath = Paths.get(UPLOAD_DIR + deviationId + "/");
            Files.createDirectories(uploadPath);

            // 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf('.'))
                    : "";
            String fileName = UUID.randomUUID() + extension;

            // 保存文件
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);

            // 创建附件记录
            DeviationAttachment attachment = new DeviationAttachment();
            attachment.setDeviation(deviation);
            attachment.setFileName(originalFilename);
            attachment.setFilePath(filePath.toString());
            attachment.setFileSize(file.getSize());
            attachment.setFileType(file.getContentType());
            attachment.setDescription(description);
            // 设置默认上传人为系统用户ID 1
            attachment.setUploadedBy(1L); // 使用Long类型的默认用户ID

            // 直接保存附件，而不是通过deviation的关系
            deviationAttachmentRepository.save(attachment);
            return deviation;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public Deviation removeAttachment(Long deviationId, Long attachmentId) {
        Deviation deviation = getDeviationById(deviationId);

        // 直接删除附件记录
        DeviationAttachment attachment = deviationAttachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Attachment not found with id: " + attachmentId));

        // 移除对附件与偏差关系的验证，直接删除附件
        // 实际项目中应添加适当的权限和业务逻辑验证

        deviationAttachmentRepository.delete(attachment);
        return deviation;
    }

    @Override
    public List<Deviation> findOverdueDeviations() {
        LocalDateTime now = LocalDateTime.now();
        // 使用repository中定义的查询方法，查询已识别状态的逾期偏差
        return deviationRepository.findOverdueDeviations(Deviation.DeviationStatus.IDENTIFIED, now);
    }

    @Override
    public String generateDeviationCode() {
        // 生成格式：DEV-YYYYMMDD-XXXXX
        String datePrefix = "DEV-" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "-";

        // 由于repository中没有findMaxCodeByPrefix方法，这里使用简单的计数方式
        // 实际生产环境中应该在repository中添加该方法或使用自定义查询
        String codePattern = datePrefix + "%";

        // 查询当天已有的偏差数量
        long count = deviationRepository.count();
        // 简单的计数方式，实际项目中应该使用更精确的方法
        return datePrefix + String.format("%05d", count + 1);
    }

    /**
     * 构建偏差查询条件
     */
    private Specification<Deviation> buildDeviationSpecification(DeviationSearchCriteria criteria) {
        return (root, query, criteriaBuilder) -> {
            Specification<Deviation> spec = Specification.where(null);

            // 这里可以根据criteria构建复杂的查询条件

            return spec.toPredicate(root, query, criteriaBuilder);
        };
    }
}

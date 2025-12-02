package com.gmp.edms.service.impl;

import com.gmp.edms.dto.ElectronicSignatureDTO;
import com.gmp.edms.entity.Document;
import com.gmp.edms.entity.DocumentVersion;
import com.gmp.edms.entity.ElectronicSignature;
import com.gmp.edms.repository.DocumentRepository;
import com.gmp.edms.repository.DocumentVersionRepository;
import com.gmp.edms.repository.ElectronicSignatureRepository;
import com.gmp.edms.service.ElectronicSignatureService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 电子签名服务实现
 */
@Service
@RequiredArgsConstructor
public class ElectronicSignatureServiceImpl implements ElectronicSignatureService {

    private final ElectronicSignatureRepository electronicSignatureRepository;
    private final DocumentRepository documentRepository;
    private final DocumentVersionRepository documentVersionRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public ElectronicSignatureDTO createSignature(ElectronicSignatureDTO signatureDTO) {
        // 创建电子签名实体
        ElectronicSignature signature = modelMapper.map(signatureDTO, ElectronicSignature.class);

        // 设置签名时间
        signature.setSignatureTime(LocalDateTime.now());

        // 设置默认验证结果
        signature.setVerificationResult(true);

        // 设置默认有效性状态
        signature.setValidityStatus("VALID");

        // 如果提供了文档ID，关联文档
        if (signatureDTO.getDocumentId() != null) {
            Document document = documentRepository.findById(signatureDTO.getDocumentId())
                    .orElseThrow(() -> new RuntimeException("文档不存在: " + signatureDTO.getDocumentId()));
            signature.setDocument(document);
        }

        // 如果提供了文档版本ID，关联文档版本
        if (signatureDTO.getDocumentVersionId() != null) {
            DocumentVersion documentVersion = documentVersionRepository.findById(signatureDTO.getDocumentVersionId())
                    .orElseThrow(() -> new RuntimeException("文档版本不存在: " + signatureDTO.getDocumentVersionId()));
            signature.setDocumentVersion(documentVersion);
        }

        // 保存电子签名
        signature = electronicSignatureRepository.save(signature);

        // 转换为DTO并返回
        return modelMapper.map(signature, ElectronicSignatureDTO.class);
    }

    @Override
    public ElectronicSignatureDTO getSignatureById(Long id) {
        // 查找电子签名
        ElectronicSignature signature = electronicSignatureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("电子签名不存在: " + id));

        // 转换为DTO并返回
        return modelMapper.map(signature, ElectronicSignatureDTO.class);
    }

    @Override
    public List<ElectronicSignatureDTO> getSignaturesByDocumentId(Long documentId) {
        // 根据文档ID查询电子签名
        List<ElectronicSignature> signatures = electronicSignatureRepository.findByDocumentId(documentId);

        // 转换为DTO列表并返回
        return signatures.stream()
                .map(signature -> modelMapper.map(signature, ElectronicSignatureDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ElectronicSignatureDTO> getSignaturesByDocumentVersionId(Long documentVersionId) {
        // 根据文档版本ID查询电子签名
        List<ElectronicSignature> signatures = electronicSignatureRepository.findByDocumentVersionId(documentVersionId);

        // 转换为DTO列表并返回
        return signatures.stream()
                .map(signature -> modelMapper.map(signature, ElectronicSignatureDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean verifySignature(Long signatureId) {
        // 查找电子签名
        ElectronicSignature signature = electronicSignatureRepository.findById(signatureId)
                .orElseThrow(() -> new RuntimeException("电子签名不存在: " + signatureId));

        // 这里简化处理，实际应调用签名验证服务进行验证
        boolean isValid = true;

        // 更新验证结果
        signature.setVerificationResult(isValid);
        signature.setValidityStatus(isValid ? "VALID" : "INVALID");

        // 保存更新
        electronicSignatureRepository.save(signature);

        return isValid;
    }

    @Override
    @Transactional
    public List<Boolean> batchVerifySignatures(List<Long> signatureIds) {
        // 批量验证电子签名
        return signatureIds.stream()
                .map(this::verifySignature)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteSignature(Long id) {
        // 检查电子签名是否存在
        ElectronicSignature signature = electronicSignatureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("电子签名不存在: " + id));

        // 删除电子签名
        electronicSignatureRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void batchDeleteSignatures(List<Long> ids) {
        // 批量删除电子签名
        electronicSignatureRepository.deleteAllById(ids);
    }
}

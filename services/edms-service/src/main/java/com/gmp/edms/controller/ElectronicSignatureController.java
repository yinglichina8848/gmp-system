package com.gmp.edms.controller;

import com.gmp.edms.dto.ApiResponse;
import com.gmp.edms.dto.ElectronicSignatureDTO;
import com.gmp.edms.service.ElectronicSignatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 电子签名控制器
 */
@RestController
@RequestMapping("/api/edms/signatures")
@RequiredArgsConstructor
public class ElectronicSignatureController {

    private final ElectronicSignatureService electronicSignatureService;

    /**
     * 创建电子签名
     * 
     * @param signatureDTO 电子签名DTO
     * @return 电子签名DTO
     */
    @PostMapping
    public ApiResponse<ElectronicSignatureDTO> createSignature(@RequestBody ElectronicSignatureDTO signatureDTO) {
        ElectronicSignatureDTO createdSignature = electronicSignatureService.createSignature(signatureDTO);
        return ApiResponse.success("电子签名创建成功", createdSignature);
    }

    /**
     * 根据ID获取电子签名
     * 
     * @param id 电子签名ID
     * @return 电子签名DTO
     */
    @GetMapping("/{id}")
    public ApiResponse<ElectronicSignatureDTO> getSignatureById(@PathVariable Long id) {
        ElectronicSignatureDTO signature = electronicSignatureService.getSignatureById(id);
        return ApiResponse.success("电子签名查询成功", signature);
    }

    /**
     * 根据文档ID获取电子签名列表
     * 
     * @param documentId 文档ID
     * @return 电子签名DTO列表
     */
    @GetMapping("/document/{documentId}")
    public ApiResponse<List<ElectronicSignatureDTO>> getSignaturesByDocumentId(@PathVariable Long documentId) {
        List<ElectronicSignatureDTO> signatures = electronicSignatureService.getSignaturesByDocumentId(documentId);
        return ApiResponse.success("电子签名列表查询成功", signatures);
    }

    /**
     * 根据文档版本ID获取电子签名列表
     * 
     * @param documentVersionId 文档版本ID
     * @return 电子签名DTO列表
     */
    @GetMapping("/version/{documentVersionId}")
    public ApiResponse<List<ElectronicSignatureDTO>> getSignaturesByDocumentVersionId(
            @PathVariable Long documentVersionId) {
        List<ElectronicSignatureDTO> signatures = electronicSignatureService
                .getSignaturesByDocumentVersionId(documentVersionId);
        return ApiResponse.success("电子签名列表查询成功", signatures);
    }

    /**
     * 验证电子签名
     * 
     * @param id 电子签名ID
     * @return 验证结果
     */
    @PostMapping("/{id}/verify")
    public ApiResponse<Boolean> verifySignature(@PathVariable Long id) {
        boolean result = electronicSignatureService.verifySignature(id);
        return ApiResponse.success("电子签名验证成功", result);
    }

    /**
     * 批量验证电子签名
     * 
     * @param signatureIds 电子签名ID列表
     * @return 验证结果列表
     */
    @PostMapping("/batch-verify")
    public ApiResponse<List<Boolean>> batchVerifySignatures(@RequestBody List<Long> signatureIds) {
        List<Boolean> results = electronicSignatureService.batchVerifySignatures(signatureIds);
        return ApiResponse.success("电子签名批量验证成功", results);
    }

    /**
     * 删除电子签名
     * 
     * @param id 电子签名ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteSignature(@PathVariable Long id) {
        electronicSignatureService.deleteSignature(id);
        return ApiResponse.success("电子签名删除成功");
    }

    /**
     * 批量删除电子签名
     * 
     * @param ids 电子签名ID列表
     * @return 操作结果
     */
    @DeleteMapping("/batch-delete")
    public ApiResponse<Void> batchDeleteSignatures(@RequestBody List<Long> ids) {
        electronicSignatureService.batchDeleteSignatures(ids);
        return ApiResponse.success("电子签名批量删除成功");
    }
}

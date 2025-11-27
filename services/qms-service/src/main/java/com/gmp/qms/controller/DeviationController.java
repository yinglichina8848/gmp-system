package com.gmp.qms.controller;

import com.gmp.qms.dto.DeviationDTO;
import com.gmp.qms.dto.DeviationSearchCriteria;
import com.gmp.qms.entity.Deviation;
import com.gmp.qms.model.ApiResponse;
import com.gmp.qms.service.DeviationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 偏差管理控制器
 * 
 * @author GMP系统开发团队
 */
@RestController
@RequestMapping("/api/v1/deviations")
@RequiredArgsConstructor
public class DeviationController {

    private final DeviationService deviationService;

    /**
     * 创建新的偏差记录
     */
    @PostMapping
    public ApiResponse<Deviation> createDeviation(@RequestBody DeviationDTO deviationDTO) {
        Deviation deviation = deviationService.createDeviation(deviationDTO);
        return ApiResponse.success(deviation);
    }

    /**
     * 更新偏差记录
     */
    @PutMapping("/{id}")
    public ApiResponse<Deviation> updateDeviation(@PathVariable Long id, @RequestBody DeviationDTO deviationDTO) {
        Deviation deviation = deviationService.updateDeviation(id, deviationDTO);
        return ApiResponse.success(deviation);
    }

    /**
     * 根据ID查询偏差详情
     */
    @GetMapping("/{id}")
    public ApiResponse<Deviation> getDeviationById(@PathVariable Long id) {
        Deviation deviation = deviationService.getDeviationById(id);
        return ApiResponse.success(deviation);
    }

    /**
     * 根据编号查询偏差详情
     */
    @GetMapping("/code/{code}")
    public ApiResponse<Deviation> getDeviationByCode(@PathVariable String code) {
        Deviation deviation = deviationService.getDeviationByCode(code);
        return ApiResponse.success(deviation);
    }

    /**
     * 分页查询偏差列表
     */
    @GetMapping
    public ApiResponse<Page<Deviation>> getDeviations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Deviation> deviations = deviationService.findAllDeviations(pageable);
        return ApiResponse.success(deviations);
    }

    /**
     * 根据条件搜索偏差
     */
    @PostMapping("/search")
    public ApiResponse<Page<Deviation>> searchDeviations(
            @RequestBody DeviationSearchCriteria criteria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Deviation> deviations = deviationService.searchDeviations(criteria, pageable);
        return ApiResponse.success(deviations);
    }

    /**
     * 更新偏差状态
     */
    @PutMapping("/{id}/status")
    public ApiResponse<Deviation> updateDeviationStatus(
            @PathVariable Long id,
            @RequestParam Deviation.DeviationStatus status,
            @RequestParam(required = false) String comments) {
        Deviation deviation = deviationService.updateDeviationStatus(id, status, comments);
        return ApiResponse.success(deviation);
    }

    /**
     * 上传偏差附件
     */
    @PostMapping("/{id}/attachments")
    public ApiResponse<Deviation> addAttachment(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) String description) {
        Deviation deviation = deviationService.addAttachment(id, file, description);
        return ApiResponse.success(deviation);
    }

    /**
     * 删除偏差附件
     */
    @DeleteMapping("/{id}/attachments/{attachmentId}")
    public ApiResponse<Deviation> removeAttachment(
            @PathVariable Long id,
            @PathVariable Long attachmentId) {
        Deviation deviation = deviationService.removeAttachment(id, attachmentId);
        return ApiResponse.success(deviation);
    }

    /**
     * 生成新的偏差编号
     */
    @GetMapping("/generate-code")
    public ApiResponse<String> generateDeviationCode() {
        String code = deviationService.generateDeviationCode();
        return ApiResponse.success(code);
    }
}

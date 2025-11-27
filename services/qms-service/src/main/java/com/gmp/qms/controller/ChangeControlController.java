package com.gmp.qms.controller;

import com.gmp.qms.dto.ChangeControlDTO;
import com.gmp.qms.dto.ChangeControlSearchCriteria;
import com.gmp.qms.entity.ChangeControl;
import com.gmp.qms.model.ApiResponse;
import com.gmp.qms.service.ChangeControlService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 变更控制控制器
 * 
 * @author GMP系统开发团队
 */
@RestController
@RequestMapping("/api/v1/change-controls")
@RequiredArgsConstructor
public class ChangeControlController {

    private final ChangeControlService changeControlService;

    /**
     * 创建新的变更控制记录
     */
    @PostMapping
    public ApiResponse<ChangeControl> createChangeControl(@RequestBody ChangeControlDTO changeControlDTO) {
        ChangeControl changeControl = changeControlService.createChangeControl(changeControlDTO);
        return ApiResponse.success(changeControl);
    }

    /**
     * 更新变更控制记录
     */
    @PutMapping("/{id}")
    public ApiResponse<ChangeControl> updateChangeControl(@PathVariable Long id, @RequestBody ChangeControlDTO changeControlDTO) {
        ChangeControl changeControl = changeControlService.updateChangeControl(id, changeControlDTO);
        return ApiResponse.success(changeControl);
    }

    /**
     * 根据ID查询变更控制详情
     */
    @GetMapping("/{id}")
    public ApiResponse<ChangeControl> getChangeControlById(@PathVariable Long id) {
        ChangeControl changeControl = changeControlService.getChangeControlById(id);
        return ApiResponse.success(changeControl);
    }

    /**
     * 根据编号查询变更控制详情
     */
    @GetMapping("/code/{code}")
    public ApiResponse<ChangeControl> getChangeControlByCode(@PathVariable String code) {
        ChangeControl changeControl = changeControlService.getChangeControlByCode(code);
        return ApiResponse.success(changeControl);
    }

    /**
     * 分页查询变更控制列表
     */
    @GetMapping
    public ApiResponse<Page<ChangeControl>> getChangeControls(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ChangeControl> changeControls = changeControlService.findAllChangeControls(pageable);
        return ApiResponse.success(changeControls);
    }

    /**
     * 根据条件搜索变更控制
     */
    @PostMapping("/search")
    public ApiResponse<Page<ChangeControl>> searchChangeControls(
            @RequestBody ChangeControlSearchCriteria criteria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ChangeControl> changeControls = changeControlService.searchChangeControls(criteria, pageable);
        return ApiResponse.success(changeControls);
    }

    /**
     * 更新变更控制状态
     */
    @PutMapping("/{id}/status")
    public ApiResponse<ChangeControl> updateChangeControlStatus(
            @PathVariable Long id,
            @RequestParam ChangeControl.ChangeStatus status,
            @RequestParam(required = false) String comments) {
        ChangeControl changeControl = changeControlService.updateChangeControlStatus(id, status, comments);
        return ApiResponse.success(changeControl);
    }

    /**
     * 上传变更控制附件
     */
    @PostMapping("/{id}/attachments")
    public ApiResponse<ChangeControl> addAttachment(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) String description) {
        ChangeControl changeControl = changeControlService.addAttachment(id, file, description);
        return ApiResponse.success(changeControl);
    }

    /**
     * 删除变更控制附件
     */
    @DeleteMapping("/{id}/attachments/{attachmentId}")
    public ApiResponse<ChangeControl> removeAttachment(
            @PathVariable Long id,
            @PathVariable Long attachmentId) {
        ChangeControl changeControl = changeControlService.removeAttachment(id, attachmentId);
        return ApiResponse.success(changeControl);
    }

    /**
     * 生成新的变更控制编号
     */
    @GetMapping("/generate-code")
    public ApiResponse<String> generateChangeControlCode() {
        String code = changeControlService.generateChangeCode();
        return ApiResponse.success(code);
    }
}

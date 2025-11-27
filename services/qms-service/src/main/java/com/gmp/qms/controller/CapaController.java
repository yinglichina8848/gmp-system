package com.gmp.qms.controller;

import com.gmp.qms.dto.CapaDTO;
import com.gmp.qms.dto.CapaSearchCriteria;
import com.gmp.qms.entity.Capa;
import com.gmp.qms.model.ApiResponse;
import com.gmp.qms.service.CapaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * CAPA管理控制器
 * 
 * @author GMP系统开发团队
 */
@RestController
@RequestMapping("/api/v1/capas")
@RequiredArgsConstructor
public class CapaController {

    private final CapaService capaService;

    /**
     * 创建新的CAPA记录
     */
    @PostMapping
    public ApiResponse<Capa> createCapa(@RequestBody CapaDTO capaDTO) {
        Capa capa = capaService.createCapa(capaDTO);
        return ApiResponse.success(capa);
    }

    /**
     * 更新CAPA记录
     */
    @PutMapping("/{id}")
    public ApiResponse<Capa> updateCapa(@PathVariable Long id, @RequestBody CapaDTO capaDTO) {
        Capa capa = capaService.updateCapa(id, capaDTO);
        return ApiResponse.success(capa);
    }

    /**
     * 根据ID查询CAPA详情
     */
    @GetMapping("/{id}")
    public ApiResponse<Capa> getCapaById(@PathVariable Long id) {
        Capa capa = capaService.getCapaById(id);
        return ApiResponse.success(capa);
    }

    /**
     * 根据编号查询CAPA详情
     */
    @GetMapping("/code/{code}")
    public ApiResponse<Capa> getCapaByCode(@PathVariable String code) {
        Capa capa = capaService.getCapaByCode(code);
        return ApiResponse.success(capa);
    }

    /**
     * 分页查询CAPA列表
     */
    @GetMapping
    public ApiResponse<Page<Capa>> getCapas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Capa> capas = capaService.findAllCapas(pageable);
        return ApiResponse.success(capas);
    }

    /**
     * 根据条件搜索CAPA
     */
    @PostMapping("/search")
    public ApiResponse<Page<Capa>> searchCapas(
            @RequestBody CapaSearchCriteria criteria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Capa> capas = capaService.searchCapas(criteria, pageable);
        return ApiResponse.success(capas);
    }

    /**
     * 更新CAPA状态
     */
    @PutMapping("/{id}/status")
    public ApiResponse<Capa> updateCapaStatus(
            @PathVariable Long id,
            @RequestParam Capa.CapaStatus status,
            @RequestParam(required = false) String comments) {
        Capa capa = capaService.updateCapaStatus(id, status, comments);
        return ApiResponse.success(capa);
    }

    /**
     * 上传CAPA附件
     */
    @PostMapping("/{id}/attachments")
    public ApiResponse<Capa> addAttachment(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) String description) {
        Capa capa = capaService.addAttachment(id, file, description);
        return ApiResponse.success(capa);
    }

    /**
     * 删除CAPA附件
     */
    @DeleteMapping("/{id}/attachments/{attachmentId}")
    public ApiResponse<Capa> removeAttachment(
            @PathVariable Long id,
            @PathVariable Long attachmentId) {
        Capa capa = capaService.removeAttachment(id, attachmentId);
        return ApiResponse.success(capa);
    }

    /**
     * 生成新的CAPA编号
     */
    @GetMapping("/generate-code")
    public ApiResponse<String> generateCapaCode() {
        String code = capaService.generateCapaCode();
        return ApiResponse.success(code);
    }
}

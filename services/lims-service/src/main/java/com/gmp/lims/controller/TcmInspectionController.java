package com.gmp.lims.controller;

import com.gmp.lims.dto.TcmInspectionDTO;
import com.gmp.lims.entity.TcmInspection;
import com.gmp.lims.dto.ApiResponse;
import com.gmp.lims.service.TcmInspectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

/**
 * 中药特色检验控制器
 * 用于管理中药材特有的检验项目和结果
 */
@RestController
@RequestMapping("/api/v1/tcm-inspections")
@RequiredArgsConstructor
public class TcmInspectionController {

    private final TcmInspectionService tcmInspectionService;

    /**
     * 创建新的中药特色检验记录
     */
    @PostMapping
    public ApiResponse<TcmInspection> createTcmInspection(@RequestBody TcmInspectionDTO tcmInspectionDTO) {
        TcmInspection tcmInspection = tcmInspectionService.createTcmInspection(tcmInspectionDTO);
        return ApiResponse.success(tcmInspection);
    }

    /**
     * 更新中药特色检验记录
     */
    @PutMapping("/{id}")
    public ApiResponse<TcmInspection> updateTcmInspection(@PathVariable Long id, @RequestBody TcmInspectionDTO tcmInspectionDTO) {
        TcmInspection tcmInspection = tcmInspectionService.updateTcmInspection(id, tcmInspectionDTO);
        return ApiResponse.success(tcmInspection);
    }

    /**
     * 根据ID查询中药特色检验详情
     */
    @GetMapping("/{id}")
    public ApiResponse<TcmInspection> getTcmInspectionById(@PathVariable Long id) {
        TcmInspection tcmInspection = tcmInspectionService.getTcmInspectionById(id);
        return ApiResponse.success(tcmInspection);
    }

    /**
     * 根据检验编号查询中药特色检验记录
     */
    @GetMapping("/code/{inspectionCode}")
    public ApiResponse<TcmInspection> getTcmInspectionByCode(@PathVariable String inspectionCode) {
        TcmInspection tcmInspection = tcmInspectionService.getTcmInspectionByCode(inspectionCode);
        return ApiResponse.success(tcmInspection);
    }

    /**
     * 分页查询中药特色检验记录列表
     */
    @GetMapping
    public ApiResponse<Page<TcmInspection>> getTcmInspections(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TcmInspection> tcmInspections = tcmInspectionService.getTcmInspections(pageable);
        return ApiResponse.success(tcmInspections);
    }

    /**
     * 删除中药特色检验记录
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteTcmInspection(@PathVariable Long id) {
        tcmInspectionService.deleteTcmInspection(id);
        return ApiResponse.success();
    }
}
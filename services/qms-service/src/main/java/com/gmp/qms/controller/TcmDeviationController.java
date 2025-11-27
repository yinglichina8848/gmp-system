package com.gmp.qms.controller;

import com.gmp.qms.dto.TcmDeviationDTO;
import com.gmp.qms.entity.TcmDeviation;
import com.gmp.qms.model.ApiResponse;
import com.gmp.qms.service.TcmDeviationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

/**
 * 中药特色偏差管理控制器
 * 用于处理中药材生产过程中特有的偏差类型
 *
 * @author GMP系统开发团队
 */
@RestController
@RequestMapping("/api/v1/tcm-deviations")
@RequiredArgsConstructor
public class TcmDeviationController {

    private final TcmDeviationService tcmDeviationService;

    /**
     * 创建新的中药特色偏差记录
     */
    @PostMapping
    public ApiResponse<TcmDeviation> createTcmDeviation(@RequestBody TcmDeviationDTO tcmDeviationDTO) {
        TcmDeviation tcmDeviation = tcmDeviationService.createTcmDeviation(tcmDeviationDTO);
        return ApiResponse.success(tcmDeviation);
    }

    /**
     * 更新中药特色偏差记录
     */
    @PutMapping("/{id}")
    public ApiResponse<TcmDeviation> updateTcmDeviation(@PathVariable Long id, @RequestBody TcmDeviationDTO tcmDeviationDTO) {
        TcmDeviation tcmDeviation = tcmDeviationService.updateTcmDeviation(id, tcmDeviationDTO);
        return ApiResponse.success(tcmDeviation);
    }

    /**
     * 根据ID查询中药特色偏差详情
     */
    @GetMapping("/{id}")
    public ApiResponse<TcmDeviation> getTcmDeviationById(@PathVariable Long id) {
        TcmDeviation tcmDeviation = tcmDeviationService.getTcmDeviationById(id);
        return ApiResponse.success(tcmDeviation);
    }

    /**
     * 根据编号查询中药特色偏差
     */
    @GetMapping("/code/{deviationCode}")
    public ApiResponse<TcmDeviation> getTcmDeviationByCode(@PathVariable String deviationCode) {
        TcmDeviation tcmDeviation = tcmDeviationService.getTcmDeviationByCode(deviationCode);
        return ApiResponse.success(tcmDeviation);
    }

    /**
     * 分页查询中药特色偏差列表
     */
    @GetMapping
    public ApiResponse<Page<TcmDeviation>> getTcmDeviations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TcmDeviation> tcmDeviations = tcmDeviationService.getTcmDeviations(pageable);
        return ApiResponse.success(tcmDeviations);
    }

    /**
     * 删除中药特色偏差记录
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteTcmDeviation(@PathVariable Long id) {
        tcmDeviationService.deleteTcmDeviation(id);
        return ApiResponse.success(null);
    }
}
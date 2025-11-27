package com.gmp.mes.controller;

import com.gmp.mes.dto.TcmProcessingProcedureDTO;
import com.gmp.mes.entity.TcmProcessingProcedure;
import com.gmp.mes.model.ApiResponse;
import com.gmp.mes.service.TcmProcessingProcedureService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

/**
 * 中药炮制工艺控制器
 * 用于管理中药材炮制过程中的工艺参数和操作信息
 *
 * @author GMP系统开发团队
 */
@RestController
@RequestMapping("/api/v1/tcm-processing-procedures")
@RequiredArgsConstructor
public class TcmProcessingProcedureController {

    private final TcmProcessingProcedureService tcmProcessingProcedureService;

    /**
     * 创建新的中药炮制工艺记录
     */
    @PostMapping
    public ApiResponse<TcmProcessingProcedure> createTcmProcessingProcedure(@RequestBody TcmProcessingProcedureDTO tcmProcessingProcedureDTO) {
        TcmProcessingProcedure tcmProcessingProcedure = tcmProcessingProcedureService.createTcmProcessingProcedure(tcmProcessingProcedureDTO);
        return ApiResponse.success(tcmProcessingProcedure);
    }

    /**
     * 更新中药炮制工艺记录
     */
    @PutMapping("/{id}")
    public ApiResponse<TcmProcessingProcedure> updateTcmProcessingProcedure(@PathVariable Long id, @RequestBody TcmProcessingProcedureDTO tcmProcessingProcedureDTO) {
        TcmProcessingProcedure tcmProcessingProcedure = tcmProcessingProcedureService.updateTcmProcessingProcedure(id, tcmProcessingProcedureDTO);
        return ApiResponse.success(tcmProcessingProcedure);
    }

    /**
     * 根据ID查询中药炮制工艺详情
     */
    @GetMapping("/{id}")
    public ApiResponse<TcmProcessingProcedure> getTcmProcessingProcedureById(@PathVariable Long id) {
        TcmProcessingProcedure tcmProcessingProcedure = tcmProcessingProcedureService.getTcmProcessingProcedureById(id);
        return ApiResponse.success(tcmProcessingProcedure);
    }

    /**
     * 根据编号查询中药炮制工艺
     */
    @GetMapping("/number/{procedureNumber}")
    public ApiResponse<TcmProcessingProcedure> getTcmProcessingProcedureByNumber(@PathVariable String procedureNumber) {
        TcmProcessingProcedure tcmProcessingProcedure = tcmProcessingProcedureService.getTcmProcessingProcedureByNumber(procedureNumber);
        return ApiResponse.success(tcmProcessingProcedure);
    }

    /**
     * 分页查询中药炮制工艺列表
     */
    @GetMapping
    public ApiResponse<Page<TcmProcessingProcedure>> getTcmProcessingProcedures(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TcmProcessingProcedure> tcmProcessingProcedures = tcmProcessingProcedureService.getTcmProcessingProcedures(pageable);
        return ApiResponse.success(tcmProcessingProcedures);
    }

    /**
     * 删除中药炮制工艺记录
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteTcmProcessingProcedure(@PathVariable Long id) {
        tcmProcessingProcedureService.deleteTcmProcessingProcedure(id);
        return ApiResponse.success();
    }
}
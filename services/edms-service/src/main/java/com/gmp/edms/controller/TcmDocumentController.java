package com.gmp.edms.controller;

import com.gmp.edms.dto.ApiResponse;
import com.gmp.edms.dto.TcmDocumentDTO;
import com.gmp.edms.entity.TcmDocument;
import com.gmp.edms.service.TcmDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 中药特色文档控制器
 * 用于管理中药材相关的专业文档
 */
@RestController
@RequestMapping("/api/v1/tcm-documents")
@RequiredArgsConstructor
public class TcmDocumentController {

    private final TcmDocumentService tcmDocumentService;

    /**
     * 创建新的中药特色文档
     */
    @PostMapping
    public ApiResponse<TcmDocument> createTcmDocument(@RequestBody TcmDocumentDTO tcmDocumentDTO) {
        TcmDocument tcmDocument = tcmDocumentService.createTcmDocument(tcmDocumentDTO);
        return ApiResponse.success(tcmDocument);
    }

    /**
     * 更新中药特色文档
     */
    @PutMapping("/{id}")
    public ApiResponse<TcmDocument> updateTcmDocument(@PathVariable Long id, @RequestBody TcmDocumentDTO tcmDocumentDTO) {
        TcmDocument tcmDocument = tcmDocumentService.updateTcmDocument(id, tcmDocumentDTO);
        return ApiResponse.success(tcmDocument);
    }

    /**
     * 根据ID查询中药特色文档详情
     */
    @GetMapping("/{id}")
    public ApiResponse<TcmDocument> getTcmDocumentById(@PathVariable Long id) {
        TcmDocument tcmDocument = tcmDocumentService.getTcmDocumentById(id);
        return ApiResponse.success(tcmDocument);
    }

    /**
     * 根据文档编号查询中药特色文档
     */
    @GetMapping("/number/{documentNumber}")
    public ApiResponse<TcmDocument> getTcmDocumentByNumber(@PathVariable String documentNumber) {
        TcmDocument tcmDocument = tcmDocumentService.getTcmDocumentByNumber(documentNumber);
        return ApiResponse.success(tcmDocument);
    }

    /**
     * 根据中药材名称查询中药特色文档列表
     */
    @GetMapping("/herb/{herbName}")
    public ApiResponse<List<TcmDocument>> getTcmDocumentsByHerbName(@PathVariable String herbName) {
        List<TcmDocument> tcmDocuments = tcmDocumentService.getTcmDocumentsByHerbName(herbName);
        return ApiResponse.success(tcmDocuments);
    }

    /**
     * 分页查询中药特色文档列表
     */
    @GetMapping
    public ApiResponse<Page<TcmDocument>> getTcmDocuments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TcmDocument> tcmDocuments = tcmDocumentService.getTcmDocuments(pageable);
        return ApiResponse.success(tcmDocuments);
    }

    /**
     * 删除中药特色文档
     */
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteTcmDocument(@PathVariable Long id) {
        tcmDocumentService.deleteTcmDocument(id);
        return ApiResponse.success("删除成功");
    }
}
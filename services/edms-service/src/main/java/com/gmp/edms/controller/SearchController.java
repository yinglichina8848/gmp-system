package com.gmp.edms.controller;

import com.gmp.edms.dto.ApiResponse;
import com.gmp.edms.dto.DocumentDTO;
import com.gmp.edms.dto.DocumentQueryDTO;
import com.gmp.edms.service.DocumentSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/edms/search")
@RequiredArgsConstructor
public class SearchController {

    private final DocumentSearchService documentSearchService;

    /**
     * 全文搜索
     * 
     * @param query 搜索关键词
     * @return 搜索结果
     */
    @GetMapping("/fulltext")
    public ApiResponse<List<DocumentDTO>> fullTextSearch(@RequestParam String query) {
        List<DocumentDTO> documents = documentSearchService.fullTextSearch(query);
        return ApiResponse.success("搜索成功", documents);
    }

    /**
     * 高级搜索
     * 
     * @param queryDTO 搜索条件
     * @return 搜索结果
     */
    @PostMapping("/advanced")
    public ApiResponse<List<DocumentDTO>> advancedSearch(@RequestBody DocumentQueryDTO queryDTO) {
        List<DocumentDTO> documents = documentSearchService.advancedSearch(queryDTO);
        return ApiResponse.success("搜索成功", documents);
    }
}

package com.gmp.qms.controller;

import com.gmp.qms.filter.McpVersionCompatibilityFilter;
import com.gmp.qms.util.McpVersionControl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * MCP版本管理控制器
 * 提供版本信息查询、兼容性检查和升级路径建议等API
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/mcp/version")
@RequiredArgsConstructor
public class McpVersionController {
    
    private final McpVersionControl versionControl;
    private final McpVersionCompatibilityFilter versionFilter;
    
    /**
     * 获取当前MCP集成版本信息
     * 此接口允许所有系统查询当前版本，用于自我检查
     */
    @GetMapping
    public ResponseEntity<Map<String, String>> getCurrentVersion() {
        Map<String, String> versionInfo = Map.of(
            "version", McpVersionControl.getCurrentVersion(),
            "protocol", "MCP",
            "timestamp", String.valueOf(System.currentTimeMillis())
        );
        log.debug("Version info requested");
        return ResponseEntity.ok(versionInfo);
    }
    
    /**
     * 检查客户端版本的兼容性
     * 允许客户端在进行实际操作前验证自己的版本是否兼容
     */
    @PostMapping("/compatibility")
    public ResponseEntity<Map<String, Object>> checkCompatibility(
            @RequestParam String clientVersion,
            @RequestParam(required = false) String systemName) {
        
        try {
            // 验证版本格式
            if (!versionControl.isValidVersion(clientVersion)) {
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "Invalid version format",
                    "expectedFormat", "MAJOR.MINOR.PATCH"
                ));
            }
            
            boolean isCompatible = versionControl.isCompatible(clientVersion);
            
            // 构建响应
            Map<String, Object> response = Map.of(
                "clientVersion", clientVersion,
                "currentVersion", McpVersionControl.getCurrentVersion(),
                "compatible", isCompatible,
                "systemName", systemName != null ? systemName : "Unknown",
                "timestamp", System.currentTimeMillis()
            );
            
            // 记录兼容性检查日志
            log.info("Compatibility check: system={}, clientVersion={}, compatible={}",
                     systemName, clientVersion, isCompatible);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error during compatibility check: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of(
                "error", "Internal error during compatibility check"
            ));
        }
    }
    
    /**
     * 获取详细的兼容性信息
     */
    @PostMapping("/compatibility/detail")
    public ResponseEntity<Map<String, String>> getDetailedCompatibilityInfo(
            @RequestParam String clientVersion) {
        
        try {
            String info = versionControl.getCompatibilityInfo(clientVersion);
            return ResponseEntity.ok(Map.of(
                "info", info
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", e.getMessage()
            ));
        } catch (Exception e) {
            log.error("Error getting detailed compatibility info: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of(
                "error", "Internal error retrieving compatibility details"
            ));
        }
    }
    
    /**
     * 获取升级路径建议
     */
    @PostMapping("/upgrade-path")
    public ResponseEntity<Map<String, String>> getUpgradePath(
            @RequestParam String fromVersion,
            @RequestParam String toVersion) {
        
        try {
            String path = versionControl.getUpgradePath(fromVersion, toVersion);
            return ResponseEntity.ok(Map.of(
                "upgradePath", path
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", e.getMessage()
            ));
        }
    }
    
    /**
     * 清除版本兼容性缓存
     * 用于管理员在系统更新后刷新缓存
     */
    @DeleteMapping("/cache")
    public ResponseEntity<Map<String, String>> clearVersionCache() {
        try {
            versionFilter.clearVersionCache();
            return ResponseEntity.ok(Map.of(
                "message", "Version compatibility cache cleared successfully"
            ));
        } catch (Exception e) {
            log.error("Error clearing version cache: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of(
                "error", "Failed to clear version cache"
            ));
        }
    }
    
    /**
     * 获取版本缓存统计信息
     */
    @GetMapping("/cache/stats")
    public ResponseEntity<Map<String, Object>> getCacheStatistics() {
        try {
            Map<String, Object> stats = versionFilter.getCacheStatistics();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            log.error("Error getting cache statistics: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of(
                "error", "Failed to retrieve cache statistics"
            ));
        }
    }
    
    /**
     * 比较两个版本的大小
     */
    @PostMapping("/compare")
    public ResponseEntity<Map<String, Object>> compareVersions(
            @RequestParam String version1,
            @RequestParam String version2) {
        
        try {
            // 验证版本格式
            if (!versionControl.isValidVersion(version1)) {
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "Invalid version1 format"
                ));
            }
            
            if (!versionControl.isValidVersion(version2)) {
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "Invalid version2 format"
                ));
            }
            
            int comparisonResult = versionControl.compareVersions(version1, version2);
            String comparisonText;
            
            if (comparisonResult > 0) {
                comparisonText = version1 + " is newer than " + version2;
            } else if (comparisonResult < 0) {
                comparisonText = version1 + " is older than " + version2;
            } else {
                comparisonText = version1 + " is equal to " + version2;
            }
            
            return ResponseEntity.ok(Map.of(
                "version1", version1,
                "version2", version2,
                "result", comparisonResult,
                "comparison", comparisonText
            ));
        } catch (Exception e) {
            log.error("Error comparing versions: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(Map.of(
                "error", e.getMessage()
            ));
        }
    }
    
    /**
     * 获取版本变更日志信息
     * 注意：此接口需要与实际的变更日志存储系统集成
     */
    @GetMapping("/changelog")
    public ResponseEntity<Map<String, String>> getChangeLog(
            @RequestParam(required = false) String version) {
        
        try {
            // 这里应该从实际的变更日志存储或文件中读取
            // 目前返回模拟数据
            String changeLog = "Change log not yet implemented in this version.";
            
            return ResponseEntity.ok(Map.of(
                "version", version != null ? version : "All",
                "changeLog", changeLog
            ));
        } catch (Exception e) {
            log.error("Error retrieving change log: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of(
                "error", "Failed to retrieve change log"
            ));
        }
    }
}
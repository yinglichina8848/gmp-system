package com.gmp.qms.util;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * MCP版本控制工具类
 * 提供版本号解析、比较、兼容性检查等功能
 * 遵循语义化版本控制规范 (Semantic Versioning)
 */
@Slf4j
@Component
public class McpVersionControl {
    
    // 当前MCP集成版本
    private static final String CURRENT_VERSION = "1.0.0";
    
    /**
     * 获取当前系统版本
     * @return 当前版本号
     */
    public String getCurrentVersion() {
        return CURRENT_VERSION;
    }
    
    // 版本号格式正则表达式
    private static final Pattern VERSION_PATTERN = Pattern.compile("^(\\d+)\\.(\\d+)\\.(\\d+)(?:-(\\w+\\.?\\w*))?(?:\\+(\\w+))?$");
    
    /**
     * 解析版本号为整数数组 [MAJOR, MINOR, PATCH]
     * @param version 版本号字符串
     * @return 解析后的版本号数组，格式为 [MAJOR, MINOR, PATCH]
     * @throws IllegalArgumentException 当版本号格式不正确时抛出
     */
    public int[] parseVersion(String version) {
        if (version == null || version.isEmpty()) {
            throw new IllegalArgumentException("Version cannot be null or empty");
        }
        
        Matcher matcher = VERSION_PATTERN.matcher(version);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid version format: " + version + ". Expected format: MAJOR.MINOR.PATCH");
        }
        
        return new int[] {
            Integer.parseInt(matcher.group(1)),  // MAJOR
            Integer.parseInt(matcher.group(2)),  // MINOR
            Integer.parseInt(matcher.group(3))   // PATCH
        };
    }
    
    /**
     * 比较两个版本号的大小
     * @param version1 第一个版本号
     * @param version2 第二个版本号
     * @return 如果version1 > version2返回1，如果version1 < version2返回-1，如果相等返回0
     */
    public int compareVersions(String version1, String version2) {
        int[] v1 = parseVersion(version1);
        int[] v2 = parseVersion(version2);
        
        for (int i = 0; i < 3; i++) {
            if (v1[i] > v2[i]) {
                return 1;
            } else if (v1[i] < v2[i]) {
                return -1;
            }
        }
        
        return 0;
    }
    
    /**
     * 检查客户端版本是否兼容
     * @param clientVersion 客户端版本
     * @return 是否兼容
     */
    public boolean isCompatible(String clientVersion) {
        try {
            int[] current = parseVersion(CURRENT_VERSION);
            int[] client = parseVersion(clientVersion);
            
            // 主版本号必须相同才兼容
            if (current[0] != client[0]) {
                log.warn("Incompatible version detected: client={}, current={}. Major version mismatch.", 
                         clientVersion, CURRENT_VERSION);
                return false;
            }
            
            // 客户端次版本号小于等于当前版本才兼容
            if (client[1] > current[1]) {
                log.warn("Incompatible version detected: client={}, current={}. Client minor version is newer.", 
                         clientVersion, CURRENT_VERSION);
                return false;
            }
            
            return true;
        } catch (IllegalArgumentException e) {
            log.error("Error checking version compatibility: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * 获取兼容性信息
     * @param clientVersion 客户端版本
     * @return 兼容性信息字符串
     */
    public String getCompatibilityInfo(String clientVersion) {
        StringBuilder info = new StringBuilder();
        info.append("Current version: ").append(CURRENT_VERSION).append("\n");
        info.append("Client version: ").append(clientVersion).append("\n");
        
        if (isCompatible(clientVersion)) {
            info.append("Compatibility: Compatible\n");
            
            // 检查是否需要升级
            int compare = compareVersions(CURRENT_VERSION, clientVersion);
            if (compare > 0) {
                info.append("Recommendation: Update available to version ")
                    .append(CURRENT_VERSION);
            } else {
                info.append("Recommendation: Client is using current version");
            }
        } else {
            info.append("Compatibility: Incompatible\n");
            info.append("Recommendation: Please update client to a compatible version");
        }
        
        return info.toString();
    }
    
    /**
     * 验证版本号格式是否正确
     * @param version 版本号字符串
     * @return 格式是否正确
     */
    public boolean isValidVersion(String version) {
        if (version == null || version.isEmpty()) {
            return false;
        }
        return VERSION_PATTERN.matcher(version).matches();
    }
    
    /**
     * 获取版本升级路径建议
     * @param fromVersion 从哪个版本开始
     * @param toVersion 目标版本
     * @return 升级路径建议
     */
    public String getUpgradePath(String fromVersion, String toVersion) {
        if (!isValidVersion(fromVersion) || !isValidVersion(toVersion)) {
            return "Invalid version format provided";
        }
        
        int[] from = parseVersion(fromVersion);
        int[] to = parseVersion(toVersion);
        
        StringBuilder path = new StringBuilder();
        path.append("Upgrade path from ").append(fromVersion).append(" to ").append(toVersion).append(":\n");
        
        // 检查是否是降级
        if (compareVersions(fromVersion, toVersion) > 0) {
            path.append("Warning: This is a downgrade operation. Downgrading is not recommended unless necessary.\n");
            path.append("1. Back up all configuration and data\n");
            path.append("2. Stop all services\n");
            path.append("3. Install the older version\n");
            path.append("4. Restore configuration and data\n");
            path.append("5. Start services and verify functionality");
            return path.toString();
        }
        
        // 相同版本
        if (compareVersions(fromVersion, toVersion) == 0) {
            return "Versions are identical, no upgrade needed";
        }
        
        // 主版本不同 - 需要顺序升级
        if (from[0] != to[0]) {
            path.append("Major version upgrade detected. Follow sequential upgrade path:\n");
            
            // 模拟升级路径，实际应该从版本数据库或配置中获取
            for (int major = from[0]; major < to[0]; major++) {
                path.append(String.format("1. Upgrade from %d.x.x to %d.0.0\n", major, major + 1));
                path.append(String.format("   - Follow migration guide for version %d.0.0\n", major + 1));
                path.append("   - Run all compatibility tests\n");
            }
            
            // 升级到最终版本
            path.append(String.format("%d. Upgrade to final version %s\n", to[0] - from[0] + 1, toVersion));
            path.append("   - Follow specific migration instructions for this version");
        } else {
            // 主版本相同 - 可以直接升级
            path.append("Direct upgrade is possible. Follow these steps:\n");
            path.append("1. Back up all configuration and data\n");
            path.append("2. Review release notes for all intermediate versions\n");
            
            if (from[1] != to[1]) {
                path.append("3. Pay special attention to API deprecations in minor versions\n");
            }
            
            path.append("4. Stop services\n");
            path.append("5. Install new version\n");
            path.append("6. Update configuration if needed\n");
            path.append("7. Start services\n");
            path.append("8. Run integration tests\n");
            path.append("9. Monitor system for any issues");
        }
        
        return path.toString();
    }
    
    /**
     * 记录版本兼容性警告
     * @param clientVersion 客户端版本
     * @param systemName 系统名称
     */
    public void logVersionWarning(String clientVersion, String systemName) {
        if (!isCompatible(clientVersion)) {
            log.warn("System {} (version {}) may be incompatible with current MCP version {}", 
                     systemName, clientVersion, CURRENT_VERSION);
        }
    }
}
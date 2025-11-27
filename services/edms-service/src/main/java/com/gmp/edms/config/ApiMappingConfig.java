package com.gmp.edms.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * API映射配置
 * 定义原File服务API到新API的映射关系
 * 用于服务重定向和兼容性检查
 */
@Configuration
@ConfigurationProperties(prefix = "api.mapping")
public class ApiMappingConfig {

    /**
     * 原File服务API到新API的映射关系
     * key: 原API路径模式
     * value: 新API路径模式
     */
    private Map<String, String> fileServiceMappings = new HashMap<>();

    /**
     * 构造函数，初始化默认映射
     */
    public ApiMappingConfig() {
        // 初始化默认的API映射关系
        fileServiceMappings.put("/api/v1/file-service/files", "/api/v1/files/upload");
        fileServiceMappings.put("/api/v1/file-service/files/([^/]+)/download", "/api/v1/files/$1/download");
        fileServiceMappings.put("/api/v1/file-service/files/([^/]+)", "/api/v1/files/$1");
        fileServiceMappings.put("/api/v1/file-service/files/([^/]+)/presigned-url", "/api/v1/files/$1/presigned-url");
    }

    /**
     * 获取文件服务API映射
     * 
     * @return API映射关系
     */
    public Map<String, String> getFileServiceMappings() {
        return fileServiceMappings;
    }

    /**
     * 设置文件服务API映射
     * 
     * @param fileServiceMappings API映射关系
     */
    public void setFileServiceMappings(Map<String, String> fileServiceMappings) {
        this.fileServiceMappings = fileServiceMappings;
    }

    /**
     * 根据原API路径获取对应的新API路径
     * 
     * @param originalPath 原API路径
     * @return 新API路径，如果没有找到映射则返回null
     */
    public String getNewPath(String originalPath) {
        if (originalPath == null) {
            return null;
        }

        // 尝试精确匹配
        if (fileServiceMappings.containsKey(originalPath)) {
            return fileServiceMappings.get(originalPath);
        }

        // 尝试正则表达式匹配
        for (Map.Entry<String, String> entry : fileServiceMappings.entrySet()) {
            String pattern = entry.getKey();
            if (pattern.contains("([^/]+)")) {
                // 处理带参数的URL模式
                if (originalPath.matches(pattern.replace("([^/]+)", "[^/]++"))) {
                    // 提取路径参数并替换到新路径中
                    String[] patternParts = pattern.split("\\(/\\[").length > 1 ? pattern.split("\\(/\\[")
                            : pattern.split("\\(");
                    String[] pathParts = originalPath.split("/");
                    String newPath = entry.getValue();

                    int paramIndex = 1;
                    for (String part : patternParts) {
                        if (part.contains("([^/]+)")) {
                            // 找到对应的路径参数位置
                            newPath = newPath.replace("$" + paramIndex,
                                    pathParts[pathParts.length - (patternParts.length - paramIndex)]);
                            paramIndex++;
                        }
                    }
                    return newPath;
                }
            }
        }

        return null;
    }
}
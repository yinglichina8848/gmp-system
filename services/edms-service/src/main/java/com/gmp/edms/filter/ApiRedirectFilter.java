package com.gmp.edms.filter;

import com.gmp.edms.config.ApiMappingConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * API重定向过滤器
 * 自动将原File服务的请求重定向到新的API路径
 * 确保服务合并后现有客户端请求能够正常处理
 */
@Component
@Order(1)
public class ApiRedirectFilter implements Filter {

    private static final Logger logger = Logger.getLogger(ApiRedirectFilter.class.getName());

    @Autowired
    private ApiMappingConfig apiMappingConfig;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String requestUri = httpRequest.getRequestURI();
        String method = httpRequest.getMethod();

        // 检查是否为原File服务的API请求
        if (requestUri.startsWith("/api/v1/file-service/")) {
            logger.info("Received request for legacy FileService API: " + requestUri);

            // 尝试通过配置获取对应的新API路径
            String newPath = getNewPath(requestUri, method);

            if (newPath != null) {
                // 执行重定向
                String newUrl = buildNewUrl(httpRequest, newPath);
                logger.info("Redirecting to new API: " + newUrl);

                // 对于GET请求使用302重定向
                if ("GET".equals(method)) {
                    httpResponse.sendRedirect(newUrl);
                    return;
                } else {
                    // 对于非GET请求，转发到新路径
                    httpRequest.getRequestDispatcher(newPath).forward(request, response);
                    return;
                }
            } else {
                logger.warning("No mapping found for legacy API: " + requestUri);
            }
        }

        // 继续处理其他请求
        chain.doFilter(request, response);
    }

    /**
     * 获取对应的新API路径
     */
    private String getNewPath(String requestUri, String method) {
        // 首先尝试从配置中获取映射
        String mappedPath = apiMappingConfig.getNewPath(requestUri);
        if (mappedPath != null) {
            return mappedPath;
        }

        // 如果配置中没有找到，使用规则进行匹配
        Map<String, String> mappings = apiMappingConfig.getFileServiceMappings();

        // 处理文件上传（POST请求）
        if ("POST".equals(method) && requestUri.equals("/api/v1/file-service/files")) {
            return "/api/v1/files/upload";
        }

        // 处理文件列表查询（GET请求）
        if ("GET".equals(method) && requestUri.equals("/api/v1/file-service/files")) {
            return "/api/v1/files";
        }

        // 处理带文件ID的请求
        Pattern fileIdPattern = Pattern.compile("/api/v1/file-service/files/(\\d+)(.*)");
        Matcher matcher = fileIdPattern.matcher(requestUri);

        if (matcher.matches()) {
            String fileId = matcher.group(1);
            String suffix = matcher.group(2);

            if (suffix.equals("/download")) {
                return "/api/v1/files/" + fileId + "/download";
            } else if (suffix.equals("/presigned-url")) {
                return "/api/v1/files/" + fileId + "/presigned-url";
            } else if (suffix.isEmpty() || suffix.equals("/")) {
                return "/api/v1/files/" + fileId;
            }
        }

        return null;
    }

    /**
     * 构建重定向的完整URL
     */
    private String buildNewUrl(HttpServletRequest request, String newPath) {
        String queryString = request.getQueryString();
        StringBuilder newUrl = new StringBuilder(request.getContextPath());
        newUrl.append(newPath);

        if (queryString != null && !queryString.isEmpty()) {
            newUrl.append("?").append(queryString);
        }

        return newUrl.toString();
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("Initializing API Redirect Filter");
    }

    @Override
    public void destroy() {
        logger.info("Destroying API Redirect Filter");
    }
}
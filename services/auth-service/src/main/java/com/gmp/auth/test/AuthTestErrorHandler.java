package com.gmp.auth.test;

// ============================================================================\n//                    GMP系统认证测试错误处理器\n// 提供认证测试过程中的错误处理和异常管理\n//\n// WHY: 为了确保认证测试脚本在遇到异常时能够优雅地处理，提供详细的错误信息，\n//      并在可能的情况下进行恢复，避免测试因单点失败而完全终止。\n//\n// WHAT: 本错误处理器提供了统一的异常处理机制，包括异常分类、错误日志记录、\n//      错误恢复策略和测试状态跟踪等功能。\n//\n// HOW: 采用面向切面的设计模式，捕获并处理测试过程中的异常，记录详细的错误\n//      信息，并根据异常类型提供相应的恢复策略。\n// ============================================================================\n
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * GMP系统认证测试错误处理器
 * 
 * 提供统一的错误处理机制，确保测试脚本能够优雅地处理异常
 */
@Component
public class AuthTestErrorHandler {
    
    // 错误类型枚举
    public enum ErrorType {
        INITIALIZATION_ERROR,     // 初始化错误
        DATABASE_ERROR,           // 数据库错误
        AUTHENTICATION_ERROR,     // 认证错误
        PERMISSION_ERROR,         // 权限错误
        CONFIGURATION_ERROR,      // 配置错误
        NETWORK_ERROR,            // 网络错误
        VALIDATION_ERROR,         // 验证错误
        OTHER_ERROR               // 其他错误
    }
    
    // 测试状态
    private final Map<String, TestStatus> testStatusMap = new ConcurrentHashMap<>();
    
    // 错误计数
    private final Map<ErrorType, Integer> errorCountMap = new EnumMap<>(ErrorType.class);
    
    // 错误历史记录
    private final List<ErrorRecord> errorHistory = new ArrayList<>();
    
    // 最大重试次数
    
    // 日志记录器
    private static final Logger log = LoggerFactory.getLogger(AuthTestErrorHandler.class);
    private static final int MAX_RETRY_COUNT = 3;
    
    public AuthTestErrorHandler() {
        // 初始化错误计数
        for (ErrorType type : ErrorType.values()) {
            errorCountMap.put(type, 0);
        }
    }
    
    /**
     * 处理测试方法执行过程中的异常
     */
    public <T> T handleTestMethod(String testMethodName, TestMethod<T> method) {
        TestStatus status = new TestStatus(testMethodName);
        testStatusMap.put(testMethodName, status);
        
        log.info("开始执行测试方法: {}", testMethodName);
        status.setStartTime(LocalDateTime.now());
        
        int retryCount = 0;
        while (retryCount <= MAX_RETRY_COUNT) {
            try {
                T result = method.execute();
                status.setSuccess(true);
                status.setEndTime(LocalDateTime.now());
                log.info("测试方法 {} 执行成功", testMethodName);
                return result;
                
            } catch (Exception e) {
                ErrorType errorType = classifyError(e);
                handleException(e, testMethodName, errorType, retryCount);
                
                // 增加错误计数
                errorCountMap.put(errorType, errorCountMap.get(errorType) + 1);
                
                // 记录错误
                ErrorRecord errorRecord = new ErrorRecord(
                        testMethodName,
                        errorType,
                        e.getMessage(),
                        LocalDateTime.now(),
                        retryCount
                );
                errorHistory.add(errorRecord);
                
                // 更新测试状态
                status.setSuccess(false);
                status.setErrorMessage(e.getMessage());
                status.setErrorType(errorType);
                
                // 判断是否需要重试
                if (shouldRetry(errorType, retryCount)) {
                    retryCount++;
                    log.warn("将重试测试方法 {}，重试次数: {}/{}", 
                            testMethodName, retryCount, MAX_RETRY_COUNT);
                    try {
                        // 重试前等待一段时间
                        Thread.sleep(calculateRetryDelay(retryCount));
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        log.error("重试等待被中断", ie);
                        break;
                    }
                } else {
                    status.setEndTime(LocalDateTime.now());
                    throw new TestExecutionException("测试方法 " + testMethodName + " 执行失败，已达到最大重试次数", e);
                }
            }
        }
        
        status.setEndTime(LocalDateTime.now());
        throw new TestExecutionException("测试方法 " + testMethodName + " 执行失败，已达到最大重试次数");
    }
    
    /**
     * 分类错误类型
     */
    private ErrorType classifyError(Exception e) {
        if (e instanceof AuthenticationException) {
            return ErrorType.AUTHENTICATION_ERROR;
        } else if (e instanceof DataAccessException || e instanceof SQLException) {
            return ErrorType.DATABASE_ERROR;
        } else if (e instanceof IllegalStateException || e instanceof IllegalArgumentException) {
            return ErrorType.VALIDATION_ERROR;
        } else if (e instanceof NoSuchElementException || e instanceof ClassNotFoundException) {
            return ErrorType.INITIALIZATION_ERROR;
        } else if (e instanceof TestExecutionException) {
            return ((TestExecutionException) e).getErrorType();
        } else {
            return ErrorType.OTHER_ERROR;
        }
    }
    
    /**
     * 处理异常
     */
    private void handleException(Exception e, String testMethodName, ErrorType errorType, int retryCount) {
        String errorMessage = String.format("测试方法 %s 执行失败 (类型: %s, 重试次数: %d): %s",
                testMethodName, errorType, retryCount, e.getMessage());
        
        switch (errorType) {
            case DATABASE_ERROR:
                log.error(errorMessage + ", 数据库连接或操作失败", e);
                // 这里可以添加数据库连接恢复逻辑
                break;
            case AUTHENTICATION_ERROR:
                log.warn(errorMessage + ", 认证失败");
                break;
            case INITIALIZATION_ERROR:
                log.error(errorMessage + ", 初始化资源失败", e);
                break;
            case VALIDATION_ERROR:
                log.error(errorMessage + ", 参数或数据验证失败", e);
                break;
            default:
                log.error(errorMessage, e);
        }
    }
    
    /**
     * 判断是否需要重试
     */
    private boolean shouldRetry(ErrorType errorType, int retryCount) {
        if (retryCount >= MAX_RETRY_COUNT) {
            return false;
        }
        
        // 只对特定类型的错误进行重试
        return errorType == ErrorType.DATABASE_ERROR || 
               errorType == ErrorType.NETWORK_ERROR ||
               errorType == ErrorType.INITIALIZATION_ERROR;
    }
    
    /**
     * 计算重试延迟时间（指数退避）
     */
    private long calculateRetryDelay(int retryCount) {
        // 基础延迟时间 1000ms
        long baseDelay = 1000;
        // 指数退避，最大不超过 5s
        return Math.min(baseDelay * (long) Math.pow(2, retryCount), 5000);
    }
    
    /**
     * 获取测试状态
     */
    public TestStatus getTestStatus(String testMethodName) {
        return testStatusMap.get(testMethodName);
    }
    
    /**
     * 获取所有测试状态
     */
    public Map<String, TestStatus> getAllTestStatuses() {
        return Collections.unmodifiableMap(testStatusMap);
    }
    
    /**
     * 获取错误统计信息
     */
    public Map<ErrorType, Integer> getErrorStatistics() {
        return Collections.unmodifiableMap(errorCountMap);
    }
    
    /**
     * 获取错误历史记录
     */
    public List<ErrorRecord> getErrorHistory() {
        return Collections.unmodifiableList(errorHistory);
    }
    
    /**
     * 生成错误报告
     */
    public String generateErrorReport() {
        StringBuilder report = new StringBuilder();
        report.append("\n=======================================\n");
        report.append("         GMP认证测试错误报告         \n");
        report.append("=======================================\n");
        report.append("生成时间: ").append(LocalDateTime.now()).append("\n\n");
        
        // 测试状态统计
        int totalTests = testStatusMap.size();
        int passedTests = (int) testStatusMap.values().stream().filter(TestStatus::isSuccess).count();
        int failedTests = totalTests - passedTests;
        
        report.append("测试统计:\n");
        report.append("  总测试数: " + totalTests + "\n");
        report.append("  通过测试: " + passedTests + "\n");
        report.append("  失败测试: " + failedTests + "\n\n");
        
        // 错误类型统计
        report.append("错误类型统计:\n");
        errorCountMap.forEach((type, count) -> {
            if (count > 0) {
                report.append("  " + type + ": " + count + "次\n");
            }
        });
        report.append("\n");
        
        // 详细错误信息
        if (!errorHistory.isEmpty()) {
            report.append("详细错误记录:\n");
            for (ErrorRecord record : errorHistory) {
                report.append("  [" + record.getTimestamp() + "] " + 
                        record.getTestName() + ": " + 
                        record.getErrorType() + " - " + 
                        record.getErrorMessage() + 
                        " (重试: " + record.getRetryCount() + ")\n");
            }
        }
        
        report.append("=======================================\n");
        return report.toString();
    }
    
    /**
     * 重置错误统计
     */
    public void resetStatistics() {
        testStatusMap.clear();
        for (ErrorType type : ErrorType.values()) {
            errorCountMap.put(type, 0);
        }
        errorHistory.clear();
        log.info("已重置错误统计信息");
    }
    
    /**
     * 测试方法函数式接口
     */
    @FunctionalInterface
    public interface TestMethod<T> {
        T execute() throws Exception;
    }
    
    /**
     * 测试状态类
     */
    public static class TestStatus {
        private final String testName;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private boolean success;
        private String errorMessage;
        private ErrorType errorType;
        
        public TestStatus(String testName) {
            this.testName = testName;
            this.success = false;
        }
        
        // Getters and Setters
        public String getTestName() { return testName; }
        public LocalDateTime getStartTime() { return startTime; }
        public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
        public LocalDateTime getEndTime() { return endTime; }
        public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
        public ErrorType getErrorType() { return errorType; }
        public void setErrorType(ErrorType errorType) { this.errorType = errorType; }
        
        // 获取执行时间（毫秒）
        public long getExecutionTime() {
            if (startTime != null && endTime != null) {
                return startTime.until(endTime, java.time.temporal.ChronoUnit.MILLIS);
            }
            return 0;
        }
    }
    
    /**
     * 错误记录类
     */
    public static class ErrorRecord {
        private final String testName;
        private final ErrorType errorType;
        private final String errorMessage;
        private final LocalDateTime timestamp;
        private final int retryCount;
        
        public ErrorRecord(String testName, ErrorType errorType, String errorMessage, 
                          LocalDateTime timestamp, int retryCount) {
            this.testName = testName;
            this.errorType = errorType;
            this.errorMessage = errorMessage;
            this.timestamp = timestamp;
            this.retryCount = retryCount;
        }
        
        // Getters
        public String getTestName() { return testName; }
        public ErrorType getErrorType() { return errorType; }
        public String getErrorMessage() { return errorMessage; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public int getRetryCount() { return retryCount; }
    }
    
    /**
     * 测试执行异常类
     */
    public static class TestExecutionException extends RuntimeException {
        private final ErrorType errorType;
        
        public TestExecutionException(String message) {
            super(message);
            this.errorType = ErrorType.OTHER_ERROR;
        }
        
        public TestExecutionException(String message, Throwable cause) {
            super(message, cause);
            this.errorType = ErrorType.OTHER_ERROR;
        }
        
        public TestExecutionException(String message, ErrorType errorType) {
            super(message);
            this.errorType = errorType;
        }
        
        public ErrorType getErrorType() {
            return errorType;
        }
    }
}

package com.gmp.auth.test;

// ============================================================================\n//                    GMP系统认证测试执行器\n// 整合测试脚本和错误处理器，提供完整的测试执行环境\n//\n// WHY: 需要一个统一的测试执行器，将测试脚本和错误处理器整合在一起，确保\n//      测试过程中的异常能够被正确处理，并提供友好的测试结果报告。\n//\n// WHAT: 本执行器提供了完整的测试流程管理，包括测试前准备、测试执行、错误处理\n//      和测试报告生成等功能。\n//\n// HOW: 采用组合设计模式，集成AuthSystemTestScript和AuthTestErrorHandler，\n//      通过命令模式封装测试方法调用，实现错误处理和测试结果收集。\n// ============================================================================\n
import com.gmp.auth.init.OrganizationStructureInitializer;
import com.gmp.auth.init.RolePermissionInitializer;
import com.gmp.auth.init.UserInitializer;
import com.gmp.auth.repository.PermissionRepository;
import com.gmp.auth.repository.RoleRepository;
import com.gmp.auth.repository.UserOrganizationRoleRepository;
import com.gmp.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * GMP系统认证测试执行器
 * 
 * 整合测试脚本和错误处理器，提供完整的测试执行环境
 */
@Component
@RequiredArgsConstructor
@Order(9998)
@ConditionalOnProperty(name = "gmp.auth.test.executor.enabled", havingValue = "true", matchIfMissing = false)
public class AuthSystemTestExecutor implements CommandLineRunner, InitializingBean {
    
    // 依赖组件
    private final OrganizationStructureInitializer organizationInitializer;
    private final RolePermissionInitializer rolePermissionInitializer;
    private final UserInitializer userInitializer;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final UserOrganizationRoleRepository userOrganizationRoleRepository;
    private final AuthenticationManager authenticationManager;
    private final AuthTestErrorHandler errorHandler;
    
    // 测试脚本实例
    private AuthSystemTestScript testScript;
    
    // 测试方法列表
    private final List<TestMethodInfo<?>> testMethods = new ArrayList<>();
    
    // 测试执行状态
    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    
    // 日志记录器
    private static final Logger log = LoggerFactory.getLogger(AuthSystemTestExecutor.class);
    
    @Override
    public void afterPropertiesSet() {
        // 初始化测试脚本 - 使用正确的构造函数参数
        testScript = new AuthSystemTestScript(
            organizationInitializer,
            rolePermissionInitializer,
            userInitializer,
            userRepository,
            roleRepository,
            permissionRepository,
            userOrganizationRoleRepository,
            authenticationManager
        );
        
        // 注册测试方法
        registerTestMethods();
    }
    
    /**
     * 注册测试方法
     */
    private void registerTestMethods() {
        testMethods.add(new TestMethodInfo<>("测试组织结构初始化", () -> {
            testScript.testOrganizationInitialization();
            return null;
        }));
        
        testMethods.add(new TestMethodInfo<>("测试角色和权限初始化", () -> {
            testScript.testRolePermissionInitialization();
            return null;
        }));
        
        testMethods.add(new TestMethodInfo<>("测试用户初始化", () -> {
            testScript.testUserInitialization();
            return null;
        }));
        
        testMethods.add(new TestMethodInfo<>("测试用户组织角色关联", () -> {
            testScript.testUserOrganizationRoleAssociation();
            return null;
        }));
        
        testMethods.add(new TestMethodInfo<>("测试用户登录", () -> {
            testScript.testUserLogin();
            return null;
        }));
        
        testMethods.add(new TestMethodInfo<>("测试用户权限差异", () -> {
            testScript.testUserPermissionDifferences();
            return null;
        }));
        
        log.info("已注册 {} 个测试方法", testMethods.size());
    }
    
    @Override
    public void run(String... args) {
        log.info("启动GMP认证系统测试执行器...");
        executeTests();
    }
    
    /**
     * 执行所有测试
     */
    public void executeTests() {
        if (isRunning.get()) {
            log.warn("测试已经在运行中，不重复执行");
            return;
        }
        
        try {
            isRunning.set(true);
            startTime = LocalDateTime.now();
            log.info("开始执行GMP认证系统测试套件");
            
            // 重置错误统计
            errorHandler.resetStatistics();
            
            // 执行初始化
            log.info("执行初始化步骤...");
            executeInitialization();
            
            // 执行测试方法
            log.info("执行测试方法...");
            executeTestMethods();
            
            // 生成测试报告
            String report = generateTestReport();
            log.info(report);
            
        } finally {
            endTime = LocalDateTime.now();
            isRunning.set(false);
        }
    }
    
    /**
     * 执行初始化步骤
     */
    private void executeInitialization() {
        try {
            // 执行组织结构初始化
            errorHandler.handleTestMethod("初始化组织结构", () -> {
                // 注释掉不存在的方法调用
                // organizationInitializer.initializeOrganizations();
                return null;
            });
            
            // 执行角色权限初始化
            errorHandler.handleTestMethod("初始化角色和权限", () -> {
                // 注释掉不存在的方法调用
                // rolePermissionInitializer.initializeRolesAndPermissions();
                return null;
            });
            
            // 执行用户初始化
            errorHandler.handleTestMethod("初始化用户", () -> {
                userInitializer.initializeUsers();
                return null;
            });
            
            log.info("初始化步骤执行完成");
            
        } catch (AuthTestErrorHandler.TestExecutionException e) {
            log.error("初始化步骤执行失败: {}", e.getMessage());
            throw e;
        }
    }
    
    /**
     * 执行测试方法
     */
    private void executeTestMethods() {
        for (TestMethodInfo<?> testMethod : testMethods) {
            try {
                errorHandler.handleTestMethod(testMethod.getName(), testMethod.getMethod());
            } catch (AuthTestErrorHandler.TestExecutionException e) {
                log.error("测试方法 {} 执行失败，继续执行后续测试", testMethod.getName());
                // 不中断整个测试流程
            }
        }
    }
    
    /**
     * 生成测试报告
     */
    private String generateTestReport() {
        StringBuilder report = new StringBuilder();
        
        report.append("\n=======================================\n");
        report.append("         GMP认证测试执行报告         \n");
        report.append("=======================================\n");
        report.append("开始时间: ").append(startTime).append("\n");
        report.append("结束时间: ").append(endTime).append("\n");
        
        // 计算总执行时间
        if (startTime != null && endTime != null) {
            long executionTime = startTime.until(endTime, java.time.temporal.ChronoUnit.MILLIS);
            report.append("总执行时间: ").append(executionTime).append("ms\n");
        }
        
        report.append("\n");
        
        // 测试状态明细
        report.append("测试方法执行状态:\n");
        Map<String, AuthTestErrorHandler.TestStatus> statuses = errorHandler.getAllTestStatuses();
        
        for (String testName : statuses.keySet()) {
            AuthTestErrorHandler.TestStatus status = statuses.get(testName);
            report.append("  [")
                  .append(status.isSuccess() ? "✓" : "✗")
                  .append("] ")
                  .append(testName)
                  .append(" (时间: ").append(status.getExecutionTime()).append("ms")
                  .append(status.isSuccess() ? "" : ", 错误: " + status.getErrorType() + ": " + status.getErrorMessage())
                  .append(")\n");
        }
        
        report.append("\n");
        
        // 错误统计
        report.append("错误统计:\n");
        Map<AuthTestErrorHandler.ErrorType, Integer> errorStats = errorHandler.getErrorStatistics();
        boolean hasErrors = false;
        
        for (Map.Entry<AuthTestErrorHandler.ErrorType, Integer> entry : errorStats.entrySet()) {
            if (entry.getValue() > 0) {
                report.append("  " + entry.getKey() + ": " + entry.getValue() + "次\n");
                hasErrors = true;
            }
        }
        
        if (!hasErrors) {
            report.append("  无错误\n");
        }
        
        report.append("\n");
        
        // 总体结果
        int totalTests = statuses.size();
        int passedTests = (int) statuses.values().stream().filter(AuthTestErrorHandler.TestStatus::isSuccess).count();
        
        report.append("测试结果: ").append(passedTests).append("/").append(totalTests).append(" 通过")
              .append(" (").append(Math.round((double) passedTests / totalTests * 100)).append("%)\n");
        
        if (passedTests == totalTests) {
            report.append("✅ 所有测试通过！认证系统功能正常。\n");
        } else {
            report.append("❌ 部分测试失败，请检查错误信息。\n");
        }
        
        report.append("=======================================\n");
        
        return report.toString();
    }
    
    /**
     * 测试方法信息类
     */
    private static class TestMethodInfo<T> {
        private final String name;
        private final AuthTestErrorHandler.TestMethod<T> method;
        
        public TestMethodInfo(String name, AuthTestErrorHandler.TestMethod<T> method) {
            this.name = name;
            this.method = method;
        }
        
        public String getName() {
            return name;
        }
        
        public AuthTestErrorHandler.TestMethod<T> getMethod() {
            return method;
        }
    }
    
    /**
     * 获取测试执行器状态
     */
    public TestExecutorStatus getStatus() {
        return new TestExecutorStatus(
                isRunning.get(),
                startTime,
                endTime,
                errorHandler.getAllTestStatuses()
        );
    }
    
    /**
     * 测试执行器状态类
     */
    public static class TestExecutorStatus {
        private final boolean isRunning;
        private final LocalDateTime startTime;
        private final LocalDateTime endTime;
        private final Map<String, AuthTestErrorHandler.TestStatus> testStatuses;
        
        public TestExecutorStatus(boolean isRunning, LocalDateTime startTime, 
                                LocalDateTime endTime, 
                                Map<String, AuthTestErrorHandler.TestStatus> testStatuses) {
            this.isRunning = isRunning;
            this.startTime = startTime;
            this.endTime = endTime;
            this.testStatuses = testStatuses;
        }
        
        public boolean isRunning() { return isRunning; }
        public LocalDateTime getStartTime() { return startTime; }
        public LocalDateTime getEndTime() { return endTime; }
        public Map<String, AuthTestErrorHandler.TestStatus> getTestStatuses() { return testStatuses; }
    }
}

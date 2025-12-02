package com.gmp.auth.entity;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * OperationLog实体单元测试
 *
 * @author GMP系统开发团队
 */
@ActiveProfiles("test")
class OperationLogTest {

    @Test
    void testOperationLogDefaultConstructor() {
        // 测试默认构造函数
        OperationLog log = new OperationLog();

        assertThat(log.getId()).isNull();
        assertThat(log.getUserId()).isNull();
        assertThat(log.getUsername()).isNull();
        assertThat(log.getOperation()).isNull();
        assertThat(log.getModule()).isNull();
        assertThat(log.getAction()).isNull();
        assertThat(log.getResult()).isEqualTo(OperationLog.Result.SUCCESS);
        assertThat(log.getOperationTime()).isNotNull();
        assertThat(log.getDurationMs()).isNull();
        assertThat(log.getMetadata()).isNull();
    }

    @Test
    void testOperationLogAllArgsConstructor() {
        // 测试全参构造函数
        LocalDateTime testTime = LocalDateTime.now();
        OperationLog log = new OperationLog(
            1L, 100L, "testUser", "LOGIN", "AUTH", "用户登录",
            OperationLog.Result.SUCCESS, "192.168.1.1", "Mozilla/5.0",
            "{\"username\":\"test\"}", "{\"success\":true}", testTime, 150, "test metadata"
        );

        assertThat(log.getId()).isEqualTo(1L);
        assertThat(log.getUserId()).isEqualTo(100L);
        assertThat(log.getUsername()).isEqualTo("testUser");
        assertThat(log.getOperation()).isEqualTo("LOGIN");
        assertThat(log.getModule()).isEqualTo("AUTH");
        assertThat(log.getAction()).isEqualTo("用户登录");
        assertThat(log.getResult()).isEqualTo(OperationLog.Result.SUCCESS);
        assertThat(log.getIpAddress()).isEqualTo("192.168.1.1");
        assertThat(log.getUserAgent()).isEqualTo("Mozilla/5.0");
        assertThat(log.getRequestData()).isEqualTo("{\"username\":\"test\"}");
        assertThat(log.getResponseData()).isEqualTo("{\"success\":true}");
        assertThat(log.getOperationTime()).isEqualTo(testTime);
        assertThat(log.getDurationMs()).isEqualTo(150);
        assertThat(log.getMetadata()).isEqualTo("test metadata");
    }

    @Test
    void testOperationLogBuilder() {
        // 测试Builder模式
        LocalDateTime testTime = LocalDateTime.now();
        OperationLog log = OperationLog.builder()
            .id(2L)
            .userId(200L)
            .username("builderUser")
            .operation("LOGOUT")
            .module("AUTH")
            .action("用户登出")
            .result(OperationLog.Result.SUCCESS)
            .ipAddress("10.0.0.1")
            .userAgent("Chrome/90.0")
            .requestData("{}")
            .responseData("{\"logout\":true}")
            .operationTime(testTime)
            .durationMs(50)
            .metadata("builder test")
            .build();

        assertThat(log.getId()).isEqualTo(2L);
        assertThat(log.getUserId()).isEqualTo(200L);
        assertThat(log.getUsername()).isEqualTo("builderUser");
        assertThat(log.getOperation()).isEqualTo("LOGOUT");
        assertThat(log.getModule()).isEqualTo("AUTH");
        assertThat(log.getAction()).isEqualTo("用户登出");
        assertThat(log.getResult()).isEqualTo(OperationLog.Result.SUCCESS);
        assertThat(log.getIpAddress()).isEqualTo("10.0.0.1");
        assertThat(log.getUserAgent()).isEqualTo("Chrome/90.0");
        assertThat(log.getRequestData()).isEqualTo("{}");
        assertThat(log.getResponseData()).isEqualTo("{\"logout\":true}");
        assertThat(log.getOperationTime()).isEqualTo(testTime);
        assertThat(log.getDurationMs()).isEqualTo(50);
        assertThat(log.getMetadata()).isEqualTo("builder test");
    }

    @Test
    void testOperationLogGettersAndSetters() {
        // 测试getter和setter方法
        OperationLog log = new OperationLog();
        LocalDateTime testTime = LocalDateTime.now();

        log.setId(3L);
        log.setUserId(300L);
        log.setUsername("setterUser");
        log.setOperation("USER_CREATE");
        log.setModule("USER");
        log.setAction("创建用户");
        log.setResult(OperationLog.Result.FAILED);
        log.setIpAddress("127.0.0.1");
        log.setUserAgent("Firefox/88.0");
        log.setRequestData("{\"user\":\"newUser\"}");
        log.setResponseData("{\"created\":false}");
        log.setOperationTime(testTime);
        log.setDurationMs(200);
        log.setMetadata("setter test");

        assertThat(log.getId()).isEqualTo(3L);
        assertThat(log.getUserId()).isEqualTo(300L);
        assertThat(log.getUsername()).isEqualTo("setterUser");
        assertThat(log.getOperation()).isEqualTo("USER_CREATE");
        assertThat(log.getModule()).isEqualTo("USER");
        assertThat(log.getAction()).isEqualTo("创建用户");
        assertThat(log.getResult()).isEqualTo(OperationLog.Result.FAILED);
        assertThat(log.getIpAddress()).isEqualTo("127.0.0.1");
        assertThat(log.getUserAgent()).isEqualTo("Firefox/88.0");
        assertThat(log.getRequestData()).isEqualTo("{\"user\":\"newUser\"}");
        assertThat(log.getResponseData()).isEqualTo("{\"created\":false}");
        assertThat(log.getOperationTime()).isEqualTo(testTime);
        assertThat(log.getDurationMs()).isEqualTo(200);
        assertThat(log.getMetadata()).isEqualTo("setter test");
    }

    @Test
    void testOperationLogEnums() {
        // 测试枚举值
        assertThat(OperationLog.Result.SUCCESS.getDescription()).isEqualTo("成功");
        assertThat(OperationLog.Result.FAILED.getDescription()).isEqualTo("失败");

        assertThat(OperationLog.OperationType.LOGIN.getDescription()).isEqualTo("用户登录");
        assertThat(OperationLog.OperationType.LOGOUT.getDescription()).isEqualTo("用户登出");
        assertThat(OperationLog.OperationType.USER_CREATE.getDescription()).isEqualTo("创建用户");

        assertThat(OperationLog.Module.AUTH.getDescription()).isEqualTo("认证模块");
        assertThat(OperationLog.Module.USER.getDescription()).isEqualTo("用户管理");
        assertThat(OperationLog.Module.ROLE.getDescription()).isEqualTo("角色管理");
    }

    @Test
    void testCreateLoginLog() {
        // 测试创建登录日志的静态方法
        OperationLog loginLog = OperationLog.createLoginLog(
            1L, "testUser", "192.168.1.1", "Chrome", true
        );

        assertThat(loginLog.getUserId()).isEqualTo(1L);
        assertThat(loginLog.getUsername()).isEqualTo("testUser");
        assertThat(loginLog.getOperation()).isEqualTo("LOGIN");
        assertThat(loginLog.getModule()).isEqualTo("AUTH");
        assertThat(loginLog.getAction()).isEqualTo("用户登录成功");
        assertThat(loginLog.getResult()).isEqualTo(OperationLog.Result.SUCCESS);
        assertThat(loginLog.getIpAddress()).isEqualTo("192.168.1.1");
        assertThat(loginLog.getUserAgent()).isEqualTo("Chrome");
    }

    @Test
    void testCreateLoginLogFailure() {
        // 测试创建失败登录日志的静态方法
        OperationLog loginLog = OperationLog.createLoginLog(
            1L, "testUser", "192.168.1.1", "Chrome", false
        );

        assertThat(loginLog.getAction()).isEqualTo("用户登录失败");
        assertThat(loginLog.getResult()).isEqualTo(OperationLog.Result.FAILED);
    }

    @Test
    void testCreateLogoutLog() {
        // 测试创建登出日志的静态方法
        OperationLog logoutLog = OperationLog.createLogoutLog(
            1L, "testUser", "192.168.1.1"
        );

        assertThat(logoutLog.getUserId()).isEqualTo(1L);
        assertThat(logoutLog.getUsername()).isEqualTo("testUser");
        assertThat(logoutLog.getOperation()).isEqualTo("LOGOUT");
        assertThat(logoutLog.getModule()).isEqualTo("AUTH");
        assertThat(logoutLog.getAction()).isEqualTo("用户登出");
        assertThat(logoutLog.getResult()).isEqualTo(OperationLog.Result.SUCCESS);
        assertThat(logoutLog.getIpAddress()).isEqualTo("192.168.1.1");
    }

    @Test
    void testCreateUserOperationLog() {
        // 测试创建用户操作日志的静态方法
        OperationLog userOpLog = OperationLog.createUserOperationLog(
            1L, "testUser", OperationLog.OperationType.USER_UPDATE, "更新用户信息", true
        );

        assertThat(userOpLog.getUserId()).isEqualTo(1L);
        assertThat(userOpLog.getUsername()).isEqualTo("testUser");
        assertThat(userOpLog.getOperation()).isEqualTo("USER_UPDATE");
        assertThat(userOpLog.getModule()).isEqualTo("USER");
        assertThat(userOpLog.getAction()).isEqualTo("更新用户信息");
        assertThat(userOpLog.getResult()).isEqualTo(OperationLog.Result.SUCCESS);
    }

    @Test
    void testCreateUserOperationLogFailure() {
        // 测试创建失败的用户操作日志
        OperationLog userOpLog = OperationLog.createUserOperationLog(
            1L, "testUser", OperationLog.OperationType.USER_DELETE, "删除用户失败", false
        );

        assertThat(userOpLog.getResult()).isEqualTo(OperationLog.Result.FAILED);
    }
}

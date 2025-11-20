package com.gmp.auth.dto;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ApiResponse DTO单元测试
 *
 * @author GMP系统开发团队
 */
@ActiveProfiles("test")
class ApiResponseTest {

    @Test
    void testSuccessApiResponse() {
        // Given
        Object data = Map.of("key", "value");

        // When
        ApiResponse<Object> response = ApiResponse.success(data);

        // Then
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getCode()).isEqualTo("200");
        assertThat(response.getMessage()).isEqualTo("操作成功");
        assertThat(response.getData()).isEqualTo(data);
        assertThat(response.getTimestamp()).isNotNull();
    }

    @Test
    void testSuccessApiResponseWithMessage() {
        // Given
        String message = "用户创建成功";
        Object data = Map.of("userId", "123");

        // When
        ApiResponse<Object> response = ApiResponse.success(message, data);

        // Then
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getCode()).isEqualTo("200");
        assertThat(response.getMessage()).isEqualTo(message);
        assertThat(response.getData()).isEqualTo(data);
        assertThat(response.getTimestamp()).isNotNull();
    }

    @Test
    void testSuccessApiResponseEmpty() {
        // When
        ApiResponse<Void> response = ApiResponse.success();

        // Then
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getCode()).isEqualTo("200");
        assertThat(response.getMessage()).isEqualTo("操作成功");
        assertThat(response.getData()).isNull();
        assertThat(response.getTimestamp()).isNotNull();
    }

    @Test
    void testErrorApiResponseWithCode() {
        // Given
        String code = "404";
        String message = "用户不存在";

        // When
        ApiResponse<Object> response = ApiResponse.error(code, message);

        // Then
        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getCode()).isEqualTo(code);
        assertThat(response.getMessage()).isEqualTo(message);
        assertThat(response.getData()).isNull();
        assertThat(response.getTimestamp()).isNotNull();
    }

    @Test
    void testErrorApiResponseDefaultCode() {
        // Given
        String message = "服务器错误";

        // When
        ApiResponse<Object> response = ApiResponse.error(message);

        // Then
        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getCode()).isEqualTo("500");
        assertThat(response.getMessage()).isEqualTo(message);
        assertThat(response.getData()).isNull();
        assertThat(response.getTimestamp()).isNotNull();
    }

    @Test
    void testCustomApiResponse() {
        // Given
        boolean success = false;
        String code = "403";
        String message = "权限不足";
        Object data = Map.of("requiredRole", "ADMIN");
        // Use constructor with all parameters

        // When
        ApiResponse<Object> response = new ApiResponse<>(success, code, message, data);
        response.setTimestamp(java.time.LocalDateTime.now());

        // Then
        assertThat(response.isSuccess()).isEqualTo(success);
        assertThat(response.getCode()).isEqualTo(code);
        assertThat(response.getMessage()).isEqualTo(message);
        assertThat(response.getData()).isEqualTo(data);
        assertThat(response.getTimestamp()).isNotNull();
    }

    @Test
    void testApiResponseFullConstructor() {
        // Given
        boolean success = true;
        String code = "201";
        String message = "资源创建成功";
        Object data = Map.of("resourceId", "456");
        java.time.LocalDateTime timestamp = java.time.LocalDateTime.now().minusMinutes(1);

        // When
        ApiResponse<Object> response = new ApiResponse<>(success, code, message, data, timestamp);

        // Then
        assertThat(response.isSuccess()).isEqualTo(success);
        assertThat(response.getCode()).isEqualTo(code);
        assertThat(response.getMessage()).isEqualTo(message);
        assertThat(response.getData()).isEqualTo(data);
        assertThat(response.getTimestamp()).isEqualTo(timestamp);
    }
    
    @Test
    void testApiResponseWithDifferentDataTypes() {
        // 测试不同类型的数据，增加泛型的分支覆盖
        
        // 测试String类型
        ApiResponse<String> stringResponse = ApiResponse.success("test-string");
        assertThat(stringResponse.getData()).isEqualTo("test-string");
        assertThat(stringResponse.getData()).isInstanceOf(String.class);
        
        // 测试List类型
        List<String> listData = Arrays.asList("item1", "item2");
        ApiResponse<List<String>> listResponse = ApiResponse.success(listData);
        assertThat(listResponse.getData()).isEqualTo(listData);
        assertThat(listResponse.getData()).isInstanceOf(List.class);
        
        // 测试Map类型
        Map<String, Object> mapData = new HashMap<>();
        mapData.put("key1", "value1");
        mapData.put("key2", 123);
        ApiResponse<Map<String, Object>> mapResponse = ApiResponse.success(mapData);
        assertThat(mapResponse.getData()).isEqualTo(mapData);
        assertThat(mapResponse.getData()).isInstanceOf(Map.class);
    }
    
    @Test
    void testErrorApiResponseWithDifferentCodes() {
        // 测试不同错误码的情况
        ApiResponse<String> notFoundResponse = ApiResponse.error("404", "资源不存在");
        assertThat(notFoundResponse.getCode()).isEqualTo("404");
        assertThat(notFoundResponse.getMessage()).isEqualTo("资源不存在");
        
        ApiResponse<String> badRequestResponse = ApiResponse.error("400", "请求参数错误");
        assertThat(badRequestResponse.getCode()).isEqualTo("400");
        assertThat(badRequestResponse.getMessage()).isEqualTo("请求参数错误");
        
        ApiResponse<String> unauthorizedResponse = ApiResponse.error("401", "未授权访问");
        assertThat(unauthorizedResponse.getCode()).isEqualTo("401");
        assertThat(unauthorizedResponse.getMessage()).isEqualTo("未授权访问");
    }
    
    @Test
    void testDefaultConstructorAndSetters() {
        // 测试默认构造函数和setter方法（通过反射）
        ApiResponse<String> response = new ApiResponse<>();
        
        try {
            // 使用反射设置字段值
            java.lang.reflect.Field successField = ApiResponse.class.getDeclaredField("success");
            successField.setAccessible(true);
            successField.set(response, false);
            
            java.lang.reflect.Field codeField = ApiResponse.class.getDeclaredField("code");
            codeField.setAccessible(true);
            codeField.set(response, "503");
            
            java.lang.reflect.Field messageField = ApiResponse.class.getDeclaredField("message");
            messageField.setAccessible(true);
            messageField.set(response, "服务不可用");
            
            java.lang.reflect.Field dataField = ApiResponse.class.getDeclaredField("data");
            dataField.setAccessible(true);
            dataField.set(response, "service-down");
            
            java.time.LocalDateTime customTime = java.time.LocalDateTime.now().minusHours(1);
            java.lang.reflect.Field timestampField = ApiResponse.class.getDeclaredField("timestamp");
            timestampField.setAccessible(true);
            timestampField.set(response, customTime);
            
            // 验证设置成功
            assertThat(response.isSuccess()).isFalse();
            assertThat(response.getCode()).isEqualTo("503");
            assertThat(response.getMessage()).isEqualTo("服务不可用");
            assertThat(response.getData()).isEqualTo("service-down");
            assertThat(response.getTimestamp()).isEqualTo(customTime);
        } catch (Exception e) {
            throw new RuntimeException("测试失败: 无法通过反射测试默认构造函数", e);
        }
    }
}

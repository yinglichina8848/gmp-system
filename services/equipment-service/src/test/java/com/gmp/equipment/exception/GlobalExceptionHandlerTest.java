package com.gmp.equipment.exception;

import com.gmp.equipment.controller.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * 全局异常处理器单元测试
 * 测试各种异常场景下的处理逻辑
 * 
 * @author GMP系统开发团队
 * @version 1.0
 * @since 2023-07-01
 */
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler exceptionHandler;

    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        webRequest = mock(WebRequest.class);
        when(webRequest.getDescription(false)).thenReturn("uri=/api/equipment");
    }

    @Test
    void testHandleEquipmentException() {
        // 创建设备异常
        EquipmentException exception = new EquipmentException("设备不存在", "EQUIPMENT_NOT_FOUND");

        // 测试异常处理
        ResponseEntity<?> response = exceptionHandler.handleEquipmentException(exception, webRequest);

        // 验证结果
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Map);
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals("设备不存在", body.get("message"));
        assertEquals("EQUIPMENT_NOT_FOUND", body.get("errorCode"));
        assertNotNull(body.get("timestamp"));
    }

    @Test
    void testHandleMethodArgumentNotValidException() {
        // 创建验证异常
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindException bindException = mock(BindException.class);
        when(exception.getBindingResult()).thenReturn(bindException);

        // 创建验证错误
        List<FieldError> fieldErrors = new ArrayList<>();
        fieldErrors.add(new FieldError("equipmentDTO", "equipmentName", "设备名称不能为空"));
        fieldErrors.add(new FieldError("equipmentDTO", "status", "无效的设备状态"));
        when(bindException.getFieldErrors()).thenReturn(fieldErrors);

        // 测试异常处理
        ResponseEntity<?> response = exceptionHandler.handleMethodArgumentNotValidException(exception, null, null, webRequest);

        // 验证结果
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Map);
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals("参数验证失败", body.get("message"));
        assertNotNull(body.get("errors"));
        assertTrue(body.get("errors") instanceof List);
        List<?> errors = (List<?>) body.get("errors");
        assertEquals(2, errors.size());
    }

    @Test
    void testHandleMissingServletRequestParameterException() {
        // 创建缺少参数异常
        MissingServletRequestParameterException exception = 
                new MissingServletRequestParameterException("id", "Long");

        // 测试异常处理
        ResponseEntity<?> response = exceptionHandler.handleMissingServletRequestParameterException(exception, null, null, webRequest);

        // 验证结果
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Map);
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals("缺少必需参数: id", body.get("message"));
        assertNotNull(body.get("timestamp"));
    }

    @Test
    void testHandleMethodArgumentTypeMismatchException() {
        // 创建参数类型不匹配异常
        MethodArgumentTypeMismatchException exception = mock(MethodArgumentTypeMismatchException.class);
        when(exception.getName()).thenReturn("id");
        when(exception.getRequiredType()).thenReturn(Long.class);

        // 测试异常处理
        ResponseEntity<?> response = exceptionHandler.handleMethodArgumentTypeMismatchException(exception, null, null, webRequest);

        // 验证结果
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Map);
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertTrue(body.get("message").toString().contains("参数'id'类型不匹配"));
        assertNotNull(body.get("timestamp"));
    }

    @Test
    void testHandleHttpMessageNotReadableException() {
        // 创建消息不可读异常（如JSON格式错误）
        HttpMessageNotReadableException exception = 
                new HttpMessageNotReadableException("JSON parse error", null);

        // 测试异常处理
        ResponseEntity<?> response = exceptionHandler.handleHttpMessageNotReadableException(exception, null, null, webRequest);

        // 验证结果
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Map);
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals("请求体格式错误: JSON parse error", body.get("message"));
        assertNotNull(body.get("timestamp"));
    }

    @Test
    void testHandleIllegalArgumentException() {
        // 创建非法参数异常
        IllegalArgumentException exception = new IllegalArgumentException("参数值非法");

        // 测试异常处理
        ResponseEntity<?> response = exceptionHandler.handleIllegalArgumentException(exception, webRequest);

        // 验证结果
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Map);
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals("参数错误: 参数值非法", body.get("message"));
        assertNotNull(body.get("timestamp"));
    }

    @Test
    void testHandleNullPointerException() {
        // 创建空指针异常
        NullPointerException exception = new NullPointerException("空指针异常");

        // 测试异常处理
        ResponseEntity<?> response = exceptionHandler.handleNullPointerException(exception, webRequest);

        // 验证结果
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Map);
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals("服务器内部错误", body.get("message"));
        assertNotNull(body.get("timestamp"));
    }

    @Test
    void testHandleGenericException() {
        // 创建通用异常
        Exception exception = new Exception("未知错误");

        // 测试异常处理
        ResponseEntity<?> response = exceptionHandler.handleGenericException(exception, webRequest);

        // 验证结果
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Map);
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals("服务器内部错误", body.get("message"));
        assertNotNull(body.get("timestamp"));
    }

    @Test
    void testBuildErrorResponse() {
        // 测试构建错误响应
        Map<?, ?> response = exceptionHandler.buildErrorResponse("测试错误", HttpStatus.BAD_REQUEST, webRequest);

        // 验证响应内容
        assertNotNull(response);
        assertEquals("测试错误", response.get("message"));
        assertEquals("400 BAD_REQUEST", response.get("status"));
        assertEquals("uri=/api/equipment", response.get("path"));
        assertNotNull(response.get("timestamp"));
    }

    @Test
    void testBuildValidationErrorResponse() {
        // 创建验证错误列表
        List<FieldError> fieldErrors = new ArrayList<>();
        fieldErrors.add(new FieldError("equipmentDTO", "equipmentName", "设备名称不能为空"));
        fieldErrors.add(new FieldError("equipmentDTO", "status", "无效的设备状态"));

        // 测试构建验证错误响应
        Map<?, ?> response = exceptionHandler.buildValidationErrorResponse(fieldErrors, webRequest);

        // 验证响应内容
        assertNotNull(response);
        assertEquals("参数验证失败", response.get("message"));
        assertNotNull(response.get("errors"));
        assertTrue(response.get("errors") instanceof List);
        List<?> errors = (List<?>) response.get("errors");
        assertEquals(2, errors.size());
        assertNotNull(response.get("timestamp"));
    }
}

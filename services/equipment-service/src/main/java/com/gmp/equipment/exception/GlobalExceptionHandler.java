package com.gmp.equipment.exception;

import com.gmp.equipment.util.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;

/**
 * 全局异常处理器
 * 用于统一处理系统中的各种异常
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理设备服务业务异常
     * @param ex 设备服务异常
     * @return 统一响应结果
     */
    @ExceptionHandler(EquipmentServiceException.class)
    @ResponseBody
    public ResponseResult<Object> handleEquipmentServiceException(EquipmentServiceException ex) {
        logger.error("设备服务业务异常: {}", ex.getMessage(), ex);
        // 如果有错误代码，则使用错误代码，否则使用默认的500
        int code = ex.getErrorCode() != null ? Integer.parseInt(ex.getErrorCode()) : 500;
        return ResponseResult.error(code, ex.getMessage());
    }

    /**
     * 处理参数验证异常（方法参数验证）
     * @param ex 方法参数验证异常
     * @return 统一响应结果
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseResult<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        logger.error("参数验证异常: {}", ex.getMessage(), ex);
        // 获取第一个错误消息
        String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return ResponseResult.error(400, errorMessage);
    }

    /**
     * 处理参数验证异常（请求参数验证）
     * @param ex 约束违反异常
     * @return 统一响应结果
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseResult<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        logger.error("参数验证异常: {}", ex.getMessage(), ex);
        // 获取第一个错误消息
        String errorMessage = ex.getConstraintViolations().iterator().next().getMessage();
        return ResponseResult.error(400, errorMessage);
    }

    /**
     * 处理空指针异常
     * @param ex 空指针异常
     * @return 统一响应结果
     */
    @ExceptionHandler(NullPointerException.class)
    @ResponseBody
    public ResponseResult<Object> handleNullPointerException(NullPointerException ex) {
        logger.error("空指针异常: {}", ex.getMessage(), ex);
        return ResponseResult.error(500, "系统内部错误：空指针异常");
    }

    /**
     * 处理所有未捕获的异常
     * @param ex 异常
     * @return 统一响应结果
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult<Object> handleException(Exception ex) {
        logger.error("未处理的系统异常: {}", ex.getMessage(), ex);
        return ResponseResult.error(500, "系统内部错误，请稍后重试");
    }
}
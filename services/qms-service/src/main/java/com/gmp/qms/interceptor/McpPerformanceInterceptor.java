package com.gmp.qms.interceptor;

import com.gmp.qms.monitoring.McpMonitoringService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * MCP性能拦截器，用于自动拦截和监控MCP调用
 * 
 * @author GMP系统开发团队
 */
@Aspect
@Component
public class McpPerformanceInterceptor {
    
    @Autowired
    private McpMonitoringService monitoringService;
    
    /**
     * 拦截MCP集成服务方法调用
     */
    @Around("execution(* com.gmp.qms.service.*Integration*.*(..))")
    public Object monitorIntegrationService(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getDeclaringType().getSimpleName() + "." + signature.getName();
        
        McpMonitoringService.McpTimer timer = monitoringService.startToolCall(methodName);
        boolean success = true;
        Object result;
        
        try {
            result = joinPoint.proceed();
        } catch (Exception e) {
            success = false;
            throw e;
        } finally {
            monitoringService.endCall(timer, success);
        }
        
        return result;
    }
    
    /**
     * 拦截Feign客户端方法调用
     */
    @Around("execution(* com.gmp.qms.client.*Client.*(..))")
    public Object monitorClientService(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getDeclaringType().getSimpleName() + "." + signature.getName();
        
        McpMonitoringService.McpTimer timer = monitoringService.startToolCall(methodName);
        boolean success = true;
        Object result;
        
        try {
            result = joinPoint.proceed();
        } catch (Exception e) {
            success = false;
            throw e;
        } finally {
            monitoringService.endCall(timer, success);
        }
        
        return result;
    }
    
    /**
     * 拦截资源访问方法调用
     */
    @Around("execution(* com.gmp.qms.service.*Resource*.*(..))")
    public Object monitorResourceService(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getDeclaringType().getSimpleName() + "." + signature.getName();
        
        McpMonitoringService.McpTimer timer = monitoringService.startResourceAccess(methodName);
        boolean success = true;
        Object result;
        
        try {
            result = joinPoint.proceed();
        } catch (Exception e) {
            success = false;
            throw e;
        } finally {
            monitoringService.endCall(timer, success);
        }
        
        return result;
    }
}

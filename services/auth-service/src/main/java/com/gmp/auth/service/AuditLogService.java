package com.gmp.auth.service;

import com.gmp.auth.entity.OperationLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 审计日志服务接口
 * 提供全面的审计日志管理功能
 *
 * @author GMP系统开发团队
 */
public interface AuditLogService {

    /**
     * 记录操作日志
     * @param log 操作日志实体
     * @return 保存后的操作日志
     */
    OperationLog logOperation(OperationLog log);

    /**
     * 记录登录操作
     * @param userId 用户ID
     * @param username 用户名
     * @param ipAddress IP地址
     * @param userAgent 用户代理
     * @param success 是否成功
     * @param additionalInfo 附加信息
     * @return 保存后的操作日志
     */
    OperationLog logLogin(Long userId, String username, String ipAddress, String userAgent, 
                        boolean success, Map<String, Object> additionalInfo);
    
    /**
     * 记录登录成功
     * @param userId 用户ID
     * @param username 用户名
     * @param ipAddress IP地址
     * @param userAgent 用户代理
     */
    void logLoginSuccess(Long userId, String username, String ipAddress, String userAgent);

    /**
     * 记录登出操作
     * @param userId 用户ID
     * @param username 用户名
     * @param ipAddress IP地址
     * @return 保存后的操作日志
     */
    OperationLog logLogout(Long userId, String username, String ipAddress);

    /**
     * 记录用户管理操作
     * @param operatorId 操作者ID
     * @param operatorName 操作者名称
     * @param targetUserId 目标用户ID
     * @param targetUsername 目标用户名
     * @param operationType 操作类型
     * @param success 是否成功
     * @param details 操作详情
     * @return 保存后的操作日志
     */
    OperationLog logUserManagement(Long operatorId, String operatorName, 
                                 Long targetUserId, String targetUsername,
                                 OperationLog.OperationType operationType,
                                 boolean success, String details);

    /**
     * 记录角色权限变更操作
     * @param operatorId 操作者ID
     * @param operatorName 操作者名称
     * @param roleId 角色ID
     * @param roleCode 角色代码
     * @param permissionChanges 权限变更详情
     * @param success 是否成功
     * @return 保存后的操作日志
     */
    OperationLog logRolePermissionChange(Long operatorId, String operatorName,
                                      Long roleId, String roleCode,
                                      String permissionChanges, boolean success);

    /**
     * 记录用户角色分配操作
     * @param operatorId 操作者ID
     * @param operatorName 操作者名称
     * @param userId 用户ID
     * @param username 用户名
     * @param roleId 角色ID
     * @param roleCode 角色代码
     * @param isAssign 是否为分配操作（true为分配，false为撤销）
     * @param success 是否成功
     * @return 保存后的操作日志
     */
    OperationLog logRoleAssignment(Long operatorId, String operatorName,
                                Long userId, String username,
                                Long roleId, String roleCode,
                                boolean isAssign, boolean success);

    /**
     * 记录安全事件
     * @param userId 用户ID
     * @param username 用户名
     * @param eventType 事件类型
     * @param severity 严重程度
     * @param details 事件详情
     * @param ipAddress IP地址
     * @return 保存后的操作日志
     */
    OperationLog logSecurityEvent(Long userId, String username, 
                               String eventType, String severity,
                               String details, String ipAddress);

    /**
     * 分页查询操作日志
     * @param pageable 分页参数
     * @param filters 过滤条件
     * @return 分页后的操作日志列表
     */
    Page<OperationLog> findLogsByPage(Pageable pageable, Map<String, Object> filters);

    /**
     * 根据用户ID查询操作日志
     * @param userId 用户ID
     * @param pageable 分页参数
     * @return 分页后的操作日志列表
     */
    Page<OperationLog> findLogsByUserId(Long userId, Pageable pageable);

    /**
     * 查询指定时间范围内的操作日志
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param pageable 分页参数
     * @return 分页后的操作日志列表
     */
    Page<OperationLog> findLogsByTimeRange(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);

    /**
     * 查询失败的操作日志
     * @param since 从什么时间开始
     * @param pageable 分页参数
     * @return 分页后的操作日志列表
     */
    Page<OperationLog> findFailedOperations(LocalDateTime since, Pageable pageable);

    /**
     * 获取操作统计信息
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 统计结果
     */
    Map<String, Object> getOperationStatistics(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 清理过期日志
     * @param daysToKeep 保留天数
     * @return 删除的日志数量
     */
    int cleanupOldLogs(int daysToKeep);

    /**
     * 导出日志到指定格式
     * @param filters 过滤条件
     * @param format 导出格式
     * @return 导出的日志数据
     */
    byte[] exportLogs(Map<String, Object> filters, String format);
    
    /**
     * 记录密码修改事件
     * @param userId 用户ID
     * @param username 用户名
     * @param ipAddress IP地址
     */
    void logPasswordChange(Long userId, String username, String ipAddress);
    
    /**
     * 记录密码重置事件
     * @param userId 用户ID
     * @param username 用户名
     * @param ipAddress IP地址
     */
    void logPasswordReset(Long userId, String username, String ipAddress);
}
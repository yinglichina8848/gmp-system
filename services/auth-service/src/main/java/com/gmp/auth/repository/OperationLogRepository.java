package com.gmp.auth.repository;

import com.gmp.auth.entity.OperationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 操作日志数据访问层
 *
 * @author GMP系统开发团队
 */
@Repository
public interface OperationLogRepository extends JpaRepository<OperationLog, Long> {

    /**
     * 根据用户查找操作日志
     */
    @Query("SELECT ol FROM OperationLog ol WHERE ol.userId = :userId ORDER BY ol.operationTime DESC")
    List<OperationLog> findByUserIdOrderByOperationTimeDesc(@Param("userId") Long userId);

    /**
     * 根据用户名查找操作日志
     */
    @Query("SELECT ol FROM OperationLog ol WHERE ol.username = :username ORDER BY ol.operationTime DESC")
    List<OperationLog> findByUsernameOrderByOperationTimeDesc(@Param("username") String username);

    /**
     * 查找时间范围内的操作日志
     */
    @Query("SELECT ol FROM OperationLog ol WHERE ol.operationTime BETWEEN :startTime AND :endTime ORDER BY ol.operationTime DESC")
    List<OperationLog> findByOperationTimeBetween(@Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    /**
     * 根据操作类型查找日志
     */
    @Query("SELECT ol FROM OperationLog ol WHERE ol.operation = :operation ORDER BY ol.operationTime DESC")
    List<OperationLog> findByOperation(@Param("operation") String operation);

    /**
     * 根据模块查找日志
     */
    @Query("SELECT ol FROM OperationLog ol WHERE ol.module = :module ORDER BY ol.operationTime DESC")
    List<OperationLog> findByModule(@Param("module") String module);

    /**
     * 根据IP地址查找日志
     */
    @Query("SELECT ol FROM OperationLog ol WHERE ol.ipAddress = :ipAddress ORDER BY ol.operationTime DESC")
    List<OperationLog> findByIpAddress(@Param("ipAddress") String ipAddress);

    /**
     * 查找登录相关的日志
     */
    @Query("SELECT ol FROM OperationLog ol WHERE ol.operation = 'LOGIN' ORDER BY ol.operationTime DESC")
    List<OperationLog> findLoginLogs();

    /**
     * 查找某个时间段内的失败操作
     */
    @Query("SELECT ol FROM OperationLog ol WHERE ol.result = 'FAILED' AND ol.operationTime >= :since ORDER BY ol.operationTime DESC")
    List<OperationLog> findFailedOperationsSince(@Param("since") LocalDateTime since);

    /**
     * 清理过期日志（保留最近N天的日志）
     */
    @Query("DELETE FROM OperationLog ol WHERE ol.operationTime < :beforeDate")
    void deleteLogsBefore(@Param("beforeDate") LocalDateTime beforeDate);
}

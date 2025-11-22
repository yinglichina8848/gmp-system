package com.gmp.auth.service;

import com.gmp.auth.entity.User;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * MFA会话管理器，用于存储用户在第一阶段登录成功后的临时会话
 */
@Component
public class MfaSessionManager {
    
    // 会话超时时间（分钟）
    private static final int SESSION_TIMEOUT_MINUTES = 10;
    
    // 存储会话信息的Map，key为会话ID，value为会话信息
    private final Map<String, MfaSession> sessions = new ConcurrentHashMap<>();
    
    /**
     * 创建新的MFA会话
     */
    public String createSession(User user) {
        String sessionId = UUID.randomUUID().toString();
        MfaSession session = new MfaSession(sessionId, user, LocalDateTime.now());
        sessions.put(sessionId, session);
        return sessionId;
    }
    
    /**
     * 获取会话信息
     */
    public MfaSession getSession(String sessionId) {
        if (sessionId == null) {
            return null;
        }
        
        MfaSession session = sessions.get(sessionId);
        if (session != null) {
            // 检查会话是否过期
            if (isSessionExpired(session)) {
                invalidateSession(sessionId);
                return null;
            }
        }
        
        return session;
    }
    
    /**
     * 使会话失效
     */
    public void invalidateSession(String sessionId) {
        if (sessionId != null) {
            sessions.remove(sessionId);
        }
    }
    
    /**
     * 检查会话是否过期
     */
    private boolean isSessionExpired(MfaSession session) {
        return session.getCreatedAt().plusMinutes(SESSION_TIMEOUT_MINUTES).isBefore(LocalDateTime.now());
    }
    
    /**
     * 清理过期会话（可以通过定时任务调用）
     */
    public void cleanupExpiredSessions() {
        LocalDateTime now = LocalDateTime.now();
        sessions.forEach((sessionId, session) -> {
            if (session.getCreatedAt().plusMinutes(SESSION_TIMEOUT_MINUTES).isBefore(now)) {
                sessions.remove(sessionId);
            }
        });
    }
    
    /**
     * MFA会话内部类
     */
    public static class MfaSession {
        private final String sessionId;
        private final User user;
        private final LocalDateTime createdAt;
        private int failedAttempts = 0;
        private LocalDateTime lastVerificationAttempt;
        
        public MfaSession(String sessionId, User user, LocalDateTime createdAt) {
            this.sessionId = sessionId;
            this.user = user;
            this.createdAt = createdAt;
        }
        
        public String getSessionId() {
            return sessionId;
        }
        
        public User getUser() {
            return user;
        }
        
        public LocalDateTime getCreatedAt() {
            return createdAt;
        }
        
        public int getFailedAttempts() {
            return failedAttempts;
        }
        
        public void incrementFailedAttempts() {
            this.failedAttempts++;
            this.lastVerificationAttempt = LocalDateTime.now();
        }
        
        public LocalDateTime getLastVerificationAttempt() {
            return lastVerificationAttempt;
        }
        
        /**
         * 检查是否达到最大失败尝试次数
         */
        public boolean isLocked() {
            return failedAttempts >= 5; // 5次失败尝试后锁定
        }
    }
}
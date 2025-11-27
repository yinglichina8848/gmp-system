package com.gmp.auth.service;

import com.gmp.auth.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * MfaSessionManager的单元测试类
 */
@ExtendWith(MockitoExtension.class)
public class MfaSessionManagerTest {

    @InjectMocks
    private MfaSessionManager mfaSessionManager;

    private User testUser;

    @BeforeEach
    void setUp() {
        // 初始化测试用户
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setUserStatus(User.UserStatus.ACTIVE);
    }

    @Test
    @DisplayName("测试创建会话")
    void testCreateSession() {
        // When
        String sessionId = mfaSessionManager.createSession(testUser);

        // Then
        assertThat(sessionId).isNotNull();
        assertThat(sessionId).matches("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$");
    }

    @Test
    @DisplayName("测试创建会话时传入null用户")
    void testCreateSessionWithNullUser() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            mfaSessionManager.createSession(null);
        });
    }

    @Test
    @DisplayName("测试获取会话")
    void testGetSession() {
        // Given
        String sessionId = mfaSessionManager.createSession(testUser);

        // When
        MfaSessionManager.MfaSession session = mfaSessionManager.getSession(sessionId);

        // Then
        assertThat(session).isNotNull();
        assertThat(session.getSessionId()).isEqualTo(sessionId);
        assertThat(session.getUser()).isEqualTo(testUser);
    }

    @Test
    @DisplayName("测试获取不存在的会话")
    void testGetNonExistentSession() {
        // When
        MfaSessionManager.MfaSession session = mfaSessionManager.getSession("non-existent-session");

        // Then
        assertThat(session).isNull();
    }

    @Test
    @DisplayName("测试获取null会话ID")
    void testGetNullSessionId() {
        // When
        MfaSessionManager.MfaSession session = mfaSessionManager.getSession(null);

        // Then
        assertThat(session).isNull();
    }

    @Test
    @DisplayName("测试会话失效")
    void testInvalidateSession() {
        // Given
        String sessionId = mfaSessionManager.createSession(testUser);
        
        // 验证会话创建成功
        assertThat(mfaSessionManager.getSession(sessionId)).isNotNull();

        // When
        mfaSessionManager.invalidateSession(sessionId);

        // Then
        assertThat(mfaSessionManager.getSession(sessionId)).isNull();
    }

    @Test
    @DisplayName("测试失效null会话ID")
    void testInvalidateNullSession() {
        // When & Then - 不应抛出异常
        mfaSessionManager.invalidateSession(null);
    }

    @Test
    @DisplayName("测试会话失败尝试计数")
    void testFailedAttempts() {
        // Given
        String sessionId = mfaSessionManager.createSession(testUser);
        MfaSessionManager.MfaSession session = mfaSessionManager.getSession(sessionId);

        // Then - 初始失败计数应为0
        assertThat(session.getFailedAttempts()).isEqualTo(0);
        assertThat(session.isLocked()).isFalse();

        // When - 增加失败尝试
        session.incrementFailedAttempts();
        
        // Then
        assertThat(session.getFailedAttempts()).isEqualTo(1);
        assertThat(session.isLocked()).isFalse();

        // When - 达到锁定阈值
        for (int i = 0; i < 4; i++) {
            session.incrementFailedAttempts();
        }
        
        // Then
        assertThat(session.getFailedAttempts()).isEqualTo(5);
        assertThat(session.isLocked()).isTrue();
    }

    @Test
    @DisplayName("测试清理过期会话")
    void testCleanupExpiredSessions() {
        // Given
        String sessionId1 = mfaSessionManager.createSession(testUser);
        
        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("testuser2");
        user2.setUserStatus(User.UserStatus.ACTIVE);
        
        String sessionId2 = mfaSessionManager.createSession(user2);

        // 验证两个会话都存在
        assertThat(mfaSessionManager.getSession(sessionId1)).isNotNull();
        assertThat(mfaSessionManager.getSession(sessionId2)).isNotNull();

        // When - 使一个会话失效
        mfaSessionManager.invalidateSession(sessionId1);
        
        // 清理过期会话
        mfaSessionManager.cleanupExpiredSessions();

        // Then - 只有一个会话应该存在
        assertThat(mfaSessionManager.getSession(sessionId1)).isNull();
        assertThat(mfaSessionManager.getSession(sessionId2)).isNotNull();
    }

    /**
     * 测试会话过期需要模拟时间，由于需要等待很长时间，这里不进行实际测试
     * 在实际项目中可以使用Clock抽象或时间模拟库来测试
     */
}

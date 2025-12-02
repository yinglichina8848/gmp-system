package com.gmp.auth.service.impl;

import com.gmp.auth.repository.UserPasswordHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * UserPasswordHistoryServiceImpl测试类
 */
class UserPasswordHistoryServiceImplTest {

    @Mock
    private UserPasswordHistoryRepository userPasswordHistoryRepository;

    @InjectMocks
    private UserPasswordHistoryServiceImpl userPasswordHistoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPasswordHistory_NormalUser() {
        Long userId = 1L;
        int limit = 5;
        List<String> history = userPasswordHistoryService.getPasswordHistory(userId, limit);
        assertNotNull(history);
        assertTrue(history.isEmpty());
    }

    @Test
    void testGetPasswordHistory_ZeroLimit() {
        Long userId = 1L;
        int limit = 0;
        List<String> history = userPasswordHistoryService.getPasswordHistory(userId, limit);
        assertNotNull(history);
        assertTrue(history.isEmpty());
    }

    @Test
    void testGetPasswordHistory_NegativeLimit() {
        Long userId = 1L;
        int limit = -5;
        List<String> history = userPasswordHistoryService.getPasswordHistory(userId, limit);
        assertNotNull(history);
        assertTrue(history.isEmpty());
    }

    @Test
    void testGetPasswordHistory_NullUserId() {
        Long userId = null;
        int limit = 5;
        List<String> history = userPasswordHistoryService.getPasswordHistory(userId, limit);
        assertNotNull(history);
        assertTrue(history.isEmpty());
    }

    @Test
    void testRecordPasswordHistory_NormalUser() {
        Long userId = 1L;
        String hashedPassword = "hashed_password";
        // 测试不抛出异常
        assertDoesNotThrow(() -> {
            userPasswordHistoryService.recordPasswordHistory(userId, hashedPassword);
        });
    }

    @Test
    void testRecordPasswordHistory_NullUserId() {
        Long userId = null;
        String hashedPassword = "hashed_password";
        assertDoesNotThrow(() -> {
            userPasswordHistoryService.recordPasswordHistory(userId, hashedPassword);
        });
    }

    @Test
    void testRecordPasswordHistory_NullPassword() {
        Long userId = 1L;
        String hashedPassword = null;
        assertDoesNotThrow(() -> {
            userPasswordHistoryService.recordPasswordHistory(userId, hashedPassword);
        });
    }

    @Test
    void testCleanOldPasswordHistory_NormalUser() {
        Long userId = 1L;
        int keepCount = 5;
        assertDoesNotThrow(() -> {
            userPasswordHistoryService.cleanOldPasswordHistory(userId, keepCount);
        });
    }

    @Test
    void testCleanOldPasswordHistory_ZeroKeepCount() {
        Long userId = 1L;
        int keepCount = 0;
        assertDoesNotThrow(() -> {
            userPasswordHistoryService.cleanOldPasswordHistory(userId, keepCount);
        });
    }

    @Test
    void testCleanOldPasswordHistory_NegativeKeepCount() {
        Long userId = 1L;
        int keepCount = -5;
        assertDoesNotThrow(() -> {
            userPasswordHistoryService.cleanOldPasswordHistory(userId, keepCount);
        });
    }

    @Test
    void testSavePasswordHistory_NormalUser() {
        Long userId = 1L;
        String passwordHash = "password_hash";
        assertDoesNotThrow(() -> {
            userPasswordHistoryService.savePasswordHistory(userId, passwordHash);
        });
    }

    @Test
    void testSavePasswordHistory_NullUserId() {
        Long userId = null;
        String passwordHash = "password_hash";
        assertDoesNotThrow(() -> {
            userPasswordHistoryService.savePasswordHistory(userId, passwordHash);
        });
    }

    @Test
    void testSavePasswordHistory_NullPassword() {
        Long userId = 1L;
        String passwordHash = null;
        assertDoesNotThrow(() -> {
            userPasswordHistoryService.savePasswordHistory(userId, passwordHash);
        });
    }

    @Test
    void testIsPasswordInHistory_NormalUser() {
        Long userId = 1L;
        String passwordHash = "password_hash";
        boolean result = userPasswordHistoryService.isPasswordInHistory(userId, passwordHash);
        assertFalse(result);
    }

    @Test
    void testIsPasswordInHistory_NullUserId() {
        Long userId = null;
        String passwordHash = "password_hash";
        boolean result = userPasswordHistoryService.isPasswordInHistory(userId, passwordHash);
        assertFalse(result);
    }

    @Test
    void testIsPasswordInHistory_NullPassword() {
        Long userId = 1L;
        String passwordHash = null;
        boolean result = userPasswordHistoryService.isPasswordInHistory(userId, passwordHash);
        assertFalse(result);
    }
}
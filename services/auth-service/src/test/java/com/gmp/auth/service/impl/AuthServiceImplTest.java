package com.gmp.auth.service.impl;

import com.gmp.auth.dto.*;
import com.gmp.auth.entity.User;
import com.gmp.auth.exception.*;
import com.gmp.auth.repository.UserRepository;
import com.gmp.auth.service.*;
import com.gmp.auth.util.JwtTokenProvider;
import com.gmp.auth.util.PasswordEncoder;
import com.gmp.auth.util.TotpUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * AuthServiceImpl的单元测试类
 *
 * @author GMP系统开发团队
 */
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private UserPasswordHistoryService userPasswordHistoryService;

    @Mock
    private UserRoleService userRoleService;

    @Mock
    private RolePermissionService rolePermissionService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private PasswordPolicyService passwordPolicyService;

    @Mock
    private AuditLogService auditLogService;

    @InjectMocks
    private AuthServiceImpl authService;

    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // 初始化测试用户
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("encodedPassword");
        testUser.setFullName("测试用户");
        testUser.setActive(true);
        testUser.setLocked(false);
        testUser.setExpired(false);
        testUser.setLoginAttempts(0);
        testUser.setMfaEnabled(false);
        testUser.setPasswordLastChanged(LocalDateTime.now().minusDays(30));
    }

    @Test
    void testLoadUserByUsername_Success() {
        // 准备
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(userRoleService.getUserRoleCodes(1L)).thenReturn(Set.of("USER"));
        when(rolePermissionService.getUserPermissionCodes(1L)).thenReturn(List.of("READ_DATA"));
        
        // 执行
        UserDetails userDetails = authService.loadUserByUsername("testuser");
        
        // 验证
        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
        assertEquals("encodedPassword", userDetails.getPassword());
        assertTrue(userDetails.isEnabled());
        assertFalse(userDetails.isAccountExpired());
        assertFalse(userDetails.isCredentialsNonExpired());
        assertFalse(userDetails.isAccountLocked());
        assertEquals(1, userDetails.getAuthorities().size());
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        // 准备
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());
        
        // 执行 & 验证
        assertThrows(UsernameNotFoundException.class, () -> 
            authService.loadUserByUsername("nonexistent")
        );
    }

    @Test
    void testLoadUserByUsername_AccountDisabled() {
        // 准备
        testUser.setActive(false);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        
        // 执行 & 验证
        assertThrows(AccountDisabledException.class, () -> 
            authService.loadUserByUsername("testuser")
        );
    }

    @Test
    void testLogin_Success() {
        // 准备
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("password");
        request.setMfaVerified(false);
        
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);
        when(userRoleService.getUserRoleCodes(1L)).thenReturn(Set.of("USER"));
        when(rolePermissionService.getUserPermissionCodes(1L)).thenReturn(List.of("READ_DATA"));
        when(tokenProvider.generateAccessToken(anyString(), anySet(), anyList())).thenReturn("access_token");
        when(tokenProvider.generateRefreshToken(anyString())).thenReturn("refresh_token");
        when(tokenProvider.getAccessTokenExpirationTime()).thenReturn(3600000L);
        
        // 执行
        LoginResponse response = authService.login(request, "127.0.0.1", "Mozilla/5.0");
        
        // 验证
        assertNotNull(response);
        assertEquals("access_token", response.getAccessToken());
        assertEquals("refresh_token", response.getRefreshToken());
        assertEquals("Bearer", response.getTokenType());
        assertEquals(3600, response.getExpiresIn());
        assertEquals("testuser", response.getUsername());
        assertEquals("测试用户", response.getFullName());
        assertEquals(Set.of("USER"), response.getRoles());
        assertEquals(List.of("READ_DATA"), response.getPermissions());
        
        // 验证调用了审计日志服务
        verify(auditLogService).logLoginSuccess("testuser", "127.0.0.1", "Mozilla/5.0");
    }

    @Test
    void testLogin_InvalidPassword() {
        // 准备
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("wrongpassword");
        request.setMfaVerified(false);
        
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("wrongpassword", "encodedPassword")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        
        // 执行 & 验证
        assertThrows(AuthenticationException.class, () -> 
            authService.login(request, "127.0.0.1", "Mozilla/5.0")
        );
        
        // 验证调用了审计日志服务记录失败
        verify(auditLogService).logLoginFailure("testuser", "127.0.0.1", "Mozilla/5.0", anyString());
    }

    @Test
    void testLogin_AccountLockedAfterFailedAttempts() {
        // 准备
        testUser.setLoginAttempts(4); // 已经有4次失败尝试
        
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("wrongpassword");
        request.setMfaVerified(false);
        
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("wrongpassword", "encodedPassword")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        
        // 执行 & 验证
        AccountLockedException exception = assertThrows(AccountLockedException.class, () -> 
            authService.login(request, "127.0.0.1", "Mozilla/5.0")
        );
        
        assertEquals("密码错误次数过多，账户已被锁定", exception.getMessage());
        verify(auditLogService, times(2)).logLoginFailure(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void testLogin_MfaRequired() {
        // 准备
        testUser.setMfaEnabled(true);
        
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("password");
        request.setMfaVerified(false);
        
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        
        // 执行
        LoginResponse response = authService.login(request, "127.0.0.1", "Mozilla/5.0");
        
        // 验证
        assertNotNull(response);
        assertTrue(response.isMfaRequired());
        assertNotNull(response.getSessionId());
    }

    @Test
    void testHasPermission_UserHasRequiredPermission() {
        // 准备
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(rolePermissionService.hasAnyPermission(1L, List.of("ADMINISTER"))).thenReturn(true);
        
        // 执行
        boolean result = authService.hasPermission("testuser", "ADMINISTER");
        
        // 验证
        assertTrue(result);
    }

    @Test
    void testHasPermission_UserDoesNotHaveRequiredPermission() {
        // 准备
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(rolePermissionService.hasAnyPermission(1L, List.of("ADMINISTER"))).thenReturn(false);
        
        // 执行
        boolean result = authService.hasPermission("testuser", "ADMINISTER");
        
        // 验证
        assertFalse(result);
        // 验证记录了权限拒绝日志
        verify(auditLogService).logSecurityEvent(eq(1L), eq("testuser"), eq("PERMISSION_DENIED"), 
            eq("MEDIUM"), eq("用户尝试访问未授权的资源"), eq("system"));
    }

    @Test
    void testHasRole_UserHasRequiredRole() {
        // 准备
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(userRoleService.getUserRoleCodes(1L)).thenReturn(Set.of("USER", "ADMIN"));
        
        // 执行
        boolean result = authService.hasRole("testuser", "ADMIN");
        
        // 验证
        assertTrue(result);
    }

    @Test
    void testHasRole_UserDoesNotHaveRequiredRole() {
        // 准备
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(userRoleService.getUserRoleCodes(1L)).thenReturn(Set.of("USER"));
        
        // 执行
        boolean result = authService.hasRole("testuser", "ADMIN");
        
        // 验证
        assertFalse(result);
    }

    @Test
    void testChangePassword_Success() {
        // 准备
        PasswordChangeRequest request = new PasswordChangeRequest();
        request.setOldPassword("oldpassword");
        request.setNewPassword("NewPassword123!");
        
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("oldpassword", "encodedPassword")).thenReturn(true);
        when(passwordPolicyService.validatePassword("NewPassword123!")).thenReturn(true);
        when(userPasswordHistoryService.isPasswordInHistory(1L, "NewPassword123!")).thenReturn(false);
        when(passwordEncoder.encode("NewPassword123!")).thenReturn("newEncodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        
        // 执行
        boolean result = authService.changePassword("testuser", request);
        
        // 验证
        assertTrue(result);
        verify(userPasswordHistoryService).savePasswordHistory(1L, "encodedPassword");
        verify(auditLogService).logPasswordChange("testuser");
    }

    @Test
    void testChangePassword_InvalidOldPassword() {
        // 准备
        PasswordChangeRequest request = new PasswordChangeRequest();
        request.setOldPassword("wrongpassword");
        request.setNewPassword("NewPassword123!");
        
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("wrongpassword", "encodedPassword")).thenReturn(false);
        
        // 执行 & 验证
        assertThrows(InvalidPasswordException.class, () -> 
            authService.changePassword("testuser", request)
        );
    }

    @Test
    void testChangePassword_InvalidNewPassword() {
        // 准备
        PasswordChangeRequest request = new PasswordChangeRequest();
        request.setOldPassword("oldpassword");
        request.setNewPassword("weak");
        
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("oldpassword", "encodedPassword")).thenReturn(true);
        when(passwordPolicyService.validatePassword("weak")).thenReturn(false);
        
        // 执行 & 验证
        assertThrows(InvalidPasswordException.class, () -> 
            authService.changePassword("testuser", request)
        );
    }

    @Test
    void testVerifyMfa_Success() {
        // 准备
        testUser.setMfaSecretKey("JBSWY3DPEHPK3PXP");
        testUser.setLastLoginSession("test-session-id");
        
        MfaVerifyRequest request = new MfaVerifyRequest();
        request.setUsername("testuser");
        request.setMfaCode("123456"); // 实际测试中应该使用有效代码
        request.setSessionId("test-session-id");
        
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(TotpUtils.verifyCode("JBSWY3DPEHPK3PXP", "123456")).thenReturn(true);
        when(userRoleService.getUserRoleCodes(1L)).thenReturn(Set.of("USER"));
        when(rolePermissionService.getUserPermissionCodes(1L)).thenReturn(List.of("READ_DATA"));
        when(tokenProvider.generateAccessToken(anyString(), anySet(), anyList())).thenReturn("access_token");
        when(tokenProvider.generateRefreshToken(anyString())).thenReturn("refresh_token");
        when(tokenProvider.getAccessTokenExpirationTime()).thenReturn(3600000L);
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        
        // 执行
        LoginResponse response = authService.verifyMfa(request);
        
        // 验证
        assertNotNull(response);
        assertEquals("access_token", response.getAccessToken());
        assertEquals("refresh_token", response.getRefreshToken());
        // 验证记录了安全事件日志
        verify(auditLogService).logSecurityEvent(eq(1L), eq("testuser"), 
            eq("MFA_VERIFICATION_SUCCESS"), eq("INFO"), eq("MFA验证成功"), eq("system"));
    }

    @Test
    void testVerifyMfa_InvalidSession() {
        // 准备
        testUser.setLastLoginSession("different-session-id");
        
        MfaVerifyRequest request = new MfaVerifyRequest();
        request.setUsername("testuser");
        request.setMfaCode("123456");
        request.setSessionId("test-session-id");
        
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        
        // 执行 & 验证
        assertThrows(InvalidSessionException.class, () -> 
            authService.verifyMfa(request)
        );
        verify(auditLogService).logLoginFailure("testuser", "unknown", "mfa_verification", "无效的会话");
    }

    @Test
    void testLogout() {
        // 执行
        authService.logout("testuser", "token");
        
        // 验证
        verify(auditLogService).logLogout("testuser");
    }

    @Test
    void testResetPassword_Success() {
        // 准备
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(passwordPolicyService.validatePassword("NewPassword123!")).thenReturn(true);
        when(passwordEncoder.encode("NewPassword123!")).thenReturn("newEncodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        
        // 执行
        boolean result = authService.resetPassword("testuser", "NewPassword123!");
        
        // 验证
        assertTrue(result);
        verify(userPasswordHistoryService).savePasswordHistory(1L, "encodedPassword");
        verify(auditLogService).logPasswordReset("testuser");
    }

    @Test
    void testRegenerateRecoveryCodes() {
        // 准备
        testUser.setMfaEnabled(true);
        testUser.setRecoveryCodesHash("existingCodesHash");
        
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.encode(anyString())).thenReturn("newEncodedRecoveryCodes");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        
        // 执行
        List<String> recoveryCodes = authService.regenerateRecoveryCodes("testuser");
        
        // 验证
        assertNotNull(recoveryCodes);
        assertEquals(10, recoveryCodes.size()); // 应该生成10个恢复码
    }

    @Test
    void testRegenerateRecoveryCodes_MfaNotEnabled() {
        // 准备
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        
        // 执行 & 验证
        assertThrows(MfaNotEnabledException.class, () -> 
            authService.regenerateRecoveryCodes("testuser")
        );
    }
}
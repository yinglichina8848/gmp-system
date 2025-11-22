package com.gmp.auth.service.impl;

import com.gmp.auth.dto.*;
import com.gmp.auth.service.AuthService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthServiceImpl implements AuthService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        throw new UsernameNotFoundException("用户不存在");
    }

    @Override
    public LoginResponse login(LoginRequest request, String ipAddress, String userAgent) {
        return new LoginResponse();
    }

    @Override
    public void logout(String username, String token) {
    }

    @Override
    public TokenResponse refreshToken(String refreshToken) {
        return new TokenResponse(null, 0);
    }

    @Override
    public boolean validateToken(String token) {
        return false;
    }

    @Override
    public boolean hasPermission(String username, String... requiredPermissions) {
        return false;
    }

    @Override
    public boolean hasRole(String username, String... requiredRoles) {
        return false;
    }

    @Override
    public List<String> getUserPermissions(String username) {
        return Collections.emptyList();
    }

    @Override
    public Set<String> getUserRoles(String username) {
        return Collections.emptySet();
    }

    @Override
    public Map<String, String> generateMfaSetup(String username) {
        return Collections.emptyMap();
    }

    @Override
    public Map<String, Object> enableMfa(MfaEnableRequest request) {
        return Collections.emptyMap();
    }

    @Override
    public void disableMfa(String username) {
    }

    @Override
    public LoginResponse verifyMfa(MfaVerifyRequest request) {
        return new LoginResponse();
    }

    @Override
    public LoginResponse loginWithRecoveryCode(String username, String recoveryCode) {
        return new LoginResponse();
    }

    @Override
    public List<String> regenerateRecoveryCodes(String username) {
        return Collections.emptyList();
    }

    @Override
    public boolean changePassword(String username, PasswordChangeRequest request) {
        return false;
    }

    @Override
    public boolean resetPassword(String username, String newPassword) {
        return false;
    }

    @Override
    public String getPasswordComplexityRequirements() {
        return "";
    }
}
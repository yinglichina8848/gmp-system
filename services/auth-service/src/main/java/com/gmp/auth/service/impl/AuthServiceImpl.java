package com.gmp.auth.service.impl;

import com.gmp.auth.dto.LoginRequest;
import com.gmp.auth.dto.LoginResponse;
import com.gmp.auth.dto.PasswordChangeRequest;
import com.gmp.auth.dto.TokenResponse;
import com.gmp.auth.dto.MfaVerifyRequest;
import com.gmp.auth.service.AuthService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
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
        TokenResponse response = new TokenResponse();
        response.setAccessToken(null);
        response.setExpiresIn(0L);
        return response;
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
    public boolean changePassword(String username, PasswordChangeRequest request) {
        return false;
    }

    @Override
    public boolean resetPassword(String username, String newPassword) {
        return false;
    }

    @Override
    public List<String> regenerateRecoveryCodes(String username) {
        // 返回空列表作为占位符实现
        return Collections.emptyList();
    }

    @Override
    public String getPasswordComplexityRequirements() {
        return "No requirements";
    }

    @Override
    public LoginResponse verifyMfa(MfaVerifyRequest request) {
        return new LoginResponse();
    }
}
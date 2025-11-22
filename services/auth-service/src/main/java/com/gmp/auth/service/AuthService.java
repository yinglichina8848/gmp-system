package com.gmp.auth.service;

import com.gmp.auth.dto.LoginRequest;
import com.gmp.auth.dto.LoginResponse;
import com.gmp.auth.dto.MfaVerifyRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 认证服务接口
 *
 * @author GMP系统开发团队
 */
public interface AuthService extends UserDetailsService {

    /**
     * 用户登录
     *
     * @param request   登录请求
     * @param ipAddress 客户端IP地址
     * @param userAgent 用户代理
     * @return 登录响应，包含JWT令牌
     */
    LoginResponse login(LoginRequest request, String ipAddress, String userAgent);

    /**
     * 用户登出
     *
     * @param username 用户名
     * @param token    JWT令牌
     */
    void logout(String username, String token);

    /**
     * 刷新访问令牌
     *
     * @param refreshToken 刷新令牌
     * @return 新的令牌响应
     */
    com.gmp.auth.dto.TokenResponse refreshToken(String refreshToken);

    /**
     * 验证JWT令牌
     *
     * @param token JWT令牌
     * @return 是否有效
     */
    boolean validateToken(String token);

    /**
     * 验证用户权限
     *
     * @param username            用户名
     * @param requiredPermissions 需要的权限列表
     * @return 是否有权限
     */
    boolean hasPermission(String username, String... requiredPermissions);

    /**
     * 验证用户角色
     *
     * @param username      用户名
     * @param requiredRoles 需要的角色列表
     * @return 是否有角色
     */
    boolean hasRole(String username, String... requiredRoles);

    /**
     * 获取用户所有权限
     *
     * @param username 用户名
     * @return 权限列表
     */
    java.util.List<String> getUserPermissions(String username);

    /**
     * 获取用户所有角色
     *
     * @param username 用户名
     * @return 角色列表
     */
    java.util.Set<String> getUserRoles(String username);
    
    /**
     * 修改密码
     *
     * @param username 用户名
     * @param request 密码修改请求
     * @return 是否成功
     */
    boolean changePassword(String username, com.gmp.auth.dto.PasswordChangeRequest request);
    
    /**
     * 重置密码
     *
     * @param username 用户名
     * @param newPassword 新密码
     * @return 是否成功
     */
    boolean resetPassword(String username, String newPassword);
    
    /**
     * 获取密码复杂度要求
     *
     * @return 密码复杂度要求描述
     */
    String getPasswordComplexityRequirements();
    
    /**
     * 验证MFA
     *
     * @param request MFA验证请求
     * @return 登录响应
     */
    LoginResponse verifyMfa(MfaVerifyRequest request);
    
    /**
     * 重新生成恢复码
     *
     * @param username 用户名
     * @return 新的恢复码列表
     */
    java.util.List<String> regenerateRecoveryCodes(String username);
}

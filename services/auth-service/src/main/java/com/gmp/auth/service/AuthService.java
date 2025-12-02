package com.gmp.auth.service;

import com.gmp.auth.dto.LoginRequest;
import com.gmp.auth.dto.LoginResponse;
import com.gmp.auth.dto.MfaVerifyRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @brief 认证服务接口
 * 
 * @details 该接口定义了GMP系统的认证和授权服务，包括用户登录、登出、令牌管理、权限验证等功能。
 * 继承自Spring Security的UserDetailsService，用于用户信息加载。
 * 
 * @author GMP系统开发团队
 * @version 1.0
 * @since 2023-01-01
 * 
 * @see org.springframework.security.core.userdetails.UserDetailsService
 */
public interface AuthService extends UserDetailsService {

    /**
     * @brief 用户登录
     * 
     * @details 处理用户登录请求，验证用户凭证，生成JWT令牌
     * 
     * @param request 登录请求，包含用户名和密码
     * @param ipAddress 客户端IP地址，用于审计日志
     * @param userAgent 用户代理，用于审计日志
     * @return LoginResponse 登录响应，包含JWT令牌和用户信息
     * 
     * @see com.gmp.auth.dto.LoginRequest
     * @see com.gmp.auth.dto.LoginResponse
     */
    LoginResponse login(LoginRequest request, String ipAddress, String userAgent);

    /**
     * @brief 用户登出
     * 
     * @details 处理用户登出请求，将JWT令牌加入黑名单
     * 
     * @param username 用户名
     * @param token JWT令牌
     */
    void logout(String username, String token);

    /**
     * @brief 刷新访问令牌
     * 
     * @details 使用刷新令牌生成新的访问令牌
     * 
     * @param refreshToken 刷新令牌
     * @return com.gmp.auth.dto.TokenResponse 新的令牌响应
     * 
     * @see com.gmp.auth.dto.TokenResponse
     */
    com.gmp.auth.dto.TokenResponse refreshToken(String refreshToken);

    /**
     * @brief 验证JWT令牌
     * 
     * @details 验证JWT令牌的有效性，包括签名、过期时间等
     * 
     * @param token JWT令牌
     * @return boolean 如果令牌有效返回true，否则返回false
     */
    boolean validateToken(String token);

    /**
     * @brief 验证用户权限
     * 
     * @details 验证用户是否拥有指定的权限
     * 
     * @param username 用户名
     * @param requiredPermissions 需要的权限列表
     * @return boolean 如果用户拥有所有指定权限返回true，否则返回false
     */
    boolean hasPermission(String username, String... requiredPermissions);

    /**
     * @brief 验证用户角色
     * 
     * @details 验证用户是否拥有指定的角色
     * 
     * @param username 用户名
     * @param requiredRoles 需要的角色列表
     * @return boolean 如果用户拥有所有指定角色返回true，否则返回false
     */
    boolean hasRole(String username, String... requiredRoles);

    /**
     * @brief 获取用户所有权限
     * 
     * @details 获取用户拥有的所有权限列表
     * 
     * @param username 用户名
     * @return java.util.List<String> 用户权限列表
     */
    java.util.List<String> getUserPermissions(String username);

    /**
     * @brief 获取用户所有角色
     * 
     * @details 获取用户拥有的所有角色列表
     * 
     * @param username 用户名
     * @return java.util.Set<String> 用户角色列表
     */
    java.util.Set<String> getUserRoles(String username);
    
    /**
     * @brief 修改密码
     * 
     * @details 处理用户密码修改请求
     * 
     * @param username 用户名
     * @param request 密码修改请求，包含旧密码和新密码
     * @return boolean 如果密码修改成功返回true，否则返回false
     * 
     * @see com.gmp.auth.dto.PasswordChangeRequest
     */
    boolean changePassword(String username, com.gmp.auth.dto.PasswordChangeRequest request);
    
    /**
     * @brief 重置密码
     * 
     * @details 重置用户密码
     * 
     * @param username 用户名
     * @param newPassword 新密码
     * @return boolean 如果密码重置成功返回true，否则返回false
     */
    boolean resetPassword(String username, String newPassword);
    
    /**
     * @brief 获取密码复杂度要求
     * 
     * @details 获取系统的密码复杂度要求描述
     * 
     * @return String 密码复杂度要求描述
     */
    String getPasswordComplexityRequirements();
    
    /**
     * @brief 验证MFA
     * 
     * @details 验证多因素认证码
     * 
     * @param request MFA验证请求，包含用户名和MFA码
     * @return LoginResponse 登录响应，包含JWT令牌
     * 
     * @see com.gmp.auth.dto.MfaVerifyRequest
     * @see com.gmp.auth.dto.LoginResponse
     */
    LoginResponse verifyMfa(MfaVerifyRequest request);
    
    /**
     * @brief 重新生成恢复码
     * 
     * @details 为用户重新生成MFA恢复码
     * 
     * @param username 用户名
     * @return java.util.List<String> 新的恢复码列表
     */
    java.util.List<String> regenerateRecoveryCodes(String username);
    
    /**
     * @brief 验证用户是否有子系统访问权限
     * 
     * @details 验证用户是否可以访问指定的子系统
     * 
     * @param username 用户名
     * @param subsystemCode 子系统代码
     * @return boolean 如果用户有访问权限返回true，否则返回false
     */
    boolean hasSubsystemAccess(String username, String subsystemCode);

    /**
     * @brief 获取用户对子系统的访问级别
     * 
     * @details 获取用户对指定子系统的访问级别
     * 
     * @param username 用户名
     * @param subsystemCode 子系统代码
     * @return Integer 访问级别，无权限返回0
     */
    Integer getSubsystemAccessLevel(String username, String subsystemCode);

    /**
     * @brief 获取用户可访问的子系统列表
     * 
     * @details 获取用户可以访问的所有子系统列表
     * 
     * @param username 用户名
     * @return List<String> 可访问的子系统列表
     */
    List<String> getUserAccessibleSubsystems(String username);
    
    /**
     * @brief 获取用户对子系统的访问级别映射
     * 
     * @details 获取用户对所有子系统的访问级别映射
     * 
     * @param username 用户名
     * @return Map<String, Integer> 子系统代码与访问级别的映射
     */
    Map<String, Integer> getUserSubsystemAccessLevels(String username);
    
    /**
     * @brief 检查用户是否有子系统的写入权限
     * 
     * @details 检查用户是否对指定子系统有写入权限
     * 
     * @param username 用户名
     * @param subsystemCode 子系统代码
     * @return boolean 如果用户有写入权限返回true，否则返回false
     */
    boolean hasSubsystemWriteAccess(String username, String subsystemCode);

    /**
     * @brief 获取用户可访问的子系统代码集合
     * 
     * @details 获取用户可以访问的所有子系统代码集合
     * 
     * @param username 用户名
     * @return Set<String> 子系统代码集合
     */
    Set<String> getUserAccessibleSubsystemCodes(String username);

    /**
     * @brief 检查用户是否有子系统的管理权限
     * 
     * @details 检查用户是否对指定子系统有管理权限
     * 
     * @param username 用户名
     * @param subsystemCode 子系统代码
     * @return boolean 如果用户有管理权限返回true，否则返回false
     */
    boolean hasSubsystemAdminAccess(String username, String subsystemCode);
}

package com.gmp.auth.service;

import com.gmp.auth.config.JwtConfig;
import com.gmp.auth.dto.LoginRequest;
import com.gmp.auth.dto.LoginResponse;
import com.gmp.auth.dto.TokenResponse;
import com.gmp.auth.entity.OperationLog;
import com.gmp.auth.entity.User;
import com.gmp.auth.repository.OperationLogRepository;
import com.gmp.auth.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 认证服务实现类
 *
 * @author GMP系统开发团队
 */
@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private OperationLogRepository operationLogRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtConfig jwtConfig;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    @Transactional
    public LoginResponse login(LoginRequest request, String ipAddress, String userAgent) {
        String username = request.getUsername();
        String password = request.getPassword();

        // 查找用户
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户名或密码错误"));

        // 检查用户状态
        if (user.getUserStatus() == User.UserStatus.LOCKED) {
            // 记录失败登录
            logFailedLoginAttempt(user, "账户已锁定", ipAddress, userAgent);
            throw new RuntimeException("账户已被锁定，请联系管理员");
        }

        // 检查密码
        if (!passwordEncoder.matches(password, user.getPassword())) {
            // 增加登录尝试次数
            user.incrementLoginAttempts();
            
            // 如果尝试次数超过限制，锁定账户
            if (user.getLoginAttempts() >= 5) {
                user.lockAccount(15); // 锁定15分钟
                logFailedLoginAttempt(user, "密码错误，账户已锁定", ipAddress, userAgent);
                throw new RuntimeException("密码错误次数过多，账户已被锁定");
            }
            
            userRepository.save(user);
            logFailedLoginAttempt(user, "密码错误", ipAddress, userAgent);
            throw new RuntimeException("用户名或密码错误");
        }

        // 登录成功，重置登录尝试次数
        user.resetLoginAttempts();
        user.setLastLoginTime(LocalDateTime.now());
        user.setLastLoginIp(ipAddress);
        userRepository.save(user);

        // 获取用户角色和权限
        Set<String> roles = new HashSet<>();
        List<String> permissions = new ArrayList<>();
        
        // 假设roles现在已经是字符串集合
        roles.addAll(user.getRoles());
        // 假设permissions已经是字符串列表
        permissions.addAll(user.getPermissions());
        // 注意：这里假设User类已经实现了getPermissions()方法

        // 生成JWT令牌
        String accessToken = jwtConfig.generateToken(username, roles, permissions);
        String refreshToken = jwtConfig.generateRefreshToken(username);

        // 记录成功登录
        logSuccessfulLogin(user, ipAddress, userAgent);

        // 创建登录响应
        LoginResponse response = new LoginResponse();
        // 直接设置字段值，不使用setter方法以避免Lombok问题
        response.accessToken = accessToken;
        response.refreshToken = refreshToken;
        response.tokenType = "Bearer";
        response.expiresIn = jwtConfig.getExpiration() / 1000;
        response.username = user.getUsername();
        response.fullName = user.getFullName();
        response.roles = new ArrayList<>(roles);
        response.permissions = permissions;
        response.loginTime = LocalDateTime.now();
        return response;
    }

    @Override
    @Transactional
    public void logout(String username, String token) {
        // 将令牌加入黑名单
        String tokenKey = "blacklist:token:" + UUID.randomUUID();
        redisTemplate.opsForValue().set(tokenKey, token);
        redisTemplate.expire(tokenKey, jwtConfig.getExpiration(), TimeUnit.MILLISECONDS);

        // 记录登出日志
        operationLogRepository.save(
                OperationLog.createLogoutLog(
                        userRepository.findByUsername(username)
                                .map(User::getId)
                                .orElse(null),
                        username,
                        null));
    }

    @Override
    public TokenResponse refreshToken(String refreshToken) {
        // 检查刷新令牌是否有效
        if (!jwtConfig.validateToken(refreshToken)) {
            throw new RuntimeException("无效的刷新令牌");
        }

        // 从令牌中获取用户名
        String username = jwtConfig.extractUsername(refreshToken);
        
        // 查找用户
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        // 获取用户角色和权限
        Set<String> roles = new HashSet<>();
        List<String> permissions = new ArrayList<>();
        
        // 假设roles现在已经是字符串集合
        roles.addAll(user.getRoles());
        // 假设permissions已经是字符串列表
        permissions.addAll(user.getPermissions());

        // 生成新的访问令牌
        String accessToken = jwtConfig.refreshAccessToken(refreshToken, roles, permissions);

        // 创建令牌响应
        TokenResponse response = new TokenResponse();
        // 显式使用setter方法
        response.setAccessToken(accessToken);
        response.setTokenType("Bearer");
        response.setExpiresIn(jwtConfig.getExpiration() / 1000);
        return response;
    }

    @Override
    public boolean validateToken(String token) {
        return jwtConfig.validateToken(token);
    }

    /**
     * 记录登录失败
     */
    private void logFailedLoginAttempt(User user, String reason, String ipAddress, String userAgent) {
        log.warn("登录失败 - 用户名: {}, 原因: {}", user.getUsername(), reason);
        operationLogRepository.save(
                OperationLog.createLoginLog(
                        user.getId(),
                        user.getUsername(),
                        ipAddress,
                        userAgent,
                        false));
    }

    /**
     * 记录登录成功
     */
    private void logSuccessfulLogin(User user, String ipAddress, String userAgent) {
        log.info("登录成功 - 用户名: {}", user.getUsername());
        operationLogRepository.save(
                OperationLog.createLoginLog(
                        user.getId(),
                        user.getUsername(),
                        ipAddress,
                        userAgent,
                        true));
    }
    
    @Override
    public java.util.Set<String> getUserRoles(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在: " + username));
        return user.getRoles();
    }
    
    @Override
    public java.util.List<String> getUserPermissions(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在: " + username));
        return user.getPermissions();
    }
    
    @Override
    public boolean hasPermission(String username, String... requiredPermissions) {
        java.util.List<String> userPermissions = getUserPermissions(username);
        java.util.Set<String> requiredPermSet = java.util.Arrays.stream(requiredPermissions).collect(java.util.stream.Collectors.toSet());
        return userPermissions.containsAll(requiredPermSet);
    }
    
    @Override
    public boolean hasRole(String username, String... requiredRoles) {
        java.util.Set<String> userRoles = getUserRoles(username);
        java.util.Set<String> requiredRoleSet = java.util.Arrays.stream(requiredRoles).collect(java.util.stream.Collectors.toSet());
        return userRoles.containsAll(requiredRoleSet);
    }
}

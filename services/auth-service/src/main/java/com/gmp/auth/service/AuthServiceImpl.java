package com.gmp.auth.service;

import com.gmp.auth.config.JwtConfig;
import com.gmp.auth.dto.LoginRequest;
import com.gmp.auth.dto.LoginResponse;
import com.gmp.auth.dto.TokenResponse;
import com.gmp.auth.entity.OperationLog;
import com.gmp.auth.entity.User;
import com.gmp.auth.repository.UserRepository;
import com.gmp.auth.repository.OperationLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final OperationLogRepository operationLogRepository;
    private final JwtConfig jwtConfig;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String USER_PERMISSIONS_CACHE = "auth:user:permissions:";
    private static final String USER_ROLES_CACHE = "auth:user:roles:";
    private static final String BLACKLIST_TOKEN_PREFIX = "auth:blacklist:token:";

    @Override
    @Transactional
    public LoginResponse login(LoginRequest request, String ipAddress, String userAgent) {
        String usernameOrEmail = request.getUsername();

        // 查找用户
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail)
                .orElseThrow(() -> new RuntimeException("用户名或密码错误"));

        // 验证密码
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            // 记录失败登录尝试
            logFailedLoginAttempt(user, ipAddress, userAgent, false);
            incrementLoginAttempts(user);
            throw new RuntimeException("用户名或密码错误");
        }

        // 检查用户状态
        if (!isUserActive(user)) {
            logFailedLoginAttempt(user, ipAddress, userAgent, false);
            throw new RuntimeException("账号已被锁定或禁用");
        }

        // 获取用户角色和权限
        Set<String> roles = getUserRoles(user.getUsername());
        List<String> permissions = getUserPermissions(user.getUsername());

        // 生成令牌
        String accessToken = jwtConfig.generateToken(user.getUsername(), roles, permissions);
        String refreshToken = jwtConfig.generateRefreshToken(user.getUsername());

        // 更新用户登录信息
        updateLoginInfo(user, ipAddress);

        // 记录成功登录
        logSuccessfulLogin(user, ipAddress, userAgent);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtConfig.getExpiration() / 1000)
                .username(user.getUsername())
                .fullName(user.getFullName())
                .roles(roles)
                .permissions(permissions)
                .loginTime(LocalDateTime.now())
                .build();
    }

    @Override
    @Transactional
    public void logout(String username, String token) {
        // 将令牌加入黑名单
        redisTemplate.opsForValue().set(
                BLACKLIST_TOKEN_PREFIX + extractTokenId(token),
                "blacklisted",
                jwtConfig.getRemainingValidity(token),
                TimeUnit.SECONDS);

        // 记录登出日志
        operationLogRepository.save(
                OperationLog.createLogoutLog(
                        userRepository.findByUsername(username)
                                .map(User::getId)
                                .orElse(null),
                        username,
                        "web-client"));
    }

    @Override
    public TokenResponse refreshToken(String refreshToken) {
        if (!jwtConfig.validateToken(refreshToken)) {
            throw new RuntimeException("无效的刷新令牌");
        }

        String username = jwtConfig.extractUsername(refreshToken);
        Set<String> roles = getUserRoles(username);
        List<String> permissions = getUserPermissions(username);

        String newAccessToken = jwtConfig.refreshAccessToken(refreshToken, roles, permissions);

        return TokenResponse.builder()
                .accessToken(newAccessToken)
                .tokenType("Bearer")
                .expiresIn(jwtConfig.getExpiration() / 1000)
                .build();
    }

    @Override
    public boolean validateToken(String token) {
        // 检查是否在黑名单中
        String blacklisted = (String) redisTemplate.opsForValue()
                .get(BLACKLIST_TOKEN_PREFIX + extractTokenId(token));
        if ("blacklisted".equals(blacklisted)) {
            return false;
        }

        return jwtConfig.validateToken(token);
    }

    @Override
    public boolean hasPermission(String username, String... requiredPermissions) {
        List<String> userPermissions = getUserPermissions(username);
        return Arrays.stream(requiredPermissions)
                .allMatch(userPermissions::contains);
    }

    @Override
    public boolean hasRole(String username, String... requiredRoles) {
        Set<String> userRoles = getUserRoles(username);
        return Arrays.stream(requiredRoles)
                .allMatch(userRoles::contains);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String> getUserPermissions(String username) {
        String cacheKey = USER_PERMISSIONS_CACHE + username;
        List<String> cached = (List<String>) redisTemplate.opsForValue().get(cacheKey);

        if (cached != null) {
            return cached;
        }

        // 这里需要实现从数据库查询用户权限的逻辑
        // 暂时返回空的权限列表，在实际实现中需要关联UserRole和RolePermission表
        List<String> permissions = new ArrayList<>();
        // TODO: 实现权限查询逻辑

        // 缓存权限
        redisTemplate.opsForValue().set(cacheKey, permissions, 30, TimeUnit.MINUTES);
        return permissions;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<String> getUserRoles(String username) {
        String cacheKey = USER_ROLES_CACHE + username;
        Set<String> cached = (Set<String>) redisTemplate.opsForValue().get(cacheKey);

        if (cached != null) {
            return cached;
        }

        // 这里需要实现从数据库查询用户角色的逻辑
        // 暂时返回操作员角色
        Set<String> roles = new HashSet<>();
        roles.add("ROLE_OPERATOR");
        // TODO: 实现角色查询逻辑

        // 缓存角色
        redisTemplate.opsForValue().set(cacheKey, roles, 30, TimeUnit.MINUTES);
        return roles;
    }

    /**
     * 检查用户是否活跃
     */
    private boolean isUserActive(User user) {
        return user.getUserStatus() == User.UserStatus.ACTIVE && !user.isLocked();
    }

    /**
     * 更新登录信息
     */
    private void updateLoginInfo(User user, String ipAddress) {
        user.setLastLoginTime(LocalDateTime.now());
        user.setLastLoginIp(ipAddress);
        user.resetLoginAttempts();
        userRepository.save(user);
    }

    /**
     * 增加登录尝试次数
     */
    private void incrementLoginAttempts(User user) {
        user.incrementLoginAttempts();
        if (user.getLoginAttempts() >= 5) {
            user.lockAccount(15); // 锁定15分钟
        }
        userRepository.save(user);
    }

    /**
     * 记录登录失败
     */
    private void logFailedLoginAttempt(User user, String ipAddress, String userAgent, boolean rememberMe) {
        operationLogRepository.save(
                OperationLog.createLoginLog(user.getId(), user.getUsername(), ipAddress, userAgent, false));
    }

    /**
     * 记录登录成功
     */
    private void logSuccessfulLogin(User user, String ipAddress, String userAgent) {
        operationLogRepository.save(
                OperationLog.createLoginLog(user.getId(), user.getUsername(), ipAddress, userAgent, true));
    }

    /**
     * 提取令牌ID用于黑名单
     */
    private String extractTokenId(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length >= 2) {
                // 使用令牌的前半部分作为ID
                return parts[0] + "." + parts[1];
            }
        } catch (Exception e) {
            log.warn("提取令牌ID失败: {}", e.getMessage());
        }
        // 如果无法提取，使用哈希值
        return String.valueOf(token.hashCode());
    }
}

package com.gmp.auth.service;

import java.util.List;

/**
 * 用户密码历史服务接口
 */
public interface UserPasswordHistoryService {

    /**
     * 获取用户的密码历史记录
     * @param userId 用户ID
     * @param limit 限制返回记录数
     * @return 密码历史记录列表（哈希值）
     */
    List<String> getPasswordHistory(Long userId, int limit);

    /**
     * 记录密码历史
     * @param userId 用户ID
     * @param hashedPassword 哈希后的密码
     */
    void recordPasswordHistory(Long userId, String hashedPassword);

    /**
     * 清理过旧的密码历史记录
     * @param userId 用户ID
     * @param keepCount 保留的记录数
     */
    void cleanOldPasswordHistory(Long userId, int keepCount);
    
    /**
     * 检查密码是否在历史记录中
     * @param userId 用户ID
     * @param password 密码
     * @return 是否在历史记录中
     */
    boolean isPasswordInHistory(Long userId, String password);
    
    /**
     * 保存密码历史记录
     * @param userId 用户ID
     * @param password 密码
     */
    void savePasswordHistory(Long userId, String password);
}
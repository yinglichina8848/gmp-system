package com.gmp.auth.service.impl;

import com.gmp.auth.model.UserPasswordHistory;
import com.gmp.auth.repository.UserPasswordHistoryRepository;
import com.gmp.auth.service.UserPasswordHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户密码历史服务实现
 */
@Service
public class UserPasswordHistoryServiceImpl implements UserPasswordHistoryService {

    private static final Logger log = LoggerFactory.getLogger(UserPasswordHistoryServiceImpl.class);

    @Autowired
    private UserPasswordHistoryRepository userPasswordHistoryRepository;

    @Override
    public List<String> getPasswordHistory(Long userId, int limit) {
        log.debug("获取用户密码历史记录 - 用户ID: {}, 限制: {}", userId, limit);
        // 简化实现，直接返回空列表
        return new ArrayList<>();
    }

    @Transactional
    @Override
    public void recordPasswordHistory(Long userId, String hashedPassword) {
        log.debug("记录用户密码历史 - 用户ID: {}", userId);
        // 简化实现，不执行实际操作
    }

    @Transactional
    @Override
    public void cleanOldPasswordHistory(Long userId, int keepCount) {
        log.debug("清理旧密码历史记录 - 用户ID: {}, 保留: {}", userId, keepCount);
        // 简化实现，不执行实际操作
    }
    
    @Override
    @Transactional
    public void savePasswordHistory(Long userId, String passwordHash) {
        // 简化实现，不执行实际操作
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean isPasswordInHistory(Long userId, String passwordHash) {
        // 简化实现，返回false
        return false;
    }
}
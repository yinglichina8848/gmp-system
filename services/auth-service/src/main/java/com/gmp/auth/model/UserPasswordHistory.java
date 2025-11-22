package com.gmp.auth.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 用户密码历史记录实体
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "user_password_history")
public class UserPasswordHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    /**
     * 构造函数
     * @param userId 用户ID
     * @param passwordHash 密码哈希值
     */
    public UserPasswordHistory(Long userId, String passwordHash) {
        this.userId = userId;
        this.passwordHash = passwordHash;
    }
}
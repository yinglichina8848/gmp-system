package com.gmp.qms.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * CAPA行动项实体类
 * 
 * @author GMP系统开发团队
 */
@Entity
@Table(name = "capa_actions")
@Data
public class CapaAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "capa_id", nullable = false)
    private Capa capa;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "responsible_person_id", nullable = false)
    private Long responsiblePersonId;

    @Column(name = "due_date", nullable = false)
    private LocalDateTime dueDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ActionStatus status;

    @Column(name = "completion_date")
    private LocalDateTime completionDate;

    @Column(name = "completion_notes", columnDefinition = "TEXT")
    private String completionNotes;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @Column(name = "updated_by")
    private Long updatedBy;

    /**
     * 行动项状态枚举
     */
    public enum ActionStatus {
        PENDING,       // 待处理
        IN_PROGRESS,   // 进行中
        COMPLETED,     // 已完成
        VERIFIED       // 已验证
    }
}
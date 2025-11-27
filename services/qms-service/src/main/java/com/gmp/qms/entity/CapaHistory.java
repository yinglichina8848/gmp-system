package com.gmp.qms.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * CAPA历史记录实体类
 * 
 * @author GMP系统开发团队
 */
@Entity
@Table(name = "capa_histories")
@Data
public class CapaHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "capa_id", nullable = false)
    private Capa capa;

    @Column(name = "action", nullable = false)
    private String action;

    @Column(name = "status_before")
    private String statusBefore;

    @Column(name = "status_after")
    private String statusAfter;

    @Column(name = "comment")
    private String comment;

    @Column(name = "performed_by", nullable = false)
    private Long performedBy;

    @CreationTimestamp
    @Column(name = "performed_at", nullable = false)
    private LocalDateTime performedAt;
}
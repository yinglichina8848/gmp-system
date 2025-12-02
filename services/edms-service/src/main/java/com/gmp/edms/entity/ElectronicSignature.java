package com.gmp.edms.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

/**
 * 电子签名实体
 */
@Entity
@Table(name = "electronic_signatures")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ElectronicSignature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "document_id")
    private Document document;

    @ManyToOne
    @JoinColumn(name = "document_version_id")
    private DocumentVersion documentVersion;

    @Column(name = "signer_id", nullable = false)
    private String signerId;

    @Column(name = "signer_name")
    private String signerName;

    @Column(name = "signer_role")
    private String signerRole;

    @Column(name = "signature_type", nullable = false)
    private String signatureType;

    @Column(name = "signature_data")
    private String signatureData;

    @Column(name = "signature_time", nullable = false)
    private LocalDateTime signatureTime;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "certificate_info")
    private String certificateInfo;

    @Column(name = "validity_status")
    private String validityStatus;

    @Column(name = "verification_result")
    private Boolean verificationResult;
}

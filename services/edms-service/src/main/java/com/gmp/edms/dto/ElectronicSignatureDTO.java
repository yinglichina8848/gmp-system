package com.gmp.edms.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

/**
 * 电子签名DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ElectronicSignatureDTO {

    private Long id;

    private Long documentId;

    private Long documentVersionId;

    private String signerId;

    private String signerName;

    private String signerRole;

    private String signatureType;

    private String signatureData;

    private LocalDateTime signatureTime;

    private String ipAddress;

    private String certificateInfo;

    private String validityStatus;

    private Boolean verificationResult;
}

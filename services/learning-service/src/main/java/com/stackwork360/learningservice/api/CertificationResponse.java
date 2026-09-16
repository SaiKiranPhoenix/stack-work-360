package com.stackwork360.learningservice.api;

import com.stackwork360.learningservice.domain.Certification;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record CertificationResponse(UUID id, String tenantId, String name, String issuer, List<UUID> requiredResourceIds, int validityMonths, LocalDate availableFrom) {
    static CertificationResponse from(Certification certification) {
        return new CertificationResponse(certification.id(), certification.tenantId(), certification.name(), certification.issuer(), certification.requiredResourceIds(), certification.validityMonths(), certification.availableFrom());
    }
}

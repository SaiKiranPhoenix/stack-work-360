package com.stackwork360.auditservice.api;

import com.stackwork360.auditservice.domain.AuditRetentionPolicy;
import java.time.Instant;

public record AuditRetentionPolicyResponse(
        String tenantId,
        int standardRetentionDays,
        int restrictedRetentionDays,
        boolean legalHoldEnabled,
        Instant updatedAt
) {
    static AuditRetentionPolicyResponse from(AuditRetentionPolicy policy) {
        return new AuditRetentionPolicyResponse(
                policy.tenantId(),
                policy.standardRetentionDays(),
                policy.restrictedRetentionDays(),
                policy.legalHoldEnabled(),
                policy.updatedAt()
        );
    }
}

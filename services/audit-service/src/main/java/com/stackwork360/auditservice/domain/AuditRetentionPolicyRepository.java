package com.stackwork360.auditservice.domain;

import java.util.Optional;

public interface AuditRetentionPolicyRepository {
    AuditRetentionPolicy save(AuditRetentionPolicy policy);

    Optional<AuditRetentionPolicy> findByTenantId(String tenantId);
}

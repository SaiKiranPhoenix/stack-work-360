package com.stackwork360.auditservice.infrastructure;

import com.stackwork360.auditservice.domain.AuditRetentionPolicy;
import com.stackwork360.auditservice.domain.AuditRetentionPolicyRepository;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryAuditRetentionPolicyRepository implements AuditRetentionPolicyRepository {
    private final Map<String, AuditRetentionPolicy> policies = new ConcurrentHashMap<>();

    @Override
    public AuditRetentionPolicy save(AuditRetentionPolicy policy) {
        policies.put(policy.tenantId(), policy);
        return policy;
    }

    @Override
    public Optional<AuditRetentionPolicy> findByTenantId(String tenantId) {
        return Optional.ofNullable(policies.get(tenantId));
    }
}

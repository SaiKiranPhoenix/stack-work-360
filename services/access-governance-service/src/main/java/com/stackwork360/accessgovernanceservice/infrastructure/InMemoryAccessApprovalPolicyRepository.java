package com.stackwork360.accessgovernanceservice.infrastructure;

import com.stackwork360.accessgovernanceservice.domain.AccessApprovalPolicy;
import com.stackwork360.accessgovernanceservice.domain.AccessApprovalPolicyRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryAccessApprovalPolicyRepository implements AccessApprovalPolicyRepository {
    private final Map<UUID, AccessApprovalPolicy> policies = new ConcurrentHashMap<>();

    public AccessApprovalPolicy save(AccessApprovalPolicy policy) {
        policies.put(policy.id(), policy);
        return policy;
    }

    public List<AccessApprovalPolicy> findByTenantId(String tenantId) {
        return policies.values().stream()
                .filter(policy -> policy.tenantId().equals(tenantId))
                .sorted(Comparator.comparing(policy -> policy.resourceType().name()))
                .toList();
    }
}

package com.stackwork360.documentservice.infrastructure;

import com.stackwork360.documentservice.domain.PolicyAcknowledgement;
import com.stackwork360.documentservice.domain.PolicyAcknowledgementRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryPolicyAcknowledgementRepository implements PolicyAcknowledgementRepository {
    private final ConcurrentMap<UUID, PolicyAcknowledgement> acknowledgementsById = new ConcurrentHashMap<>();

    @Override
    public PolicyAcknowledgement save(PolicyAcknowledgement acknowledgement) {
        acknowledgementsById.put(acknowledgement.id(), acknowledgement);
        return acknowledgement;
    }

    @Override
    public Optional<PolicyAcknowledgement> findByDocumentIdAndWorkerId(UUID documentId, String workerId) {
        return acknowledgementsById.values().stream()
                .filter(acknowledgement -> acknowledgement.documentId().equals(documentId))
                .filter(acknowledgement -> acknowledgement.workerId().equals(workerId))
                .findFirst();
    }

    @Override
    public List<PolicyAcknowledgement> findByTenantId(String tenantId) {
        return acknowledgementsById.values().stream()
                .filter(acknowledgement -> acknowledgement.tenantId().equals(tenantId))
                .sorted(Comparator.comparing(PolicyAcknowledgement::acknowledgedAt))
                .toList();
    }
}

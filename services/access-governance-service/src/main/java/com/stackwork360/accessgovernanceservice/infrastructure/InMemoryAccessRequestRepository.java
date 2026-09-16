package com.stackwork360.accessgovernanceservice.infrastructure;

import com.stackwork360.accessgovernanceservice.domain.AccessRequest;
import com.stackwork360.accessgovernanceservice.domain.AccessRequestRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryAccessRequestRepository implements AccessRequestRepository {
    private final Map<UUID, AccessRequest> requests = new ConcurrentHashMap<>();

    public AccessRequest save(AccessRequest request) {
        requests.put(request.id(), request);
        return request;
    }

    public Optional<AccessRequest> findById(UUID id) {
        return Optional.ofNullable(requests.get(id));
    }

    public List<AccessRequest> findByTenantId(String tenantId) {
        return requests.values().stream()
                .filter(request -> request.tenantId().equals(tenantId))
                .sorted(Comparator.comparing(AccessRequest::requestedAt).reversed())
                .toList();
    }
}

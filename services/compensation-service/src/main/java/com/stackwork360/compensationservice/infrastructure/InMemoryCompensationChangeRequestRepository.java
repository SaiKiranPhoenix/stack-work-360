package com.stackwork360.compensationservice.infrastructure;

import com.stackwork360.compensationservice.domain.CompensationChangeRequest;
import com.stackwork360.compensationservice.domain.CompensationChangeRequestRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryCompensationChangeRequestRepository implements CompensationChangeRequestRepository {
    private final Map<UUID, CompensationChangeRequest> requests = new ConcurrentHashMap<>();

    public CompensationChangeRequest save(CompensationChangeRequest request) {
        requests.put(request.id(), request);
        return request;
    }

    public Optional<CompensationChangeRequest> findById(UUID id) {
        return Optional.ofNullable(requests.get(id));
    }

    public List<CompensationChangeRequest> findByTenantId(String tenantId) {
        return requests.values().stream()
                .filter(request -> request.tenantId().equals(tenantId))
                .sorted(Comparator.comparing(CompensationChangeRequest::createdAt).reversed())
                .toList();
    }
}

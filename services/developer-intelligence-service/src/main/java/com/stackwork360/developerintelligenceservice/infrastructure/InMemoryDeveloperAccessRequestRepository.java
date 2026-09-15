package com.stackwork360.developerintelligenceservice.infrastructure;

import com.stackwork360.developerintelligenceservice.domain.DeveloperAccessRequest;
import com.stackwork360.developerintelligenceservice.domain.DeveloperAccessRequestRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryDeveloperAccessRequestRepository implements DeveloperAccessRequestRepository {
    private final Map<UUID, DeveloperAccessRequest> requests = new ConcurrentHashMap<>();

    @Override
    public DeveloperAccessRequest save(DeveloperAccessRequest request) {
        requests.put(request.id(), request);
        return request;
    }

    @Override
    public Optional<DeveloperAccessRequest> findById(UUID id) {
        return Optional.ofNullable(requests.get(id));
    }

    @Override
    public List<DeveloperAccessRequest> findByTenantId(String tenantId) {
        return requests.values().stream()
                .filter(request -> request.tenantId().equals(tenantId))
                .sorted(Comparator.comparing(DeveloperAccessRequest::createdAt).reversed())
                .toList();
    }

    @Override
    public List<DeveloperAccessRequest> findByRepositoryId(UUID repositoryId) {
        return requests.values().stream()
                .filter(request -> request.repositoryId().equals(repositoryId))
                .sorted(Comparator.comparing(DeveloperAccessRequest::createdAt).reversed())
                .toList();
    }
}

package com.stackwork360.talentmarketplaceservice.infrastructure;

import com.stackwork360.talentmarketplaceservice.domain.InternalApplication;
import com.stackwork360.talentmarketplaceservice.domain.InternalApplicationRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryInternalApplicationRepository implements InternalApplicationRepository {
    private final Map<UUID, InternalApplication> applications = new ConcurrentHashMap<>();

    public InternalApplication save(InternalApplication application) {
        applications.put(application.id(), application);
        return application;
    }

    public Optional<InternalApplication> findById(UUID id) {
        return Optional.ofNullable(applications.get(id));
    }

    public List<InternalApplication> findByWorker(String tenantId, String workerId) {
        return applications.values().stream()
                .filter(application -> application.tenantId().equals(tenantId))
                .filter(application -> application.workerId().equals(workerId))
                .sorted(Comparator.comparing(InternalApplication::appliedAt).reversed())
                .toList();
    }

    public List<InternalApplication> findByTenantId(String tenantId) {
        return applications.values().stream()
                .filter(application -> application.tenantId().equals(tenantId))
                .sorted(Comparator.comparing(InternalApplication::appliedAt).reversed())
                .toList();
    }
}

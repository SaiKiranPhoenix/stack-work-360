package com.stackwork360.learningservice.infrastructure;

import com.stackwork360.learningservice.domain.LearningResource;
import com.stackwork360.learningservice.domain.LearningResourceRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryLearningResourceRepository implements LearningResourceRepository {
    private final Map<UUID, LearningResource> resources = new ConcurrentHashMap<>();

    public LearningResource save(LearningResource resource) {
        resources.put(resource.id(), resource);
        return resource;
    }

    public Optional<LearningResource> findById(UUID id) {
        return Optional.ofNullable(resources.get(id));
    }

    public List<LearningResource> findByTenantId(String tenantId) {
        return resources.values().stream()
                .filter(resource -> resource.tenantId().equals(tenantId))
                .sorted(Comparator.comparing(LearningResource::title))
                .toList();
    }
}

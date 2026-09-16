package com.stackwork360.accessgovernanceservice.infrastructure;

import com.stackwork360.accessgovernanceservice.domain.AccessResource;
import com.stackwork360.accessgovernanceservice.domain.AccessResourceRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryAccessResourceRepository implements AccessResourceRepository {
    private final Map<String, AccessResource> resources = new ConcurrentHashMap<>();

    public AccessResource save(AccessResource resource) {
        resources.put(key(resource.tenantId(), resource.resourceCode()), resource);
        return resource;
    }

    public Optional<AccessResource> findByCode(String tenantId, String resourceCode) {
        return Optional.ofNullable(resources.get(key(tenantId, resourceCode)));
    }

    public List<AccessResource> findByTenantId(String tenantId) {
        return resources.values().stream()
                .filter(resource -> resource.tenantId().equals(tenantId))
                .sorted(Comparator.comparing(AccessResource::resourceCode))
                .toList();
    }

    private String key(String tenantId, String resourceCode) {
        return tenantId + ":" + resourceCode;
    }
}

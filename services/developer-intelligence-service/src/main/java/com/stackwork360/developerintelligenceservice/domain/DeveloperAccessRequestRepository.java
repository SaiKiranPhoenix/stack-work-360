package com.stackwork360.developerintelligenceservice.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeveloperAccessRequestRepository {
    DeveloperAccessRequest save(DeveloperAccessRequest request);

    Optional<DeveloperAccessRequest> findById(UUID id);

    List<DeveloperAccessRequest> findByTenantId(String tenantId);

    List<DeveloperAccessRequest> findByRepositoryId(UUID repositoryId);
}

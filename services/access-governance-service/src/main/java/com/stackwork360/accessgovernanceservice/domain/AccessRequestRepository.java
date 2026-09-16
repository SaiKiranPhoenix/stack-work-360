package com.stackwork360.accessgovernanceservice.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccessRequestRepository {
    AccessRequest save(AccessRequest request);
    Optional<AccessRequest> findById(UUID id);
    List<AccessRequest> findByTenantId(String tenantId);
}

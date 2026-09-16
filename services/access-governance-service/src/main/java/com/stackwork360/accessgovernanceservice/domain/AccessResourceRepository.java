package com.stackwork360.accessgovernanceservice.domain;

import java.util.List;
import java.util.Optional;

public interface AccessResourceRepository {
    AccessResource save(AccessResource resource);
    Optional<AccessResource> findByCode(String tenantId, String resourceCode);
    List<AccessResource> findByTenantId(String tenantId);
}

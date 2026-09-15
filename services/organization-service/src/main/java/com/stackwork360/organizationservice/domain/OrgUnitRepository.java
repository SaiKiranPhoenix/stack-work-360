package com.stackwork360.organizationservice.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrgUnitRepository {
    OrgUnit save(OrgUnit orgUnit);

    Optional<OrgUnit> findById(UUID id);

    List<OrgUnit> findByTenantId(String tenantId);

    List<OrgUnit> findChildren(String tenantId, UUID parentId);

    boolean existsByTenantIdAndName(String tenantId, String name);
}

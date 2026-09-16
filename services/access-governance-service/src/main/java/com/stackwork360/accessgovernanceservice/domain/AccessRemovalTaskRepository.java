package com.stackwork360.accessgovernanceservice.domain;

import java.util.List;

public interface AccessRemovalTaskRepository {
    AccessRemovalTask save(AccessRemovalTask task);
    List<AccessRemovalTask> findByTenantId(String tenantId);
}

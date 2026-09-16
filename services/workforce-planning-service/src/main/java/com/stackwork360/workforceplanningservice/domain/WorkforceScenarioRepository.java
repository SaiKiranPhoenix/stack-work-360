package com.stackwork360.workforceplanningservice.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkforceScenarioRepository {
    WorkforceScenario save(WorkforceScenario scenario);
    Optional<WorkforceScenario> findById(UUID id);
    List<WorkforceScenario> findByTenantId(String tenantId);
}

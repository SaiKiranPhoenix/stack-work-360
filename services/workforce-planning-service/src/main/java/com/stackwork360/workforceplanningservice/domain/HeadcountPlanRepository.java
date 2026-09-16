package com.stackwork360.workforceplanningservice.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HeadcountPlanRepository {
    HeadcountPlan save(HeadcountPlan plan);
    Optional<HeadcountPlan> findById(UUID id);
    List<HeadcountPlan> findByTenantId(String tenantId);
}

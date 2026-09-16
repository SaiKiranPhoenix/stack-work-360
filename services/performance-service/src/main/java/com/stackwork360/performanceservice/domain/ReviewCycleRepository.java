package com.stackwork360.performanceservice.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewCycleRepository {
    ReviewCycle save(ReviewCycle cycle);
    Optional<ReviewCycle> findById(UUID id);
    List<ReviewCycle> findByTenantId(String tenantId);
}

package com.stackwork360.performanceservice.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewTemplateRepository {
    ReviewTemplate save(ReviewTemplate template);
    Optional<ReviewTemplate> findById(UUID id);
    List<ReviewTemplate> findByTenantId(String tenantId);
}

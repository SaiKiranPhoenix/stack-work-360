package com.stackwork360.learningservice.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LearningResourceRepository {
    LearningResource save(LearningResource resource);
    Optional<LearningResource> findById(UUID id);
    List<LearningResource> findByTenantId(String tenantId);
}

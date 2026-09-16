package com.stackwork360.learningservice.domain;

import java.util.List;
import java.util.UUID;

public interface LearningPathRepository {
    LearningPath save(LearningPath path);
    List<LearningPath> findByTenantId(String tenantId);
    List<LearningPath> findByRoleTarget(String tenantId, String roleTarget);
}

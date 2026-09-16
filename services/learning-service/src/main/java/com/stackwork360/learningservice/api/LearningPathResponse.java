package com.stackwork360.learningservice.api;

import com.stackwork360.learningservice.domain.LearningPath;
import java.util.List;
import java.util.UUID;

public record LearningPathResponse(UUID id, String tenantId, String title, String roleTarget, List<UUID> resourceIds, boolean active) {
    static LearningPathResponse from(LearningPath path) {
        return new LearningPathResponse(path.id(), path.tenantId(), path.title(), path.roleTarget(), path.resourceIds(), path.active());
    }
}

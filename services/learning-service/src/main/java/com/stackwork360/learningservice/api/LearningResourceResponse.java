package com.stackwork360.learningservice.api;

import com.stackwork360.learningservice.domain.LearningResource;
import com.stackwork360.learningservice.domain.LearningResourceType;
import java.util.List;
import java.util.UUID;

public record LearningResourceResponse(UUID id, String tenantId, String title, LearningResourceType type, String provider, int durationMinutes, List<String> skills, boolean active) {
    static LearningResourceResponse from(LearningResource resource) {
        return new LearningResourceResponse(resource.id(), resource.tenantId(), resource.title(), resource.type(), resource.provider(), resource.durationMinutes(), resource.skills(), resource.active());
    }
}

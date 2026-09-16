package com.stackwork360.learningservice.api;

import com.stackwork360.learningservice.domain.RecommendationInput;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record RecommendationInputResponse(UUID id, String tenantId, String workerId, String roleTarget, List<String> skillGaps, Instant capturedAt) {
    static RecommendationInputResponse from(RecommendationInput input) {
        return new RecommendationInputResponse(input.id(), input.tenantId(), input.workerId(), input.roleTarget(), input.skillGaps(), input.capturedAt());
    }
}

package com.stackwork360.learningservice.domain;

import java.util.List;
import java.util.UUID;

public record LearningRecommendation(
        String workerId,
        String roleTarget,
        List<UUID> resourceIds
) {
}

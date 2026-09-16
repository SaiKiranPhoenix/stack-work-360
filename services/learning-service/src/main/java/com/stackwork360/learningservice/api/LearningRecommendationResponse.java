package com.stackwork360.learningservice.api;

import com.stackwork360.learningservice.domain.LearningRecommendation;
import java.util.List;
import java.util.UUID;

public record LearningRecommendationResponse(String workerId, String roleTarget, List<UUID> resourceIds) {
    static LearningRecommendationResponse from(LearningRecommendation recommendation) {
        return new LearningRecommendationResponse(recommendation.workerId(), recommendation.roleTarget(), recommendation.resourceIds());
    }
}

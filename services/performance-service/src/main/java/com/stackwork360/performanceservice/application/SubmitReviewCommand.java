package com.stackwork360.performanceservice.application;

import com.stackwork360.performanceservice.domain.ReviewType;
import java.util.Map;
import java.util.UUID;

public record SubmitReviewCommand(
        String tenantId,
        UUID cycleId,
        String subjectWorkerId,
        String reviewerWorkerId,
        ReviewType type,
        int rating,
        String narrative,
        Map<String, Integer> competencyRatings
) {
}

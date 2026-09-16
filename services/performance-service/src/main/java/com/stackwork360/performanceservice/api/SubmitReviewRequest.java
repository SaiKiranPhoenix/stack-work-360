package com.stackwork360.performanceservice.api;

import com.stackwork360.performanceservice.application.SubmitReviewCommand;
import com.stackwork360.performanceservice.domain.ReviewType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import java.util.UUID;

public record SubmitReviewRequest(
        @NotNull UUID cycleId,
        @NotBlank String subjectWorkerId,
        @NotBlank String reviewerWorkerId,
        @NotNull ReviewType type,
        @Min(1) @Max(5) int rating,
        @NotBlank String narrative,
        @NotEmpty Map<String, Integer> competencyRatings
) {
    SubmitReviewCommand toCommand(String tenantId) {
        return new SubmitReviewCommand(tenantId, cycleId, subjectWorkerId, reviewerWorkerId, type, rating, narrative, competencyRatings);
    }
}

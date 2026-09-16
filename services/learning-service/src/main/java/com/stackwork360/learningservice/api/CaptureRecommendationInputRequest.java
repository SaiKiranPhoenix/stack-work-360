package com.stackwork360.learningservice.api;

import com.stackwork360.learningservice.application.CaptureRecommendationInputCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record CaptureRecommendationInputRequest(
        @NotBlank String workerId,
        @NotBlank String roleTarget,
        @NotEmpty List<String> skillGaps
) {
    CaptureRecommendationInputCommand toCommand(String tenantId) {
        return new CaptureRecommendationInputCommand(tenantId, workerId, roleTarget, skillGaps);
    }
}

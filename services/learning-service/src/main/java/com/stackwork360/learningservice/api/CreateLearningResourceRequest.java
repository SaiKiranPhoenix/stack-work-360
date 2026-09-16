package com.stackwork360.learningservice.api;

import com.stackwork360.learningservice.application.CreateLearningResourceCommand;
import com.stackwork360.learningservice.domain.LearningResourceType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record CreateLearningResourceRequest(
        @NotBlank String title,
        @NotNull LearningResourceType type,
        @NotBlank String provider,
        @Min(1) int durationMinutes,
        @NotEmpty List<String> skills,
        boolean active
) {
    CreateLearningResourceCommand toCommand(String tenantId) {
        return new CreateLearningResourceCommand(tenantId, title, type, provider, durationMinutes, skills, active);
    }
}

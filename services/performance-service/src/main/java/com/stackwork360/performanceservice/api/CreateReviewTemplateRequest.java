package com.stackwork360.performanceservice.api;

import com.stackwork360.performanceservice.application.CreateReviewTemplateCommand;
import com.stackwork360.performanceservice.domain.ReviewType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record CreateReviewTemplateRequest(
        @NotBlank String name,
        @NotNull ReviewType type,
        @NotEmpty List<String> competencies,
        boolean active
) {
    CreateReviewTemplateCommand toCommand(String tenantId) {
        return new CreateReviewTemplateCommand(tenantId, name, type, competencies, active);
    }
}

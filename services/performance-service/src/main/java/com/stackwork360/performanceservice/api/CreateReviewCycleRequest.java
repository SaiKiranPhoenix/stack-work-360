package com.stackwork360.performanceservice.api;

import com.stackwork360.performanceservice.application.CreateReviewCycleCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record CreateReviewCycleRequest(
        @NotBlank String name,
        @NotNull LocalDate startsOn,
        @NotNull LocalDate endsOn
) {
    CreateReviewCycleCommand toCommand(String tenantId) {
        return new CreateReviewCycleCommand(tenantId, name, startsOn, endsOn);
    }
}

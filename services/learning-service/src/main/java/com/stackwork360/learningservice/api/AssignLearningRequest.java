package com.stackwork360.learningservice.api;

import com.stackwork360.learningservice.application.AssignLearningCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

public record AssignLearningRequest(
        @NotBlank String workerId,
        @NotBlank String managerId,
        @NotNull UUID resourceId,
        @NotNull LocalDate dueOn
) {
    AssignLearningCommand toCommand(String tenantId) {
        return new AssignLearningCommand(tenantId, workerId, managerId, resourceId, dueOn);
    }
}

package com.stackwork360.learningservice.api;

import com.stackwork360.learningservice.application.CreateLearningPathCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import java.util.UUID;

public record CreateLearningPathRequest(
        @NotBlank String title,
        @NotBlank String roleTarget,
        @NotEmpty List<UUID> resourceIds,
        boolean active
) {
    CreateLearningPathCommand toCommand(String tenantId) {
        return new CreateLearningPathCommand(tenantId, title, roleTarget, resourceIds, active);
    }
}

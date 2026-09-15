package com.stackwork360.developerintelligenceservice.api;

import com.stackwork360.developerintelligenceservice.application.RequestDeveloperAccessCommand;
import com.stackwork360.developerintelligenceservice.domain.DeveloperAccessLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record RequestDeveloperAccessRequest(
        @NotNull UUID repositoryId,
        @NotBlank String developerId,
        @NotNull DeveloperAccessLevel requestedLevel,
        @NotBlank String reason
) {
    RequestDeveloperAccessCommand toCommand(String tenantId) {
        return new RequestDeveloperAccessCommand(tenantId, repositoryId, developerId, requestedLevel, reason);
    }
}

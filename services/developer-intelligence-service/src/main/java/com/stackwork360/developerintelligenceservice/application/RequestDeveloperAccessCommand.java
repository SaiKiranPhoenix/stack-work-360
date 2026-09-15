package com.stackwork360.developerintelligenceservice.application;

import com.stackwork360.developerintelligenceservice.domain.DeveloperAccessLevel;
import java.util.UUID;

public record RequestDeveloperAccessCommand(
        String tenantId,
        UUID repositoryId,
        String developerId,
        DeveloperAccessLevel requestedLevel,
        String reason
) {
}

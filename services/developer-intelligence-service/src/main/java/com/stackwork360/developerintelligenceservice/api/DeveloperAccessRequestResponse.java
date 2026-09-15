package com.stackwork360.developerintelligenceservice.api;

import com.stackwork360.developerintelligenceservice.domain.DeveloperAccessLevel;
import com.stackwork360.developerintelligenceservice.domain.DeveloperAccessRequest;
import com.stackwork360.developerintelligenceservice.domain.DeveloperAccessStatus;
import java.time.Instant;
import java.util.UUID;

public record DeveloperAccessRequestResponse(
        UUID id,
        String tenantId,
        UUID repositoryId,
        String developerId,
        DeveloperAccessLevel requestedLevel,
        String reason,
        DeveloperAccessStatus status,
        String decidedBy,
        Instant decidedAt,
        Instant createdAt,
        Instant updatedAt
) {
    static DeveloperAccessRequestResponse from(DeveloperAccessRequest request) {
        return new DeveloperAccessRequestResponse(
                request.id(),
                request.tenantId(),
                request.repositoryId(),
                request.developerId(),
                request.requestedLevel(),
                request.reason(),
                request.status(),
                request.decidedBy(),
                request.decidedAt(),
                request.createdAt(),
                request.updatedAt()
        );
    }
}

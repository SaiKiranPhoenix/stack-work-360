package com.stackwork360.developerintelligenceservice.application;

import com.stackwork360.developerintelligenceservice.domain.RepositoryProvider;
import java.time.Instant;

public record GitWebhookCommand(
        String tenantId,
        RepositoryProvider provider,
        String externalId,
        String repositoryName,
        String defaultBranch,
        String developerId,
        int commitCount,
        Instant lastContributionAt
) {
}

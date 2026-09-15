package com.stackwork360.developerintelligenceservice.api;

import com.stackwork360.developerintelligenceservice.application.GitWebhookCommand;
import com.stackwork360.developerintelligenceservice.domain.RepositoryProvider;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public record GitWebhookRequest(
        @NotNull RepositoryProvider provider,
        @NotBlank String externalId,
        @NotBlank String repositoryName,
        @NotBlank String defaultBranch,
        @NotBlank String developerId,
        @Min(1) int commitCount,
        @NotNull Instant lastContributionAt
) {
    GitWebhookCommand toCommand(String tenantId) {
        return new GitWebhookCommand(
                tenantId,
                provider,
                externalId,
                repositoryName,
                defaultBranch,
                developerId,
                commitCount,
                lastContributionAt
        );
    }
}

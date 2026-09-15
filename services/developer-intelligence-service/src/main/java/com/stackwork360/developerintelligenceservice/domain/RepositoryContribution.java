package com.stackwork360.developerintelligenceservice.domain;

import java.time.Instant;
import java.util.Objects;

public record RepositoryContribution(
        String developerId,
        int commitCount,
        Instant lastContributionAt
) {
    public RepositoryContribution {
        developerId = requireText(developerId, "developer id is required");
        if (commitCount <= 0) {
            throw new IllegalArgumentException("commit count must be positive");
        }
        lastContributionAt = Objects.requireNonNull(lastContributionAt, "last contribution time is required");
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}

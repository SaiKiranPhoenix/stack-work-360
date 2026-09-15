package com.stackwork360.developerintelligenceservice.api;

import com.stackwork360.developerintelligenceservice.domain.RepositoryContribution;
import java.time.Instant;

public record RepositoryContributionResponse(
        String developerId,
        int commitCount,
        Instant lastContributionAt
) {
    static RepositoryContributionResponse from(RepositoryContribution contribution) {
        return new RepositoryContributionResponse(
                contribution.developerId(),
                contribution.commitCount(),
                contribution.lastContributionAt()
        );
    }
}

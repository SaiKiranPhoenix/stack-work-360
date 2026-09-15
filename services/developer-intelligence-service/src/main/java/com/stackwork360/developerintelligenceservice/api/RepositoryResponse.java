package com.stackwork360.developerintelligenceservice.api;

import com.stackwork360.developerintelligenceservice.domain.CodeRepository;
import com.stackwork360.developerintelligenceservice.domain.RepositoryLifecycle;
import com.stackwork360.developerintelligenceservice.domain.RepositoryProvider;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record RepositoryResponse(
        UUID id,
        String tenantId,
        RepositoryProvider provider,
        String externalId,
        String name,
        String defaultBranch,
        RepositoryLifecycle lifecycle,
        String serviceName,
        String owningTeam,
        List<String> primaryMaintainers,
        List<CodeAreaResponse> codeAreas,
        List<RepositoryContributionResponse> contributions,
        int busFactor,
        boolean busFactorRisk,
        Instant createdAt,
        Instant updatedAt
) {
    static RepositoryResponse from(CodeRepository repository) {
        return new RepositoryResponse(
                repository.id(),
                repository.tenantId(),
                repository.provider(),
                repository.externalId(),
                repository.name(),
                repository.defaultBranch(),
                repository.lifecycle(),
                repository.ownership().serviceName(),
                repository.ownership().owningTeam(),
                repository.ownership().primaryMaintainers(),
                repository.ownership().codeAreas().stream().map(CodeAreaResponse::from).toList(),
                repository.contributions().stream().map(RepositoryContributionResponse::from).toList(),
                repository.busFactor(),
                repository.busFactorRisk(),
                repository.createdAt(),
                repository.updatedAt()
        );
    }
}

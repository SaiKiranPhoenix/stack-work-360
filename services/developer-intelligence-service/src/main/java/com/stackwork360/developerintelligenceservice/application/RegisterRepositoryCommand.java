package com.stackwork360.developerintelligenceservice.application;

import com.stackwork360.developerintelligenceservice.domain.RepositoryProvider;
import java.util.List;

public record RegisterRepositoryCommand(
        String tenantId,
        RepositoryProvider provider,
        String externalId,
        String name,
        String defaultBranch,
        String serviceName,
        String owningTeam,
        List<String> primaryMaintainers,
        List<CodeAreaCommand> codeAreas
) {
}

package com.stackwork360.developerintelligenceservice.application;

import com.stackwork360.developerintelligenceservice.domain.RepositoryLifecycle;
import java.util.List;

public record UpdateRepositoryCommand(
        String name,
        String defaultBranch,
        RepositoryLifecycle lifecycle,
        String serviceName,
        String owningTeam,
        List<String> primaryMaintainers,
        List<CodeAreaCommand> codeAreas
) {
}

package com.stackwork360.developerintelligenceservice.application;

import com.stackwork360.developerintelligenceservice.domain.CodeArea;
import java.util.List;
import java.util.UUID;

public record OwnershipMapEntry(
        UUID repositoryId,
        String repositoryName,
        String serviceName,
        String owningTeam,
        List<String> primaryMaintainers,
        List<CodeArea> codeAreas,
        int busFactor,
        boolean busFactorRisk
) {
}

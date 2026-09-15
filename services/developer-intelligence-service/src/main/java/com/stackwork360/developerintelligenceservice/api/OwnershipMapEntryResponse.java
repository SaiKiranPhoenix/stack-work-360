package com.stackwork360.developerintelligenceservice.api;

import com.stackwork360.developerintelligenceservice.application.OwnershipMapEntry;
import java.util.List;
import java.util.UUID;

public record OwnershipMapEntryResponse(
        UUID repositoryId,
        String repositoryName,
        String serviceName,
        String owningTeam,
        List<String> primaryMaintainers,
        List<CodeAreaResponse> codeAreas,
        int busFactor,
        boolean busFactorRisk
) {
    static OwnershipMapEntryResponse from(OwnershipMapEntry entry) {
        return new OwnershipMapEntryResponse(
                entry.repositoryId(),
                entry.repositoryName(),
                entry.serviceName(),
                entry.owningTeam(),
                entry.primaryMaintainers(),
                entry.codeAreas().stream().map(CodeAreaResponse::from).toList(),
                entry.busFactor(),
                entry.busFactorRisk()
        );
    }
}

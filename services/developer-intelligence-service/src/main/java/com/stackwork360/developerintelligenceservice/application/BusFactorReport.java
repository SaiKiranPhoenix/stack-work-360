package com.stackwork360.developerintelligenceservice.application;

import java.util.List;

public record BusFactorReport(
        String tenantId,
        int repositoryCount,
        int riskyRepositoryCount,
        List<OwnershipMapEntry> riskyRepositories
) {
}

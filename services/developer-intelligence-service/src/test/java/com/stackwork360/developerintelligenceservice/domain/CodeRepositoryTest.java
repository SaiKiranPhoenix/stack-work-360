package com.stackwork360.developerintelligenceservice.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;

class CodeRepositoryTest {
    @Test
    void calculatesBusFactorFromPrimaryMaintainers() {
        CodeRepository repository = CodeRepository.register(
                "tenant-1",
                RepositoryProvider.GITHUB,
                "repo-1",
                "people-service",
                "main",
                ServiceOwnership.create("people-service", "hr-platform", List.of("dev-1", "dev-2"), List.of())
        );

        assertEquals(2, repository.busFactor());
        assertFalse(repository.busFactorRisk());
    }

    @Test
    void marksSingleMaintainerRepositoryAsRisky() {
        CodeRepository repository = CodeRepository.register(
                "tenant-1",
                RepositoryProvider.GITHUB,
                "repo-1",
                "payroll-service",
                "main",
                ServiceOwnership.create("payroll-service", "finance-platform", List.of("dev-1"), List.of())
        );

        assertEquals(1, repository.busFactor());
        assertTrue(repository.busFactorRisk());
    }

    @Test
    void recordsLatestContributionPerDeveloper() {
        CodeRepository repository = CodeRepository.register(
                "tenant-1",
                RepositoryProvider.GITHUB,
                "repo-1",
                "workflow-service",
                "main",
                ServiceOwnership.create("workflow-service", "platform", List.of("dev-1"), List.of())
        );

        repository.recordContribution("dev-1", 2, Instant.parse("2026-01-01T00:00:00Z"));
        repository.recordContribution("dev-1", 5, Instant.parse("2026-01-02T00:00:00Z"));

        assertEquals(1, repository.contributions().size());
        assertEquals(5, repository.contributions().get(0).commitCount());
    }

    @Test
    void rejectsInvalidContributionCount() {
        CodeRepository repository = CodeRepository.register(
                "tenant-1",
                RepositoryProvider.GITHUB,
                "repo-1",
                "workflow-service",
                "main",
                ServiceOwnership.create("workflow-service", "platform", List.of("dev-1"), List.of())
        );

        assertThrows(IllegalArgumentException.class, () -> repository.recordContribution("dev-1", 0, Instant.now()));
    }
}

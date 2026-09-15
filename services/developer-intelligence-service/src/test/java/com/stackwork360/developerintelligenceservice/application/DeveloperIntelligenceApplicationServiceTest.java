package com.stackwork360.developerintelligenceservice.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.stackwork360.developerintelligenceservice.domain.DeveloperAccessLevel;
import com.stackwork360.developerintelligenceservice.domain.DeveloperAccessRequest;
import com.stackwork360.developerintelligenceservice.domain.DeveloperAccessStatus;
import com.stackwork360.developerintelligenceservice.domain.RepositoryProvider;
import com.stackwork360.developerintelligenceservice.infrastructure.InMemoryCodeRepositoryRepository;
import com.stackwork360.developerintelligenceservice.infrastructure.InMemoryDeveloperAccessRequestRepository;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;

class DeveloperIntelligenceApplicationServiceTest {
    private final DeveloperIntelligenceApplicationService service = new DeveloperIntelligenceApplicationService(
            new InMemoryCodeRepositoryRepository(),
            new InMemoryDeveloperAccessRequestRepository()
    );

    @Test
    void buildsOwnershipMapAndBusFactorReport() {
        service.register(new RegisterRepositoryCommand(
                "tenant-1",
                RepositoryProvider.GITHUB,
                "repo-1",
                "people-service",
                "main",
                "people-service",
                "hr-platform",
                List.of("dev-1"),
                List.of(new CodeAreaCommand("/services/people/**", "hr-platform"))
        ));

        BusFactorReport report = service.busFactor("tenant-1");

        assertEquals(1, report.repositoryCount());
        assertEquals(1, report.riskyRepositoryCount());
        assertTrue(report.riskyRepositories().get(0).busFactorRisk());
    }

    @Test
    void ingestsWebhookContributionForExistingRepository() {
        service.register(new RegisterRepositoryCommand(
                "tenant-1",
                RepositoryProvider.GITHUB,
                "repo-1",
                "workflow-service",
                "main",
                "workflow-service",
                "platform",
                List.of("dev-1", "dev-2"),
                List.of()
        ));

        service.ingestGitWebhook(new GitWebhookCommand(
                "tenant-1",
                RepositoryProvider.GITHUB,
                "repo-1",
                "workflow-service",
                "main",
                "dev-3",
                4,
                Instant.parse("2026-01-01T00:00:00Z")
        ));

        assertEquals(1, service.repositories("tenant-1").get(0).contributions().size());
        assertEquals("dev-3", service.repositories("tenant-1").get(0).contributions().get(0).developerId());
    }

    @Test
    void handlesAccessApprovalLifecycle() {
        var repository = service.register(new RegisterRepositoryCommand(
                "tenant-1",
                RepositoryProvider.GITHUB,
                "repo-1",
                "workflow-service",
                "main",
                "workflow-service",
                "platform",
                List.of("dev-1", "dev-2"),
                List.of()
        ));
        DeveloperAccessRequest request = service.requestAccess(new RequestDeveloperAccessCommand(
                "tenant-1",
                repository.id(),
                "dev-3",
                DeveloperAccessLevel.WRITE,
                "Handle workflow incidents"
        ));

        DeveloperAccessRequest approved = service.approveAccess(request.id(), "lead-1");

        assertEquals(DeveloperAccessStatus.APPROVED, approved.status());
    }
}

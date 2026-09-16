package com.stackwork360.accessgovernanceservice.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.stackwork360.accessgovernanceservice.domain.*;
import com.stackwork360.accessgovernanceservice.infrastructure.*;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class AccessGovernanceApplicationServiceTest {
    private final AccessGovernanceApplicationService service = new AccessGovernanceApplicationService(
            new InMemoryAccessResourceRepository(),
            new InMemoryAccessRequestRepository(),
            new InMemoryAccessApprovalPolicyRepository(),
            new InMemoryAccessReviewCampaignRepository(),
            new InMemoryAccessRemovalTaskRepository()
    );

    @Test
    void governsRepoCloudAndProductionAccessWorkflows() {
        seedResourcesAndPolicies();
        AccessRequest request = service.requestAccess(new RequestAccessCommand("tenant-1", "worker-1", "prod-console", AccessLevel.ADMIN, "deploy support"));

        service.approve(request.id(), new DecisionCommand("approved by security"));
        service.provision(request.id());
        AccessRemovalTask task = service.requestRemoval(request.id(), new DecisionCommand("access no longer needed"));

        assertThat(task.resourceCode()).isEqualTo("prod-console");
        assertThat(service.removalTasks("tenant-1")).hasSize(1);
    }

    @Test
    void detectsOrphanedAccessAndReportsPrivilegedAccess() {
        seedResourcesAndPolicies();
        AccessRequest request = service.requestAccess(new RequestAccessCommand("tenant-1", "inactive-worker", "prod-console", AccessLevel.ADMIN, "emergency"));
        service.approve(request.id(), new DecisionCommand("approved"));
        service.provision(request.id());

        assertThat(service.orphanedAccess("tenant-1", List.of("active-worker")))
                .singleElement()
                .satisfies(finding -> assertThat(finding.requesterId()).isEqualTo("inactive-worker"));
        assertThat(service.privilegedAudit("tenant-1").requesterIds()).containsExactly("inactive-worker");
    }

    @Test
    void createsAccessReviewCampaign() {
        AccessReviewCampaign campaign = service.createCampaign(new CreateReviewCampaignCommand("tenant-1", "Q1 repo review", ResourceType.REPOSITORY, LocalDate.now().plusDays(30)));

        assertThat(service.campaigns("tenant-1")).containsExactly(campaign);
    }

    private void seedResourcesAndPolicies() {
        service.createResource(new CreateResourceCommand("tenant-1", "repo-payroll", "Payroll Repo", ResourceType.REPOSITORY, false, "owner-1"));
        service.createResource(new CreateResourceCommand("tenant-1", "aws-admin", "AWS Admin", ResourceType.CLOUD, true, "owner-2"));
        service.createResource(new CreateResourceCommand("tenant-1", "prod-console", "Production Console", ResourceType.PRODUCTION, true, "owner-3"));
        service.createPolicy(new CreatePolicyCommand("tenant-1", ResourceType.REPOSITORY, AccessLevel.WRITE, true, true, false));
        service.createPolicy(new CreatePolicyCommand("tenant-1", ResourceType.CLOUD, AccessLevel.ADMIN, true, true, true));
        service.createPolicy(new CreatePolicyCommand("tenant-1", ResourceType.PRODUCTION, AccessLevel.READ, true, true, true));
    }
}

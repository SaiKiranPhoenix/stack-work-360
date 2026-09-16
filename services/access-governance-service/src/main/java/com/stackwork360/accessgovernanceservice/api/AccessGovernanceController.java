package com.stackwork360.accessgovernanceservice.api;

import com.stackwork360.accessgovernanceservice.application.AccessGovernanceApplicationService;
import com.stackwork360.accessgovernanceservice.domain.*;
import com.stackwork360.common.CorrelationIds;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/access-governance/v1")
public class AccessGovernanceController {
    private final AccessGovernanceApplicationService service;

    public AccessGovernanceController(AccessGovernanceApplicationService service) {
        this.service = service;
    }

    @PostMapping("/resources")
    public ResponseEntity<AccessResource> createResource(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId, @Valid @RequestBody CreateResourceRequest request) {
        AccessResource response = service.createResource(request.toCommand(tenantId));
        return ResponseEntity.created(URI.create("/api/access-governance/v1/resources/" + response.resourceCode())).body(response);
    }

    @GetMapping("/resources")
    public List<AccessResource> resources(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId) {
        return service.resources(tenantId);
    }

    @PostMapping("/policies")
    public ResponseEntity<AccessApprovalPolicy> createPolicy(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId, @Valid @RequestBody CreatePolicyRequest request) {
        AccessApprovalPolicy response = service.createPolicy(request.toCommand(tenantId));
        return ResponseEntity.created(URI.create("/api/access-governance/v1/policies/" + response.id())).body(response);
    }

    @GetMapping("/policies")
    public List<AccessApprovalPolicy> policies(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId) {
        return service.policies(tenantId);
    }

    @PostMapping("/requests")
    public ResponseEntity<AccessRequest> requestAccess(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId, @Valid @RequestBody RequestAccessRequest request) {
        AccessRequest response = service.requestAccess(request.toCommand(tenantId));
        return ResponseEntity.created(URI.create("/api/access-governance/v1/requests/" + response.id())).body(response);
    }

    @GetMapping("/requests")
    public List<AccessRequest> requests(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId) {
        return service.requests(tenantId);
    }

    @PatchMapping("/requests/{requestId}/approve")
    public AccessRequest approve(@PathVariable UUID requestId, @Valid @RequestBody DecisionRequest request) {
        return service.approve(requestId, request.toCommand());
    }

    @PatchMapping("/requests/{requestId}/reject")
    public AccessRequest reject(@PathVariable UUID requestId, @Valid @RequestBody DecisionRequest request) {
        return service.reject(requestId, request.toCommand());
    }

    @PatchMapping("/requests/{requestId}/provision")
    public AccessRequest provision(@PathVariable UUID requestId) {
        return service.provision(requestId);
    }

    @PostMapping("/requests/{requestId}/removal-tasks")
    public AccessRemovalTask requestRemoval(@PathVariable UUID requestId, @Valid @RequestBody DecisionRequest request) {
        return service.requestRemoval(requestId, request.toCommand());
    }

    @PostMapping("/review-campaigns")
    public AccessReviewCampaign createCampaign(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId, @Valid @RequestBody CreateReviewCampaignRequest request) {
        return service.createCampaign(request.toCommand(tenantId));
    }

    @GetMapping("/review-campaigns")
    public List<AccessReviewCampaign> campaigns(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId) {
        return service.campaigns(tenantId);
    }

    @GetMapping("/removal-tasks")
    public List<AccessRemovalTask> removalTasks(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId) {
        return service.removalTasks(tenantId);
    }

    @PostMapping("/orphaned-access")
    public List<OrphanedAccessFinding> orphanedAccess(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId, @Valid @RequestBody OrphanedAccessRequest request) {
        return service.orphanedAccess(tenantId, request.activeWorkerIds());
    }

    @GetMapping("/privileged-audit")
    public PrivilegedAccessAuditReport privilegedAudit(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId) {
        return service.privilegedAudit(tenantId);
    }
}

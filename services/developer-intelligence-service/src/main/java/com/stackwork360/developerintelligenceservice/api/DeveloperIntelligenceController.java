package com.stackwork360.developerintelligenceservice.api;

import com.stackwork360.common.CorrelationIds;
import com.stackwork360.developerintelligenceservice.application.DeveloperIntelligenceApplicationService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/developer-intelligence/v1")
public class DeveloperIntelligenceController {
    private final DeveloperIntelligenceApplicationService developerIntelligenceApplicationService;

    public DeveloperIntelligenceController(DeveloperIntelligenceApplicationService developerIntelligenceApplicationService) {
        this.developerIntelligenceApplicationService = developerIntelligenceApplicationService;
    }

    @PostMapping("/repositories")
    public ResponseEntity<RepositoryResponse> registerRepository(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @Valid @RequestBody RegisterRepositoryRequest request
    ) {
        RepositoryResponse response = RepositoryResponse.from(developerIntelligenceApplicationService.register(request.toCommand(tenantId)));
        return ResponseEntity.created(URI.create("/api/developer-intelligence/v1/repositories/" + response.id()))
                .body(response);
    }

    @PutMapping("/repositories/{repositoryId}")
    public RepositoryResponse updateRepository(
            @PathVariable UUID repositoryId,
            @Valid @RequestBody UpdateRepositoryRequest request
    ) {
        return RepositoryResponse.from(developerIntelligenceApplicationService.update(repositoryId, request.toCommand()));
    }

    @GetMapping("/repositories")
    public List<RepositoryResponse> repositories(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId) {
        return developerIntelligenceApplicationService.repositories(tenantId).stream()
                .map(RepositoryResponse::from)
                .toList();
    }

    @GetMapping("/repositories/{repositoryId}")
    public RepositoryResponse repository(@PathVariable UUID repositoryId) {
        return RepositoryResponse.from(developerIntelligenceApplicationService.getRepository(repositoryId));
    }

    @PostMapping("/webhooks/git")
    public RepositoryResponse ingestGitWebhook(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @Valid @RequestBody GitWebhookRequest request
    ) {
        return RepositoryResponse.from(developerIntelligenceApplicationService.ingestGitWebhook(request.toCommand(tenantId)));
    }

    @GetMapping("/ownership-map")
    public List<OwnershipMapEntryResponse> ownershipMap(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId) {
        return developerIntelligenceApplicationService.ownershipMap(tenantId).stream()
                .map(OwnershipMapEntryResponse::from)
                .toList();
    }

    @GetMapping("/bus-factor")
    public BusFactorReportResponse busFactor(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId) {
        return BusFactorReportResponse.from(developerIntelligenceApplicationService.busFactor(tenantId));
    }

    @PostMapping("/access-requests")
    public ResponseEntity<DeveloperAccessRequestResponse> requestAccess(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @Valid @RequestBody RequestDeveloperAccessRequest request
    ) {
        DeveloperAccessRequestResponse response = DeveloperAccessRequestResponse.from(
                developerIntelligenceApplicationService.requestAccess(request.toCommand(tenantId))
        );
        return ResponseEntity.created(URI.create("/api/developer-intelligence/v1/access-requests/" + response.id()))
                .body(response);
    }

    @GetMapping("/access-requests")
    public List<DeveloperAccessRequestResponse> accessRequests(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId) {
        return developerIntelligenceApplicationService.accessRequests(tenantId).stream()
                .map(DeveloperAccessRequestResponse::from)
                .toList();
    }

    @PatchMapping("/access-requests/{requestId}/approve")
    public DeveloperAccessRequestResponse approveAccess(
            @PathVariable UUID requestId,
            @Valid @RequestBody AccessDecisionRequest request
    ) {
        return DeveloperAccessRequestResponse.from(developerIntelligenceApplicationService.approveAccess(requestId, request.actorId()));
    }

    @PatchMapping("/access-requests/{requestId}/reject")
    public DeveloperAccessRequestResponse rejectAccess(
            @PathVariable UUID requestId,
            @Valid @RequestBody AccessDecisionRequest request
    ) {
        return DeveloperAccessRequestResponse.from(developerIntelligenceApplicationService.rejectAccess(requestId, request.actorId()));
    }

    @PatchMapping("/access-requests/{requestId}/revoke")
    public DeveloperAccessRequestResponse revokeAccess(
            @PathVariable UUID requestId,
            @Valid @RequestBody AccessDecisionRequest request
    ) {
        return DeveloperAccessRequestResponse.from(developerIntelligenceApplicationService.revokeAccess(requestId, request.actorId()));
    }
}

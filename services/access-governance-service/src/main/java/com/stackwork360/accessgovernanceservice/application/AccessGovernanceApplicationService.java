package com.stackwork360.accessgovernanceservice.application;

import com.stackwork360.accessgovernanceservice.domain.*;
import com.stackwork360.web.ResourceNotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class AccessGovernanceApplicationService {
    private final AccessResourceRepository resourceRepository;
    private final AccessRequestRepository requestRepository;
    private final AccessApprovalPolicyRepository policyRepository;
    private final AccessReviewCampaignRepository campaignRepository;
    private final AccessRemovalTaskRepository removalTaskRepository;

    public AccessGovernanceApplicationService(
            AccessResourceRepository resourceRepository,
            AccessRequestRepository requestRepository,
            AccessApprovalPolicyRepository policyRepository,
            AccessReviewCampaignRepository campaignRepository,
            AccessRemovalTaskRepository removalTaskRepository
    ) {
        this.resourceRepository = resourceRepository;
        this.requestRepository = requestRepository;
        this.policyRepository = policyRepository;
        this.campaignRepository = campaignRepository;
        this.removalTaskRepository = removalTaskRepository;
    }

    public AccessResource createResource(CreateResourceCommand command) {
        return resourceRepository.save(new AccessResource(null, command.tenantId(), command.resourceCode(), command.name(), command.type(), command.privileged(), command.ownerId()));
    }

    public AccessApprovalPolicy createPolicy(CreatePolicyCommand command) {
        return policyRepository.save(new AccessApprovalPolicy(null, command.tenantId(), command.resourceType(), command.minimumLevel(), command.managerApprovalRequired(), command.ownerApprovalRequired(), command.securityApprovalRequired()));
    }

    public AccessRequest requestAccess(RequestAccessCommand command) {
        AccessResource resource = resource(command.tenantId(), command.resourceCode());
        boolean governed = policyRepository.findByTenantId(command.tenantId()).stream().anyMatch(policy -> policy.appliesTo(resource, command.level()));
        AccessRequest request = new AccessRequest(null, command.tenantId(), command.requesterId(), command.resourceCode(), command.level(), command.justification(), AccessRequestStatus.REQUESTED, governed ? null : "auto-approved by no matching policy", null, null);
        if (!governed) {
            request.approve("auto-approved by no matching policy");
        }
        return requestRepository.save(request);
    }

    public AccessRequest approve(UUID requestId, DecisionCommand command) {
        AccessRequest request = request(requestId);
        request.approve(command.reason());
        return requestRepository.save(request);
    }

    public AccessRequest reject(UUID requestId, DecisionCommand command) {
        AccessRequest request = request(requestId);
        request.reject(command.reason());
        return requestRepository.save(request);
    }

    public AccessRequest provision(UUID requestId) {
        AccessRequest request = request(requestId);
        request.provision();
        return requestRepository.save(request);
    }

    public AccessRemovalTask requestRemoval(UUID requestId, DecisionCommand command) {
        AccessRequest request = request(requestId);
        request.requestRemoval(command.reason());
        requestRepository.save(request);
        return removalTaskRepository.save(new AccessRemovalTask(null, request.tenantId(), request.id(), request.requesterId(), request.resourceCode(), command.reason(), false, null));
    }

    public AccessReviewCampaign createCampaign(CreateReviewCampaignCommand command) {
        return campaignRepository.save(new AccessReviewCampaign(null, command.tenantId(), command.name(), command.resourceType(), command.dueOn()));
    }

    public List<OrphanedAccessFinding> orphanedAccess(String tenantId, List<String> activeWorkerIds) {
        return requestRepository.findByTenantId(tenantId).stream()
                .filter(request -> request.status() == AccessRequestStatus.PROVISIONED)
                .filter(request -> !activeWorkerIds.contains(request.requesterId()))
                .map(request -> new OrphanedAccessFinding(request.id(), request.requesterId(), request.resourceCode(), "worker is not active"))
                .toList();
    }

    public PrivilegedAccessAuditReport privilegedAudit(String tenantId) {
        List<String> requesters = requestRepository.findByTenantId(tenantId).stream()
                .filter(request -> request.status() == AccessRequestStatus.PROVISIONED)
                .filter(request -> resourceRepository.findByCode(tenantId, request.resourceCode()).map(AccessResource::privileged).orElse(false) || request.level().ordinal() >= AccessLevel.ADMIN.ordinal())
                .map(AccessRequest::requesterId)
                .distinct()
                .sorted()
                .toList();
        return new PrivilegedAccessAuditReport(tenantId, requesters.size(), requesters);
    }

    public List<AccessResource> resources(String tenantId) {
        return resourceRepository.findByTenantId(tenantId);
    }

    public List<AccessRequest> requests(String tenantId) {
        return requestRepository.findByTenantId(tenantId);
    }

    public List<AccessApprovalPolicy> policies(String tenantId) {
        return policyRepository.findByTenantId(tenantId);
    }

    public List<AccessReviewCampaign> campaigns(String tenantId) {
        return campaignRepository.findByTenantId(tenantId);
    }

    public List<AccessRemovalTask> removalTasks(String tenantId) {
        return removalTaskRepository.findByTenantId(tenantId);
    }

    private AccessResource resource(String tenantId, String resourceCode) {
        return resourceRepository.findByCode(tenantId, resourceCode).orElseThrow(() -> new ResourceNotFoundException("access resource not found"));
    }

    private AccessRequest request(UUID id) {
        return requestRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("access request not found"));
    }
}

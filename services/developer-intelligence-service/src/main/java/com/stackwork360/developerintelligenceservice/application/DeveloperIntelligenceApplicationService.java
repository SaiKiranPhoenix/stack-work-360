package com.stackwork360.developerintelligenceservice.application;

import com.stackwork360.developerintelligenceservice.domain.CodeArea;
import com.stackwork360.developerintelligenceservice.domain.CodeRepository;
import com.stackwork360.developerintelligenceservice.domain.CodeRepositoryRepository;
import com.stackwork360.developerintelligenceservice.domain.DeveloperAccessRequest;
import com.stackwork360.developerintelligenceservice.domain.DeveloperAccessRequestRepository;
import com.stackwork360.developerintelligenceservice.domain.RepositoryLifecycle;
import com.stackwork360.developerintelligenceservice.domain.ServiceOwnership;
import com.stackwork360.web.ResourceNotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class DeveloperIntelligenceApplicationService {
    private final CodeRepositoryRepository repositoryRepository;
    private final DeveloperAccessRequestRepository accessRequestRepository;

    public DeveloperIntelligenceApplicationService(
            CodeRepositoryRepository repositoryRepository,
            DeveloperAccessRequestRepository accessRequestRepository
    ) {
        this.repositoryRepository = repositoryRepository;
        this.accessRequestRepository = accessRequestRepository;
    }

    public CodeRepository register(RegisterRepositoryCommand command) {
        CodeRepository repository = CodeRepository.register(
                command.tenantId(),
                command.provider(),
                command.externalId(),
                command.name(),
                command.defaultBranch(),
                ownership(command.serviceName(), command.owningTeam(), command.primaryMaintainers(), command.codeAreas())
        );
        return repositoryRepository.save(repository);
    }

    public CodeRepository update(UUID repositoryId, UpdateRepositoryCommand command) {
        CodeRepository repository = getRepository(repositoryId);
        repository.updateDetails(
                command.name(),
                command.defaultBranch(),
                command.lifecycle(),
                ownership(command.serviceName(), command.owningTeam(), command.primaryMaintainers(), command.codeAreas())
        );
        return repositoryRepository.save(repository);
    }

    public CodeRepository ingestGitWebhook(GitWebhookCommand command) {
        CodeRepository repository = repositoryRepository
                .findByTenantIdAndProviderAndExternalId(command.tenantId(), command.provider(), command.externalId())
                .orElseGet(() -> CodeRepository.register(
                        command.tenantId(),
                        command.provider(),
                        command.externalId(),
                        command.repositoryName(),
                        command.defaultBranch(),
                        ServiceOwnership.create(command.repositoryName(), "unassigned", List.of(command.developerId()), List.of())
                ));
        repository.recordContribution(command.developerId(), command.commitCount(), command.lastContributionAt());
        return repositoryRepository.save(repository);
    }

    public DeveloperAccessRequest requestAccess(RequestDeveloperAccessCommand command) {
        getRepository(command.repositoryId());
        return accessRequestRepository.save(DeveloperAccessRequest.request(
                command.tenantId(),
                command.repositoryId(),
                command.developerId(),
                command.requestedLevel(),
                command.reason()
        ));
    }

    public DeveloperAccessRequest approveAccess(UUID requestId, String actorId) {
        DeveloperAccessRequest request = getAccessRequest(requestId);
        request.approve(actorId);
        return accessRequestRepository.save(request);
    }

    public DeveloperAccessRequest rejectAccess(UUID requestId, String actorId) {
        DeveloperAccessRequest request = getAccessRequest(requestId);
        request.reject(actorId);
        return accessRequestRepository.save(request);
    }

    public DeveloperAccessRequest revokeAccess(UUID requestId, String actorId) {
        DeveloperAccessRequest request = getAccessRequest(requestId);
        request.revoke(actorId);
        return accessRequestRepository.save(request);
    }

    public CodeRepository getRepository(UUID repositoryId) {
        return repositoryRepository.findById(repositoryId)
                .orElseThrow(() -> new ResourceNotFoundException("repository not found"));
    }

    public DeveloperAccessRequest getAccessRequest(UUID requestId) {
        return accessRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("access request not found"));
    }

    public List<CodeRepository> repositories(String tenantId) {
        return repositoryRepository.findByTenantId(tenantId);
    }

    public List<DeveloperAccessRequest> accessRequests(String tenantId) {
        return accessRequestRepository.findByTenantId(tenantId);
    }

    public List<OwnershipMapEntry> ownershipMap(String tenantId) {
        return repositories(tenantId).stream()
                .map(this::ownershipMapEntry)
                .toList();
    }

    public BusFactorReport busFactor(String tenantId) {
        List<OwnershipMapEntry> entries = ownershipMap(tenantId);
        List<OwnershipMapEntry> riskyRepositories = entries.stream()
                .filter(OwnershipMapEntry::busFactorRisk)
                .toList();
        return new BusFactorReport(tenantId, entries.size(), riskyRepositories.size(), riskyRepositories);
    }

    private OwnershipMapEntry ownershipMapEntry(CodeRepository repository) {
        return new OwnershipMapEntry(
                repository.id(),
                repository.name(),
                repository.ownership().serviceName(),
                repository.ownership().owningTeam(),
                repository.ownership().primaryMaintainers(),
                repository.ownership().codeAreas(),
                repository.busFactor(),
                repository.busFactorRisk()
        );
    }

    private ServiceOwnership ownership(
            String serviceName,
            String owningTeam,
            List<String> primaryMaintainers,
            List<CodeAreaCommand> codeAreas
    ) {
        return ServiceOwnership.create(
                serviceName,
                owningTeam,
                primaryMaintainers == null ? List.of() : primaryMaintainers,
                codeAreas == null
                        ? List.of()
                        : codeAreas.stream()
                                .map(area -> new CodeArea(area.pathPattern(), area.ownerGroup()))
                                .toList()
        );
    }
}

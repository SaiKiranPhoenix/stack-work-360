package com.stackwork360.learningservice.application;

import com.stackwork360.learningservice.domain.*;
import com.stackwork360.web.ResourceNotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class LearningApplicationService {
    private final LearningResourceRepository resourceRepository;
    private final LearningPathRepository pathRepository;
    private final CertificationRepository certificationRepository;
    private final CourseCompletionRepository completionRepository;
    private final ManagerLearningAssignmentRepository assignmentRepository;
    private final RecommendationInputRepository recommendationInputRepository;

    public LearningApplicationService(
            LearningResourceRepository resourceRepository,
            LearningPathRepository pathRepository,
            CertificationRepository certificationRepository,
            CourseCompletionRepository completionRepository,
            ManagerLearningAssignmentRepository assignmentRepository,
            RecommendationInputRepository recommendationInputRepository
    ) {
        this.resourceRepository = resourceRepository;
        this.pathRepository = pathRepository;
        this.certificationRepository = certificationRepository;
        this.completionRepository = completionRepository;
        this.assignmentRepository = assignmentRepository;
        this.recommendationInputRepository = recommendationInputRepository;
    }

    public LearningResource createResource(CreateLearningResourceCommand command) {
        return resourceRepository.save(new LearningResource(null, command.tenantId(), command.title(), command.type(), command.provider(), command.durationMinutes(), command.skills(), command.active()));
    }

    public LearningPath createPath(CreateLearningPathCommand command) {
        command.resourceIds().forEach(this::resource);
        return pathRepository.save(new LearningPath(null, command.tenantId(), command.title(), command.roleTarget(), command.resourceIds(), command.active()));
    }

    public Certification createCertification(CreateCertificationCommand command) {
        command.requiredResourceIds().forEach(this::resource);
        return certificationRepository.save(new Certification(null, command.tenantId(), command.name(), command.issuer(), command.requiredResourceIds(), command.validityMonths(), command.availableFrom()));
    }

    public ManagerLearningAssignment assign(AssignLearningCommand command) {
        resource(command.resourceId());
        return assignmentRepository.save(new ManagerLearningAssignment(null, command.tenantId(), command.workerId(), command.managerId(), command.resourceId(), command.dueOn(), AssignmentStatus.ASSIGNED, null, null));
    }

    public CourseCompletion recordCompletion(RecordCompletionCommand command) {
        resource(command.resourceId());
        CourseCompletion completion = completionRepository.save(new CourseCompletion(null, command.tenantId(), command.workerId(), command.resourceId(), command.score(), null));
        assignmentRepository.findByWorker(command.tenantId(), command.workerId()).stream()
                .filter(assignment -> assignment.resourceId().equals(command.resourceId()))
                .filter(assignment -> assignment.status() == AssignmentStatus.ASSIGNED)
                .forEach(assignment -> {
                    assignment.complete();
                    assignmentRepository.save(assignment);
                });
        return completion;
    }

    public RecommendationInput captureRecommendationInput(CaptureRecommendationInputCommand command) {
        return recommendationInputRepository.save(new RecommendationInput(null, command.tenantId(), command.workerId(), command.roleTarget(), command.skillGaps(), null));
    }

    public LearningRecommendation recommendations(String tenantId, String workerId) {
        RecommendationInput latest = recommendationInputRepository.findByWorker(tenantId, workerId).stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("recommendation input not found"));
        List<UUID> matches = resourceRepository.findByTenantId(tenantId).stream()
                .filter(LearningResource::active)
                .filter(resource -> resource.teachesAny(latest.skillGaps()))
                .filter(resource -> !completionRepository.existsForWorkerAndResource(tenantId, workerId, resource.id()))
                .map(LearningResource::id)
                .toList();
        return new LearningRecommendation(workerId, latest.roleTarget(), matches);
    }

    public List<LearningResource> resources(String tenantId) {
        return resourceRepository.findByTenantId(tenantId);
    }

    public List<LearningPath> paths(String tenantId) {
        return pathRepository.findByTenantId(tenantId);
    }

    public List<Certification> certifications(String tenantId) {
        return certificationRepository.findByTenantId(tenantId);
    }

    public List<CourseCompletion> completions(String tenantId, String workerId) {
        return completionRepository.findByWorker(tenantId, workerId);
    }

    public List<ManagerLearningAssignment> assignments(String tenantId, String workerId) {
        return assignmentRepository.findByWorker(tenantId, workerId);
    }

    private LearningResource resource(UUID id) {
        return resourceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("learning resource not found"));
    }
}

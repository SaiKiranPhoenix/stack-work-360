package com.stackwork360.learningservice.api;

import com.stackwork360.common.CorrelationIds;
import com.stackwork360.learningservice.application.LearningApplicationService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/learning/v1")
public class LearningController {
    private final LearningApplicationService learningApplicationService;

    public LearningController(LearningApplicationService learningApplicationService) {
        this.learningApplicationService = learningApplicationService;
    }

    @PostMapping("/resources")
    public ResponseEntity<LearningResourceResponse> createResource(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId, @Valid @RequestBody CreateLearningResourceRequest request) {
        LearningResourceResponse response = LearningResourceResponse.from(learningApplicationService.createResource(request.toCommand(tenantId)));
        return ResponseEntity.created(URI.create("/api/learning/v1/resources/" + response.id())).body(response);
    }

    @GetMapping("/resources")
    public List<LearningResourceResponse> resources(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId) {
        return learningApplicationService.resources(tenantId).stream().map(LearningResourceResponse::from).toList();
    }

    @PostMapping("/paths")
    public ResponseEntity<LearningPathResponse> createPath(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId, @Valid @RequestBody CreateLearningPathRequest request) {
        LearningPathResponse response = LearningPathResponse.from(learningApplicationService.createPath(request.toCommand(tenantId)));
        return ResponseEntity.created(URI.create("/api/learning/v1/paths/" + response.id())).body(response);
    }

    @GetMapping("/paths")
    public List<LearningPathResponse> paths(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId) {
        return learningApplicationService.paths(tenantId).stream().map(LearningPathResponse::from).toList();
    }

    @PostMapping("/certifications")
    public ResponseEntity<CertificationResponse> createCertification(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId, @Valid @RequestBody CreateCertificationRequest request) {
        CertificationResponse response = CertificationResponse.from(learningApplicationService.createCertification(request.toCommand(tenantId)));
        return ResponseEntity.created(URI.create("/api/learning/v1/certifications/" + response.id())).body(response);
    }

    @GetMapping("/certifications")
    public List<CertificationResponse> certifications(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId) {
        return learningApplicationService.certifications(tenantId).stream().map(CertificationResponse::from).toList();
    }

    @PostMapping("/completions")
    public ResponseEntity<CourseCompletionResponse> recordCompletion(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId, @Valid @RequestBody RecordCompletionRequest request) {
        CourseCompletionResponse response = CourseCompletionResponse.from(learningApplicationService.recordCompletion(request.toCommand(tenantId)));
        return ResponseEntity.created(URI.create("/api/learning/v1/completions/" + response.id())).body(response);
    }

    @GetMapping("/completions")
    public List<CourseCompletionResponse> completions(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId, @RequestParam String workerId) {
        return learningApplicationService.completions(tenantId, workerId).stream().map(CourseCompletionResponse::from).toList();
    }

    @PostMapping("/assignments")
    public ResponseEntity<ManagerLearningAssignmentResponse> assign(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId, @Valid @RequestBody AssignLearningRequest request) {
        ManagerLearningAssignmentResponse response = ManagerLearningAssignmentResponse.from(learningApplicationService.assign(request.toCommand(tenantId)));
        return ResponseEntity.created(URI.create("/api/learning/v1/assignments/" + response.id())).body(response);
    }

    @GetMapping("/assignments")
    public List<ManagerLearningAssignmentResponse> assignments(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId, @RequestParam String workerId) {
        return learningApplicationService.assignments(tenantId, workerId).stream().map(ManagerLearningAssignmentResponse::from).toList();
    }

    @PostMapping("/recommendation-inputs")
    public RecommendationInputResponse captureRecommendationInput(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId, @Valid @RequestBody CaptureRecommendationInputRequest request) {
        return RecommendationInputResponse.from(learningApplicationService.captureRecommendationInput(request.toCommand(tenantId)));
    }

    @GetMapping("/recommendations")
    public LearningRecommendationResponse recommendations(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId, @RequestParam String workerId) {
        return LearningRecommendationResponse.from(learningApplicationService.recommendations(tenantId, workerId));
    }
}

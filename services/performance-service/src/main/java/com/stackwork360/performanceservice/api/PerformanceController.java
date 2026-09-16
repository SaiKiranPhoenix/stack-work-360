package com.stackwork360.performanceservice.api;

import com.stackwork360.common.CorrelationIds;
import com.stackwork360.performanceservice.application.PerformanceApplicationService;
import com.stackwork360.performanceservice.domain.ReviewerRole;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/performance/v1")
public class PerformanceController {
    private final PerformanceApplicationService performanceApplicationService;

    public PerformanceController(PerformanceApplicationService performanceApplicationService) {
        this.performanceApplicationService = performanceApplicationService;
    }

    @PostMapping("/cycles")
    public ResponseEntity<ReviewCycleResponse> createCycle(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @Valid @RequestBody CreateReviewCycleRequest request
    ) {
        ReviewCycleResponse response = ReviewCycleResponse.from(performanceApplicationService.createCycle(request.toCommand(tenantId)));
        return ResponseEntity.created(URI.create("/api/performance/v1/cycles/" + response.id())).body(response);
    }

    @GetMapping("/cycles")
    public List<ReviewCycleResponse> cycles(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId) {
        return performanceApplicationService.cycles(tenantId).stream().map(ReviewCycleResponse::from).toList();
    }

    @PatchMapping("/cycles/{cycleId}/activate")
    public ReviewCycleResponse activateCycle(@PathVariable UUID cycleId) {
        return ReviewCycleResponse.from(performanceApplicationService.activateCycle(cycleId));
    }

    @PatchMapping("/cycles/{cycleId}/calibration")
    public ReviewCycleResponse startCalibration(@PathVariable UUID cycleId) {
        return ReviewCycleResponse.from(performanceApplicationService.startCalibration(cycleId));
    }

    @PatchMapping("/cycles/{cycleId}/close")
    public ReviewCycleResponse closeCycle(@PathVariable UUID cycleId) {
        return ReviewCycleResponse.from(performanceApplicationService.closeCycle(cycleId));
    }

    @PostMapping("/templates")
    public ResponseEntity<ReviewTemplateResponse> createTemplate(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @Valid @RequestBody CreateReviewTemplateRequest request
    ) {
        ReviewTemplateResponse response = ReviewTemplateResponse.from(performanceApplicationService.createTemplate(request.toCommand(tenantId)));
        return ResponseEntity.created(URI.create("/api/performance/v1/templates/" + response.id())).body(response);
    }

    @GetMapping("/templates")
    public List<ReviewTemplateResponse> templates(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId) {
        return performanceApplicationService.templates(tenantId).stream().map(ReviewTemplateResponse::from).toList();
    }

    @PostMapping("/reviews")
    public ResponseEntity<ReviewSubmissionResponse> submitReview(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @Valid @RequestBody SubmitReviewRequest request
    ) {
        ReviewSubmissionResponse response = ReviewSubmissionResponse.from(performanceApplicationService.submitReview(request.toCommand(tenantId)));
        return ResponseEntity.created(URI.create("/api/performance/v1/reviews/" + response.id())).body(response);
    }

    @GetMapping("/reviews")
    public List<ReviewSubmissionResponse> reviews(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @RequestParam(required = false) String workerId
    ) {
        return performanceApplicationService.reviews(tenantId, workerId).stream().map(ReviewSubmissionResponse::from).toList();
    }

    @GetMapping("/reviews/visible")
    public List<ReviewSubmissionResponse> visibleReviews(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @RequestParam String actorWorkerId,
            @RequestParam ReviewerRole role
    ) {
        return performanceApplicationService.visibleReviews(tenantId, actorWorkerId, role).stream().map(ReviewSubmissionResponse::from).toList();
    }

    @PatchMapping("/reviews/{reviewId}/calibrate")
    public ReviewSubmissionResponse calibrate(@PathVariable UUID reviewId, @Valid @RequestBody CalibrateReviewRequest request) {
        return ReviewSubmissionResponse.from(performanceApplicationService.calibrate(reviewId, request.toCommand()));
    }

    @PatchMapping("/reviews/{reviewId}/visible")
    public ReviewSubmissionResponse makeVisible(@PathVariable UUID reviewId) {
        return ReviewSubmissionResponse.from(performanceApplicationService.makeVisible(reviewId));
    }

    @GetMapping("/workers/{workerId}/promotion-readiness")
    public PromotionReadinessSummaryResponse promotionReadiness(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @PathVariable String workerId
    ) {
        return PromotionReadinessSummaryResponse.from(performanceApplicationService.promotionReadiness(tenantId, workerId));
    }
}

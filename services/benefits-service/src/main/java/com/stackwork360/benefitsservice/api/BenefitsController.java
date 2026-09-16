package com.stackwork360.benefitsservice.api;

import com.stackwork360.benefitsservice.application.BenefitsApplicationService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/benefits/v1")
public class BenefitsController {
    private final BenefitsApplicationService benefitsApplicationService;

    public BenefitsController(BenefitsApplicationService benefitsApplicationService) {
        this.benefitsApplicationService = benefitsApplicationService;
    }

    @PostMapping("/plans")
    public ResponseEntity<BenefitPlanResponse> createPlan(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @Valid @RequestBody CreateBenefitPlanRequest request
    ) {
        BenefitPlanResponse response = BenefitPlanResponse.from(benefitsApplicationService.createPlan(request.toCommand(tenantId)));
        return ResponseEntity.created(URI.create("/api/benefits/v1/plans/" + response.planCode())).body(response);
    }

    @GetMapping("/plans")
    public List<BenefitPlanResponse> plans(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId) {
        return benefitsApplicationService.plans(tenantId).stream().map(BenefitPlanResponse::from).toList();
    }

    @PostMapping("/eligibility-rules")
    public ResponseEntity<EligibilityRuleResponse> createEligibilityRule(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @Valid @RequestBody CreateEligibilityRuleRequest request
    ) {
        EligibilityRuleResponse response = EligibilityRuleResponse.from(benefitsApplicationService.createEligibilityRule(request.toCommand(tenantId)));
        return ResponseEntity.created(URI.create("/api/benefits/v1/eligibility-rules/" + response.id())).body(response);
    }

    @GetMapping("/eligibility-rules")
    public List<EligibilityRuleResponse> eligibilityRules(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId) {
        return benefitsApplicationService.eligibilityRules(tenantId).stream().map(EligibilityRuleResponse::from).toList();
    }

    @PostMapping("/eligibility-checks")
    public EligibilityDecisionResponse eligibility(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @Valid @RequestBody EligibilityCheckRequest request
    ) {
        return EligibilityDecisionResponse.from(benefitsApplicationService.eligibility(tenantId, request.profile().toDomain(), request.planCode(), request.asOf()));
    }

    @PostMapping("/open-enrollment-windows")
    public ResponseEntity<OpenEnrollmentWindowResponse> createOpenEnrollmentWindow(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @Valid @RequestBody CreateOpenEnrollmentWindowRequest request
    ) {
        OpenEnrollmentWindowResponse response = OpenEnrollmentWindowResponse.from(benefitsApplicationService.createOpenEnrollmentWindow(request.toCommand(tenantId)));
        return ResponseEntity.created(URI.create("/api/benefits/v1/open-enrollment-windows/" + response.id())).body(response);
    }

    @GetMapping("/open-enrollment-windows")
    public List<OpenEnrollmentWindowResponse> openEnrollmentWindows(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId) {
        return benefitsApplicationService.openEnrollmentWindows(tenantId).stream().map(OpenEnrollmentWindowResponse::from).toList();
    }

    @PostMapping("/enrollments")
    public ResponseEntity<EmployeeEnrollmentResponse> enroll(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @Valid @RequestBody EnrollEmployeeRequest request
    ) {
        EmployeeEnrollmentResponse response = EmployeeEnrollmentResponse.from(benefitsApplicationService.enroll(request.toCommand(tenantId)));
        return ResponseEntity.created(URI.create("/api/benefits/v1/enrollments/" + response.id())).body(response);
    }

    @GetMapping("/enrollments")
    public List<EmployeeEnrollmentResponse> enrollments(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @RequestParam(required = false) String workerId
    ) {
        return benefitsApplicationService.enrollments(tenantId, workerId).stream().map(EmployeeEnrollmentResponse::from).toList();
    }

    @PostMapping("/change-requests")
    public ResponseEntity<BenefitChangeRequestResponse> requestChange(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @Valid @RequestBody RequestBenefitChangeRequest request
    ) {
        BenefitChangeRequestResponse response = BenefitChangeRequestResponse.from(benefitsApplicationService.requestChange(request.toCommand(tenantId)));
        return ResponseEntity.created(URI.create("/api/benefits/v1/change-requests/" + response.id())).body(response);
    }

    @GetMapping("/change-requests")
    public List<BenefitChangeRequestResponse> changeRequests(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId) {
        return benefitsApplicationService.changeRequests(tenantId).stream().map(BenefitChangeRequestResponse::from).toList();
    }

    @PatchMapping("/change-requests/{requestId}/approve")
    public BenefitChangeRequestResponse approve(@PathVariable UUID requestId, @Valid @RequestBody DecisionRequest request) {
        return BenefitChangeRequestResponse.from(benefitsApplicationService.approve(requestId, request.actorId()));
    }

    @PatchMapping("/change-requests/{requestId}/reject")
    public BenefitChangeRequestResponse reject(@PathVariable UUID requestId, @Valid @RequestBody DecisionRequest request) {
        return BenefitChangeRequestResponse.from(benefitsApplicationService.reject(requestId, request.actorId()));
    }

    @PatchMapping("/change-requests/{requestId}/apply")
    public BenefitChangeRequestResponse apply(@PathVariable UUID requestId) {
        return BenefitChangeRequestResponse.from(benefitsApplicationService.apply(requestId));
    }

    @GetMapping("/usage-summary")
    public BenefitsUsageSummaryResponse usageSummary(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId) {
        return BenefitsUsageSummaryResponse.from(benefitsApplicationService.usageSummary(tenantId));
    }
}

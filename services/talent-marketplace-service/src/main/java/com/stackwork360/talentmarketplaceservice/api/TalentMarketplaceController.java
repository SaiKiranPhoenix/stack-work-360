package com.stackwork360.talentmarketplaceservice.api;

import com.stackwork360.common.CorrelationIds;
import com.stackwork360.talentmarketplaceservice.application.TalentMarketplaceApplicationService;
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
@RequestMapping("/api/talent-marketplace/v1")
public class TalentMarketplaceController {
    private final TalentMarketplaceApplicationService service;

    public TalentMarketplaceController(TalentMarketplaceApplicationService service) {
        this.service = service;
    }

    @PostMapping("/opportunities")
    public ResponseEntity<TalentOpportunityResponse> createOpportunity(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId, @Valid @RequestBody CreateOpportunityRequest request) {
        TalentOpportunityResponse response = TalentOpportunityResponse.from(service.createOpportunity(request.toCommand(tenantId)));
        return ResponseEntity.created(URI.create("/api/talent-marketplace/v1/opportunities/" + response.id())).body(response);
    }

    @PatchMapping("/opportunities/{opportunityId}/open")
    public TalentOpportunityResponse openOpportunity(@PathVariable UUID opportunityId) {
        return TalentOpportunityResponse.from(service.openOpportunity(opportunityId));
    }

    @GetMapping("/opportunities")
    public List<TalentOpportunityResponse> opportunities(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId) {
        return service.opportunities(tenantId).stream().map(TalentOpportunityResponse::from).toList();
    }

    @PostMapping("/opportunities/{opportunityId}/applications")
    public ResponseEntity<InternalApplicationResponse> apply(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId, @PathVariable UUID opportunityId, @Valid @RequestBody ApplyToOpportunityRequest request) {
        InternalApplicationResponse response = InternalApplicationResponse.from(service.apply(request.toCommand(tenantId, opportunityId)));
        return ResponseEntity.created(URI.create("/api/talent-marketplace/v1/applications/" + response.id())).body(response);
    }

    @GetMapping("/applications")
    public List<InternalApplicationResponse> applications(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId) {
        return service.applications(tenantId).stream().map(InternalApplicationResponse::from).toList();
    }

    @PatchMapping("/applications/{applicationId}/manager-approve")
    public InternalApplicationResponse managerApprove(@PathVariable UUID applicationId, @Valid @RequestBody DecisionRequest request) {
        return InternalApplicationResponse.from(service.managerApprove(applicationId, request.toCommand()));
    }

    @PatchMapping("/applications/{applicationId}/manager-reject")
    public InternalApplicationResponse managerReject(@PathVariable UUID applicationId, @Valid @RequestBody DecisionRequest request) {
        return InternalApplicationResponse.from(service.managerReject(applicationId, request.toCommand()));
    }

    @PatchMapping("/applications/{applicationId}/accept")
    public InternalApplicationResponse accept(@PathVariable UUID applicationId, @Valid @RequestBody DecisionRequest request) {
        return InternalApplicationResponse.from(service.accept(applicationId, request.toCommand()));
    }

    @PostMapping("/recommendations")
    public List<OpportunityRecommendationResponse> recommendations(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId, @Valid @RequestBody RecommendationRequest request) {
        return service.recommendations(request.toQuery(tenantId)).stream().map(OpportunityRecommendationResponse::from).toList();
    }

    @GetMapping("/mobility-history")
    public List<MobilityHistoryResponse> mobilityHistory(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId, @RequestParam String workerId) {
        return service.mobilityHistory(tenantId, workerId).stream().map(MobilityHistoryResponse::from).toList();
    }
}

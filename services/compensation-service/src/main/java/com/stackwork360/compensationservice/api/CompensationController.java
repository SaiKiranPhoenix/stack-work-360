package com.stackwork360.compensationservice.api;

import com.stackwork360.common.CorrelationIds;
import com.stackwork360.compensationservice.application.CompensationApplicationService;
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
@RequestMapping("/api/compensation/v1")
public class CompensationController {
    private final CompensationApplicationService compensationApplicationService;

    public CompensationController(CompensationApplicationService compensationApplicationService) {
        this.compensationApplicationService = compensationApplicationService;
    }

    @PostMapping("/salary-bands")
    public ResponseEntity<SalaryBandResponse> createSalaryBand(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @Valid @RequestBody CreateSalaryBandRequest request
    ) {
        SalaryBandResponse response = SalaryBandResponse.from(compensationApplicationService.createSalaryBand(request.toCommand(tenantId)));
        return ResponseEntity.created(URI.create("/api/compensation/v1/salary-bands/" + response.id())).body(response);
    }

    @GetMapping("/salary-bands")
    public List<SalaryBandResponse> salaryBands(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId) {
        return compensationApplicationService.salaryBands(tenantId).stream()
                .map(SalaryBandResponse::from)
                .toList();
    }

    @PostMapping("/history")
    public ResponseEntity<CompensationHistoryResponse> recordHistory(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @Valid @RequestBody RecordCompensationRequest request
    ) {
        CompensationHistoryResponse response = CompensationHistoryResponse.from(compensationApplicationService.recordCompensation(request.toCommand(tenantId)));
        return ResponseEntity.created(URI.create("/api/compensation/v1/history/" + response.id())).body(response);
    }

    @GetMapping("/history")
    public List<CompensationHistoryResponse> history(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @RequestParam(required = false) String workerId
    ) {
        return compensationApplicationService.history(tenantId, workerId).stream()
                .map(CompensationHistoryResponse::from)
                .toList();
    }

    @PostMapping("/change-requests")
    public ResponseEntity<CompensationChangeRequestResponse> requestChange(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @Valid @RequestBody RequestCompensationChangeRequest request
    ) {
        CompensationChangeRequestResponse response = CompensationChangeRequestResponse.from(compensationApplicationService.requestChange(request.toCommand(tenantId)));
        return ResponseEntity.created(URI.create("/api/compensation/v1/change-requests/" + response.id())).body(response);
    }

    @GetMapping("/change-requests")
    public List<CompensationChangeRequestResponse> changeRequests(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId) {
        return compensationApplicationService.requests(tenantId).stream()
                .map(CompensationChangeRequestResponse::from)
                .toList();
    }

    @PatchMapping("/change-requests/{requestId}/approve")
    public CompensationChangeRequestResponse approve(
            @PathVariable UUID requestId,
            @Valid @RequestBody DecisionRequest request
    ) {
        return CompensationChangeRequestResponse.from(compensationApplicationService.approve(requestId, request.actorId()));
    }

    @PatchMapping("/change-requests/{requestId}/reject")
    public CompensationChangeRequestResponse reject(
            @PathVariable UUID requestId,
            @Valid @RequestBody DecisionRequest request
    ) {
        return CompensationChangeRequestResponse.from(compensationApplicationService.reject(requestId, request.actorId()));
    }

    @PatchMapping("/change-requests/{requestId}/apply")
    public CompensationChangeRequestResponse apply(@PathVariable UUID requestId) {
        return CompensationChangeRequestResponse.from(compensationApplicationService.apply(requestId));
    }

    @PostMapping("/benchmarks")
    public ResponseEntity<CompensationBenchmarkResponse> importBenchmark(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @Valid @RequestBody ImportBenchmarkRequest request
    ) {
        CompensationBenchmarkResponse response = CompensationBenchmarkResponse.from(compensationApplicationService.importBenchmark(request.toCommand(tenantId)));
        return ResponseEntity.created(URI.create("/api/compensation/v1/benchmarks/" + response.id())).body(response);
    }

    @GetMapping("/pay-equity-alerts")
    public List<PayEquityAlertResponse> payEquityAlerts(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId) {
        return compensationApplicationService.payEquityAlerts(tenantId).stream()
                .map(PayEquityAlertResponse::from)
                .toList();
    }

    @GetMapping("/budget-impact")
    public BudgetImpactReportResponse budgetImpact(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId) {
        return BudgetImpactReportResponse.from(compensationApplicationService.budgetImpact(tenantId));
    }
}

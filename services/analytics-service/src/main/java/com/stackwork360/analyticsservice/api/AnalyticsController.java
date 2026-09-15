package com.stackwork360.analyticsservice.api;

import com.stackwork360.analyticsservice.application.AnalyticsApplicationService;
import com.stackwork360.analyticsservice.domain.ProjectionType;
import com.stackwork360.common.CorrelationIds;
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
@RequestMapping("/api/analytics/v1")
public class AnalyticsController {
    private final AnalyticsApplicationService analyticsApplicationService;

    public AnalyticsController(AnalyticsApplicationService analyticsApplicationService) {
        this.analyticsApplicationService = analyticsApplicationService;
    }

    @PostMapping("/projections")
    public ResponseEntity<ProjectionSnapshotResponse> upsert(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @Valid @RequestBody UpsertProjectionRequest request
    ) {
        ProjectionSnapshotResponse response = ProjectionSnapshotResponse.from(analyticsApplicationService.upsert(request.toCommand(tenantId)));
        return ResponseEntity.created(URI.create("/api/analytics/v1/projections/" + response.id()))
                .body(response);
    }

    @GetMapping("/employee-lifecycle")
    public ProjectionSnapshotResponse employeeLifecycle(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId) {
        return ProjectionSnapshotResponse.from(analyticsApplicationService.employeeLifecycle(tenantId));
    }

    @GetMapping("/org-health")
    public ProjectionSnapshotResponse orgHealth(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId) {
        return ProjectionSnapshotResponse.from(analyticsApplicationService.orgHealth(tenantId));
    }

    @GetMapping("/workforce-availability")
    public ProjectionSnapshotResponse workforceAvailability(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @RequestParam(defaultValue = "global") String scope
    ) {
        return ProjectionSnapshotResponse.from(analyticsApplicationService.workforceAvailability(tenantId, scope));
    }

    @GetMapping("/engineering-ownership")
    public ProjectionSnapshotResponse engineeringOwnership(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId) {
        return ProjectionSnapshotResponse.from(analyticsApplicationService.engineeringOwnership(tenantId));
    }

    @GetMapping("/payroll-summary")
    public ProjectionSnapshotResponse payrollSummary(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @RequestParam(defaultValue = "global") String scope
    ) {
        return ProjectionSnapshotResponse.from(analyticsApplicationService.payrollSummary(tenantId, scope));
    }

    @GetMapping("/risk-dashboard")
    public ProjectionSnapshotResponse riskDashboard(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId) {
        return ProjectionSnapshotResponse.from(analyticsApplicationService.riskDashboard(tenantId));
    }

    @GetMapping("/projections")
    public List<ProjectionSnapshotResponse> projections(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @RequestParam ProjectionType type
    ) {
        return analyticsApplicationService.list(tenantId, type).stream()
                .map(ProjectionSnapshotResponse::from)
                .toList();
    }
}

package com.stackwork360.analyticsservice.application;

import com.stackwork360.analyticsservice.domain.AnalyticsMetric;
import com.stackwork360.analyticsservice.domain.ProjectionSnapshot;
import com.stackwork360.analyticsservice.domain.ProjectionSnapshotRepository;
import com.stackwork360.analyticsservice.domain.ProjectionType;
import com.stackwork360.web.ResourceNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsApplicationService {
    private static final String GLOBAL_SCOPE = "global";

    private final ProjectionSnapshotRepository projectionSnapshotRepository;

    public AnalyticsApplicationService(ProjectionSnapshotRepository projectionSnapshotRepository) {
        this.projectionSnapshotRepository = projectionSnapshotRepository;
    }

    public ProjectionSnapshot upsert(UpsertProjectionCommand command) {
        ProjectionSnapshot snapshot = ProjectionSnapshot.create(
                command.tenantId(),
                command.type(),
                normalizeScope(command.scope()),
                metrics(command.metrics())
        );
        return projectionSnapshotRepository.save(snapshot);
    }

    public ProjectionSnapshot employeeLifecycle(String tenantId) {
        return latest(tenantId, ProjectionType.EMPLOYEE_LIFECYCLE, GLOBAL_SCOPE);
    }

    public ProjectionSnapshot orgHealth(String tenantId) {
        return latest(tenantId, ProjectionType.ORG_HEALTH, GLOBAL_SCOPE);
    }

    public ProjectionSnapshot workforceAvailability(String tenantId, String scope) {
        return latest(tenantId, ProjectionType.WORKFORCE_AVAILABILITY, normalizeScope(scope));
    }

    public ProjectionSnapshot engineeringOwnership(String tenantId) {
        return latest(tenantId, ProjectionType.ENGINEERING_OWNERSHIP, GLOBAL_SCOPE);
    }

    public ProjectionSnapshot payrollSummary(String tenantId, String scope) {
        return latest(tenantId, ProjectionType.PAYROLL_SUMMARY, normalizeScope(scope));
    }

    public ProjectionSnapshot riskDashboard(String tenantId) {
        return latest(tenantId, ProjectionType.RISK_DASHBOARD, GLOBAL_SCOPE);
    }

    public List<ProjectionSnapshot> list(String tenantId, ProjectionType type) {
        return projectionSnapshotRepository.findByTenantIdAndType(tenantId, type);
    }

    private ProjectionSnapshot latest(String tenantId, ProjectionType type, String scope) {
        return projectionSnapshotRepository.findLatest(tenantId, type, scope)
                .orElseThrow(() -> new ResourceNotFoundException("analytics projection not found"));
    }

    private static List<AnalyticsMetric> metrics(List<MetricCommand> commands) {
        return (commands == null ? List.<MetricCommand>of() : commands).stream()
                .map(command -> new AnalyticsMetric(command.key(), command.value()))
                .toList();
    }

    private static String normalizeScope(String scope) {
        return scope == null || scope.isBlank() ? GLOBAL_SCOPE : scope.trim();
    }
}

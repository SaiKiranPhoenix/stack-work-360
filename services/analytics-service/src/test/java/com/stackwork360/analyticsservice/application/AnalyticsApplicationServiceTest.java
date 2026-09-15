package com.stackwork360.analyticsservice.application;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.stackwork360.analyticsservice.domain.ProjectionType;
import com.stackwork360.analyticsservice.infrastructure.InMemoryProjectionSnapshotRepository;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

class AnalyticsApplicationServiceTest {
    private final AnalyticsApplicationService service = new AnalyticsApplicationService(new InMemoryProjectionSnapshotRepository());

    @Test
    void returnsLatestSnapshotForDashboard() {
        service.upsert(new UpsertProjectionCommand(
                "tenant-1",
                ProjectionType.RISK_DASHBOARD,
                "global",
                List.of(new MetricCommand("openCriticalSignals", BigDecimal.ONE))
        ));
        service.upsert(new UpsertProjectionCommand(
                "tenant-1",
                ProjectionType.RISK_DASHBOARD,
                "global",
                List.of(new MetricCommand("openCriticalSignals", BigDecimal.valueOf(3)))
        ));

        assertEquals("3.00", service.riskDashboard("tenant-1").metric("openCriticalSignals").value().toPlainString());
    }

    @Test
    void separatesScopedPayrollSummaries() {
        service.upsert(new UpsertProjectionCommand(
                "tenant-1",
                ProjectionType.PAYROLL_SUMMARY,
                "2026-01",
                List.of(new MetricCommand("workersPaid", BigDecimal.valueOf(10)))
        ));
        service.upsert(new UpsertProjectionCommand(
                "tenant-1",
                ProjectionType.PAYROLL_SUMMARY,
                "2026-02",
                List.of(new MetricCommand("workersPaid", BigDecimal.valueOf(12)))
        ));

        assertEquals("10.00", service.payrollSummary("tenant-1", "2026-01").metric("workersPaid").value().toPlainString());
        assertEquals("12.00", service.payrollSummary("tenant-1", "2026-02").metric("workersPaid").value().toPlainString());
    }
}

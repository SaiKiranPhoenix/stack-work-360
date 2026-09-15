package com.stackwork360.analyticsservice.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;

class ProjectionSnapshotTest {
    @Test
    void storesMetricsWithTwoDecimalScale() {
        ProjectionSnapshot snapshot = ProjectionSnapshot.create(
                "tenant-1",
                ProjectionType.PAYROLL_SUMMARY,
                "2026-01",
                List.of(AnalyticsMetric.of("grossPay", "12000.555"))
        );

        assertEquals("12000.56", snapshot.metric("grossPay").value().toPlainString());
    }

    @Test
    void requiresAtLeastOneMetric() {
        assertThrows(IllegalArgumentException.class, () -> ProjectionSnapshot.create(
                "tenant-1",
                ProjectionType.ORG_HEALTH,
                "global",
                List.of()
        ));
    }
}

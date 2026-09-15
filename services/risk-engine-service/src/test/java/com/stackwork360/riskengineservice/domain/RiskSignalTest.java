package com.stackwork360.riskengineservice.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class RiskSignalTest {
    @Test
    void acknowledgesOpenSignal() {
        RiskSignal signal = signal();

        signal.acknowledge();

        assertEquals(RiskSignalStatus.ACKNOWLEDGED, signal.status());
    }

    @Test
    void resolvesOpenSignal() {
        RiskSignal signal = signal();

        signal.resolve("risk-owner-1");

        assertEquals(RiskSignalStatus.RESOLVED, signal.status());
        assertEquals("risk-owner-1", signal.resolvedBy());
    }

    @Test
    void rejectsClosingClosedSignalTwice() {
        RiskSignal signal = signal();
        signal.dismiss("risk-owner-1");

        assertThrows(IllegalStateException.class, () -> signal.resolve("risk-owner-2"));
    }

    private static RiskSignal signal() {
        return RiskSignal.open(
                "tenant-1",
                RiskSource.HELPDESK,
                "ticket-1",
                UUID.randomUUID(),
                "Overdue critical ticket",
                RiskSeverity.CRITICAL,
                "Ticket is overdue",
                Map.of("overdueHours", "8")
        );
    }
}

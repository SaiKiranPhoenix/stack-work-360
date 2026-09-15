package com.stackwork360.attendanceservice.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import org.junit.jupiter.api.Test;

class AttendanceEntryTest {
    @Test
    void calculatesOvertimeAfterCheckOut() {
        AttendanceEntry entry = AttendanceEntry.checkIn(
                "tenant-1",
                "worker-1",
                AttendanceEntryType.OFFICE,
                Instant.parse("2026-01-01T09:00:00Z"),
                "HQ"
        );

        entry.checkOut(Instant.parse("2026-01-01T19:30:00Z"));

        assertEquals(150, entry.overtime().toMinutes());
    }

    @Test
    void flagsMissingCheckOutAndLongShift() {
        AttendanceEntry entry = AttendanceEntry.checkIn(
                "tenant-1",
                "worker-1",
                AttendanceEntryType.OFFICE,
                Instant.parse("2026-01-01T09:00:00Z"),
                "HQ"
        );

        AttendanceSummary summary = entry.summary(Instant.parse("2026-01-01T22:00:00Z"));

        assertTrue(summary.anomalies().stream().anyMatch(anomaly -> anomaly.type() == AttendanceAnomalyType.MISSING_CHECK_OUT));
        assertTrue(summary.anomalies().stream().anyMatch(anomaly -> anomaly.type() == AttendanceAnomalyType.LONG_SHIFT));
    }

    @Test
    void correctionRequiresCheckOutAfterCheckIn() {
        AttendanceEntry entry = AttendanceEntry.remoteWork(
                "tenant-1",
                "worker-1",
                Instant.parse("2026-01-01T09:00:00Z"),
                Instant.parse("2026-01-01T17:00:00Z"),
                "Home"
        );

        assertThrows(IllegalArgumentException.class, () -> entry.correct(
                Instant.parse("2026-01-01T18:00:00Z"),
                Instant.parse("2026-01-01T09:00:00Z"),
                AttendanceEntryType.REMOTE,
                "Home",
                "Wrong time"
        ));
    }
}

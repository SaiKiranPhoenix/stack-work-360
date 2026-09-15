package com.stackwork360.attendanceservice.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.stackwork360.attendanceservice.domain.AttendanceEntryStatus;
import com.stackwork360.attendanceservice.domain.AttendanceEntryType;
import com.stackwork360.attendanceservice.infrastructure.InMemoryAttendanceEntryRepository;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class AttendanceApplicationServiceTest {
    private final AttendanceApplicationService service = new AttendanceApplicationService(new InMemoryAttendanceEntryRepository());

    @Test
    void preventsDuplicateOpenEntriesForWorker() {
        service.checkIn(new CheckInCommand(
                "tenant-1",
                "worker-1",
                AttendanceEntryType.OFFICE,
                Instant.parse("2026-01-01T09:00:00Z"),
                "HQ"
        ));

        assertThrows(IllegalStateException.class, () -> service.checkIn(new CheckInCommand(
                "tenant-1",
                "worker-1",
                AttendanceEntryType.REMOTE,
                Instant.parse("2026-01-01T10:00:00Z"),
                "Home"
        )));
    }

    @Test
    void checksOutOpenEntry() {
        service.checkIn(new CheckInCommand(
                "tenant-1",
                "worker-1",
                AttendanceEntryType.OFFICE,
                Instant.parse("2026-01-01T09:00:00Z"),
                "HQ"
        ));

        var closed = service.checkOut("tenant-1", "worker-1", new CheckOutCommand(Instant.parse("2026-01-01T17:30:00Z")));

        assertEquals(AttendanceEntryStatus.CLOSED, closed.status());
        assertEquals(30, closed.overtime().toMinutes());
    }

    @Test
    void recordsManualCorrection() {
        var entry = service.recordRemoteWork(new RemoteWorkCommand(
                "tenant-1",
                "worker-1",
                Instant.parse("2026-01-01T09:00:00Z"),
                Instant.parse("2026-01-01T17:00:00Z"),
                "Home"
        ));

        var corrected = service.correct(entry.id(), new CorrectAttendanceCommand(
                Instant.parse("2026-01-01T08:30:00Z"),
                Instant.parse("2026-01-01T17:30:00Z"),
                AttendanceEntryType.REMOTE,
                "Home",
                "Missed actual start time"
        ));

        assertEquals(AttendanceEntryStatus.CORRECTED, corrected.status());
        assertEquals("Missed actual start time", corrected.correctionReason());
    }
}

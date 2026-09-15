package com.stackwork360.shiftschedulingservice.domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;

class ShiftAssignmentTest {
    @Test
    void detectsOverlappingAssignmentsForSameWorker() {
        ShiftTemplate morning = template("Morning", LocalTime.of(9, 0), LocalTime.of(17, 0));
        ShiftTemplate overlap = template("Overlap", LocalTime.of(16, 0), LocalTime.of(22, 0));

        ShiftAssignment first = ShiftAssignment.assign("tenant-1", morning, "worker-1", LocalDate.of(2026, 1, 1));
        ShiftAssignment second = ShiftAssignment.assign("tenant-1", overlap, "worker-1", LocalDate.of(2026, 1, 1));

        assertTrue(first.conflictsWith(second));
    }

    @Test
    void ignoresDifferentWorkersForConflictCheck() {
        ShiftTemplate template = template("Morning", LocalTime.of(9, 0), LocalTime.of(17, 0));

        ShiftAssignment first = ShiftAssignment.assign("tenant-1", template, "worker-1", LocalDate.of(2026, 1, 1));
        ShiftAssignment second = ShiftAssignment.assign("tenant-1", template, "worker-2", LocalDate.of(2026, 1, 1));

        assertFalse(first.conflictsWith(second));
    }

    private static ShiftTemplate template(String name, LocalTime start, LocalTime end) {
        return new ShiftTemplate(null, "tenant-1", name, start, end, 1);
    }
}

package com.stackwork360.shiftschedulingservice.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.stackwork360.shiftschedulingservice.domain.ShiftSwapStatus;
import com.stackwork360.shiftschedulingservice.infrastructure.InMemoryShiftAssignmentRepository;
import com.stackwork360.shiftschedulingservice.infrastructure.InMemoryShiftSwapRequestRepository;
import com.stackwork360.shiftschedulingservice.infrastructure.InMemoryShiftTemplateRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;

class ShiftSchedulingApplicationServiceTest {
    private final ShiftSchedulingApplicationService service = new ShiftSchedulingApplicationService(
            new InMemoryShiftTemplateRepository(),
            new InMemoryShiftAssignmentRepository(),
            new InMemoryShiftSwapRequestRepository()
    );

    @Test
    void rejectsConflictingAssignmentForSameWorker() {
        var template = service.createTemplate(new CreateShiftTemplateCommand(
                "tenant-1",
                "Day",
                LocalTime.of(9, 0),
                LocalTime.of(17, 0),
                1
        ));

        service.assign(new AssignShiftCommand(template.id(), "worker-1", LocalDate.of(2026, 1, 1)));

        assertThrows(IllegalArgumentException.class, () -> service.assign(new AssignShiftCommand(
                template.id(),
                "worker-1",
                LocalDate.of(2026, 1, 1)
        )));
    }

    @Test
    void generatesRotatingAssignments() {
        var template = service.createTemplate(new CreateShiftTemplateCommand(
                "tenant-1",
                "Day",
                LocalTime.of(9, 0),
                LocalTime.of(17, 0),
                1
        ));

        var assignments = service.generateRotatingAssignments(
                new CreateRotatingScheduleCommand("tenant-1", template.id(), List.of("worker-1", "worker-2"), LocalDate.of(2026, 1, 1)),
                LocalDate.of(2026, 1, 3)
        );

        assertEquals(3, assignments.size());
        assertEquals("worker-1", assignments.get(0).workerId());
        assertEquals("worker-2", assignments.get(1).workerId());
    }

    @Test
    void approvesSwapAndReassignsShift() {
        var template = service.createTemplate(new CreateShiftTemplateCommand(
                "tenant-1",
                "Day",
                LocalTime.of(9, 0),
                LocalTime.of(17, 0),
                1
        ));
        var assignment = service.assign(new AssignShiftCommand(template.id(), "worker-1", LocalDate.of(2026, 1, 1)));
        var swap = service.requestSwap(new RequestShiftSwapCommand("tenant-1", assignment.id(), "worker-1", "worker-2", "Need coverage"));

        var approved = service.approveSwap(swap.id(), "manager-1");

        assertEquals(ShiftSwapStatus.APPROVED, approved.status());
        assertEquals("worker-2", service.assignment(assignment.id()).workerId());
    }

    @Test
    void reportsUnderstaffedCoverage() {
        var template = service.createTemplate(new CreateShiftTemplateCommand(
                "tenant-1",
                "Day",
                LocalTime.of(9, 0),
                LocalTime.of(17, 0),
                2
        ));
        service.assign(new AssignShiftCommand(template.id(), "worker-1", LocalDate.of(2026, 1, 1)));

        var coverage = service.coverage(template.id(), LocalDate.of(2026, 1, 1));

        assertEquals(true, coverage.understaffed());
        assertEquals(1, coverage.assignedStaff());
    }
}

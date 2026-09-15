package com.stackwork360.peoplecoreservice.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class WorkerTest {
    @Test
    void hiresWorkerIntoPreboardingState() {
        Worker worker = hireWorker();

        assertEquals(WorkerStatus.PREBOARDING, worker.status());
        assertEquals("priya.shah@example.com", worker.workEmail());
        assertEquals("Priya Shah", worker.displayName());
    }

    @Test
    void movesFromPreboardingToActive() {
        Worker worker = hireWorker();

        worker.markActive();

        assertEquals(WorkerStatus.ACTIVE, worker.status());
    }

    @Test
    void rejectsOffboardingBeforeActiveState() {
        Worker worker = hireWorker();

        assertThrows(IllegalStateException.class, () -> worker.startOffboarding(LocalDate.now().plusDays(30)));
    }

    @Test
    void movesActiveWorkerThroughOffboardingToAlumni() {
        Worker worker = hireWorker();
        worker.markActive();

        worker.startOffboarding(LocalDate.now().plusDays(30));
        assertEquals(WorkerStatus.OFFBOARDING, worker.status());

        worker.markAlumni();
        assertEquals(WorkerStatus.ALUMNI, worker.status());
    }

    @Test
    void rejectsInvalidWorkEmail() {
        assertThrows(IllegalArgumentException.class, () -> Worker.hire(
                "tenant-1",
                "E-002",
                "Bad",
                "Email",
                "not-an-email",
                EmploymentType.EMPLOYEE,
                "Engineer",
                "engineering",
                null,
                LocalDate.now()
        ));
    }

    private static Worker hireWorker() {
        return Worker.hire(
                "tenant-1",
                "E-001",
                "Priya",
                "Shah",
                "PRIYA.SHAH@example.com",
                EmploymentType.EMPLOYEE,
                "Senior Engineer",
                "engineering",
                null,
                LocalDate.now()
        );
    }
}

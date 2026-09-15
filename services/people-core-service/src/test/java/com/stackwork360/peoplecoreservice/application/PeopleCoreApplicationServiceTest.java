package com.stackwork360.peoplecoreservice.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.stackwork360.peoplecoreservice.domain.EmploymentType;
import com.stackwork360.peoplecoreservice.domain.Worker;
import com.stackwork360.peoplecoreservice.domain.WorkerStatus;
import com.stackwork360.peoplecoreservice.infrastructure.InMemoryWorkerRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class PeopleCoreApplicationServiceTest {
    private final PeopleCoreApplicationService service =
            new PeopleCoreApplicationService(new InMemoryWorkerRepository());

    @Test
    void createsWorkerProfile() {
        Worker worker = service.create(createCommand("E-001", "samira@example.com"));

        assertEquals("E-001", worker.employeeNumber());
        assertEquals(WorkerStatus.PREBOARDING, worker.status());
    }

    @Test
    void rejectsDuplicateEmployeeNumberWithinTenant() {
        service.create(createCommand("E-001", "one@example.com"));

        assertThrows(IllegalArgumentException.class, () -> service.create(createCommand("E-001", "two@example.com")));
    }

    @Test
    void rejectsDuplicateWorkEmailWithinTenant() {
        service.create(createCommand("E-001", "same@example.com"));

        assertThrows(IllegalArgumentException.class, () -> service.create(createCommand("E-002", "same@example.com")));
    }

    @Test
    void updatesWorkerProfile() {
        Worker worker = service.create(createCommand("E-001", "samira@example.com"));

        Worker updated = service.update(worker.id(), new UpdateWorkerCommand(
                "Samira",
                "Rao",
                "samira.rao@example.com",
                "Staff Engineer",
                "platform",
                null
        ));

        assertEquals("Staff Engineer", updated.jobTitle());
        assertEquals("samira.rao@example.com", updated.workEmail());
    }

    private static CreateWorkerCommand createCommand(String employeeNumber, String workEmail) {
        return new CreateWorkerCommand(
                "tenant-1",
                employeeNumber,
                "Samira",
                "Rao",
                workEmail,
                EmploymentType.EMPLOYEE,
                "Engineer",
                "engineering",
                null,
                LocalDate.now()
        );
    }
}

package com.stackwork360.leaveservice.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.stackwork360.leaveservice.domain.LeaveRequest;
import com.stackwork360.leaveservice.domain.LeaveRequestStatus;
import com.stackwork360.leaveservice.domain.LeaveType;
import com.stackwork360.leaveservice.infrastructure.InMemoryLeaveBalanceRepository;
import com.stackwork360.leaveservice.infrastructure.InMemoryLeavePolicyRepository;
import com.stackwork360.leaveservice.infrastructure.InMemoryLeaveRequestRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class LeaveApplicationServiceTest {
    private final LeaveApplicationService service = new LeaveApplicationService(
            new InMemoryLeavePolicyRepository(),
            new InMemoryLeaveBalanceRepository(),
            new InMemoryLeaveRequestRepository()
    );

    @Test
    void submitsLeaveRequestAndReservesBalance() {
        LeaveRequest request = submit("worker-1", LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 2));

        assertEquals(LeaveRequestStatus.PENDING, request.status());
        assertEquals(1, service.balances("tenant-1", "worker-1").size());
        assertEquals(1, service.list("tenant-1").size());
    }

    @Test
    void rejectsOverlappingLeaveRequest() {
        submit("worker-1", LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 2));

        assertThrows(IllegalArgumentException.class, () -> submit(
                "worker-1",
                LocalDate.of(2026, 1, 2),
                LocalDate.of(2026, 1, 3)
        ));
    }

    @Test
    void approvesLeaveRequestAndUpdatesAvailability() {
        LeaveRequest request = submit("worker-1", LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 1));

        LeaveRequest approved = service.approve(request.id(), "manager-1");

        assertEquals(LeaveRequestStatus.APPROVED, approved.status());
        assertFalse(service.availability("tenant-1", LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 1))
                .get(0)
                .unavailableWorkerIds()
                .isEmpty());
    }

    @Test
    void rejectionReleasesReservedBalance() {
        LeaveRequest request = submit("worker-1", LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 1));

        service.reject(request.id(), "manager-1");

        assertEquals(0, service.balances("tenant-1", "worker-1").get(0).reservedDays().intValue());
    }

    private LeaveRequest submit(String workerId, LocalDate startDate, LocalDate endDate) {
        return service.submit(new SubmitLeaveRequestCommand(
                "tenant-1",
                workerId,
                LeaveType.VACATION,
                startDate,
                endDate,
                "Rest"
        ));
    }
}

package com.stackwork360.leaveservice.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class LeaveRequestTest {
    @Test
    void calculatesRequestedDaysInclusively() {
        LeaveRequest request = request(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 3));

        assertEquals(BigDecimal.valueOf(3), request.requestedDays());
    }

    @Test
    void approvesPendingRequest() {
        LeaveRequest request = request(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 1));

        request.approve("manager-1");

        assertEquals(LeaveRequestStatus.APPROVED, request.status());
        assertEquals("manager-1", request.decidedBy());
    }

    @Test
    void detectsDateOverlap() {
        LeaveRequest request = request(LocalDate.of(2026, 1, 5), LocalDate.of(2026, 1, 7));

        assertTrue(request.overlaps(LocalDate.of(2026, 1, 7), LocalDate.of(2026, 1, 8)));
    }

    @Test
    void rejectsEndDateBeforeStartDate() {
        assertThrows(IllegalArgumentException.class, () -> request(
                LocalDate.of(2026, 1, 8),
                LocalDate.of(2026, 1, 7)
        ));
    }

    private static LeaveRequest request(LocalDate startDate, LocalDate endDate) {
        return LeaveRequest.submit("tenant-1", "worker-1", LeaveType.VACATION, startDate, endDate, "Rest");
    }
}

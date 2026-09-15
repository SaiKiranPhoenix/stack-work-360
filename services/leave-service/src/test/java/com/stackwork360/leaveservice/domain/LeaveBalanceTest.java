package com.stackwork360.leaveservice.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class LeaveBalanceTest {
    @Test
    void reservesAndConsumesBalance() {
        LeaveBalance balance = new LeaveBalance("tenant-1", "worker-1", LeaveType.VACATION, BigDecimal.TEN);

        balance.reserve(BigDecimal.valueOf(2));
        assertEquals(BigDecimal.valueOf(8), balance.remainingDays());

        balance.consumeReserved(BigDecimal.valueOf(2));
        assertEquals(BigDecimal.valueOf(8), balance.availableDays());
        assertEquals(BigDecimal.ZERO, balance.reservedDays());
    }

    @Test
    void rejectsReservationBeyondAvailableBalance() {
        LeaveBalance balance = new LeaveBalance("tenant-1", "worker-1", LeaveType.VACATION, BigDecimal.ONE);

        assertThrows(IllegalStateException.class, () -> balance.reserve(BigDecimal.valueOf(2)));
    }
}

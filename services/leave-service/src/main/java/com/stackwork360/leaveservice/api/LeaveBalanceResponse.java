package com.stackwork360.leaveservice.api;

import com.stackwork360.leaveservice.domain.LeaveBalance;
import com.stackwork360.leaveservice.domain.LeaveType;
import java.math.BigDecimal;

public record LeaveBalanceResponse(
        String workerId,
        LeaveType leaveType,
        BigDecimal availableDays,
        BigDecimal reservedDays,
        BigDecimal remainingDays
) {
    public static LeaveBalanceResponse from(LeaveBalance balance) {
        return new LeaveBalanceResponse(
                balance.workerId(),
                balance.leaveType(),
                balance.availableDays(),
                balance.reservedDays(),
                balance.remainingDays()
        );
    }
}

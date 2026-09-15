package com.stackwork360.leaveservice.application;

import com.stackwork360.leaveservice.domain.LeaveType;
import java.time.LocalDate;

public record SubmitLeaveRequestCommand(
        String tenantId,
        String workerId,
        LeaveType leaveType,
        LocalDate startDate,
        LocalDate endDate,
        String reason
) {
}

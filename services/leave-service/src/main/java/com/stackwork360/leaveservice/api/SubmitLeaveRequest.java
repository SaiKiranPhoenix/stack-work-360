package com.stackwork360.leaveservice.api;

import com.stackwork360.leaveservice.domain.LeaveType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record SubmitLeaveRequest(
        @NotBlank
        String workerId,

        @NotNull
        LeaveType leaveType,

        @NotNull
        LocalDate startDate,

        @NotNull
        LocalDate endDate,

        String reason
) {
}

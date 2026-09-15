package com.stackwork360.shiftschedulingservice.api;

import com.stackwork360.shiftschedulingservice.application.RequestShiftSwapCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record RequestShiftSwapRequest(
        @NotNull UUID assignmentId,
        @NotBlank String requestedBy,
        @NotBlank String targetWorkerId,
        @NotBlank String reason
) {
    RequestShiftSwapCommand toCommand(String tenantId) {
        return new RequestShiftSwapCommand(tenantId, assignmentId, requestedBy, targetWorkerId, reason);
    }
}

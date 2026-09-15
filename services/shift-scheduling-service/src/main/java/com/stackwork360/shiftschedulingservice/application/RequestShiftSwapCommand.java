package com.stackwork360.shiftschedulingservice.application;

import java.util.UUID;

public record RequestShiftSwapCommand(
        String tenantId,
        UUID assignmentId,
        String requestedBy,
        String targetWorkerId,
        String reason
) {
}

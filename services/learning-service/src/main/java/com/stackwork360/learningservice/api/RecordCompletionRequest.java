package com.stackwork360.learningservice.api;

import com.stackwork360.learningservice.application.RecordCompletionCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

public record RecordCompletionRequest(
        @NotBlank String workerId,
        @NotNull UUID resourceId,
        @NotNull BigDecimal score
) {
    RecordCompletionCommand toCommand(String tenantId) {
        return new RecordCompletionCommand(tenantId, workerId, resourceId, score);
    }
}

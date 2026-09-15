package com.stackwork360.integrationservice.api;

import com.stackwork360.integrationservice.application.RecordCheckpointCommand;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public record RecordCheckpointRequest(
        @NotBlank String cursor
) {
    RecordCheckpointCommand toCommand(String tenantId, UUID connectorId) {
        return new RecordCheckpointCommand(tenantId, connectorId, cursor);
    }
}

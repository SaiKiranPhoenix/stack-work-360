package com.stackwork360.integrationservice.application;

import java.util.UUID;

public record RecordCheckpointCommand(
        String tenantId,
        UUID connectorId,
        String cursor
) {
}

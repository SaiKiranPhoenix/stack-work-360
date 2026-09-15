package com.stackwork360.integrationservice.api;

import com.stackwork360.integrationservice.domain.ExternalProviderCheckpoint;
import java.time.Instant;
import java.util.UUID;

public record CheckpointResponse(
        String tenantId,
        UUID connectorId,
        String cursor,
        Instant syncedAt
) {
    static CheckpointResponse from(ExternalProviderCheckpoint checkpoint) {
        return new CheckpointResponse(
                checkpoint.tenantId(),
                checkpoint.connectorId(),
                checkpoint.cursor(),
                checkpoint.syncedAt()
        );
    }
}

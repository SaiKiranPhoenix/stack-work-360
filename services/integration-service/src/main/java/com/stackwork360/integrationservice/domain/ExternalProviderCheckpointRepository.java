package com.stackwork360.integrationservice.domain;

import java.util.Optional;
import java.util.UUID;

public interface ExternalProviderCheckpointRepository {
    ExternalProviderCheckpoint save(ExternalProviderCheckpoint checkpoint);

    Optional<ExternalProviderCheckpoint> find(String tenantId, UUID connectorId);
}

package com.stackwork360.integrationservice.infrastructure;

import com.stackwork360.integrationservice.domain.ExternalProviderCheckpoint;
import com.stackwork360.integrationservice.domain.ExternalProviderCheckpointRepository;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryExternalProviderCheckpointRepository implements ExternalProviderCheckpointRepository {
    private final Map<String, ExternalProviderCheckpoint> checkpoints = new ConcurrentHashMap<>();

    @Override
    public ExternalProviderCheckpoint save(ExternalProviderCheckpoint checkpoint) {
        checkpoints.put(key(checkpoint.tenantId(), checkpoint.connectorId()), checkpoint);
        return checkpoint;
    }

    @Override
    public Optional<ExternalProviderCheckpoint> find(String tenantId, UUID connectorId) {
        return Optional.ofNullable(checkpoints.get(key(tenantId, connectorId)));
    }

    private static String key(String tenantId, UUID connectorId) {
        return tenantId + ":" + connectorId;
    }
}

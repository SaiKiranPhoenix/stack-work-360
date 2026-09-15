package com.stackwork360.integrationservice.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConnectorConfigurationRepository {
    ConnectorConfiguration save(ConnectorConfiguration configuration);

    Optional<ConnectorConfiguration> findById(UUID id);

    List<ConnectorConfiguration> findByTenantId(String tenantId);

    List<ConnectorConfiguration> findByTenantIdAndCategory(String tenantId, ConnectorCategory category);
}

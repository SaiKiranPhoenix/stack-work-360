package com.stackwork360.integrationservice.infrastructure;

import com.stackwork360.integrationservice.domain.ConnectorCategory;
import com.stackwork360.integrationservice.domain.ConnectorConfiguration;
import com.stackwork360.integrationservice.domain.ConnectorConfigurationRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryConnectorConfigurationRepository implements ConnectorConfigurationRepository {
    private final Map<UUID, ConnectorConfiguration> connectors = new ConcurrentHashMap<>();

    @Override
    public ConnectorConfiguration save(ConnectorConfiguration configuration) {
        connectors.put(configuration.id(), configuration);
        return configuration;
    }

    @Override
    public Optional<ConnectorConfiguration> findById(UUID id) {
        return Optional.ofNullable(connectors.get(id));
    }

    @Override
    public List<ConnectorConfiguration> findByTenantId(String tenantId) {
        return connectors.values().stream()
                .filter(connector -> connector.tenantId().equals(tenantId))
                .sorted(Comparator.comparing(ConnectorConfiguration::displayName))
                .toList();
    }

    @Override
    public List<ConnectorConfiguration> findByTenantIdAndCategory(String tenantId, ConnectorCategory category) {
        return connectors.values().stream()
                .filter(connector -> connector.tenantId().equals(tenantId))
                .filter(connector -> connector.category() == category)
                .sorted(Comparator.comparing(ConnectorConfiguration::displayName))
                .toList();
    }
}

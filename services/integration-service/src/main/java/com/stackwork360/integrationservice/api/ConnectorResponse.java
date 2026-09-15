package com.stackwork360.integrationservice.api;

import com.stackwork360.integrationservice.domain.ConnectorCategory;
import com.stackwork360.integrationservice.domain.ConnectorConfiguration;
import com.stackwork360.integrationservice.domain.ConnectorProvider;
import com.stackwork360.integrationservice.domain.ConnectorStatus;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record ConnectorResponse(
        UUID id,
        String tenantId,
        ConnectorCategory category,
        ConnectorProvider provider,
        String displayName,
        ConnectorStatus status,
        Map<String, String> settings,
        Instant createdAt,
        Instant updatedAt
) {
    static ConnectorResponse from(ConnectorConfiguration connector) {
        return new ConnectorResponse(
                connector.id(),
                connector.tenantId(),
                connector.category(),
                connector.provider(),
                connector.displayName(),
                connector.status(),
                connector.settings(),
                connector.createdAt(),
                connector.updatedAt()
        );
    }
}

package com.stackwork360.integrationservice.application;

import com.stackwork360.integrationservice.domain.ConnectorCategory;
import com.stackwork360.integrationservice.domain.ConnectorProvider;
import java.util.Map;

public record CreateConnectorCommand(
        String tenantId,
        ConnectorCategory category,
        ConnectorProvider provider,
        String displayName,
        Map<String, String> settings,
        String webhookSecret
) {
}

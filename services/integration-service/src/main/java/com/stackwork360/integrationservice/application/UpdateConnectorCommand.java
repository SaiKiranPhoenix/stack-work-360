package com.stackwork360.integrationservice.application;

import com.stackwork360.integrationservice.domain.ConnectorStatus;
import java.util.Map;

public record UpdateConnectorCommand(
        String displayName,
        ConnectorStatus status,
        Map<String, String> settings,
        String webhookSecret
) {
}

package com.stackwork360.integrationservice.api;

import com.stackwork360.integrationservice.application.CreateConnectorCommand;
import com.stackwork360.integrationservice.domain.ConnectorCategory;
import com.stackwork360.integrationservice.domain.ConnectorProvider;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

public record CreateConnectorRequest(
        @NotNull ConnectorCategory category,
        @NotNull ConnectorProvider provider,
        @NotBlank String displayName,
        Map<String, String> settings,
        @NotBlank String webhookSecret
) {
    CreateConnectorCommand toCommand(String tenantId) {
        return new CreateConnectorCommand(
                tenantId,
                category,
                provider,
                displayName,
                settings == null ? Map.of() : settings,
                webhookSecret
        );
    }
}

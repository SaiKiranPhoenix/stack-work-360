package com.stackwork360.integrationservice.api;

import com.stackwork360.integrationservice.application.UpdateConnectorCommand;
import com.stackwork360.integrationservice.domain.ConnectorStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

public record UpdateConnectorRequest(
        @NotBlank String displayName,
        @NotNull ConnectorStatus status,
        Map<String, String> settings,
        @NotBlank String webhookSecret
) {
    UpdateConnectorCommand toCommand() {
        return new UpdateConnectorCommand(displayName, status, settings == null ? Map.of() : settings, webhookSecret);
    }
}

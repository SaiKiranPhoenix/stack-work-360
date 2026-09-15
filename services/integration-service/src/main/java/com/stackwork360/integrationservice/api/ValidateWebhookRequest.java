package com.stackwork360.integrationservice.api;

import com.stackwork360.integrationservice.application.ValidateWebhookCommand;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public record ValidateWebhookRequest(
        @NotBlank String payload,
        @NotBlank String signature
) {
    ValidateWebhookCommand toCommand(UUID connectorId) {
        return new ValidateWebhookCommand(connectorId, payload, signature);
    }
}

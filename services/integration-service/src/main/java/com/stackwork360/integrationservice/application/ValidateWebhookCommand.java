package com.stackwork360.integrationservice.application;

import java.util.UUID;

public record ValidateWebhookCommand(
        UUID connectorId,
        String payload,
        String signature
) {
}

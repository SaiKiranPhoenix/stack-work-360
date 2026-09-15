package com.stackwork360.integrationservice.api;

import com.stackwork360.integrationservice.domain.WebhookValidationResult;

public record WebhookValidationResponse(
        boolean valid,
        String reason
) {
    static WebhookValidationResponse from(WebhookValidationResult result) {
        return new WebhookValidationResponse(result.valid(), result.reason());
    }
}

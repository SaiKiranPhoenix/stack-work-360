package com.stackwork360.integrationservice.domain;

public record WebhookValidationResult(
        boolean valid,
        String reason
) {
    public static WebhookValidationResult valid() {
        return new WebhookValidationResult(true, "valid");
    }

    public static WebhookValidationResult invalid(String reason) {
        return new WebhookValidationResult(false, reason);
    }
}

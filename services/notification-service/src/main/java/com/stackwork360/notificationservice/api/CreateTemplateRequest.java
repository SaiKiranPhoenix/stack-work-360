package com.stackwork360.notificationservice.api;

import com.stackwork360.notificationservice.application.CreateTemplateCommand;
import com.stackwork360.notificationservice.domain.NotificationPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTemplateRequest(
        @NotBlank String key,
        @NotBlank String subject,
        @NotBlank String body,
        @NotNull NotificationPriority defaultPriority
) {
    CreateTemplateCommand toCommand(String tenantId) {
        return new CreateTemplateCommand(tenantId, key, subject, body, defaultPriority);
    }
}

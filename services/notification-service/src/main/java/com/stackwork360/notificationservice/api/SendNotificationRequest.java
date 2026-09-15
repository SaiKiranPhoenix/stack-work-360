package com.stackwork360.notificationservice.api;

import com.stackwork360.notificationservice.application.SendNotificationCommand;
import com.stackwork360.notificationservice.domain.NotificationChannel;
import jakarta.validation.constraints.NotBlank;
import java.util.Map;
import java.util.Set;

public record SendNotificationRequest(
        @NotBlank String recipientId,
        @NotBlank String templateKey,
        Map<String, String> variables,
        Set<NotificationChannel> requestedChannels
) {
    SendNotificationCommand toCommand(String tenantId) {
        return new SendNotificationCommand(
                tenantId,
                recipientId,
                templateKey,
                variables == null ? Map.of() : variables,
                requestedChannels
        );
    }
}

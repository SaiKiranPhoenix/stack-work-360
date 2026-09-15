package com.stackwork360.notificationservice.api;

import com.stackwork360.notificationservice.application.ConfigurePreferenceCommand;
import com.stackwork360.notificationservice.domain.NotificationChannel;
import jakarta.validation.constraints.NotBlank;
import java.util.Set;

public record ConfigurePreferenceRequest(
        Set<NotificationChannel> enabledChannels,
        boolean quietHoursEnabled,
        @NotBlank String timezone
) {
    ConfigurePreferenceCommand toCommand(String tenantId, String workerId) {
        return new ConfigurePreferenceCommand(tenantId, workerId, enabledChannels, quietHoursEnabled, timezone);
    }
}

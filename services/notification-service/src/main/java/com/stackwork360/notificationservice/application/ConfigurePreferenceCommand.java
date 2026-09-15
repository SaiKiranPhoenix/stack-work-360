package com.stackwork360.notificationservice.application;

import com.stackwork360.notificationservice.domain.NotificationChannel;
import java.util.Set;

public record ConfigurePreferenceCommand(
        String tenantId,
        String workerId,
        Set<NotificationChannel> enabledChannels,
        boolean quietHoursEnabled,
        String timezone
) {
}

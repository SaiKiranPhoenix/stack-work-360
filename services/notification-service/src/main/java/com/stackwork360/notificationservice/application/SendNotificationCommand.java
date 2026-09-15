package com.stackwork360.notificationservice.application;

import com.stackwork360.notificationservice.domain.NotificationChannel;
import java.util.Map;
import java.util.Set;

public record SendNotificationCommand(
        String tenantId,
        String recipientId,
        String templateKey,
        Map<String, String> variables,
        Set<NotificationChannel> requestedChannels
) {
}

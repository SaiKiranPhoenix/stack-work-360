package com.stackwork360.notificationservice.application;

import com.stackwork360.notificationservice.domain.NotificationPriority;

public record CreateTemplateCommand(
        String tenantId,
        String key,
        String subject,
        String body,
        NotificationPriority defaultPriority
) {
}

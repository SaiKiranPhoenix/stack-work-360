package com.stackwork360.notificationservice.domain;

public record RenderedNotification(
        String subject,
        String body,
        NotificationPriority priority
) {
}

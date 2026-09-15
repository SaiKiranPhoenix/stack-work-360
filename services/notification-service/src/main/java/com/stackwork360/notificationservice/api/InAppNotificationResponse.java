package com.stackwork360.notificationservice.api;

import com.stackwork360.notificationservice.domain.InAppNotification;
import com.stackwork360.notificationservice.domain.NotificationPriority;
import com.stackwork360.notificationservice.domain.NotificationStatus;
import java.time.Instant;
import java.util.UUID;

public record InAppNotificationResponse(
        UUID id,
        String tenantId,
        String recipientId,
        String subject,
        String body,
        NotificationPriority priority,
        NotificationStatus status,
        Instant createdAt,
        Instant updatedAt
) {
    static InAppNotificationResponse from(InAppNotification notification) {
        return new InAppNotificationResponse(
                notification.id(),
                notification.tenantId(),
                notification.recipientId(),
                notification.subject(),
                notification.body(),
                notification.priority(),
                notification.status(),
                notification.createdAt(),
                notification.updatedAt()
        );
    }
}

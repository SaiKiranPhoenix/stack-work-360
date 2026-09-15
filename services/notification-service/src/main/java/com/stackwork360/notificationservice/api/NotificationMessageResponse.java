package com.stackwork360.notificationservice.api;

import com.stackwork360.notificationservice.domain.DeliveryStatus;
import com.stackwork360.notificationservice.domain.NotificationChannel;
import com.stackwork360.notificationservice.domain.NotificationMessage;
import com.stackwork360.notificationservice.domain.NotificationPriority;
import java.time.Instant;
import java.util.UUID;

public record NotificationMessageResponse(
        UUID id,
        String tenantId,
        String recipientId,
        NotificationChannel channel,
        String subject,
        String body,
        NotificationPriority priority,
        DeliveryStatus deliveryStatus,
        String providerReference,
        String failureReason,
        Instant createdAt,
        Instant updatedAt
) {
    static NotificationMessageResponse from(NotificationMessage message) {
        return new NotificationMessageResponse(
                message.id(),
                message.tenantId(),
                message.recipientId(),
                message.channel(),
                message.subject(),
                message.body(),
                message.priority(),
                message.deliveryStatus(),
                message.providerReference(),
                message.failureReason(),
                message.createdAt(),
                message.updatedAt()
        );
    }
}

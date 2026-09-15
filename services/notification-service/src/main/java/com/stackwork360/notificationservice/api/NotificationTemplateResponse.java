package com.stackwork360.notificationservice.api;

import com.stackwork360.notificationservice.domain.NotificationPriority;
import com.stackwork360.notificationservice.domain.NotificationTemplate;
import java.time.Instant;
import java.util.UUID;

public record NotificationTemplateResponse(
        UUID id,
        String tenantId,
        String key,
        String subject,
        String body,
        NotificationPriority defaultPriority,
        Instant createdAt,
        Instant updatedAt
) {
    static NotificationTemplateResponse from(NotificationTemplate template) {
        return new NotificationTemplateResponse(
                template.id(),
                template.tenantId(),
                template.key(),
                template.subject(),
                template.body(),
                template.defaultPriority(),
                template.createdAt(),
                template.updatedAt()
        );
    }
}

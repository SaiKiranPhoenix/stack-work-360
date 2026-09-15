package com.stackwork360.notificationservice.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InAppNotificationRepository {
    InAppNotification save(InAppNotification notification);

    Optional<InAppNotification> findById(UUID id);

    List<InAppNotification> findByTenantIdAndRecipientId(String tenantId, String recipientId);
}

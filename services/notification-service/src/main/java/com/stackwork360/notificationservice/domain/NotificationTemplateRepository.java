package com.stackwork360.notificationservice.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationTemplateRepository {
    NotificationTemplate save(NotificationTemplate template);

    Optional<NotificationTemplate> findById(UUID id);

    Optional<NotificationTemplate> findByTenantIdAndKey(String tenantId, String key);

    List<NotificationTemplate> findByTenantId(String tenantId);
}

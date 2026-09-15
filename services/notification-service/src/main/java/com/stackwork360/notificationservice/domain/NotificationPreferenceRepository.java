package com.stackwork360.notificationservice.domain;

import java.util.Optional;

public interface NotificationPreferenceRepository {
    NotificationPreference save(NotificationPreference preference);

    Optional<NotificationPreference> find(String tenantId, String workerId);
}

package com.stackwork360.notificationservice.domain;

import java.util.List;

public interface NotificationMessageRepository {
    NotificationMessage save(NotificationMessage message);

    List<NotificationMessage> findByTenantId(String tenantId);
}

package com.stackwork360.notificationservice.infrastructure;

import com.stackwork360.notificationservice.domain.InAppNotification;
import com.stackwork360.notificationservice.domain.InAppNotificationRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryInAppNotificationRepository implements InAppNotificationRepository {
    private final Map<UUID, InAppNotification> notifications = new ConcurrentHashMap<>();

    @Override
    public InAppNotification save(InAppNotification notification) {
        notifications.put(notification.id(), notification);
        return notification;
    }

    @Override
    public Optional<InAppNotification> findById(UUID id) {
        return Optional.ofNullable(notifications.get(id));
    }

    @Override
    public List<InAppNotification> findByTenantIdAndRecipientId(String tenantId, String recipientId) {
        return notifications.values().stream()
                .filter(notification -> notification.tenantId().equals(tenantId))
                .filter(notification -> notification.recipientId().equals(recipientId))
                .sorted(Comparator.comparing(InAppNotification::createdAt).reversed())
                .toList();
    }
}

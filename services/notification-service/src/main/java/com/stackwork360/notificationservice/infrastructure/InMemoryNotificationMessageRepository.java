package com.stackwork360.notificationservice.infrastructure;

import com.stackwork360.notificationservice.domain.NotificationMessage;
import com.stackwork360.notificationservice.domain.NotificationMessageRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryNotificationMessageRepository implements NotificationMessageRepository {
    private final Map<UUID, NotificationMessage> messages = new ConcurrentHashMap<>();

    @Override
    public NotificationMessage save(NotificationMessage message) {
        messages.put(message.id(), message);
        return message;
    }

    @Override
    public List<NotificationMessage> findByTenantId(String tenantId) {
        return messages.values().stream()
                .filter(message -> message.tenantId().equals(tenantId))
                .sorted(Comparator.comparing(NotificationMessage::createdAt).reversed())
                .toList();
    }
}

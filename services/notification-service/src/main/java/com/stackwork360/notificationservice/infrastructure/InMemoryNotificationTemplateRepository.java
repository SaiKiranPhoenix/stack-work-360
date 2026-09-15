package com.stackwork360.notificationservice.infrastructure;

import com.stackwork360.notificationservice.domain.NotificationTemplate;
import com.stackwork360.notificationservice.domain.NotificationTemplateRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryNotificationTemplateRepository implements NotificationTemplateRepository {
    private final Map<UUID, NotificationTemplate> templates = new ConcurrentHashMap<>();

    @Override
    public NotificationTemplate save(NotificationTemplate template) {
        templates.put(template.id(), template);
        return template;
    }

    @Override
    public Optional<NotificationTemplate> findById(UUID id) {
        return Optional.ofNullable(templates.get(id));
    }

    @Override
    public Optional<NotificationTemplate> findByTenantIdAndKey(String tenantId, String key) {
        return templates.values().stream()
                .filter(template -> template.tenantId().equals(tenantId))
                .filter(template -> template.key().equals(key))
                .findFirst();
    }

    @Override
    public List<NotificationTemplate> findByTenantId(String tenantId) {
        return templates.values().stream()
                .filter(template -> template.tenantId().equals(tenantId))
                .sorted(Comparator.comparing(NotificationTemplate::key))
                .toList();
    }
}

package com.stackwork360.notificationservice.infrastructure;

import com.stackwork360.notificationservice.domain.NotificationPreference;
import com.stackwork360.notificationservice.domain.NotificationPreferenceRepository;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryNotificationPreferenceRepository implements NotificationPreferenceRepository {
    private final Map<String, NotificationPreference> preferences = new ConcurrentHashMap<>();

    @Override
    public NotificationPreference save(NotificationPreference preference) {
        preferences.put(key(preference.tenantId(), preference.workerId()), preference);
        return preference;
    }

    @Override
    public Optional<NotificationPreference> find(String tenantId, String workerId) {
        return Optional.ofNullable(preferences.get(key(tenantId, workerId)));
    }

    private static String key(String tenantId, String workerId) {
        return tenantId + ":" + workerId;
    }
}

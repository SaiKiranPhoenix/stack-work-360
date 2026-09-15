package com.stackwork360.notificationservice.api;

import com.stackwork360.notificationservice.domain.NotificationChannel;
import com.stackwork360.notificationservice.domain.NotificationPreference;
import java.time.Instant;
import java.util.Set;

public record NotificationPreferenceResponse(
        String tenantId,
        String workerId,
        Set<NotificationChannel> enabledChannels,
        boolean quietHoursEnabled,
        String timezone,
        Instant updatedAt
) {
    static NotificationPreferenceResponse from(NotificationPreference preference) {
        return new NotificationPreferenceResponse(
                preference.tenantId(),
                preference.workerId(),
                preference.enabledChannels(),
                preference.quietHoursEnabled(),
                preference.timezone(),
                preference.updatedAt()
        );
    }
}

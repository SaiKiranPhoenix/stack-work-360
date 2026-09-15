package com.stackwork360.notificationservice.domain;

import java.time.Instant;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

public final class NotificationPreference {
    private final String tenantId;
    private final String workerId;
    private final EnumSet<NotificationChannel> enabledChannels;
    private boolean quietHoursEnabled;
    private String timezone;
    private Instant updatedAt;

    private NotificationPreference(
            String tenantId,
            String workerId,
            Set<NotificationChannel> enabledChannels,
            boolean quietHoursEnabled,
            String timezone,
            Instant updatedAt
    ) {
        this.tenantId = requireText(tenantId, "tenant id is required");
        this.workerId = requireText(workerId, "worker id is required");
        this.enabledChannels = enabledChannels == null || enabledChannels.isEmpty()
                ? EnumSet.of(NotificationChannel.IN_APP)
                : EnumSet.copyOf(enabledChannels);
        this.quietHoursEnabled = quietHoursEnabled;
        this.timezone = requireText(timezone, "timezone is required");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updated at is required");
    }

    public static NotificationPreference defaults(String tenantId, String workerId) {
        return new NotificationPreference(
                tenantId,
                workerId,
                EnumSet.of(NotificationChannel.EMAIL, NotificationChannel.IN_APP),
                false,
                "UTC",
                Instant.now()
        );
    }

    public static NotificationPreference configure(
            String tenantId,
            String workerId,
            Set<NotificationChannel> enabledChannels,
            boolean quietHoursEnabled,
            String timezone
    ) {
        return new NotificationPreference(tenantId, workerId, enabledChannels, quietHoursEnabled, timezone, Instant.now());
    }

    public boolean allows(NotificationChannel channel) {
        return enabledChannels.contains(channel);
    }

    public void update(Set<NotificationChannel> enabledChannels, boolean quietHoursEnabled, String timezone) {
        this.enabledChannels.clear();
        this.enabledChannels.addAll(enabledChannels == null || enabledChannels.isEmpty()
                ? EnumSet.of(NotificationChannel.IN_APP)
                : EnumSet.copyOf(enabledChannels));
        this.quietHoursEnabled = quietHoursEnabled;
        this.timezone = requireText(timezone, "timezone is required");
        this.updatedAt = Instant.now();
    }

    public String tenantId() {
        return tenantId;
    }

    public String workerId() {
        return workerId;
    }

    public Set<NotificationChannel> enabledChannels() {
        return Set.copyOf(enabledChannels);
    }

    public boolean quietHoursEnabled() {
        return quietHoursEnabled;
    }

    public String timezone() {
        return timezone;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}

package com.stackwork360.notificationservice.domain;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public final class NotificationTemplate {
    private final UUID id;
    private final String tenantId;
    private final String key;
    private String subject;
    private String body;
    private NotificationPriority defaultPriority;
    private final Instant createdAt;
    private Instant updatedAt;

    private NotificationTemplate(
            UUID id,
            String tenantId,
            String key,
            String subject,
            String body,
            NotificationPriority defaultPriority,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = Objects.requireNonNull(id, "template id is required");
        this.tenantId = requireText(tenantId, "tenant id is required");
        this.key = requireText(key, "template key is required");
        this.subject = requireText(subject, "template subject is required");
        this.body = requireText(body, "template body is required");
        this.defaultPriority = Objects.requireNonNull(defaultPriority, "default priority is required");
        this.createdAt = Objects.requireNonNull(createdAt, "created at is required");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updated at is required");
    }

    public static NotificationTemplate create(
            String tenantId,
            String key,
            String subject,
            String body,
            NotificationPriority defaultPriority
    ) {
        Instant now = Instant.now();
        return new NotificationTemplate(UUID.randomUUID(), tenantId, key, subject, body, defaultPriority, now, now);
    }

    public void update(String subject, String body, NotificationPriority defaultPriority) {
        this.subject = requireText(subject, "template subject is required");
        this.body = requireText(body, "template body is required");
        this.defaultPriority = Objects.requireNonNull(defaultPriority, "default priority is required");
        this.updatedAt = Instant.now();
    }

    public RenderedNotification render(Map<String, String> variables) {
        Map<String, String> safeVariables = variables == null ? Map.of() : variables;
        return new RenderedNotification(
                replace(subject, safeVariables),
                replace(body, safeVariables),
                defaultPriority
        );
    }

    public UUID id() {
        return id;
    }

    public String tenantId() {
        return tenantId;
    }

    public String key() {
        return key;
    }

    public String subject() {
        return subject;
    }

    public String body() {
        return body;
    }

    public NotificationPriority defaultPriority() {
        return defaultPriority;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

    private static String replace(String template, Map<String, String> variables) {
        String rendered = template;
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            rendered = rendered.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }
        return rendered;
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}

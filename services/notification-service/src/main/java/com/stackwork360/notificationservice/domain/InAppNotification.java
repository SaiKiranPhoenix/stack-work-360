package com.stackwork360.notificationservice.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public final class InAppNotification {
    private final UUID id;
    private final String tenantId;
    private final String recipientId;
    private final String subject;
    private final String body;
    private final NotificationPriority priority;
    private NotificationStatus status;
    private final Instant createdAt;
    private Instant updatedAt;

    private InAppNotification(
            UUID id,
            String tenantId,
            String recipientId,
            String subject,
            String body,
            NotificationPriority priority,
            NotificationStatus status,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = Objects.requireNonNull(id, "in-app notification id is required");
        this.tenantId = requireText(tenantId, "tenant id is required");
        this.recipientId = requireText(recipientId, "recipient id is required");
        this.subject = requireText(subject, "subject is required");
        this.body = requireText(body, "body is required");
        this.priority = Objects.requireNonNull(priority, "priority is required");
        this.status = Objects.requireNonNull(status, "notification status is required");
        this.createdAt = Objects.requireNonNull(createdAt, "created at is required");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updated at is required");
    }

    public static InAppNotification unread(String tenantId, String recipientId, String subject, String body, NotificationPriority priority) {
        Instant now = Instant.now();
        return new InAppNotification(UUID.randomUUID(), tenantId, recipientId, subject, body, priority, NotificationStatus.UNREAD, now, now);
    }

    public void markRead() {
        if (status == NotificationStatus.ARCHIVED) {
            throw new IllegalStateException("archived notifications cannot be marked read");
        }
        status = NotificationStatus.READ;
        updatedAt = Instant.now();
    }

    public void archive() {
        status = NotificationStatus.ARCHIVED;
        updatedAt = Instant.now();
    }

    public UUID id() {
        return id;
    }

    public String tenantId() {
        return tenantId;
    }

    public String recipientId() {
        return recipientId;
    }

    public String subject() {
        return subject;
    }

    public String body() {
        return body;
    }

    public NotificationPriority priority() {
        return priority;
    }

    public NotificationStatus status() {
        return status;
    }

    public Instant createdAt() {
        return createdAt;
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

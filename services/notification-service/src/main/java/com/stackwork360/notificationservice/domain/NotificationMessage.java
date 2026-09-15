package com.stackwork360.notificationservice.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public final class NotificationMessage {
    private final UUID id;
    private final String tenantId;
    private final String recipientId;
    private final NotificationChannel channel;
    private final String subject;
    private final String body;
    private final NotificationPriority priority;
    private DeliveryStatus deliveryStatus;
    private String providerReference;
    private String failureReason;
    private final Instant createdAt;
    private Instant updatedAt;

    private NotificationMessage(
            UUID id,
            String tenantId,
            String recipientId,
            NotificationChannel channel,
            String subject,
            String body,
            NotificationPriority priority,
            DeliveryStatus deliveryStatus,
            String providerReference,
            String failureReason,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = Objects.requireNonNull(id, "message id is required");
        this.tenantId = requireText(tenantId, "tenant id is required");
        this.recipientId = requireText(recipientId, "recipient id is required");
        this.channel = Objects.requireNonNull(channel, "channel is required");
        this.subject = requireText(subject, "subject is required");
        this.body = requireText(body, "body is required");
        this.priority = Objects.requireNonNull(priority, "priority is required");
        this.deliveryStatus = Objects.requireNonNull(deliveryStatus, "delivery status is required");
        this.providerReference = providerReference;
        this.failureReason = failureReason;
        this.createdAt = Objects.requireNonNull(createdAt, "created at is required");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updated at is required");
    }

    public static NotificationMessage accepted(
            String tenantId,
            String recipientId,
            NotificationChannel channel,
            String subject,
            String body,
            NotificationPriority priority
    ) {
        Instant now = Instant.now();
        return new NotificationMessage(UUID.randomUUID(), tenantId, recipientId, channel, subject, body, priority, DeliveryStatus.ACCEPTED, null, null, now, now);
    }

    public void markDelivered(String providerReference) {
        this.deliveryStatus = DeliveryStatus.DELIVERED;
        this.providerReference = requireText(providerReference, "provider reference is required");
        this.failureReason = null;
        this.updatedAt = Instant.now();
    }

    public void markFailed(String failureReason) {
        this.deliveryStatus = DeliveryStatus.FAILED;
        this.failureReason = requireText(failureReason, "failure reason is required");
        this.updatedAt = Instant.now();
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

    public NotificationChannel channel() {
        return channel;
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

    public DeliveryStatus deliveryStatus() {
        return deliveryStatus;
    }

    public String providerReference() {
        return providerReference;
    }

    public String failureReason() {
        return failureReason;
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

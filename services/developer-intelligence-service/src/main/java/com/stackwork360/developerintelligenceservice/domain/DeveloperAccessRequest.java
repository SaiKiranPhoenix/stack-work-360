package com.stackwork360.developerintelligenceservice.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public final class DeveloperAccessRequest {
    private final UUID id;
    private final String tenantId;
    private final UUID repositoryId;
    private final String developerId;
    private final DeveloperAccessLevel requestedLevel;
    private final String reason;
    private DeveloperAccessStatus status;
    private String decidedBy;
    private Instant decidedAt;
    private final Instant createdAt;
    private Instant updatedAt;

    private DeveloperAccessRequest(
            UUID id,
            String tenantId,
            UUID repositoryId,
            String developerId,
            DeveloperAccessLevel requestedLevel,
            String reason,
            DeveloperAccessStatus status,
            String decidedBy,
            Instant decidedAt,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = Objects.requireNonNull(id, "access request id is required");
        this.tenantId = requireText(tenantId, "tenant id is required");
        this.repositoryId = Objects.requireNonNull(repositoryId, "repository id is required");
        this.developerId = requireText(developerId, "developer id is required");
        this.requestedLevel = Objects.requireNonNull(requestedLevel, "requested level is required");
        this.reason = requireText(reason, "access reason is required");
        this.status = Objects.requireNonNull(status, "access request status is required");
        this.decidedBy = decidedBy;
        this.decidedAt = decidedAt;
        this.createdAt = Objects.requireNonNull(createdAt, "created at is required");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updated at is required");
    }

    public static DeveloperAccessRequest request(
            String tenantId,
            UUID repositoryId,
            String developerId,
            DeveloperAccessLevel requestedLevel,
            String reason
    ) {
        Instant now = Instant.now();
        return new DeveloperAccessRequest(
                UUID.randomUUID(),
                tenantId,
                repositoryId,
                developerId,
                requestedLevel,
                reason,
                DeveloperAccessStatus.REQUESTED,
                null,
                null,
                now,
                now
        );
    }

    public void approve(String actorId) {
        decide(actorId, DeveloperAccessStatus.APPROVED);
    }

    public void reject(String actorId) {
        decide(actorId, DeveloperAccessStatus.REJECTED);
    }

    public void revoke(String actorId) {
        if (status != DeveloperAccessStatus.APPROVED) {
            throw new IllegalStateException("only approved access can be revoked");
        }
        decide(actorId, DeveloperAccessStatus.REVOKED);
    }

    public UUID id() {
        return id;
    }

    public String tenantId() {
        return tenantId;
    }

    public UUID repositoryId() {
        return repositoryId;
    }

    public String developerId() {
        return developerId;
    }

    public DeveloperAccessLevel requestedLevel() {
        return requestedLevel;
    }

    public String reason() {
        return reason;
    }

    public DeveloperAccessStatus status() {
        return status;
    }

    public String decidedBy() {
        return decidedBy;
    }

    public Instant decidedAt() {
        return decidedAt;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

    private void decide(String actorId, DeveloperAccessStatus nextStatus) {
        if (status != DeveloperAccessStatus.REQUESTED && nextStatus != DeveloperAccessStatus.REVOKED) {
            throw new IllegalStateException("access request has already been decided");
        }
        decidedBy = requireText(actorId, "actor id is required");
        decidedAt = Instant.now();
        status = nextStatus;
        updatedAt = decidedAt;
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}

package com.stackwork360.integrationservice.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public final class ExternalProviderCheckpoint {
    private final String tenantId;
    private final UUID connectorId;
    private String cursor;
    private Instant syncedAt;

    private ExternalProviderCheckpoint(String tenantId, UUID connectorId, String cursor, Instant syncedAt) {
        this.tenantId = requireText(tenantId, "tenant id is required");
        this.connectorId = Objects.requireNonNull(connectorId, "connector id is required");
        this.cursor = requireText(cursor, "cursor is required");
        this.syncedAt = Objects.requireNonNull(syncedAt, "synced at is required");
    }

    public static ExternalProviderCheckpoint record(String tenantId, UUID connectorId, String cursor) {
        return new ExternalProviderCheckpoint(tenantId, connectorId, cursor, Instant.now());
    }

    public void advance(String cursor) {
        this.cursor = requireText(cursor, "cursor is required");
        this.syncedAt = Instant.now();
    }

    public String tenantId() {
        return tenantId;
    }

    public UUID connectorId() {
        return connectorId;
    }

    public String cursor() {
        return cursor;
    }

    public Instant syncedAt() {
        return syncedAt;
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}

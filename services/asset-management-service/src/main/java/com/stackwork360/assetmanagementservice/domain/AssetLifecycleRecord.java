package com.stackwork360.assetmanagementservice.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record AssetLifecycleRecord(
        UUID id,
        String tenantId,
        String assetTag,
        AssetLifecycleAction action,
        String actorId,
        String note,
        Instant occurredAt
) {
    public AssetLifecycleRecord {
        id = id == null ? UUID.randomUUID() : id;
        tenantId = requireText(tenantId, "tenant id is required");
        assetTag = requireText(assetTag, "asset tag is required");
        action = Objects.requireNonNull(action, "action is required");
        actorId = requireText(actorId, "actor id is required");
        note = requireText(note, "note is required");
        occurredAt = occurredAt == null ? Instant.now() : occurredAt;
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}

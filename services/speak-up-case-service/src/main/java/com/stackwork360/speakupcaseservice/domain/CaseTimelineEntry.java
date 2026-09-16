package com.stackwork360.speakupcaseservice.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record CaseTimelineEntry(
        UUID id,
        UUID caseId,
        String actorId,
        CaseTimelineAction action,
        String note,
        Instant occurredAt
) {
    public CaseTimelineEntry {
        id = id == null ? UUID.randomUUID() : id;
        caseId = Objects.requireNonNull(caseId, "case id is required");
        actorId = requireText(actorId, "actor id is required");
        action = Objects.requireNonNull(action, "timeline action is required");
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

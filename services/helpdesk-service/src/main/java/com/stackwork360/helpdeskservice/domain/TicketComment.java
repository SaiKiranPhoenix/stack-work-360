package com.stackwork360.helpdeskservice.domain;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public record TicketComment(
        UUID id,
        String authorId,
        String body,
        boolean internal,
        List<AttachmentMetadata> attachments,
        Instant createdAt
) {
    public TicketComment {
        id = id == null ? UUID.randomUUID() : id;
        authorId = requireText(authorId, "comment author is required");
        body = requireText(body, "comment body is required");
        attachments = List.copyOf(attachments == null ? List.of() : attachments);
        createdAt = createdAt == null ? Instant.now() : createdAt;
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}

package com.stackwork360.speakupcaseservice.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record EvidenceMetadata(
        UUID id,
        String fileName,
        String contentType,
        long sizeBytes,
        String storageReference,
        String uploadedBy,
        Instant uploadedAt
) {
    public EvidenceMetadata {
        id = id == null ? UUID.randomUUID() : id;
        fileName = requireText(fileName, "file name is required");
        contentType = requireText(contentType, "content type is required");
        if (sizeBytes <= 0) {
            throw new IllegalArgumentException("size must be positive");
        }
        storageReference = requireText(storageReference, "storage reference is required");
        uploadedBy = requireText(uploadedBy, "uploaded by is required");
        uploadedAt = uploadedAt == null ? Instant.now() : uploadedAt;
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}

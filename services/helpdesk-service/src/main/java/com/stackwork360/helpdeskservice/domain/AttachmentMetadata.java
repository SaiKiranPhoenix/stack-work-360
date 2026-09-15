package com.stackwork360.helpdeskservice.domain;

import java.util.Objects;
import java.util.UUID;

public record AttachmentMetadata(
        UUID id,
        String fileName,
        String objectKey,
        String contentType,
        long sizeBytes
) {
    public AttachmentMetadata {
        id = id == null ? UUID.randomUUID() : id;
        fileName = requireText(fileName, "file name is required");
        objectKey = requireText(objectKey, "object key is required");
        contentType = requireText(contentType, "content type is required");
        if (sizeBytes < 1) {
            throw new IllegalArgumentException("attachment size must be positive");
        }
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}

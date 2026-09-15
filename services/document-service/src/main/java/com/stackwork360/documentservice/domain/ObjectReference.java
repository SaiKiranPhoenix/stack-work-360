package com.stackwork360.documentservice.domain;

import java.util.Objects;

public record ObjectReference(
        String bucket,
        String objectKey,
        String checksumSha256,
        long sizeBytes,
        String contentType
) {
    public ObjectReference {
        bucket = requireText(bucket, "object bucket is required");
        objectKey = requireText(objectKey, "object key is required");
        checksumSha256 = requireText(checksumSha256, "checksum is required");
        contentType = requireText(contentType, "content type is required");
        if (sizeBytes < 1) {
            throw new IllegalArgumentException("object size must be positive");
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

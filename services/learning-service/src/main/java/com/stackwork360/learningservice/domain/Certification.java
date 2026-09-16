package com.stackwork360.learningservice.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public record Certification(
        UUID id,
        String tenantId,
        String name,
        String issuer,
        List<UUID> requiredResourceIds,
        int validityMonths,
        LocalDate availableFrom
) {
    public Certification {
        id = id == null ? UUID.randomUUID() : id;
        tenantId = requireText(tenantId, "tenant id is required");
        name = requireText(name, "certification name is required");
        issuer = requireText(issuer, "issuer is required");
        requiredResourceIds = List.copyOf(Objects.requireNonNull(requiredResourceIds, "required resources are required"));
        if (requiredResourceIds.isEmpty()) {
            throw new IllegalArgumentException("certification requires at least one resource");
        }
        if (validityMonths <= 0) {
            throw new IllegalArgumentException("validity months must be positive");
        }
        availableFrom = Objects.requireNonNull(availableFrom, "available from is required");
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}

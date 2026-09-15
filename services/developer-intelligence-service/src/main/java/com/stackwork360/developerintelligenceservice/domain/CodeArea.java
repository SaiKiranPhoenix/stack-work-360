package com.stackwork360.developerintelligenceservice.domain;

import java.util.Objects;

public record CodeArea(
        String pathPattern,
        String ownerGroup
) {
    public CodeArea {
        pathPattern = requireText(pathPattern, "path pattern is required");
        ownerGroup = requireText(ownerGroup, "owner group is required");
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}

package com.stackwork360.organizationservice.domain;

import java.util.Objects;

public record Location(
        String code,
        String name,
        String country,
        String timezone
) {
    public Location {
        code = requireText(code, "location code is required").toUpperCase();
        name = requireText(name, "location name is required");
        country = requireText(country, "location country is required");
        timezone = requireText(timezone, "location timezone is required");
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}

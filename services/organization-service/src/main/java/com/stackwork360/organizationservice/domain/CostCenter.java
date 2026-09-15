package com.stackwork360.organizationservice.domain;

import java.util.Objects;

public record CostCenter(
        String code,
        String name
) {
    public CostCenter {
        code = requireText(code, "cost center code is required").toUpperCase();
        name = requireText(name, "cost center name is required");
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}

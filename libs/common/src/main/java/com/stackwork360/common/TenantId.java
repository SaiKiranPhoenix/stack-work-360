package com.stackwork360.common;

import java.util.Objects;

public record TenantId(String value) {
    public TenantId {
        Objects.requireNonNull(value, "tenant id is required");
        if (value.isBlank()) {
            throw new IllegalArgumentException("tenant id must not be blank");
        }
    }
}

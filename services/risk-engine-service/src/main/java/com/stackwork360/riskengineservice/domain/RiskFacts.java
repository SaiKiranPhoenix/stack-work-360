package com.stackwork360.riskengineservice.domain;

import java.util.Map;
import java.util.Objects;

public record RiskFacts(
        String tenantId,
        RiskSource source,
        String subjectId,
        Map<String, String> values
) {
    public RiskFacts {
        tenantId = requireText(tenantId, "tenant id is required");
        source = Objects.requireNonNull(source, "source is required");
        subjectId = requireText(subjectId, "subject id is required");
        values = Map.copyOf(values == null ? Map.of() : values);
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}

package com.stackwork360.workforceplanningservice.domain;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public record WorkforceScenario(
        UUID id,
        String tenantId,
        String name,
        ScenarioType type,
        List<ScenarioAdjustment> adjustments
) {
    public WorkforceScenario {
        id = id == null ? UUID.randomUUID() : id;
        tenantId = requireText(tenantId, "tenant id is required");
        name = requireText(name, "scenario name is required");
        type = Objects.requireNonNull(type, "scenario type is required");
        adjustments = List.copyOf(Objects.requireNonNull(adjustments, "adjustments are required"));
        if (adjustments.isEmpty()) {
            throw new IllegalArgumentException("scenario requires at least one adjustment");
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

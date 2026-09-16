package com.stackwork360.accessgovernanceservice.domain;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public record AccessReviewCampaign(
        UUID id,
        String tenantId,
        String name,
        ResourceType resourceType,
        LocalDate dueOn
) {
    public AccessReviewCampaign {
        id = id == null ? UUID.randomUUID() : id;
        tenantId = requireText(tenantId, "tenant id is required");
        name = requireText(name, "campaign name is required");
        resourceType = Objects.requireNonNull(resourceType, "resource type is required");
        dueOn = Objects.requireNonNull(dueOn, "due date is required");
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}

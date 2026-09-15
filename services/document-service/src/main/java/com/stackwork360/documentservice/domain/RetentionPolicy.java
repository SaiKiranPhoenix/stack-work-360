package com.stackwork360.documentservice.domain;

import java.time.LocalDate;
import java.util.Objects;

public record RetentionPolicy(
        LocalDate retainUntil,
        String reason
) {
    public RetentionPolicy {
        retainUntil = Objects.requireNonNull(retainUntil, "retain until date is required");
        reason = reason == null ? "" : reason.trim();
    }

    public LocalDate deleteAfter() {
        return retainUntil.plusDays(1);
    }
}

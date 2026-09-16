package com.stackwork360.benefitsservice.api;

import com.stackwork360.benefitsservice.domain.OpenEnrollmentWindow;
import java.time.LocalDate;
import java.util.UUID;

public record OpenEnrollmentWindowResponse(
        UUID id,
        String tenantId,
        String name,
        LocalDate startsOn,
        LocalDate endsOn
) {
    static OpenEnrollmentWindowResponse from(OpenEnrollmentWindow window) {
        return new OpenEnrollmentWindowResponse(window.id(), window.tenantId(), window.name(), window.startsOn(), window.endsOn());
    }
}

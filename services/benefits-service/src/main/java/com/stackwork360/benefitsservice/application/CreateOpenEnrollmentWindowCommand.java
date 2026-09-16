package com.stackwork360.benefitsservice.application;

import java.time.LocalDate;

public record CreateOpenEnrollmentWindowCommand(
        String tenantId,
        String name,
        LocalDate startsOn,
        LocalDate endsOn
) {
}

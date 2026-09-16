package com.stackwork360.benefitsservice.api;

import com.stackwork360.benefitsservice.application.CreateOpenEnrollmentWindowCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record CreateOpenEnrollmentWindowRequest(
        @NotBlank String name,
        @NotNull LocalDate startsOn,
        @NotNull LocalDate endsOn
) {
    CreateOpenEnrollmentWindowCommand toCommand(String tenantId) {
        return new CreateOpenEnrollmentWindowCommand(tenantId, name, startsOn, endsOn);
    }
}

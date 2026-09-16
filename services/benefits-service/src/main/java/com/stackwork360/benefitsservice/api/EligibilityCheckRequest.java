package com.stackwork360.benefitsservice.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record EligibilityCheckRequest(
        @Valid @NotNull EmployeeProfileRequest profile,
        @NotBlank String planCode,
        @NotNull LocalDate asOf
) {
}

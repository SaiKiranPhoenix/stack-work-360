package com.stackwork360.learningservice.api;

import com.stackwork360.learningservice.application.CreateCertificationCommand;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record CreateCertificationRequest(
        @NotBlank String name,
        @NotBlank String issuer,
        @NotEmpty List<UUID> requiredResourceIds,
        @Min(1) int validityMonths,
        @NotNull LocalDate availableFrom
) {
    CreateCertificationCommand toCommand(String tenantId) {
        return new CreateCertificationCommand(tenantId, name, issuer, requiredResourceIds, validityMonths, availableFrom);
    }
}

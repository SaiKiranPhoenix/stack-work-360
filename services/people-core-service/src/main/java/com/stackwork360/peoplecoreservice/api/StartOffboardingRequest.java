package com.stackwork360.peoplecoreservice.api;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record StartOffboardingRequest(
        @NotNull
        LocalDate endDate
) {
}

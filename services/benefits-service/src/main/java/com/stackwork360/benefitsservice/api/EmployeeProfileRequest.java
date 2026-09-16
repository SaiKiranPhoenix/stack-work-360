package com.stackwork360.benefitsservice.api;

import com.stackwork360.benefitsservice.domain.EmployeeProfile;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record EmployeeProfileRequest(
        @NotBlank String workerId,
        @NotBlank String employmentType,
        @NotBlank String region,
        @NotNull LocalDate hireDate
) {
    EmployeeProfile toDomain() {
        return new EmployeeProfile(workerId, employmentType, region, hireDate);
    }
}

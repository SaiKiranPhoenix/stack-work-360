package com.stackwork360.peoplecoreservice.api;

import com.stackwork360.peoplecoreservice.domain.EmploymentType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record CreateWorkerRequest(
        @NotBlank
        String employeeNumber,

        @NotBlank
        String firstName,

        @NotBlank
        String lastName,

        @Email
        @NotBlank
        String workEmail,

        @NotNull
        EmploymentType employmentType,

        @NotBlank
        String jobTitle,

        String departmentId,

        String managerWorkerId,

        @NotNull
        LocalDate startDate
) {
}

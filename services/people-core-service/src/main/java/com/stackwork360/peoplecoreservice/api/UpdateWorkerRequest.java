package com.stackwork360.peoplecoreservice.api;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateWorkerRequest(
        @NotBlank
        String firstName,

        @NotBlank
        String lastName,

        @Email
        @NotBlank
        String workEmail,

        @NotBlank
        String jobTitle,

        String departmentId,

        String managerWorkerId
) {
}

package com.stackwork360.peoplecoreservice.application;

public record UpdateWorkerCommand(
        String firstName,
        String lastName,
        String workEmail,
        String jobTitle,
        String departmentId,
        String managerWorkerId
) {
}

package com.stackwork360.peoplecoreservice.domain;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public final class Worker {
    private final UUID id;
    private final String tenantId;
    private String employeeNumber;
    private String firstName;
    private String lastName;
    private String workEmail;
    private EmploymentType employmentType;
    private WorkerStatus status;
    private String jobTitle;
    private String departmentId;
    private String managerWorkerId;
    private LocalDate startDate;
    private LocalDate endDate;
    private final Instant createdAt;
    private Instant updatedAt;

    private Worker(
            UUID id,
            String tenantId,
            String employeeNumber,
            String firstName,
            String lastName,
            String workEmail,
            EmploymentType employmentType,
            WorkerStatus status,
            String jobTitle,
            String departmentId,
            String managerWorkerId,
            LocalDate startDate,
            LocalDate endDate,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = Objects.requireNonNull(id, "worker id is required");
        this.tenantId = requireText(tenantId, "tenant id is required");
        this.employeeNumber = requireText(employeeNumber, "employee number is required");
        this.firstName = requireText(firstName, "first name is required");
        this.lastName = requireText(lastName, "last name is required");
        this.workEmail = normalizeEmail(workEmail);
        this.employmentType = Objects.requireNonNull(employmentType, "employment type is required");
        this.status = Objects.requireNonNull(status, "worker status is required");
        this.jobTitle = requireText(jobTitle, "job title is required");
        this.departmentId = departmentId;
        this.managerWorkerId = managerWorkerId;
        this.startDate = Objects.requireNonNull(startDate, "start date is required");
        this.endDate = endDate;
        this.createdAt = Objects.requireNonNull(createdAt, "created at is required");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updated at is required");
        validateDates();
    }

    public static Worker hire(
            String tenantId,
            String employeeNumber,
            String firstName,
            String lastName,
            String workEmail,
            EmploymentType employmentType,
            String jobTitle,
            String departmentId,
            String managerWorkerId,
            LocalDate startDate
    ) {
        Instant now = Instant.now();
        return new Worker(
                UUID.randomUUID(),
                tenantId,
                employeeNumber,
                firstName,
                lastName,
                workEmail,
                employmentType,
                WorkerStatus.PREBOARDING,
                jobTitle,
                departmentId,
                managerWorkerId,
                startDate,
                null,
                now,
                now
        );
    }

    public void updateProfile(
            String firstName,
            String lastName,
            String workEmail,
            String jobTitle,
            String departmentId,
            String managerWorkerId
    ) {
        ensureNotTerminal();
        this.firstName = requireText(firstName, "first name is required");
        this.lastName = requireText(lastName, "last name is required");
        this.workEmail = normalizeEmail(workEmail);
        this.jobTitle = requireText(jobTitle, "job title is required");
        this.departmentId = departmentId;
        this.managerWorkerId = managerWorkerId;
        this.updatedAt = Instant.now();
    }

    public void markActive() {
        if (status != WorkerStatus.PREBOARDING && status != WorkerStatus.ONBOARDING) {
            throw new IllegalStateException("worker can only become active from preboarding or onboarding");
        }
        this.status = WorkerStatus.ACTIVE;
        this.updatedAt = Instant.now();
    }

    public void startOffboarding(LocalDate endDate) {
        if (status != WorkerStatus.ACTIVE) {
            throw new IllegalStateException("only active workers can start offboarding");
        }
        this.status = WorkerStatus.OFFBOARDING;
        this.endDate = Objects.requireNonNull(endDate, "end date is required");
        validateDates();
        this.updatedAt = Instant.now();
    }

    public void markAlumni() {
        if (status != WorkerStatus.OFFBOARDING) {
            throw new IllegalStateException("worker can only become alumni after offboarding");
        }
        this.status = WorkerStatus.ALUMNI;
        this.updatedAt = Instant.now();
    }

    public UUID id() {
        return id;
    }

    public String tenantId() {
        return tenantId;
    }

    public String employeeNumber() {
        return employeeNumber;
    }

    public String firstName() {
        return firstName;
    }

    public String lastName() {
        return lastName;
    }

    public String displayName() {
        return firstName + " " + lastName;
    }

    public String workEmail() {
        return workEmail;
    }

    public EmploymentType employmentType() {
        return employmentType;
    }

    public WorkerStatus status() {
        return status;
    }

    public String jobTitle() {
        return jobTitle;
    }

    public String departmentId() {
        return departmentId;
    }

    public String managerWorkerId() {
        return managerWorkerId;
    }

    public LocalDate startDate() {
        return startDate;
    }

    public LocalDate endDate() {
        return endDate;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

    private void ensureNotTerminal() {
        if (status == WorkerStatus.ALUMNI || status == WorkerStatus.TERMINATED) {
            throw new IllegalStateException("terminal worker profile cannot be changed");
        }
    }

    private void validateDates() {
        if (endDate != null && endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("end date cannot be before start date");
        }
    }

    private static String normalizeEmail(String value) {
        String email = requireText(value, "work email is required").toLowerCase();
        if (!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException("work email is invalid");
        }
        return email;
    }

    private static String requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}

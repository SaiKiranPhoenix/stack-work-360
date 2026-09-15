package com.stackwork360.payrollprepservice.domain;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public final class PayrollPeriod {
    private final UUID id;
    private final String tenantId;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private PayrollPeriodStatus status;
    private final List<PayrollInput> inputs;
    private final Instant createdAt;
    private Instant updatedAt;

    private PayrollPeriod(
            UUID id,
            String tenantId,
            LocalDate startDate,
            LocalDate endDate,
            PayrollPeriodStatus status,
            List<PayrollInput> inputs,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = Objects.requireNonNull(id, "payroll period id is required");
        this.tenantId = requireText(tenantId, "tenant id is required");
        this.startDate = Objects.requireNonNull(startDate, "start date is required");
        this.endDate = Objects.requireNonNull(endDate, "end date is required");
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("end date cannot be before start date");
        }
        this.status = Objects.requireNonNull(status, "payroll period status is required");
        this.inputs = new ArrayList<>(inputs == null ? List.of() : inputs);
        this.createdAt = Objects.requireNonNull(createdAt, "created at is required");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updated at is required");
    }

    public static PayrollPeriod open(String tenantId, LocalDate startDate, LocalDate endDate) {
        Instant now = Instant.now();
        return new PayrollPeriod(UUID.randomUUID(), tenantId, startDate, endDate, PayrollPeriodStatus.OPEN, List.of(), now, now);
    }

    public PayrollInput addInput(String workerId, Money basePay) {
        ensureOpen();
        if (findInput(workerId).isPresent()) {
            throw new IllegalArgumentException("payroll input already exists for worker");
        }
        PayrollInput input = PayrollInput.create(workerId, basePay);
        inputs.add(input);
        updatedAt = Instant.now();
        return input;
    }

    public PayrollInput addAdjustment(String workerId, PayrollAdjustment adjustment) {
        ensureOpen();
        PayrollInput input = findInput(workerId)
                .orElseThrow(() -> new IllegalArgumentException("payroll input not found for worker"));
        input.addAdjustment(adjustment);
        updatedAt = Instant.now();
        return input;
    }

    public void submitForApproval() {
        ensureOpen();
        if (inputs.isEmpty()) {
            throw new IllegalStateException("payroll period must contain inputs before approval");
        }
        status = PayrollPeriodStatus.PENDING_APPROVAL;
        updatedAt = Instant.now();
    }

    public void approve() {
        if (status != PayrollPeriodStatus.PENDING_APPROVAL) {
            throw new IllegalStateException("payroll period is not pending approval");
        }
        status = PayrollPeriodStatus.APPROVED;
        updatedAt = Instant.now();
    }

    public void close() {
        if (status != PayrollPeriodStatus.APPROVED) {
            throw new IllegalStateException("only approved payroll periods can be closed");
        }
        status = PayrollPeriodStatus.CLOSED;
        updatedAt = Instant.now();
    }

    public Money totalGrossPay() {
        if (inputs.isEmpty()) {
            return Money.of("0", "USD");
        }
        Money total = new Money(java.math.BigDecimal.ZERO, inputs.get(0).basePay().currency());
        for (PayrollInput input : inputs) {
            total = total.plus(input.grossPay());
        }
        return total;
    }

    public Optional<PayrollInput> findInput(String workerId) {
        return inputs.stream().filter(input -> input.workerId().equals(workerId)).findFirst();
    }

    public UUID id() {
        return id;
    }

    public String tenantId() {
        return tenantId;
    }

    public LocalDate startDate() {
        return startDate;
    }

    public LocalDate endDate() {
        return endDate;
    }

    public PayrollPeriodStatus status() {
        return status;
    }

    public List<PayrollInput> inputs() {
        return inputs.stream().sorted(Comparator.comparing(PayrollInput::workerId)).toList();
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

    private void ensureOpen() {
        if (status != PayrollPeriodStatus.OPEN) {
            throw new IllegalStateException("payroll period is not open");
        }
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}

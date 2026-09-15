package com.stackwork360.payrollprepservice.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class PayrollInput {
    private final UUID id;
    private final String workerId;
    private Money basePay;
    private final List<PayrollAdjustment> adjustments;

    private PayrollInput(UUID id, String workerId, Money basePay, List<PayrollAdjustment> adjustments) {
        this.id = Objects.requireNonNull(id, "payroll input id is required");
        this.workerId = requireText(workerId, "worker id is required");
        this.basePay = Objects.requireNonNull(basePay, "base pay is required");
        this.adjustments = new ArrayList<>(adjustments == null ? List.of() : adjustments);
    }

    public static PayrollInput create(String workerId, Money basePay) {
        return new PayrollInput(UUID.randomUUID(), workerId, basePay, List.of());
    }

    public void updateBasePay(Money basePay) {
        this.basePay = Objects.requireNonNull(basePay, "base pay is required");
    }

    public void addAdjustment(PayrollAdjustment adjustment) {
        adjustments.add(Objects.requireNonNull(adjustment, "adjustment is required"));
    }

    public Money grossPay() {
        Money total = basePay;
        for (PayrollAdjustment adjustment : adjustments) {
            total = total.plus(adjustment.signedAmount());
        }
        return total;
    }

    public UUID id() {
        return id;
    }

    public String workerId() {
        return workerId;
    }

    public Money basePay() {
        return basePay;
    }

    public List<PayrollAdjustment> adjustments() {
        return List.copyOf(adjustments);
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}

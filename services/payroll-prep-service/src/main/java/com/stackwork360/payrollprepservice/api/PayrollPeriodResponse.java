package com.stackwork360.payrollprepservice.api;

import com.stackwork360.payrollprepservice.domain.PayrollPeriod;
import com.stackwork360.payrollprepservice.domain.PayrollPeriodStatus;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record PayrollPeriodResponse(
        UUID id,
        String tenantId,
        LocalDate startDate,
        LocalDate endDate,
        PayrollPeriodStatus status,
        MoneyResponse totalGrossPay,
        List<PayrollInputResponse> inputs,
        Instant createdAt,
        Instant updatedAt
) {
    static PayrollPeriodResponse from(PayrollPeriod period) {
        return new PayrollPeriodResponse(
                period.id(),
                period.tenantId(),
                period.startDate(),
                period.endDate(),
                period.status(),
                MoneyResponse.from(period.totalGrossPay()),
                period.inputs().stream().map(PayrollInputResponse::from).toList(),
                period.createdAt(),
                period.updatedAt()
        );
    }
}

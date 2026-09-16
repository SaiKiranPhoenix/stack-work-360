package com.stackwork360.compensationservice.application;

import com.stackwork360.compensationservice.domain.CompensationChangeReason;
import com.stackwork360.compensationservice.domain.Money;
import java.time.LocalDate;

public record RequestCompensationChangeCommand(
        String tenantId,
        String workerId,
        String currentJobLevel,
        String proposedJobLevel,
        Money currentSalary,
        Money proposedSalary,
        LocalDate effectiveDate,
        CompensationChangeReason reason
) {
}

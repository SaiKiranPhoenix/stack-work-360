package com.stackwork360.compensationservice.application;

import com.stackwork360.compensationservice.domain.Money;
import java.time.LocalDate;

public record RecordCompensationCommand(String tenantId, String workerId, String jobLevel, String location, Money salary, LocalDate effectiveDate, String source) {
}

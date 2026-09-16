package com.stackwork360.compensationservice.application;

import com.stackwork360.compensationservice.domain.Money;

public record CreateSalaryBandCommand(String tenantId, String jobLevel, String location, Money minimum, Money midpoint, Money maximum) {
}

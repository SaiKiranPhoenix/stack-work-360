package com.stackwork360.compensationservice.application;

import com.stackwork360.compensationservice.domain.Money;

public record ImportBenchmarkCommand(String tenantId, String jobLevel, String location, Money marketMedian, String source) {
}

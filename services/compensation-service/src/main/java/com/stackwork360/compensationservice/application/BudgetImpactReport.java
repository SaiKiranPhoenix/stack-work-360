package com.stackwork360.compensationservice.application;

import com.stackwork360.compensationservice.domain.Money;
import java.util.List;

public record BudgetImpactReport(String tenantId, int approvedChangeCount, Money annualizedImpact, List<String> requestIds) {
}

package com.stackwork360.compensationservice.api;

import com.stackwork360.compensationservice.application.BudgetImpactReport;
import java.util.List;

public record BudgetImpactReportResponse(
        String tenantId,
        int approvedChangeCount,
        MoneyResponse annualizedImpact,
        List<String> requestIds
) {
    static BudgetImpactReportResponse from(BudgetImpactReport report) {
        return new BudgetImpactReportResponse(
                report.tenantId(),
                report.approvedChangeCount(),
                MoneyResponse.from(report.annualizedImpact()),
                report.requestIds()
        );
    }
}

package com.stackwork360.developerintelligenceservice.api;

import com.stackwork360.developerintelligenceservice.application.BusFactorReport;
import java.util.List;

public record BusFactorReportResponse(
        String tenantId,
        int repositoryCount,
        int riskyRepositoryCount,
        List<OwnershipMapEntryResponse> riskyRepositories
) {
    static BusFactorReportResponse from(BusFactorReport report) {
        return new BusFactorReportResponse(
                report.tenantId(),
                report.repositoryCount(),
                report.riskyRepositoryCount(),
                report.riskyRepositories().stream().map(OwnershipMapEntryResponse::from).toList()
        );
    }
}

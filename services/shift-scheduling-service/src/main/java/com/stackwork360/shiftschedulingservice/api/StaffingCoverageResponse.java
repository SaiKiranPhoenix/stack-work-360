package com.stackwork360.shiftschedulingservice.api;

import com.stackwork360.shiftschedulingservice.domain.StaffingCoverage;
import java.time.LocalDate;
import java.util.UUID;

public record StaffingCoverageResponse(
        UUID templateId,
        LocalDate shiftDate,
        int requiredStaff,
        int assignedStaff,
        boolean understaffed
) {
    static StaffingCoverageResponse from(StaffingCoverage coverage) {
        return new StaffingCoverageResponse(
                coverage.templateId(),
                coverage.shiftDate(),
                coverage.requiredStaff(),
                coverage.assignedStaff(),
                coverage.understaffed()
        );
    }
}

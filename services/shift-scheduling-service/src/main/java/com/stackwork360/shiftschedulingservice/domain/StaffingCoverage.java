package com.stackwork360.shiftschedulingservice.domain;

import java.time.LocalDate;
import java.util.UUID;

public record StaffingCoverage(
        UUID templateId,
        LocalDate shiftDate,
        int requiredStaff,
        int assignedStaff
) {
    public boolean understaffed() {
        return assignedStaff < requiredStaff;
    }
}

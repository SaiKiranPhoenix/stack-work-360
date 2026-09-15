package com.stackwork360.leaveservice.api;

import com.stackwork360.leaveservice.application.AvailabilityDay;
import java.time.LocalDate;
import java.util.List;

public record AvailabilityDayResponse(
        LocalDate date,
        List<String> unavailableWorkerIds
) {
    public static AvailabilityDayResponse from(AvailabilityDay day) {
        return new AvailabilityDayResponse(day.date(), day.unavailableWorkerIds());
    }
}

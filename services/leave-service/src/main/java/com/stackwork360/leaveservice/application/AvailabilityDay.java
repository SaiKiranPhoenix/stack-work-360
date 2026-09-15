package com.stackwork360.leaveservice.application;

import java.time.LocalDate;
import java.util.List;

public record AvailabilityDay(
        LocalDate date,
        List<String> unavailableWorkerIds
) {
}

package com.stackwork360.performanceservice.application;

import java.time.LocalDate;

public record CreateReviewCycleCommand(String tenantId, String name, LocalDate startsOn, LocalDate endsOn) {
}

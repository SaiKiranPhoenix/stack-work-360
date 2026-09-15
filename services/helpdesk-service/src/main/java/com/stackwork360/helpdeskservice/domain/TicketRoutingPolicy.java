package com.stackwork360.helpdeskservice.domain;

import java.time.Duration;

public final class TicketRoutingPolicy {
    private TicketRoutingPolicy() {
    }

    public static String assigneeGroupFor(TicketCategory category) {
        return switch (category) {
            case PAYROLL -> "PAYROLL_OPS";
            case BENEFITS -> "BENEFITS_OPS";
            case ACCESS -> "SECURITY_ADMIN";
            case ASSET -> "IT_ADMIN";
            case POLICY, HR -> "HR_OPS";
            case GENERAL -> "EMPLOYEE_SUPPORT";
        };
    }

    public static Duration slaFor(TicketPriority priority) {
        return switch (priority) {
            case URGENT -> Duration.ofHours(4);
            case HIGH -> Duration.ofHours(12);
            case NORMAL -> Duration.ofHours(48);
            case LOW -> Duration.ofHours(96);
        };
    }
}

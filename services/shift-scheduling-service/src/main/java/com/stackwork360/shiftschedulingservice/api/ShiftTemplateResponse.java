package com.stackwork360.shiftschedulingservice.api;

import com.stackwork360.shiftschedulingservice.domain.ShiftTemplate;
import java.time.LocalTime;
import java.util.UUID;

public record ShiftTemplateResponse(
        UUID id,
        String tenantId,
        String name,
        LocalTime startTime,
        LocalTime endTime,
        long durationMinutes,
        int requiredStaff
) {
    static ShiftTemplateResponse from(ShiftTemplate template) {
        return new ShiftTemplateResponse(
                template.id(),
                template.tenantId(),
                template.name(),
                template.startTime(),
                template.endTime(),
                template.duration().toMinutes(),
                template.requiredStaff()
        );
    }
}

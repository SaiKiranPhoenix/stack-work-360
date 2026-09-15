package com.stackwork360.shiftschedulingservice.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalTime;
import org.junit.jupiter.api.Test;

class ShiftTemplateTest {
    @Test
    void calculatesOvernightDuration() {
        ShiftTemplate template = new ShiftTemplate(
                null,
                "tenant-1",
                "Night",
                LocalTime.of(22, 0),
                LocalTime.of(6, 0),
                2
        );

        assertEquals(480, template.duration().toMinutes());
    }
}

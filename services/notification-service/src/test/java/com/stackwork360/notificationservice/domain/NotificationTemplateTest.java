package com.stackwork360.notificationservice.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import org.junit.jupiter.api.Test;

class NotificationTemplateTest {
    @Test
    void rendersSubjectAndBodyVariables() {
        NotificationTemplate template = NotificationTemplate.create(
                "tenant-1",
                "leave-approved",
                "Leave approved for {{workerName}}",
                "{{managerName}} approved {{days}} days",
                NotificationPriority.NORMAL
        );

        RenderedNotification rendered = template.render(Map.of(
                "workerName", "Sai",
                "managerName", "Priya",
                "days", "2"
        ));

        assertEquals("Leave approved for Sai", rendered.subject());
        assertEquals("Priya approved 2 days", rendered.body());
    }
}

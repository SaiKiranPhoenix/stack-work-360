package com.stackwork360.notificationservice.application;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.stackwork360.notificationservice.domain.DeliveryStatus;
import com.stackwork360.notificationservice.domain.NotificationChannel;
import com.stackwork360.notificationservice.domain.NotificationPriority;
import com.stackwork360.notificationservice.infrastructure.InMemoryInAppNotificationRepository;
import com.stackwork360.notificationservice.infrastructure.InMemoryNotificationMessageRepository;
import com.stackwork360.notificationservice.infrastructure.InMemoryNotificationPreferenceRepository;
import com.stackwork360.notificationservice.infrastructure.InMemoryNotificationTemplateRepository;
import com.stackwork360.notificationservice.infrastructure.RecordingEmailDeliveryAdapter;
import com.stackwork360.notificationservice.infrastructure.RecordingSlackDeliveryAdapter;
import com.stackwork360.notificationservice.infrastructure.RecordingTeamsDeliveryAdapter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

class NotificationApplicationServiceTest {
    private final NotificationApplicationService service = new NotificationApplicationService(
            new InMemoryNotificationPreferenceRepository(),
            new InMemoryNotificationTemplateRepository(),
            new InMemoryNotificationMessageRepository(),
            new InMemoryInAppNotificationRepository(),
            List.of(new RecordingEmailDeliveryAdapter(), new RecordingSlackDeliveryAdapter(), new RecordingTeamsDeliveryAdapter())
    );

    @Test
    void sendsThroughAllowedExternalAndInAppChannels() {
        service.configurePreference(new ConfigurePreferenceCommand(
                "tenant-1",
                "worker-1",
                Set.of(NotificationChannel.EMAIL, NotificationChannel.IN_APP),
                false,
                "Asia/Kolkata"
        ));
        service.createTemplate(new CreateTemplateCommand(
                "tenant-1",
                "risk-alert",
                "Risk alert for {{subject}}",
                "{{subject}} needs review",
                NotificationPriority.HIGH
        ));

        SendNotificationResult result = service.send(new SendNotificationCommand(
                "tenant-1",
                "worker-1",
                "risk-alert",
                Map.of("subject", "repo-1"),
                Set.of(NotificationChannel.EMAIL, NotificationChannel.IN_APP)
        ));

        assertEquals(1, result.messages().size());
        assertEquals(DeliveryStatus.DELIVERED, result.messages().get(0).deliveryStatus());
        assertEquals(1, result.inAppNotifications().size());
        assertEquals("Risk alert for repo-1", result.inAppNotifications().get(0).subject());
    }

    @Test
    void filtersRequestedChannelsByPreference() {
        service.configurePreference(new ConfigurePreferenceCommand(
                "tenant-1",
                "worker-1",
                Set.of(NotificationChannel.IN_APP),
                false,
                "UTC"
        ));
        service.createTemplate(new CreateTemplateCommand(
                "tenant-1",
                "ticket-update",
                "Ticket {{ticketId}}",
                "Ticket updated",
                NotificationPriority.NORMAL
        ));

        SendNotificationResult result = service.send(new SendNotificationCommand(
                "tenant-1",
                "worker-1",
                "ticket-update",
                Map.of("ticketId", "T-1"),
                Set.of(NotificationChannel.EMAIL, NotificationChannel.IN_APP)
        ));

        assertEquals(0, result.messages().size());
        assertEquals(1, result.inAppNotifications().size());
    }

    @Test
    void marksInAppNotificationRead() {
        service.createTemplate(new CreateTemplateCommand(
                "tenant-1",
                "hello",
                "Hello {{name}}",
                "Welcome",
                NotificationPriority.NORMAL
        ));
        SendNotificationResult result = service.send(new SendNotificationCommand(
                "tenant-1",
                "worker-1",
                "hello",
                Map.of("name", "Sai"),
                Set.of(NotificationChannel.IN_APP)
        ));

        var read = service.markRead(result.inAppNotifications().get(0).id());

        assertEquals(com.stackwork360.notificationservice.domain.NotificationStatus.READ, read.status());
    }
}

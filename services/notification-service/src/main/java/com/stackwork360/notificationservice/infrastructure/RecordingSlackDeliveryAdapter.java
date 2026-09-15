package com.stackwork360.notificationservice.infrastructure;

import com.stackwork360.notificationservice.application.DeliveryResult;
import com.stackwork360.notificationservice.application.NotificationDeliveryAdapter;
import com.stackwork360.notificationservice.domain.NotificationChannel;
import com.stackwork360.notificationservice.domain.NotificationMessage;
import org.springframework.stereotype.Component;

@Component
public class RecordingSlackDeliveryAdapter implements NotificationDeliveryAdapter {
    @Override
    public NotificationChannel channel() {
        return NotificationChannel.SLACK;
    }

    @Override
    public DeliveryResult deliver(NotificationMessage message) {
        return DeliveryResult.delivered("slack-" + message.id());
    }
}

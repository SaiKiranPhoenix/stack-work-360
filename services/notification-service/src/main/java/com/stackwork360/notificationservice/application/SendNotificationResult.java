package com.stackwork360.notificationservice.application;

import com.stackwork360.notificationservice.domain.InAppNotification;
import com.stackwork360.notificationservice.domain.NotificationMessage;
import java.util.List;

public record SendNotificationResult(
        List<NotificationMessage> messages,
        List<InAppNotification> inAppNotifications
) {
}

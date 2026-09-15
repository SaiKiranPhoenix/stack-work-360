package com.stackwork360.notificationservice.api;

import com.stackwork360.notificationservice.application.SendNotificationResult;
import java.util.List;

public record SendNotificationResponse(
        List<NotificationMessageResponse> messages,
        List<InAppNotificationResponse> inAppNotifications
) {
    static SendNotificationResponse from(SendNotificationResult result) {
        return new SendNotificationResponse(
                result.messages().stream().map(NotificationMessageResponse::from).toList(),
                result.inAppNotifications().stream().map(InAppNotificationResponse::from).toList()
        );
    }
}

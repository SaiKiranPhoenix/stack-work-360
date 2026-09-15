package com.stackwork360.notificationservice.application;

import com.stackwork360.notificationservice.domain.NotificationChannel;
import com.stackwork360.notificationservice.domain.NotificationMessage;

public interface NotificationDeliveryAdapter {
    NotificationChannel channel();

    DeliveryResult deliver(NotificationMessage message);
}

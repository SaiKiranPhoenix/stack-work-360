package com.stackwork360.notificationservice.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class InAppNotificationTest {
    @Test
    void marksUnreadNotificationAsRead() {
        InAppNotification notification = notification();

        notification.markRead();

        assertEquals(NotificationStatus.READ, notification.status());
    }

    @Test
    void rejectsReadAfterArchive() {
        InAppNotification notification = notification();
        notification.archive();

        assertThrows(IllegalStateException.class, notification::markRead);
    }

    private static InAppNotification notification() {
        return InAppNotification.unread(
                "tenant-1",
                "worker-1",
                "Policy update",
                "Please review the new policy",
                NotificationPriority.NORMAL
        );
    }
}

package com.stackwork360.notificationservice.application;

import com.stackwork360.notificationservice.domain.InAppNotification;
import com.stackwork360.notificationservice.domain.InAppNotificationRepository;
import com.stackwork360.notificationservice.domain.NotificationChannel;
import com.stackwork360.notificationservice.domain.NotificationMessage;
import com.stackwork360.notificationservice.domain.NotificationMessageRepository;
import com.stackwork360.notificationservice.domain.NotificationPreference;
import com.stackwork360.notificationservice.domain.NotificationPreferenceRepository;
import com.stackwork360.notificationservice.domain.NotificationTemplate;
import com.stackwork360.notificationservice.domain.NotificationTemplateRepository;
import com.stackwork360.notificationservice.domain.RenderedNotification;
import com.stackwork360.web.ResourceNotFoundException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class NotificationApplicationService {
    private final NotificationPreferenceRepository preferenceRepository;
    private final NotificationTemplateRepository templateRepository;
    private final NotificationMessageRepository messageRepository;
    private final InAppNotificationRepository inAppNotificationRepository;
    private final Map<NotificationChannel, NotificationDeliveryAdapter> deliveryAdapters;

    public NotificationApplicationService(
            NotificationPreferenceRepository preferenceRepository,
            NotificationTemplateRepository templateRepository,
            NotificationMessageRepository messageRepository,
            InAppNotificationRepository inAppNotificationRepository,
            List<NotificationDeliveryAdapter> deliveryAdapters
    ) {
        this.preferenceRepository = preferenceRepository;
        this.templateRepository = templateRepository;
        this.messageRepository = messageRepository;
        this.inAppNotificationRepository = inAppNotificationRepository;
        this.deliveryAdapters = deliveryAdapters.stream()
                .collect(Collectors.toMap(NotificationDeliveryAdapter::channel, Function.identity()));
    }

    public NotificationPreference configurePreference(ConfigurePreferenceCommand command) {
        NotificationPreference preference = preferenceRepository.find(command.tenantId(), command.workerId())
                .orElseGet(() -> NotificationPreference.defaults(command.tenantId(), command.workerId()));
        preference.update(command.enabledChannels(), command.quietHoursEnabled(), command.timezone());
        return preferenceRepository.save(preference);
    }

    public NotificationPreference preference(String tenantId, String workerId) {
        return preferenceRepository.find(tenantId, workerId)
                .orElseGet(() -> preferenceRepository.save(NotificationPreference.defaults(tenantId, workerId)));
    }

    public NotificationTemplate createTemplate(CreateTemplateCommand command) {
        NotificationTemplate existing = templateRepository.findByTenantIdAndKey(command.tenantId(), command.key())
                .orElse(null);
        if (existing != null) {
            existing.update(command.subject(), command.body(), command.defaultPriority());
            return templateRepository.save(existing);
        }
        return templateRepository.save(NotificationTemplate.create(
                command.tenantId(),
                command.key(),
                command.subject(),
                command.body(),
                command.defaultPriority()
        ));
    }

    public List<NotificationTemplate> templates(String tenantId) {
        return templateRepository.findByTenantId(tenantId);
    }

    public SendNotificationResult send(SendNotificationCommand command) {
        NotificationPreference preference = preference(command.tenantId(), command.recipientId());
        NotificationTemplate template = templateRepository.findByTenantIdAndKey(command.tenantId(), command.templateKey())
                .orElseThrow(() -> new ResourceNotFoundException("notification template not found"));
        RenderedNotification rendered = template.render(command.variables());
        EnumSet<NotificationChannel> channels = effectiveChannels(command.requestedChannels(), preference);

        List<NotificationMessage> messages = new ArrayList<>();
        List<InAppNotification> inAppNotifications = new ArrayList<>();
        for (NotificationChannel channel : channels) {
            if (channel == NotificationChannel.IN_APP) {
                InAppNotification notification = InAppNotification.unread(
                        command.tenantId(),
                        command.recipientId(),
                        rendered.subject(),
                        rendered.body(),
                        rendered.priority()
                );
                inAppNotifications.add(inAppNotificationRepository.save(notification));
                continue;
            }

            NotificationMessage message = NotificationMessage.accepted(
                    command.tenantId(),
                    command.recipientId(),
                    channel,
                    rendered.subject(),
                    rendered.body(),
                    rendered.priority()
            );
            NotificationDeliveryAdapter adapter = deliveryAdapters.get(channel);
            if (adapter == null) {
                message.markFailed("delivery adapter not configured");
            } else {
                DeliveryResult result = adapter.deliver(message);
                if (result.delivered()) {
                    message.markDelivered(result.providerReference());
                } else {
                    message.markFailed(result.failureReason());
                }
            }
            messages.add(messageRepository.save(message));
        }
        return new SendNotificationResult(messages, inAppNotifications);
    }

    public List<InAppNotification> inbox(String tenantId, String recipientId) {
        return inAppNotificationRepository.findByTenantIdAndRecipientId(tenantId, recipientId);
    }

    public InAppNotification markRead(UUID notificationId) {
        InAppNotification notification = getInAppNotification(notificationId);
        notification.markRead();
        return inAppNotificationRepository.save(notification);
    }

    public InAppNotification archive(UUID notificationId) {
        InAppNotification notification = getInAppNotification(notificationId);
        notification.archive();
        return inAppNotificationRepository.save(notification);
    }

    public List<NotificationMessage> deliveries(String tenantId) {
        return messageRepository.findByTenantId(tenantId);
    }

    private InAppNotification getInAppNotification(UUID notificationId) {
        return inAppNotificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("in-app notification not found"));
    }

    private EnumSet<NotificationChannel> effectiveChannels(
            java.util.Set<NotificationChannel> requestedChannels,
            NotificationPreference preference
    ) {
        EnumSet<NotificationChannel> requested = requestedChannels == null || requestedChannels.isEmpty()
                ? EnumSet.copyOf(preference.enabledChannels())
                : EnumSet.copyOf(requestedChannels);
        requested.removeIf(channel -> !preference.allows(channel));
        if (requested.isEmpty()) {
            requested.add(NotificationChannel.IN_APP);
        }
        return requested;
    }
}

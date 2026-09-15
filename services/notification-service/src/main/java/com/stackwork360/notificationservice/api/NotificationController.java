package com.stackwork360.notificationservice.api;

import com.stackwork360.common.CorrelationIds;
import com.stackwork360.notificationservice.application.NotificationApplicationService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications/v1")
public class NotificationController {
    private final NotificationApplicationService notificationApplicationService;

    public NotificationController(NotificationApplicationService notificationApplicationService) {
        this.notificationApplicationService = notificationApplicationService;
    }

    @PutMapping("/workers/{workerId}/preferences")
    public NotificationPreferenceResponse configurePreference(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @PathVariable String workerId,
            @Valid @RequestBody ConfigurePreferenceRequest request
    ) {
        return NotificationPreferenceResponse.from(notificationApplicationService.configurePreference(request.toCommand(tenantId, workerId)));
    }

    @GetMapping("/workers/{workerId}/preferences")
    public NotificationPreferenceResponse preference(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @PathVariable String workerId
    ) {
        return NotificationPreferenceResponse.from(notificationApplicationService.preference(tenantId, workerId));
    }

    @PostMapping("/templates")
    public ResponseEntity<NotificationTemplateResponse> createTemplate(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @Valid @RequestBody CreateTemplateRequest request
    ) {
        NotificationTemplateResponse response = NotificationTemplateResponse.from(notificationApplicationService.createTemplate(request.toCommand(tenantId)));
        return ResponseEntity.created(URI.create("/api/notifications/v1/templates/" + response.id()))
                .body(response);
    }

    @GetMapping("/templates")
    public List<NotificationTemplateResponse> templates(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId) {
        return notificationApplicationService.templates(tenantId).stream()
                .map(NotificationTemplateResponse::from)
                .toList();
    }

    @PostMapping("/send")
    public SendNotificationResponse send(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @Valid @RequestBody SendNotificationRequest request
    ) {
        return SendNotificationResponse.from(notificationApplicationService.send(request.toCommand(tenantId)));
    }

    @GetMapping("/workers/{workerId}/inbox")
    public List<InAppNotificationResponse> inbox(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @PathVariable String workerId
    ) {
        return notificationApplicationService.inbox(tenantId, workerId).stream()
                .map(InAppNotificationResponse::from)
                .toList();
    }

    @PatchMapping("/inbox/{notificationId}/read")
    public InAppNotificationResponse markRead(@PathVariable UUID notificationId) {
        return InAppNotificationResponse.from(notificationApplicationService.markRead(notificationId));
    }

    @PatchMapping("/inbox/{notificationId}/archive")
    public InAppNotificationResponse archive(@PathVariable UUID notificationId) {
        return InAppNotificationResponse.from(notificationApplicationService.archive(notificationId));
    }

    @GetMapping("/deliveries")
    public List<NotificationMessageResponse> deliveries(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId) {
        return notificationApplicationService.deliveries(tenantId).stream()
                .map(NotificationMessageResponse::from)
                .toList();
    }
}

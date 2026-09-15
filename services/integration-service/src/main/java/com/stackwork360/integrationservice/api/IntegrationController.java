package com.stackwork360.integrationservice.api;

import com.stackwork360.common.CorrelationIds;
import com.stackwork360.integrationservice.application.IntegrationApplicationService;
import com.stackwork360.integrationservice.domain.ConnectorCategory;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/integrations/v1")
public class IntegrationController {
    private final IntegrationApplicationService integrationApplicationService;

    public IntegrationController(IntegrationApplicationService integrationApplicationService) {
        this.integrationApplicationService = integrationApplicationService;
    }

    @PostMapping("/connectors")
    public ResponseEntity<ConnectorResponse> create(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @Valid @RequestBody CreateConnectorRequest request
    ) {
        ConnectorResponse response = ConnectorResponse.from(integrationApplicationService.create(request.toCommand(tenantId)));
        return ResponseEntity.created(URI.create("/api/integrations/v1/connectors/" + response.id()))
                .body(response);
    }

    @PutMapping("/connectors/{connectorId}")
    public ConnectorResponse update(
            @PathVariable UUID connectorId,
            @Valid @RequestBody UpdateConnectorRequest request
    ) {
        return ConnectorResponse.from(integrationApplicationService.update(connectorId, request.toCommand()));
    }

    @GetMapping("/connectors")
    public List<ConnectorResponse> list(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @RequestParam(required = false) ConnectorCategory category
    ) {
        return integrationApplicationService.list(tenantId, category).stream()
                .map(ConnectorResponse::from)
                .toList();
    }

    @GetMapping("/connectors/{connectorId}")
    public ConnectorResponse get(@PathVariable UUID connectorId) {
        return ConnectorResponse.from(integrationApplicationService.get(connectorId));
    }

    @PostMapping("/connectors/{connectorId}/validate-webhook")
    public WebhookValidationResponse validateWebhook(
            @PathVariable UUID connectorId,
            @Valid @RequestBody ValidateWebhookRequest request
    ) {
        return WebhookValidationResponse.from(integrationApplicationService.validateWebhook(request.toCommand(connectorId)));
    }

    @PutMapping("/connectors/{connectorId}/checkpoint")
    public CheckpointResponse recordCheckpoint(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @PathVariable UUID connectorId,
            @Valid @RequestBody RecordCheckpointRequest request
    ) {
        return CheckpointResponse.from(integrationApplicationService.recordCheckpoint(request.toCommand(tenantId, connectorId)));
    }

    @GetMapping("/connectors/{connectorId}/checkpoint")
    public CheckpointResponse checkpoint(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @PathVariable UUID connectorId
    ) {
        return CheckpointResponse.from(integrationApplicationService.checkpoint(tenantId, connectorId));
    }
}

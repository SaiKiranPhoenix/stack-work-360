package com.stackwork360.integrationservice.application;

import com.stackwork360.integrationservice.domain.ConnectorCategory;
import com.stackwork360.integrationservice.domain.ConnectorConfiguration;
import com.stackwork360.integrationservice.domain.ConnectorConfigurationRepository;
import com.stackwork360.integrationservice.domain.ExternalProviderCheckpoint;
import com.stackwork360.integrationservice.domain.ExternalProviderCheckpointRepository;
import com.stackwork360.integrationservice.domain.WebhookValidationResult;
import com.stackwork360.web.ResourceNotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class IntegrationApplicationService {
    private final ConnectorConfigurationRepository connectorRepository;
    private final ExternalProviderCheckpointRepository checkpointRepository;
    private final WebhookSignatureValidator signatureValidator;

    public IntegrationApplicationService(
            ConnectorConfigurationRepository connectorRepository,
            ExternalProviderCheckpointRepository checkpointRepository,
            WebhookSignatureValidator signatureValidator
    ) {
        this.connectorRepository = connectorRepository;
        this.checkpointRepository = checkpointRepository;
        this.signatureValidator = signatureValidator;
    }

    public ConnectorConfiguration create(CreateConnectorCommand command) {
        return connectorRepository.save(ConnectorConfiguration.create(
                command.tenantId(),
                command.category(),
                command.provider(),
                command.displayName(),
                command.settings(),
                command.webhookSecret()
        ));
    }

    public ConnectorConfiguration update(UUID connectorId, UpdateConnectorCommand command) {
        ConnectorConfiguration connector = get(connectorId);
        connector.update(command.displayName(), command.status(), command.settings(), command.webhookSecret());
        return connectorRepository.save(connector);
    }

    public ConnectorConfiguration get(UUID connectorId) {
        return connectorRepository.findById(connectorId)
                .orElseThrow(() -> new ResourceNotFoundException("connector not found"));
    }

    public List<ConnectorConfiguration> list(String tenantId) {
        return connectorRepository.findByTenantId(tenantId);
    }

    public List<ConnectorConfiguration> list(String tenantId, ConnectorCategory category) {
        return category == null
                ? connectorRepository.findByTenantId(tenantId)
                : connectorRepository.findByTenantIdAndCategory(tenantId, category);
    }

    public WebhookValidationResult validateWebhook(ValidateWebhookCommand command) {
        ConnectorConfiguration connector = get(command.connectorId());
        if (!connector.active()) {
            return WebhookValidationResult.invalid("connector is not active");
        }
        return signatureValidator.validate(command.payload(), connector.webhookSecret(), command.signature());
    }

    public ExternalProviderCheckpoint recordCheckpoint(RecordCheckpointCommand command) {
        get(command.connectorId());
        ExternalProviderCheckpoint checkpoint = checkpointRepository.find(command.tenantId(), command.connectorId())
                .orElseGet(() -> ExternalProviderCheckpoint.record(command.tenantId(), command.connectorId(), command.cursor()));
        checkpoint.advance(command.cursor());
        return checkpointRepository.save(checkpoint);
    }

    public ExternalProviderCheckpoint checkpoint(String tenantId, UUID connectorId) {
        return checkpointRepository.find(tenantId, connectorId)
                .orElseThrow(() -> new ResourceNotFoundException("checkpoint not found"));
    }
}

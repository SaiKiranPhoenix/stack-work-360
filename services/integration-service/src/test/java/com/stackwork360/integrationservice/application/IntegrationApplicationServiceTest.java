package com.stackwork360.integrationservice.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.stackwork360.integrationservice.domain.ConnectorCategory;
import com.stackwork360.integrationservice.domain.ConnectorConfiguration;
import com.stackwork360.integrationservice.domain.ConnectorProvider;
import com.stackwork360.integrationservice.infrastructure.InMemoryConnectorConfigurationRepository;
import com.stackwork360.integrationservice.infrastructure.InMemoryExternalProviderCheckpointRepository;
import java.util.Map;
import org.junit.jupiter.api.Test;

class IntegrationApplicationServiceTest {
    private final WebhookSignatureValidator validator = new WebhookSignatureValidator();
    private final IntegrationApplicationService service = new IntegrationApplicationService(
            new InMemoryConnectorConfigurationRepository(),
            new InMemoryExternalProviderCheckpointRepository(),
            validator
    );

    @Test
    void validatesWebhookForActiveConnector() {
        ConnectorConfiguration connector = service.create(new CreateConnectorCommand(
                "tenant-1",
                ConnectorCategory.GIT_PROVIDER,
                ConnectorProvider.GITHUB,
                "GitHub",
                Map.of("org", "stack-work"),
                "secret"
        ));
        String payload = "{\"event\":\"push\"}";
        String signature = validator.signature(payload, "secret");

        assertTrue(service.validateWebhook(new ValidateWebhookCommand(connector.id(), payload, signature)).valid());
    }

    @Test
    void recordsAndAdvancesCheckpoint() {
        ConnectorConfiguration connector = service.create(new CreateConnectorCommand(
                "tenant-1",
                ConnectorCategory.IDENTITY_PROVIDER,
                ConnectorProvider.OKTA,
                "Okta",
                Map.of("domain", "example.okta.com"),
                "secret"
        ));

        service.recordCheckpoint(new RecordCheckpointCommand("tenant-1", connector.id(), "cursor-1"));
        var checkpoint = service.recordCheckpoint(new RecordCheckpointCommand("tenant-1", connector.id(), "cursor-2"));

        assertEquals("cursor-2", checkpoint.cursor());
    }
}

package com.stackwork360.integrationservice.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import org.junit.jupiter.api.Test;

class ConnectorConfigurationTest {
    @Test
    void createsMatchingProviderCategoryPair() {
        ConnectorConfiguration connector = ConnectorConfiguration.create(
                "tenant-1",
                ConnectorCategory.GIT_PROVIDER,
                ConnectorProvider.GITHUB,
                "GitHub",
                Map.of("org", "stack-work"),
                "secret"
        );

        assertTrue(connector.active());
        assertEquals(ConnectorProvider.GITHUB, connector.provider());
    }

    @Test
    void rejectsMismatchedProviderCategoryPair() {
        assertThrows(IllegalArgumentException.class, () -> ConnectorConfiguration.create(
                "tenant-1",
                ConnectorCategory.IDENTITY_PROVIDER,
                ConnectorProvider.GITHUB,
                "Bad connector",
                Map.of(),
                "secret"
        ));
    }
}

package com.stackwork360.integrationservice.domain;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public final class ConnectorConfiguration {
    private final UUID id;
    private final String tenantId;
    private final ConnectorCategory category;
    private final ConnectorProvider provider;
    private String displayName;
    private ConnectorStatus status;
    private Map<String, String> settings;
    private String webhookSecret;
    private final Instant createdAt;
    private Instant updatedAt;

    private ConnectorConfiguration(
            UUID id,
            String tenantId,
            ConnectorCategory category,
            ConnectorProvider provider,
            String displayName,
            ConnectorStatus status,
            Map<String, String> settings,
            String webhookSecret,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = Objects.requireNonNull(id, "connector id is required");
        this.tenantId = requireText(tenantId, "tenant id is required");
        this.category = Objects.requireNonNull(category, "connector category is required");
        this.provider = Objects.requireNonNull(provider, "connector provider is required");
        ensureProviderMatchesCategory(category, provider);
        this.displayName = requireText(displayName, "display name is required");
        this.status = Objects.requireNonNull(status, "connector status is required");
        this.settings = Map.copyOf(settings == null ? Map.of() : settings);
        this.webhookSecret = requireText(webhookSecret, "webhook secret is required");
        this.createdAt = Objects.requireNonNull(createdAt, "created at is required");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updated at is required");
    }

    public static ConnectorConfiguration create(
            String tenantId,
            ConnectorCategory category,
            ConnectorProvider provider,
            String displayName,
            Map<String, String> settings,
            String webhookSecret
    ) {
        Instant now = Instant.now();
        return new ConnectorConfiguration(UUID.randomUUID(), tenantId, category, provider, displayName, ConnectorStatus.ACTIVE, settings, webhookSecret, now, now);
    }

    public void update(String displayName, ConnectorStatus status, Map<String, String> settings, String webhookSecret) {
        this.displayName = requireText(displayName, "display name is required");
        this.status = Objects.requireNonNull(status, "connector status is required");
        this.settings = Map.copyOf(settings == null ? Map.of() : settings);
        this.webhookSecret = requireText(webhookSecret, "webhook secret is required");
        this.updatedAt = Instant.now();
    }

    public boolean active() {
        return status == ConnectorStatus.ACTIVE;
    }

    public UUID id() {
        return id;
    }

    public String tenantId() {
        return tenantId;
    }

    public ConnectorCategory category() {
        return category;
    }

    public ConnectorProvider provider() {
        return provider;
    }

    public String displayName() {
        return displayName;
    }

    public ConnectorStatus status() {
        return status;
    }

    public Map<String, String> settings() {
        return settings;
    }

    public String webhookSecret() {
        return webhookSecret;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

    private static void ensureProviderMatchesCategory(ConnectorCategory category, ConnectorProvider provider) {
        boolean matches = switch (category) {
            case IDENTITY_PROVIDER -> provider == ConnectorProvider.OKTA
                    || provider == ConnectorProvider.AZURE_AD
                    || provider == ConnectorProvider.GOOGLE_WORKSPACE;
            case GIT_PROVIDER -> provider == ConnectorProvider.GITHUB
                    || provider == ConnectorProvider.GITLAB
                    || provider == ConnectorProvider.BITBUCKET;
            case NOTIFICATION_PROVIDER -> provider == ConnectorProvider.SLACK
                    || provider == ConnectorProvider.MICROSOFT_TEAMS
                    || provider == ConnectorProvider.SMTP;
        };
        if (!matches) {
            throw new IllegalArgumentException("connector provider does not match category");
        }
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}

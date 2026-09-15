package com.stackwork360.developerintelligenceservice.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class CodeRepository {
    private final UUID id;
    private final String tenantId;
    private final RepositoryProvider provider;
    private final String externalId;
    private String name;
    private String defaultBranch;
    private RepositoryLifecycle lifecycle;
    private ServiceOwnership ownership;
    private final List<RepositoryContribution> contributions;
    private final Instant createdAt;
    private Instant updatedAt;

    private CodeRepository(
            UUID id,
            String tenantId,
            RepositoryProvider provider,
            String externalId,
            String name,
            String defaultBranch,
            RepositoryLifecycle lifecycle,
            ServiceOwnership ownership,
            List<RepositoryContribution> contributions,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = Objects.requireNonNull(id, "repository id is required");
        this.tenantId = requireText(tenantId, "tenant id is required");
        this.provider = Objects.requireNonNull(provider, "provider is required");
        this.externalId = requireText(externalId, "external repository id is required");
        this.name = requireText(name, "repository name is required");
        this.defaultBranch = requireText(defaultBranch, "default branch is required");
        this.lifecycle = Objects.requireNonNull(lifecycle, "repository lifecycle is required");
        this.ownership = Objects.requireNonNull(ownership, "ownership is required");
        this.contributions = new ArrayList<>(contributions == null ? List.of() : contributions);
        this.createdAt = Objects.requireNonNull(createdAt, "created at is required");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updated at is required");
    }

    public static CodeRepository register(
            String tenantId,
            RepositoryProvider provider,
            String externalId,
            String name,
            String defaultBranch,
            ServiceOwnership ownership
    ) {
        Instant now = Instant.now();
        return new CodeRepository(
                UUID.randomUUID(),
                tenantId,
                provider,
                externalId,
                name,
                defaultBranch,
                RepositoryLifecycle.ACTIVE,
                ownership,
                List.of(),
                now,
                now
        );
    }

    public void updateDetails(String name, String defaultBranch, RepositoryLifecycle lifecycle, ServiceOwnership ownership) {
        this.name = requireText(name, "repository name is required");
        this.defaultBranch = requireText(defaultBranch, "default branch is required");
        this.lifecycle = Objects.requireNonNull(lifecycle, "repository lifecycle is required");
        this.ownership = Objects.requireNonNull(ownership, "ownership is required");
        this.updatedAt = Instant.now();
    }

    public void recordContribution(String developerId, int commitCount, Instant lastContributionAt) {
        RepositoryContribution contribution = new RepositoryContribution(developerId, commitCount, lastContributionAt);
        contributions.removeIf(existing -> existing.developerId().equals(contribution.developerId()));
        contributions.add(contribution);
        updatedAt = Instant.now();
    }

    public int busFactor() {
        return ownership.primaryMaintainers().isEmpty() ? 0 : ownership.primaryMaintainers().size();
    }

    public boolean busFactorRisk() {
        return busFactor() < 2;
    }

    public UUID id() {
        return id;
    }

    public String tenantId() {
        return tenantId;
    }

    public RepositoryProvider provider() {
        return provider;
    }

    public String externalId() {
        return externalId;
    }

    public String name() {
        return name;
    }

    public String defaultBranch() {
        return defaultBranch;
    }

    public RepositoryLifecycle lifecycle() {
        return lifecycle;
    }

    public ServiceOwnership ownership() {
        return ownership;
    }

    public List<RepositoryContribution> contributions() {
        return contributions.stream()
                .sorted(Comparator.comparing(RepositoryContribution::commitCount).reversed())
                .toList();
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}

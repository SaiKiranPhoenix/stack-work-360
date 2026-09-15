package com.stackwork360.developerintelligenceservice.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class ServiceOwnership {
    private final String serviceName;
    private String owningTeam;
    private final List<String> primaryMaintainers;
    private final List<CodeArea> codeAreas;

    private ServiceOwnership(String serviceName, String owningTeam, List<String> primaryMaintainers, List<CodeArea> codeAreas) {
        this.serviceName = requireText(serviceName, "service name is required");
        this.owningTeam = requireText(owningTeam, "owning team is required");
        this.primaryMaintainers = new ArrayList<>();
        primaryMaintainers.forEach(this::addPrimaryMaintainer);
        this.codeAreas = new ArrayList<>();
        codeAreas.forEach(this::addCodeArea);
    }

    public static ServiceOwnership create(String serviceName, String owningTeam, List<String> primaryMaintainers, List<CodeArea> codeAreas) {
        return new ServiceOwnership(
                serviceName,
                owningTeam,
                primaryMaintainers == null ? List.of() : primaryMaintainers,
                codeAreas == null ? List.of() : codeAreas
        );
    }

    public void update(String owningTeam, List<String> primaryMaintainers, List<CodeArea> codeAreas) {
        this.owningTeam = requireText(owningTeam, "owning team is required");
        this.primaryMaintainers.clear();
        (primaryMaintainers == null ? List.<String>of() : primaryMaintainers).forEach(this::addPrimaryMaintainer);
        this.codeAreas.clear();
        (codeAreas == null ? List.<CodeArea>of() : codeAreas).forEach(this::addCodeArea);
    }

    public void addPrimaryMaintainer(String developerId) {
        String normalized = requireText(developerId, "developer id is required");
        if (!primaryMaintainers.contains(normalized)) {
            primaryMaintainers.add(normalized);
        }
    }

    public void addCodeArea(CodeArea codeArea) {
        CodeArea required = Objects.requireNonNull(codeArea, "code area is required");
        if (!codeAreas.contains(required)) {
            codeAreas.add(required);
        }
    }

    public String serviceName() {
        return serviceName;
    }

    public String owningTeam() {
        return owningTeam;
    }

    public List<String> primaryMaintainers() {
        return List.copyOf(primaryMaintainers);
    }

    public List<CodeArea> codeAreas() {
        return List.copyOf(codeAreas);
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}

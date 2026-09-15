package com.stackwork360.tenantservice.domain;

import java.util.Set;

public record TenantEntitlements(
        TenantPlan plan,
        Set<String> entitledFeatures,
        Set<String> assignedFeatures
) {
    public boolean entitledTo(String feature) {
        return entitledFeatures.contains(feature);
    }
}

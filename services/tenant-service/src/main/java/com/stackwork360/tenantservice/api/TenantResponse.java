package com.stackwork360.tenantservice.api;

import com.stackwork360.tenantservice.domain.DataResidencyRegion;
import com.stackwork360.tenantservice.domain.Tenant;
import com.stackwork360.tenantservice.domain.TenantPlan;
import com.stackwork360.tenantservice.domain.TenantStatus;
import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public record TenantResponse(
        UUID id,
        String slug,
        String displayName,
        TenantStatus status,
        TenantPlan plan,
        DataResidencyRegion dataResidencyRegion,
        int retentionDays,
        Set<String> enabledFeatures,
        Map<String, String> configuration,
        Instant createdAt,
        Instant updatedAt
) {
    public static TenantResponse from(Tenant tenant) {
        return new TenantResponse(
                tenant.id(),
                tenant.slug(),
                tenant.displayName(),
                tenant.status(),
                tenant.plan(),
                tenant.dataResidencyRegion(),
                tenant.retentionDays(),
                tenant.enabledFeatures(),
                tenant.configuration(),
                tenant.createdAt(),
                tenant.updatedAt()
        );
    }
}

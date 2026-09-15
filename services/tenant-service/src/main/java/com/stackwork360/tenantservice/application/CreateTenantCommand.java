package com.stackwork360.tenantservice.application;

import com.stackwork360.tenantservice.domain.DataResidencyRegion;
import com.stackwork360.tenantservice.domain.TenantPlan;
import java.util.Map;
import java.util.Set;

public record CreateTenantCommand(
        String slug,
        String displayName,
        TenantPlan plan,
        DataResidencyRegion dataResidencyRegion,
        int retentionDays,
        Set<String> enabledFeatures,
        Map<String, String> configuration
) {
}

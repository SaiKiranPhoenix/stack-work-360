package com.stackwork360.tenantservice.api;

import com.stackwork360.tenantservice.domain.DataResidencyRegion;
import com.stackwork360.tenantservice.domain.TenantPlan;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import java.util.Set;

public record UpdateTenantRequest(
        @NotBlank
        String displayName,

        @NotNull
        TenantPlan plan,

        @NotNull
        DataResidencyRegion dataResidencyRegion,

        @Min(30)
        @Max(3650)
        int retentionDays,

        Set<String> enabledFeatures,

        Map<String, String> configuration
) {
}

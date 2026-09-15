package com.stackwork360.tenantservice.api;

import java.util.Set;

public record AssignTenantFeaturesRequest(
        Set<String> enabledFeatures
) {
}

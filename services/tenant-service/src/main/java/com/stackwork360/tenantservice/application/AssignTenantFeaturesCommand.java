package com.stackwork360.tenantservice.application;

import java.util.Set;

public record AssignTenantFeaturesCommand(
        Set<String> enabledFeatures
) {
}

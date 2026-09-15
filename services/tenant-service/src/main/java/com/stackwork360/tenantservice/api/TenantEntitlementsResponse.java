package com.stackwork360.tenantservice.api;

import com.stackwork360.tenantservice.domain.TenantEntitlements;
import com.stackwork360.tenantservice.domain.TenantPlan;
import java.util.Set;

public record TenantEntitlementsResponse(
        TenantPlan plan,
        Set<String> entitledFeatures,
        Set<String> assignedFeatures
) {
    static TenantEntitlementsResponse from(TenantEntitlements entitlements) {
        return new TenantEntitlementsResponse(
                entitlements.plan(),
                entitlements.entitledFeatures(),
                entitlements.assignedFeatures()
        );
    }
}
